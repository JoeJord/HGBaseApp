package at.hagru.hgbase.lib.internal.billing;

import androidx.annotation.NonNull;

import com.android.billingclient.api.ProductDetails;

import java.util.List;

/**
 * Interface for application-specific purchase event handling.
 */
public interface HGBaseBillingListener {
    /**
     * Called when the products have been loaded.
     *
     * @param products The list of the loaded products.
     */
    void onProductsLoaded(@NonNull List<ProductDetails> products);

    /**
     * Called when a purchase is successfully completed and acknowledged.
     *
     * @param productId The ID of the purchased product.
     */
    void onPurchaseSuccess(@NonNull String productId);

    /**
     * Called when the billing client or purchase flow encounters an error.
     *
     * @param message A short description of the error.
     */
    void onBillingError(@NonNull String message);
}
