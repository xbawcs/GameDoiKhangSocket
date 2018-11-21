/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.sql.Connection;
import model.Message;
import model.Player;
import model.User;
import view.GUI_Server;

/**
 *
 * @author nguye
 */
public class SocketThread implements Runnable, Serializable {

    Player player;
    GUI_Server gui_server;
    ObjectInputStream ois;
    Boolean is_running = true;
    Connection con;
    Request request;

    public SocketThread(GUI_Server gui_server, Socket socket, Connection con) {
        try {
            this.player = new Player(socket);
            this.player.oos = new ObjectOutputStream(socket.getOutputStream());
            this.gui_server = gui_server;
            this.gui_server.appendMessage("[Player]: Connected at port " + socket.getPort());
            this.con = con;
            request = new Request(this.con);
        } catch (IOException ex) {
            this.gui_server.appendMessage("[SocketThreadExeption]: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        Message msg = null;
        try {
            while (is_running) {
                ois = new ObjectInputStream(this.player.socket.getInputStream());
                //read message
                msg = (Message) ois.readObject();
                switch (msg.getAction()) {
                    case "login":
                        request.login(msg, this.player, this.gui_server);
                        break;
                    case "signup":
                        request.signup(msg.getData(), this.player, gui_server);
                        break;
                    case "logout":

                        break;
                    case "loadOnline":
                        request.sendOnlineList(this.player.oos, gui_server.onlineList);
                        break;
                    case "rank":
                        request.sendRank(msg.getText(),this.player);
                        break;
                    case "challenge":
                        request.challenge(gui_server.onlinePlayer, msg.getUser(), this.player.user);
                        break;
                    case "repChallenge":
                        request.repChallenge(gui_server, msg, this.player);
                        break;
                    case "result":
                        request.result(gui_server, msg, this.player);
                        break;
                    case "replay":
                        request.challenge(gui_server.onlinePlayer, msg.getUser(), this.player.user);
                        break;
                    case "updateStatus":
                        request.updateStatus(player, gui_server, msg.getText());
                        break;
                    default:

                        break;
                }

            }
        } catch (IOException ex) {
            gui_server.onlineList.remove(this.player.user);
            gui_server.onlinePlayer.remove(this.player);
            gui_server.appendMessage("[Player] " + player.user.getNickname() + ": disconnected!");
            System.out.println("[Player] " + player.user.getNickname() + ": disconnected!");

        } catch (ClassNotFoundException ex) {
            gui_server.appendMessage("[Server]: unreadable message of " + player.user.getNickname());
        }
    }
}
