package at.hagru.hgbase.android.view;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * An adapter that has an empty implementation for all methods of the interface.
 * 
 * @author hagru
 */
public class TextWatchAdapter implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// NOCHECK: empty implementation
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// NOCHECK: empty implementation
	}

	@Override
	public void afterTextChanged(Editable s) {
		// NOCHECK: empty implementation
	}
}
