package at.hagru.testapp;

import android.view.MenuItem;

import at.hagru.hgbase.HGBaseWelcomeActivity;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.activity.SimpleProgressAsyncTask;
import at.hagru.hgbase.gui.HGBaseDialog;
import at.hagru.hgbase.gui.HGBaseGuiTools;
import at.hagru.hgbase.gui.HGBaseStatusBar;
import at.hagru.hgbase.gui.ProgressState;
import at.hagru.hgbase.gui.menu.IMenuAction;
import at.hagru.hgbase.gui.menu.actions.StartConfigDialogAction;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseTools;

public class MainActivity extends HGBaseWelcomeActivity {
    /**
     * Create the main test app activity.
     */
    public MainActivity() {
        super(R.layout.activity_main, R.menu.main);
    }

    @Override
    protected void onCreateDuringWelcome() {
        // wait to let the welcome dialog show
        HGBaseTools.sleep(2000);
    }

    @Override
    protected void onCreatePostWelcome() {
        setStatusBar(true);
        HGBaseStatusBar status = new HGBaseStatusBar(this, new int[]{50, 0, 50, 100});
        setStatusBar(status);
        status.setText(0, "A");
        status.setText(1, "User: " + HGBaseAppTools.getUserName());
        status.setImage(2, HGBaseGuiTools.loadImage(R.drawable.ic_launcher));
        status.setProgressPanel(3);
    }

    @Override
    protected void registerOptionsMenuActions() {
        registerOptionsMenuAction(R.id.action_start_progress, new StartProgressAction());
        registerOptionsMenuAction(R.id.color_dialog, new ColorPickerAction());
        registerOptionsMenuAction(R.id.config_dialog, new StartConfigDialogAction(this, TestAppConfigDialog.class));
    }

    /**
     * Starts the progress to set a waiting state for a particular time.
     */
    private class StartProgressAction implements IMenuAction {

        private static final long TIME_TO_WAIT = 3000;

        @Override
        public void perform(int id, MenuItem item) {
            new SimpleProgressAsyncTask(MainActivity.this) {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    HGBaseLog.logDebug("Start progress...");
                    setStatusText("Start progress...");
                    setStatusProgress(ProgressState.STATE_WAITING);
                }

                @Override
                protected Void doInBackground(Void... params) {
                    HGBaseTools.delay(TIME_TO_WAIT);
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    setStatusText("");
                    HGBaseLog.logDebug("Progress finished.");
                    setStatusProgress(ProgressState.STATE_FINISHED);
                }

            }.execute();
        }

    }

    /**
     * Displays the color picker dialog and prints out the selected color.
     */
    private class ColorPickerAction implements IMenuAction {

        @Override
        public void perform(int id, MenuItem item) {
            HGBaseDialog.showColorDialog(MainActivity.this, null, color -> HGBaseLog.logDebug("Color: " + color));
        }
    }

}
