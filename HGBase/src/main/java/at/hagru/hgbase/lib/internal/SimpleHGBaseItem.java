package at.hagru.hgbase.lib.internal;

import at.hagru.hgbase.lib.HGBaseItem;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;


/**
 * Simple base class to implement a HGBaseItem. 
 * 
 * @author hagru
 */
public class SimpleHGBaseItem implements HGBaseItem {
    
    private String id;

    public SimpleHGBaseItem(String id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see hgb.gui.HGBaseItem#getId()
     */
    @Override
    public String getId() {
        return id;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return HGBaseTools.equalClass(this, obj) 
                && HGBaseTools.equalObject(getId(), ((SimpleHGBaseItem)obj).getId());
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HGBaseTools.hashCode(getId());
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return HGBaseText.getText(getId());
    }

}