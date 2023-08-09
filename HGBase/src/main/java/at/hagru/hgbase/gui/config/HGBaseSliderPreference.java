package at.hagru.hgbase.gui.config;

import android.app.Activity;
import android.preference.DialogPreference;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.lib.HGBaseConfig;
import at.hagru.hgbase.lib.HGBaseText;

/**
 * A number slider preference that uses the android seek bar.
 *
 * @author hagru
 */
public class HGBaseSliderPreference extends DialogPreference implements SeekBar.OnSeekBarChangeListener {

    private Activity activity;
    private SeekBar seekBar;
    private TextView txtValue;
    private int currentValue;
    private int minValue;
    private int maxValue;
    private String unitText;

    public HGBaseSliderPreference(Activity activity, int minValue, int maxValue, String unitText) {
        super(activity, null);
        this.activity = activity;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.unitText = (unitText == null) ? "" : unitText;
        setDefaultValue(Integer.valueOf(minValue));
    }

    @Override
    protected View onCreateDialogView() {
        currentValue = getValue();
        LinearLayout mainPanel = HGBaseGuiTools.createLinearLayout(activity, false);
        if (HGBaseText.existsText(getKey())) {
            TextView txtTitel = HGBaseGuiTools.createViewForMessage(activity, HGBaseText.getText(getKey()));
            mainPanel.addView(txtTitel, HGBaseGuiTools.createLinearLayoutParams(true, false));
        }
        txtValue = HGBaseGuiTools.createViewForMessage(activity, "");
        mainPanel.addView(txtValue, HGBaseGuiTools.createLinearLayoutParams(true, false));
        txtValue.setGravity(Gravity.CENTER);
        actualizeValue();
        seekBar = new SeekBar(activity);
        seekBar.setMax(maxValue - minValue);
        seekBar.setProgress(currentValue - minValue);
        seekBar.setOnSeekBarChangeListener(this);
        mainPanel.addView(seekBar, HGBaseGuiTools.createLinearLayoutParams(true, false));
        return mainPanel;
    }

    @Override
    public void setDefaultValue(Object defaultValue) {
        super.setDefaultValue(((Integer) defaultValue).intValue() - minValue);
    }

    /**
     * Actualizes the value, including the text of the unit
     */
    protected void actualizeValue() {
        if (txtValue != null) {
            txtValue.setText(String.valueOf(currentValue) + " " + unitText);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
        currentValue = value + minValue;
        actualizeValue();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            setValue(currentValue);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seek) {
        //NOCHECK: nothing to do by default
    }

    @Override
    public void onStopTrackingTouch(SeekBar seek) {
        //NOCHECK: nothing to do by default
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
        currentValue = value;
        HGBaseConfig.set(getKey(), value);
        if (seekBar != null) {
            seekBar.setProgress(value - minValue);
        }
        actualizeValue();
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
}
