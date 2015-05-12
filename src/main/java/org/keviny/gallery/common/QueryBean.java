package org.keviny.gallery.common;

import java.util.Map;
import java.util.Set;

/**
 * Created by Kevin YOUNG on 2015/5/9.
 */

public class QueryBean {
	
	private Map<String, Object> params;
	private Set<String> fields;

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Set<String> getFields() {
		return fields;
	}

	public void setFields(Set<String> fields) {
		this.fields = fields;
	}

}
