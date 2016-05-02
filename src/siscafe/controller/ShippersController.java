/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import siscafe.model.Shippers;
import siscafe.persistence.ShippersJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.ShippersView;

/**
 *
 * @author Administrador
 */
public class ShippersController  implements ActionListener, ListSelectionListener{
    
    public ShippersController(Shippers shippers, ShippersView shippersView){
        this.shippers = shippers;
        this.shippersView = shippersView;
        this.shippersJpaController = new ShippersJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listShippers = shippersJpaController.findShippersEntities();
        GenericListModel genericListModel = new GenericListModel();
        listShippers.stream().forEach((ShippersFor) -> {
            genericListModel.add(ShippersFor.getBusinessName());
        });
        this.shippersView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.shippersView.jButton4.addActionListener(this);
        this.shippersView.jButton2.addActionListener(this);
        this.shippersView.jButton3.addActionListener(this);
        this.shippersView.jButton5.addActionListener(this);
        this.shippersView.jList3.addListSelectionListener(this);
        refresh();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "add":
                add();
            break;
            case "edit":
                edit();
            break;
            case "refresh":
                refresh();
            break;
            case "clear":
                clear();
            break;
            case "delete":
                //clear();
            break;
        }
    }
    
    private void add() {
        Date dNow = new Date( );
        this.shippers.setCreatedDate(dNow);
        this.shippers.setUpdatedDate(dNow);
        this.shippers.setBusinessName(shippersView.jTextField3.getText());
        this.shippers.setDescription(shippersView.jTextArea1.getText());
        shippersJpaController.create(shippers);
        JOptionPane.showInternalMessageDialog(shippersView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.shipperssSelected.setCreatedDate(dNow);
        this.shipperssSelected.setUpdatedDate(dNow);
        this.shipperssSelected.setBusinessName(shippersView.jTextField3.getText());
        this.shipperssSelected.setDescription(shippersView.jTextArea1.getText());
        try {
            shippersJpaController.edit(shipperssSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ShippersController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ShippersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(shippersView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        shippersView.jTextField3.setText("");
        shippersView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.shipperssSelected = findShippersByNameLocal(this.shippersView.jList3.getSelectedValue());
            this.shippersView.jTextField3.setText(shipperssSelected.getBusinessName());
            this.shippersView.jTextArea1.setText(shipperssSelected.getDescription());
        }
    }
    
    private Shippers findShippersByNameLocal(String name) {
        Iterator<Shippers> iterator = this.listShippers.iterator();
        Shippers shippers = null;
        while(iterator.hasNext()) {
            shippers = iterator.next();
            if(shippers.getBusinessName().matches(name)) {
                return shippers;
            }
        }
        return shippers;
    }
    
    private List <Shippers> listShippers;
    private Shippers shipperssSelected;
    private Shippers shippers;
    private final ShippersView shippersView;
    private final ShippersJpaController shippersJpaController;
}
