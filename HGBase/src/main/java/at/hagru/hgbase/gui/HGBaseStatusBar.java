package at.hagru.hgbase.gui;

import android.app.Activity;

/**
 * A status bar for the application.
 * 
 * @author Harald
 */
public class HGBaseStatusBar extends HGBaseMultiTextPanel {
	
	private static ProgressPanel pnProgress; // needs to be static, otherwise it makes troubles with Android 
	
    public HGBaseStatusBar(Activity activity) {
        this(activity, new int[] { 0 });
    }

    public HGBaseStatusBar(Activity activity, int[] panelWidth) {
        super(activity, panelWidth, true, true);
    }

    /**
     * @param text text to set on the "main" panel of the status bar.
     */
    public void setText(String text) {
        setText(centeredPanel, text);
    }

    /**
     * Replaces the panel at the given index by the default progress panel. Set only one progress panel for a
     * status bar.
     * 
     * @param index The index for the progress panel.
     */
    public void setProgressPanel(int index) {
        setProgressPanel(index, new ProgressPanel(getActivity(), getBackgroundColor()));
    }

    /**
     * Replaces the panel at the given index by the default progress panel. Set only one progress panel for a
     * status bar.
     * 
     * @param index The index for the progress panel.
     * @param panel The progress panel to set.
     */
    public void setProgressPanel(int index, ProgressPanel panel) {
    	if (replacePanel(index, panel) != null) {
    		pnProgress = panel;
    	}
    }

    /**
     * @param state State for the default progress panel, if there exists one.
     */
    public void setProgressState(ProgressState state) {
        if (pnProgress != null) {
            pnProgress.setState(state);
        }
    }
    
}
