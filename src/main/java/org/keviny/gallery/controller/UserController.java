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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
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

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
    @Resource
    private UserRepository userRepository;
    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @Cacheable(value = "user")
    public User getUser(
    		@PathVariable("id") String id,
    		@RequestParam(value = "_field", required = false) String _field,
    		@RequestParam(value = "_callback", required = false) String _callback
    ) {
    	if (LOG.isDebugEnabled()) 
    		LOG.debug("Fetch user with id: " + id);
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
        } else {
            params.put("username", id);
        }
        params.put("deleted", false);
        //params.put("locked", false);
        q.setParams(params);
        user = userRepository.findOne(q);
       /* if(user == null) {
        	Map<String, Object> error = new HashMap<String, Object>();
        	error.put("message", "Resource `api/users/" + id + "` does not exist!");
        	error.put("code", 404);
        	return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
        }
        
        if(user.isLocked()) {
        	Map<String, Object> error = new HashMap<String, Object>();
        	error.put("message", "User `" + user.getUsername() + "` has been locked.");
        	error.put("code", 409);
        	return new ResponseEntity<Object>(error, HttpStatus.CONFLICT);
        }*/
        
        return user;
    }

}
