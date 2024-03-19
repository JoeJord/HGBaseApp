package at.hagru.hgbase.gui;

import android.graphics.Paint;
import android.graphics.Rect;

import java.util.function.Supplier;

import at.hagru.hgbase.android.awt.Dimension;

/**
 * Supplier for the size of a specified string.
 */
public class StringSizeSupplier implements Supplier<Dimension> {
    /**
     * The text for which the font metrics should be returned.
     */
    private final String text;
    /**
     * The paint object which is used to determine the dimension.
     */
    private final Paint paint;

    /**
     * Constructs a new instance.
     *
     * @param text  The text for which the dimension should be determined.
     * @param paint The paint object which is used to determine the dimension.
     */
    public StringSizeSupplier(String text, Paint paint) {
        this.text = text;
        this.paint = paint;
    }

    @Override
    public Dimension get() {
        return getFontMetrics();
    }

    /**
     * Returns the dimension of the text with the specified font size.
     *
     * @param fontSize The size of the font with which the dimension should be determined.
     * @return The dimension of the text with the specified font size.
     */
    public Dimension get(float fontSize) {
        float currentSize = paint.getTextSize();
        paint.setTextSize(fontSize);
        Dimension metrics = getFontMetrics();
        paint.setTextSize(currentSize);
        return metrics;
    }

    /**
     * Returns the dimension of the text with the current font.
     *
     * @return The dimension of the text with the current font.
     */
    private Dimension getFontMetrics() {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return new Dimension(bounds.width(), bounds.height());
    }
}
