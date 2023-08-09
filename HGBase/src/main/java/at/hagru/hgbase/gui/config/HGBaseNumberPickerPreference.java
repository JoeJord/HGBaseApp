package at.hagru.hgbase.gui.config;

import android.app.Activity;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.lib.HGBaseConfig;
import at.hagru.hgbase.lib.HGBaseText;

/**
 * A number picker preference.
 *
 * @author hagru
 */
public class HGBaseNumberPickerPreference extends DialogPreference {

    private final Activity activity;
    private NumberPicker numberPicker;
    private final int minValue;
    private final int maxValue;
    private final int diff;
    /**
     * Flag if the picked value should be shown in the summary.
     */
    private final boolean showPickedValue;

    public HGBaseNumberPickerPreference(Activity activity, int minValue, int maxValue, int diff, boolean showPickedValue) {
        super(activity, null);
        this.activity = activity;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.diff = diff;
        this.showPickedValue = showPickedValue;
    }

    @Override
    protected View onCreateDialogView() {
        LinearLayout mainPanel = HGBaseGuiTools.createLinearLayout(activity, false);
        if (HGBaseText.existsText(getKey())) {
            TextView txtTitle = HGBaseGuiTools.createViewForMessage(activity, HGBaseText.getText(getKey()));
            mainPanel.addView(txtTitle, HGBaseGuiTools.createLinearLayoutParams(true, false));
        }
        numberPicker = HGBaseGuiTools.createRangeNumberPicker(activity, minValue, maxValue, diff, getValue());
        mainPanel.addView(numberPicker, HGBaseGuiTools.createLinearLayoutParams(true, false));
        return mainPanel;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            int value = numberPicker.getValue() * diff;
            setValue(value);
            onPreferenceChange(value);
            if (showPickedValue) {
                setSummary(String.valueOf(value));
            }
        }
    }

    /**
     * This method is called, when the value of the preference has changed.
     *
     * @param newValue The new value of the preference.
     */
    private void onPreferenceChange(int newValue) {
        OnPreferenceChangeListener preferenceChangeListener = getOnPreferenceChangeListener();
        if (preferenceChangeListener != null) {
            preferenceChangeListener.onPreferenceChange(this, newValue);
        }
    }

    /**
     * @return the current value
     */
    public int getValue() {
        return (HGBaseConfig.existsKey(getKey())) ? HGBaseConfig.getInt(getKey()) : minValue;
    }

    /**
     * @param value the new value to set
     */
    public void setValue(int value) {
        HGBaseConfig.set(getKey(), value);
        if (numberPicker != null) {
            numberPicker.setValue(value / diff);
        }
    }

    /**
     * @return the minimum value
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * @return the maximum value
     */
    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public CharSequence getSummary() {
        return (showPickedValue) ? String.valueOf(getValue()) : null;
    }
}
