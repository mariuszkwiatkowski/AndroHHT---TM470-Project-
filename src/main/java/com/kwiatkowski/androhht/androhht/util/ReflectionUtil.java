package com.kwiatkowski.androhht.androhht.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Based on http://stackoverflow.com/questions/735230/java-reflection-access-protected-field
 *
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 *
 */
public class ReflectionUtil {

    public static Field getField(Class reflected_class, String fieldName) throws NoSuchFieldException {
        try {
            return reflected_class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Class superClass = reflected_class.getSuperclass();
            if (superClass == null) {
                throw e;
            } else {
                return getField(superClass, fieldName);
            }
        }
    }
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()))
        {
            field.setAccessible(true);
        }
    }

}
