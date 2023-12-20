package at.hagru.hgbase.lib.xml;

import android.content.res.XmlResourceParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Contains xml tools
 *
 * @author hagru
 */
public final class HGBaseXMLTools {

    private HGBaseXMLTools() {
        super();
    }

    /**
     * Reads the given xml file and returns the root element.
     *
     * @param file path of the xml-file
     * @return root element or null, if an error occurred
     */
    public static Element readXML(String file) {
        return readXML(HGBaseFileTools.openInternalFileStream(file));
    }
    
    /**
     * Reads the given xml file and returns the root element.
     *
     * @param file file object of the xml-file
     * @param errorMessage error message to be printed in case of an error
     * @return root element or null, if an error occurred
     */
    public static Element readXML(File file, String errorMessage) {
    	return readXML(HGBaseFileTools.openFileStream(file, errorMessage));
    }

    /**
     * Reads the given xml content from and input stream and returns the root element.
     *
     * @param in The input stream
     * @return The root element or null
     */
    public static Element readXML(InputStream in) {
        if (in != null) {
            try {
                DocumentBuilder build = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = build.parse(in);
                if (doc != null) {
                    return doc.getDocumentElement();
                }
            } catch (SAXException | IOException | ParserConfigurationException e) {
                HGBaseLog.logError("Error when reading xml file! " + e.getMessage());
            } finally {
                HGBaseFileTools.closeStream(in);
            }
        }
        return null;
    }
    
    /**
     * Returns the Android XML parser for parsing a XML file that is not placed in the assets directory.
     * 
     * @param resId the resource id
     * @return the Android XML parser
     */
    public static XmlResourceParser getXMLParser(int resId) {
    	return (resId != 0)? HGBaseAppTools.getContext().getResources().getXml(resId) : null;
    }
    
    /**
     * Writes a given document to a xml file on the <b>internal</b> file system.
     *
     * @param doc document structure
     * @param file path of the xml-file
     * @return true, if writing was successful
     */
    public static boolean writeXML(Document doc, String file) {
    	return writeXML(doc, HGBaseFileTools.getFileForIntern(file));
    }

