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
import model.Match;
import model.Player;
import model.User;
import view.GUI_Server;

/**
 *
 * @author nguye
 */
public class GameServer implements Runnable, Constant {

    ServerSocket serverSocket;
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
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Error : " + ex);
        }
    }

    @Override
    public void run() {
        while (keepGoing) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(new SocketThread(gui_server, socket, connect)).start();

            } catch (IOException ex) {
                gui_server.appendMessage("[Server IOExepion]:  " + ex.getMessage());
            }
        }

    }

    public void stop() {
        try {
            serverSocket.close();
            keepGoing = false;
            gui_server.appendMessage("[Server]: Server stoped");
            System.out.println("[Server]: Server stoped");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
