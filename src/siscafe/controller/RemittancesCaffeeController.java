/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import siscafe.model.Clients;
import siscafe.model.PackingCaffee;
import siscafe.model.RemittancesCaffee;
import siscafe.model.SlotStore;
import siscafe.model.StateOperation;
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
import siscafe.report.Reports;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.RemittancesCaffeeView;

/**
 *
 * @author Administrador
 */
public class RemittancesCaffeeController  implements ActionListener, KeyListener{

    public RemittancesCaffeeController (RemittancesCaffee remittancesCaffee, String username, RemittancesCaffeeView remittancesCaffeeView) {
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
        remittancesCaffeeView.jButton6.addActionListener(this);
        remittancesCaffeeView.jButton4.addActionListener(this);
        remittancesCaffeeView.jButton2.addActionListener(this);
        remittancesCaffeeView.jButton3.addActionListener(this);
        remittancesCaffeeView.jButton9.addActionListener(this);
        remittancesCaffeeView.jTextField9.addKeyListener(this);
        remittancesCaffeeView.jButton6.addActionListener(this);
        listClients = clientsJpaController.findClientsEntities();
        listPackingCaffee = packingCaffeeJpaController.findPackingCaffeeEntities();
        listUnitsCaffee = unitsCaffeeJpaController.findUnitsCaffeeEntities();
        listStoresCaffee = storesCaffeeJpaController.findStoresCaffeeEntities();
        listSlotStore = slotStoreJpaController.findSlotStoreEntities();
        listUserDriver = usersJpaController.findUsersByProfiles(profilesJpaController.findProfiles(3));
        listUserSampler = usersJpaController.findUsersByProfiles(profilesJpaController.findProfiles(4));
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
            case "printer":
                printer();
            break;
            case "clear":
                clear();
            break;
            case "getSlot":
                getSlot();
            break;
        }
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
    
