package at.hagru.hgbase.gui;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.android.awt.Color;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * A panel that provides multiple labels for text.
 *
 * @author Harald
 */
public class HGBaseMultiTextPanel extends LinearLayout {
	
	final static public int STATE_BAR_HEIGHT = HGBaseGuiTools.getFieldHeight();
	final static protected int SEPARATOR_COLOR = Color.LIGHT_GRAY.getColorCode(); 
    final static protected int DEFAULT_TEXT_COLOR = Color.WHITE.getColorCode();
    final static protected int DEFAULT_BACKGROUND_COLOR = Color.DARK_GRAY.getColorCode();        
	
	final private int textColor;
	final private int backgroundColor;
    final private boolean hasSeparators;

	private Activity activity;
    private TextView[] txtLabel = null;
    protected int centeredPanel = -1;
	private int[] panelWidths;
    
    /**
     * @param panelWidths An array with the different panel widths, one has to include 0.
     * @param hasBorder True if the panel shall have a dark border.
     * @param hasSeparators True to divide the panels by separators.
     */
    public HGBaseMultiTextPanel(Activity activity, int[] panelWidths, boolean hasBorder, boolean hasSeparators) {
        this(activity, panelWidths, hasBorder, hasSeparators, DEFAULT_TEXT_COLOR, DEFAULT_BACKGROUND_COLOR);
    }

