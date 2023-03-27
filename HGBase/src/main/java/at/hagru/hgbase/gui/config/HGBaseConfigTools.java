package at.hagru.hgbase.gui.config;

import android.graphics.Bitmap;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.widget.EditText;

import at.hagru.hgbase.android.awt.Color;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.lib.HGBaseConfig;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Utility class to create configuration/preference elements.
 *
 * @author hagru
 */
final public class HGBaseConfigTools {

    private static final String SWITCH_TEXT_ON_APPENDING = "_on";
    private static final String SWITCH_TEXT_OFF_APPENDING = "_off";

    /**
     * Prevent instantiation of utility class.
     */
    private HGBaseConfigTools() {
        super();
    }

    /**
     * Create a check box preference.
     *
     * @param activity     the preference activity
     * @param key          the preference key
     * @param defaultValue the default value
     * @return the new created check box preference
     */
    public static CheckBoxPreference createCheckBoxPreference(PreferenceActivity activity, String key, boolean defaultValue) {
        CheckBoxPreference chkPref = new CheckBoxPreference(activity);
        setKeyTitleDefaultValue(chkPref, key, defaultValue);
        chkPref.setChecked(HGBaseConfig.getBoolean(key, defaultValue));
        return chkPref;
    }

    /**
     * Create a switch (on/Off) preference.
     *
     * @param activity     the preference activity
     * @param key          the preference key
     * @param defaultValue the default value
     * @return the new created switch preference
     */
    public static SwitchPreference createSwitchPreference(PreferenceActivity activity, String key, boolean defaultValue) {
        SwitchPreference switchPref = new SwitchPreference(activity);
        setKeyTitleDefaultValue(switchPref, key, defaultValue);
        if (HGBaseText.existsText(key + SWITCH_TEXT_ON_APPENDING)) {
            switchPref.setSwitchTextOn(HGBaseText.getText(key + SWITCH_TEXT_ON_APPENDING));
        }
        if (HGBaseText.existsText(key + SWITCH_TEXT_OFF_APPENDING)) {
            switchPref.setSwitchTextOff(HGBaseText.getText(key + SWITCH_TEXT_OFF_APPENDING));
        }
        switchPref.setChecked(HGBaseConfig.getBoolean(key, defaultValue));
        return switchPref;
    }

    /**
     * Create a list preference that holds a given number of text.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param values       a list of values that can be selected
     * @param defaultValue the default value
     * @return the appropriate list preference
     */
    public static ListPreference createListPreference(PreferenceActivity activity, String key, String[] values, String defaultValue) {
        return createListPreference(activity, key, values, null, defaultValue);
    }

    /**
     * Create a list preference that holds a given number of text.
     *
     * @param activity          the preference activity
     * @param key               the key of the new preference
     * @param values            a list of values that can be selected
     * @param defaultValue      the default value
     * @param showSelectedValue if {@code true} the selected value will be shown in the summary.
     * @return the appropriate list preference
     */
    public static ListPreference createListPreference(PreferenceActivity activity, String key, String[] values, String defaultValue, boolean showSelectedValue) {
        return createListPreference(activity, key, values, null, defaultValue, showSelectedValue);
    }

    /**
     * Create a list preference that holds a given number of text.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param values       a list of values that can be selected
     * @param images       a list with images to be displayed instead of the text, may be null or must have same size as values
     * @param defaultValue the default value
     * @return the appropriate list preference
     */
    public static ListPreference createListPreference(PreferenceActivity activity, String key, String[] values, Bitmap[] images, String defaultValue) {
        return createListPreference(activity, key, values, images, false, defaultValue);
    }

    /**
     * Create a list preference that holds a given number of text.
     *
     * @param activity          the preference activity
     * @param key               the key of the new preference
     * @param values            a list of values that can be selected
     * @param images            a list with images to be displayed instead of the text, may be null or must have same size as values
     * @param defaultValue      the default value
     * @param showSelectedValue if {@code true} the selected value will be shown in the summary.
     * @return the appropriate list preference
     */
    public static ListPreference createListPreference(PreferenceActivity activity, String key, String[] values, Bitmap[] images, String defaultValue, boolean showSelectedValue) {
        return createListPreference(activity, key, values, images, false, defaultValue, showSelectedValue);
    }

