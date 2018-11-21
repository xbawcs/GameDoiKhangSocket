/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Interface.ToObject;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author nguye
 */
public class User implements ToObject, Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String nickname;
    private String password;
    private int numOfmatches;//Số game đã chơi
    private int win;
    private int status; //trạng thái của ng chới(avaliable or busy)
    private double scores;
    private int time;

    public User() {
    }

    public User(HashMap user) {
        this.id = Integer.parseInt(user.get("id") + "");
        this.nickname = user.get("nickname") + "";
        this.numOfmatches = Integer.parseInt(user.get("numOfMatches") + "");
        this.password = user.get("password") + "";
        this.scores = Integer.parseInt(user.get("scores") + "");
        this.status = Integer.parseInt(user.get("status") + "");
        this.username = user.get("username") + "";
        this.win = Integer.parseInt(user.get("win") + "");
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumOfmatches() {
        return numOfmatches;
    }

    public void setNumOfmatches(int numOfmatches) {
        this.numOfmatches = numOfmatches;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getScore() {
        return scores;
    }

    public void setScore(double scores) {
        this.scores = scores;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public Object[] toObject() {
        return new Object[]{id, username, nickname, password, numOfmatches, win, status, scores, time};
    }
}
