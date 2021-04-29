package at.hagru.hgbase.gui;

import android.graphics.Color;
import android.view.Gravity;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.android.LinkManager;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Shows the about dialog.
 *
 * @author hagru
 */
public final class HGBaseAboutDlg {

    /**
     * Prevent instantiation.
     */
    private HGBaseAboutDlg() {
	super();
    }

    /**
     * Shows a default about dialog using the following resources:
     * <p>
     * <ul>
     * <li>Name (manifest file) or app_name: the name of the app</li>
     * <li>app_name_long (values/string): an optional long name for the app</li>
     * <li>Description (manifest file) or app_description: a description for the app</li>
     * <li>Version name (manifest file): the version of the app</li>
     * <li>ic_launcher (drawable): image to be displayed in the about dialog</li>
     * <li>infoCopyright(values/strings): the copyright information</li>
     * <li>infoUrl (values/strings): the URL to the web site</li>
     * <li>button_ok (values/strings): label for the OK button</li>
     * </ul>
     * 
     * @param activity the activity to get the graphical context
     */
    public static void show(HGBaseActivity activity) {
	String html = buildHtmlContent(activity);
	HGBaseHTMLPageTextView panel = new HGBaseHTMLPageTextView(activity, html);
	panel.setTextSize(14);
	panel.setGravity(Gravity.CENTER);
	panel.setBackgroundColor(Color.WHITE);
	HGBaseDialog.showOkDialog(activity, panel, HGBaseAppTools.getAppName());
    }

    /**
     * Build the HTML content from the standard values.
     * 
     * @param activity the activity to get the graphical context
     * @return the HTML content
     * @see #show()
     */
    protected static String buildHtmlContent(HGBaseActivity activity) {
	String application = HGBaseText.getTextOrDefault("app_name_long", HGBaseAppTools.getAppName());
	String infoVersion = HGBaseAppTools.getAppVersion();
	String aboutInfo = HGBaseAppTools.getAppDescription();
	String infoCopy = HGBaseText.getTextOrDefault("infoCopyright", "");
	String httpURL = HGBaseText.getTextOrDefault("infoUrl", "");
	StringBuilder htmlBody = new StringBuilder();
	htmlBody.append("<html><div>").append("<b>" + application + " " + infoVersion + "</b>")
		.append("<br>" + infoCopy + "<br>");
	if (HGBaseTools.hasContent(aboutInfo)) {
	    htmlBody.append("<br>" + aboutInfo + "<br>");
	}
	if (HGBaseTools.hasContent(httpURL)) {
	    htmlBody.append("<br><a href=\"" + httpURL + "\">" + httpURL + "</a><br>");
	}
	htmlBody.append(getHtmlForLinkImages(activity));
	htmlBody.append("</div></html>");
	return htmlBody.toString();
    }

    /**
     * Build the HTML content for default links defined for the app.
     * 
     * @param activity the activity to get the graphical context
     * @return the HTML content with default links or an empty string
     */
    public static String getHtmlForLinkImages(HGBaseActivity activity) {
	LinkManager linkMgr = LinkManager.getInstance();
	StringBuilder htmlLinks = new StringBuilder();
	htmlLinks.append("<br>");
	for (String linkName : linkMgr.getLinkNames()) {
	    if (LinkManager.showLink(activity.isProVersion(), linkMgr.getLinkAppType(linkName))) {
		String url = linkMgr.getLinkUrl(linkName);
		String icon = linkMgr.getLinkIcon(linkName);
		htmlLinks.append("<a href=\"" + url + "\">");
		if (HGBaseResources.getResourceIdByName(icon, HGBaseResources.DRAWABLE) != 0) {
		    htmlLinks.append("<img src=\"" + icon + "\">");
		} else {
		    htmlLinks.append(linkName);
		}
		htmlLinks.append("</a> ");
	    }
	}
	return htmlLinks.toString();
    }
}
