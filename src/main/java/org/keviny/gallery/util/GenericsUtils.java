package org.keviny.gallery.util;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public class GenericsUtils {

	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {    
        Type genType = clazz.getGenericSuperclass(); 

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;   
        }  

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();                   
        if (index >= params.length || index < 0) { 
        	 throw new RuntimeException("Index "+ (index<0 ? "less than 0" : "out of bound"));
        }      
        if (!(params[index] instanceof Class)) {
            return Object.class;   
        }   
        return (Class<?>)params[index];
    }

	public static Class<?> getSuperClassGenricType(Class<?> clazz) {
    	return getSuperClassGenricType(clazz,0);
    }

}
