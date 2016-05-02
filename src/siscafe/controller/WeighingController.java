
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import siscafe.util.BasculeSerialPort;
import siscafe.view.frontend.WeighingView;

/**
 *
 * @author jecheverri
 */
public class WeighingController implements ActionListener{

    public WeighingController(WeighingView weighingView) {
        this.weighingView = weighingView;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void activeBascule() {
        try {
            this.basculeSerialPort = new BasculeSerialPort(this.weighingView.jLabel1);
            this.basculeSerialPort.connect();
        } catch (Exception ex) {
            Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private final WeighingView weighingView;
    private BasculeSerialPort basculeSerialPort;
}
