package at.hagru.hgbase.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import at.hagru.hgbase.HGBaseActivity;
import at.hagru.hgbase.R;
import at.hagru.hgbase.android.HGBaseAppTools;
import at.hagru.hgbase.android.HGBaseResources;
import at.hagru.hgbase.android.awt.Dimension;
import at.hagru.hgbase.android.awt.Polygon;
import at.hagru.hgbase.gui.menu.IMenuAction;
import at.hagru.hgbase.lib.HGBaseFileTools;
import at.hagru.hgbase.lib.HGBaseLog;
import at.hagru.hgbase.lib.HGBaseText;
import at.hagru.hgbase.lib.HGBaseTools;

/**
 * Contains some UI tools, adapted for Android.
 *
 * @author hagru
 */
public class HGBaseGuiTools {
	
	final static private float DENSITY = HGBaseAppTools.getContext().getResources().getDisplayMetrics().scaledDensity;
    final static private int DEFAULT_FIELD_HEIGHT = (int) (18 * (DENSITY + 0.75f)); //42   
    final static private int DEFAULT_BUTTON_HEIGHT = (int) (28 * (DENSITY + 0.75f)); //64
    
    private final static Map<Integer, Bitmap> imageResIdMap = new HashMap<>();    
    private final static Map<String, Bitmap> imageStringMap = new HashMap<>();    
    
    /**
     * @return the default field height (for small fields)
     */
    public static int getFieldHeight() {
        return DEFAULT_FIELD_HEIGHT;
    }

    /**
     * @return the default button height
     */
    public static int getButtonHeight() {
        return DEFAULT_BUTTON_HEIGHT;
    }
    
	/**
	 * Creates a simple text view for the given message.
	 * 
	 * @param activity the activity that is related to the view
	 * @param message the message to create the view for
	 * @return the view for the message
	 */
	public static TextView createViewForMessage(Activity activity, String message) {
        TextView view = new TextView(activity);        if (HGBaseText.existsText(message)) {
        	view.setText(HGBaseText.getText(message));
        } else {
        	view.setText(message);
        }
        return view;
	}
	
	/**
	 * Convenience method for {@link #createViewForMessage(Activity, String)}.
	 * 
	 * @param activity the activity that is related to the view
	 * @param message the message to create the view for
	 * @return the view for the message
	 */
	public static TextView createLabel(Activity activity, String message) {
		return createViewForMessage(activity, message);
	}
	
