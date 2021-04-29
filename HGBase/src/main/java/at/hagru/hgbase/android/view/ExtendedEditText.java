package at.hagru.hgbase.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * An extended text field that has overwritten some methods and allows more listeners.<p>
 * At a first glance it is possible to react on a button of the software keyboard when editing the text. 
 * 
 * @author hagru
 */
public class ExtendedEditText extends EditText {
	
	private OnKeyPreImeListener keyPreImeListener;

	public ExtendedEditText(Context context) {
		super(context);
	}

	public ExtendedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExtendedEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
    /**
	 * @return the IME listener, may be null
	 */
	public OnKeyPreImeListener getKeyPreImeListener() {
		return keyPreImeListener;
	}

	/**
	 * @param keyPreImeListener the IME listener to set
	 */
	public void setKeyPreImeListener(OnKeyPreImeListener keyPreImeListener) {
		this.keyPreImeListener = keyPreImeListener;
	}

	@Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyPreImeListener == null || !keyPreImeListener.onKeyPreIme(this, keyCode, event)) {
			return super.onKeyPreIme(keyCode, event);
		} else {
            return true;
		}
    }	

}
    
