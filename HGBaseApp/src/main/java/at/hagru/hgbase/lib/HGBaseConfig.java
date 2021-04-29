package at.hagru.hgbase.lib;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Collections;
import java.util.Set;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.awt.Color;

/**
 * This is a facade to the android shared preferences and contains the configuration information.
 *
 * @author hagru
 */
public final class HGBaseConfig {
	
	private static final SharedPreferences PREFS = PreferenceManager.getDefaultSharedPreferences(HGBaseAppTools.getContext());
	
    private HGBaseConfig() {
        super();
    }
    
    /**
     * Returns the shared preferences of the application.
     *  
     * @return the shared preferences
     */
    public static SharedPreferences getPreferences() {
    	return PREFS;
    }

    /**
     * Checks if the given key exists in the preferences.
     * 
     * @param key A key to test.
     * @return True, if this key exists.
     */
    public static boolean existsKey(String key) {
    	return PREFS.contains(key);
    }
    
    /**
	 * Returns the given text option
	 *
	 * @param key key of option
	 * @return option
	 */
	public static String get(String key) {
    	Object value = PREFS.getAll().get(key);
    	return (value != null) ? value.toString() : "";
	    //return get(key, ""); -> fails if the preference is no String, but of another type
	}

	/**
     * Returns an array with the text options that are set
     *
     * @return array with keys of options
     */
    public static String[] getKeys() {
    	Set<String> keys = PREFS.getAll().keySet();
    	return keys.toArray(new String[keys.size()]);
    }

    /**
     * Returns the given text option
     *
     * @param set key of text option
     * @param defaultValue default value
     * @return text option
     */
    public static String get(String key, String defaultValue) {
    	return PREFS.getString(key, defaultValue);
    }

    /**
     * Returns the given text option as set of strings.
     *
     * @param set key of text option
     * @return text option as set of strings.
     */
    public static Set<String> getStrings(String key) {
    	return PREFS.getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * Returns the given text option as set of strings.
     *
     * @param set key of text option
     * @param defaultValues default values
     * @return text option as set of strings.
     */
    public static Set<String> getStrings(String key, Set<String> defaultValues) {
    	return PREFS.getStringSet(key, defaultValues);
    }

    /**
     * Returns the given text option as integer
     *
     * @param set key of text option
     * @return text option as integer
     */
    public static int getInt(String key) {
        return getInt(key, HGBaseTools.INVALID_INT);
    }

    /**
     * Returns the given text option as integer
     *
     * @param set key of text option
     * @param defaultValue default value
     * @return text option as integer
     */
    public static int getInt(String key, int defaultValue) {
        return PREFS.getInt(key, defaultValue);
    }

    /**
     * Returns the given text option as boolean
     *
     * @param key key of text option
     * @return text option as boolean
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Returns the given text option as boolean
     *
     * @param key key of text option
     * @param defaultValue default value
     * @return text option as boolean
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return PREFS.getBoolean(key, defaultValue);
    }

    /**
     * @param key The key of the option.
     * @return The color or null if invalid.
     */
    public static Color getColor(String key) {
        int rgb = getInt(key);
        return (rgb == HGBaseTools.INVALID_INT) ? null : new Color(rgb);
    }

    /**
     *
     * @param key The key of the option.
     * @param defaultColor The default color if color is not set.
     * @return The color defined or the default color;
     */
    public static Color getColor(String key, Color defaultColor) {
        Color c = getColor(key);
        return (c == null) ? defaultColor : c;
    }
    

    /**
     * Removes the given text option
     *
     * @param key key of option
     * @return true if this key existed and was deleted
     */
    public static void remove(String key) {
    	PREFS.edit().remove(key).commit();
    }

    /**
     * Sets the given text option
     *
     * @param key key of option
     * @param option text of the option
     */
    public static void set(String key, String option) {
    	if (option == null) {
    		remove(key);
    	} else {
    		PREFS.edit().putString(key, option).commit();
    	}
    }

    /**
     * Sets the given text options
     *
     * @param key key of option
     * @param options string set of the options
     */
    public static void set(String key, Set<String> options) {
        PREFS.edit().putStringSet(key, options).commit();
    }

    /**
     * Sets the given integer option
     *
     * @param key key of option
     * @param option integer value of the option
     */
    public static void set(String key, int option) {
        PREFS.edit().putInt(key, option).commit();
    }

    /**
     * Sets the given boolean option
     *
     * @param key key of option
     * @param option boolean value of the option
     */
    public static void set(String key, boolean option) {
		PREFS.edit().putBoolean(key, option).commit();
    }

    /**
     * @param key The key of the option.
     * @param color The color value to set.
     */
    public static void set(String key, Color color) {
    	if (color == null) {
    		remove(key);
    	} else {
    		set(key, color.getColorCode());
    	}
    }


    /**
     * Sets default values from a preferences file by reading the {@code android:defaultvalue} attribute.
     * 
     * @param resId the resource id of the preferences file
     */
    public static void setDefaultValuesFromPreferencesFile(int resId) {
    	PreferenceManager.setDefaultValues(HGBaseAppTools.getContext(), resId, false);    	
    }

}
