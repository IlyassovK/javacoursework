package com.example.lab9.repository;

import com.example.lab9.model.Article;

import javax.ejb.Stateful;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class ArticleRepository {

//    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_URL = "jdbc:h2:mem:testInMemory";

    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection conn;
    private Statement stmt;
//    private final PreparedStatement stmt_p = null;

    public ArticleRepository() throws SQLException {
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

    public List<Article> getArticles() throws SQLException {
        String sql = "SELECT * FROM ARTICLE_TABLE";
        ResultSet rs = stmt.executeQuery(sql);
        List<Article> articles = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("ID");
            Integer authorId = rs.getInt("authorId");
            String title = rs.getString("TITLE");
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");
            String theme = rs.getString("THEME");

            String sql2 = "SELECT COUNT(ID) FROM LIKES_TABLE WHERE articleId=?";
            PreparedStatement ps = conn.prepareStatement(sql2);
            ps.setInt(1, id);
            ResultSet rs2 = ps.executeQuery();
            Integer likes = 0;
            while (rs2.next()) {
                likes = rs.getInt("LIKES");
            }

            articles.add(new Article(id,title, text, date, theme,likes, authorId ));
        }
        return articles;
    }

    public Article getArticles(String theme) throws SQLException {
        String sql = "SELECT * FROM ARTICLE_TABLE WHERE THEME=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, theme);
        ResultSet rs = ps.executeQuery();
        Article article = new Article();
        while (rs.next()) {
            Integer id = rs.getInt("ID");
            Integer authorId = rs.getInt("authorId");
            String title = rs.getString("TITLE");
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");

            String sql2 = "SELECT COUNT(ID) FROM LIKES_TABLE WHERE articleId=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();
            Integer likes = 0;
            while (rs2.next()) {
                likes = rs.getInt("LIKES");
            }

            article = new Article(id,title, text, date, theme,likes,authorId);
        }
        return article;
    }

    public Article getArticleInfo(Integer id) throws SQLException {
        String sql = "SELECT * FROM ARTICLE_TABLE WHERE ID=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Article article = new Article();
        while (rs.next()) {
            Integer authorId = rs.getInt("authorId");
            String title = rs.getString("TITLE");
            String text = rs.getString("TEXT");
            String date = rs.getString("DATE");
            String theme = rs.getString("THEME");

            String sql2 = "SELECT COUNT(ID) FROM LIKES_TABLE WHERE articleId=?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1, id);
            ResultSet rs2 = ps2.executeQuery();
            Integer likes = 0;
            while (rs2.next()) {
                likes = rs.getInt("LIKES");
            }

            article = new Article(id,title, text, date, theme,likes, authorId);
        }
        return article;
    }

    public Response addArticle(Article article) {
        try{
            String sql = "INSERT INTO ARTICLE_TABLE(id, auhorId, title, text, date, theme, likes) values (?,?,?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, article.getId());
            ps.setInt(2, article.getAuthorId());
            ps.setString(3, article.getTitle());
            ps.setString(4, article.getText());
            ps.setString(5, article.getDate());
            ps.setString(6, article.getTheme());
            ps.setInt(7, 0);
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while adding article").build());
        }
    }

    public Response editArticle(Article article) {
        try{
            String sql =
                    "UPDATE ARTICLE_TABLE SET TITLE = ?, TEXT = ?, authorId = ?, DATE = ?, THEME = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, article.getTitle());
            ps.setString(2, article.getText());
            ps.setInt(3, article.getAuthorId());
            ps.setString(4, article.getDate());
            ps.setString(5, article.getTheme());
            ps.setInt(6, article.getId());
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while editing article").build());
        }
    }

    public Response deleteArticle(Integer id){
        try {
            String sql = "DELETE FROM ARTICLE_TABLE WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while deleting article").build());
        }
    }

    public Response like(Integer userId, Integer articleId) {
        try {
            String sql = "SELECT * FROM LIKES_TABLE WHERE userid=? and articleId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Response.ok("Article is already liked").build();
            }
            String sql2 = "INSERT INTO LIKES_TABLE(userId, articleId) values(?, ?)";
            ps = conn.prepareStatement(sql2);
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ps.executeUpdate();
            return Response.ok("Article has been liked").build();
        }catch (SQLException e) {
            throw new WebApplicationException(Response.status(500, "Error while liking article").build());
        }
    }

    public Response unLike(Integer userId, Integer articleId) {
        try {
            String sql = "SELECT * FROM LIKES_TABLE WHERE userId=? and articleId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sql2 = "DELETE FROM LIKES_TABLE WHERE userId=? and articleId=?";
                ps = conn.prepareStatement(sql2);
                ps.setInt(1, userId);
                ps.setInt(2, articleId);
                ps.executeUpdate();
                return Response.ok("Article has been unliked").build();
            }
            return Response.ok("Article isn't liked").build();
        }catch (SQLException e) {
            throw new WebApplicationException(Response.status(500, "Error while unliking article").build());
        }
    }

    public Response deleteLikes(Integer articleId) {
        try {
            String sql = "DELETE FROM LIKES_TABLE WHERE articleId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, articleId);
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while deleting comment").build());
        }
    }

//    public Response deleteArticle(Article article){
//        try {
//            String sql = "DELETE FROM ARTICLE_TABLE WHERE id = ?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, article.getId());
//            ps.executeUpdate();
//            return Response.ok().build();
//        }catch (Exception e){
//            throw new WebApplicationException(Response.status(500, "Error while deleting article").build());
//        }
//    }
}
