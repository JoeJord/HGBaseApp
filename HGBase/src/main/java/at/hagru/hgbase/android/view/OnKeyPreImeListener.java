package at.hagru.hgbase.android.view;

import android.view.KeyEvent;
import android.view.View;

/**
 * Listen to IME events before the are handled by the keyboard.
 * 
 * @author hagru
 */
public interface OnKeyPreImeListener {
	
    /**
     * Handle a key event before it is processed by any input method
     * associated with the view hierarchy.  This can be used to intercept
     * key events in special situations before the IME consumes them; a
     * typical example would be handling the BACK key to update the application's
     * UI instead of allowing the IME to see it and close itself.
     * 
     * @param v The view that is currently being edited.
     * @param keyCode The value in event.getKeyCode().
     * @param event Description of the key event.
     * @return If you handled the event, return true. If you want to allow the
     *         event to be handled by the next receiver, return false.
     */	
	public boolean onKeyPreIme(View v, int keyCode, KeyEvent event);

}