    private void printer(){
        String IdRemittancesCaffee = remittancesCaffeeView.jTextField9.getText();
        RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(Integer.parseInt(IdRemittancesCaffee));
        if(findRemittancesCaffee != null){
            new Reports().showReportRemittancesCaffeeRadicated(findRemittancesCaffee, username);
        }
        else{
            JOptionPane.showMessageDialog(remittancesCaffeeView, 
                    "No existe ningun registro", 
                    "Error registro no existe", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void add() {
        Date dNow = new Date();
        remittancesCaffee.setCreatedDate(dNow);
        remittancesCaffee.setUpdatedDated(dNow);
        remittancesCaffee.setQuantityBagRadicatedIn(Integer.valueOf(this.remittancesCaffeeView.jTextField10.getText()));
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
        UnitsCaffee unitsCaffeeLocal = findUnitsCaffeeByNameLocal(unitsCaffee);
        remittancesCaffee.setUnitsCafeeId(unitsCaffeeLocal);
        String packingCaffee = (String) this.remittancesCaffeeView.jComboBox4.getSelectedItem();
        remittancesCaffee.setPackingCafeeId(findPackingCaffeeByNameLocal(packingCaffee));
        String driverUsers = (String) this.remittancesCaffeeView.jComboBox6.getSelectedItem();
        remittancesCaffee.setStaffDriverId(findUsersByNameLocal(driverUsers));
        String samplerUsers = (String) this.remittancesCaffeeView.jComboBox5.getSelectedItem();
        remittancesCaffee.setStaffSampleId(findUsersByNameLocal(samplerUsers));
        Double weightCaffeeNominal = unitsCaffeeLocal.getQuantity()*(Double.valueOf(this.remittancesCaffeeView.jTextField10.getText()));
        remittancesCaffee.setTotalWeightNetNominal(weightCaffeeNominal);
        remittancesCaffee.setStatusOperation(new StateOperation(6));
        remittancesCaffee.setIsActive(true);
        remittancesCaffee.setTotalTare(0);
        remittancesCaffee.setQuantityBagInStore(0);
        remittancesCaffee.setQuantityBagOutStore(0);
        remittancesCaffee.setTotalWeightNetReal(0.0);
        remittancesCaffee.setQuantityInPalletCaffee(0);
        remittancesCaffee.setQuantityOutPalletCaffee(0);
        remittancesCaffee.setTareDownload(0.0);
        remittancesCaffee.setTarePackaging(0.0);
        try {
            remittancesCaffeeJpaController.create(remittancesCaffee);
        } catch (Exception ex) {
            Logger.getLogger(RemittancesCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            RemittancesCaffee remittancesCaffeeCreated = remittancesCaffeeJpaController.findRemittancesCaffeeByGuidedAndLot(guideId, lotCaffee);
            remittancesCaffeeView.jTextField9.setText(String.valueOf(remittancesCaffeeCreated.getId()));
            //createPdfRemettance(remittancesCaffeeCreated);
            new Reports().showReportRemittancesCaffeeRadicated(remittancesCaffeeCreated, username);
            JOptionPane.showInternalMessageDialog(remittancesCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void edit() {
        String IdRemittancesCaffee = remittancesCaffeeView.jTextField9.getText();
        RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(Integer.parseInt(IdRemittancesCaffee));
        Date dNow = new Date();
        findRemittancesCaffee.setCreatedDate(dNow);
        findRemittancesCaffee.setUpdatedDated(dNow);
        findRemittancesCaffee.setQuantityBagRadicatedIn(Integer.valueOf(this.remittancesCaffeeView.jTextField10.getText()));
        String lotCaffee = remittancesCaffeeView.jTextField3.getText();
        findRemittancesCaffee.setLotCaffee(lotCaffee);
        findRemittancesCaffee.setAutoOtm(remittancesCaffeeView.jTextField4.getText()); 
        findRemittancesCaffee.setVehiclePlate(remittancesCaffeeView.jTextField7.getText());
        String guideId = remittancesCaffeeView.jTextField5.getText();
        findRemittancesCaffee.setGuideId(guideId);
        String slotStore = (String) this.remittancesCaffeeView.jTextField2.getText();
        findRemittancesCaffee.setSlotStoreId(findSlotStoreByNameLocal(slotStore));
        String clients = (String) this.remittancesCaffeeView.jComboBox1.getSelectedItem();
        findRemittancesCaffee.setClientId(findClientsByNameLocal(clients));
        String unitsCaffee = (String) this.remittancesCaffeeView.jComboBox3.getSelectedItem();
        UnitsCaffee unitsCaffeeLocal = findUnitsCaffeeByNameLocal(unitsCaffee);
        findRemittancesCaffee.setUnitsCafeeId(unitsCaffeeLocal);
        String packingCaffee = (String) this.remittancesCaffeeView.jComboBox4.getSelectedItem();
        findRemittancesCaffee.setPackingCafeeId(findPackingCaffeeByNameLocal(packingCaffee));
        String driverUsers = (String) this.remittancesCaffeeView.jComboBox6.getSelectedItem();
        findRemittancesCaffee.setStaffDriverId(findUsersByNameLocal(driverUsers));
        String samplerUsers = (String) this.remittancesCaffeeView.jComboBox5.getSelectedItem();
        findRemittancesCaffee.setStaffSampleId(findUsersByNameLocal(samplerUsers));
        try {
            remittancesCaffeeJpaController.edit(findRemittancesCaffee);
        } catch (Exception ex) {
            Logger.getLogger(RemittancesCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(remittancesCaffeeView, "Registro actualizado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void find(String IdRemittancesCaffee){
        RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(Integer.parseInt(IdRemittancesCaffee));
        if(findRemittancesCaffee != null){
            remittancesCaffeeView.jTextField10.setText(String.valueOf(findRemittancesCaffee.getQuantityBagRadicatedIn()));
            remittancesCaffeeView.jComboBox3.getModel().setSelectedItem(findRemittancesCaffee.getUnitsCafeeId());
            remittancesCaffeeView.jComboBox1.getModel().setSelectedItem(findRemittancesCaffee.getClientId());
            remittancesCaffeeView.jTextField3.setText(findRemittancesCaffee.getLotCaffee());
            remittancesCaffeeView.jTextField4.setText(findRemittancesCaffee.getAutoOtm());
            remittancesCaffeeView.jTextField7.setText(findRemittancesCaffee.getVehiclePlate());
            remittancesCaffeeView.jTextField5.setText(findRemittancesCaffee.getGuideId());
            remittancesCaffeeView.jComboBox4.getModel().setSelectedItem(findRemittancesCaffee.getPackingCafeeId());
            remittancesCaffeeView.jComboBox4.repaint();
            remittancesCaffeeView.jComboBox1.getModel().setSelectedItem(findRemittancesCaffee.getClientId());
            remittancesCaffeeView.jComboBox1.repaint();
            remittancesCaffeeView.jComboBox5.getModel().setSelectedItem(findRemittancesCaffee.getStaffSampleId());
            remittancesCaffeeView.jComboBox5.repaint();
            remittancesCaffeeView.jComboBox6.getModel().setSelectedItem(findRemittancesCaffee.getStaffDriverId());
            remittancesCaffeeView.jComboBox6.repaint();
            remittancesCaffeeView.jComboBox2.getModel().setSelectedItem(findRemittancesCaffee.getSlotStoreId().getStoresCaffeeId());
            remittancesCaffeeView.jComboBox2.repaint();
            remittancesCaffeeView.jComboBox3.getModel().setSelectedItem(findRemittancesCaffee.getUnitsCafeeId());
            remittancesCaffeeView.jComboBox3.repaint();
            remittancesCaffeeView.jTextField2.setText(findRemittancesCaffee.getSlotStoreId().getNameSpace());
        }
        else{
            JOptionPane.showMessageDialog(remittancesCaffeeView, 
                    "No existe ningun registro", 
                    "Error regisrto no existe", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void clear() {
        this.remittancesCaffeeView.jTextField3.setText("");
        this.remittancesCaffeeView.jTextField4.setText("");
        this.remittancesCaffeeView.jTextField7.setText("");
        this.remittancesCaffeeView.jTextField5.setText("");
        remittancesCaffeeView.jTextField9.setText("");
        remittancesCaffeeView.jTextField10.setText("");
        remittancesCaffeeView.jTextField3.setText("");
        remittancesCaffeeView.jTextField4.setText("");
        remittancesCaffeeView.jTextField5.setText("");
        remittancesCaffeeView.jTextField5.setText("");
        this.remittancesCaffeeView.jComboBox1.setSelectedItem("");
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
        RemittancesCaffee remittancesCaffeeLocal = null;
        while(iterator.hasNext()) {
            remittancesCaffeeLocal = iterator.next();
            if(String.valueOf(remittancesCaffeeLocal.getId()).matches(name)) {
                return remittancesCaffeeLocal;
            }
        }
        return remittancesCaffeeLocal;
    }
    
    private String username;
    private MyComboBoxModel myComboBoxModelUnitsCaffee;
    private MyComboBoxModel myComboBoxModelClients;
    private MyComboBoxModel myComboBoxModelStoresCaffee;
    private MyComboBoxModel myComboBoxModelPackingCaffee;
    private MyComboBoxModel myComboBoxModelStoreCaffees;
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

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 10){
            String IdRemittancesCaffee = remittancesCaffeeView.jTextField9.getText();
            if(IdRemittancesCaffee != null){
                find(IdRemittancesCaffee);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}