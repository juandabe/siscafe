/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import siscafe.model.SlotStore;
import siscafe.model.StoresCaffee;
import siscafe.persistence.SlotStoreJpaController;
import siscafe.persistence.StoresCaffeeJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.StoresCaffeeView;

/**
 *
 * @author Administrador
 */
public class StoresCaffeeController implements ActionListener, ListSelectionListener{
    
    
    public StoresCaffeeController (StoresCaffee storesCaffee, StoresCaffeeView storesCaffeeView) {
        this.storesCaffee = storesCaffee;
        this.storesCaffeeView = storesCaffeeView;
        this.storesCaffeeJpaController = new StoresCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.slotStoreJpaController = new SlotStoreJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    public void initListener() {
        this.storesCaffeeView.jButton4.addActionListener(this);
        this.storesCaffeeView.jButton2.addActionListener(this);
        this.storesCaffeeView.jButton3.addActionListener(this);
        this.storesCaffeeView.jButton5.addActionListener(this);
        this.storesCaffeeView.jList3.addListSelectionListener(this);
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
    
    private void refresh() {
        this.listClients = storesCaffeeJpaController.findStoresCaffeeEntities();
        GenericListModel genericListModel = new GenericListModel();
        listClients.stream().forEach((storesCaffeeFor) -> {
            genericListModel.add(storesCaffeeFor.getStoreName());
        });
        this.storesCaffeeView.jList3.setModel(genericListModel);
    }
    
    public void add() {
        Date dNow = new Date( );
        this.storesCaffee.setCreatedDate(dNow);
        this.storesCaffee.setUpdatedDate(dNow);
        this.storesCaffee.setStoreName(this.storesCaffeeView.jTextField3.getText());
        this.storesCaffee.setCityLocation((String) this.storesCaffeeView.jComboBox1.getSelectedItem());
        try {
            this.storesCaffeeJpaController.create(storesCaffee);
        } catch (Exception ex) {
            Logger.getLogger(StoresCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(storesCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        refresh();
    }
    
    public void edit() {
        Date dNow = new Date( );
        this.storesCaffeeSelected.setUpdatedDate(dNow);
        this.storesCaffeeSelected.setStoreName(this.storesCaffeeView.jTextField3.getText());
        this.storesCaffeeSelected.setCityLocation((String) this.storesCaffeeView.jComboBox1.getSelectedItem());
        this.storesCaffeeSelected.setSlotStoreList(getListSlotStoreLoaded(this.storesCaffeeView.jList1.getModel()));
        try {
            this.storesCaffeeJpaController.edit(storesCaffeeSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(storesCaffeeView, "Registro actualizado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    public void clear() {
        this.storesCaffeeView.jTextField3.setText("");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.storesCaffeeSelected = findStoresCaffeeByNameLocal(this.storesCaffeeView.jList3.getSelectedValue());
            this.storesCaffeeView.jTextField3.setText(storesCaffeeSelected.getStoreName());
            this.storesCaffeeView.jComboBox1.setSelectedItem(storesCaffeeSelected.getCityLocation());
            List <SlotStore> listSlotStore = slotStoreJpaController.findSlotStoreEntitiesByStore(storesCaffeeSelected);
            System.out.println(listSlotStore.size());
            this.storesCaffeeView.jList1.setListData(getArrayListModelSlotStore(listSlotStore));
        }
    }
    
    private StoresCaffee findStoresCaffeeByNameLocal(String name) {
        Iterator<StoresCaffee> iterator = this.listClients.iterator();
        StoresCaffee storesCaffee = null;
        while(iterator.hasNext()) {
            storesCaffee = iterator.next();
            if(storesCaffee.getStoreName().matches(name)) {
                return storesCaffee;
            }
        }
        return storesCaffee;
    }
    
    private String[] getArrayListModelSlotStore(List<SlotStore> listSlotStore) {
        SlotStore [] array = listSlotStore.toArray(new SlotStore[listSlotStore.size()]);
        String[] arrayData = new String[array.length];
        for(int i=0; i<array.length; i++) {
            arrayData[i] = array[i].getNameSpace();
        }
        return arrayData;
    }
    
    private List<SlotStore> getListSlotStoreLoaded(ListModel<String> listModel) {
        List<SlotStore> listslotStoreLoaded = new ArrayList();
        List<SlotStore> listSlotStoreLocal = slotStoreJpaController.findSlotStoreEntities();
        for(int i=0; i<listModel.getSize(); i++) {
            Iterator<SlotStore> iterator = listSlotStoreLocal.iterator();
            while(iterator.hasNext()) {
                SlotStore slotStore = iterator.next();
                if(slotStore.getNameSpace().matches(listModel.getElementAt(i))) {
                    listslotStoreLoaded.add(slotStore);
                    break;
                }
            }
        }
        return listslotStoreLoaded;
    }
    
    private List<StoresCaffee> listClients;
    private final StoresCaffee storesCaffee;
    private StoresCaffee storesCaffeeSelected;
    private final StoresCaffeeView storesCaffeeView;
    private StoresCaffeeJpaController storesCaffeeJpaController;
    private SlotStoreJpaController slotStoreJpaController;
}