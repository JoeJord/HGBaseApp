package at.hagru.hgbase.android.view;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A subclass of the array adapter that works with {@link HGBaseAdapterFilter}.
 * 
 * @author hagru
 */
public class HGBaseAdapter<T> extends ArrayAdapter<T> {
	
	private final List<T> originalItems;
	private List<T> currentItems;
	
	public HGBaseAdapter(Context context, int resource, List<T> objects) {
		this(context, resource, 0, objects);
	}

	public HGBaseAdapter(Context context, int resource, int textViewResourceId, List<T> objects) {
		super(context, resource, textViewResourceId, objects);
		originalItems = Collections.unmodifiableList(objects);
		currentItems = new ArrayList<T>(objects);
	}

	public HGBaseAdapter(Context context, int resource, T[] objects) {
		this(context, resource, 0, objects);
	}
	
	public HGBaseAdapter(Context context, int resource, int textViewResourceId, T[] objects) {
		super(context, resource, textViewResourceId, objects);
		originalItems = Arrays.asList(objects);
		currentItems = Arrays.asList(objects);
	}
	
	@Override
	public T getItem(int position) {
		return currentItems.get(position);
	}
	
	@Override
	public int getCount() {
		return currentItems.size();
	}
	
	@Override
	public int getPosition(T item) {
		int index = 0;
		for (T ci : currentItems) {
			if (ci.equals(item)) {
				return index;
			}
			index++;
		}
		return -1;
	}
	
	/**
	 * Sets the current items. This allows the filter to change the displayed items.
	 * 
	 * @param objects the items to be displayed by the adapter
	 */
	public void setItems(List<T> objects) {
		this.currentItems = objects;
	}
	
	/**
	 * @return the original provided items
	 */
	public List<T> getOriginalItems() {
		return originalItems;
	}

}
