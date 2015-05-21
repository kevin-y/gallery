package org.keviny.gallery.rdb.repository;

import org.keviny.gallery.common.QueryBean;
import org.keviny.gallery.rdb.model.User;

import java.util.List;

/**
 * Created by Kevin YOUNG on 2015/5/8.
 */

public interface UserRepository extends RdbRepository<User> {
    public void create(User user);
    public void remove(Integer id);
    public void remove(String username);
    public void removeByEmail(String email);
    public boolean hasUsernameTaken(String username);
    public boolean hasEmailTaken(String email);
    public String getSimilarUsername(String username);
}
