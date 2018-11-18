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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import model.Message;
import model.Player;
import model.User;
import view.GUI_Home;
import view.ItemModel;

/**
 *
 * @author nguye
 */
public class ReceivingThread implements Runnable {

    GameClient game_client;
    ObjectInputStream ois;
    ObjectOutputStream oos;
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
                        if ("".equals(response.getText())) {
                            JOptionPane.showMessageDialog(null, "Login success");
                            //
                            this.game_client.user = response.getUser();
                            //Show GUI_Home
                            this.game_client.gui_home = new GUI_Home(game_client);
                            this.game_client.gui_home.nickName.setText(this.game_client.user.getNickname());
                            this.game_client.gui_home.setVisible(true);
                            //hide gui_login
                            this.game_client.gui_login.setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, response.getText());
                        }
                        break;
                    case "loadOnline":
                        this.game_client.onlineList = response.getList();
                        this.showOnlineList(this.game_client.onlineList);
                        break;
                    case "challenge":
                        this.challenge(response.getUser());
                        break;
                    case "repChallenge":
                        this.repChallenge(response);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Disconnected!");
            Logger.getLogger(ReceivingThread.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("[Error]: " + ex.toString());
        }
    }

    public void showOnlineList(ArrayList<User> users) {
        int online = users.size();
        int idUser = this.game_client.user.getId();

        DefaultListModel model = new DefaultListModel();
        JList<User> list = this.game_client.gui_home.onlineList;

        for (User user : users) {
            if (user.getId() != idUser) {
                model.addElement(user);
            }
        }
        list.setModel(model);
        list.setCellRenderer(new ItemModel());
    }

    public void challenge(User user) throws IOException {
        int comfirm = JOptionPane.showConfirmDialog(null, user.getNickname().toUpperCase() + " wants to challenge you.\n Do you agree?");
        oos = new ObjectOutputStream(this.game_client.socket.getOutputStream());
        if (comfirm == 0) {
            oos.writeObject(new Message("repChallenge", this.game_client.user, "yes"));
        } else {
            oos.writeObject(new Message("repChallenge", this.game_client.user, "no"));
        }
        oos.flush();
    }

    public void repChallenge(Message msg) throws IOException {
        if("yes".equalsIgnoreCase(msg.getText())) {
            JOptionPane.showMessageDialog(null, msg.getUser().getNickname().toUpperCase() + " has accepted");
        } else {
            JOptionPane.showMessageDialog(null, msg.getUser().getNickname().toUpperCase() + " has refused");
        }
    }
}
