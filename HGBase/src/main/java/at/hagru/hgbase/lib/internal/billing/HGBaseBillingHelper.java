package at.hagru.hgbase.lib.internal.billing;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.PendingPurchasesParams;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * A reusable helper class for handling Google Play Billing.
 * <p>It handles initialization, purchase flow, product querying, and persistence of purchase state.</p>.
 */
public class HGBaseBillingHelper implements PurchasesUpdatedListener {
    /**
     * SharedPreferences key prefix to store purchase states locally.
     */
    private static final String PREFERENCE_NAME = "at.hagru.hgbase.lib.internal.billing";
    /**
     * The shared billing helper instance (singleton pattern).
     */
    private static HGBaseBillingHelper instance;
    /**
     * Cached product details fetched from Google Play.
     */
    private final Map<String, ProductDetails> productDetailsMap = new HashMap<>();
    /**
     * The billing client used to communicate with Google Play.
     */
    private BillingClient billingClient;
    /**
     * The application context.
     */
    private Context appContext;
    /**
     * A list of all available product IDs for the current application.
     */
    private List<String> productIds;
    /**
     * A listener interface for purchase updates.
     */
    private List<WeakReference<HGBaseBillingListener>> listeners;

    /**
     * Private constructor for singleton.
     */
    private HGBaseBillingHelper() {
        // Nothing to do.
    }

