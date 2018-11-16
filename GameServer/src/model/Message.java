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
    private Player player;
    private ArrayList<Player> list;
    private String error = "";

    public Message() {
    }

    public Message(String action,String error) {
        this.error = error;
        this.action = action;
    }

    public Message(String action, Player player) {
        this.action = action;
        this.player = player;
    }

    public Message(String action, ArrayList<Player> list) {
        this.action = action;
        this.list = list;
    }

    public Message(String action, Player player, ArrayList<Player> list) {
        this.action = action;
        this.player = player;
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Player> getList() {
        return list;
    }

    public void setList(ArrayList<Player> list) {
        this.list = list;
    }

    @Override
    public Object[] toObject() {
        return new Object[]{action, data, player, list, error};
    }

}
