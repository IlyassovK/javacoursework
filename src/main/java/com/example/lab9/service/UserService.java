package com.example.lab9.service;

import com.example.lab9.model.User;
import com.example.lab9.repository.UserRepository;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Stateful
public class UserService {

    @EJB
    private UserRepository repository;

    public List<User> getUsers() throws SQLException {
        return repository.getUsers();
    }

    public User getUser(Integer id) throws SQLException {
        return repository.getUser(id);
    }

    public Response addUser(User user) {
        return repository.addUser(user);
    }

    public Response editUser(User user) {
        return repository.editUser(user);
    }

    public Response deleteUser(Integer id){
        return repository.deleteUser(id);
    }

    public Response addToFavorite(Integer userId, Integer articleId) {
        return repository.addToFavorite(userId, articleId);
    }

    public Response removeFromFavorite(Integer userId, Integer articleId) {
        return repository.removeFromFavorite(userId, articleId);
    }

}
