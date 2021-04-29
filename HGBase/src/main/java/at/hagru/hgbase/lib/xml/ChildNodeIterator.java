package at.hagru.hgbase.lib.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Harald
 *
 * Goes through the nodes of a document structure.
 */
public abstract class ChildNodeIterator {
    
    private NodeList list;
    private int index;
    private Object userObject;
    
    /**
     * @param parent parent node.
     * @param userObject user object to be manipulated.
     */
    public ChildNodeIterator(Node parent, Object userObject) {
        this(parent, null, userObject);
    }

    /**
     * @param parent parent node.
     * @param name name that parent node should have.
     * @param userObject user object to be manipulated.
     */
    public ChildNodeIterator(Node parent, String name, Object userObject) {
        super();
        if (parent!=null && (name==null || parent.getNodeName().equals(name))) {
            this.list = parent.getChildNodes();
            this.index = 0;
            this.userObject = userObject;
        }
    }
    
    /**
     * Actions that shall be done with the child node.
     * 
     * @param node child node.
     * @param index index of this child.
     * @param obj user object to be manipulated.
     */
    public abstract void performNode(Node node, int index, Object obj);

    /**
     * @return the current node.
     */
    private Node getCurrentNode() {
        if (list!=null && list.getLength()>getCurrentPosition()) return list.item(index);
        return null;
    }
    
    /**
     * @return current position in the node list.
     */
    private int getCurrentPosition() {
        return index;
    }
    
    /**
     * Goes to the next list position.
     */
    private void next() {
        index++;
    }

    /**
     * @return the user's object.
     */
    private Object getUserObject() {
        return this.userObject;
    }
    
    
    /**
     * @param nodes runs a node iterator.
     */
    public static void run(ChildNodeIterator nodes) {
        Node node;
        while (((node = nodes.getCurrentNode()) != null) && (!nodes.isInterrupted())) {
            nodes.performNode(node, nodes.getCurrentPosition(), nodes.getUserObject());
            nodes.next();
        }
    }

    /**
     * Checks if the iterator has been interrupted.
     * 
     * @return <code>true</code> if the iterator has been interrupted or
     *         <code>false</code> otherwise.
     */
    protected boolean isInterrupted() {
        return false;
    }
}