    /**
     * Create a list preference that holds a given number of text.
     *
     * @param activity         the preference activity
     * @param key              the key of the new preference
     * @param values           a list of values that can be selected
     * @param images           a list with images to be displayed instead of the text, may be null or must have same size as values
     * @param showTextAndImage true to show the text value and the image (if set), or false to show only images (if set)
     * @param defaultValue     the default value
     * @return the appropriate list preference
     */
    public static ListPreference createListPreference(PreferenceActivity activity, String key, String[] values, Bitmap[] images, boolean showTextAndImage, String defaultValue) {
        return createListPreference(activity, key, values, images, showTextAndImage, defaultValue, true);
    }

    /**
     * Create a list preference that holds a given number of text.
     *
     * @param activity          the preference activity
     * @param key               the key of the new preference
     * @param values            a list of values that can be selected
     * @param images            a list with images to be displayed instead of the text, may be null or must have same size as values
     * @param showTextAndImage  true to show the text value and the image (if set), or false to show only images (if set)
     * @param defaultValue      the default value
     * @param showSelectedValue if {@code true} the selected value will be shown in the summary.
     * @return the appropriate list preference
     */
    public static ListPreference createListPreference(PreferenceActivity activity, String key, String[] values, Bitmap[] images, boolean showTextAndImage, String defaultValue, boolean showSelectedValue) {
        ListPreference list = new ListPreference(activity);
        setKeyTitleDefaultValue(list, key, defaultValue);
        list.setEntryValues(values);
        if (images == null || images.length == 0) {
            String[] valueText = values.clone();
            for (int i = 0; i < valueText.length; i++) {
                if (HGBaseText.existsText(valueText[i])) {
                    valueText[i] = HGBaseText.getText(valueText[i]);
                }
            }
            list.setEntries(valueText);
        } else {
            CharSequence[] valueImage = new CharSequence[Math.max(images.length, values.length)];
            for (int i = 0; i < images.length; i++) {
                if (images[i] != null) {
                    valueImage[i] = HGBaseGuiTools.createStringForImage(activity.getApplicationContext(), images[i], HGBaseGuiTools.getButtonHeight(), HGBaseGuiTools.getScreenSize(activity).x / 2);
                    if (showTextAndImage && i < values.length && valueImage[i] instanceof SpannableStringBuilder) {
                        ((SpannableStringBuilder) valueImage[i]).append("   " + HGBaseText.getText(values[i]));
                    }
                } else if (showTextAndImage && i < values.length) {
                    valueImage[i] = HGBaseText.getText(values[i]);
                }
            }
            if (values.length > images.length) {
                for (int i = images.length; i < values.length; i++) {
                    valueImage[i] = HGBaseText.getText(values[i]);
                }
            }
            list.setEntries(valueImage);
        }
        list.setValue(HGBaseConfig.get(key, defaultValue));
        if (showSelectedValue) {
            list.setSummary("%s");
        }
        return list;
    }


    /**
     * Create a number preference that holds numbers.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param min          the minimum value
     * @param max          the maximum value, must be greater than the minimum
     * @param defaultValue the default value
     * @return the appropriate list preference
     */
    public static ListPreference createNumberPreference(PreferenceActivity activity, String key, int min, int max, int defaultValue) {
        if (max <= min) {
            HGBaseTools.throwAsRuntimeException(new IllegalArgumentException("The minimum has to be lower than the maximum"));
        }
        String[] values = new String[max - min + 1];
        for (int i = min; i <= max; i++) {
            values[i - min] = String.valueOf(i);
        }
        return createListPreference(activity, key, values, String.valueOf(defaultValue));
    }

