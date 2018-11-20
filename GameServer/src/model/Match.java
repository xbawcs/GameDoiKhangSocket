/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author nguye
 */
public class Match {

    private User user1;
    private User user2;
    private String[] text1 = new String[]{"", ""};
    private String[] text2 = new String[]{"", ""};
    private int matchID;

    public Match(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.matchID = user1.getId();
    }

    public String[] getText1() {
        return text1;
    }

    public void setText1(String[] text1) {
        this.text1 = text1;
    }

    public String[] getText2() {
        return text2;
    }

    public void setText2(String[] text2) {
        this.text2 = text2;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

}