    /**
     * Writes a given document to a xml file.
     *
     * @param doc document structure
     * @param file the file object of the xml-file
     * @return true, if writing was successful
     */
    public static boolean writeXML(Document doc, File file) {
        if (doc != null && file != null && prepareFileToWrite(file)) {
            try {
                String content = transformDocument(doc);
                FileOutputStream fout = new FileOutputStream(file);
                fout.write(content.getBytes(getEncoding()));
                fout.close();
                return true;
            } catch (IOException | TransformerFactoryConfigurationError | TransformerException e) {
            	HGBaseLog.logError("Could not write xml file! " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Creates directories if necessary.
     *
     * @param file The file to write.
     * @return true if preparation was successful
     */
    private static boolean prepareFileToWrite(File file) {
        File parentDir = file.getParentFile();
        if (!parentDir.isDirectory() && !file.getParentFile().mkdirs()) {
            HGBaseLog.logError("Could not create directory: " + parentDir);
        	return false;
        }
        return true;
    }

    /**
     * Returns the given attribute of the node.
     *
     * @param node node to look up.
     * @param attribute attribute to look for.
     * @return attribute value.
     */
    public static String getAttributeValue(Node node, String attribute) {
        if (node != null && node.getAttributes() != null) {
            Node name = node.getAttributes().getNamedItem(attribute);
            if (name != null) {
                return name.getNodeValue();
            }
        }
        return "";
    }

    /**
     * Returns the given attribute of the node as int.
     *
     * @param node node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as int.
     */
    public static int getAttributeIntValue(Node node, String attribute) {
        return HGBaseTools.toInt(getAttributeValue(node, attribute));
    }
    
    /**
     * Returns the given attribute of the node as int.
     *
     * @param node node to look up.
     * @param attribute attribute to look for.
     * @param defaultValue default value if the attribute value is invalid.
     * @return attribute value as int or {@code defaultValue} if the attribute value is invalid.
     */
    public static int getAttributeIntValue(Node node, String attribute, int defaultValue) {
        return HGBaseTools.toInt(getAttributeValue(node, attribute), defaultValue);
    }

    /**
     * Returns the given attribute of the node as long.
     *
     * @param node node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as long.
     */
    public static long getAttributeLongValue(Node node, String attribute) {
        return HGBaseTools.toLong(getAttributeValue(node, attribute));
    }
    
    /**
     * Returns the given attribute of the node as long.
     *
     * @param node node to look up.
     * @param attribute attribute to look for.
     * @param defaultValue default value if the attribute value is invalid.
     * @return attribute value as long or {@code defaultValue} if the attribute value is invalid.
     */
    public static long getAttributeLongValue(Node node, String attribute, long defaultValue) {
        return HGBaseTools.toLong(getAttributeValue(node, attribute), defaultValue);
    }
    
    /**
     * Return the given attribute of the node as boolean.
     * 
     * @param node node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as boolean.
     */
    public static boolean getAttributeBooleanValue(Node node, String attribute) {
        return HGBaseTools.toBoolean(getAttributeValue(node, attribute));
    }

    /**
     * Returns the given attribute of the node as boolean or {@code defaultValue} if the attribute value was not specified.
     *
     * @param node The node to look up.
     * @param attribute The attribute to look for.
     * @param defaultValue The value if the value was not specified.
     * @return The given attribute of the node as boolean or {@code defaultValue} if the attribute value was not specified.
     */
    public static boolean getAttributeBooleanValue(Node node, String attribute, boolean defaultValue) {
        String value = getAttributeValue(node, attribute);
        return HGBaseTools.hasContent(value) ? HGBaseTools.toBoolean(value) : defaultValue;
    }

    /**
     * Returns the text value of the node.
     *
     * @param node node to look up.
     * @return text value.
     */
    public static String getNodeValue(Node node) {
        Node text = node.getFirstChild();
        if (text != null) {
            return text.getNodeValue();
        }
        return "";
    }
    
    /**
     * Sets the given value as value of the node.
     * 
     * @param node the element to set the value
     * @param value the value to set
     */
    public static void setNodeValue(Element node, String value) {
        node.setTextContent(value);
    }

    /**
     * Transforms a document object to an xml string.
     *
     * @param newDoc an xml document.
     * @return an xml string.
     */
    public static String transformDocument(Document newDoc) throws TransformerConfigurationException,
            														TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        } catch (IllegalArgumentException ex) {
            // NOCHECK: there should be no exception
        }
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, getEncoding());
        transformer.transform(new DOMSource(newDoc), new StreamResult(output));
        try {
            return output.toString(getEncoding());
        } catch (UnsupportedEncodingException e) {
            HGBaseLog.logWarn("The character set '" + getEncoding() + "' is not supported!");
            return output.toString();
        }
    }

    /**
     * Creates a new xml document.
     *
     * @return a document object or null.
     */
    public static Document createDocument() {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (FactoryConfigurationError | ParserConfigurationException e) {
            // NOCHECK: ignore, will return null
        }
        return null;
    }
    
    /**
     * Creates a new element and appends it to the given parent. If not parent is given, the new node is added
     * to the document as root element.
     * 
     * @param doc the xml document
     * @param parent the parent node, may be null to add new node as root node of the xml document
     * @param name the name of the new element
     * @return the new created element
     */
    public static Element createElement(Document doc, Element parent, String name) {
        Element node = doc.createElement(name);
        if (parent == null) {
            doc.appendChild(node);
        } else {
            parent.appendChild(node);
        }
        return node;
    }

    /**
     * Just return the default encoding of Android.
     *
     * @return The xml encoding.
     */
    public static String getEncoding() {
        return HGBaseFileTools.getEncoding();
    }
}
