package at.hagru.hgbase.gui.config;

import android.content.Context;
import android.preference.EditTextPreference;
import android.util.AttributeSet;

/**
 * A text edit preference which is able to show the entered text in the summary.
 * 
 * @author Josef Jordan
 */
public class HGBaseEditTextPreference extends EditTextPreference {

    public HGBaseEditTextPreference(Context context) {
	super(context);
    }

    public HGBaseEditTextPreference(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    public HGBaseEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
	super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
	super.onDialogClosed(positiveResult);
	setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
	return getText();
    }
}
