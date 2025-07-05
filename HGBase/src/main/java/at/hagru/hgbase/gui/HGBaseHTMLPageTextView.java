package at.hagru.hgbase.gui;

import android.app.Activity;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import androidx.appcompat.widget.AppCompatTextView;

import java.io.InputStream;
import java.net.URL;

import at.hagru.hgbase.android.view.DefaultImageGetter;
import at.hagru.hgbase.lib.HGBaseFileTools;

/**
 * A basic html page that can start the browser by clicking an URL and displays image resources.
 * 
 * @author hagru
 */
public class HGBaseHTMLPageTextView extends AppCompatTextView {
	
	private static final int DEFAULT_TEXT_SIZE = 14;
	
    /**
     * Creates an HTML page from the given text.
     * 
	 * @param activity the activity that is connected with the widget
     * @param htmlText the HTML coded text
     */
    public HGBaseHTMLPageTextView(Activity activity, String htmlText) {
    	super(activity);
        init(htmlText);
    }
    
    /**
     * Creates an HTML page from the given stream.
     * 
     * @param activity the activity
     * @param htmlStream the HTML coded stream
     */
    public HGBaseHTMLPageTextView(Activity activity, InputStream htmlStream) {
    	this(activity, HGBaseFileTools.getString(htmlStream));
    }

	/**
	 * Sets the text to the view and prepares everything for activating links.
	 * 
     * @param htmlText the HTML coded text
	 */
	private void init(String htmlText) {
        setText(Html.fromHtml(htmlText, new DefaultImageGetter(), null));
        setTextSize(DEFAULT_TEXT_SIZE);
        setAutoLinkMask(0);
        setMovementMethod(LinkMovementMethod.getInstance());
	}

	/**
	 * Set another page from the given URL.
	 * 
	 * @param url the URL to set for the new page
	 */
	public void setPage(URL url) {
		init(HGBaseFileTools.getString(HGBaseFileTools.openUrlStream(url)));
	}
	

	/**
	 * Set another page from the given Uri.
	 * 
	 * @param url the Uri to set for the new page
	 */
	public void setPage(Uri url) {
		init(HGBaseFileTools.getString(HGBaseFileTools.openUriStream(url)));
	}}
