package org.keviny.gallery.controller;

import java.net.URI;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.keviny.gallery.amqp.RabbitMessageService;
import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.common.RegularExpression;
import org.keviny.gallery.common.amqp.RabbitMessage;
import org.keviny.gallery.common.mail.MailMessage;
import org.keviny.gallery.rdb.model.User;
import org.keviny.gallery.rdb.repository.UserRepository;
import org.keviny.gallery.util.MessageDigestUtils;
import org.keviny.gallery.util.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

@Controller
@RequestMapping("/users")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RabbitMessageService rabbitMessageService;

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
