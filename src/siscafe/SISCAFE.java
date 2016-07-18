
package siscafe;

import java.util.Iterator;
import java.util.List;
import siscafe.util.ScreenSplash;

//import siscafe.view.frontend.Frontend;


/**
 *
 * @author jecheverri
 */
public class SISCAFE {
    public static void main(String[] args) {
        //GetContainerList getContainerList = new GetContainerList();
        //getContainerList.setSFecha("20160623");
        //GetContainerListResponse getContainerListResponse = new GetContainerListResponse();
        //InfoLlenadoCOPC getContainerListResult = getContainerListResponse.getGetContainerListResult();
        java.awt.EventQueue.invokeLater(new ScreenSplash()::animar);
    }
}
