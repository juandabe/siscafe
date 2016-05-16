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
import siscafe.model.Clients;
import siscafe.model.PackingCaffee;
import siscafe.model.RemittancesCaffee;
import siscafe.model.SlotStore;
import siscafe.model.StoresCaffee;
import siscafe.model.UnitsCaffee;
import siscafe.model.Users;
import siscafe.persistence.ClientsJpaController;
import siscafe.persistence.PackingCaffeeJpaController;
import siscafe.persistence.ProfilesJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.SlotStoreJpaController;
import siscafe.persistence.StoresCaffeeJpaController;
import siscafe.persistence.UnitsCaffeeJpaController;
import siscafe.persistence.UsersJpaController;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.RemittancesCaffeeView;

/**
 *
 * @author Administrador
 */
public class RemittancesCaffeeController  implements ActionListener, ListSelectionListener{

    public RemittancesCaffeeController (RemittancesCaffee remittancesCaffee, RemittancesCaffeeView remittancesCaffeeView) {
        this.remittancesCaffee = remittancesCaffee;
        this.remittancesCaffeeView = remittancesCaffeeView;
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        clientsJpaController = new ClientsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        unitsCaffeeJpaController = new UnitsCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        storesCaffeeJpaController = new StoresCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        packingCaffeeJpaController = new PackingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        slotStoreJpaController = new SlotStoreJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        usersJpaController = new UsersJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        profilesJpaController = new ProfilesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    public void initListener() {
        remittancesCaffeeView.jButton4.addActionListener(this);
        remittancesCaffeeView.jButton2.addActionListener(this);
        remittancesCaffeeView.jButton3.addActionListener(this);
        remittancesCaffeeView.jButton5.addActionListener(this);
        remittancesCaffeeView.jList1.addListSelectionListener(this);
        remittancesCaffeeView.jButton9.addActionListener(this);
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
                //edit();
            break;
            case "refresh":
                //refresh();
            break;
            case "clear":
                //clear();
            break;
            case "delete":
                //clear();
            break;
            case "getSlot":
                getSlot();
            break;
        }
    }
    
    private void refresh() {
        listClients = clientsJpaController.findClientsEntities();
        listPackingCaffee = packingCaffeeJpaController.findPackingCaffeeEntities();
        listUnitsCaffee = unitsCaffeeJpaController.findUnitsCaffeeEntities();
        listStoresCaffee = storesCaffeeJpaController.findStoresCaffeeEntities();
        listSlotStore = slotStoreJpaController.findSlotStoreEntities();
        listUserDriver = usersJpaController.findUsersByProfiles(profilesJpaController.findProfiles(5));
        listUserSampler = usersJpaController.findUsersByProfiles(profilesJpaController.findProfiles(6));
        listUsers = usersJpaController.findUsersEntities();
        myComboBoxModelClients = new MyComboBoxModel(listClients);
        remittancesCaffeeView.jComboBox1.setModel(myComboBoxModelClients);
        remittancesCaffeeView.jComboBox1.repaint();
        myComboBoxModelUnitsCaffee = new MyComboBoxModel(listUnitsCaffee);
        remittancesCaffeeView.jComboBox3.setModel(myComboBoxModelUnitsCaffee);
        remittancesCaffeeView.jComboBox3.repaint();
        myComboBoxModelStoresCaffee = new MyComboBoxModel(listStoresCaffee);
        remittancesCaffeeView.jComboBox2.setModel(myComboBoxModelStoresCaffee);
        remittancesCaffeeView.jComboBox2.repaint();
        myComboBoxModelDriver = new MyComboBoxModel(listUserDriver);
        remittancesCaffeeView.jComboBox6.setModel(myComboBoxModelDriver);
        remittancesCaffeeView.jComboBox6.repaint();
        myComboBoxModelSample = new MyComboBoxModel(listUserSampler);
        remittancesCaffeeView.jComboBox5.setModel(myComboBoxModelSample);
        remittancesCaffeeView.jComboBox5.repaint();
        myComboBoxModelPackingCaffee = new MyComboBoxModel(listPackingCaffee);
        remittancesCaffeeView.jComboBox4.setModel(myComboBoxModelPackingCaffee);
        remittancesCaffeeView.jComboBox4.repaint();
    }
    
