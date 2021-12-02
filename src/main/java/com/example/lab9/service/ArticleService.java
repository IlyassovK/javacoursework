package com.example.lab9.service;

import com.example.lab9.model.Article;
import com.example.lab9.repository.ArticleRepository;
import com.example.lab9.repository.CommentRepository;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.core.Response;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Stateful
public class ArticleService {

    @EJB
    private ArticleRepository repository;
    @EJB
    private CommentRepository commentRepository;

    public List<Article> getArticles() throws SQLException{
        return repository.getArticles();
    }

    public Article getArticleInfo(Integer id) throws SQLException {
        return repository.getArticleInfo(id);
    }

    public Article getArticles(String theme) throws SQLException {
        return repository.getArticles(theme);
    }

    public Response addArticle(Article article) throws SQLException {
        return repository.addArticle(article);
    }

    public Response editArticle(Article article) throws SQLException {
        return repository.editArticle(article);
    }

    public Response deleteArticle(Integer id) throws SQLException {
        commentRepository.deleteComments(id);
        repository.deleteLikes(id);
        return repository.deleteArticle(id);
    }

    public Response like(Integer userId, Integer articleId) {
        return repository.like(userId, articleId);
    }

    public Response unLike(Integer userId, Integer articleId) {
        return repository.unLike(userId, articleId);
    }

//    public Response deleteArticle(Article article) throws SQLException {
//        return repository.deleteArticle(article);
//    }
}
