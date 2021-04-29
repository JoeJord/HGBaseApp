package at.hagru.hgbase.android.awt;

import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Wrapper class for storing a dimension (i.e., width and height).
 * 
 * @author hagru
 */
public class Dimension {
	
	public final int width;
	public final int height;
	
	public Dimension(int width, int height) {
		super();
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	@Override
	public boolean equals(Object o) {
	    return (HGBaseTools.equalClass(this, o) && getWidth() == ((Dimension) o).getWidth() 
	                                            && getHeight() == ((Dimension) o).getHeight());
	}
	
	@Override
	public String toString() {
		return getClass().getName() + "[width=" + width + ",height=" + height + "]";
	}
}
