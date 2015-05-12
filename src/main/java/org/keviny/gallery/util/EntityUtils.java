package org.keviny.gallery.util;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public final class EntityUtils {

	public static String getEntityName(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Entity.class)) {
			Entity entityAnnotation = clazz.getAnnotation(Entity.class);
			return entityAnnotation.name();
		}
		return null;
	}

	public static String getTableName(Class<?> clazz) {
		if (clazz.isAnnotationPresent(Table.class)) {
			Table entityAnnotation = clazz.getAnnotation(Table.class);
			return entityAnnotation.name();
		}
		return null;
	}
	
	public static <T> T getInstanceOf(Object[] cols, List<String> fields, Class<T> clazz) {
		T obj = null;
		try {
			obj = clazz.newInstance();
			int len = cols.length;
			for(int i = 0; i < len; i++) {
				if(cols[i] == null)
					continue;
				Field f = clazz.getDeclaredField(fields.get(i));
				f.setAccessible(true);
				f.set(obj, cols[i]);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return obj;
	}

}
