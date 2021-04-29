package at.hagru.hgbase.lib.internal;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import at.hagru.hgbase.lib.HGBaseTools;
import at.hagru.hgbase.lib.xml.ChildNodeIterator;
import at.hagru.hgbase.lib.xml.HGBaseXMLTools;

/**
 * A map for saving int, boolean and String values for keys.
 *
 * @author hagru
 */
public class IntBooleanStringMap {

    private static final String INCLUDE_TAG = "include";
    private static final String FILE_ATTRIBUTE = "file";
    private Map<String,String> textOptions;

    public IntBooleanStringMap() {
        super();
        textOptions=new HashMap<>();
    }

    /**
     * Reads the configuration from the given file,
     * errors are ignored
     *
     * @return 0 if there was no error.
     */
    public int fromFile(final String file, final String rootName, final String nodeName, final String attrKey, final String attrValue) {
        textOptions.clear();
        Element root=HGBaseXMLTools.readXML(file);
        return fromDomElement(root, rootName, nodeName, attrKey, attrValue);
    }

    /**
     * Reads the configuration from the given stream,
     * errors are ignored
     *
     * @return 0 if there was no error.
     */
    public int fromFile(final InputStream stream, final String rootName, final String nodeName,
            final String attrKey, final String attrValue) {
        textOptions.clear();
        Element root=HGBaseXMLTools.readXML(stream);
        return fromDomElement(root, rootName, nodeName, attrKey, attrValue);
    }

    /**
     * @param root The root XML element.
     * @return 0 if there was no error.
     */
    private int fromDomElement(Element root, final String rootName, final String nodeName,
            final String attrKey, final String attrValue) {
        if (root != null) {
            readNodes(root, rootName, nodeName, attrKey, attrValue);
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * @param root The root element of the XML structure.
     * @param rootName The name of the root element.
     * @param nodeName The node name to test for values.
     * @param attrKey The attribute name of the key.
     * @param attrValue The attribute name of the value.
     */
    private void readNodes(Element root, final String rootName, final String nodeName,
                                         final String attrKey, final String attrValue) {
        ChildNodeIterator.run(new ChildNodeIterator(root, rootName, null) {
            @Override
            public void performNode(Node node, int index, Object obj) {
                String currNodeName = node.getNodeName();
                if (nodeName.equals(currNodeName)) {
                    // the node name indicates a value
                    String code = HGBaseXMLTools.getAttributeValue(node, attrKey);
                    String value = HGBaseXMLTools.getAttributeValue(node, attrValue);
                    if (code != null && code.length() > 0 && value != null && value.length() > 0) {
                        set(code, value);
                    }
                } else if (INCLUDE_TAG.equals(currNodeName)) {
                    // there is a file to include for reading values
                    String includeFile = HGBaseXMLTools.getAttributeValue(node, FILE_ATTRIBUTE);
                    if (HGBaseTools.hasContent(includeFile)) {
                        Element includeRoot = HGBaseXMLTools.readXML(includeFile);
                        if (includeRoot != null) {
                            readNodes(includeRoot, rootName, nodeName, attrKey, attrValue);
                        }
                    }
                }
            }
        });
    }

    /**
     * Writes the configuration to the given file,
     *
     * @param file file name for the configuration
     * @return 0 if no errors
     */
    public int toFile(final String file, final String rootName, final String nodeName,
                                         final String attrKey, final String attrValue) {
        try {
            Document newDoc = HGBaseXMLTools.createDocument();
            Element root = newDoc.createElement(rootName);
            newDoc.appendChild(root);
            String[] s = getKeys();
            if (s != null) {
                for (int i = 0; i < s.length; i++) {
                    Element elem = newDoc.createElement(nodeName);
                    elem.setAttribute(attrKey, s[i]);
                    elem.setAttribute(attrValue, get(s[i]));
                    root.appendChild(elem);
                }
            }
            if (HGBaseXMLTools.writeXML(newDoc, file)) {
                return 0;
            }
        } catch (DOMException e) {
            // NOCHECK: ignore execption, will return -1
        }
        return -1;
    }

    /**
     * @param key A key to test.
     * @return True, if this key exists.
     */
    public boolean existsKey(String key) {
        return textOptions.containsKey(key);
    }

    /**
     * Returns an array with the text options that are set
     *
     * @return array with keys of options
     */
    public String[] getKeys() {
        return textOptions.keySet().toArray(new String[textOptions.size()]);
    }

    /**
     * Returns the given text option
     *
     * @param key key of option
     * @param defaultValue default value if key does not exist.
     * @return option
     */
    public String get(String key, String defaultValue) {
        String value = textOptions.get(key);
        return (value==null)? defaultValue : value;
    }

    /**
     * Returns the given text option
     *
     * @param key key of option
     * @return option
     */
    public String get(String key) {
        return get(key, null);
    }

    /**
     * Returns the given integer option
     *
     * @param key key of option
     * @param defaultValue default value if key does not exist
     * @return option
     */
    public int getInt(String key, int defaultValue) {
    	String value = textOptions.get(key);
        return (value==null)? defaultValue : HGBaseTools.toInt(value);
    }

    /**
     * Returns the given integer option
     *
     * @param key key of option
     * @return option
     */
    public int getInt(String key) {
        return getInt(key, HGBaseTools.INVALID_INT);
    }

    /**
     * Returns the given boolean option
     *
     * @param key key of option
     * @param defaultValue default value if key does not exist
     * @return option as boolean
     */
    public boolean getBoolean(String key, boolean defaultValue) {
    	String value = textOptions.get(key);
        return (value==null)
               ? defaultValue
               : HGBaseTools.toBoolean(value);
    }

    /**
     * Returns the given boolean option
     *
     * @param key key of option
     * @return option as boolean
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Removes the given text option
     *
     * @param key key of option
     * @return true if this key existed and was deleted
     */
    public boolean remove(String key) {
        if (!textOptions.containsKey(key)) {
            return false;
        }
        textOptions.remove(key);
        return true;
    }

    /**
     * Sets the given text option
     *
     * @param key key of option
     * @param option text of the option
     */
    public void set(String key,String option) {
        textOptions.put(key,option);
    }

    /**
     * Sets the given integer option
     *
     * @param key key of option
     * @param option integer value of the option
     */
    public void set(String key,int option) {
        set(key, String.valueOf(option));
    }

    /**
     * Sets the given boolean option
     *
     * @param key key of option
     * @param option boolean value of the option
     */
    public void set(String key,boolean option) {
        int iOption=(option)? 1:0;
        set(key,iOption);
    }
}
