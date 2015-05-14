package org.keviny.gallery.rdb.repository;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.model.User;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface UserRepository extends RdbRepository<User> {
    void create(User user);
    void remove(Integer id);
    void remove(String username);
    void removeByEmail(String email);
}
