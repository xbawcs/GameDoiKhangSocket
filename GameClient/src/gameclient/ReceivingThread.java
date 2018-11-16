/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import model.Message;
import model.Player;
import view.GUI_Home;

/**
 *
 * @author nguye
 */
public class ReceivingThread implements Runnable {

    GameClient game_client;
    ObjectInputStream ois;
    boolean is_running = true;
    Message response;

    public ReceivingThread(GameClient gameclient) {
        this.game_client = gameclient;
    }

    @Override
    public void run() {
        try {
            ois = new ObjectInputStream(game_client.socket.getInputStream());
            while (is_running) {
                response = (Message) ois.readObject();
                switch (response.getAction()) {
                    case "login":
                        if ("".equals(response.getError())) {
                            JOptionPane.showMessageDialog(null, "Login success");
                            //Show GUI_Home
                            this.game_client.gui_home = new GUI_Home(game_client);
                            this.game_client.gui_home.setVisible(true);
                            //hide gui_login
                            this.game_client.gui_login.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, response.getError());
                        }
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Disconnected!");
            System.out.println("[Error]: " + ex.toString());
        }
    }

}
