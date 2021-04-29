package at.hagru.hgbase.android.view;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple filter for adapters that require only to overwrite one filter method.
 * 
 * @author hagru
 */
public abstract class HGBaseAdapterFilter<T> extends Filter {
	
	private final HGBaseAdapter<T> adapter;
	private String activeFilter = "";

	public HGBaseAdapterFilter(HGBaseAdapter<T> adapter) {
		this.adapter = adapter;
	}
	
	/**
	 * @return the active filter, may be an empty string if no filter is set
	 */
	public String getActiveFilter() {
		return activeFilter;
	}
	
	/**
	 * Checks whether the given item shall be filtered by the active filter.
	 * 
	 * @param item the item to test
	 * @param filter the filter to use, may be an empty string
	 * @return true if the item shall be filtered, false if not
	 */
	abstract protected boolean filterItem(T item, String filter);
	
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
    	activeFilter = (constraint == null)? "" : constraint.toString();
        FilterResults results = new FilterResults();
        final List<T> newList = new ArrayList<>();
        for (T item : adapter.getOriginalItems()) {
            if (!filterItem(item, activeFilter)) {
            	newList.add(item);
            }
        }
        results.values = newList;
        results.count = newList.size();
        return results;
    }

    @SuppressWarnings("unchecked")
	@Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
    	adapter.setItems((List<T>) results.values);
    	adapter.notifyDataSetChanged();
    }

}