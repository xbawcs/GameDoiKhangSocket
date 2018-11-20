/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameclient;

import Interface.Constant;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.GUI_Game;
import view.GUI_GameOver;
import view.GUI_Home;

/**
 *
 * @author nguye
 */
public class TimeWatch implements Runnable {

    GUI_Game gui_game;
    public int time = Constant.TIME_PLAY;
    boolean isRunning = true;

    public TimeWatch(GUI_Game gui_game) {
        this.gui_game = gui_game;
    }

    public void play() {
        this.gui_game.setVisible(true);
        new Thread(this).start();
    }

    public String showTime(int time) {
        int m = time / 60;
        int s = time % 60;
        return "0" + m + ":" + (s > 9 ? s : "0" + s);
    }
    public void stop() {
        isRunning = false;
    }

    @Override
    public void run() {
        while (isRunning && time > 0) {
            time--;
            gui_game.txtTime.setText(showTime(time));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(TimeWatch.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
