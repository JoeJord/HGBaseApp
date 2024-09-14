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
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
import at.hagru.hgbase.android.awt.Color;
import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseTools;
import at.hagru.hgbase.lib.Pair;
import at.hagru.hgbase.lib.TriConsumer;

/**
 * Contains xml tools
 *
 * @author hagru
 */
public final class HGBaseXMLTools {
    /**
     * The XML key for the map entry node.
     */
    private static final String MAP_ENTRY_NODE_KEY = "mapentry";
    /**
     * The XML key for the key node.
     */
    private static final String KEY_NODE_KEY = "key";
    /**
     * The XML key for the value node.
     */
    private static final String VALUE_NODE_KEY = "value";

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
     * @param file         file object of the xml-file
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
        return (resId != 0) ? HGBaseAppTools.getContext().getResources().getXml(resId) : null;
    }

    /**
     * Writes a given document to a xml file on the <b>internal</b> file system.
     *
     * @param doc  document structure
     * @param file path of the xml-file
     * @return true, if writing was successful
     */
    public static boolean writeXML(Document doc, String file) {
        return writeXML(doc, HGBaseFileTools.getFileForIntern(file));
    }

    /**
     * Writes a given document to a xml file.
     *
     * @param doc  document structure
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
     * @param node      node to look up.
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
     * @param node      node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as int.
     */
    public static int getAttributeIntValue(Node node, String attribute) {
        return HGBaseTools.toInt(getAttributeValue(node, attribute));
    }

    /**
     * Returns the given attribute of the node as int.
     *
     * @param node         node to look up.
     * @param attribute    attribute to look for.
     * @param defaultValue default value if the attribute value is invalid.
     * @return attribute value as int or {@code defaultValue} if the attribute value is invalid.
     */
    public static int getAttributeIntValue(Node node, String attribute, int defaultValue) {
        return HGBaseTools.toInt(getAttributeValue(node, attribute), defaultValue);
    }

    /**
     * Returns the given attribute of the node as long.
     *
     * @param node      node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as long.
     */
    public static long getAttributeLongValue(Node node, String attribute) {
        return HGBaseTools.toLong(getAttributeValue(node, attribute));
    }

    /**
     * Returns the given attribute of the node as long.
     *
     * @param node         node to look up.
     * @param attribute    attribute to look for.
     * @param defaultValue default value if the attribute value is invalid.
     * @return attribute value as long or {@code defaultValue} if the attribute value is invalid.
     */
    public static long getAttributeLongValue(Node node, String attribute, long defaultValue) {
        return HGBaseTools.toLong(getAttributeValue(node, attribute), defaultValue);
    }

    /**
     * Return the given attribute of the node as boolean.
     *
     * @param node      node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as boolean.
     */
    public static boolean getAttributeBooleanValue(Node node, String attribute) {
        return HGBaseTools.toBoolean(getAttributeValue(node, attribute));
    }

    /**
     * Returns the given attribute of the node as boolean or {@code defaultValue} if the attribute value was not specified.
     *
     * @param node         The node to look up.
     * @param attribute    The attribute to look for.
     * @param defaultValue The value if the value was not specified.
     * @return The given attribute of the node as boolean or {@code defaultValue} if the attribute value was not specified.
     */
    public static boolean getAttributeBooleanValue(Node node, String attribute, boolean defaultValue) {
        String value = getAttributeValue(node, attribute);
        return HGBaseTools.hasContent(value) ? HGBaseTools.toBoolean(value) : defaultValue;
    }

    /**
     * Returns the given attribute of the node as color.<br>
     * The value of the value must be {@code RRGGBB} where {@code RR}, {@code GG} and {@code BB} are the hexadecimal values for red, green and blue.<br>
     * If the value is not a valid color, then {@code null} will be returned.
     *
     * @param node      node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as {@code Color} or {@code null}.
     */
    public static Color getAttributeRGBColorValue(Node node, String attribute) {
        String value = getAttributeValue(node, attribute);
        try {
            int r = Integer.parseInt(value.substring(0, 2), 16);
            int g = Integer.parseInt(value.substring(2, 4), 16);
            int b = Integer.parseInt(value.substring(4, 6), 16);
            return new Color(r, g, b);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Returns the given attribute of the node as color.<br>
     * The value of the value must be {@code RRGGBB} where {@code RR}, {@code GG} and {@code BB} are the hexadecimal values for red, green and blue.<br>
     * If the value is not a valid color, then {@code defaultValue} will be returned.
     *
     * @param node      node to look up.
     * @param attribute attribute to look for.
     * @return attribute value as {@code Color} or {@code defaultValue}.
     */
    public static Color getAttributeRGBColorValue(Node node, String attribute, Color defaultValue) {
        Color color = getAttributeRGBColorValue(node, attribute);
        if (color == null) {
            color = defaultValue;
        }
        return color;
    }

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
     * @param node  the element to set the value
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
    public static String transformDocument(Document newDoc) throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
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
     * @param doc    the xml document
     * @param parent the parent node, may be null to add new node as root node of the xml document
     * @param name   the name of the new element
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

    /**
     * Writes the specified collection to the specified XML document.
     *
     * @param doc The XML document.
     * @param parent The parent node.
     * @param collectionName The name of the node for the collection.
     * @param elementName The name of the nodes for the collection elements.
     * @param collection The collection to write.
     * @param elementWriter The writer to write one element to the XML document.
     * @param <T> The type of the collection elements.
     */
    public static <T> void writeCollection(Document doc, Element parent, String collectionName, String elementName, Collection<T> collection, BiConsumer<Element, T> elementWriter) {
        if (!HGBaseTools.hasContent(collectionName)) {
            throw new IllegalArgumentException("The specified collection name must not be empty!");
        }
        Element collectionNode = createElement(doc, parent, collectionName);
        if ((collection == null) || (collection.isEmpty())) {
            return;
        }
        if (!HGBaseTools.hasContent(elementName)) {
            throw new IllegalArgumentException("The specified element name must not be empty!");
        }
        collection.forEach(element -> elementWriter.accept(createElement(doc, collectionNode, elementName), element));
    }

    /**
     * Reads a list of values from the specified node.
     *
     * @param parent The parent node of the collection.
     * @param collectionName The name of the node of the collection.
     * @param elementName The name of the nodes of the collection elements.
     * @param collectionSupplier The supplier for the collection where to store the values.
     * @param elementReader The reader to read one element from the element node.
     * @return The collection with the read values.
     * @param <T> The type of the collection elements.
     */
    public static <T> Collection<T> readCollection(Node parent, String collectionName, String elementName, Supplier<Collection<T>> collectionSupplier, Function<Node, T> elementReader) {
        if (!HGBaseTools.hasContent(collectionName)) {
            throw new IllegalArgumentException("The specified collection name must not be empty!");
        }
        if (!HGBaseTools.hasContent(elementName)) {
            throw new IllegalArgumentException("The specified element name must not be empty!");
        }
        Collection<T> collection = collectionSupplier.get();
        ChildNodeIterator.run(new ChildNodeIterator(parent, null, collection) {
            @Override
            public void performNode(Node node, int index, Object obj) {
                if (!collectionName.equals(node.getNodeName())) {
                    return;
                }
                ChildNodeIterator.run(new ChildNodeIterator(node, null, obj) {
                    @Override
                    public void performNode(Node node, int index, Object obj) {
                        if (!elementName.equals(node.getNodeName())) {
                            return;
                        }
                        collection.add(elementReader.apply(node));
                    }
                });
            }
        });
        return collection;
    }

    /**
     * Writes the specified map to the specified XML document.
     *
     * @param doc The XML document.
     * @param parent The parent node.
     * @param mapName The name of the node for the map.
     * @param map The map to write.
     * @param entryWriter The writer to write one map entry to the XML document.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     */
    public static <K, V> void writeMap(Document doc, Element parent, String mapName, Map<K, V> map, TriConsumer<Element, K, V> entryWriter) {
        Element mapNode = createElement(doc, parent, mapName);
        if ((map == null) || (map.isEmpty())) {
            return;
        }
        map.forEach((key, value) -> entryWriter.accept(mapNode, key, value));
    }

    /**
     * Writes the specified map to the specified XML document.
     *
     * @param doc The XML document.
     * @param parent The parent node.
     * @param mapName The name of the node for the map.
     * @param map The map to write.
     * @param keyWriter The writer to write the key of one map entry to the XML document.
     * @param valueWriter The writer to write the value on one map entry to the XML document.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     */
    public static <K, V> void writeMap(Document doc, Element parent, String mapName, Map<K, V> map, BiConsumer<Element, K> keyWriter, BiConsumer<Element, V> valueWriter) {
        writeMap(doc, parent, mapName, map, (mapNode, key, value) -> {
            Element entryNode = createElement(doc, mapNode, MAP_ENTRY_NODE_KEY);
            keyWriter.accept(createElement(doc, entryNode, KEY_NODE_KEY), key);
            valueWriter.accept(createElement(doc, entryNode, VALUE_NODE_KEY), value);
        });
    }

    /**
     * Reads mapped values from the specified node.
     *
     * @param parent The parent node of the map.
     * @param mapName The name of the node of the map.
     * @param mapSupplier The supplier for the map where to store the elements.
     * @param elementReader The reader to read one element from the element node.
     * @return The map with the read values.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     */
    public static <K, V> Map<K, V> readMap(Node parent, String mapName, Supplier<Map<K, V>> mapSupplier, Function<Node, Set<Pair<K, V>>> elementReader) {
        if (!HGBaseTools.hasContent(mapName)) {
            throw new IllegalArgumentException("The specified map name must not be empty!");
        }
        Map<K, V> map = mapSupplier.get();
        ChildNodeIterator.run(new ChildNodeIterator(parent, null, map) {
            @Override
            public void performNode(Node node, int index, Object obj) {
                if (!mapName.equals(node.getNodeName())) {
                    return;
                }
                elementReader.apply(node).forEach(entry -> map.put(entry.getFirst(), entry.getSecond()));
            }
        });
        return map;
    }

    /**
     * Reads mapped values from the specified node.
     *
     * @param parent The parent node of the map.
     * @param mapName The name of the node of the map.
     * @param mapSupplier The supplier for the map where to store the elements.
     * @param keyReader The reader to read the key.
     * @param valueReader The reader to read the value.
     * @return The map with the read values.
     * @param <K> The type of the map keys.
     * @param <V> The type of the map values.
     */
    public static <K, V> Map<K, V> readMap(Node parent, String mapName, Supplier<Map<K, V>> mapSupplier, Function<Node, K> keyReader, Function<Node, V> valueReader) {
        if (!HGBaseTools.hasContent(mapName)) {
            throw new IllegalArgumentException("The specified map name must not be empty!");
        }
        Map<K, V> map = mapSupplier.get();
        ChildNodeIterator.run(new ChildNodeIterator(parent, null, map) {
            @Override
            public void performNode(Node mapNode, int index, Object obj) {
                if (!mapName.equals(mapNode.getNodeName())) {
                    return;
                }
                ChildNodeIterator.run(new ChildNodeIterator(mapNode, null, obj) {
                    @Override
                    public void performNode(Node entryNode, int index, Object obj) {
                        if (!MAP_ENTRY_NODE_KEY.equals(entryNode.getNodeName())) {
                            return;
                        }
                        Pair<K, V> pair = new Pair<>();
                        ChildNodeIterator.run(new ChildNodeIterator(entryNode, null, obj) {
                            @Override
                            public void performNode(Node node, int index, Object obj) {
                                switch (node.getNodeName()) {
                                    case KEY_NODE_KEY:
                                        pair.setFirst(keyReader.apply(node));
                                        break;
                                    case VALUE_NODE_KEY:
                                        pair.setSecond(valueReader.apply(node));
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
                        map.put(pair.getFirst(), pair.getSecond());
                    }
                });
            }
        });
        return map;
    }
}
