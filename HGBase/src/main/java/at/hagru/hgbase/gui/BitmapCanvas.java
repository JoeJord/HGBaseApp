package at.hagru.hgbase.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import at.hagru.hgbase.android.HGBaseAppTools;

/**
 * Uses a canvas (and an underlying bitmap) to directly draw on a bitmap. 
 * Allows you to retrieve a drawable object or the bitmap itself.
 * 
 * @author hagru
 */
public class BitmapCanvas extends Canvas {

    private Bitmap bitmap;

    /**
     * Create a new canvas with the given size.
     * 
     * @param width width of the image
     * @param height of the image
     */
    public BitmapCanvas(int width, int height) {
        this(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888));
    }

    /**
     * Create a new canvas with the given bitmap.
     * 
     * @param bitmap the bitmap to draw
     */
    public BitmapCanvas(Bitmap bitmap) {
        super(bitmap);
        this.bitmap = bitmap;
    }
    
    /**
     * Return the bitmap that is modified by this canvas.
     * 
     * @return the bitmap
     */
    public Bitmap getBitmap() {
        return bitmap;
    }
    
    /**
     * Returns a drawable object holding the image.
     * 
     * @return a drawable object
     */
    public Drawable getDrawable() {
        return new BitmapDrawable(HGBaseAppTools.getContext().getResources(), bitmap);
    }

}
