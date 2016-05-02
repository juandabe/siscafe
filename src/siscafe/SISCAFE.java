
package siscafe;

import siscafe.view.frontend.Frontend;


/**
 *
 * @author jecheverri
 */
public class SISCAFE {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Frontend frontend = new Frontend();
                frontend.initConfig();
                frontend.setVisible(true);
            }
        });
    }
}