    private void getSlot() {
        String storesCaffeeString = (String) this.remittancesCaffeeView.jComboBox2.getSelectedItem();
        StoresCaffee storesCaffee = findStoresCaffeeByNameLocal(storesCaffeeString);
        List<SlotStore> listSlots = slotStoreJpaController.getSlotStoreEmptyByStoreCaffee(storesCaffee);
        if(listSlots.size() != 0) {
            remittancesCaffeeView.jTextField2.setText(listSlots.get(0).getNameSpace());
        }
        else {
            remittancesCaffeeView.jTextField2.setText("-N.U-");
        }
    }
    
    public void add() {
        Date dNow = new Date();
        remittancesCaffee.setCreatedDate(dNow);
        remittancesCaffee.setUpdatedDated(dNow);
        remittancesCaffee.setQuantityBagInStore(Integer.valueOf(this.remittancesCaffeeView.jTextField10.getText()));
        String lotCaffee = remittancesCaffeeView.jTextField3.getText();
        remittancesCaffee.setLotCaffee(lotCaffee);
        remittancesCaffee.setAutoOtm(remittancesCaffeeView.jTextField4.getText()); 
        remittancesCaffee.setVehiclePlate(remittancesCaffeeView.jTextField7.getText());
        String guideId = remittancesCaffeeView.jTextField5.getText();
        remittancesCaffee.setGuideId(guideId);
        String slotStore = (String) this.remittancesCaffeeView.jTextField2.getText();
        remittancesCaffee.setSlotStoreId(findSlotStoreByNameLocal(slotStore));
        String clients = (String) this.remittancesCaffeeView.jComboBox1.getSelectedItem();
        remittancesCaffee.setClientId(findClientsByNameLocal(clients));
        String unitsCaffee = (String) this.remittancesCaffeeView.jComboBox3.getSelectedItem();
        remittancesCaffee.setUnitsCafeeId(findUnitsCaffeeByNameLocal(unitsCaffee));
        String packingCaffee = (String) this.remittancesCaffeeView.jComboBox4.getSelectedItem();
        remittancesCaffee.setPackingCafeeId(findPackingCaffeeByNameLocal(packingCaffee));
        String driverUsers = (String) this.remittancesCaffeeView.jComboBox6.getSelectedItem();
        remittancesCaffee.setStaffDriverId(findUsersByNameLocal(driverUsers));
        String samplerUsers = (String) this.remittancesCaffeeView.jComboBox5.getSelectedItem();
        remittancesCaffee.setStaffSampleId(findUsersByNameLocal(samplerUsers));
        remittancesCaffee.setIsActive(true);
        remittancesCaffee.setTotalTare(0.0);
        remittancesCaffee.setQuantityBagOutStore(0);
        remittancesCaffee.setTotalWeightNetNominal(0.0);
        remittancesCaffee.setTotalWeightNetReal(0.0);
        remittancesCaffee.setQuantityInPalletCaffee(0);
        remittancesCaffee.setQuantityOutPalletCaffee(0);
        try {
            remittancesCaffeeJpaController.create(remittancesCaffee);
        } catch (Exception ex) {
            Logger.getLogger(RemittancesCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            RemittancesCaffee remittancesCaffeeCreated = remittancesCaffeeJpaController.findRemittancesCaffeeByGuidedAndLot(guideId, lotCaffee);
            remittancesCaffeeView.jTextField9.setText(String.valueOf(remittancesCaffeeCreated.getId()));
            JOptionPane.showInternalMessageDialog(remittancesCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void edit() {
        Date dNow = new Date( );
        remittancesCaffeeSelected.setCreatedDate(dNow);
        remittancesCaffeeSelected.setQuantityBagInStore(Integer.valueOf(this.remittancesCaffeeView.jTextField10.getText()));
        remittancesCaffeeSelected.setLotCaffee(remittancesCaffeeView.jTextField3.getText());
        remittancesCaffeeSelected.setAutoOtm(remittancesCaffeeView.jTextField4.getText()); 
        remittancesCaffeeSelected.setVehiclePlate(remittancesCaffeeView.jTextField7.getText());
        remittancesCaffeeSelected.setGuideId(remittancesCaffeeView.jTextField5.getText());
        try {
            remittancesCaffeeJpaController.edit(remittancesCaffeeSelected);
        } catch (Exception ex) {
            Logger.getLogger(RemittancesCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(remittancesCaffeeView, "Registro actualizado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    public void clear() {
        this.remittancesCaffeeView.jTextField3.setText("");
        this.remittancesCaffeeView.jTextField4.setText("");
        this.remittancesCaffeeView.jTextField7.setText("");
        this.remittancesCaffeeView.jTextField5.setText("");
        this.remittancesCaffeeView.jComboBox1.setSelectedItem("");
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            String remmittangeNumber = (String) this.remittancesCaffeeView.jList1.getSelectedValue();
            this.remittancesCaffeeSelected = findRemittancesCaffeeByNameLocal(remmittangeNumber);
            this.remittancesCaffeeView.jTextField10.setText(String.valueOf(remittancesCaffeeSelected.getQuantityBagInStore()));
            remittancesCaffeeView.jComboBox3.setSelectedItem(remittancesCaffeeSelected.getUnitsCafeeId());
        }
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
    
    private SlotStore findSlotStoreByNameLocal(String name) {
        Iterator<SlotStore> iterator = this.listSlotStore.iterator();
        SlotStore slotStore = null;
        while(iterator.hasNext()) {
            slotStore = iterator.next();
            if(slotStore.getNameSpace().matches(name)) {
                return slotStore;
            }
        }
        return slotStore;
    }
    
    private Clients findClientsByNameLocal(String name) {
        Iterator<Clients> iterator = this.listClients.iterator();
        Clients clients = null;
        while(iterator.hasNext()) {
            clients = iterator.next();
            if(clients.getBusinessName().matches(name)) {
                return clients;
            }
        }
        return clients;
    }
    
    private UnitsCaffee findUnitsCaffeeByNameLocal(String name) {
        Iterator<UnitsCaffee> iterator = this.listUnitsCaffee.iterator();
        UnitsCaffee unitsCaffee = null;
        while(iterator.hasNext()) {
            unitsCaffee = iterator.next();
            if(unitsCaffee.getNameUnit().matches(name)) {
                return unitsCaffee;
            }
        }
        return unitsCaffee;
    }
    
    private Users findUsersByNameLocal(String name) {
        Iterator<Users> iterator = this.listUsers.iterator();
        Users users = null;
        while(iterator.hasNext()) {
            users = iterator.next();
            String fullNameUser = users.getFirstName() + " " + users.getLastName();
            if(fullNameUser.matches(name)) {
                return users;
            }
        }
        return users;
    }
    
    private PackingCaffee findPackingCaffeeByNameLocal(String name) {
        Iterator<PackingCaffee> iterator = this.listPackingCaffee.iterator();
        PackingCaffee packingCaffee = null;
        while(iterator.hasNext()) {
            packingCaffee = iterator.next();
            if(packingCaffee.getName().matches(name)) {
                return packingCaffee;
            }
        }
        return packingCaffee;
    }
    
    private RemittancesCaffee findRemittancesCaffeeByNameLocal(String name) {
        Iterator<RemittancesCaffee> iterator = this.listRemittancesCaffee.iterator();
        RemittancesCaffee remittancesCaffee = null;
        while(iterator.hasNext()) {
            remittancesCaffee = iterator.next();
            if(String.valueOf(remittancesCaffee.getId()).matches(name)) {
                return remittancesCaffee;
            }
        }
        return remittancesCaffee;
    }
    
    private MyComboBoxModel myComboBoxModelUnitsCaffee;
    private MyComboBoxModel myComboBoxModelClients;
    private MyComboBoxModel myComboBoxModelStoresCaffee;
    private MyComboBoxModel myComboBoxModelPackingCaffee;
    private MyComboBoxModel myComboBoxModelSlotStore;
    private MyComboBoxModel myComboBoxModelDriver;
    private MyComboBoxModel myComboBoxModelSample;
    private List <Users> listUsers;
    private List <Users> listUserDriver;
    private List <Users> listUserSampler;
    private List <PackingCaffee> listPackingCaffee;
    private List <Clients> listClients;
    private List <UnitsCaffee> listUnitsCaffee;
    private List <RemittancesCaffee> listRemittancesCaffee;
    private List <StoresCaffee> listStoresCaffee;
    private List <SlotStore> listSlotStore; 
    private final RemittancesCaffee remittancesCaffee;
    private RemittancesCaffee remittancesCaffeeSelected;
    private final RemittancesCaffeeView remittancesCaffeeView;
    private final RemittancesCaffeeJpaController remittancesCaffeeJpaController;
    private final ClientsJpaController clientsJpaController;
    private final StoresCaffeeJpaController storesCaffeeJpaController;
    private PackingCaffeeJpaController packingCaffeeJpaController;
    private final UnitsCaffeeJpaController unitsCaffeeJpaController;
    private SlotStoreJpaController slotStoreJpaController;
    private UsersJpaController usersJpaController;
    private ProfilesJpaController profilesJpaController;
}