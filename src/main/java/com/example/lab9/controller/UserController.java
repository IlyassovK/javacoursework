package com.example.lab9.controller;

import com.example.lab9.Cache;
import com.example.lab9.model.User;
import com.example.lab9.service.JMS;
import com.example.lab9.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/user")
@RolesAllowed({"ADMIN", "OWNER", "USER"})
public class UserController {
    @EJB
    private UserService service;

    @EJB
    private JMS jms;

    @EJB
    private Cache cache;

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/allUsers")
    public List<User> getUsers() throws SQLException {
        return service.getUsers();
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/getUser/{id}")
    public User getUser(@PathParam("id") Integer id) throws SQLException {
        return cache.getData(id);
    }

    @POST
    @Produces("application/json")
    @PermitAll
    @Path("/addUser")
    public Response addUser(User user) {
        return jms.sendUserJMS(user);
    }

    @POST
    @Produces("application/json")
    @PermitAll
    @Path("/addUser")
    public Response addUser() {
        User user = jms.getUserJMS();
        return service.addUser(user);
    }

    @PUT
    @Produces("application/json")
    @PermitAll
    @Path("/editUser")
    public Response editUser(User user) {
        return service.editUser(user);
    }

    @DELETE
    @Produces("application/json")
    @PermitAll
    @Path("/deleteUser/{id}")
    public Response deleteUser(@PathParam("id") Integer id){
        return service.deleteUser(id);
    }

    @PUT
    @Produces("application/json")
    @PermitAll
    @Path("/addToFavorite/{userId}-{articleId}")
    public Response addToFavorite(@PathParam("userId") Integer userId,@PathParam("articleId") Integer articleId) {
        return service.addToFavorite(userId, articleId);
    }

    @DELETE
    @Produces("application/json")
    @PermitAll
    @Path("/removeFromFavorite/{userId}-{articleId}")
    public Response removeFromFavorite(@PathParam("userId") Integer userId,@PathParam("articleId") Integer articleId) {
        return service.removeFromFavorite(userId, articleId);
    }
}
