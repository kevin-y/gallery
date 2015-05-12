package org.keviny.gallery.rdb.repository;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.model.User;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface UserRepository extends RdbRepository<User> {
    public void create(User user);
    public User findByEmail(String email);
    public void remove(Integer id);
    public void remove(String username);
    public void removeByEmail(String email);
    public User findByUsername(final QueryBean q);
}