package at.hagru.hgbase.android;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseTools;
import at.hagru.hgbase.lib.xml.ChildNodeIterator;
import at.hagru.hgbase.lib.xml.HGBaseXMLTools;

/**
 * Reads links from the {@code /res/raw/links.xml} file to manage default links of the application that are displayed in the about dialog.
 * <p>
 * {@code <links><link name="Donate" url="http://donate.com" icon="donateIcon" version="FREE|PRO|BOTH"/>...</links> }
 * 
 * @author hagru
 */
public class LinkManager {

    private static final String LINKS_FILE = "links";
    private static final String XML_LINKS = "links";
    private static final String XML_LINK = "link";
    private static final String XML_NAME = "name";
    private static final String XML_URL = "url";
    private static final String XML_ICON = "icon";
    private static final String XML_APPTYPE = "apptype";

    private static final LinkManager INSTANCE = new LinkManager();

    private final Map<String, LinkData> linkMap = new LinkedHashMap<>();

    /**
     * Creates a new link manager that reads the links.xml file.
     * <p>
     * NOTE: To be called by the HGBase activity!
     */
    private LinkManager() {
	readLinksXmlFile();
    }

    /**
     * @return the single instance of the link manager
     */
    public static LinkManager getInstance() {
	return INSTANCE;
    }

    /**
     * Returns a list with link names whereas every name is unique.
     * 
     * @return an unmodifiable collection with link names
     */
    public Collection<String> getLinkNames() {
	return Collections.unmodifiableCollection(linkMap.keySet());
    }

    /**
     * Returns the URL for the link.
     *
     * @param linkName the name of the link
     * @return the URL for the link or an empty string if the link name is invalid
     */
    public String getLinkUrl(String linkName) {
	LinkData ld = linkMap.get(linkName);
	return (ld == null) ? "" : ld.getUrl();
    }

    /**
     * Returns the name of the icon for the link.
     * 
     * @param linkName the name of the link
     * @return the name of the icon for the link or an empty string if the link name is invalid or no icon was defined
     */
    public String getLinkIcon(String linkName) {
	LinkData ld = linkMap.get(linkName);
	return (ld == null) ? "" : ld.getIcon();
    }

    /**
     * Returns the flag in which type the link should be shown. If {@code null}, then there is no restriction.
     * 
     * @param linkName the name of the link
     * @return the flag in which type the link should be shown. If {@code null}, then there is no restriction.
     */
    public APPTYPE getLinkAppType(String linkName) {
	LinkData ld = linkMap.get(linkName);
	return (ld == null) ? null : ld.getAppType();
    }

    @Override
    public String toString() {
	return "LinkManger contains " + linkMap.size() + " links: " + linkMap;
    }

    /**
     * Read the link data from the links.xml file and init fields.
     */
    private void readLinksXmlFile() {
	InputStream in = HGBaseFileTools.openRawResourceFileStream(LINKS_FILE);
	Element root = HGBaseXMLTools.readXML(in);
	if (root != null) {
	    ChildNodeIterator.run(new ChildNodeIterator(root, XML_LINKS) {

		@Override
		public void performNode(Node node, int index, Object obj) {
		    if (XML_LINK.equals(node.getNodeName())) {
			String name = HGBaseXMLTools.getAttributeValue(node, XML_NAME);
			String url = HGBaseXMLTools.getAttributeValue(node, XML_URL);
			String icon = HGBaseXMLTools.getAttributeValue(node, XML_ICON);
			String apptype = HGBaseXMLTools.getAttributeValue(node, XML_APPTYPE);
			if (HGBaseTools.hasContent(name) && HGBaseTools.hasContent(url)) {
			    linkMap.put(name, new LinkData(url, icon, APPTYPE.valueOfIgnoreCase(apptype)));
			}
		    }
		}
	    });
	}
    }

    /**
     * Returns {@code true} if the link should be shown in the current application version (pro or free).
     * 
     * @param isProVersion Flag, if the application is currently the pro version.
     * @param linkAppType The flag in which application version the link should be shown. If {@code null}, then there is no restriction.
     * @return {@code true} if the link should be shown in the current application version (pro or free).
     */
    public static boolean showLink(boolean isProVersion, APPTYPE linkAppType) {
	return APPTYPE.functionAvailable(isProVersion, linkAppType);
    }

    /**
     * Stores URL and name of the icon for a link.
     */
    private class LinkData {
	private String url;
	private String iconName;
	private APPTYPE apptype;

	/**
	 * @param url the URL for the link
	 * @param iconName the name of the icon
	 * @param apptype the flag in which type the link should be shown. If {@code null}, then there is no restriction.
	 */
	public LinkData(String url, String iconName, APPTYPE apptype) {
	    this.url = url;
	    this.iconName = iconName;
	    this.apptype = apptype;
	}

	/**
	 * @return the URL for the link
	 */
	public String getUrl() {
	    return url;
	}

	/**
	 * @return the name of the icon for the link
	 */
	public String getIcon() {
	    return iconName;
	}

	/**
	 * @return the flag in which type the link should be shown. If {@code null}, then there is no restriction.
	 */
	public APPTYPE getAppType() {
	    return apptype;
	}

	@Override
	public String toString() {
	    return "LinkData: url=" + url + ", icon=" + iconName + ", appType=" + apptype;
	}
    }

}
