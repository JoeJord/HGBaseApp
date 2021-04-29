package at.hagru.hgbase.gui.config;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * A configuration dialog. Although called dialog, the implementation beyond uses an {@link PreferenceActivity}<p>
 * <b>Important: </b>It is necessary to implement a constructor without parameters and to add it to the manifest!<p>
 * The GUI-elements are created in the method <code>createCompontents</code>, user either a preferences xml file or 
 * create the elements dynamically there. If OK is pressed, all preference fields are saved, if other things
 * shall happen extend <code>okPressed</code>.
 * 
 * @author hagru 
 */
public abstract class HGBaseConfigDialog extends PreferenceActivity {

	private String title;

    public HGBaseConfigDialog() {
        this(null);
    }
    
    protected HGBaseConfigDialog(String title) {
        this.title = title;
    }
 
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HGBaseTools.hasContent(title)) {
        	setTitle(title);
        }
        createComponents();
    }
	
	/**
	 * Shows the preference activity in style of a configuration dialog.
	 * 
	 * @param callingActivity the activity that calls the dialog
	 * @param dialog the dialog class to display, i.e. a subclass of this class
	 */
	public static void show(Activity callingActivity, Class<? extends HGBaseConfigDialog> dialogClass) {
		show(callingActivity, dialogClass, -1);
	}

	/**
	 * Shows the preference activity in style of a configuration dialog.
	 * 
	 * @param callingActivity the activity that calls the dialog
	 * @param requestCode the code to react in the calling activity (@link requestCode#onActivityResult(int, int, Intent)}
	 * @param dialog the dialog to display, i.e. a subclass of this class
	 */
	public static void show(Activity callingActivity, Class<? extends HGBaseConfigDialog> dialogClass, int requestCode) {
	    HGBaseAppTools.startActivityForResult(callingActivity, dialogClass, requestCode);
	}
	
	@Override
	public void finish() {
	    okPressed();
		super.finish();
	}
	
    /**
     * Convenience method when the preference dialog was left.
     */
    protected void okPressed() {
        //NOCHECK: nothing to do by default
    }
    
    /**
     * Adds a preference to the dialog.
     * 
     * @param activity the preference activity
     * @param pref the preference to add
     */
    protected void addPreference(Preference pref) {
        PreferenceScreen screen = getOrCreatePreferenceScreen();
        screen.addPreference(pref);
    }

    /**
     * Tries to get the preference screen and creates it, if it does not exist.
     * 
     * @return the preference screen
     */
    @SuppressWarnings("deprecation")
    private PreferenceScreen getOrCreatePreferenceScreen() {
        PreferenceScreen screen = getPreferenceScreen();
    	if (screen == null) {
    		screen = getPreferenceManager().createPreferenceScreen(getApplicationContext());
    		setPreferenceScreen(screen);
    	}
    	return screen;
    }    
    
    /**
     * Adds a new preference category.
     * 
     * @param msgKey the key for the category name
     */
    protected void addPreferenceCategory(String msgKey) {
        PreferenceCategory prefCat = new PreferenceCategory(getApplicationContext());
        String name = (HGBaseText.existsText(msgKey))? HGBaseText.getText(msgKey) : msgKey;
        prefCat.setTitle(name);
        addPreference(prefCat);
    }

    /**
     * Add preference Gui-elements to the dialog panel.<p>
     * This can be done by using an xml preferences file, e.g.:
     * <code><br>
     * activity.addPreferencesFromResource(R.xml.myConfigDialog);  
     * </code>
     * <p>
     * or by dynamically adding preference items, e.g.:
     * <code><br>
     * 	ListPreference numbers = HGBaseConfigurationTools.createNumberPreference(activity, HGBaseText.getText(R.string.config), 1, 10, 5);
	 *	activity.getPreferenceScreen().addPreference(numbers);
     * </code>
     * 
     * @see PreferenceActivity#addPreferencesFromResource(int)
     */
    abstract protected void createComponents();
    
}
