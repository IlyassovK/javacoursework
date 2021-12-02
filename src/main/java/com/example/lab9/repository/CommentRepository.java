package com.example.lab9.repository;

import com.example.lab9.model.Comment;

import javax.ejb.Stateful;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class CommentRepository {

//    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_URL = "jdbc:h2:mem:testInMemory";

    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection conn;
    private Statement stmt;
//    private final PreparedStatement stmt_p = null;

    public CommentRepository() throws SQLException {
        // STEP 1: Register JDBC driver
        System.out.println("Start");
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("h2 JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }
        // STEP 2: Open a connection
        System.out.println("Connecting to a selected database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        // conn.setAutoCommit(false);
        System.out.println("Connected database successfully...");

        // STEP 3: Execute a query
        stmt = conn.createStatement();

    }

    public List<Comment> getComments() throws SQLException {
        String sql = "SELECT * FROM COMMENT_TABLE";
        ResultSet rs = stmt.executeQuery(sql);
        List<Comment> comments = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("ID");
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");
            int authorId = rs.getInt("authorId");
            int articleId = rs.getInt("articleId");
            comments.add(new Comment(id, text, date,authorId, articleId));
        }
        return comments;
    }

    public Comment getComment(int id) throws SQLException {
        String sql = "SELECT * FROM COMMENT_TABLE WHERE ID=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Comment comment = new Comment();
        while (rs.next()) {
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");
            int authorId = rs.getInt("authorId");
            int articleId = rs.getInt("articleId");
            comment = new Comment(id, text, date,authorId, articleId);
        }
        return comment;
    }

    public List<Comment> getArticleComments(Integer articleId) throws SQLException {
        String sql = "SELECT * FROM COMMENT_TABLE WHERE articleId=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, articleId);
        ResultSet rs = ps.executeQuery();
        List<Comment> comments = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("ID");
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");
            int authorId = rs.getInt("authorId");
            comments.add(new Comment(id, text, date,authorId, articleId));
        }
        return comments;
    }

    public List<Comment> getUserComments(Integer authorId) throws SQLException {
        String sql = "SELECT * FROM COMMENT_TABLE WHERE authorId=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, authorId);
        ResultSet rs = ps.executeQuery();
        List<Comment> comments = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("ID");
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");
            int articleId = rs.getInt("articleId");
            comments.add(new Comment(id, text, date,authorId, articleId));
        }
        return comments;
    }

    public Response addComment(Comment comment) {
        try{
            String sql = "INSERT INTO COMMENT_TABLE(id, text, date, authorId, articleId) values (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, comment.getId());
            ps.setString(2, comment.getText());
            ps.setString(3, comment.getDate());
            ps.setInt(4, comment.getAuthorId());
            ps.setInt(5, comment.getArticleId());
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while adding comment").build());
        }
    }

    public Response editComment(Comment comment) {
        try{
            String sql =
                    "UPDATE COMMENT_TABLE SET TEXT = ?, DATE = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, comment.getText());
            ps.setString(2, comment.getDate());
            ps.setInt(3, comment.getId());
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while editing comment").build());
        }
    }

    public Response deleteComment(Integer id){
        try {
            String sql = "DELETE FROM COMMENT_TABLE WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while deleting comment").build());
        }
    }

    public Response deleteComments(Integer articleId) {
        try {
            String sql = "DELETE FROM COMMENT_TABLE WHERE articleId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, articleId);
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while deleting comments").build());
        }
    }

//    public Response deleteComment(Comment comment){
//        try {
//            String sql = "DELETE FROM COMMENT_TABLE WHERE id = ?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, comment.getId());
//            ps.executeUpdate();
//            return Response.ok().build();
//        }catch (Exception e){
//            throw new WebApplicationException(Response.status(500, "Error while deleting comment").build());
//        }
//    }


}
