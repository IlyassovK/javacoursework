package com.example.lab9.controller;

import com.example.lab9.model.Article;
import com.example.lab9.service.ArticleService;
import com.example.lab9.service.JMS;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/article")
@RolesAllowed({"ADMIN", "OWNER", "USER"})
public class ArticleController {

    @EJB
    private ArticleService service;
    @EJB
    private JMS jms;

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/allArticles")
    public List<Article> getArticles() throws SQLException {
        return service.getArticles();
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/getArticles/{theme}")
    public Article getArticles(@PathParam("theme") String theme) throws SQLException {
        return service.getArticles(theme);
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/getArticle/{id}")
    public Article getArticleInfo(@PathParam("id") Integer id) throws SQLException {
        return service.getArticleInfo(id);
    }

    @POST
    @Produces("application/json")
    @PermitAll
    @Path("/addArticle")
    public Response addArticle(Article article) throws SQLException {
        return jms.sendArticleJMS(article);
    }

    @GET
    @Produces("application/json")
    @PermitAll
    @Path("/addArticle")
    public Response addArticle() throws SQLException {
        Article article = jms.getArticleJMS();
        return service.addArticle(article);
    }

    @PUT
    @Produces("application/json")
    @PermitAll
    @Path("/editArticle")
    public Response editArticle(Article article) throws SQLException {
        return service.editArticle(article);
    }

    @DELETE
    @Produces("application/json")
    @PermitAll
    @Path("/deleteArticle/{id}")
    public Response deleteArticle(@PathParam("id")Integer id) throws SQLException {
        return service.deleteArticle(id);
    }

    @PUT
    @Produces("application/json")
    @PermitAll
    @Path("/like/{userId}-{articleId}")
    public Response like(@PathParam("userId") Integer userId, @PathParam("articleId") Integer articleId) {
        return service.like(userId, articleId);
    }

    @DELETE
    @Produces("application/json")
    @PermitAll
    @Path("/unlike/{userId}-{articleId}")
    public Response unLike(@PathParam("userId") Integer userId,@PathParam("articleId") Integer articleId) {
        return service.unLike(userId, articleId);
    }
}
