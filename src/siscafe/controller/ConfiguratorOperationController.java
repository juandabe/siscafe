/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import siscafe.model.StoresCaffee;
import siscafe.persistence.StoresCaffeeJpaController;
import siscafe.util.MyComboBoxModel;
import siscafe.util.ReaderProperties;
import siscafe.view.frontend.ConfiguratorOperationView;

/**
 *
 * @author Administrador
 */
public class ConfiguratorOperationController implements ActionListener{
    
    public ConfiguratorOperationController(ConfiguratorOperationView configuratorOperationView) {
        this.configuratorOperationView = configuratorOperationView;
        storesCaffeeJpaController = new StoresCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        listStoresCaffee = storesCaffeeJpaController.findStoresCaffeeEntities();
        myComboBoxModel = new MyComboBoxModel(listStoresCaffee);
    }
    
    public void initListener() {
        configuratorOperationView.jButton2.addActionListener(this);
        configuratorOperationView.jButton5.addActionListener(this);
        configuratorOperationView.jComboBox2.setModel(myComboBoxModel);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "edit":
                edit();
            break;
            case "refresh":
                refresh();
            break;
        }
    }
    
    private void edit() {
        String storesCaffeeName = (String) configuratorOperationView.jComboBox1.getSelectedItem();
        String operationType = (String) configuratorOperationView.jComboBox1.getSelectedItem();
        if(storesCaffeeName.isEmpty()) {
            JOptionPane.showInternalMessageDialog(configuratorOperationView, "Por favor dilefencie los campos correctamente", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            StoresCaffee storesCaffee  = findStoresCaffeeByNameLocal(storesCaffeeName);
            try {
                Properties prop = new Properties();
                prop.setProperty("STORE", storesCaffee.getStoreName());
                prop.setProperty("WEIGHTOPERATION", operationType);
                FileOutputStream fileProperties = new FileOutputStream("C:\\SISCAFE\\dist\\config.properties");
                prop.store(fileProperties, "");
                fileProperties.close();
            } catch (IOException ex) {
                Logger.getLogger(ReaderProperties.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showInternalMessageDialog(configuratorOperationView, "Se regisrto correctamente las variables", "Registro correcto", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void refresh() {
    }
    
    private StoresCaffee findStoresCaffeeByNameLocal(String name) {
        Iterator<StoresCaffee> iterator = this.listStoresCaffee.iterator();
        StoresCaffee storesCaffee = null;
        while(iterator.hasNext()) {
            storesCaffee = iterator.next();
            if(storesCaffee.getStoreName().matches(name)) {
                return storesCaffee;
            }
        }
        return storesCaffee;
    }
    
    private ConfiguratorOperationView configuratorOperationView;
    private List<StoresCaffee> listStoresCaffee;
    private StoresCaffeeJpaController storesCaffeeJpaController;
    private MyComboBoxModel myComboBoxModel;
}
