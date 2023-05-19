package at.hagru.testapp;

import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;

import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.gui.config.HGBaseColorPreference;
import at.hagru.hgbase.gui.config.HGBaseConfigDialog;
import at.hagru.hgbase.gui.config.HGBaseConfigStateDialog;
import at.hagru.hgbase.gui.config.HGBaseConfigTools;
import at.hagru.hgbase.gui.config.HGBaseNumberPickerPreference;
import at.hagru.hgbase.gui.config.HGBaseSliderPreference;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Test the {@link HGBaseConfigDialog}.
 *
 * @author hagru
 */
public class TestAppConfigDialog extends HGBaseConfigStateDialog {
    public TestAppConfigDialog() {
        super("TestApp Configuration");
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void createComponents() {
        addPreferencesFromResource(HGBaseResources.getResourceIdByName("configdialog", HGBaseResources.XML));
        ListPreference numbers = HGBaseConfigTools.createNumberPreference(this, "config_number", 1, 10, 5);
        addPreference(numbers);
        HGBaseColorPreference color = HGBaseConfigTools.createColorPreference(this, "config_color", null);
        addPreference(color);
        HGBaseSliderPreference slider = HGBaseConfigTools.createSliderPreference(this, "config_value", 0, 100, 50, "%");
        addPreference(slider);
        HGBaseNumberPickerPreference picker = HGBaseConfigTools.createNumberPickerPreference(this, "config_picker", 10, 50, 1, 25);
        addPreference(picker);
        SwitchPreference onOff = HGBaseConfigTools.createSwitchPreference(this, "config_onoff", false);
        addPreference(onOff);
    }

    @Override
    protected boolean canLeave(Preference preference, String newValue) {
        if ("config_name".equals(preference.getKey())) {
            if (HGBaseTools.hasContent(newValue)) {
                char firstLetter = newValue.charAt(0);
                if (firstLetter < 'A' || firstLetter > 'Z') {
                    setWarnMessage("Name doest not start with a capital letter.");
                }
            } else {
                setErrorMessage("Name must not be empty!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected void okPressed() {
        HGBaseLog.logDebug("The configuration dialog was left.");
    }

}
