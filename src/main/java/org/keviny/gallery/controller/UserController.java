package org.keviny.gallery.controller;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.common.RegularExpression;
import org.keviny.gallery.rdb.model.User;
import org.keviny.gallery.rdb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

@Controller
@RequestMapping("/users")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
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


	@RequestMapping(
			value = "",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	@ResponseBody
	public List<User> list() {
		QueryBean q = new QueryBean();
		List<User> users = userRepository.find(q);
		return  users;
	}


	@RequestMapping(
			value = "/{id}",
			method = RequestMethod.DELETE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Object delete(
			@PathVariable("id") Integer id) {
		//if(LOG.isDebugEnabled())
		//	LOG.debug("Delete user " +  id);
        System.out.println("Delete user " +  id);
		return null;
	}


	@RequestMapping(
			value = "/{id}/locked/{locked}",
			method = RequestMethod.PUT,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public Object lockOrUnlock(
			@PathVariable("id") Integer id,
			@PathVariable("locked") Boolean locked) {
		//if(LOG.isDebugEnabled())
		//LOG.debug("User " + id + " " + (locked ? "locked" : "unlocked"));
        System.out.println("User " + id + " " + (locked ? "locked" : "unlocked"));
		return null;
	}


	@RequestMapping(
			value = "/login",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			method = RequestMethod.POST
	)
	@ResponseBody
	public ResponseEntity login(
			@RequestParam("username") String username,
			@RequestParam("password") String password) throws Exception {


		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI("/"));

		return new ResponseEntity(headers, HttpStatus.MOVED_PERMANENTLY);
	}

	@RequestMapping(
			value = "/register",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			method = RequestMethod.POST
	)
    @ResponseBody
	public Object register(
			@RequestParam(value = "email", required = true) String email,
			@RequestParam(value = "password", required = true) String password) {

        boolean hasTaken = userRepository.hasEmailTaken(email);
        Map<String, Object> result =  new HashMap<>();
        if(hasTaken) {
            result.put("email", email);
            result.put("message", "This email is not available");
        }
        return result;
	}

    @RequestMapping(
            value = "/{field}/exists",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public Object exists(
            @PathVariable("field") String field,
            @RequestParam("type") String type) {

        Map<String, Object> result = new HashMap<>();
        boolean hasTaken = false;
        if(type.equalsIgnoreCase("EMAIL")) {
            hasTaken = userRepository.hasEmailTaken(field);
            result.put("hasTaken", hasTaken);
        } else if(type.equalsIgnoreCase("USERNAME")) {
            hasTaken = userRepository.hasUsernameTaken(field);
            result.put("hasTaken", hasTaken);
        } else {
            result.put("message", "Unknown type `" + type + "`");
        }
        return result;
    }
}
