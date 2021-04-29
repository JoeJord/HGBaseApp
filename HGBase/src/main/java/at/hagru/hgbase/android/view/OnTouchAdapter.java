package at.hagru.hgbase.android.view;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * A touch listener that does nothing. Can be necessary to react on {@link View#onTouchEvent(event)}.
 * 
 * @author hagru
 */
public class OnTouchAdapter implements OnTouchListener {

	/**
	 * Create a new on touch adapter.
	 */
	public OnTouchAdapter() {
		super();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

}
