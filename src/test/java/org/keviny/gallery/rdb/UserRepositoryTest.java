package org.keviny.gallery.rdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 5/23/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:mvc-config.xml"})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUpdateSpecifiedFields() {
        final QueryBean q = new QueryBean();
        Map<String, Object> params = new HashMap<>();
        params.put("email", "kevin@gallery.org");
        q.setParams(params);
        Map<String, Object> values = new HashMap<>();
        values.put("locked", true);
        values.put("email", "kings988@163.com");
        values.put("website",  "http://www.kevin.com");
        userRepository.updateSpecifiedFields(q, values);

    }
}
