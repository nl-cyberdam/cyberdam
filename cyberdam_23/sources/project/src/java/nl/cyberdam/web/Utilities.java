/*
 * Utilities.java
 *
 * see: http://www-128.ibm.com/developerworks/java/library/j-hibval.html
 */

package nl.cyberdam.web;

import java.lang.reflect.Method;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

/**
 * A Utility class.  It is a singleton.
 *
 * @author Ted Bergeron
 * @version $Id: Utilities.java,v 1.14 2006/04/06 03:35:43 ted Exp $
 */

public class Utilities {
    private static Log log = LogFactory.getLog(Utilities.class);
    
    private static final String PROPERTY_SEPERATOR = ".";
    
    private static Method findGetterMethod(Object object, String propertyPath) {
        if (object == null) {
            log.debug("Object was null.");
            return null;
        }
        
        if (propertyPath == null) {
            log.debug("propertyPath was null.");
            return null;
        }
        
        //RequestContext requestContext = (RequestContext) pageContext.getAttribute(RequestContextAwareTag.REQUEST_CONTEXT_PAGE_ATTRIBUTE);
        // Object target = requestContext.getModelObject(beanName);  (BindStatus:132) Converts string path to object instance.
        
        log.debug("Testing methods.");
        log.debug("Path = " + propertyPath);
        log.debug("Derived object = " + getObjectFromPropertyPath(propertyPath));
        log.debug("Derived property = " + getPropertyFromPropertyPath(propertyPath));
        log.debug("Passed object classname = " + object.getClass().getName());
        
        String property = getPropertyFromPropertyPath(propertyPath);
        
        String getterMethodName = convertPropertyNameToGetMethodName(property); // property is "email"  convert to getEmail
        
        try {
            return object.getClass().getMethod(getterMethodName);
        } catch (NoSuchMethodException e) {
            log.error(e);
            return null; // Method name passed in was invalid.
        }
        
    }
    
    @SuppressWarnings("boxing")
	public static Integer maxLength(Object object, String propertyPath) {
        Method getMethod = findGetterMethod(object, propertyPath);
        if (getMethod == null) {
            return null;
        }
        
        if (getMethod.isAnnotationPresent(Length.class)) {
            Length length = getMethod.getAnnotation(Length.class);
            int max = length.max();
            log.debug("Max length = " + max);
            if (max > 0) {
                return max;
            } else {
                return null; // Length was defined, but max was not provided, or was set to 0.
            }
        } else {
            return null; // Length annotation was not found.
        }
    }
    
    @SuppressWarnings("boxing")
	public static Boolean required(Object object, String propertyPath) {
        Method getMethod = findGetterMethod(object, propertyPath);
        if (getMethod == null) {
            return null;
        } else {
            return getMethod.isAnnotationPresent(NotNull.class);
        }
    }
    
    @SuppressWarnings("boxing")
	public static Boolean isDate(Object object, String propertyPath) {
        return java.util.Date.class.equals(getReturnType(object, propertyPath));
    }
    
    @SuppressWarnings("unchecked")
	public static Class getReturnType(Object object, String propertyPath) {
        Method getMethod = findGetterMethod(object, propertyPath);
        if (getMethod == null) {
            return null;
        } else {
            return getMethod.getReturnType();
        }
    }
    
    public static Object getReferencedObject(Object object, String propertyPath) {
        Method getMethod = findGetterMethod(object, propertyPath);
        if (getMethod == null) {
            return null;
        }
        try {
            return getMethod.invoke(object);
        } catch (Exception e) {
            log.debug("Unable to get object via reflection.");
            return null;
        }
    }
    
    public static String identifyingValue(Object object, String propertyPath) {
        Object referencedObject = getReferencedObject(object, propertyPath);
        if (referencedObject != null && referencedObject instanceof Selectable) {
            Selectable selectable = (Selectable) referencedObject;
            return selectable.getIdentifyingValue();
        } else {
            return null;
        }
    }
    
    public static String convertPropertyNameToGetMethodName(String property) {
        if (property == null) {
            throw new IllegalArgumentException("Property name was null.");
        }
        StringBuilder builder = new StringBuilder(property.length() + 3);
        String firstLetter = property.substring(0, 1);
        String wordRemainder = property.substring(1);
        firstLetter = firstLetter.toUpperCase();
        builder.append("get").append(firstLetter).append(wordRemainder);
        return builder.toString();
    }
    
    public static String getObjectFromPropertyPath(String propertyPath) {
        // Path is "customer.contact.fax.number", need to return "customer.contact.fax"
        log.debug("propertyPath = " + propertyPath);
        int lastSeperatorPosition = propertyPath.lastIndexOf(PROPERTY_SEPERATOR);
        String objectString = propertyPath.substring(0, lastSeperatorPosition);
        log.debug("objectString = " + objectString);
        return objectString;
    }
    
    public static String getPropertyFromPropertyPath(String propertyPath) {
        // Path is "customer.contact.fax.number", need to return "number"
        log.debug("propertyPath = " + propertyPath);
        int lastSeperatorPosition = propertyPath.lastIndexOf(PROPERTY_SEPERATOR);
        String propertyString = propertyPath.substring(lastSeperatorPosition + 1);
        log.debug("propertyString = " + propertyString);
        return propertyString;
    }
    
    /*
    public static void main(String args[]) {
        System.out.println("Hashvalue of " + args[0] + " = " + hashValue(args[0]));
    }
     */
    
    public static String convertDatabaseNameToJavaName(String val) {
        if (val == null) {
            return null;
        }
        
        String value = val.toLowerCase();
        StringBuffer buf = null;
        
        StringTokenizer st = new StringTokenizer(value, "_");
        if (st.hasMoreTokens()) {
            buf = new StringBuffer(st.nextToken()); // Take first piece as is
            while (st.hasMoreTokens()) {
                String fragment = st.nextToken();
                buf.append(initCap(fragment));
            }
        }
        
        if (buf == null) {
            return null;
        } else {
            return buf.toString();
        }
    }
    
    public static String convertGet(String value) {
        StringBuilder buf = new StringBuilder("get");
        buf.append(initCap(convertDatabaseNameToJavaName(value)));
        return buf.toString();
    }
    
    public static String convertSet(String value) {
        StringBuilder buf = new StringBuilder("set");
        buf.append(initCap(convertDatabaseNameToJavaName(value)));
        return buf.toString();
    }
    
    public static String initCap(String value) {
        if (value == null) {
            return null;
        }
        
        String initCap = null;
        String suffix = null;
        
        try { // Need to convert first letter to caps
            initCap = value.substring(0, 1).toUpperCase();
            suffix = value.substring(1, value.length());
        } catch (IndexOutOfBoundsException e) {
            log.error(e);
        }
        return initCap + suffix;
    }
    
} // End Class Utilities
