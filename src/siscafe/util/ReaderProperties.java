/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrador
 */
public class ReaderProperties {
    
    public String getProperties(String keyName) {
        try {
            prop = new Properties();
            prop.load(this.getClass().getClassLoader().getResourceAsStream("META-INF/config.properties"));
            return prop.getProperty(keyName);
        } catch (IOException ex) {
            Logger.getLogger(ReaderProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prop.getProperty(keyName);
    }
    
    private Properties prop = new Properties();
    private FileOutputStream  output;
    private FileInputStream input;
}
