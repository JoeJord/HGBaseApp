package at.hagru.hgbase.lib;

import android.util.Pair;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Contains some tools
 *
 * @author Harald Gruber
 * @version 1.0
 */
public final class HGBaseTools {

    public static final int INVALID_INT = Integer.MIN_VALUE + 17;
    public static final float INVALID_FLOAT = -17.00017f;
    public static final double INVALID_DOUBLE = -17.00017;
    public static final long INVALID_LONG = Long.MIN_VALUE + 17L;

    private static final float DIFF_FLOAT = 0.00001f;
    private static final float DIFF_DOUBLE = 0.00001f;

    private static long lastTime = -1;

    private HGBaseTools() {
        super();
    }

    /**
     * Returns true if the value is even (0, 2, 4, ..).
     *
     * @param value the value to test
     * @return true if the value is odd, false if the value is even
     */
    public static boolean isEven(int value) {
        return (value % 2 == 0);
    }

    /**
     * Returns true if the value is odd (-1, 1, 3, 5, ..).
     *
     * @param value the value to test
     * @return true if the value is odd, false if the value is even
     */
    public static boolean isOdd(int value) {
        return !isEven(value);
    }

    /**
     * Checks whether the given float value is valid.
     *
     * @param value the float value to test
     * @return true if the float is valid, otherwise false
     */
    public static boolean isValid(float value) {
        return !isEqual(value, INVALID_FLOAT);
    }

    /**
     * Checks whether the given double value is valid.
     *
     * @param value the double value to test
     * @return true if the double is valid, otherwise false
     */
    public static boolean isValid(double value) {
        return !isEqual(value, INVALID_DOUBLE);
    }

    /**
     * Checks whether the given integer value is valid.
     *
     * @param value the integer value to test
     * @return true if the integer is valid, otherwise false
     */
    public static boolean isValid(int value) {
        return (value != INVALID_INT);
    }

