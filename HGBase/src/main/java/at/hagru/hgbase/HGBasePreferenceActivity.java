package at.hagru.hgbase;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.lib.HGBaseText;

/**
 * Default preferences activity to be shown automatically by the {@code action_preferences} menu action.
 * The preferences file is expected to be named {@code /xml/preferences.xml}.<p>
 * This class does not build up preferences by several fragments but just by one preferences file.
 * 
 * @author hagru
 */
public class HGBasePreferenceActivity extends PreferenceActivity {

	/**
	 * Create a new preference activity.
	 */
	public HGBasePreferenceActivity() {
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(HGBaseText.getText("action_preferences"));
		addPreferencesToActivity();
	}

	/**
	 * Add the preferences from the xml file to the activity.
	 */
	@SuppressWarnings("deprecation")
	protected void addPreferencesToActivity() {
		int prefFileId = HGBaseResources.getResourceIdByName("preferences", HGBaseResources.XML);
		if (prefFileId != 0) {
			addPreferencesFromResource(prefFileId);			
		}
	}

}
