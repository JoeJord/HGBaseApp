package at.hagru.hgbase.android.awt;

import at.hagru.hgbase.lib.HGBaseTools;

/**
 * A facade to the android color that works like AWT colors.
 * 
 * @author hagru
 */
public class Color {
	
	public static final Color BLACK = new Color(android.graphics.Color.BLACK);
	public static final Color WHITE = new Color(android.graphics.Color.WHITE);
	public static final Color RED = new Color(android.graphics.Color.RED);
	public static final Color GREEN = new Color(android.graphics.Color.GREEN);
	public static final Color BLUE = new Color(android.graphics.Color.BLUE);
	public static final Color YELLOW = new Color(android.graphics.Color.YELLOW);
	public static final Color GRAY = new Color(android.graphics.Color.GRAY);
	public static final Color LIGHT_GRAY = new Color(android.graphics.Color.LTGRAY);
	public static final Color DARK_GRAY = new Color(android.graphics.Color.DKGRAY);
	public static final Color TRANSPARENT = new Color(android.graphics.Color.TRANSPARENT);

	private static final float BRIGHTER_FACTOR = 1.25f;
	private static final float DARKER_FACTOR = 0.25f;
	
	private final int colorCode;

	/**
	 * Create a new color.
	 * 
	 * @param colorCode the alpha, red, green and blue component.
	 */
	public Color(int colorCode) {
		this.colorCode = colorCode;
	}

	/**
	 * Create a new color.
	 * 
	 * @param red the red component [0..255]
	 * @param green the green component [0..255]
	 * @param blue the blue component [0..255]
	 */
	public Color(int red, int green, int blue) {
		this.colorCode = android.graphics.Color.rgb(red, green, blue);
	}

	/**
	 * Create a new color.
	 * 
	 * @param red the red component [0..255]
	 * @param green the green component [0..255]
	 * @param blue the blue component [0..255]
	 * @param alpha the alpha component [0..255], meaning 0 is fully transparent and 255 is opaque
	 */
	public Color(int red, int green, int blue, int alpha) {
		this.colorCode = android.graphics.Color.argb(alpha, red, green, blue);
	}
	
	/**
	 * Create a new color with float values.
	 * 
	 * @param r the red component
	 * @param g the green component
	 * @param b the blue component
	 * @param a the alpha component
	 */
	public Color(float r, float g, float b, float a) {
		this((int)(r*255+0.5), (int)(g*255+0.5), (int)(b*255+0.5), (int)(a*255+0.5));
	}

	/**
	 * Returns the android color code.
	 * 
	 * @return the android color code
	 */
	public int getColorCode() {
		return colorCode;
	}
	
	/**
	 * @return a brighter color then the current one
	 */
	public Color brighter() {
		float r = android.graphics.Color.red(colorCode) * BRIGHTER_FACTOR;
		float g = android.graphics.Color.green(colorCode) * BRIGHTER_FACTOR;
		float b = android.graphics.Color.blue(colorCode) * BRIGHTER_FACTOR;
		float a = android.graphics.Color.alpha(colorCode);
		return new Color(Math.min(r, 1.0f), Math.min(g, 1.0f), Math.min(b, 1.0f), a);
	}
	
	/**
	 * @return a darker color then the current one
	 */
	public Color darker() {
		float r = android.graphics.Color.red(colorCode) * DARKER_FACTOR;
		float g = android.graphics.Color.green(colorCode) * DARKER_FACTOR;
		float b = android.graphics.Color.blue(colorCode) * DARKER_FACTOR;
		float a = android.graphics.Color.alpha(colorCode);
		return new Color(r, g, b, a);
	}
	
	@Override
	public boolean equals(Object o) {
	    return (HGBaseTools.equalClass(this, o) && getColorCode() == ((Color) o).getColorCode());
	}
	
	@Override
	public String toString() {
		return String.valueOf(getColorCode());
	}

}
