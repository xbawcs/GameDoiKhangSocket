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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;
import model.Player;
import model.User;

/**
 *
 * @author nguye
 */
public class SocketThread extends Request implements Runnable, Serializable {

    Player player;
    GameServer game_server;
    ObjectInputStream ois;
    Boolean is_running = true;

    public SocketThread(GameServer game_server, Socket socket) {
        try {
            this.player = new Player(socket);
            this.player.oos = new ObjectOutputStream(socket.getOutputStream());
            this.game_server = game_server;
            this.game_server.gui_server.appendMessage("[Player]: Connected at port " + socket.getPort());
        } catch (IOException ex) {
            this.game_server.gui_server.appendMessage("[SocketThreadExeption]: " + ex.getMessage());
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
                        this.login(msg);
                        this.sendOnlineList(this.player.oos, this.game_server.onlineList);
                        break;
                    case "signup":
                        break;
                    case "logout":

                        break;
                    case "loadOnline": 
                        this.sendOnlineList(this.player.oos, this.game_server.onlineList);
                        break;
                    case "challenge":
                        this.challenge(this.game_server.onlinePlayer, msg.getUser(), this.player.user);
                        break;
                    case "repChallenge": 
                        this.repChallenge(this.game_server.onlinePlayer, msg);
                        break;
                    default:
                        
                        break;
                }

            }
        } catch (IOException ex) {
            game_server.onlineList.remove(this.player.user);
            game_server.onlinePlayer.remove(this.player);
            game_server.gui_server.appendMessage("[Player] " + player.user.getNickname() + ": disconnected!");
            System.out.println("[Player] " + player.user.getNickname() + ": disconnected!");

        } catch (ClassNotFoundException ex) {
            game_server.gui_server.appendMessage("[Server]: unreadable message of " + player.user.getNickname());
        }
    }

    public void login(Message msg) throws IOException {
        if (this.checkLogin(this.game_server.onlineList, msg.getData()[0])) {
            User user = this.getUser(this.game_server.connect, msg.getData()[0], msg.getData()[1]);
            if (user != null) {
                this.player.user = user;
                this.player.status = 1;
                //add player to the online players list
                this.game_server.onlinePlayer.add(this.player);
                this.game_server.onlineList.add(this.player.user);
                //send a message to the requested player
                this.player.oos.writeObject(new Message("login", this.player.user));
                this.player.oos.flush();
                return;
            }
            this.player.oos.writeObject(new Message("login", "Username or password are incorrect"));
            this.player.oos.flush();
            return;
        }
        this.player.oos.writeObject(new Message("login", "Player is online"));
        this.player.oos.flush();
    }
}
