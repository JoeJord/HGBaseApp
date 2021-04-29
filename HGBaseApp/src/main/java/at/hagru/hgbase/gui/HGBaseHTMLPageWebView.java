package at.hagru.hgbase.gui;

import android.app.Activity;
import android.net.Uri;

import java.io.InputStream;
import java.net.URL;

import at.hagru.hgbase.lib.HGBaseFileTools;

/**
 * A basic html page using the web view.<p>
 * NOTE: As the android.webkit.WebView is not implemented in this library version, I use the HTML TextView implementation.
 * 
 * @author hagru
 */
public class HGBaseHTMLPageWebView extends HGBaseHTMLPageTextView /*WebView*/ {
	
	/**
	 * Creates an HTML page from an URL.
	 *  
	 * @param activity the activity that is connected with the widget
	 * @param url the URL to retrieve the page from
	 */
    public HGBaseHTMLPageWebView(Activity activity, URL url) {
    	this(activity, HGBaseFileTools.openUrlStream(url));
    }

	/**
	 * Creates an HTML page from an Uri.
	 *  
	 * @param activity the activity that is connected with the widget
	 * @param url the Uri to retrieve the page from
	 */
    public HGBaseHTMLPageWebView(Activity activity, Uri url) {
    	this(activity, HGBaseFileTools.openUriStream(url));
    }

	/**
	 * Creates an HTML page from an resource specified by the id.
	 *  
	 * @param activity the activity that is connected with the widget
	 * @param htmlResId the id of the HTML resource (raw resource)
	 */
    public HGBaseHTMLPageWebView(Activity activity, int htmlResId) {
    	this(activity, HGBaseFileTools.openRawResourceFileStream(htmlResId));
    }

	/**
	 * Creates an HTML page from an input stream.
	 *  
	 * @param activity the activity that is connected with the widget
	 * @param in the input stream
	 */
    public HGBaseHTMLPageWebView(Activity activity, InputStream in) {
    	super(activity, in);
    }    

    /**
     * Creates an HTML page from the given text.
     * 
	 * @param activity the activity that is connected with the widget
     * @param htmlText the HTML coded text
     */
    public HGBaseHTMLPageWebView(Activity activity, String htmlText) {
    	super(activity, htmlText);
    }
}
