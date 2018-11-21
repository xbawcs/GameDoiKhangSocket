/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;
import model.Player;
import view.GUI_Server;

/**
 *
 * @author nguye
 */
public class OnlineListThread implements Runnable {

    GUI_Server gui_server;

    public OnlineListThread(GUI_Server gui_server) {
        this.gui_server = gui_server;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                for(Player player : gui_server.onlinePlayer) {
//                    System.out.println(gui_server.onlinePlayer.size() + "--" + gui_server.onlineList.size());
//                    System.out.println(player.socket);
                    player.oos.writeObject(new Message("loadOnline", gui_server.onlineList, gui_server.onlinePlayer.size() + ""));
                    player.oos.flush();
                }
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            gui_server.appendMessage("[InterruptedException]: " + e.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(OnlineListThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
