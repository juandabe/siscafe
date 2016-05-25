
package siscafe;

import siscafe.util.ScreenSplash;
//import siscafe.view.frontend.Frontend;


/**
 *
 * @author jecheverri
 */
public class SISCAFE {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new ScreenSplash()::animar);
    }
}