	/**
	 * Create the string array adapter for a spinner holding string elements. <p>
	 * If a selectOptionEntry is set, then an additional item is created to show the user that an option shall be selected.
	 * 
	 * @param activity the activity that holds the spinner
	 * @param array the array with values
	 * @param selectOptionEntry an optional entry to tell the user to select an option (will be the first one), may be null
	 * @return the new created array adapter
	 */
	public static SpinnerAdapter createSpinnerAdapter(Activity activity, String[] array, String selectOptionEntry) {
		if (array == null) {
			array = new String[0];
		}
		if (selectOptionEntry != null) {
			List<String> newList = new ArrayList<>(Arrays.asList(array));
			newList.add(0, selectOptionEntry);
			array = newList.toArray(new String[newList.size()]);
		}
    	ArrayAdapter<String> adapter = createArrayAdapter(activity, array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
	}
	
	/**
	 * Create a string array adapter for a list holding string elements.
	 * 
	 * @param activity the activity that holds the list
	 * @param array the array with values
	 * @return the new created array adapter
	 */
	public static ListAdapter createListAdapter(Activity activity, String[] array) {
		if (array == null) {
			array = new String[0];
		}
		ArrayAdapter<String> adapter = createArrayAdapter(activity, array, android.R.layout.simple_list_item_1);
		return adapter;
	}
	
	/**
	 * Create a button with the given text.
	 * 
	 * @param activity the activity
	 * @param text the text to display on the button
	 * @param listener the listener to react on button clicks, must not be null
	 * @return the new created button
	 */
	public static Button createButton(Activity activity, String text, OnClickListener listener) {
        Button button = new Button(activity);
        button.setText(text);
        button.setOnClickListener(listener);
        return button;
	}
	
    /**
     * Creates a button that invokes the menu action with the given id.<p>
     * The title of the button is set by a text with the same key.
     * 
     * @param activity the activity that contains the button (and the menu)
     * @param actionId the menu action id as string
     * @return the new created button or null if the action id resource does not exist
     */
    public static Button createActionButton(final HGBaseActivity activity, final String actionId) {
        final int resActionId = HGBaseResources.getResourceIdByName(actionId, HGBaseResources.ID);
        if (resActionId != 0) {
            Button btAction = createButton(activity, HGBaseText.getText(actionId), new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    IMenuAction action = activity.getOptionsMenuAction(resActionId);
                    if (action == null) {
                    	HGBaseLog.logError("Action is not defined: " + actionId);
                    } else {
                    	action.perform(resActionId, null);
                    }
                }
            });
            return btAction;
        } else {
            return null;
        }
    }

	
	
	/**
	 * Create a check box with the given text.
	 * 
	 * @param activity the activity
	 * @param text the text to display for the check box
	 * @param listener an optional click listener, may be null
	 * @return the new created check box
	 */
	public static CheckBox createCheckBox(Activity activity, String text, OnClickListener listener) {
		CheckBox chkBox = new CheckBox(activity);
		chkBox.setText(text);
		if (listener != null) {
			chkBox.setOnClickListener(listener);
		}
		return chkBox;
	}
	
	/**
	 * Creates a scroll view that is stretched to its boundaries.
	 *  
	 * @param activity the activity
	 * @return the new created scroll view
	 */
	public static ScrollView createScrollView(Activity activity) {
	    ScrollView scroll = new ScrollView(activity);
	    scroll.setFillViewport(true);
        scroll.setScrollbarFadingEnabled(true);        
	    return scroll;
	}
	
	/**
	 * Create a linear layout and set the orientation.
	 * 
	 * @param activity the activity
	 * @param horizontal true for horizontal orientation (left-to-right), false for vertical one (top-to-bottom)
	 * @return the new created layout
	 */
	public static LinearLayout createLinearLayout(Activity activity, boolean horizontal) {
		LinearLayout newLayout = new LinearLayout(activity);
		newLayout.setOrientation((horizontal) ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
		return newLayout;
	}
	
	/**
	 * Convenience method to create layout parameters for linear layout.
	 * 
	 * @param fullWidth true to stretch the view to full width, otherwise to required size
	 * @param fullHeight true to stretch the view to full height, otherwise to required size
	 * @return the new created layout parameters
	 */
	public static LayoutParams createLinearLayoutParams(boolean fullWidth, boolean fullHeight) {
		int width = (fullWidth) ? ViewGroup.LayoutParams.MATCH_PARENT
								: ViewGroup.LayoutParams.WRAP_CONTENT;
		int height = (fullHeight) ? ViewGroup.LayoutParams.MATCH_PARENT
								  : ViewGroup.LayoutParams.WRAP_CONTENT;
		return new LayoutParams(width, height);
	}
	
	/**
	 * Convenience method to create layout parameters for viewgroup layout.
	 * 
	 * @param fullWidth true to stretch the view to full width, otherwise to required size
	 * @param fullHeight true to stretch the view to full height, otherwise to required size
	 * @return the new created layout parameters
	 */
	public static ViewGroup.LayoutParams createViewGroupLayoutParams(boolean fullWidth, boolean fullHeight) {
		int width = (fullWidth) ? ViewGroup.LayoutParams.MATCH_PARENT
								: ViewGroup.LayoutParams.WRAP_CONTENT;
		int height = (fullHeight) ? ViewGroup.LayoutParams.MATCH_PARENT
								  : ViewGroup.LayoutParams.WRAP_CONTENT;
		return new ViewGroup.LayoutParams(width, height);
	}
	
	/**
	 * Creates an array adapter to be used for filling UI elements with the given string array.
	 * 
	 * @param activity the activity that holds the UI element
	 * @param array the string array to create an adapter for
	 * @param resId the resource id of the item for the adapter
	 * @return a new array adapter, may be empty
	 */
	public static <T> ArrayAdapter<T> createArrayAdapter(Activity activity, T[] array, int resId) {
		if (array == null) {
			// it would be preferable to create an array of T with size 0, but this is not possible with Java
			return new ArrayAdapter<T>(activity, resId, array);
		} else {
			return new ArrayAdapter<T>(activity, resId, array);
		}
	}
	
	/**
	 * Creates a number picker for the defined range.<p>
	 * Node: If diff is set greater than 1 then the value has to be multiplied with this factor.
	 * 
	 * @param activity the activity
     * @param min lower number limit
     * @param max upper number limit
     * @param diff difference between numbers
	 * @param value the current value for the slider
	 * @return a number picker
	 */
	public static NumberPicker createRangeNumberPicker(Activity activity, int min, int max, final int diff, int value) {
		NumberPicker picker = new NumberPicker(activity);
		picker.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		picker.setMinValue(min / diff);
		picker.setMaxValue(max / diff);
		picker.setValue(value / diff);
		if (diff != 1) {
			picker.setFormatter(new NumberPicker.Formatter() {
				
				@Override
				public String format(int value) {
					return String.valueOf(value * diff);
				}
			});
		}
		return picker;
	}
	
    /**
	 * @param tabbedPane the tab pane to add the view
	 * @param panel the view to show on the tab
	 * @param tabId the tab id, which is also the message key for the title
	 */
	public static void addViewToTab(TabHost tabbedPane, final View panel, String tabId) {
		TabSpec playerTab = tabbedPane.newTabSpec(tabId);
		playerTab.setIndicator(HGBaseText.getText(tabId));
		playerTab.setContent(new TabHost.TabContentFactory(){
		    @Override
			public View createTabContent(String tag) {                                   
		        return panel;
		    }       
		});
		tabbedPane.addTab(playerTab);
	}	
  
    /**
     * Returns the size of the current screen.
     *
     * @param activity the activity to get the screen size for, must not be null
     * @return the size of the current screen
     */
    public static Point getScreenSize(Activity activity) {
	return getScreenSize(activity, false);
    }

    /**
     * Return the size of the full screen.
     *
     * @param activity the activity to get the screen size for, must not be null 
     * @return the size of the full screen
     */
    public static Point getFullscreenSize(Activity activity) {
	return getScreenSize(activity, true);
    }

    /**
     * Returns the size of the current screen.
     *
     * @param activity the activity to get the screen size for, must not be null
     * @param fullscreen if {@code true} the size of the full screen is returned, if {@code false} the size of the available screen (full screen without the navigation area).
     * @return the size of the current screen
     */
    public static Point getScreenSize(Activity activity, boolean fullscreen) {
    	if (activity == null) {
    		activity = HGBaseActivity.getInstance();
    	}
    	WindowManager wm = activity.getWindowManager();
    	Display display = wm.getDefaultDisplay();
    	Point size = new Point();
    	if (fullscreen) {
    	    display.getRealSize(size);
    	} else {
    	    display.getSize(size);
    	}
        return size;
    }
    
    /**
     * Returns the current screen orientation.
     * 
     * @param activity the current activity
     * @return either {@link Configuration#ORIENTATION_PORTRAIT} or {@link Configuration#ORIENTATION_LANDSCAPE}
     */
    public static int getScreenOrientation(Activity activity) {
    	return activity.getResources().getConfiguration().orientation;   	
    }
    
    /**
     * @param activity the current activity
     * @return true if the screen orientation is portrait
     */
    public static boolean isScreenPortrait(Activity activity) {
    	return (getScreenOrientation(activity) == Configuration.ORIENTATION_PORTRAIT);
    }
    
    /**
     * @param activity the current activity
     * @return true if the screen orientation is landscape
     */
    public static boolean isScreenLandscape(Activity activity) {
    	return (getScreenOrientation(activity) == Configuration.ORIENTATION_LANDSCAPE);
    }
    
    /**
     * Sets the size of a view.<p>
     * NOTE: Does not work properly if the layout parameters are new {@link ViewGroup.LayoutParams}.
     * 
     * @param view the view to set the size for
     * @param size the size to set
     */
    public static void setViewSize(View view, Dimension size) {
    	ViewGroup.LayoutParams lp = view.getLayoutParams();
    	if (lp == null) {
    		view.setLayoutParams(new ViewGroup.LayoutParams(size.width, size.height));
    	} else {
        	lp.width = size.width;
        	lp.height = size.height;
        	view.requestLayout();
    	}
    	view.setMinimumWidth(size.width);
    	view.setMinimumHeight(size.height);
    }
    
    /**
     * Returns the size of the given view.<p>
     * NOTE: It seems that this method does not work quite well.
     * 
     * @param view the view to get the size for, must not be null
     * @return the size of the given view
     */
    public static Dimension getViewSize(View view) {
    	return new Dimension(view.getWidth(), view.getHeight());
    }
    
    /**
     * Returns the width of the drawable object.
     * 
     * @param d the drawable object, must not be null
     * @return the width of the drawable object
     */
    public static int getWidth(Drawable d) {
    	return d.getBounds().width();
    }
    
    /**
     * Returns the height of the drawable object.
     * 
     * @param d the drawable object, must not be null
     * @return the height of the drawable object
     */
    public static int getHeight(Drawable d) {
    	return d.getBounds().height();
    }
    
    /**
     * Loads an image given by an resource id.
     *
     * @param resId the resource id of the image
     * @return the image or null if resource is invalid
     */
    public static Bitmap loadImage(int resId) {
    	Bitmap img = imageResIdMap.get(Integer.valueOf(resId));
    	if (img != null) {
    		return img;
    	} else {
	        try {
	            img = HGBaseResources.getBitmap(resId);
	            if (img != null) {
	            	imageResIdMap.put(Integer.valueOf(resId), img);
	            }
	            return img;
	        } catch (NotFoundException e) {
	            return null;
	        }
    	}
    }
    
    /**
     * Loads an image given by an (relative) image path.<p>
     * First the drawables folder is searched, then the assets folder (only there sub-folders are possible) and finally 
     * from external file. 
     * 
     * @param imagePath the path to the image (must be in folder drawable)
     * @return the image or null if path is invalid
     */
    public static Bitmap loadImage(String imagePath) {
    	int resId = HGBaseResources.getResourceIdByName(imagePath, HGBaseResources.DRAWABLE);
    	if (resId > 0) {
    		return loadImage(resId);
    	} else {
    		Bitmap img = imageStringMap.get(imagePath);
    		if (img == null) {
				img = BitmapFactory.decodeStream(HGBaseFileTools.openAssetsFileStream(imagePath));
				if (img != null) {
					imageStringMap.put(imagePath, img);
	    		} else {
					img = BitmapFactory.decodeFile(imagePath);
					if (img != null) {
						imageStringMap.put(imagePath, img);
					}
	    		}
    		}
			return img;
    	}
    }

	/**
	 * Loads an image from a file name.
	 * 
	 * @param imagePath the path of the image file
	 * @return the image or null if not found
	 */
    public static Bitmap loadImageFromFile(String imagePath) {
    	return BitmapFactory.decodeFile(imagePath);
    }
    
    /**
     * Takes a screenshot from the activity and returns a bitmap.
     * 
     * @param activity the activity to take the screenshot from
     * @return the bitmap with the screenshot
     */
    public static Bitmap takeScreenshot(Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);			
    	return bitmap;
    }
    
    /**
     * Sets the text on a label in a thread-safe way.<p>
     * As it is only allowed to modify the label from the same thread that has created the view, 
     * a helper method is needed to set the text on a label from another view.
     * 
     * @param label the label to modify
     * @param text the text to set
     */
    public static void setTextOnLabel(final TextView label, final CharSequence text) {
        if (label != null) {
            boolean success = label.post(new Runnable() {

                @Override
                public void run() {
                    label.setText(text);
                }
            });
            if (!success) {
            	HGBaseLog.logWarn("Could not set text '" + text + "' on label " + label);
            }
        }        
    }
    
    /**
     * Puts a image on the given label and scales it if necessary.
     * If the image is too big it's scaled to the width/height of the label.
     *
     * @param label The label.
     * @param image The image to display, can be null.
     */
    public static void setImageOnLabel(TextView label, Bitmap image) {
        if (label!=null) {
            setImageOnLabel(label, image, label.getWidth(), label.getHeight());
        }
    }

    /**
     * Puts a image on the given label and scales it if necessary.
     * If the image is too big it's scaled to the given width/height.
     * Use this, if the size of the label is unknown when setting the image on it or
     * if you want to place a text beside the image.
     *
     * @param label The label.
     * @param image The image to display, can be null.
     * @param width The maximal width of the result image.
     * @param height The maximal height of the result image.
     */
    public static void setImageOnLabel(final TextView label, final Bitmap image, final int width, final int height) {
        if (label != null) {
            setTextOnLabel(label, createStringForImage(label.getContext(), image, width, height));
        }
    } 
    
    /**
     * Create the string for a text view that will show the given image.
     * 
     * @param context the application context related to the text view
     * @param image the image to display, may be null
     * @param width The maximal width of the result image.
     * @param height The maximal height of the result image.
     * @return the text for displaying the image, may be empty
     */
    public static synchronized CharSequence createStringForImage(Context context, Bitmap image, int width, int height) {
        if (image == null) {
            return "";
        } else {
        	image = getScaledBitmap(image, width, height);
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(" ");
            sb.setSpan(new ImageSpan(context, image), sb.length() - 1, sb.length(), 0);
            return sb;
        }
    }
    
    /**
     * Return a scaled bitmap if necessary or the original one.
     * 
     * @param image the image to be displayed, must not be null
     * @param width the target width
     * @param height the target height
     * @return the scaled bitmap or the original one
     */
    public static Bitmap getScaledBitmap(Bitmap image, int width, int height) {
        double lw = (width - 6) * 1.0;  // was - 2, but text has an automatic space to top
        double lh = (height - 6) * 1.0; // was - 2
        double iw = image.getWidth() * 1.0;
        double ih = image.getHeight() * 1.0;
        if ((lw > 0 && lw < iw) || (lh > 0 && lh < ih)) {
            double factorW = lw / iw;
            double factorH = lh / ih;
            double f = (factorW < factorH) ? factorW : factorH;
            return Bitmap.createScaledBitmap(image, (int) (iw * f), (int) (ih * f), true);
        } else {
        	return image;
        }
    }
    
    /**
     * Sets a tool tip for the given view that is displayed by a long click on the view.
     *  
     * @param view the view to set the tool tip
     * @param text the text for the tool tip (id or plain text), null or empty string for removing the tool tip
     */
    public static void setToolTipText(View view, String text) {
    	if (HGBaseTools.hasContent(text)) {
    		final String tooltipText = (HGBaseText.existsText(text)) ? HGBaseText.getText(text) : text;
    		view.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					HGBaseAppTools.showToolTip(tooltipText, v.getContext());
					return true;
				}
			});
    	} else {
    		view.setOnLongClickListener(null);
    		view.setLongClickable(false);
    	}
    }
    
    /**
     * Removes a view from its parent.
     * 
     * @param view the view to remove
     * @return true if the view was attached to a parent and by that could be removed
     */
    public static boolean removeViewFromParent(View view) {
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
            return true;
        } else {
            return false;
        }        
    }
    
    /**
     * Checks if the given view has any (direct or indirect) parent of the given type.
     * 
     * @param view the view to test
     * @param viewType the type for that the parent is tested
     * @return true if any parent of the given view has the given type
     */
    public static boolean hasViewAnyParentOfType(View view, Class<? extends View> viewType) {
    	ViewParent parent = view.getParent();
    	if (parent == null || !(parent instanceof View)) {
    		return false;
    	} else if (parent.getClass().equals(viewType)) {
    		return true;
    	} else {
    		return hasViewAnyParentOfType((View) parent, viewType);
    	}
    }
    
    /**
     * Checks if the given view has any (direct or indirect) parent of the given type.
     * 
     * @param view the view to test
     * @param typeName the simple class name for that the parent is tested
     * @return true if any parent of the given view has the given type
     */
    public static boolean hasViewAnyParentOfType(View view, String className) {
    	ViewParent parent = view.getParent();
    	if (parent == null || !(parent instanceof View)) {
    		return false;
    	} else if (parent.getClass().getSimpleName().equals(className)) {
    		return true;
    	} else {
    		return hasViewAnyParentOfType((View) parent, className);
    	}
    }
    
    /**
     * Returns the first view of the given type by going recursively through the view hierarchy, 
     * this could also be the given root view.<p>
     * 
     * @param rootView the root view to the search
     * @param viewType the type of the view to get the same type
     * @return a view of the given type or null
     */
    public static View findFirstViewOfType(View rootView, final Class<? extends View> viewType) {
        return findFirstViewOfType(rootView, new ClassTypeChecker<View>() {
			
			@Override
			public boolean isCorrectType(Class<?> classType, View checkObject) {
				return classType.equals(viewType);
			}
		});
    }
    
    /**
     * Returns the first view of the given type by going recursively through the view hierarchy, 
     * this could also be the given root view.<p>
     * 
     * @param rootView the root view to the search
     * @param typeChecker a type checker that allows more detailed checks for the class type
     * @return a view of the given type or null
     */
    public static View findFirstViewOfType(View rootView, ClassTypeChecker<View> typeChecker) {
        if (rootView != null && typeChecker.isCorrectType(rootView.getClass(), rootView)) {
            return rootView;
        }
        if (rootView instanceof ViewGroup) {
            ViewGroup rootGroup = (ViewGroup) rootView;
            int childCount = rootGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
            	View childView = rootGroup.getChildAt(i);
                View foundView = findFirstViewOfType(childView, typeChecker);
                if (foundView != null) {
                    return foundView;
                }
            }
        }
        return null;
    }
    
    /**
     * Checks whether the given class is of correct type.
     * 
     * @param T the type of the object to check
     */
    public static interface ClassTypeChecker<T> {
    	
    	public boolean isCorrectType(Class<?> classType, T checkObject);    	
    }
    
    /**
     * Creates a new polygon from the given point array.
     * 
     * @param positions the points to define the polygon
     * @return the new created polygon
     */
    public static Polygon createPolygon(Point[] positions) {
        int[] x = new int[positions.length];
        int[] y = new int[positions.length];
        for (int i = 0; i < positions.length; i++) {
            x[i] = positions[i].x;
            y[i] = positions[i].y;
        }
        return new Polygon(x, y, positions.length);
    }    

    /**
     * Checks whether a given point is within a polygon specified by an array of points.
     *
     * @param point the point the test
     * @param polygon the polygon defined by an arbitrary number of points
     * @return true if the point is within the polygon, otherwise false
     */
    public static boolean isPointInPolygon(Point point, Point[] polygon) {
        return createPolygon(polygon).contains(point);
    }

    /**
     * Returns the center of the given polygon.
     *
     * @param polygon an arbitrary number of points
     * @return the center or null if the polygon is null or has size 0
     */
    public static Point getCenterOfPolygon(Point ... polygon) {
        if (polygon == null || polygon.length == 0) {
            return null;
        } else  if (polygon.length == 1) {
            return polygon[0];
        } else  if (polygon.length == 2) {
            int leftX = Math.min(polygon[0].x, polygon[1].x);
            int rightX = Math.max(polygon[0].x, polygon[1].x);
            int centerX = leftX + (rightX - leftX + 1) / 2;
            int topY = Math.min(polygon[0].y, polygon[1].y);
            int bottomY = Math.max(polygon[0].y, polygon[1].y);
            int centerY = topY + (bottomY - topY + 1) / 2;
            return new Point(centerX, centerY);
        } else {
            double x = 0.;
            double y = 0.;
            for (int i = 0; i < polygon.length; i++){
                x += polygon[i].x;
                y += polygon[i].y;
            }
            x = x / polygon.length;
            y = y / polygon.length;
            return new Point((int) x, (int) y);
        }
    }
        
    /**
     * Sets the given view enabled/disabled in a null-safe way.
     *
     * @param v the view to enable/disable, may be null
     * @param enable true to enable, false to disable
     */
    public static void setEnabledNullSafe(View v, boolean enable) {
        if (v != null) {
            v.setEnabled(enable);
        }
    }

    /**
     * Returns a set of components with the given state (i.e. enabled or disabled). It looks for all
     * components that are children (direct or indirect) of the given parent including the parent itself.
     *
     * @param parent the parent to look for components
     * @param enabled true to look for enabled components, false to look for disabled ones
     * @return a (weak) set of components having the given activity state
     */
    public static Set<View> getAllCompenentsByEnabledState(View parent, final boolean enabled) {

        final Set<View> components = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
        new HierarchyWalker() {

            @Override
            protected void perform(View component) {
                if (component.isEnabled() == enabled) {
                    components.add(component);
                }
            }
        }.walk(parent);
        return components;
    }

     /**
      * Enable all views starting with the given parent, except the further passed components.<p>
      * It can be useful to store all previously disabled views before calling this method.
      *
      * @param parent the parent to start to enable/disable
      * @param enable true to enable all component, false to disable
      * @param except an arbitrary number of views that shall not be enabled
      * @see #getAllCompenentsByEnabledState(View, boolean)
      */
     public static void setEnabledAllComponents(View parent, boolean enable, View...except) {
         new EnableDisableWalker(enable, except).walk(parent);
     }
     
     /**
      * Returns all items of the given menu with the given enabled state.
      * 
      * @param menu the menu to get the items for, must not be null
      * @param enabled true to get all enabled items, false to get all disabled ones
      * @return a set with all menu items of the given state, may be empty
      */
     public static Set<MenuItem> getAllMenuItemsByEnabledState(Menu menu, boolean enabled) {
    	 Set<MenuItem> itemSet = new LinkedHashSet<>();
  		for (int i = 0; i < menu.size(); i++) {
 			MenuItem item = menu.getItem(i);
 			if (item.isEnabled() == enabled) {
 				itemSet.add(item);
 			}
  		}
  		return itemSet;
     }
     
     /**
      * Enable/disable all menu items of the given menu.
      * 
      * @param menu the menu to enable/disable the items
      * @param enable true to enable, false to disable
      * @param except an arbitrary list of items that shall not be affacted
      */
     public static void setEnabledAllMenuItems(Menu menu, boolean enable, MenuItem...except) {
    	 Set<MenuItem> exceptSet = new HashSet<>(Arrays.asList(except));
 		for (int i = 0; i < menu.size(); i++) {
 			MenuItem item = menu.getItem(i);
 			if (!exceptSet.contains(item)) {
 				item.setEnabled(enable);
 			}
		}    	 
     }

     /**
      * Component hierarchy walker that enable or disables Views.
      */
     private static class EnableDisableWalker extends HierarchyWalker {

         private final boolean enable;
         private final Set<View> exceptSet;

        /**
          * @param enable true to enable all component, false to disable them
      * @param except an arbitrary number of views that shall not be disabled
          */
         public EnableDisableWalker(boolean enable, View...except) {
             this.enable = enable;
             this.exceptSet = new HashSet<>(Arrays.asList(except));
         }

        @Override
        protected void perform(View component) {
            if (!exceptSet.contains(component)) {
                component.setEnabled(enable);
            }
        }
     }

     /**
      * Walks to the given view hierarchy and does the implemented action.
      */
     public abstract static class HierarchyWalker {

         /**
          * Start walking through the hierarchy.
          *
          * @param parent the parent to start, must not be null
          */
         public final void walk(View parent) {
             perform(parent);
             if (parent instanceof ViewGroup) {
            	 ViewGroup vg = (ViewGroup) parent;
            	 for (int i = 0; i < vg.getChildCount(); i++) {
            		 walk(vg.getChildAt(i));
            	 }
             }
         }

         /**
          * Do the action with the given view.
          *
          * @param view the view to use for an action
          */
         protected abstract void perform(View view);
     }

    /**
     * Programmatically hide the drop down of a spinner.
     * 
     * @param spinner the spinner to hide the drop down for
     */
    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        	HGBaseLog.logError("Could not hide spinner drop down " + e.getMessage());
        }
    }

    /**
     * Sets a black border around the given view.
     * 
     * @param view
     */
    public static void setBlackBorder(View view) {
    	view.setBackground(HGBaseResources.getDrawable(R.drawable.border));
    }
    
    /**
     * Changes the size of the given dialog to full screen.<p>
     * NOTE: You need a view that is able to resize to full screen (e.g. just a dummy View). It will not work for the 
     * default dialogs created by {@link HGBaseDialog}.
     * 
     * @param activity the activity to get the screen size from
     * @param dialog the dialog to change the size, must not be null
     */
    public static void setSizeToFullScreen(Activity activity, Dialog dialog) {
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}