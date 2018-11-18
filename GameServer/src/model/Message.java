/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import Interface.ToObject;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author nguye
 */
public class Message implements ToObject, Serializable {

    public static final long serialVersionUID = -3040096452457271695L;
    private String action = "";
    private String[] data;
    private User user;
    private ArrayList<User> list;
    private String text = "";

    public Message() {
    }

    public Message(String action) {
        this.action = action;
    }

    public Message(String action, String text) {
        this.text = text;
        this.action = action;
    }

    public Message(String action, User user) {
        this.action = action;
        this.user = user;
    }

    public Message(String action, String[] data) {
        this.action = action;
        this.data = data;
    }

    public Message(String action, ArrayList<User> list) {
        this.action = action;
        this.list = list;
    }

    public Message(String action, User user, ArrayList<User> list) {
        this.action = action;
        this.user = user;
        this.list = list;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<User> getList() {
        return list;
    }

    public void setList(ArrayList<User> list) {
        this.list = list;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Object[] toObject() {
        return new Object[]{action, data, user, list, text};
    }
}
