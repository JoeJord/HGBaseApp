package at.hagru.hgbase.gui.menu;

import android.view.MenuItem;

/**
 * A menu action to be registered in the activity that handles the particular menu.
 *
 * @author hagru
 */
public interface IMenuAction {

    /**
     * Perform a menu action.
     *
     * @param id the action id
     * @param item the menu item that was selected, may be null if this method is called by hand
     */
    void perform(int id, MenuItem item);

}
