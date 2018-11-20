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

    private String action = "";
    private String[] data;
    private User user;
    private ArrayList list = new ArrayList<>();
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

    public Message(String action, ArrayList list, String text, String[] data) {
        this.text = text;
        this.action = action;
        this.data = data;
        this.list = list;
    }

    public Message(String action, String text, String[] data) {
        this.text = text;
        this.action = action;
        this.data = data;
    }

    public Message(String action, ArrayList list, String text) {
        this.action = action;
        this.text = text;
        this.list = list;
    }

    public Message(String action, String text, User user) {
        this.text = text;
        this.action = action;
        this.user = user;
    }

    public Message(String action, User user) {
        this.action = action;
        this.user = user;
    }

    public Message(String action, String[] data) {
        this.action = action;
        this.data = data;
    }

    public Message(String action, ArrayList list) {
        this.action = action;
        this.list = list;
    }

    public Message(String action, User user, ArrayList list) {
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

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
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
