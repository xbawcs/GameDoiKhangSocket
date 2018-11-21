/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.Message;
import model.User;
import view.GUI_Game;
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
                        this.login(response);
                        break;
                    case "signup":
                        if ("existed".equalsIgnoreCase(response.getText())) {
                            JOptionPane.showMessageDialog(null, "the Account has existed");
                        }
                        break;
                    case "loadOnline":
                        this.game_client.onlineList = response.getList();
                        System.out.println("số người chơi " + "---" + response.getList().size());
                        this.showOnlineList(response.getList());
                        break;
                    case "rank":
                        this.game_client.gui_home.showRank(response.getList());
                        break;
                    case "challenge":
                        this.challenge(response.getUser());
                        break;
                    case "repChallenge":
                        this.repChallenge(response);
                        break;
                    case "result":
                        this.result(response);
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Disconnected!");
            Logger.getLogger(ReceivingThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void login(Message msg) {
        if ("".equals(msg.getText())) {
            this.game_client.user = msg.getUser();
            this.game_client.isLogin = true;
            //Show GUI_Home
            this.game_client.gui_home = new GUI_Home(game_client);
            this.game_client.gui_home.nickName.setText(this.game_client.user.getNickname());
            this.game_client.gui_home.cbStatus.setSelectedItem(this.game_client.user.getStatus() == 1 ? "Online" : "Busy");
            this.game_client.gui_home.setVisible(true);
            //hide gui_login
            this.game_client.gui_login.setVisible(false);
            this.game_client.gui_signup.setVisible(false);
            JOptionPane.showMessageDialog(null, "Login success");
        } else {
            JOptionPane.showMessageDialog(null, msg.getText());
        }
    }
    
    public void showOnlineList(ArrayList<User> users) {
        int online = users.size();
        int idUser = this.game_client.user.getId();
        DefaultListModel<User> model = new DefaultListModel();
        for (User user : users) {
            if (user.getId() != idUser) {
                model.addElement(user);
            }
        }
        GUI_Home.onlineList.setModel(model);
        GUI_Home.onlineList.setCellRenderer(new ItemModel());
    }
    
    public void challenge(User user) throws IOException {
        this.game_client.gui_game.setVisible(false);
        this.game_client.gui_gameover.setVisible(false);
        int comfirm = JOptionPane.showConfirmDialog(null, user.getNickname().toUpperCase() + " wants to challenge you.\n Do you agree?");
        oos = new ObjectOutputStream(this.game_client.socket.getOutputStream());
        if (comfirm == 0) {
            oos.writeObject(new Message("repChallenge", "yes", user));
        } else {
            oos.writeObject(new Message("repChallenge", "no", user));
        }
        oos.flush();
    }
    
    public void repChallenge(Message msg) throws IOException {
        if ("yes".equalsIgnoreCase(msg.getText())) {
            this.game_client.gui_game = new GUI_Game(this.game_client,msg.getList(), this.game_client.socket, Integer.parseInt(msg.getData()[0]), msg.getUser());
            this.game_client.gui_game.play();
        } else {
            JOptionPane.showMessageDialog(null, msg.getUser().getNickname().toUpperCase() + " has refused");
        }
    }
    
    public void result(Message msg) {
        if ("win".equals(msg.getText())) {
            this.game_client.gui_game.showResult("You win!!!");
            this.game_client.gui_game.time.stop();
            return;
        }
        if ("lose".equals(msg.getText())) {
            this.game_client.gui_game.showResult("You lose!!!");
            this.game_client.gui_game.time.stop();
            this.game_client.gui_game.btnSubmit.setEnabled(false);
            return;
        }
        this.game_client.gui_game.showResult("You have a draw!!!");
        this.game_client.gui_game.time.stop();
    }
}
