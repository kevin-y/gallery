package org.keviny.gallery.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import org.keviny.gallery.common.annotation.Ignore;
import org.keviny.gallery.rdb.model.User;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public final class FieldUtils {
	
	/*public static String forgeColumnString(Class<?> clazz) {
		return forgeColumnString(clazz, true);
	}
	
	public static String forgeColumnString(Class<?> clazz, boolean ignore) {
		return forgeColumnString(clazz, ignore, null);
	}
	
	public static String forgeColumnString(Class<?> clazz, boolean ignore, String prefix) {
		StringBuilder _field = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class)) {
				if(field.isAnnotationPresent(Ignore.class) && ignore)
					continue;
				Column col = field.getAnnotation(Column.class);
				if(prefix != null) 
					_field.append(prefix).append('.');
				_field.append(col.name()).append(',');
			}
		}
		if (_field.length() > 0)
			_field.deleteCharAt(_field.length() - 1);
		return _field.toString();
	}
	
	public static String forgeColumnString(Object[] fieldSet, Class<?> clazz) {
		return forgeColumnString(fieldSet, clazz, true);
	}
	
	public static String forgeColumnString(Object[] fieldSet, Class<?> clazz, boolean ignore) {
		return forgeColumnString(fieldSet, clazz, ignore, null);
	}
	
	
	public static String forgeColumnString(Object[] fieldSet, Class<?> clazz, boolean ignore, String prefix) {
		StringBuilder _column = new StringBuilder();
		for (Object f : fieldSet) {
			Field field = null;
			try {
				field = clazz.getDeclaredField(f.toString());
			} catch (NoSuchFieldException e) {
				continue;
			} catch (SecurityException e) {
				e.printStackTrace();
			}
			
			if (field.isAnnotationPresent(Column.class)) {
				// When ignore set to true,field with an Ignore annotation,
				// should not be exposed to the outside world, otherwise ignore
				// this field
				if(field.isAnnotationPresent(Ignore.class) && ignore)
					continue;
				Column col = field.getAnnotation(Column.class);
				if(prefix != null) 
					_column.append(prefix).append('.');
				_column.append(col.name()).append(',');
			}
		}

		if (_column.length() > 0)
			_column.deleteCharAt(_column.length() - 1);
		return _column.toString();
	}
*/
	
	
	/*public static String forgeFieldString(Class<?> clazz) {
		return forgeFieldString(clazz, true);
	}
	
	public static String forgeFieldString(Class<?> clazz, boolean ignore) {
		return forgeFieldString(clazz, ignore, null);
	}
	
	public static String forgeFieldString(Class<?> clazz, boolean ignore, String prefix) {
		StringBuilder _field = new StringBuilder();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class)) {
				if(field.isAnnotationPresent(Ignore.class) && ignore)
					continue;
				if(prefix != null) 
					_field.append(prefix).append('.');
				_field.append(field.getName()).append(',');
			}
		}
		if (_field.length() > 0)
			_field.deleteCharAt(_field.length() - 1);
		return _field.toString();
	}*/
	
	public static List<String> forgeFieldList(Set<String> fieldSet, Class<?> clazz) {
		return forgeFieldList(fieldSet, clazz, true);
	}
	
	public static List<String> forgeFieldList(Set<String> fieldSet, Class<?> clazz, boolean ignore) {
		List<String> fieldList = new ArrayList<String>();
		for(String f : fieldSet) {
			try {
				Field field = clazz.getDeclaredField(f);
				if(field.isAnnotationPresent(Column.class)) {
					if(field.isAnnotationPresent(Ignore.class) && ignore)
						continue;
					fieldList.add(f);
				}
			} catch (NoSuchFieldException e) {
				continue;
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return fieldList;
	}
	
	public static List<String> forgeFieldList(Class<?> clazz) {
		return forgeFieldList(clazz, true);
	}
	
	public static List<String> forgeFieldList(Class<?> clazz, boolean ignore) {
		List<String> fieldList = new ArrayList<String>();
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class)) {
				if(field.isAnnotationPresent(Ignore.class) && ignore)
					continue;
				fieldList.add(field.getName());
			}
		}
		return fieldList;
	}
	
	public static String join(List<String> fields) {
		return join(fields, null);
	}
	
	public static String join(List<String> fields, String prefix) {
		StringBuilder _fields = new StringBuilder();
		for(String field : fields) {
			if(prefix != null && !"".equals(prefix.trim()))
				_fields.append(prefix).append('.');
			_fields.append(field).append(',');
		}
		if (_fields.length() > 0)
			_fields.deleteCharAt(_fields.length() - 1);
		return _fields.toString();
	}
	
	public static void main(String[] args) {
		Set<String> fieldSet = new HashSet<String>();
		fieldSet.add("username");
		fieldSet.add("password");
		fieldSet.add("loginTime");
		
		List<String> list = forgeFieldList(fieldSet, User.class);
		for(String s : list) {
			System.out.println(s);
		}
		
	}
}
