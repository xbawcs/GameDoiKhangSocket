/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameServer;

import Interface.Constant;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Player;
import model.User;
import view.GUI_Server;

/**
 *
 * @author nguye
 */
public class GameServer implements Runnable, Constant {

    ServerSocket serverSocket;
    public static ArrayList<Player> onlineList = new ArrayList<>();
    public static ArrayList<User> allUser = new ArrayList<>();
    public Connection connect = null;
    GUI_Server gui_server;
    boolean keepGoing = true;

    public GameServer(GUI_Server gui_server, int port) {
        //start server
        try {
            this.gui_server = gui_server;
            this.gui_server.appendMessage("[Server]: Server is preparing to start at port " + port);

            this.serverSocket = new ServerSocket(port);
            gui_server.appendMessage("[Server]: Server started");
        } catch (IOException ex) {
            gui_server.appendMessage("[Server]: Server can't start");
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //check connect to mysql
        checkConnectMSQL();
    }

    public void checkConnectMSQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                this.connect = DriverManager.getConnection("jdbc:mysql://" + DB_HOST + ":3306/" + DB_NAME, DB_USERNAME, DB_PASSWORD);
                this.gui_server.appendMessage("[Server]: Server connected to mysql");
            } catch (SQLException ex) {
                System.out.println("Can't connect to mysql.");
                this.gui_server.appendMessage("[Server]: Server can't connect to mysql");
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Error : " + ex);
            Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (keepGoing) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new SocketThread(this, socket)).start();
                
            } catch (IOException ex) {
                gui_server.appendMessage("[Server IOExepion]:  " + ex.getMessage());
                Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void stop() {
        try {
            serverSocket.close();
            keepGoing = false;

            gui_server.appendMessage("[Server]: Server stoped");
            System.out.println("Máy Chủ bị đóng..!");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
