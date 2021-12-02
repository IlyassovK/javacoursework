package com.example.lab9.service;

import com.example.lab9.model.Comment;
import com.example.lab9.repository.CommentRepository;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Stateful
public class CommentService {

    @EJB
    private CommentRepository repository;

    public List<Comment> getComments() throws SQLException {
        return repository.getComments();
    }

    public Comment getComment(Integer id) throws SQLException {
        return repository.getComment(id);
    }

    public List<Comment> getArticleComments(Integer articleId) throws SQLException {
        return repository.getArticleComments(articleId);
    }

    public List<Comment> getUserComments(Integer authorId) throws SQLException {
        return repository.getUserComments(authorId);
    }

    public Response addComment(Comment comment) {
        return repository.addComment(comment);
    }

    public Response editComment(Comment comment) {
        return repository.editComment(comment);
    }

    public Response deleteComment(Integer id){
        return repository.deleteComment(id);
    }

}
