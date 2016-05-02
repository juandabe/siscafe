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
import siscafe.model.ItemsServices;
import siscafe.persistence.ItemsServicesJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.ItemsServicesView;

/**
 *
 * @author Administrador
 */
public class ItemsServicesController  implements ActionListener, ListSelectionListener{
    
    public ItemsServicesController(ItemsServices itemsServices, ItemsServicesView itemsServicesView){
        this.itemsServices = itemsServices;
        this.itemsServicesView = itemsServicesView;
        this.itemsServicesJpaController = new ItemsServicesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listItemsServices = itemsServicesJpaController.findItemsServicesEntities();
        GenericListModel genericListModel = new GenericListModel();
        listItemsServices.stream().forEach((itemsServicesFor) -> {
            genericListModel.add(itemsServicesFor.getServicesName());
        });
        this.itemsServicesView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.itemsServicesView.jButton4.addActionListener(this);
        this.itemsServicesView.jButton2.addActionListener(this);
        this.itemsServicesView.jButton3.addActionListener(this);
        this.itemsServicesView.jButton5.addActionListener(this);
        this.itemsServicesView.jList3.addListSelectionListener(this);
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
        this.itemsServices.setCreatedDate(dNow);
        this.itemsServices.setUpdatedDate(dNow);
        this.itemsServices.setServicesName(itemsServicesView.jTextField3.getText());
        this.itemsServices.setUnoeeRef(Integer.parseInt(itemsServicesView.jTextField5.getText()));
        try {
            itemsServicesJpaController.create(itemsServices);
        } catch (Exception ex) {
            Logger.getLogger(ItemsServicesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(itemsServicesView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.itemsServicesSelected.setCreatedDate(dNow);
        this.itemsServicesSelected.setUpdatedDate(dNow);
        this.itemsServicesSelected.setServicesName(itemsServicesView.jTextField3.getText());
        this.itemsServicesSelected.setUnoeeRef(Integer.parseInt(itemsServicesView.jTextField5.getText()));
        try {
            itemsServicesJpaController.edit(itemsServicesSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ItemsServicesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ItemsServicesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(itemsServicesView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        itemsServicesView.jTextField3.setText("");
        itemsServicesView.jTextField5.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.itemsServicesSelected = findItemsServicesByNameLocal(this.itemsServicesView.jList3.getSelectedValue());
            this.itemsServicesView.jTextField3.setText(itemsServicesSelected.getServicesName());
            this.itemsServicesView.jTextField5.setText(String.valueOf(itemsServicesSelected.getUnoeeRef()));
        }
    }
    
    private ItemsServices findItemsServicesByNameLocal(String name) {
        Iterator<ItemsServices> iterator = this.listItemsServices.iterator();
        ItemsServices itemsServices = null;
        while(iterator.hasNext()) {
            itemsServices = iterator.next();
            if(itemsServices.getServicesName().matches(name)) {
                return itemsServices;
            }
        }
        return itemsServices;
    }
    
    private List <ItemsServices> listItemsServices;
    private ItemsServices itemsServicesSelected;
    private final ItemsServices itemsServices;
    private final ItemsServicesView itemsServicesView;
    private final ItemsServicesJpaController itemsServicesJpaController;
}
