/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author D E L L
 */
public class User {
    private String username;
    private String password;
    private float score;
    private int win;
    private int draw;
    private int lose;
    private float avgCompetitor;
    private float avgTime;

    // Constructor không tham số
    public User() {
    }

    // Constructor đầy đủ tham số
    public User(String username, String password, float score, int win, int draw, int lose, float avgCompetitor, float avgTime) {
        this.username = username;
        this.password = password;
        this.score = score;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.avgCompetitor = avgCompetitor;
        this.avgTime = avgTime;
    }

    // Getter và Setter cho các thuộc tính
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public float getAvgCompetitor() {
        return avgCompetitor;
    }

    public void setAvgCompetitor(float avgCompetitor) {
        this.avgCompetitor = avgCompetitor;
    }

    public float getAvgTime() {
        return avgTime;
    }

    public void setAvgTime(float avgTime) {
        this.avgTime = avgTime;
    }

    // Phương thức chuyển đối tượng User thành chuỗi
    @Override
    public String toString() {
        return username + "|" + score + "|" + win + "|" + draw + "|" + lose + "|" + avgCompetitor + "|" + avgTime;
    }
}