    /**
     * Creates a preference object for entering a number.
     *
     * @param activity     The preference activity.
     * @param key          The key of the new preference.
     * @param defaultValue The default value.
     * @param maxDigits    The maximum number of digits that can be entered.
     * @return The new created preference.
     */
    public static EditTextPreference createEditNumberPreference(PreferenceActivity activity, String key, int defaultValue, int maxDigits) {
        EditTextPreference preference = createTextPreference(activity, key, String.valueOf(defaultValue));
        EditText edit = preference.getEditText();
        edit.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxDigits)});
        return preference;
    }

    /**
     * Create a preference object for entering text.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param defaultValue the default value
     * @return the new created text preference
     */
    public static EditTextPreference createTextPreference(PreferenceActivity activity, String key, String defaultValue) {
        return createTextPreference(activity, key, defaultValue, false);
    }

    /**
     * Create a preference object for entering text.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param defaultValue the default value
     * @param singleLine   true to create a text field with a single line, false for multi-line (default)
     * @return the new created text preference
     */
    public static EditTextPreference createTextPreference(PreferenceActivity activity, String key, String defaultValue, boolean singleLine) {
        return createTextPreference(activity, key, defaultValue, singleLine, true);
    }

    /**
     * Create a preference object for entering text.
     *
     * @param activity         the preference activity
     * @param key              the key of the new preference
     * @param defaultValue     the default value
     * @param singleLine       true to create a text field with a single line, false for multi-line (default)
     * @param showEnteredValue if {@code true} the selected value will be shown in the summary.
     * @return the new created text preference
     */
    public static EditTextPreference createTextPreference(PreferenceActivity activity, String key, String defaultValue, boolean singleLine, boolean showEnteredValue) {
        EditTextPreference textPref;
        if (showEnteredValue) {
            textPref = new HGBaseEditTextPreference(activity);
        } else {
            textPref = new EditTextPreference(activity);
        }
        setKeyTitleDefaultValue(textPref, key, defaultValue);
        textPref.setText(HGBaseConfig.get(key, defaultValue));
        textPref.getEditText().setSingleLine(singleLine);
        return textPref;
    }

    /**
     * Create a preference object for selecting a color.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param defaultColor the default color, may be null if no color is allowed
     * @return the new created text preference
     */
    public static HGBaseColorPreference createColorPreference(PreferenceActivity activity, String key, Color defaultColor) {
        HGBaseColorPreference colorPref = new HGBaseColorPreference(activity);
        setKeyTitleDefaultValue(colorPref, key, defaultColor);
        if (HGBaseConfig.existsKey(key)) {
            colorPref.setColor(HGBaseConfig.getColor(key));
        } else {
            colorPref.setColor(defaultColor);
        }
        return colorPref;
    }

    /**
     * Create a preference object for selecting a number with a slider.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param minValue     the minimum number value
     * @param maxValue     the maximum number value
     * @param defaultValue the default value, has to be between min and max value
     * @param unitText     the text for the unit to display, me be empty
     * @return the new created number preference
     */
    public static HGBaseSliderPreference createSliderPreference(PreferenceActivity activity, String key, int minValue, int maxValue, int defaultValue, String unitText) {
        HGBaseSliderPreference sliderPref = new HGBaseSliderPreference(activity, minValue, maxValue, unitText);
        setKeyTitleDefaultValue(sliderPref, key, Integer.valueOf(defaultValue));
        sliderPref.setValue(HGBaseConfig.getInt(key, defaultValue));
        return sliderPref;
    }

    /**
     * Create a preference object for selecting a number with a number picker.
     *
     * @param activity     the preference activity
     * @param key          the key of the new preference
     * @param minValue     the minimum number value
     * @param maxValue     the maximum number value
     * @param diff         the difference between values
     * @param defaultValue the default value, has to be between min and max value
     * @return the new created number preference
     */
    public static HGBaseNumberPickerPreference createNumberPickerPreference(PreferenceActivity activity, String key, int minValue, int maxValue, int diff, int defaultValue) {
        return createNumberPickerPreference(activity, key, minValue, maxValue, diff, defaultValue, true);
    }

    /**
     * Create a preference object for selecting a number with a number picker.
     *
     * @param activity        the preference activity
     * @param key             the key of the new preference
     * @param minValue        the minimum number value
     * @param maxValue        the maximum number value
     * @param diff            the difference between values
     * @param defaultValue    the default value, has to be between min and max value
     * @param showPickedValue if {@code true} the picked value will be shown in the summary.
     * @return the new created number preference
     */
    public static HGBaseNumberPickerPreference createNumberPickerPreference(PreferenceActivity activity, String key, int minValue, int maxValue, int diff, int defaultValue, boolean showPickedValue) {
        HGBaseNumberPickerPreference pickerPref = new HGBaseNumberPickerPreference(activity, minValue, maxValue, diff, showPickedValue);
        setKeyTitleDefaultValue(pickerPref, key, Integer.valueOf(defaultValue));
        pickerPref.setValue(HGBaseConfig.getInt(key, defaultValue));
        return pickerPref;
    }

    /**
     * Sets key, title and default value for the given preference.<p>
     * The title is the string value that is found with the preference key.
     *
     * @param pref         the preference object to set the key and title for
     * @param key          the preference key
     * @param defaultValue the default value
     */
    private static <T> void setKeyTitleDefaultValue(Preference pref, String key, T defaultValue) {
        pref.setKey(key);
        pref.setTitle(HGBaseText.getText(key));
        pref.setDefaultValue(defaultValue);
    }

}
