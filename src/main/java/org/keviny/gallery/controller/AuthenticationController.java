package org.keviny.gallery.controller;

import org.keviny.gallery.amqp.RabbitMessageService;
import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.common.amqp.RabbitMessage;
import org.keviny.gallery.common.exception.ErrorCode;
import org.keviny.gallery.common.exception.ErrorCodeException;
import org.keviny.gallery.common.mail.MailMessage;
import org.keviny.gallery.rdb.model.User;
import org.keviny.gallery.rdb.repository.UserRepository;
import org.keviny.gallery.util.MessageDigestUtils;
import org.keviny.gallery.util.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by kevin on 5/23/15.
 */
@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RabbitMessageService rabbitMessageService;

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
        // Fetch specified fields
        Set<String> fields = new HashSet<>();
        fields.add("username");
        fields.add("password");
        fields.add("email");
        fields.add("locked");

        // decide whether it's an email or username, just based on `@`
        Map<String, Object> params = new HashMap<String, Object>();
        if(username.contains("@")) // it's email
            params.put("email", username);
        else
            params.put("username", username);

        q.setParams(params);
        q.setFields(fields);
        User user = userRepository.findOne(q);
        if(user == null)
            throw new ErrorCodeException(ErrorCode.USER_DOES_NOT_EXIST);

        if(user.isLocked())
            throw new ErrorCodeException(ErrorCode.USER_IS_LOCKED);

        String passwd = user.getPassword();
        String requestPassword = getEncryptedPassword(password, passwd.split(":")[1]);
        if(!passwd.equals(requestPassword))
            throw new ErrorCodeException(ErrorCode.PASSWORD_INCORRECT);
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
        // check if email is available
        boolean hasTaken = userRepository.hasEmailTaken(email);
        Map<String, Object> result =  new HashMap<>();
        result.put("email", email);
        HttpStatus status = HttpStatus.OK;
        if(hasTaken)
            throw new ErrorCodeException(ErrorCode.EMAIL_HAS_BEEN_TAKEN);

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
        user.setVcodeExpiresIn(new Timestamp(System.currentTimeMillis() + 7200000));
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
                + "<a href=\"http://localhost:8080/gallery/activate?email=" + email + "&code=" + verificationCode + "\">Kevin's github repository</a><br>"
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
            value = "/activate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void activate(@RequestParam(value = "email", required = true) String email,
                         @RequestParam(value = "code", required = true) String code) {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("email", email);
        m.put("code", code);
        Map<String, Object> params = new HashMap<String, Object>();
        m.put("email", email);
        final QueryBean q = new QueryBean();
        q.setParams(params);
        User user = userRepository.findOne(q);
        if(user == null)
            throw new ErrorCodeException(ErrorCode.USER_DOES_NOT_EXIST);

        if(user.isVerified()) { // email has already been verified
            return;
        }

        // check if verification code has or hasn't expired
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(!user.getVcodeExpiresIn().after(now))
            throw new ErrorCodeException(ErrorCode.VERIFICATION_CODE_HAS_EXPIRED);

        if(!code.equals(user.getVerificationCode()))
            throw new ErrorCodeException(ErrorCode.INVALID_VERIFICATION_CODE);

        Map<String, Object> values = new HashMap<>();
        values.put("verified", true);
        userRepository.updateSpecifiedFields(q, values);
    }

    // useful methods
    private static String getEncryptedPassword(String password) {
            return getEncryptedPassword(password, null);
    }

    private static String getEncryptedPassword(String password, String salt) {
        if(salt == null || "".equals(salt))
            salt = RandomUtils.getRandomString();
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
}
