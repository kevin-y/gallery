package org.keviny.gallery.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.common.RegularExpression;
import org.keviny.gallery.rdb.model.User;
import org.keviny.gallery.rdb.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

@Controller
@RequestMapping("api/users")
public class UserController {

    @Resource
    private UserRepository userRepository;
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public 
    @ResponseBody
    ResponseEntity<Object> getUser(
    		@PathVariable("id") String id,
    		@RequestParam(value = "_field", required = false) String _field,
    		@RequestParam(value = "_callback", required = false) String _callback
    ) {
    	Set<String> fields = null;
    	if(_field != null) {
    		fields = new HashSet<String>();
    		String[] fArray = _field.split(",");
    		for(String field : fArray) {
    			fields.add(field);
    		}
    	}
    	
    	QueryBean q = new QueryBean();
    	q.setFields(fields);
    	Map<String, Object> params = new HashMap<String, Object>();

        User user = null;
        Pattern p = Pattern.compile(RegularExpression.DIGITS);
        Matcher m = p.matcher(id);
        if(m.matches()) {
        	params.put("id", Integer.parseInt(id));
        	user = userRepository.findOne(q);
        } else {
            params.put("username", id);
            q.setParams(params);
            user = userRepository.findOne(q);
        }

        if(user == null) {
        	Map<String, Object> error = new HashMap<String, Object>();
        	error.put("message", "Resource `api/users/" + id + "` does not exist!");
        	error.put("code", 404);
        	return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Object>(user, HttpStatus.OK);
    }

}
