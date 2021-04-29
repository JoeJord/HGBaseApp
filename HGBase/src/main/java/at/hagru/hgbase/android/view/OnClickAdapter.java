package at.hagru.hgbase.android.view;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * A click listener that does nothing. Can be necessary to react on {@link View#performClick()}.
 * 
 * @author hagru
 */
public class OnClickAdapter implements OnClickListener {

	/**
	 * Create a new on click adapter.
	 */
	public OnClickAdapter() {
		super();
	}

	@Override
	public void onClick(View v) {
		//NOCHECK: nothing to do by the adapter
	}

}
