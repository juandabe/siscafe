/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import siscafe.model.SlotStore;
import siscafe.model.StoresCaffee;
import siscafe.persistence.SlotStoreJpaController;
import siscafe.persistence.StoresCaffeeJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.SlotStoreView;

/**
 *
 * @author Administrador
 */
public class SlotStoreController  implements ActionListener, ListSelectionListener{
    
    public SlotStoreController(SlotStore slotStore, SlotStoreView slotStoreView){
        this.slotStore = slotStore;
        this.slotStoreView = slotStoreView;
        this.slotStoreJpaController = new SlotStoreJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.storesCaffeeJpaController = new StoresCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listStoreCaffees = storesCaffeeJpaController.findStoresCaffeeEntities();
        this.listSlotStore = slotStoreJpaController.findSlotStoreEntities();
        this.myComboBoxModelStore = new MyComboBoxModel(listStoreCaffees);
        this.slotStoreView.jComboBox1.setModel(myComboBoxModelStore);
        GenericListModel genericListModel = new GenericListModel();
        listSlotStore.stream().forEach((slotsStoreFor) -> {
            String nameSlot = slotsStoreFor.getNameSpace()+" "+slotsStoreFor.getStoresCaffeeId().getStoreName();
            genericListModel.add(nameSlot);
        });
        this.slotStoreView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.listStoreCaffees = storesCaffeeJpaController.findStoresCaffeeEntities();
        this.myComboBoxModelStore = new MyComboBoxModel(listStoreCaffees);
        this.slotStoreView.jButton4.addActionListener(this);
        this.slotStoreView.jButton2.addActionListener(this);
        this.slotStoreView.jButton3.addActionListener(this);
        this.slotStoreView.jButton5.addActionListener(this);
        this.slotStoreView.jComboBox1.setModel(myComboBoxModelStore);
        this.slotStoreView.jList3.addListSelectionListener(this);
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
        this.slotStore.setNameSpace(this.slotStoreView.jTextField3.getText());
        String storesCaffee = (String) this.slotStoreView.jComboBox1.getSelectedItem();
        this.slotStore.setStoresCaffeeId(findStoresCaffeeByNameLocal(storesCaffee));
        slotStoreJpaController.create(slotStore);
        JOptionPane.showInternalMessageDialog(slotStoreView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        refresh();
    }
    
    private void edit() {
        this.slotStoreSelected.setNameSpace(slotStoreView.jTextField3.getText());
        String storesCaffee = (String) this.slotStoreView.jComboBox1.getSelectedItem();
        this.slotStoreSelected.setStoresCaffeeId(findStoresCaffeeByNameLocal(storesCaffee));
        try {
            slotStoreJpaController.edit(slotStoreSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SlotStoreController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SlotStoreController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(slotStoreView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        slotStoreView.jTextField3.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.slotStoreSelected = findSlotStoreByNameLocal(this.slotStoreView.jList3.getSelectedValue());
            this.slotStoreView.jTextField3.setText(slotStoreSelected.getNameSpace());
            this.slotStoreView.jComboBox1.getModel().setSelectedItem(this.slotStoreSelected.getStoresCaffeeId().getStoreName());
            this.slotStoreView.jComboBox1.repaint();
        }
    }
    
    private SlotStore findSlotStoreByNameLocal(String name) {
        Iterator<SlotStore> iterator = this.listSlotStore.iterator();
        SlotStore slotStore = null;
        while(iterator.hasNext()) {
            slotStore = iterator.next();
            String nameSlote = slotStore.getNameSpace()+" "+slotStore.getStoresCaffeeId().getStoreName();
            if(nameSlote.matches(name)) {
                return slotStore;
            }
        }
        return slotStore;
    }
    
    private StoresCaffee findStoresCaffeeByNameLocal(String name) {
        Iterator<StoresCaffee> iterator = this.listStoreCaffees.iterator();
        StoresCaffee storesCaffee = null;
        while(iterator.hasNext()) {
            storesCaffee = iterator.next();
            if(storesCaffee.getStoreName().matches(name)) {
                return storesCaffee;
            }
        }
        return storesCaffee;
    }
    
    private MyComboBoxModel myComboBoxModelStore;
    private List <StoresCaffee> listStoreCaffees;
    private List <SlotStore> listSlotStore;
    private SlotStore slotStore;
    private SlotStore slotStoreSelected;
    private final SlotStoreView slotStoreView;
    private final SlotStoreJpaController slotStoreJpaController;
    private StoresCaffeeJpaController storesCaffeeJpaController;
}
