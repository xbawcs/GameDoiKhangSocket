/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameclient;

import Interface.Constant;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import model.User;
import view.GUI_Home;
import view.GUI_Login;

/**
 *
 * @author nguye
 */
public class GameClient implements Constant {

    public Socket socket;
    public User user;
    public boolean isLogin = false;
    public ArrayList<User> onlineList = new ArrayList<>();
    public GUI_Home gui_home;
    public GUI_Login gui_login;

    public void init() {
        try {
            //Create socket 
            this.socket = new Socket(IP_SERVER, SOCKET_PORT);
            new Thread(new ReceivingThread(this)).start();

            this.gui_login = new GUI_Login(this);
            this.gui_login.setVisible(true);
        } catch (IOException ex) {
            Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        GameClient game_client = new GameClient();
        game_client.init();
    }
}
