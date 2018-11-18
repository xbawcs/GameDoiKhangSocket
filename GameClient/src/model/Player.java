/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author nguye
 */
public class Player implements Serializable {

    public Socket socket;
    public User user;
    public int status;
    public ObjectOutputStream oos;
    
    public Player(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
    }
}
