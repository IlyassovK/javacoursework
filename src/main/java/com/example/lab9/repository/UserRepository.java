package com.example.lab9.repository;

import com.example.lab9.model.User;

import javax.ejb.Stateful;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class UserRepository {

//    private static final String DB_URL = "jdbc:h2:~/test";
    private static final String DB_URL = "jdbc:h2:mem:testInMemory";

    //  Database credentials
    private static final String USER = "sa";
    private static final String PASS = "";

    private Connection conn;
    private Statement stmt;
//    private final PreparedStatement stmt_p = null;

    public UserRepository() throws SQLException {
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

    public List<User> getUsers() throws SQLException {
        String sql = "SELECT * FROM USER_TABLE";
        ResultSet rs = stmt.executeQuery(sql);
        List<User> users = new ArrayList<>();
        while (rs.next()){
            Integer id = rs.getInt("ID");
            String firstName = rs.getString("FIRSTNAME");
            String lastName = rs.getString("LASTNAME");
            String email = rs.getString("EMAIL");
            String password = rs.getString("PASSWORD");
            users.add(new User(id,firstName, lastName, email,password));
        }
        return users;
    }

    public User getUser(Integer id) throws SQLException {
        String sql = "SELECT * FROM USER_TABLE WHERE ID=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        while (rs.next()) {
            String firstName = rs.getString("FIRSTNAME");
            String lastName = rs.getString("LASTNAME");
            String email = rs.getString("EMAIL");
            String password = rs.getString("PASSWORD");
            user = new User(id,firstName, lastName, email,password);
        }
        return user;
    }

    public Response addUser(User user) {
        try{
            String sql = "INSERT INTO USER_TABLE(id, firstName, lastName, email, password) values (?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while adding user").build());
        }
    }

    public Response editUser(User user) {
        try{
            String sql =
                    "UPDATE USER_TABLE SET FIRSTNAME = ?, LASTNAME = ?, EMAIL = ?, PASSWORD = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getId());
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while editing user").build());
        }
    }

    public Response deleteUser(Integer id){
        try {
            String sql = "DELETE FROM USER_TABLE WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            return Response.ok().build();
        }catch (Exception e){
            throw new WebApplicationException(Response.status(500, "Error while deleting user").build());
        }
    }

    public Response addToFavorite(Integer userId, Integer articleId) {
        try {
            String sql = "SELECT * FROM FAVORITE_TABLE WHERE userid=? and articleId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Response.ok("Article is already favorite").build();
            }
            String sql2 = "INSERT INTO FAVORITE_TABLE(userId, articleId) values(?, ?)";
            ps = conn.prepareStatement(sql2);
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ps.executeUpdate();
            return Response.ok("Article has been added to favorite").build();
        }catch (SQLException e) {
            throw new WebApplicationException(Response.status(500, "Error while adding article to favorite").build());
        }
    }

    public Response removeFromFavorite(Integer userId, Integer articleId) {
        try {
            String sql = "SELECT * FROM FAVORITES_TABLE WHERE userId=? and articleId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, articleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sql2 = "DELETE FROM FAVORITES_TABLE WHERE userId=? and articleId=?";
                ps = conn.prepareStatement(sql2);
                ps.setInt(1, userId);
                ps.setInt(2, articleId);
                ps.executeUpdate();
                return Response.ok("Article has been removed from favorites").build();
            }
            return Response.ok("Article isn't in favorites").build();
        }catch (SQLException e) {
            throw new WebApplicationException(Response.status(500, "Error while removing article from favorites").build());
        }
    }

//    public Response deleteUser(User user){
//        try {
//            String sql = "DELETE FROM USER_TABLE WHERE id = ?";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ps.setInt(1, user.getId());
//            ps.executeUpdate();
//            return Response.ok().build();
//        }catch (Exception e){
//            throw new WebApplicationException(Response.status(500, "Error while deleting user").build());
//        }
//    }


}