    /**
     * Returns the shared instance of the billing helper.
     * <p>Must be initialized first with {@link #init(Context, List)}.</p>
     *
     * @return The shared instance of the billing helper.
     */
    public static synchronized HGBaseBillingHelper getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BillingHelper not initialized. Call init() first.");
        }
        return instance;
    }

    /**
     * Initializes the billing helper singleton.
     *
     * @param context    The application context.
     * @param productIds The list of product IDs for this application.
     */
    public static synchronized void init(@NonNull Context context, @NonNull List<String> productIds) {
        if (instance != null) {
            return;
        }
        instance = new HGBaseBillingHelper();
        instance.appContext = context.getApplicationContext();
        instance.productIds = productIds;
        instance.listeners = new CopyOnWriteArrayList<>();
        instance.initBillingClient();
    }

    /**
     * Adds the specified billing listener.
     *
     * @param listener The listener to add.
     */
    public void addListener(HGBaseBillingListener listener) {
        if (listener == null) {
            return;
        }
        if (!existsListener(listener)) {
            listeners.add(new WeakReference<>(listener));
        }
    }

    /**
     * Removes the specified billing listener.
     *
     * @param listener The listener to remove.
     */
    public void removeListener(HGBaseBillingListener listener) {
        if (listener == null) {
            return;
        }
        Iterator<WeakReference<HGBaseBillingListener>> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            HGBaseBillingListener l = iterator.next().get();
            if (l == null || l == listener) {
                iterator.remove();
            }
        }
    }

    /**
     * Removes all billing listeners.
     */
    public void removeAllListener() {
        listeners.clear();
    }

    /**
     * Removes all dead listeners.
     */
    private void cleanupListeners() {
        listeners.removeIf(ref -> ref.get() == null);
    }

    /**
     * Returns {@code true} if the specified listeners already exists.
     *
     * @param listener The listener to check.
     * @return {@code true} if the specified listeners already exists.
     */
    private boolean existsListener(HGBaseBillingListener listener) {
        return listeners.stream().map(WeakReference::get).anyMatch(l -> l == listener);
    }

    /**
     * Initializes the billing client and queries products.
     */
    private void initBillingClient() {
        PendingPurchasesParams pendingPurchasesParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build();
        billingClient = BillingClient.newBuilder(appContext).setListener(this).enablePendingPurchases(pendingPurchasesParams).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
                onBillingError("Billing service disconnected.");
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    queryProductDetails();
                    restorePurchases();
                } else {
                    onBillingError("Billing setup failed: " + billingResult.getDebugMessage());
                }
            }
        });
    }

    /**
     * Returns the products for the specified product IDs.
     *
     * @param productIds The product IDs.
     * @return The products for the specified product IDs.
     */
    private List<QueryProductDetailsParams.Product> getProducts(List<String> productIds) {
        return productIds.stream().map(id -> QueryProductDetailsParams.Product.newBuilder().setProductId(id).setProductType(BillingClient.ProductType.INAPP).build()).collect(Collectors.toList());
    }

    /**
     * Queries Google Play for product details.
     */
    private void queryProductDetails() {
        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder().setProductList(getProducts(productIds)).build();

        billingClient.queryProductDetailsAsync(params, (billingResult, response) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                List<ProductDetails> detailsList = response.getProductDetailsList();
                detailsList.forEach(details -> productDetailsMap.put(details.getProductId(), details));
                onProductsLoaded(detailsList);
            } else {
                onBillingError("Product query failed: " + billingResult.getDebugMessage());
            }
        });
    }

    /**
     * Launches the purchase flow for the given product.
     *
     * @param activity  The activity used to launch the billing UI.
     * @param productId The ID of the product to purchase.
     */
    public void launchPurchase(@NonNull Activity activity, @NonNull String productId) {
        ProductDetails product = productDetailsMap.get(productId);
        if (product == null) {
            onBillingError("Product details not available: " + productId);
            return;
        }

        BillingFlowParams flowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(Collections.singletonList(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(product).build())).build();

        billingClient.launchBillingFlow(activity, flowParams);
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult result, @Nullable List<Purchase> purchases) {
        if ((result.getResponseCode() == BillingClient.BillingResponseCode.OK) && (purchases != null)) {
            purchases.forEach(this::handlePurchase);
        } else if (result.getResponseCode() != BillingClient.BillingResponseCode.USER_CANCELED) {
            onBillingError("Purchase failed: " + result.getDebugMessage());
        }
    }

    /**
     * Handles a single purchase: saves and acknowledges it.
     *
     * @param purchase The purchase to handle.
     */
    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED) {
            return;
        }

        purchase.getProducts().forEach(this::savePurchase);

        if (purchase.isAcknowledged()) {
            purchase.getProducts().forEach(this::onPurchaseSuccess);
        } else {
            AcknowledgePurchaseParams acknowledgeParams = AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();
            billingClient.acknowledgePurchase(acknowledgeParams, ackResult -> {
                if (ackResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    purchase.getProducts().forEach(this::onPurchaseSuccess);
                } else {
                    onBillingError("Acknowledge failed: " + ackResult.getDebugMessage());
                }
            });
        }
    }

    /**
     * Returns the editor of the preference where the purchases are stored locally.
     *
     * @return The editor of the preference where the purchases are stored locally.
     */
    private SharedPreferences.Editor getPreferenceEditor() {
        return appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
    }

    /**
     * Saves the purchase state locally using {@link SharedPreferences}.
     *
     * @param productId The ID of the purchased product.
     */
    private void savePurchase(@NonNull String productId) {
        getPreferenceEditor().putBoolean(productId, true).apply();
    }

    /**
     * Restores purchases from Google Play.
     */
    private void restorePurchases() {
        billingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(), (billingResult, purchasesList) -> {
            if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                onBillingError("Restore failed: " + billingResult.getDebugMessage());
                return;
            }
            syncPurchases(purchasesList);
            handleNewPurchased(purchasesList);

        });
    }

    /**
     * Synchronizes the the locally stored information about purchases with the store.
     *
     * @param purchasesList The list of the purchases.
     */
    private void syncPurchases(List<Purchase> purchasesList) {
        if (purchasesList == null) {
            return;
        }
        List<String> purchasedProductIds = getAllPurchasedProductIds(purchasesList);
        SharedPreferences.Editor editor = getPreferenceEditor();
        productIds.forEach(id -> editor.putBoolean(id, purchasedProductIds.contains(id)));
        editor.apply();
    }

    /**
     * Handles the new purchases.
     *
     * @param purchasesList The list of the purchases.
     */
    private void handleNewPurchased(List<Purchase> purchasesList) {
        if (purchasesList == null) {
            return;
        }
        purchasesList.forEach(this::handlePurchase);
    }

    /**
     * Returns the IDs of all purchased products.
     *
     * @param purchasesList The list of the purchases.
     * @return The IDs of all purchased products.
     */
    private List<String> getAllPurchasedProductIds(List<Purchase> purchasesList) {
        return purchasesList.stream().flatMap(p -> p.getProducts().stream()).collect(Collectors.toList());
    }

    /**
     * Checks if the given product has already been purchased.
     *
     * @param productId The ID of the product.
     * @return {@code true} if purchased, {@code false} otherwise.
     */
    public boolean isPurchased(@NonNull String productId) {
        return appContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(productId, false);
    }

    /**
     * Releases the billing client.
     */
    public void release() {
        if (billingClient != null && billingClient.isReady()) {
            billingClient.endConnection();
        }
    }

    /**
     * Called when the products have been loaded.
     *
     * @param detailsList The list of the loaded products.
     */
    private void onProductsLoaded(List<ProductDetails> detailsList) {
        cleanupListeners();
        listeners.stream().map(WeakReference::get).filter(Objects::nonNull).forEach(listener -> listener.onProductsLoaded(detailsList));
    }

    /**
     * Called when a purchase is successfully completed and acknowledged.
     *
     * @param productId The ID of the purchased product.
     */
    private void onPurchaseSuccess(String productId) {
        cleanupListeners();
        listeners.stream().map(WeakReference::get).filter(Objects::nonNull).forEach(listener -> listener.onPurchaseSuccess(productId));
    }

    /**
     * Called when the billing client or purchase flow encounters an error.
     *
     * @param message A short description of the error.
     */
    private void onBillingError(String message) {
        cleanupListeners();
        listeners.stream().map(WeakReference::get).filter(Objects::nonNull).forEach(listener -> listener.onBillingError(message));
    }
}
