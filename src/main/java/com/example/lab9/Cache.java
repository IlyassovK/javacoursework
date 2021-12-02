package com.example.lab9;

import com.example.lab9.model.User;
import com.example.lab9.repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Startup
@Stateless
public class Cache {

    private Map cache;

    @EJB
    private UserRepository repository;

    private Map<Integer, User> createCache() throws SQLException {
        Map<Integer, User> map = new HashMap<>();
        List<User> dataList = new ArrayList<>();
        for (User user : dataList) {
            map.put(user.getId(),user);
        }
        return map;
    }

    @PostConstruct
    private void populateCache() throws SQLException {
        cache = createCache();
    }

    public User getData(Integer key) {
        return (User) cache.get(key);
    }

}
