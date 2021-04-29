package at.hagru.hgbase.gui.menu.actions;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.gui.menu.IMenuAction;

/**
 * A default action class for menu.
 *
 * @author hagru
 */
public abstract class AbstractMenuAction implements IMenuAction {

    private HGBaseActivity activity;

    /**
     * Create a new menu actions that has access to the activity that shows the menu.
     * 
     * @param menuActivity
     */
    protected AbstractMenuAction(HGBaseActivity activity) {
        this.activity = activity;
    }

    /**
     * Returns the activity given by constructor.
     *
     * @return the activity that shows the menu
     */
    public HGBaseActivity getActivity() {
        return this.activity;
    }

}
