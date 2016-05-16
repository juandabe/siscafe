/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.util;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Administrador
 */

public class ClockPartFrontendView implements Runnable {
    
    public ClockPartFrontendView(JLabel jLabelClock) {
        this.jLabelClock = jLabelClock;
    }
    
    @Override
    public void run() {
        while(true) {
           try {
            this.jLabelClock.setText(new Date().toLocaleString());
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClockPartFrontendView.class.getName()).log(Level.SEVERE, null, ex);
        } 
        }
    }

    private final JLabel jLabelClock;
}
