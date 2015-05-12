package org.keviny.gallery.controller;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController {
	
	@RequestMapping(
			value = "",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> index() {
		Map<String, Object> m = null;
		m = new HashMap<String, Object>();
		m.put("server", "Gallery Server");
		m.put("projectName", "Gallery");
		m.put("serverTime", new Date());
		m.put("test", null);
		
		return new ResponseEntity<Map<String,Object>>(m, HttpStatus.FOUND);
	}
	
	@RequestMapping(
			value = "heartbeat",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<Map<String, Object>> heartbeat() {
		Map<String, Object> m = null;
		m = new HashMap<String, Object>();
		m.put("server", "Gallery Server");
		m.put("projectName", "Gallery");
		m.put("serverTime", new Date());
		m.put("test", null);
		
		return new ResponseEntity<Map<String,Object>>(m, HttpStatus.FOUND);
	}
}
