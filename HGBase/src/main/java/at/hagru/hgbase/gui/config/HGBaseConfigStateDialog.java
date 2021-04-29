package at.hagru.hgbase.gui.config;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import at.hagru.hgbase.R;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.gui.HGBaseMultiTextPanel;
import at.hagru.hgbase.lib.HGBaseConfig;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Extends the config dialog with listeners, that show an error message if a configuration value is invalid.<p>
 * NOTE: There is no ok/cancel button any more.
 * 
 * @author hagru
 */
public abstract class HGBaseConfigStateDialog extends HGBaseConfigDialog {
	
    public static final int MSG_NONE = 1;
    public static final int MSG_INFO = 1;
    public static final int MSG_WARN = 2;
    public static final int MSG_ERROR = 3;
    
	private static final int STATUS_LABEL_HEIGHT = HGBaseMultiTextPanel.STATE_BAR_HEIGHT;
	private static final int STATUS_ICON_WIDTH = HGBaseMultiTextPanel.STATE_BAR_HEIGHT;	
	private static final int ICON_INDEX = 0;
	private static final int TEXT_INDEX = 1;
	
	private HGBaseMultiTextPanel statusLabel;
	private OnPreferenceChangeListener prefChangeListener;

	public HGBaseConfigStateDialog() {
		this(null);
	}

	public HGBaseConfigStateDialog(String title) {
		super(title);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		statusLabel = addStatusLabel();
		prefChangeListener = createPrefChangeListener();
		// the UI elements should have already been created in createComponents that is called from super.onCreate
		addPreferenceChangeListeners();
	}
	
    /**
     * @return the status bar that is modified by {@link #setMessage(String, int)}
     */
    protected HGBaseMultiTextPanel getStatusLabel() {
        return statusLabel;
    }

    /**
     * @return the preference change listener
     */
    protected OnPreferenceChangeListener getPrefChangeListener() {
        return prefChangeListener;
    }

    /**
	 * Adds a status label at the bottom of the view
	 */
	protected HGBaseMultiTextPanel addStatusLabel() {
		HGBaseMultiTextPanel label = new HGBaseMultiTextPanel(this, new int[] {STATUS_ICON_WIDTH, 0}, true, false);
		addContentView(label, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
																 ViewGroup.LayoutParams.WRAP_CONTENT,
																 android.view.Gravity.BOTTOM));
		View view = ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
		view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), STATUS_LABEL_HEIGHT);
		return label;
	}
	

    /**
     * @return the preference change listener
     */
    private OnPreferenceChangeListener createPrefChangeListener() {
        OnPreferenceChangeListener listener = new OnPreferenceChangeListener() {
            
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
        	HGBaseConfigStateDialog.this.onPreferenceChange(preference, newValue);
                return canLeave(preference, (newValue == null) ? null : newValue.toString());
            }            
        };
        return listener;
    }	

    /**
     * Called when a Preference has been changed by the user. This is called before the state of the Preference is about to be updated and before the state is persisted.
     * 
     * @param preference The changed Preference.
     * @param newValue The new value of the Preference.
     */
    protected void onPreferenceChange(Preference preference, Object newValue) {
        setMessage("", MSG_NONE);
    }

	/**
	 * Add the preference change listener to all UI elements
	 */
	@SuppressWarnings("deprecation")
    protected void addPreferenceChangeListeners() {
	    boolean foundError = false;
        PreferenceScreen prefScreen = getPreferenceScreen();
        int countPrefs = prefScreen.getPreferenceCount();
        for (int i = 0; i < countPrefs; i++) {
            Preference pref = prefScreen.getPreference(i);
            pref.setOnPreferenceChangeListener(getPrefChangeListener());
            if (!foundError && !canLeave(pref, HGBaseConfig.get(pref.getKey()))) {
                foundError = true;
            }
        }
	}

    /**
     * @param messageText The message text to be displayed.
     * @param messageType The type of the message (MSG_INFO, MSG_WARN, MSG_ERROR)
     */
    public void setMessage(String messageText, int messageType) {
        if (HGBaseTools.hasContent(messageText)) {
        	statusLabel.setText(TEXT_INDEX,messageText);
            Bitmap img = null;
            if (messageType==MSG_INFO) {
                img = HGBaseGuiTools.loadImage(R.drawable.msginfo);
            } else if (messageType==MSG_WARN) {
                img = HGBaseGuiTools.loadImage(R.drawable.msgwarn);
            } else if (messageType==MSG_ERROR) {
                img = HGBaseGuiTools.loadImage(R.drawable.msgerror);
            }
        	statusLabel.setImage(ICON_INDEX, img);
        } else {
        	statusLabel.setText(TEXT_INDEX, "");
        	statusLabel.setImage(ICON_INDEX, null);
        }
    }
    public void setErrorMessage(String messageText) {
        setMessage(messageText, MSG_ERROR);
    }
    public void setWarnMessage(String messageText) {
        setMessage(messageText, MSG_WARN);
    }
    public void setInfoMessage(String messageText) {
        setMessage(messageText, MSG_INFO);
    }
    
	/**
	 * Implement this method to define the conditions for leaving the dialog and set the error/status message
	 * 
     * @param preference the preference object where the value is changed
	 * @param newValue  the new value for the preference as String
	 * @return True if user is allowed to press the ok-button.
	 * @see #setMessage(String, int)
	 */
	abstract protected boolean canLeave(Preference preference, String newValue);

}