    /**
     * @param panelWidths An array with the different panel widths, one has to include 0.
     * @param hasBorder True if the panel shall have a dark border.
     * @param hasSeparators True to divide the panels by separators.
     * @param textColor the color of the text
     * @aram backgroundColor the color the background
     */
    public HGBaseMultiTextPanel(Activity activity, int[] panelWidths, boolean hasBorder, boolean hasSeparators,
                                                                      int textColor, int backgroundColor) {
        super(activity);
        this.activity = activity;
        this.hasSeparators = hasSeparators;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
        this.panelWidths = panelWidths;
        createPanels(panelWidths);
        setFocusable(false);
        setOrientation(LinearLayout.HORIZONTAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getPanelHeight()));
        if (hasBorder) {
            HGBaseGuiTools.setBlackBorder(this);
        }
    }
    
    /**
     * @return the corresponding activity
     */
    public Activity getActivity() {
    	return activity;
    }
    
    /**
     * @return calculated height of the status bar
     */
    public int getPanelHeight() {
    	return STATE_BAR_HEIGHT;
    }
    
    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
		View center = getPanel(centeredPanel);
    	if (center != null) {
    		ViewGroup.LayoutParams lp = center.getLayoutParams();
    		lp.width = calculateCenterPanelWidth(panelWidths);
    	}
    }

    /**
     * @return The number of panels.
     */
    public int length() {
        return (txtLabel==null)? 0 : txtLabel.length;
    }
    
    /**
     * @return the index of the center panel, or -1 in case there is no center panel (should not happen)
     */
    public int getCenterIndex() {
        return centeredPanel;
    }

    /**
     * @param panels array with widths of the panels, one has to be 0.
     */
    private void createPanels(int[] panels) {
        int nullPanels = countNullPanels(panels);
        int numPanels = panels.length;
        if (nullPanels==0) {
            numPanels++;
        } else if (nullPanels>1) {
            numPanels = numPanels - nullPanels + 1;
        }
        txtLabel = new TextView[numPanels];
        int idx = 0;
        int centerPanelWidth = calculateCenterPanelWidth(panels); 
        if (nullPanels==0) {
            this.addView(createPanel(idx, centerPanelWidth));
            centeredPanel = idx;
            idx = 1;
        }
        for (int i = 0; i < panels.length; i++) {
            if (panels[i] == 0) {
                if (centeredPanel < 0) {
                    this.addView(createPanel(idx, centerPanelWidth));
                    centeredPanel = idx;
                    idx++;
                }
            } else {
                this.addView(createPanel(idx, panels[i]));
                idx++;
            }
            if (hasSeparators) {
                this.addView(createSeparator());
            }        
            
        }
    }

	/**
	 * @param panels the widths of the panels
	 * @return the size of the center panel, i.e., the remaining space
	 */
	protected int calculateCenterPanelWidth(int[] panels) {
		boolean fullscreen = false;
		if (activity instanceof HGBaseActivity) {
		    fullscreen = (((HGBaseActivity) activity).getFullscreenMode() != null);
		}
		Point size = HGBaseGuiTools.getScreenSize(activity, fullscreen);
        int centerPanelWidth = Math.max(0, size.x - countPanelWidths(panels));
		return centerPanelWidth;
	}
	
    /**
	 * @return the color of the text
	 */
	protected int getTextColor() {
		return textColor;
	}

	/**
	 * @return the background color
	 */
	protected int getBackgroundColor() {
		return backgroundColor;
	}

	/**
     * @return A vertical separator.
     */
    private View createSeparator() {
        View s = new View(getContext());
        s.setLayoutParams(new LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT));
        s.setBackgroundColor(SEPARATOR_COLOR);
        return s;
    }

    /**
     * @param idx index of the panel.
     * @param width preferred width of the panel.
     * @return the new created panel.
     */
    private View createPanel(int idx, int width) {
        TextView pn = new TextView(getContext());
        pn.setLayoutParams(new LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
        pn.setTextColor(textColor);
        pn.setBackgroundColor(backgroundColor);
        pn.setMaxWidth(width);
        pn.setMaxHeight(STATE_BAR_HEIGHT);
        txtLabel[idx] = pn;
        return pn;
    }

    /**
     * @param panels array with panels.
     * @return number of width that are 0.
     */
    private int countNullPanels(int[] panels) {
        int num = 0;
        for (int i=0; i<panels.length; i++) {
            if (panels[i]==0) {
                num++;
            }
        }
        return num;
    }
    
    /**
     * @param panels array with panels.
     * @return number of width that are 0.
     */
    private int countPanelWidths(int[] panels) {
        int width = 0;
        for (int i=0; i<panels.length; i++) {
        	width += panels[i];
        }
        return width;
    }    

    /**
     * @param index index of the panel.
     * @return the status panel with this index or null.
     */
    public View getPanel(int index) {
        if (txtLabel == null || index < 0 || index >= txtLabel.length) {
            return null;
        } else {
            return txtLabel[index];
        }
    }
    
    /**
     * @return the center panel, may be null if no panels are defined
     */
    public View getCenterPanel() {
        return getPanel(centeredPanel);
    }

    /**
     * @param index index of the panel.
     * @return the status panel with this index or null.
     */
    public TextView getLabel(int index) {
        return (TextView) getPanel(index);
    }
    
    /**
     * Replaces an existing panel by another one.
     * 
     * @param index the index of the panel
     * @param newPanel the new panel to add
     * @return the old panel that was replaced or null if index was invalid
     */
    public TextView replacePanel(int index, TextView newPanel) {
    	TextView oldPanel = getLabel(index);
    	if (oldPanel != null) {
    		txtLabel[index] = newPanel;
    		LayoutParams lp = new LayoutParams(oldPanel.getLayoutParams());
            newPanel.setMaxWidth(lp.width);
            newPanel.setMaxHeight(STATE_BAR_HEIGHT);
            newPanel.setLayoutParams(lp);
            index = (hasSeparators) ? index * 2 : index;
            removeView(oldPanel);
            addView(newPanel, index);
    	}
    	return oldPanel;
    }

    /**
     * @param index index of the status panel.
     * @param text text to set on this panel.
     */
    public void setText(int index, String text) {
        if (index >= 0 && index < txtLabel.length) {
        	if (text == null) {
        		text = "";
        	}
        	if (HGBaseTools.hasContent(text) && !text.startsWith(" ")) {
        		text = " " + text;
        	}
            HGBaseGuiTools.setTextOnLabel(txtLabel[index], text);
        }
    }
    
    /**
     * @param index the index of the status panel
     * @param image the image to set, may be null to remove the image
     */
    public void setImage(final int index, final Bitmap image) {
        if (index >= 0 && index < txtLabel.length) {
            TextView label = txtLabel[index];
            HGBaseGuiTools.setImageOnLabel(label, image, label.getMaxWidth(), label.getMaxHeight());
        }        
    }

}
