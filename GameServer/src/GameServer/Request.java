/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.MD5;
import model.Message;
import model.Player;
import model.User;

/**
 *
 * @author nguye
 */
public class Request {

    public Request() {
    }

    public User getUser(Connection con, String username, String password) {
        try {
            String query = "SELECT * From `users` WHERE `username` = ? AND `password` = ?";
            ResultSet result;

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, username);
                ps.setString(2, MD5.md5(password));

                result = ps.executeQuery();
                result.last();

                if (result.getRow() != 0) {
                    return createUser(result);
                }
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean checkLogin(ArrayList<User> list, String username) {
        for (User user : list) {
            if (username.equals(user.getUsername())) {
                return false;
            }
        }
        return true;
    }

    public void signup() {

    }

    public User createUser(ResultSet result) {
        try {
            User user = new User();
            user.setId(result.getInt("id"));
            user.setNickname(result.getString("nickname"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));
            user.setNumOfmatches(result.getInt("matches"));
            user.setWin(result.getInt("win"));
            user.setScore(result.getInt("scores"));
            user.setStatus(1);

            return user;
        } catch (SQLException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void sendOnlineList(ObjectOutputStream oos, ArrayList<User> list) throws IOException {
        oos.writeObject(new Message("loadOnline", list));
        oos.flush();
    }

    public void challenge(ArrayList<Player> list, User user1, User user2) throws IOException {
        Player player = getPlayer(list, user1.getUsername());
        ObjectOutputStream oos = player.oos;
        oos.writeObject(new Message("challenge", user2));
        oos.flush();
    }
    public void repChallenge(ArrayList<Player> list, Message msg) throws IOException {
        Player player = getPlayer(list, msg.getUser().getUsername());
        ObjectOutputStream oos = player.oos;
        oos.writeObject(msg);
        oos.flush();
    }
    public Player getPlayer(ArrayList<Player> list, String username) {
        for (Player p : list) {
            if (p.user.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }
}
