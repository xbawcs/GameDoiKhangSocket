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
import model.Match;
import model.Message;
import model.Player;
import model.Question;
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
//            con.close();
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

    public void signup(String username, String password) {

    }

    public boolean checkUser(String username) {

        return true;
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

    public void repChallenge(GameServer game_server, Message msg, Player player1) throws IOException {
        Player player = getPlayer(game_server.onlinePlayer, msg.getUser().getUsername());
        if ("yes".equals(msg.getText())) {
            ArrayList<Question> questionList = getQuestions(game_server.connect);
            //create match and add into match list
            game_server.matchList.add(new Match(player.user, player1.user));
            Message message = new Message("repChallenge", questionList, "yes", new String[]{player.user.getId() + ""});
            //send the question for player 1
            player.oos.writeObject(message);
            player.oos.flush();
            //send the question for player 2
            player1.oos.writeObject(message);
            player1.oos.flush();
        } else {
            Message message = new Message("repChallenge",player1.user, "no");
            player.oos.writeObject(message);
            player.oos.flush();
        }
    }

    public void result(GameServer game_server, Message msg, Player player1) throws IOException {
        Match match = findMatch(game_server, Integer.parseInt(msg.getData()[0]));
        Player player2 = getPlayer(game_server.onlinePlayer, match.getUser1().getUsername().equalsIgnoreCase(player1.user.getUsername()) ? match.getUser2().getUsername() : match.getUser1().getUsername());

        if ("15".equals(msg.getData()[1])) {
            saveResult(game_server.connect, 1, player1.user);
            //send result to player 1
            player1.oos.writeObject(new Message("result", "win"));
            player1.oos.flush();
            //save and send result to player 2
            saveResult(game_server.connect, 0, player2.user);
            player2.oos.writeObject(new Message("result", "lose"));
            player2.oos.flush();
            game_server.matchList.remove(match);
        } else {
            if (player1.user.getUsername().equals(match.getUser1().getUsername())) {
                match.setText1(new String[]{msg.getData()[0]});
                if (!"".equals(match.getText2()[0])) {
                    //save
                    saveResult(game_server.connect, 0.5, player1.user);
                    //send result to player 1
                    player1.oos.writeObject(new Message("result", "draw"));
                    player1.oos.flush();
                    //save
                    saveResult(game_server.connect, 0.5, player2.user);
                    //send result to player 2
                    player2.oos.writeObject(new Message("result", "draw"));
                    player2.oos.flush();
                    game_server.matchList.remove(match);
                }
            } else {
                match.setText2(new String[]{msg.getData()[0]});
                if (!"".equals(match.getText1()[0])) {
                    //save and send result to player 1
                    saveResult(game_server.connect, 0.5, player1.user);
                    player1.oos.writeObject(new Message("result", "draw"));
                    player1.oos.flush();
                    //save and send result to player 2
                    saveResult(game_server.connect, 0.5, player2.user);
                    player2.oos.writeObject(new Message("result", "draw"));
                    player2.oos.flush();
                    game_server.matchList.remove(match);
                }
            }
        }

    }

    public void updateStatus(User user) {
//        for    {
//
//        }
    }

    public void saveResult(Connection con, double score, User user) {
        int win = score == 1 ? 1 : 0;
        user.setNumOfmatches(user.getNumOfmatches() + 1);
        user.setScore(user.getScore() + score);
        user.setWin(user.getWin() + win);
        try {
            String query = "UPDATE `users` SET `scores` = ?, `win` = ?, `matches` = ? WHERE `username` = ?";
            ResultSet result;

            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setDouble(1, user.getScore());
                ps.setInt(2, user.getWin());
                ps.setInt(3, user.getNumOfmatches());
                ps.setString(4, user.getUsername());
                ps.executeUpdate();
            }
//            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Match findMatch(GameServer game_server, int id) {
        for (Match match : game_server.matchList) {
            if (match.getMatchID() == id) {
                return match;
            }
        }
        return null;
    }

    public Player getPlayer(ArrayList<Player> list, String username) {
        for (Player p : list) {
            if (p.user.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Question> getQuestions(Connection con) {
        String query = "SELECT * FROM `questions` ORDER BY RAND() LIMIT 15";
        ResultSet result;
        ArrayList<Question> list = new ArrayList<>();
        Question question;
        try {
            PreparedStatement ps = con.prepareStatement(query);
            result = ps.executeQuery();
            while (result.next()) {
                question = createQuestion(result);
                list.add(question);
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Question createQuestion(ResultSet result) throws SQLException {
        Question q = new Question();
        q.setId(result.getInt("id"));
        q.setContent(result.getString("content"));
        q.setAnswer1(result.getString("answer1"));
        q.setAnswer2(result.getString("answer2"));
        q.setAnswer3(result.getString("answer3"));
        q.setAnswer4(result.getString("answer4"));
        q.setKey(result.getString("key"));

        return q;
    }

}
