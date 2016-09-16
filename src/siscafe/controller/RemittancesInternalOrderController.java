/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import siscafe.model.AdictionalElements;
import siscafe.model.AdictionalElementsHasPackagingCaffee;
import siscafe.model.AdictionalElementsHasPackagingCaffeePK;
import siscafe.model.Clients;
import siscafe.model.DetailPackagingCaffee;
import siscafe.model.DetailPackagingCaffeePK;
import siscafe.model.NavyAgent;
import siscafe.model.PackagingCaffee;
import siscafe.model.RemittancesCaffee;
import siscafe.model.ShippingLines;
import siscafe.model.StatePackaging;
import siscafe.persistence.AdictionalElementsHasPackagingCaffeeJpaController;
import siscafe.persistence.AdictionalElementsJpaController;
import siscafe.persistence.ClientsJpaController;
import siscafe.persistence.DetailPackagingCaffeeJpaController;
import siscafe.persistence.NavyAgentJpaController;
import siscafe.persistence.PackagingCaffeeJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.ShippingLinesJpaController;
import siscafe.persistence.StatePackagingJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.report.Reports;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.RemittancesInternalOrderView;

/**
 *
 * @author Administrador
 */
public class RemittancesInternalOrderController implements ActionListener, ItemListener, KeyListener {
    
    public RemittancesInternalOrderController(RemittancesInternalOrderView remittancesInternalOrderView, String username) {
        this.remittancesInternalOrderView = remittancesInternalOrderView;
        this.username = username;
    }
    
