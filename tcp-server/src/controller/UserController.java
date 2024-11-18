/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connection.DatabaseConnection;
import model.UserModel;
/**
 *
 * @author admin
 */
public class UserController {
    //  SQL
    private final String INSERT_USER = "INSERT INTO users (username, password, score, win, draw, lose, avgCompetitor, avgTime) VALUES (?, ?, 0, 0, 0, 0, 0, 0)";
    
    private final String CHECK_USER = "SELECT userId from users WHERE username = ? limit 1";
    
    private final String LOGIN_USER = "SELECT username, password, score, win, draw, lose FROM users WHERE username=? AND password=?";
    
    private final String GET_INFO_USER = "SELECT username, password, score, win, draw, lose, avgCompetitor, avgTime FROM users WHERE username=?";
    
    private final String UPDATE_USER = "UPDATE users SET score = ?, win = ?, draw = ?, lose = ?, avgCompetitor = ?, avgTime = ? WHERE username=?";
    
    private final String GET_RANKING = "SELECT username, score, win, draw, lose, avgCompetitor, avgTime FROM users ORDER BY score DESC, avgCompetitor DESC, avgTime ASC LIMIT 10";

    //  Instance
    private final Connection con;
    
    public UserController() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    public String register(String username, String password) {
        try {
            PreparedStatement p = con.prepareStatement(CHECK_USER);
            p.setString(1, username);
            ResultSet r = p.executeQuery();

            if (r.next()) {
                return "failed; User Already Exists";
            } else {
                r.close();
                p.close();

                // Chèn dữ liệu mới nếu không tồn tại
                p = con.prepareStatement(INSERT_USER);
                p.setString(1, username);
                p.setString(2, password);
                p.executeUpdate();
                p.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "failed; Error occurred while registering user.";
        }
        return "success;";
    }

  
    public String login(String username, String password) {
        try {
            PreparedStatement p = con.prepareStatement(LOGIN_USER);
            p.setString(1, username);
            p.setString(2, password);
            ResultSet r = p.executeQuery();

            // Sử dụng next() thay vì first()
            if (r.next()) {
                float score = r.getFloat("score");
                int win = r.getInt("win");
                int draw = r.getInt("draw");
                int lose = r.getInt("lose");
                return "success;" + username + ";" + score + ";" + win + ";" + draw + ";" + lose;
            } else {
                return "failed;" + "Please enter the correct account password!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getInfoUser(String username) {
        UserModel user = new UserModel();
        try {
            PreparedStatement p = con.prepareStatement(GET_INFO_USER);
            p.setString(1, username);
            
            ResultSet r = p.executeQuery();
            while(r.next()) {
                user.setUserName(r.getString("username"));
                user.setScore(r.getFloat("score"));
                user.setWin(r.getInt("win"));
                user.setDraw(r.getInt("draw"));
                user.setLose(r.getInt("lose"));
                user.setAvgCompetitor(r.getFloat("avgCompetitor"));
                user.setAvgTime(r.getFloat("avgTime"));
            }
            return "success;" + user.getUserName() + ";" + user.getScore() + ";" + user.getWin() + ";" + user.getDraw() + ";" + user.getLose() + ";" + user.getAvgCompetitor() + ";" + user.getAvgTime() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return null;
    }
    
    public boolean updateUser(UserModel user) throws SQLException {
        boolean rowUpdated;
        PreparedStatement p = con.prepareStatement(UPDATE_USER);
        //  Login User 
        p.setFloat(1, user.getScore());
        p.setInt(2, user.getWin());
        p.setInt(3, user.getDraw());
        p.setInt(4, user.getLose());
        p.setFloat(5, user.getAvgCompetitor());
        p.setFloat(6, user.getAvgTime());
        p.setString(7, user.getUserName());

//            ResultSet r = p.executeQuery();
        rowUpdated = p.executeUpdate() > 0;
        return rowUpdated;
    }

    public UserModel getUser(String username) {
        UserModel user = new UserModel();
        try {
            PreparedStatement p = con.prepareStatement(GET_INFO_USER);
            p.setString(1, username);
            
            ResultSet r = p.executeQuery();
            while(r.next()) {
                user.setUserName(r.getString("username"));
                user.setScore(r.getFloat("score"));
                user.setWin(r.getInt("win"));
                user.setDraw(r.getInt("draw"));
                user.setLose(r.getInt("lose"));
                user.setAvgCompetitor(r.getFloat("avgCompetitor"));
                user.setAvgTime(r.getFloat("avgTime"));
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }   
        return null;
    }
    
    public String getRanking() {
        StringBuilder ranking = new StringBuilder();
        try {
            PreparedStatement p = con.prepareStatement(GET_RANKING);
            ResultSet rs = p.executeQuery();

            while (rs.next()) {
                String username = rs.getString("username");
                float score = rs.getFloat("score");
                int win = rs.getInt("win");
                int draw = rs.getInt("draw");
                int lose = rs.getInt("lose");
                float avgCompetitor = rs.getFloat("avgCompetitor");
                float avgTime = rs.getFloat("avgTime");

                // Gắn kết các trường thành một bản ghi
                ranking.append(username).append("|")
                       .append(score).append("|")
                       .append(win).append("|")
                       .append(draw).append("|")
                       .append(lose).append("|")
                       .append(avgCompetitor).append("|")
                       .append(avgTime).append(";"); // Dùng ";" để phân cách các bản ghi
            }

            // Xóa dấu ";" cuối cùng (nếu có)
            if (ranking.length() > 0) {
                ranking.setLength(ranking.length() - 1);
            }
            return ranking.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "error; Unable to retrieve rankings.";
        }
    }

}

