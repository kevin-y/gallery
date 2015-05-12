package org.keviny.gallery.common;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomizedObjectMapper extends ObjectMapper {

	private static final long serialVersionUID = 4935797420349352150L;

	public CustomizedObjectMapper(){
		super();
		SimpleModule module = new SimpleModule("ObjectIdModule");
		module.addSerializer(ObjectId.class,  new ObjectIdSerializer());
		this.registerModule(module);
		//this.writerWithDefaultPrettyPrinter();	
		this.setSerializationInclusion(Include.NON_NULL);
		this.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		//this.enable(SerializationFeature.INDENT_OUTPUT);
		this.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
	}
	
	private class ObjectIdSerializer extends JsonSerializer<ObjectId> implements Serializable {
		private static final long serialVersionUID = -5821680237847967478L;

		@Override
		public void serialize(ObjectId value, JsonGenerator gen,
				SerializerProvider serializers) throws IOException,
				JsonProcessingException {
			gen.writeString(value.toString());
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("server", "FastCRM Server");
		m.put("projectName", "FastCRM");
		m.put("serverTime", new Date());
		m.put("test", null);
		ObjectMapper o = new ObjectMapper();
		
		CustomizedObjectMapper om = new CustomizedObjectMapper();
		o.setSerializationInclusion(Include.NON_NULL);
		o.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		System.out.println(om.writeValueAsString(m));
	}

}