    public void initListener() {
        remittancesInternalOrderView.jComboBox3.addItemListener(this);
        remittancesInternalOrderView.jButton4.addActionListener(this);
        remittancesInternalOrderView.jButton1.addActionListener(this);
        remittancesInternalOrderView.jTextField5.addKeyListener(this);
        remittancesInternalOrderView.jButton8.addActionListener(this);
        remittancesInternalOrderView.jButton3.addActionListener(this);
        remittancesInternalOrderView.jButton15.addActionListener(this);
        remittancesInternalOrderView.jButton16.addActionListener(this);
        remittancesInternalOrderView.jButton2.addActionListener(this);
        remittancesInternalOrderView.jButton16.addActionListener(this);
        clientsJpaController = new ClientsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        shippingLinesJpaController = new ShippingLinesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        navyAgentJpaController = new NavyAgentJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        packagingCaffeeJpaController = new PackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        statePackagingJpaController = new StatePackagingJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        detailPackagingCaffeeJpaController = new DetailPackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        adictionalElementsJpaController = new AdictionalElementsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        adictionalElementsHasPackagingCaffeeJpaController = new AdictionalElementsHasPackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        exportersComboxModel = new MyComboBoxModel(clientsJpaController.findClientsEntities());
        shippingLinesComboxModel = new MyComboBoxModel(shippingLinesJpaController.findShippingLinesEntities());
        statePackagingModel = new MyComboBoxModel(statePackagingJpaController.findStatePackagingEntities());
        navyAgentComboxModel = new MyComboBoxModel(navyAgentJpaController.findNavyAgentEntities());
        adictionalElementModel = new MyComboBoxModel(adictionalElementsJpaController.findAdictionalElementsEntities());
        remittancesInternalOrderView.jComboBox3.setModel(exportersComboxModel);
        remittancesInternalOrderView.jComboBox4.setModel(shippingLinesComboxModel);
        remittancesInternalOrderView.jComboBox6.setModel(navyAgentComboxModel);
        remittancesInternalOrderView.jComboBox5.setModel(adictionalElementModel);
        listClients = clientsJpaController.findClientsEntities();
        listShippingLines = shippingLinesJpaController.findShippingLinesEntities();
        listNavyAgent = navyAgentJpaController.findNavyAgentEntities();
        listDetailPackagingCaffee = new ArrayList();
        listDetailAdictionalElements = new ArrayList();
        initListenerListRemittancesCaffee();
        
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
            case "clear":
                clear();
            break;
            case "print":
                print();
            break;
            case "remesa":
                unSelectedRemmitances();
            break;
            case "elemento":
                unAdictionalElement();
            break;
            case "adictionalElements":
                adictionalElements();
            break;
        }
    }
    
    private void print() {
        String OIEFind = remittancesInternalOrderView.jTextField5.getText();
        if(OIEFind != null){
            new Reports().reportCommodityCaffeeDeliver(OIEFind, this.username);
        }
    }
    
    /*public void initListenerListRemittancesCovered() {
        ListSelectionModel model=remittancesInternalOrderView.jTable2.getSelectionModel();
        model.addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent e){
                int showInternalConfirmDialog = JOptionPane.showInternalConfirmDialog(remittancesInternalOrderView, "¿Desea borrar esta remesa amparada?", "Borrar programación", JOptionPane.YES_NO_OPTION);
                if(showInternalConfirmDialog == 0) {
                    int row = remittancesInternalOrderView.jTable2.getSelectedRow();
                    Object idRemittancesCaffee = remittancesInternalOrderView.jTable2.getValueAt(row, 0);
                    DefaultTableModel defaultTableModel = (DefaultTableModel)remittancesInternalOrderView.jTable2.getModel();
                    defaultTableModel.removeRow(row);
                    modifyListDetailPackagingCaffee(Integer.parseInt(String.valueOf(idRemittancesCaffee)));
                }
            }
        });
    }*/
    
    private void unSelectedRemmitances(){
        DefaultTableModel defaultTableModelLocal = (DefaultTableModel)remittancesInternalOrderView.jTable2.getModel();
        int rowSelected = -1;
        rowSelected = remittancesInternalOrderView.jTable2.getSelectedRow();
        if(rowSelected != -1){
            int showInternalConfirmDialog = JOptionPane.showInternalConfirmDialog(remittancesInternalOrderView, "¿Desea borrar esta remesa amparada?", "Borrar programación", JOptionPane.YES_NO_OPTION);
            if(showInternalConfirmDialog == 0) {
                tmpListDetailPackagingCaffee = listDetailPackagingCaffee;
                flagEditedDetailPackagingCaffee = true;
                Object idRemittancesCaffee = remittancesInternalOrderView.jTable2.getValueAt(rowSelected, 0);
                Iterator<DetailPackagingCaffee> iteratorTmpDetailPackagingCaffee = tmpListDetailPackagingCaffee.iterator();
                while(iteratorTmpDetailPackagingCaffee.hasNext()){
                    DetailPackagingCaffee nextDetailPackagingCaffee = iteratorTmpDetailPackagingCaffee.next();
                    RemittancesCaffee remittancesCaffee = nextDetailPackagingCaffee.getRemittancesCaffee();
                    Integer quantityBagRadicatedOut = remittancesCaffee.getQuantityBagRadicatedOut();
                    int newQuantityBagRadicatedOut =  (quantityBagRadicatedOut - nextDetailPackagingCaffee.getQuantityRadicatedBagOut());
                    remittancesCaffee.setQuantityBagRadicatedOut(newQuantityBagRadicatedOut);
                    try {
                        remittancesCaffeeJpaController.edit(remittancesCaffee);
                    } catch (Exception ex) {
                        Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                defaultTableModelLocal.removeRow(rowSelected);
                modifyListDetailPackagingCaffee(Integer.parseInt(String.valueOf(idRemittancesCaffee)));
                showListDetailPackagingCaffee(listDetailPackagingCaffee);
                showRemittancesCaffeeByExporter(exporterName);

            }
        }
        else{
            JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "Seleccione una remesa amparada", "No ha seleccionado", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void unAdictionalElement(){
        int rowSelected = -1;
        rowSelected = remittancesInternalOrderView.jTable3.getSelectedRow();
        if(rowSelected != -1){
            int showInternalConfirmDialog = JOptionPane.showInternalConfirmDialog(remittancesInternalOrderView, "¿Desea borrar este elemento adicional?", "Borrar elemento adicional", JOptionPane.YES_NO_OPTION);
            if(showInternalConfirmDialog == 0) {
                flagEditedAdictionalElements = false;
                Object nameAdictionalElements = remittancesInternalOrderView.jTable3.getValueAt(rowSelected, 0);
                modifyListidAdictionalElements(String.valueOf(nameAdictionalElements));
                showListAdictionalElementsHasPackagingCaffee(listDetailAdictionalElements);
            }
        }
    }
    
    public void initListenerListRemittancesCaffee() {
        Date dateNow = new Date();
        ListSelectionModel model=remittancesInternalOrderView.jTable1.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(!model.isSelectionEmpty()){
                int selectedRow=remittancesInternalOrderView.jTable1.getSelectedRow();
                String idRemittancesCaffee = String.valueOf(rowDataProcess[selectedRow][1]);
                int numBags = Integer.parseInt(JOptionPane.showInternalInputDialog(remittancesInternalOrderView, "¿Cuantos sacos desea programar para embalar de la remesa "+idRemittancesCaffee+"?", "Confirmación", JOptionPane.YES_NO_OPTION));
                RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(Integer.parseInt(idRemittancesCaffee));
                exporterName = findRemittancesCaffee.getClientId().getBusinessName();
                int totalradi = numBags + findRemittancesCaffee.getQuantityBagRadicatedOut();
                if(findRemittancesCaffee.getQuantityBagInStore() >= totalradi) {
                    DetailPackagingCaffee detailPackagingCaffee = new DetailPackagingCaffee();
                    detailPackagingCaffee.setCreatedDate(dateNow);
                    detailPackagingCaffee.setUpdatedDate(dateNow);
                    detailPackagingCaffee.setQuantityRadicatedBagOut(numBags);
                    detailPackagingCaffee.setRemittancesCaffee(findRemittancesCaffee);
                    listDetailPackagingCaffee.add(detailPackagingCaffee);
                    flagEditedDetailPackagingCaffee = true;
                }
                else{
                    JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "No hay cantidad de sacos de esta remesa", "Cantidad sacos errados", JOptionPane.ERROR_MESSAGE);
                }
                showListDetailPackagingCaffee(listDetailPackagingCaffee);
            }
        });
    }
    
    private void adictionalElements(){
        AdictionalElements adictionalElement = findAdictionalElementsByNameLocal((String)remittancesInternalOrderView.jComboBox5.getSelectedItem());
        AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffee = new AdictionalElementsHasPackagingCaffee();
        AdictionalElementsHasPackagingCaffeePK adictionalElementsHasPackagingCaffeePK = new AdictionalElementsHasPackagingCaffeePK();
        adictionalElementsHasPackagingCaffeePK.setAdictionalElementsId(adictionalElement.getId());
        adictionalElementsHasPackagingCaffee.setAdictionalElementsHasPackagingCaffeePK(adictionalElementsHasPackagingCaffeePK);
        adictionalElementsHasPackagingCaffee.setAdictionalElements(adictionalElement);
        adictionalElementsHasPackagingCaffee.setQuantity((int)remittancesInternalOrderView.jSpinner1.getValue());
        adictionalElementsHasPackagingCaffee.setTypeUnit((String)remittancesInternalOrderView.jComboBox13.getSelectedItem());
        listDetailAdictionalElements.add(adictionalElementsHasPackagingCaffee);
        showListAdictionalElementsHasPackagingCaffee(listDetailAdictionalElements);
    }
    
    private void add() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String DEX = remittancesInternalOrderView.jTextField1.getText();
        PackagingCaffee packagingCaffee = new PackagingCaffee();
        packagingCaffee.setExportStatement(DEX);
        packagingCaffee.setCreatedDate(new Date());
        StatePackaging findStatePackaging = statePackagingJpaController.findStatePackaging(2);
        packagingCaffee.setStatePackagingId(findStatePackaging);//StatePackaging=ordenado
        packagingCaffee.setPackagingType((String) remittancesInternalOrderView.jComboBox1.getSelectedItem());
        packagingCaffee.setNavyAgentId(findNavyAgentByNameLocal((String) remittancesInternalOrderView.jComboBox6.getSelectedItem()));
        packagingCaffee.setShippingLinesId(findshippingLinesByNameLocal((String) remittancesInternalOrderView.jComboBox4.getSelectedItem()));
        packagingCaffee.setMotorShipName(remittancesInternalOrderView.jTextField2.getText());
        packagingCaffee.setPackagingMode((String)remittancesInternalOrderView.jComboBox2.getSelectedItem());
        packagingCaffee.setWeightToOut(remittancesInternalOrderView.jCheckBox1.isSelected());
        packagingCaffee.setBookingExpo(remittancesInternalOrderView.jTextField13.getText());
        packagingCaffee.setIsoCtn((String)remittancesInternalOrderView.jComboBox7.getSelectedItem());
        
        if(!listDetailPackagingCaffee.isEmpty() && !listDetailAdictionalElements.isEmpty()){
            exporterName = listDetailPackagingCaffee.get(0).getRemittancesCaffee().getClientId().getBusinessName();
            remittancesInternalOrderView.jTextField5.setText(formatter.format(packagingCaffee.getCreatedDate()));
            remittancesInternalOrderView.jTextField7.setText(packagingCaffee.getStatePackagingId().getName());
            remittancesInternalOrderView.jTextField6.setText(formatter.format(packagingCaffee.getCreatedDate()));
            int newpackagingCaffeeId = packagingCaffeeJpaController.create(packagingCaffee);
            remittancesInternalOrderView.jTextField5.setText(String.format("%06d", newpackagingCaffeeId));
            Iterator<DetailPackagingCaffee> iteratorDetailPackagingCaffee = listDetailPackagingCaffee.iterator();
            int count=0;
            while(iteratorDetailPackagingCaffee.hasNext()){
                DetailPackagingCaffee nextDetailPackagingCaffee = iteratorDetailPackagingCaffee.next();
                RemittancesCaffee remittancesCaffee = nextDetailPackagingCaffee.getRemittancesCaffee();
                int oldQuantityBagRadicatedOut = remittancesCaffee.getQuantityBagRadicatedOut();
                int newQuantityBagRadicatedOut = nextDetailPackagingCaffee.getQuantityRadicatedBagOut()+oldQuantityBagRadicatedOut;
                remittancesCaffee.setQuantityBagRadicatedOut(newQuantityBagRadicatedOut);
                try {
                    remittancesCaffeeJpaController.edit(remittancesCaffee);
                } catch (Exception ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                nextDetailPackagingCaffee.setPackagingCaffee(packagingCaffee);
                try {
                    detailPackagingCaffeeJpaController.create(nextDetailPackagingCaffee);
                } catch (Exception ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                listDetailPackagingCaffee.set(count, nextDetailPackagingCaffee);
                count++;
            }
            Iterator<AdictionalElementsHasPackagingCaffee> iteratorAdictionalElements = listDetailAdictionalElements.iterator();
            count=0;
            while(iteratorAdictionalElements.hasNext()){
                AdictionalElementsHasPackagingCaffee nextAdictionalElementsHasPackagingCaffee = iteratorAdictionalElements.next();
                nextAdictionalElementsHasPackagingCaffee.setPackagingCaffee(packagingCaffee);
                listDetailAdictionalElements.set(count, nextAdictionalElementsHasPackagingCaffee);
                try {
                    adictionalElementsHasPackagingCaffeeJpaController.create(nextAdictionalElementsHasPackagingCaffee);
                } catch (Exception ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                count++;
            }
            packagingCaffee.setAdictionalElementsHasPackagingCaffeeList(listDetailAdictionalElements);
            try {
                packagingCaffeeJpaController.edit(packagingCaffee);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        else{
            JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "Seleccione remesas y cantidad de sacos a embalar\n"
                    + "y elementos adicionales del embalaje.", "Error seleccionado remesa u lote", JOptionPane.ERROR_MESSAGE);
        }
        showRemittancesCaffeeByExporter(exporterName);
    }
    
    private void edit(){
        String OIEFind = remittancesInternalOrderView.jTextField5.getText();
        PackagingCaffee findPackagingCaffee = packagingCaffeeJpaController.findPackagingCaffee(Integer.parseInt(OIEFind));
        if(OIEFind != null && findPackagingCaffee != null){
            findPackagingCaffee.setCreatedDate(new Date());
            findPackagingCaffee.setExportStatement(remittancesInternalOrderView.jTextField1.getText());
            findPackagingCaffee.setMotorShipName(remittancesInternalOrderView.jTextField2.getText());
            findPackagingCaffee.setNavyAgentId(findNavyAgentByNameLocal((String)remittancesInternalOrderView.jComboBox6.getSelectedItem()));
            findPackagingCaffee.setPackagingMode((String)remittancesInternalOrderView.jComboBox2.getSelectedItem());
            findPackagingCaffee.setPackagingType((String)remittancesInternalOrderView.jComboBox1.getSelectedItem());
            findPackagingCaffee.setWeightToOut(remittancesInternalOrderView.jCheckBox1.isSelected());
            findPackagingCaffee.setBookingExpo(remittancesInternalOrderView.jTextField13.getText());
            findPackagingCaffee.setIsoCtn((String)remittancesInternalOrderView.jComboBox7.getSelectedItem());
            if(flagEditedDetailPackagingCaffee == true){
                List<DetailPackagingCaffee> findListDetailPackagingCaffeeByOIE = detailPackagingCaffeeJpaController.findListDetailPackagingCaffeeByOIE(findPackagingCaffee.getId());
                Iterator<DetailPackagingCaffee> iteratorOldDetailPackagingCaffee = findListDetailPackagingCaffeeByOIE.iterator();
                while(iteratorOldDetailPackagingCaffee.hasNext()){
                    DetailPackagingCaffee nextDetailPackagingCaffee = iteratorOldDetailPackagingCaffee.next();
                    try {
                        detailPackagingCaffeeJpaController.destroy(nextDetailPackagingCaffee.getDetailPackagingCaffeePK());
                    } catch (NonexistentEntityException ex) {
                        Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                Iterator<DetailPackagingCaffee> iteratorNewDetailPackagingCaffee = listDetailPackagingCaffee.iterator();
                while(iteratorNewDetailPackagingCaffee.hasNext()){
                    DetailPackagingCaffee nextNewDetailPackagingCaffee = iteratorNewDetailPackagingCaffee.next();
                    nextNewDetailPackagingCaffee.setPackagingCaffee(findPackagingCaffee);
                    Integer idRemmitancesCaffee = nextNewDetailPackagingCaffee.getRemittancesCaffee().getId();
                    RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(idRemmitancesCaffee);
                    int oldRadBagOut = findRemittancesCaffee.getQuantityBagRadicatedOut();
                    int newRadBagOut = oldRadBagOut+nextNewDetailPackagingCaffee.getQuantityRadicatedBagOut();
                    findRemittancesCaffee.setQuantityBagRadicatedOut(newRadBagOut);
                    try {
                        remittancesCaffeeJpaController.edit(findRemittancesCaffee);
                    } catch (Exception ex) {
                        Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        detailPackagingCaffeeJpaController.create(nextNewDetailPackagingCaffee);
                    } catch (Exception ex) {
                        Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                flagEditedDetailPackagingCaffee = false;
            }
            if(flagEditedAdictionalElements == true){
                List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeList = findPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList();
                Iterator<AdictionalElementsHasPackagingCaffee> iteratorAdictionalElementsHasPackagingCaffee = adictionalElementsHasPackagingCaffeeList.iterator();
                while(iteratorAdictionalElementsHasPackagingCaffee.hasNext()){
                    AdictionalElementsHasPackagingCaffee nextAdictionalElementsHasPackagingCaffee = iteratorAdictionalElementsHasPackagingCaffee.next();
                    try {
                        adictionalElementsHasPackagingCaffeeJpaController.destroy(nextAdictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK());
                    } catch (siscafe.controller.exceptions.NonexistentEntityException ex) {
                        Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                int count=0;
                Iterator<AdictionalElementsHasPackagingCaffee> iteratorAdictionalElements = listDetailAdictionalElements.iterator();
                while(iteratorAdictionalElements.hasNext()){ 
                    AdictionalElementsHasPackagingCaffee nextAdictionalElements = iteratorAdictionalElements.next();
                    nextAdictionalElements.setPackagingCaffee(findPackagingCaffee);
                    listDetailAdictionalElements.set(count, nextAdictionalElements);
                    System.out.println("PackagingCaffee id: "+nextAdictionalElements.getPackagingCaffee().getId());
                    System.out.println("AdictionalElement id: "+nextAdictionalElements.getAdictionalElements().getId());
                    try {
                        adictionalElementsHasPackagingCaffeeJpaController.create(nextAdictionalElements);
                    } catch (Exception ex) {
                        Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    count++;
                }
                flagEditedAdictionalElements = false;
                findPackagingCaffee.setAdictionalElementsHasPackagingCaffeeList(listDetailAdictionalElements);
            }
            try {
                packagingCaffeeJpaController.edit(findPackagingCaffee);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
                JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "Se edito el registro con exito", "Exito del registro", JOptionPane.INFORMATION_MESSAGE);
                showRemittancesCaffeeByExporter(exporterName);
        }
        else{
            JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "No existe ningun registro OIE", "Ningun registro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void clear() {
        remittancesInternalOrderView.jTextField1.setText("");
        remittancesInternalOrderView.jTextField6.setText("");
        remittancesInternalOrderView.jTextField5.setText("");
        remittancesInternalOrderView.jTextField7.setText("");
        remittancesInternalOrderView.jCheckBox1.setSelected(true);
        remittancesInternalOrderView.jTextField3.setText("");
        remittancesInternalOrderView.jTextField4.setText("");
        remittancesInternalOrderView.jComboBox1.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox2.setSelectedItem("");
        remittancesInternalOrderView.jComboBox3.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox3.repaint();
        remittancesInternalOrderView.jComboBox4.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox4.repaint();
        remittancesInternalOrderView.jComboBox6.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox6.repaint();
        remittancesInternalOrderView.jTextField2.setText("");
        listDetailAdictionalElements = new ArrayList();
        listDetailPackagingCaffee = new ArrayList();
        showListDetailPackagingCaffee(listDetailPackagingCaffee);
        showListAdictionalElementsHasPackagingCaffee(listDetailAdictionalElements);
    }
    
    private void find(String OIEFind){
        PackagingCaffee findPackagingCaffee = packagingCaffeeJpaController.findPackagingCaffee(Integer.parseInt(OIEFind));
        if(findPackagingCaffee != null){
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            remittancesInternalOrderView.jTextField6.setText(formatter.format(findPackagingCaffee.getCreatedDate()));
            remittancesInternalOrderView.jTextField13.setText(findPackagingCaffee.getBookingExpo());
            remittancesInternalOrderView.jTextField7.setText(findPackagingCaffee.getStatePackagingId().getName());
            remittancesInternalOrderView.jTextField1.setText(findPackagingCaffee.getExportStatement());
            remittancesInternalOrderView.jComboBox1.getModel().setSelectedItem(findPackagingCaffee.getPackagingType());
            remittancesInternalOrderView.jComboBox1.repaint();
            remittancesInternalOrderView.jComboBox2.getModel().setSelectedItem(findPackagingCaffee.getPackagingMode());
            remittancesInternalOrderView.jComboBox2.repaint();
            remittancesInternalOrderView.jCheckBox1.setSelected(findPackagingCaffee.getWeightToOut());
            remittancesInternalOrderView.jComboBox4.getModel().setSelectedItem(findPackagingCaffee.getShippingLinesId());
            remittancesInternalOrderView.jComboBox4.repaint();
            remittancesInternalOrderView.jComboBox6.getModel().setSelectedItem(findPackagingCaffee.getNavyAgentId());
            remittancesInternalOrderView.jComboBox6.repaint();
            remittancesInternalOrderView.jTextField2.setText(findPackagingCaffee.getMotorShipName());
            remittancesInternalOrderView.jComboBox7.getModel().setSelectedItem(findPackagingCaffee.getIsoCtn());
            listDetailPackagingCaffee = detailPackagingCaffeeJpaController.findListDetailPackagingCaffeeByOIE(findPackagingCaffee.getId());
            tmpListDetailPackagingCaffee = detailPackagingCaffeeJpaController.findListDetailPackagingCaffeeByOIE(Integer.parseInt(OIEFind));
            exporterName = listDetailPackagingCaffee.get(0).getRemittancesCaffee().getClientId().getBusinessName();
            remittancesInternalOrderView.jComboBox3.getModel().setSelectedItem((String)exporterName);
            remittancesInternalOrderView.jComboBox3.repaint();
            showListDetailPackagingCaffee(listDetailPackagingCaffee);
            //List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeList = findPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList();
            listDetailAdictionalElements = findPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList();
            showListAdictionalElementsHasPackagingCaffee(listDetailAdictionalElements);
        }
        else{
            JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "No se encontro ninguna OIE", "Ningun registro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showListDetailPackagingCaffee(List<DetailPackagingCaffee> listDetailPackagingCaffee) {
        int sizeList = listDetailPackagingCaffee.size();
        System.out.println(sizeList);
        rowDataCovered = new Object[sizeList][3];
        Iterator <DetailPackagingCaffee> iteratorRemittancesCaffee = listDetailPackagingCaffee.iterator();
        int indexRow=0;
        Object columnNames[] = {"Remesa","Lote","# Sacos"};
        while(iteratorRemittancesCaffee.hasNext()) {
            DetailPackagingCaffee detailPackagingCaffee = iteratorRemittancesCaffee.next();
            rowDataCovered[indexRow][0] = detailPackagingCaffee.getRemittancesCaffee().getId();
            rowDataCovered[indexRow][1] = detailPackagingCaffee.getRemittancesCaffee().getLotCaffee();
            rowDataCovered[indexRow][2] = detailPackagingCaffee.getQuantityRadicatedBagOut();
            indexRow++;
        }
        remittancesCaffeeCovered = new DefaultTableModel(rowDataCovered,columnNames);
        remittancesInternalOrderView.jTable2.setModel(remittancesCaffeeCovered);
        remittancesInternalOrderView.jTable2.repaint();
    }
    
    private void showListAdictionalElementsHasPackagingCaffee(List <AdictionalElementsHasPackagingCaffee> listDetailAdictionalElements) {
        int sizeList = listDetailAdictionalElements.size();
        rowDataAdictionalElement = new Object[sizeList][3];
        Iterator <AdictionalElementsHasPackagingCaffee> iteratorAdictionalElementsHasPackagingCaffee = listDetailAdictionalElements.iterator();
        int indexRow=0;
        Object columnNames[] = {"Elemento","Unidad","Cantidad"};
        while(iteratorAdictionalElementsHasPackagingCaffee.hasNext()) {
            AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffee = iteratorAdictionalElementsHasPackagingCaffee.next();
            rowDataAdictionalElement[indexRow][0] = adictionalElementsHasPackagingCaffee.getAdictionalElements().getName();
            rowDataAdictionalElement[indexRow][1] = adictionalElementsHasPackagingCaffee.getTypeUnit();
            rowDataAdictionalElement[indexRow][2] = adictionalElementsHasPackagingCaffee.getQuantity();
            indexRow++;
        }
        remittancesCaffeeCovered = new DefaultTableModel(rowDataAdictionalElement,columnNames);
        remittancesInternalOrderView.jTable3.setModel(remittancesCaffeeCovered);
        remittancesInternalOrderView.jTable3.clearSelection();
        remittancesInternalOrderView.jTable3.repaint();
    }
    
    private void showRemittancesCaffeeByExporter(String nameExporter) {
        Clients exporterSelected = findclientsByNameLocal(nameExporter);
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByExporter(exporterSelected);
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int sizeList = listRemittancesCaffee.size();
        int indexRow=0;
        rowDataProcess = new Object[sizeList][6];
        Object columnNames[] = { "Fecha","Remesa", "Sacos Rad Out","Lote","Sacos In", "Sacos Out"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
            rowDataProcess[indexRow][0] = dt1.format(remittance.getCreatedDate());
            rowDataProcess[indexRow][1] = remittance.getId();
            rowDataProcess[indexRow][2] = remittance.getQuantityBagRadicatedOut();
            rowDataProcess[indexRow][3] = remittance.getLotCaffee();
            rowDataProcess[indexRow][4] = remittance.getQuantityBagInStore();
            rowDataProcess[indexRow][5] = remittance.getQuantityBagOutStore();
            indexRow++;

        }
        this.defaultTableModel = new DefaultTableModel(rowDataProcess,columnNames);
        remittancesInternalOrderView.jTable1.setModel(defaultTableModel);
        remittancesInternalOrderView.jTable1.repaint();
    }
    
    private Clients findclientsByNameLocal(String name) {
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
    
    private ShippingLines findshippingLinesByNameLocal(String name) {
        Iterator<ShippingLines> iterator = this.listShippingLines.iterator();
        ShippingLines shippingLines = null;
        while(iterator.hasNext()) {
            shippingLines = iterator.next();
            if(shippingLines.getBusinessName().matches(name)) {
                return shippingLines;
            }
        }
        return shippingLines;
    }
    
    private NavyAgent findNavyAgentByNameLocal(String name) {
        Iterator<NavyAgent> iterator = this.listNavyAgent.iterator();
        NavyAgent navyAgent = null;
        while(iterator.hasNext()) {
            navyAgent = iterator.next();
            if(navyAgent.getName().matches(name)) {
                return navyAgent;
            }
        }
        return navyAgent;
    }
    
    private StatePackaging findStatePackagingByNameLocal(String name) {
        Iterator<StatePackaging> iterator = statePackagingJpaController.findStatePackagingEntities().iterator();
        StatePackaging statePackaging = null;
        while(iterator.hasNext()) {
            statePackaging = iterator.next();
            if(statePackaging.getName().matches(name)) {
                return statePackaging;
            }
        }
        return statePackaging;
    }
    
    private AdictionalElements findAdictionalElementsByNameLocal(String name) {
        Iterator<AdictionalElements> iteratorAdictionalElements = adictionalElementsJpaController.findAdictionalElementsEntities().iterator();
        AdictionalElements adictionalElements = null;
        while(iteratorAdictionalElements.hasNext()) {
            adictionalElements = iteratorAdictionalElements.next();
            if(adictionalElements.getName().matches(name)) {
                return adictionalElements;
            }
        }
        return adictionalElements;
    }
   
    private void modifyListDetailPackagingCaffee(int idRemittancesCaffee){
        int size = listDetailPackagingCaffee.size();
        int ind=0;
        while(ind != size) {
            DetailPackagingCaffee getDetailPackagingCaffee = listDetailPackagingCaffee.get(ind);
            if(getDetailPackagingCaffee.getRemittancesCaffee().getId() == idRemittancesCaffee) {
                RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(idRemittancesCaffee);
                Integer nowQuantityBagRadicatedOut = findRemittancesCaffee.getQuantityBagRadicatedOut();
                Integer radQuantityBags = getDetailPackagingCaffee.getQuantityRadicatedBagOut();
                findRemittancesCaffee.setQuantityBagRadicatedOut(nowQuantityBagRadicatedOut-radQuantityBags);
                try {
                    detailPackagingCaffeeJpaController.destroy(getDetailPackagingCaffee.getDetailPackagingCaffeePK());
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                listDetailPackagingCaffee.remove(ind);
            }
            ind++;
        }
    }
    
    private void modifyListidAdictionalElements(String adictionalElements){
        int size = listDetailAdictionalElements.size();
        int ind=0;
        while(ind != size) {
            AdictionalElementsHasPackagingCaffee objectNextlistDetailAdictionalElements = listDetailAdictionalElements.get(ind);
            if(objectNextlistDetailAdictionalElements.getAdictionalElements().getName().matches(adictionalElements)){
                
                try {
                    adictionalElementsHasPackagingCaffeeJpaController.destroy(objectNextlistDetailAdictionalElements.getAdictionalElementsHasPackagingCaffeePK());
                } catch (Exception ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                listDetailAdictionalElements.remove(ind);
            }
            ind++;
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object eventObject = e.getSource();
        if(eventObject.getClass() == JComboBox.class){
            if (e.getStateChange() == ItemEvent.SELECTED) {
                exporterName = (String) e.getItem();
                showRemittancesCaffeeByExporter(exporterName);
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 10){
            String OIEFind = remittancesInternalOrderView.jTextField5.getText();
            System.out.println(OIEFind);
            if(OIEFind != null){
                find(OIEFind);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private List <DetailPackagingCaffee> listDetailPackagingCaffee;
    private List <AdictionalElementsHasPackagingCaffee> listDetailAdictionalElements;
    private List<DetailPackagingCaffee> tmpListDetailPackagingCaffee;
    private boolean flagEditedDetailPackagingCaffee = false;
    private boolean flagEditedAdictionalElements = false;
    private final String username;
    private String exporterName;
    private Object[][] rowDataCovered;
    private Object[][] rowDataAdictionalElement;
    private DefaultTableModel defaultTableModel;
    private DefaultTableModel remittancesCaffeeCovered;
    private Object[][] rowDataProcess;
    private List<Clients> listClients;
    private List<ShippingLines> listShippingLines;
    private List<NavyAgent> listNavyAgent;
    private NavyAgentJpaController navyAgentJpaController;
    private RemittancesCaffeeJpaController remittancesCaffeeJpaController;
    private ShippingLinesJpaController shippingLinesJpaController;
    private final RemittancesInternalOrderView remittancesInternalOrderView;
    private ClientsJpaController clientsJpaController;
    private MyComboBoxModel adictionalElementModel;
    private MyComboBoxModel exportersComboxModel;
    private MyComboBoxModel shippingLinesComboxModel;
    private MyComboBoxModel navyAgentComboxModel;
    private MyComboBoxModel statePackagingModel;
    private PackagingCaffeeJpaController packagingCaffeeJpaController;
    private StatePackagingJpaController statePackagingJpaController;
    private DetailPackagingCaffeeJpaController detailPackagingCaffeeJpaController;
    private AdictionalElementsJpaController adictionalElementsJpaController;
    private AdictionalElementsHasPackagingCaffeeJpaController adictionalElementsHasPackagingCaffeeJpaController;
}
