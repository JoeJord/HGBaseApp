package at.hagru.hgbase.gui.config;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.ViewGroup;

import at.hagru.hgbase.android.awt.Color;
import at.hagru.hgbase.android.awt.Dimension;
import at.hagru.hgbase.android.dialog.HSVColorPickerDialog.OnColorSelectedListener;
import at.hagru.hgbase.gui.HGBaseDialog;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.lib.HGBaseConfig;

/**
 * A color preference.<p>
 * If the default color is -1, than it is allowed that no color is selected.
 * 
 * @author hagru
 */
public class HGBaseColorPreference extends Preference implements OnColorSelectedListener {

	private static final int NO_COLOR = -1;
	private static final Dimension PREVIEW_SIZE = new Dimension(HGBaseGuiTools.getButtonHeight(), 
																HGBaseGuiTools.getButtonHeight());
	
	private PreferenceActivity activity;
	private Color defaultValue;
	private View colorPreview;

	public HGBaseColorPreference(PreferenceActivity activity) {
		super(activity, null);
		this.activity = activity;
	}
	
	@Override
	protected void onClick() {
		super.onClick();
		boolean allowNoColor = (defaultValue == null); 
		HGBaseDialog.showColorDialog(activity, getColor(), allowNoColor, HGBaseColorPreference.this);
	}
	
	@Override
	protected void onBindView(View view) {
		super.onBindView(view);
		colorPreview = new View(view.getContext());
		((ViewGroup) view).addView(colorPreview);
		HGBaseGuiTools.setViewSize(colorPreview, PREVIEW_SIZE);
		showColorPreview();
	}
	
	/**
	 * Shows the color in the preview view
	 */
	protected void showColorPreview() {
		if (colorPreview != null) {
			Color color = getColor();
			if (color == null) {
				colorPreview.setVisibility(View.INVISIBLE);
			} else {
				colorPreview.setVisibility(View.VISIBLE);
				colorPreview.setBackgroundColor(color.getColorCode());
			}
		}
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		super.setDefaultValue(defaultValue);
		this.defaultValue = (defaultValue == null) ? null : (Color) defaultValue;
	}
	   
	/**
	 * @param color the color to store
	 */
    public void setColor(Color color) {
        if (color == null) {
            HGBaseConfig.remove(getKey());
        } else {
            HGBaseConfig.set(getKey(), color);
        }
        showColorPreview();        
    }
    
	/**
	 * @param color the color as integer value to store
	 */
	public void setColor(int color) {
		if (color == NO_COLOR) {
		    setColor(null);
		} else {
			setColor(new Color(color));
		}
	}	

    /**
     * @return the color preference
     */
    public Color getColor() {
        if (HGBaseConfig.existsKey(getKey())) {
            return HGBaseConfig.getColor(getKey());
        } else {
            return null;
        }
    }	

	/**
	 * @return the color preference as integer value
	 */
	public int getColorCode() {
	    Color color = getColor();
	    return (color == null) ? NO_COLOR : color.getColorCode();
	}

	@Override
	public void colorSelected(Integer color) {
		if (color == null || color.intValue() == NO_COLOR) {
			setColor(NO_COLOR);
		} else {
			setColor(color.intValue());
		}
	}	
	
}
