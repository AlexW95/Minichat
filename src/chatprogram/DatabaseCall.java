package chatprogram;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseCall {

    static DataInputStream dataInputStream;
    static DataOutputStream dataOutputStream;

    private String JDBC_DRIVER;
    private String DB_URL;
    //Database credentials
    private String USER;
    private String PASS;
    private Connection conn;
    private String query;

    public DatabaseCall() {
        JDBC_DRIVER = "com.mysql.jdbc.Driver";
        DB_URL = "jdbc:mysql://localhost:3306/chatthandler?zeroDateTimeBehavior=convertToNull";
        USER = "root";
        PASS = "root";
        conn = null;
        openConnection();
    }

    public void openConnection() {
        try {
            //Register JDBC driver
            Class.forName(JDBC_DRIVER);
            //Open a connection
            System.out.print("Connecting to a selected database... ");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            //Handle errors for JDBC
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean checkIfUserExists(String username, String password) {
        if (checkIfColumnIsExisting(username, "username", "USERS") && checkIfColumnIsExisting(password, "password", "USERS")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfColumnIsExisting(String columnValue, String columnName, String table) {
        query = "SELECT " + columnName + " FROM " + table + " WHERE " + columnName + " = \"" + columnValue + "\"";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (columnValue.equals(resultSet.getString(columnName))) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int getUserID(String username) {
        query = "SELECT id FROM users WHERE username= \"" + username + "\"";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    public boolean addNewUserToDatabase(String username, String password) {
        String insertQuery = "INSERT INTO users(username, password) values(\"" + username + "\", \"" + password + "\");";
        String checkSuccessQuery = "SELECT username FROM USERS WHERE username = \"" + username + "\"";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = conn.prepareStatement(insertQuery);
            preparedStatement.execute();
            preparedStatement = conn.prepareStatement(checkSuccessQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username)) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return false;
    }

    public String addMessage(String message, int userId, String time, int conversationId) {
        query = "INSERT INTO conversation_reply(reply,userId,time,conversationID) VALUES(\"" + message + "\", " + userId + ", \"" + time + "\", " + conversationId + ");";
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        try {
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return time + " " + message;
    }

    public ArrayList<String> getMessages(int conversationID, boolean limit) {
        query = "select u.username, cr.reply, cr.time from conversation c\n"
                + " inner join conversation_reply cr on c.id = cr.conversationID\n"
                + " inner join users u on u.id = cr.userId\n"
                + " where c.id = " + conversationID + "\n";

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        ArrayList<String> messages = new ArrayList<>();
        try {
            if (limit) {
                query += "order by time desc limit 1";
            } else {
                query += "order by time asc";

            }
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            messages.add("");
            while (resultSet.next()) {
                messages.add(resultSet.getString("cr.time"));
                messages.add(resultSet.getString("u.username"));
                messages.add(resultSet.getString("cr.reply"));
            }
            return messages;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<String>();
    }

    public ArrayList<Integer> getChatRooms() {
        query = "Select id from conversation;";
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        ArrayList<Integer> ids = new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ids.add(resultSet.getInt("id"));
            }
            return ids;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseCall.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Integer>();
    }
}
