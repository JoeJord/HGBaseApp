/*
 * Created on 02.08.2005
 */
package at.hagru.hgbase.lib.internal;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import at.hagru.hgbase.lib.HGBaseLog;

/**
 * A simple factory for creating a class by the class path.
 *
 * @author hagru
 */
public class ClassFactory {

    private ClassFactory() {
        super();
    }
    
    /**
     * Checks if the class exists without instantiating the class.
     * 
     * @param classPath the full class path
     * @return true if the class exists
     */
    public static boolean existsClass(String classPath) {
		try {
			Class.forName(classPath);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
    }

    /**
     * @param classPath The class path.
     * @param classType The type of the new class.
     * @param errorName The class name for error messages.
     * @return A new class of the given type or null.
     */
    public static <T> T createClass(String classPath, Class<T> classType, String errorName) {
        return createClass(classPath, classType, errorName, null, null);
    }

    /**
     * @param classPath The class path.
     * @param classType The type of the new class.
     * @param errorName The class name for error messages.
     * @param classes The class types of the constructor arguments.
     * @param params An array with the arguments for the constructor.
     * @return A new object of the given type or null.
     */
    @SuppressWarnings("unchecked")
    public static <T> T createClass(String classPath, Class<T> classType, String errorName, Class<?>[] classes, Object[] params) {
        StringBuilder errorConstr = new StringBuilder();
        try {
        	Class<?> c = new ClassFactory().getClass().getClassLoader().loadClass(classPath);
            Object o = null;
            if (params!=null && params.length>0 && classes!=null && classes.length==params.length) {
                for (int i=0; i<classes.length; i++) {
                    if (i>0) {
                        errorConstr.append(", ");
                    }
                    errorConstr.append(classes[i].getName());
                }
                Constructor<?> constr = c.getConstructor(classes);
                o = constr.newInstance(params);
            } else {
                o = c.newInstance();
            }
            if (classType.isInstance(o)) {
                return (T) o;
            } else {
                HGBaseLog.logWarn("The "+errorName+" class "+classPath+" has wrong type (need "+classType.getName()+")!");
            }
        } catch (ClassNotFoundException e) {
            HGBaseLog.logWarn("The "+errorName+" class "+classPath+" was not found!");
        } catch (SecurityException | NoSuchMethodException e) {
            HGBaseLog.logWarn("The "+errorName+" class "+classPath+" does not implement the demanded constructor ("+errorConstr.toString()+")!");
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            HGBaseLog.logWarn("Could not create "+errorName+" class "+classPath+"! " + e);
        } catch (RuntimeException e) {
        	HGBaseLog.logError("Exception occured " + e.getClass() + "(" + e.getMessage() + ")");
        }
        return null;
    }
    
}