    /**
     * Converts a String to int.
     *
     * @param s string to be converted
     * @return int, in case of error <code>INVALID_INT</code>.
     */
    public static int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return INVALID_INT;
        }
    }

    /**
     * Converts a String to int.
     *
     * @param s            string to be converted
     * @param defaultValue a default value if the string is invalid
     * @return int, in case of error the default value.
     */
    public static int toInt(String s, int defaultValue) {
        int value = toInt(s);
        return (value == INVALID_INT) ? defaultValue : value;
    }

    /**
     * converts a boolean to int.
     *
     * @param b boolean to be converted
     * @return int is 0 (<code>false</code>) or 1 (<code>true</code>)
     */
    public static int toInt(boolean b) {
        return (b) ? 1 : 0;
    }

    /**
     * Converts a String to float.
     *
     * @param s string to be converted
     * @return float, in case of error <code>INVALID_FLOAT</code>.
     */
    public static float toFloat(String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            return INVALID_FLOAT;
        }
    }

    /**
     * Converts a String to double.
     *
     * @param s string to be converted
     * @return double, in case of error <code>INVALID_DOUBLE</code>.
     */
    public static double toDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return INVALID_DOUBLE;
        }
    }

    /**
     * Converts a String to long.
     *
     * @param s string to be converted
     * @return long, in case of error <code>INVALID_LONG</code>.
     */
    public static long toLong(String s) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return INVALID_LONG;
        }
    }

    /**
     * Converts a String to long.
     *
     * @param s            string to be converted
     * @param defaultValue a default value if the string is invalid
     * @return long, in case of error the default value.
     */
    public static long toLong(String s, long defaultValue) {
        long value = toLong(s);
        return (value == INVALID_LONG) ? defaultValue : value;
    }

    /**
     * Converts a String to double.
     *
     * @param s            string to be converted
     * @param defaultValue a default value if the string is invalid
     * @return double, in case of error the default value.
     */
    public static double toDouble(String s, double defaultValue) {
        double value = toDouble(s);
        return (isEqual(value, INVALID_DOUBLE)) ? defaultValue : value;
    }

    /**
     * Compares two floats for equality using a default difference between those values.
     *
     * @param f1 the first float
     * @param f2 the second float
     * @return true if there difference is so small that they seem to be equal
     */
    public static boolean isEqual(float f1, float f2) {
        return Math.abs(f1 - f2) < DIFF_FLOAT;
    }

    /**
     * Compares two doubles for equality using a default difference between those values.
     *
     * @param d1 the first double
     * @param d2 the second double
     * @return true if there difference is so small that they seem to be equal
     */
    public static boolean isEqual(double d1, double d2) {
        return Math.abs(d1 - d2) < DIFF_DOUBLE;
    }

    /**
     * Checks the condition and throws an {@link IllegalStateException} with the given error message if
     * the check fails.
     *
     * @param condition    the condition to check
     * @param errorMessage the error message for the exception
     * @throws IllegalStateException if the condition check fails
     */
    public static void checkCondition(boolean condition, String errorMessage) {
        if (!condition) {
            throw new IllegalStateException(errorMessage);
        }
    }

    /**
     * Throws an exception if the passed parameter is null.
     *
     * @param objToTest    the object to test for null
     * @param errorMessage the error message for the exception
     */
    public static <T> T requireNonNull(T objToTest, String errorMessage) {
        if (objToTest == null) {
            throw new NullPointerException(errorMessage);
        } else {
            return objToTest;
        }
    }

    /**
     * Transforms the value into a boolean value.
     *
     * @param value a string value, possibly representing a boolean.
     * @return <code>true</code> if the value is a number greater 0 or has a
     * text like "true" or "yes".
     */
    public static boolean toBoolean(String value) {
        return HGBaseTools.toInt(value) > 0 || "true".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value);
    }

    /**
     * Returns a string with the message and the stack trace of the exception.
     *
     * @param t the throwable object, may be null
     * @return the string with the stack trace
     */
    public static String toString(Throwable t) {
        if (t == null) {
            return "";
        } else {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            t.printStackTrace(printWriter);
            return toString(t.getMessage()) + "\n" + writer;
        }
    }

    /**
     * Returns the string of an object in a null-safe way.
     *
     * @param o the object to return the string for, may be null
     * @return the string, can be empty if o is null
     */
    public static String toString(Object o) {
        return (o == null) ? "" : o.toString();
    }

    /**
     * Returns the system specific carriage return/line feed string.
     *
     * @return the system specific carriage return/line feed string
     */
    public static String getCRLF() {
        return System.getProperty("line.separator");
    }

    /**
     * Clones an array in a null-safe way.
     *
     * @param o the array to clone, may be null
     * @return a copy of the array or null if o is null
     */
    public static <T> T[] clone(T[] o) {
        return (o == null) ? null : o.clone();
    }

    /**
     * Clones an int-array in a null-safe way. Need special handling for basic data types.
     *
     * @param o the array to clone, may be null
     * @return a copy of the array or null if o is null
     */
    public static int[] clone(int[] o) {
        return (o == null) ? null : o.clone();
    }

    /**
     * Clones an object in a null-safe way.
     *
     * @param o the object to clone, may be null
     * @return a clone of the object or null if o is null
     * @throws UnsupportedOperationException if clone method is not public
     * @throws CloneNotSupportedException    if clone method throws this exception
     */
    @SuppressWarnings("unchecked")
    public static <T extends Cloneable> T clone(T o) {
        if (o == null) {
            return null;
        } else {
            try {
                Method cloneMethod = o.getClass().getMethod("clone");
                return (T) cloneMethod.invoke(o);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException |
                     IllegalArgumentException | InvocationTargetException e) {
                throw new UnsupportedOperationException(e);
            }
        }
    }

    /**
     * Orders a given list, depending of the lists element compare-method
     *
     * @param list array with comparable objects that shall be sorted
     */
    public static void orderList(Comparable<?>[] list) {
        orderList(list, false);
    }

    /**
     * Orders a given list, depending of the lists element compare-method
     *
     * @param list    array with comparable objects that shall be sorted
     * @param inverse <code>true</code> to get the inverse order.
     */
    public static void orderList(Comparable<?>[] list, boolean inverse) {
        if (list != null && list.length > 1) {
            if (inverse) {
                Arrays.sort(list, Collections.reverseOrder());
            } else {
                Arrays.sort(list);
            }
        }
    }

    /**
     * Returns the value for the given attribute. If the value does not exist, the default
     * value is added to the map and returned.
     *
     * @param map          the map to get the value from
     * @param key          the key to get the value for
     * @param defaultValue the default value if there is no entry for the key
     * @return the value for the key, may be the default value
     */
    public static <K, V> V getOrCreateValue(Map<K, V> map, K key, V defaultValue) {
        V value = map.get(key);
        if (value == null && defaultValue != null) {
            value = defaultValue;
            map.put(key, value);
        }
        return value;
    }

    /**
     * Returns the difference in milliseconds to the last call of this function
     *
     * @return difference in milliseconds
     */
    public static long testPerformance() {
        long diff = 0;
        long now = new Date().getTime();
        if (lastTime != -1) {
            diff = now - lastTime;
        }
        lastTime = now;
        return diff;
    }

    /**
     * Returns the difference in milliseconds to the last call of this function
     * and prints the given text and the difference
     *
     * @param text text of a message that is printed before the time difference
     * @return difference in milliseconds
     */
    public static long testPerformance(String text) {
        long diff = testPerformance();
        System.out.println(text + ":" + diff);
        return diff;
    }

    /**
     * Transforms an array list with Integer objects to an int array.
     *
     * @param list Array list with Integers.
     * @return An int array.
     */
    public static int[] toIntArray(List<Integer> list) {
        return toIntArray(list.toArray(new Integer[0]));
    }

    /**
     * Transforms an array with Integer objects to an int array.
     *
     * @param values Array with Integers.
     * @return An int array.
     */
    public static int[] toIntArray(Integer[] values) {
        int[] intList = new int[values.length];
        for (int i = 0; i < intList.length; i++) {
            intList[i] = values[i];
        }
        return intList;
    }

    /**
     * Transforms an array with int to an Integer object array.
     *
     * @param values Array with ints.
     * @return An Integer array.
     */
    public static Integer[] toIntegerArray(int[] values) {
        Integer[] intList = new Integer[values.length];
        for (int i = 0; i < intList.length; i++) {
            intList[i] = values[i];
        }
        return intList;
    }

    /**
     * Transforms an array with float to an Float object array.
     *
     * @param values Array with floats.
     * @return An Float array.
     */
    public static Float[] toFloatArray(float[] values) {
        Float[] fList = new Float[values.length];
        for (int i = 0; i < fList.length; i++) {
            fList[i] = values[i];
        }
        return fList;
    }

    /**
     * Transforms an array list to a string array.
     *
     * @param list array list.
     * @return string array.
     */
    public static String[] toStringArray(Collection<?> list) {
        String[] strLines = new String[list.size()];
        Iterator<?> it = list.iterator();
        for (int i = 0; it.hasNext(); i++) {
            strLines[i] = it.next().toString();
        }
        return strLines;
    }

    /**
     * Transforms an array with objects to a string array.
     *
     * @param list array with objects.
     * @return string array.
     */
    public static String[] toStringArray(Object[] list) {
        String[] strLines = new String[list.length];
        for (int i = 0; i < strLines.length; i++) {
            strLines[i] = list[i].toString();
        }
        return strLines;
    }

    /**
     * Transforms an array with {@link HGBaseItem} to a string array with the according Ids.
     *
     * @param list array with objects.
     * @return string array.
     */
    public static String[] toStringIdArray(HGBaseItem[] list) {
        String[] strIds = new String[list.length];
        for (int i = 0; i < strIds.length; i++) {
            strIds[i] = list[i].getId();
        }
        return strIds;
    }

    /**
     * Flattens a string array into a single string that is separated by the given separator.
     *
     * @param text      The array with strings.
     * @param separator The separator to separate the strings in the single results string.
     * @return A single string.
     */
    public static String toStringText(String[] text, String separator) {
        HGBaseStringBuilder single = new HGBaseStringBuilder(separator);
        for (String singleText : text) {
            single.append(singleText);
        }
        return single.toString();
    }

    /**
     * Returns the index of the given object in the list or -1.
     *
     * @param list  array with objects.
     * @param value value to find in the array.
     * @return index or -1.
     */
    public static int getIndexOf(Object[] list, Object value) {
        int index = -1;
        for (int i = 0; index == -1 && list != null && i < list.length; i++) {
            if (list[i].equals(value)) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Checks if the {@code subList} is a sub-list (including equal) of the {@code completeList}.
     *
     * @param completeList the complete list the check the potential sub list, must not be null
     * @param subList      the potential sub list that is tested, must not be null
     * @return true if {@code subList} is a sub-list of the {@code completeList}
     */
    public static boolean isSublist(List<?> completeList, List<?> subList) {
        if (completeList.size() >= subList.size()) {
            for (Object o : subList) {
                if (!completeList.contains(o)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param text text to invert.
     * @return inverted text (asdf -> fdsa).
     */
    public static String invertString(String text) {
        StringBuilder sb = new StringBuilder(text);
        return sb.reverse().toString();
    }

    /**
     * @return a split without regular expression.
     */
    public static String[] split(String text, String token) {
        StringTokenizer st = new StringTokenizer(text, token);
        List<String> list = new ArrayList<>();
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return HGBaseTools.toStringArray(list);
    }

    /**
     * @param text Text to investigate.
     * @return True if it's a text with content.
     */
    public static boolean hasContent(String text) {
        return (text != null && !text.isEmpty());
    }

    /**
     * Shortens a given text and adds dots in that case.
     *
     * @param text   the original text
     * @param maxLen the maximum length without shortening
     * @return the shortened text or the original one
     */
    public static String shortenString(String text, int maxLen) {
        if (text.length() > maxLen) {
            return text.substring(0, maxLen - 4) + "...";
        } else {
            return text;
        }
    }

    /**
     * Capitalizes the first letter of the given text.
     *
     * @param text the text to capitalize the first letter
     * @return a text with the first letter capitalized
     */
    public static String capitalizeFirstLetter(String text) {
        if (hasContent(text)) {
            String firstLetter = text.substring(0, 1).toUpperCase(Locale.getDefault());
            text = firstLetter + text.substring(1);
        }
        return text;
    }

    /**
     * Adds two arrays and returns the new one.
     *
     * @param array1 first array.
     * @param array2 second array.
     * @return new array containing the given arrays.
     */
    public static Object[] sumArrays(Object[] array1, Object[] array2) {
        Object[] sum = new Object[array1.length + array2.length];
        sumArrays(sum, array1, array2);
        return sum;
    }

    /**
     * Fills the first array with the two other arrays. The first array has to
     * be big enough to hold the other two ones.
     *
     * @param sum    The result array (needs at least a size of array1.length +
     *               array2.length).
     * @param array1 The first array.
     * @param array2 The second array.
     */
    public static void sumArrays(Object[] sum, Object[] array1, Object[] array2) {
        System.arraycopy(array1, 0, sum, 0, array1.length);
        System.arraycopy(array2, 0, sum, array1.length, array2.length);
    }

    /**
     * Returns the time difference between two moments.
     *
     * @param time1 The beginning time.
     * @param time2 The end time.
     * @return The difference, is positive if time1 is before time2.
     */
    public static long getTimeDifference(Date time1, Date time2) {
        return time2.getTime() - time1.getTime();
    }

    /**
     * Returns the time difference between an event in the past and now.
     *
     * @param time The time in the past.
     * @return The difference, is positive if time is in the past.
     */
    public static long getTimeDifference(Date time) {
        return getTimeDifference(time, new Date());
    }

    /**
     * Returns the maximum value of an int array.
     *
     * @param values An integer array.
     * @return The maximum int value.
     */
    public static int getMaximum(int[] values) {
        if (values != null && values.length > 0) {
            int[] valuesClone = values.clone();
            Arrays.sort(valuesClone);
            return valuesClone[valuesClone.length - 1];
        }
        return 0;
    }

    /**
     * Returns the minimum value of an int array.
     *
     * @param values An integer array.
     * @return The minimum int value.
     */
    public static int getMinimum(int[] values) {
        if (values != null && values.length > 0) {
            int[] valuesClone = values.clone();
            Arrays.sort(valuesClone);
            return valuesClone[0];
        }
        return 0;
    }

    /**
     * @param day A date formatted like yyyy-mm-dd .
     * @return A date object representing the day or null.
     */
    public static Date convertString2Date(String day) {
        String[] s = HGBaseTools.split(day, "-");
        if (s.length == 3) {
            int year = HGBaseTools.toInt(s[0]);
            if (year < 100) {
                if (year > 50) {
                    year += 1900;
                } else {
                    year += 2000;
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.set(year, HGBaseTools.toInt(s[1]) - 1, HGBaseTools.toInt(s[2]));
            return cal.getTime();
        }
        return null;
    }

    /**
     * @param day A day object.
     * @return String representing the date (yyyy-mm-dd).
     */
    public static String convertDate2String(Date day) {
        if (day == null) {
            return "";
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(day);
        int year = cal.get(Calendar.YEAR);// day.getYear()+1900;
        int mon = cal.get(Calendar.MONTH) + 1;// day.getMonth()+1;
        String dayText = year + "-";
        if (mon < 10) {
            dayText = dayText + "0";
        }
        dayText = dayText + mon + "-";
        if (cal.get(Calendar.DAY_OF_MONTH) < 10) {
            dayText = dayText + "0";
        }
        dayText = dayText + cal.get(Calendar.DAY_OF_MONTH);
        return dayText;
    }

    /**
     * @param i1 The first int value.
     * @param i2 The second int value.
     * @return The result of the comparison by Integer.
     */
    public static int compareInt(int i1, int i2) {
        return Integer.compare(i1, i2);
    }

    /**
     * @param o1 The first object.
     * @param o2 The second object.
     * @return <code>true</code> if both objects are null or equal.
     */
    public static boolean equalObject(Object o1, Object o2) {
        return (o1 == null && o2 == null || o1 != null && o1.equals(o2));
    }

    /**
     * Checks whether both classes are equal.
     *
     * @param o1 The first object.
     * @param o2 The second object.
     * @return <code>true</code> if both objects are not null and are of the same class.
     */
    public static boolean equalClass(Object o1, Object o2) {
        return (o1 != null && o2 != null && o1.getClass().equals(o2.getClass()));
    }

    /**
     * Returns the hash code of the given object or zero if object is null.
     *
     * @param o the object to get the hash code for
     * @return the has code or zero if object is null
     */
    public static int hashCode(Object o) {
        return (o == null) ? 0 : o.hashCode();
    }

    /**
     * Returns the first object of the given list that fulfills the criteria, or
     * null.
     *
     * @param list A list with objects.
     * @param fl   The listener to test each object.
     * @return The object that was looked for or null if not found.
     */
    public static <T> T findObject(T[] list, IFinderListener<T> fl) {
        for (int i = 0; list != null && i < list.length; i++) {
            if (fl.test(list[i])) {
                return list[i];
            }
        }
        return null;
    }

    /**
     * @param list A list with objects.
     * @param fl   The listener to test each object.
     * @return The index of the object that was looked for or -1 if not found.
     */
    public static <T> int findIndex(T[] list, IFinderListener<T> fl) {
        for (int i = 0; list != null && i < list.length; i++) {
            if (fl.test(list[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param list a list with HGBaseItem
     * @param id   the id of the item to look for
     * @return the item with the given id or null if not found
     */
    public static <T extends HGBaseItem> T findItemById(T[] list, final String id) {
        return findObject(list, o -> o.getId().equals(id));
    }

    /**
     * Returns a collection with all object that fulfill the criteria. The
     * resulting collection is allowed to be modified by the caller.
     *
     * @param list a list with objects
     * @param fl   the listener to test criteria for an object
     * @return a collection with all object where the test returns true, can be
     * an empty list
     */
    public static <T> Collection<T> findAll(T[] list, IFinderListener<T> fl) {
        Collection<T> newList = new ArrayList<>();
        for (int i = 0; list != null && i < list.length; i++) {
            if (fl.test(list[i])) {
                newList.add(list[i]);
            }
        }
        return newList;
    }

    /**
     * Returns the first element of the collection or null if the collection is empty.
     *
     * @param list the collection with the elements
     * @return the first element or null
     */
    public static <T> T getFirstOrNull(Collection<T> list) {
        return (list == null || list.isEmpty()) ? null : list.iterator().next();
    }

    /**
     * Returns the first and only element of the collection or null if the collection is empty or contains
     * more than one element.
     *
     * @param list the collection with the elements
     * @return the first and only element or null
     */
    public static <T> T getSingleElementOrNull(Collection<T> list) {
        return (list == null || list.size() != 1) ? null : list.iterator().next();
    }

    /**
     * Returns the element at the given index from the list or null if list is null or too short.
     *
     * @param list  a list with objects
     * @param index the index in the list to get the element
     * @return the element at the index or null
     */
    public static <T> T getElementOrNull(T[] list, int index) {
        return (list == null || list.length <= index) ? null : list[index];
    }

    /**
     * @param s A list with string.
     * @param i The index to get.
     * @return The string at the index or an empty string.
     */
    public static String getStringWithIndex(String[] s, int i) {
        return (i >= 0 && i < s.length) ? s[i] : "";
    }

    /**
     * @param value A value.
     * @return The number of digits this value contains.
     */
    public static int getNumberDigits(long value) {
        int numDigits = 1;
        long rest = value;
        while ((rest / 10) >= 1) {
            rest = rest / 10;
            numDigits++;
        }
        return numDigits;
    }

    /**
     * Returns the current date and time as string.
     *
     * @param format the format (e.g. d MMM yyyy, HH:mm)
     * @return the date and time as string with the given format
     */
    public static String getCurrentTime(String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
        return df.format(Calendar.getInstance().getTime());
    }


    /**
     * Transforms any exception into a runtime exception (
     * {@link IllegalStateException}).
     *
     * @param e the original exception.
     */
    public static void throwAsRuntimeException(Exception e) {
        throw new IllegalStateException(e.getMessage(), e);
    }

    /**
     * Waits for the given time and starts the timer afterwards.
     *
     * @param milliSeconds the number of milliseconds to wait
     * @param task         the timer task to start after the delay time
     */
    public static void delay(long milliSeconds, TimerTask task) {
        new Timer().schedule(task, milliSeconds);
    }

    /**
     * @param milliSeconds The number of milliseconds to wait.
     */
    public static void delay(long milliSeconds) {
        sleep(milliSeconds);
    }

    /**
     * Call {@link Thread#sleep(long)} and ignore interrupted exception.
     *
     * @param milliseconds the length of time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // NOCHECK: ignore this exception
        }
    }

    /**
     * Increases the size of the specified {@code Collection}, if necessary, to ensure that it has at least the number of elements specified by the minimum size argument.
     *
     * @param collection The {@code Collection} to check.
     * @param minSize    The desired minimum size.
     */
    public static void ensureSize(Collection<?> collection, int minSize) {
        while (collection.size() < minSize) {
            collection.add(null);
        }
    }

    /**
     * Returns the key and value of a key/value pair which is separated by {@code =}.
     *
     * @param str The string to parse.
     * @return The key and value of a key/value pair which is separated by {@code =}.
     */
    public static Pair<String, String> parseKeyValue(String str) {
        return parseKeyValue(str, "=");
    }

    /**
     * Returns the key and value of a key/value pair which is separated by {@code separator}.
     *
     * @param str       The string to parse.
     * @param separator The separator which separates key and value.
     * @return The key and value of a key/value pair which is separated by {@code separator}.
     */
    public static Pair<String, String> parseKeyValue(String str, String separator) {
        Pair<String, String> pair = null;
        if (hasContent(str)) {
            String[] splits = split(str, separator);
            pair = new Pair<>(splits[0], splits[1]);
        }
        return pair;
    }

    /**
     * Parses a string which represents a list of key/value pairs and returns it as map.
     *
     * @param str            The string to parse.
     * @param listSeparator  The separator which separates the elements in the list.
     * @param valueSeparator The separator which separates key and value.
     * @return A map with the parsed elements.
     */
    public static HashMap<String, String> parseKeyValueList(String str, String listSeparator, String valueSeparator) {
        HashMap<String, String> map = new HashMap<>();
        String[] splits = split(str, listSeparator);
        for (String split : splits) {
            Pair<String, String> pair = parseKeyValue(split, valueSeparator);
            map.put(pair.first, pair.second);
        }
        return map;
    }
}
