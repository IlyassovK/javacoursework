package com.example.lab9.controller;

import com.example.lab9.model.Comment;
import com.example.lab9.service.CommentService;
import com.example.lab9.service.JMS;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/comment")
@RolesAllowed({"ADMIN", "OWNER", "USER"})
public class CommentController {
    @EJB
    private CommentService service;

    @EJB
    private JMS jms;

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/allComments")
    public List<Comment> getComments() throws SQLException {
        return service.getComments();
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/getComment/{id}")
    public Comment getComment(@PathParam("id") Integer id) throws SQLException {
        return service.getComment(id);
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/getArticleComment/{id}")
    public List<Comment> getArticleComments(@PathParam("id") Integer articleId) throws SQLException {
        return service.getArticleComments(articleId);
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/getUserComment/{id}")
    public List<Comment> getUserComments(@PathParam("id") Integer authorId) throws SQLException {
        return service.getUserComments(authorId);
    }

    @POST
    @Produces("application/json")
    @PermitAll
    @Path("/addComment")
    public Response addComment(Comment comment) {
        return jms.sendCommentJMS(comment);
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/addComment")
    public Response addComment() {
        Comment comment = jms.getCommentJMS();
        return service.addComment(comment);
    }

    @PUT
    @Produces("application/json")
    @PermitAll
    @Path("/editComment")
    public Response editComment(Comment comment) {
        return service.editComment(comment);
    }

    @DELETE
    @Produces("application/json")
    @PermitAll
    @Path("/deleteComment/{id}")
    public Response deleteComment(@PathParam("id") Integer id){
        return service.deleteComment(id);
    }
}
