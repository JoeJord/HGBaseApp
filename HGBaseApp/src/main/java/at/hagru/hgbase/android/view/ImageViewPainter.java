package at.hagru.hgbase.android.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.widget.ImageView;

/**
 * Helper class to easily paint on an image view by overriding an
 * {@code onPaint} method.
 * 
 * @author hagru
 */
public abstract class ImageViewPainter {

	private ImageView view;

	/**
	 * Create a painter for the given view.
	 * 
	 * @param view the view, must not be null
	 */
	public ImageViewPainter(ImageView view) {
		super();
		this.view = view;
	}

	/**
	 * @return the width of the view
	 */
	public int getWidth() {
		return view.getWidth();
	}

	/**
	 * @return the height of the view
	 */
	public int getHeight() {
		return view.getHeight();
	}

	/**
	 * Start the painting on the image view.
	 */
	public final void paint() {
		if (getWidth() > 0 && getHeight() > 0) {
			Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			onPaint(canvas);
			view.setImageBitmap(bitmap);
		}
	}

	/**
	 * Paint on the canvas that will be displayed in the view.
	 * 
	 * @param canvas the canvas to paint on
	 */
	protected abstract void onPaint(Canvas canvas);

}
