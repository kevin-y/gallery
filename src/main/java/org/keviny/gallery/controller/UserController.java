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
			value = "/login",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			method = RequestMethod.POST
	)
	@ResponseBody
	public ResponseEntity<Void> login(
			@RequestParam("username") String username,
			@RequestParam("password") String password) throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(new URI("/"));
		final QueryBean q = new QueryBean();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		q.setParams(params);
		User user = userRepository.findOne(q);
		if(user == null) { // user doesn't exist
			// TODO
		}
		
		return new ResponseEntity<Void>(headers, HttpStatus.MOVED_PERMANENTLY);
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

        // check validity of email

        // check if email is available
        boolean hasTaken = userRepository.hasEmailTaken(email);

        Map<String, Object> result =  new HashMap<>();
        result.put("email", email);
        HttpStatus status = HttpStatus.OK;
        if(hasTaken) {
            result.put("message", "This email is not available");
            status = HttpStatus.CONFLICT;
        }

        String username = email.split("@")[0];
        String similarUsername = userRepository.getSimilarUsername(username);
        if(similarUsername != null) {
        	username = buildAvailabeUsername(similarUsername);
        }
        
        User user = new User();
        user.setUsername(username);
        user.setPassword(getEncryptedPassword(password));
        user.setGender('N');
        user.setEmail(email);
        user.setLocked(false);
        user.setVerified(false);
        String verificationCode = RandomUtils.getRandomString(8);
        user.setVerificationCode(verificationCode);
        userRepository.create(user);
        result.put("user", user);
        
        MailMessage mm = new MailMessage();
        Set<String> recipients = new HashSet<String>();
        recipients.add(email);
        mm.setRecipients(recipients);
        mm.setSubject("Kevin's Galley: Please activate your account!");
        
        String content = "Bellow's the link:<br>"
				   + "<a href=\"http://localhost:8080/gallery/users/activate?email=" + email + "&code=" + verificationCode + "\">Kevin's github repository</a><br>"
				   + "Sample image:<br>"
				   + "<img src=\"http://www.ipaddesk.com/uploadfile/2013/0118/20130118104739919.jpg\" title=\"Architecture\" alt=\"Architecture\">";
        mm.setContent(content);
        //mm.setContent("http://localhost:8080/gallery/activate?email=" + email + "&code=" + verificationCode);
        RabbitMessage<MailMessage> rm = new RabbitMessage<MailMessage>();
        
        rm.setExchange("gallery.mail");
        rm.setRoutingKey("gallery.mail.send");
        rm.setBody(mm);
        rabbitMessageService.publish(rm);
        
        return new ResponseEntity<Object>(result, status);
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
    
    @RequestMapping(
            value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<Object> activate(@RequestParam(value = "email", required = true) String email,
    		@RequestParam(value = "code", required = true) String code) {
    	Map<String, Object> m = new HashMap<String, Object>();
    	m.put("email", email);
    	m.put("code", code);
    	Map<String, Object> params = new HashMap<String, Object>();
    	m.put("email", email);
    	QueryBean q = new QueryBean();
    	q.setParams(params);
    	User user = userRepository.findOne(q);
    	if(user == null) {
    		// TODO: user doesn't exists
    	}
    	if(code.equals(user.getVerificationCode())) {
    		Timestamp now = new Timestamp(System.currentTimeMillis());
    		if(user.getVcodeExpiresIn().after(now)) {
    			// verification succeeded
    			// TODO
    		}
    	}
    	return new ResponseEntity<Object>(m, HttpStatus.OK);
    }
    
    private static String getEncryptedPassword(String password) {
		String salt = RandomUtils.getRandomString();
		MessageDigestUtils md5 = MessageDigestUtils.getInstance("md5");
		String md5Str = md5.encode(Base64Utils.encodeToString((password + salt).getBytes()));
		return (md5Str + ":" + salt);
    }
    
    private static String buildAvailabeUsername(String s) {
    	int len = s.length();
    	char[] chs = s.toCharArray();
    	int i = len - 1;
    	StringBuilder digits = new StringBuilder();
    	for( ; i >= 0; i--) {
    		if(chs[i] >= 48 && chs[i] <= 57)
    			digits.append(chs[i]);
    		else 
    			break;
    	}
    	String username = s.substring(0, i + 1);
    	if(digits.length() > 0)
    		username = username + (Integer.parseInt(digits.toString()) + 1);
    	else 
    		username += "0";
    	return username;
    }
   
    
    public static void main(String[] args) {
    	
	}
}
