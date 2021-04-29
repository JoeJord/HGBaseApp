package at.hagru.hgbase.gui.config;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.MenuItem;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.gui.menu.actions.AbstractMenuAction;
import at.hagru.hgbase.lib.HGBaseConfig;

/**
 * A menu action that is related to a boolean configuration item.
 * 
 * @author hagru
 */
public abstract class HGBaseConfigMenuAction extends AbstractMenuAction {

	private final String configId;
	private final int iconOn;
	private final int iconOff;
    private final OnSharedPreferenceChangeListener prefListener;

	/**
	 * @param activity the activity
	 * @param id the id of the menu, has to be the same as the configuration key
	 * @param iconOn the id of the drawable if the configuration is on/true
	 * @param iconOff the id of the drawable if the configuration is off/false
	 */
	public HGBaseConfigMenuAction(HGBaseActivity activity, String id, int iconOn, int iconOff) {
		super(activity);
		this.configId = id;
		this.iconOn = iconOn;
		this.iconOff = iconOff;
		this.prefListener = new OnSharedPreferenceChangeListener() {
            
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(configId)) {
                    setIconByConfiguration();
                }
            }
        };
        HGBaseConfig.getPreferences().registerOnSharedPreferenceChangeListener(prefListener);
		setIconByConfiguration();
	}
	
    /**
     * @return the current configuration value
     */
    protected boolean getCurrentConfiguration() {
        return HGBaseConfig.getBoolean(configId);
    }
    
    

	/**
     * @return the icon for activated configuration
     */
    protected int getIconOn() {
        return iconOn;
    }

    /**
     * @return the icon for deactivated configuration
     */
    protected int getIconOff() {
        return iconOff;
    }

    /**
	 * Sets the icon of the menu action (in the action bar) depending on the configuration.
	 */
	public void setIconByConfiguration() {
		int resId = HGBaseResources.getResourceIdByName(configId, HGBaseResources.ID);
		if (resId > 0 && iconOn > 0 && iconOff > 0) {
		    MenuItem item = getActivity().getOptionsMenuItem(resId);
		    if (item != null) {
    		    int iconId = getCurrentConfiguration() ? iconOn : iconOff;
    		    item.setIcon(iconId);
		    }
		}
	}
	
	@Override
	public void perform(int id, MenuItem item) {
	    boolean config = getCurrentConfiguration();
	    HGBaseConfig.set(configId, !config);	    
	}

}
