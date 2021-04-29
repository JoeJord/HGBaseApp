package at.hagru.hgbase;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;

import java.util.Date;

import at.hagru.hgbase.gui.HGBaseWelcome;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * A main activity that shows a welcome dialog (splash screen) when it is started.<p>
 * The welcome image must be named {@code welcome.jpg}.
 * 
 * @see #onCreatePreWelcome()
 * @see #onCreateDuringWelcome()
 * @see #onCreatePostWelcome()
 * @author hagru
 */
public abstract class HGBaseWelcomeActivity extends HGBaseActivity {
	
	private static final int WELCOME_MIN_MILLIS = 1000;

	protected static boolean firstCall = true;
	private Dialog welcomeDialog;
	private long startTime;	
	
	public HGBaseWelcomeActivity() {
		super();
	}

	public HGBaseWelcomeActivity(int activityLayoutId) {
		super(activityLayoutId);
	}

	public HGBaseWelcomeActivity(int activityLayoutId, int optionsMenuId) {
		super(activityLayoutId, optionsMenuId);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new InitClassWithWelcomeTask().execute();
	}
	
	/**
	 * Returns the welcome dialog class. To be overwritten when a special welcome screen shall be displayed that shall 
	 * show other information than the default one.
	 * 
	 * @return the dialog to be displayed as welcome screen
	 */
	protected Dialog getWelcomeDialog() {
		return HGBaseWelcome.createDialog(this);
	}
	
	/**
	 * Do GUI related actions before the initialization task.<p>
	 * The default implementation is always empty.
	 */
	protected void onCreatePreWelcome() {
		// NOCHECK: nothing to do in default implementation
	}
	
	/**
	 * Allows to specify initialization tasks that shall be done when the welcome screen is shown.<p>
	 * The default implementation is always empty.
	 */
	protected void onCreateDuringWelcome() {
		// NOCHECK: nothing to do in default implementation
	}
	
	/**
	 * Do GUI related actions before the initialization task.<p>
	 * The default implementation is always empty.
	 */
	protected void onCreatePostWelcome() {
		// NOCHECK: nothing to do in default implementation
	}
	
	/**
	 * Allows to do some initialization when the welcome screen is displayed. 
	 * The initialization is done in method {@link HGBaseActivity#onCreateDuringWelcome()}.
	 * 
	 * @see HGBaseActivity#getWelcomeDialog()
	 * @see HGBaseActivity#onCreateDuringWelcome()
	 */
	private final class InitClassWithWelcomeTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			onCreatePreWelcome();
			if (firstCall) {
				welcomeDialog = getWelcomeDialog();
				if (welcomeDialog != null) {
					startTime = new Date().getTime();
					welcomeDialog.show();
				}
			}
			firstCall = false;			
		}

		@Override
		protected Void doInBackground(Void... params) {
			onCreateDuringWelcome();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (welcomeDialog != null) {
				long diff = WELCOME_MIN_MILLIS - (new Date().getTime() - startTime);
				if (diff > 0) {
					HGBaseTools.delay(diff);
				}
				welcomeDialog.dismiss();
				welcomeDialog = null;
			}
			onCreatePostWelcome();
		}
	}
	
}
