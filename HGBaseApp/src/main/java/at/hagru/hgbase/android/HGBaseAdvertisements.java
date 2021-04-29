package at.hagru.hgbase.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.Locale;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.R;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Show my own advertisements as a view that covers most parts of the screen.
 * 
 * @author hagru
 */
public final class HGBaseAdvertisements {
    /**
     * The placeholder for the language in an URL.
     */
    private static final String LANGUAGE_URL_PLACEHOLDER = "[LANG]";
    private static View advertisementView;
    private static WebView advertisementWebView;
    private static int adViewWidth;
    private static int adViewHeight;

	/**
	 * Prevent instantiation.
	 */
	private HGBaseAdvertisements() {
		super();
	}

	/**
	 * @return the currently shown advertisement view, may be null
	 */
	public static View getAdvertisementView() {
		return advertisementView;
	}
	
	/**
	 * @return true if the advertisement is currently shown
	 */
	public static boolean isShowing() {
		return (advertisementView != null);
	}
	
    /**
     * Initializes the web advertisement view.
     *
     * @param mainFrame The main frame.
     */
    public static void initWebView(final HGBaseActivity mainFrame) {
	advertisementWebView = new WebView(mainFrame);
	final String advertisementErrorPageURL = mainFrame.getAdvertisementErrorPageURL();
	if (HGBaseTools.hasContent(advertisementErrorPageURL)) {
	    advertisementWebView.setWebViewClient(new WebViewClient() {
		@Override
		public void onReceivedError(WebView view, int errorCode, String description,
			String failingUrl) {
		    if (advertisementErrorPageURL.equals(failingUrl)) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		    } else {
			view.loadUrl(advertisementErrorPageURL);
		    }
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
		    // Load URLs with system browser.
		    mainFrame.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		    return true;
		}
	    });
	}
	ImageButton closeIcon = new ImageButton(mainFrame);
	closeIcon.setImageResource(R.drawable.close);
	closeIcon.setBackgroundColor(Color.TRANSPARENT);
	closeIcon.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		mainFrame.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
		mainFrame.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
	    }
	});
	advertisementWebView.addView(closeIcon);
    }

    /**
     * Adds a listener that hides the advertisement panel by clicking on the given view (which is typically this panel).
     * 
     * @param adView the view to click on
     */
    protected static void addClickOnCloseForAdvertisementPanel(final HGBaseActivity mainFrame, View adView) {
        adView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                hideAdvertisementDialog(mainFrame);
            }
        });
    }	
	
    /**
     * Creates the view that shows advertisements.
     * 
     * @param mainFrame the main frame
     * @param width the width of the advertisement view
     * @param height the height of the advertisement view
     * @return the new created view with advertisements
     */
    public static View createAdvertisementPanel(HGBaseActivity mainFrame, int width, int height) {
        Bitmap adImg = HGBaseGuiTools.loadImage("advertisement");
        String adUrl = mainFrame.getAdvertisementURL();
	return ((HGBaseTools.hasContent(adUrl) && (HGBaseTools.hasContent(mainFrame.getAdvertisementErrorPageURL()) || HGBaseAppTools.isInternetAvailable()))
		? createAdvertisementPanel(mainFrame, width, height, adUrl, true)
		: ((adImg == null) ? null : createAdvertisementPanel(mainFrame, width, height, adImg, true)));
    }    
    
    /**
     * Creates the view that shows an advertisement image.
     * 
     * @param mainFrame the main frame
     * @param width the width of the advertisement view
     * @param height the height of the advertisement view
     * @param adImg the image with the advertisement, must not be {@code null}
     * @param closeOnClick {@code true} if the panel shall be closed by clicking on it, {@code false} to not allow closing
     * @return the new created view with advertisements
     */
    public static View createAdvertisementPanel(HGBaseActivity mainFrame, int width, int height, Bitmap adImg, boolean closeOnClick) {
        ImageView adView = new ImageView(mainFrame);
        adView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        adView.setImageBitmap(adImg);
        if (closeOnClick) {
        	addClickOnCloseForAdvertisementPanel(mainFrame, adView);
        }
        adViewWidth = width;
        adViewHeight = height;
        return adView;
    }

    /**
     * Creates the view thats shows an advertisement URL.<br>
     * <b>Attention</b>: In {@code AndroidManifest.xml} the following line must be included: {@code <uses-permission android:name="android.permission.INTERNET" />}
     *
     * @param mainFrame the main frame
     * @param width the width of the advertisement view
     * @param height the height of the advertisement view
     * @param url the URL with the advertisement
     * @param closeOnClick {@code true} if the panel shall be closed by clicking on it, {@code false} to not allow closing
     * @return the new created view with advertisements
     */
    public static View createAdvertisementPanel(HGBaseActivity mainFrame, int width, int height,
	    final String url, boolean closeOnClick) {
	if (advertisementWebView != null) {
	    mainFrame.runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    advertisementWebView.loadUrl(parseUrlPlaceholder(url));
		}
	    });
	    if (closeOnClick) {
		addClickOnCloseForAdvertisementPanel(mainFrame, advertisementWebView);
	    } else if (advertisementWebView.hasOnClickListeners()) {
		advertisementWebView.setOnClickListener(null);
	    }
	}
	adViewWidth = width;
	adViewHeight = height;
	return advertisementWebView;
    }

    /**
     * Parses the specified URL and replaces placeholder with their corresponding value.
     * 
     * @param url The URL to parse.
     * @return The parsed URL.
     */
    private static String parseUrlPlaceholder(String url) {
	String language = "ENG";
	if (new Locale("de").getLanguage().equals(HGBaseAppTools.getLanguageCode())) {
	    language = "GER";
	}
	return url.replace(LANGUAGE_URL_PLACEHOLDER, language);
    }

    /**
     * Shows an advertisement with the default image.
     *  
     * @param mainFrame the main frame
     */
    public static void showAdvertisementDialog(HGBaseActivity mainFrame) {
        boolean fullscreen = (mainFrame.getFullscreenMode() != null);
        Point size = HGBaseGuiTools.getScreenSize(mainFrame, fullscreen);
        int width = (int) (size.x * mainFrame.getAdvertisementViewWidthPercent() / 100);
        int height = (int) (size.y * mainFrame.getAdvertisementViewHeightPercent() / 100);
        View adView = createAdvertisementPanel(mainFrame, width, height);
        showAdvertisementDialog(mainFrame, adView);
    }

    /**
     * Shows an advertisement with the given view.
     *  
     * @param mainFrame the main frame
     * @param adView the view with the advertisement, must be created by calling {@link #createAdvertisementPanel(...)}
     */
    public static void showAdvertisementDialog(HGBaseActivity mainFrame, View adView) {
        int width = adViewWidth;
        int height = adViewHeight;
        advertisementView = adView;
        boolean fullscreen = (mainFrame.getFullscreenMode() != null);
        final View frameView = HGBaseGuiTools.findFirstViewOfType(mainFrame.getRootView(), FrameLayout.class);
        if (frameView instanceof FrameLayout && advertisementView != null) {
            Point size = HGBaseGuiTools.getScreenSize(mainFrame, fullscreen);
            final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height);
            lp.leftMargin = (int) (size.x - width) / 2;
            lp.topMargin = (int) (size.y - height) / 2;
            mainFrame.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((FrameLayout) frameView).addView(advertisementView, lp);
                    advertisementView.bringToFront();
                }
            });
        }
    }      
    
    /**
     * Hides the advertisement if there is currently one displayed.
     * 
     * @param mainFrame the main frame
     */
    public static void hideAdvertisementDialog(HGBaseActivity mainFrame) {
        if (advertisementView != null) {
            mainFrame.runOnUiThread(new Runnable() {
                
                @Override
                public void run() {
                    HGBaseGuiTools.removeViewFromParent(advertisementView);
                    advertisementView = null;
                }
            });
        }
    }	

}
