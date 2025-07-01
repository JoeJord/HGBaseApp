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
        switch(preference.getKey()) {
            case "config_name":
                if (!HGBaseTools.hasContent(newValue)) {
                    setErrorMessage("Name must not be empty!");
                    return false;
                }
                char firstLetter = newValue.charAt(0);
                if (firstLetter < 'A' || firstLetter > 'Z') {
                    setWarnMessage("Name doest not start with a capital letter.");
                }
                break;
            case "config_number":
                if (HGBaseTools.hasContent(newValue) && Integer.parseInt(newValue) < 3) {
                    setWarnMessage("Number is less than 3.");
                }
                break;
            case "config_color":
                if ((!HGBaseTools.hasContent(newValue)) || (Integer.parseInt(newValue) == -1)) {
                    setWarnMessage("You haven't picked a color.");
                }
                break;
            case "config_value":
                if (Integer.parseInt(newValue) > 90) {
                    setWarnMessage("The slider value is greater than 90.");
                }
                break;
            case "config_picker":
                if (Integer.parseInt(newValue) > 45) {
                    setWarnMessage("Picked number is greater than 45.");
                }
                break;
            case "config_onoff":
                if ("false".equals(newValue)) {
                    setWarnMessage("The switch is turned off!");
                }
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void okPressed() {
        HGBaseLog.logDebug("The configuration dialog was left.");
    }

}
