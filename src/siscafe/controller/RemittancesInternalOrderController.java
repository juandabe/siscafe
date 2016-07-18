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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import siscafe.model.Clients;
import siscafe.model.NavyAgent;
import siscafe.model.PackagingCaffee;
import siscafe.model.PortOperators;
import siscafe.model.RemittancesCaffee;
import siscafe.model.ShippingLines;
import siscafe.persistence.ClientsJpaController;
import siscafe.persistence.NavyAgentJpaController;
import siscafe.persistence.PackagingCaffeeJpaController;
import siscafe.persistence.PortOperatorsJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.ShippingLinesJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.report.Reports;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.RemittancesInternalOrderView;

/**
 *
 * @author Administrador
 */
public class RemittancesInternalOrderController implements ActionListener, ItemListener, ListSelectionListener {
    
    public RemittancesInternalOrderController(RemittancesInternalOrderView remittancesInternalOrderView, String username) {
        this.remittancesInternalOrderView = remittancesInternalOrderView;
        this.username = username;
    }
    
    public void initListener() {
        remittancesInternalOrderView.jComboBox3.addItemListener(this);
        remittancesInternalOrderView.jButton4.addActionListener(this);
        remittancesInternalOrderView.jButton6.addActionListener(this);
        remittancesInternalOrderView.jButton1.addActionListener(this);
        remittancesInternalOrderView.jTable2.getSelectionModel().addListSelectionListener(this);
        clientsJpaController = new ClientsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        shippingLinesJpaController = new ShippingLinesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        portOperatorsJpaController = new PortOperatorsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        navyAgentJpaController = new NavyAgentJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        packagingCaffeeJpaController = new PackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        exportersComboxModel = new MyComboBoxModel(clientsJpaController.findClientsEntities());
        shippingLinesComboxModel = new MyComboBoxModel(shippingLinesJpaController.findShippingLinesEntities());
        portOperatorComboxModel = new MyComboBoxModel(portOperatorsJpaController.findPortOperatorsEntities());
        navyAgentComboxModel = new MyComboBoxModel(navyAgentJpaController.findNavyAgentEntities());
        remittancesInternalOrderView.jComboBox3.setModel(exportersComboxModel);
        remittancesInternalOrderView.jComboBox4.setModel(shippingLinesComboxModel);
        remittancesInternalOrderView.jComboBox5.setModel(portOperatorComboxModel);
        remittancesInternalOrderView.jComboBox6.setModel(navyAgentComboxModel);
        listClients = clientsJpaController.findClientsEntities();
        listShippingLines = shippingLinesJpaController.findShippingLinesEntities();
        listPortOperators = portOperatorsJpaController.findPortOperatorsEntities();
        listNavyAgent = navyAgentJpaController.findNavyAgentEntities();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "add":
                add();
            break;
            case "find":
                find();
            break;
            case "clear":
                clear();
            break;
            case "print":
                print();
            break;
        }
    }
    
    private void print() {
        String DEX = remittancesInternalOrderView.jTextField1.getText();
        new Reports().reportCommodityCaffeeDeliver(DEX, this.username);
    }
    
    private void add() {
        int row[] = remittancesInternalOrderView.jTable1.getSelectedRows();
        Date dateNow = remittancesInternalOrderView.jXDatePicker1.getDate();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String DEX = remittancesInternalOrderView.jTextField1.getText();
        List <RemittancesCaffee> listRemittancesCaffee = new ArrayList();
        PackagingCaffee packagingCaffee = new PackagingCaffee();
        packagingCaffee.setExportStatement(DEX);
        packagingCaffee.setPackagingType((String) remittancesInternalOrderView.jComboBox1.getSelectedItem());
        packagingCaffee.setNavyAgentId(findNavyAgentByNameLocal((String) remittancesInternalOrderView.jComboBox6.getSelectedItem()));
        packagingCaffee.setShippingLinesId(findshippingLinesByNameLocal((String) remittancesInternalOrderView.jComboBox4.getSelectedItem()));
        packagingCaffee.setPortOperatorsId(findPortOperatorsByNameLocal((String) remittancesInternalOrderView.jComboBox5.getSelectedItem()));
        packagingCaffee.setCreatedDate(new Date(formatter.format(dateNow)));
        packagingCaffee.setMotorShipId(remittancesInternalOrderView.jTextField2.getText());
        packagingCaffee.setPackagingMode((String)remittancesInternalOrderView.jComboBox2.getSelectedItem());
        for(int i=0;i<row.length; i++){
            int remittancesId =  (int) this.defaultTableModel.getValueAt(row[i], 1);
            RemittancesCaffee remittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(remittancesId);
            remittancesCaffee.setStatusOperation(4);
            try {
                remittancesCaffeeJpaController.edit(remittancesCaffee);
            } catch (Exception ex) {
                Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
            listRemittancesCaffee.add(remittancesCaffee);
        }
        packagingCaffee.setRemittancesCaffeeList(listRemittancesCaffee);
        try {
            packagingCaffeeJpaController.create(packagingCaffee);
            Iterator<RemittancesCaffee> remittancesCaffeeIterator = packagingCaffee.getRemittancesCaffeeList().iterator();
            System.out.println(packagingCaffee.getExportStatement());
            while(remittancesCaffeeIterator.hasNext()) {
                RemittancesCaffee remittancesCaffee= remittancesCaffeeIterator.next();
                System.out.println(remittancesCaffee.getTotalWeightNetReal());
            }
        } catch (Exception ex) {
            Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
        listRemittancesCaffee(listRemittancesCaffee);
        new Reports().reportCommodityCaffeeDeliver(packagingCaffee.getExportStatement(), "juandabe");
        JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "Se guardo correctamente la programación", "Programación de Café", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void edit(int remittancesCaffeeId) {
        Date dateNow = remittancesInternalOrderView.jXDatePicker1.getDate();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String DEX = remittancesInternalOrderView.jTextField1.getText();
        PackagingCaffee packagingCaffee = packagingCaffeeJpaController.findPackingCaffeeByDEX(DEX);
        List<RemittancesCaffee> listRemittancesCaffee = packagingCaffee.getRemittancesCaffeeList();
        Iterator<RemittancesCaffee> iterator = listRemittancesCaffee.iterator();
        int index=0;
        while(iterator.hasNext()) {
            RemittancesCaffee next = iterator.next();
            if(next.getId() != remittancesCaffeeId) {
                next.setStatusOperation(4);
                try {
                    packagingCaffeeJpaController.edit(packagingCaffee);
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
                }
                listRemittancesCaffee.remove(index);
            }
            index++;
        }
        listRemittancesCaffee(listRemittancesCaffee);
        packagingCaffee.setExportStatement(DEX);
        packagingCaffee.setPackagingType((String) remittancesInternalOrderView.jComboBox1.getSelectedItem());
        packagingCaffee.setNavyAgentId(findNavyAgentByNameLocal((String) remittancesInternalOrderView.jComboBox6.getSelectedItem()));
        packagingCaffee.setShippingLinesId(findshippingLinesByNameLocal((String) remittancesInternalOrderView.jComboBox4.getSelectedItem()));
        packagingCaffee.setPortOperatorsId(findPortOperatorsByNameLocal((String) remittancesInternalOrderView.jComboBox5.getSelectedItem()));
        packagingCaffee.setCreatedDate(new Date(formatter.format(dateNow)));
        packagingCaffee.setMotorShipId(remittancesInternalOrderView.jTextField2.getText());
        packagingCaffee.setPackagingMode((String)remittancesInternalOrderView.jComboBox2.getSelectedItem());
        packagingCaffee.setRemittancesCaffeeList(listRemittancesCaffee);
        try {
            packagingCaffeeJpaController.edit(packagingCaffee);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RemittancesInternalOrderController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void clear() {
        remittancesInternalOrderView.jTextField1.setText("");
        remittancesInternalOrderView.jXDatePicker1.setDate(null);
        remittancesInternalOrderView.jComboBox1.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox2.setSelectedItem("");
        remittancesInternalOrderView.jComboBox3.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox3.repaint();
        remittancesInternalOrderView.jComboBox4.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox4.repaint();
        remittancesInternalOrderView.jComboBox5.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox5.repaint();
        remittancesInternalOrderView.jComboBox6.getModel().setSelectedItem("");
        remittancesInternalOrderView.jComboBox6.repaint();
        remittancesInternalOrderView.jTextField2.setText("");
    }
    
    private void find(){
        String DEX = remittancesInternalOrderView.jTextField1.getText();
        PackagingCaffee packagingCaffeeFind = packagingCaffeeJpaController.findPackingCaffeeByDEX(DEX);
        if(packagingCaffeeFind != null) {
            List<RemittancesCaffee> listFindPackingCaffeeByDEX = packagingCaffeeJpaController.findPackingCaffeeByDEX(DEX).getRemittancesCaffeeList();
            listRemittancesCaffee(listFindPackingCaffeeByDEX);
            remittancesInternalOrderView.jXDatePicker1.setDate(packagingCaffeeFind.getCreatedDate());
            remittancesInternalOrderView.jComboBox1.setSelectedItem(packagingCaffeeFind.getPackagingType());
            remittancesInternalOrderView.jComboBox2.setSelectedItem(packagingCaffeeFind.getPackagingMode());
            remittancesInternalOrderView.jComboBox3.getModel().setSelectedItem(packagingCaffeeFind.getRemittancesCaffeeList().get(0).getClientId().getBusinessName());
            remittancesInternalOrderView.jComboBox3.repaint();
            remittancesInternalOrderView.jComboBox4.getModel().setSelectedItem(packagingCaffeeFind.getShippingLinesId().getBusinessName());
            remittancesInternalOrderView.jComboBox4.repaint();
            remittancesInternalOrderView.jComboBox5.getModel().setSelectedItem(packagingCaffeeFind.getPortOperatorsId().getName());
            remittancesInternalOrderView.jComboBox5.repaint();
            remittancesInternalOrderView.jComboBox6.getModel().setSelectedItem(packagingCaffeeFind.getNavyAgentId().getName());
            remittancesInternalOrderView.jComboBox6.repaint();
            remittancesInternalOrderView.jTextField2.setText(packagingCaffeeFind.getMotorShipId());
        }
        else {
            JOptionPane.showInternalMessageDialog(remittancesInternalOrderView, "No se ha encontrado el DEX", "ningun resultado busquedad", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void listRemittancesCaffee(List <RemittancesCaffee> listRemittancesCaffee) {
        int sizeList = listRemittancesCaffee.size();
        rowDataCovered = new Object[sizeList][1];
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int indexRow=0;
        Object columnNames[] = {"Remesa"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
            rowDataCovered[indexRow][0] = remittance.getId();
            indexRow++;
        }
        remittancesCaffeeCovered = new DefaultTableModel(rowDataCovered,columnNames);
        remittancesInternalOrderView.jTable2.setModel(remittancesCaffeeCovered);
        remittancesInternalOrderView.jTable2.clearSelection();
        remittancesInternalOrderView.jTable2.repaint();
    }
    
    private void findRemittancesCaffeeByExporter(String nameExporter) {
        Clients exporterSelected = findclientsByNameLocal(nameExporter);
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByExporter(exporterSelected);
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int sizeList = listRemittancesCaffee.size();
        int indexRow=0;
        rowDataProcess = new Object[sizeList][7];
        Object columnNames[] = { "Fecha","Remesa", "Lote","Sacos In","Pallet In", "Sacos Out","Pallet Out"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
            rowDataProcess[indexRow][0] = dt1.format(remittance.getCreatedDate());
            rowDataProcess[indexRow][1] = remittance.getId();
            rowDataProcess[indexRow][2] = remittance.getLotCaffee();
            rowDataProcess[indexRow][3] = remittance.getQuantityBagInStore();
            rowDataProcess[indexRow][4] = remittance.getQuantityInPalletCaffee();
            rowDataProcess[indexRow][5] = remittance.getQuantityBagOutStore();
            rowDataProcess[indexRow][6] = remittance.getQuantityOutPalletCaffee();
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
    
    private PortOperators findPortOperatorsByNameLocal(String name) {
        Iterator<PortOperators> iterator = this.listPortOperators.iterator();
        PortOperators portOperators = null;
        while(iterator.hasNext()) {
            portOperators = iterator.next();
            if(portOperators.getName().matches(name)) {
                return portOperators;
            }
        }
        return portOperators;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Object eventObject = e.getSource();
        if(eventObject.getClass() == JComboBox.class){
            if (e.getStateChange() == ItemEvent.SELECTED) {
                findRemittancesCaffeeByExporter((String) e.getItem());
            }
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        int showInternalConfirmDialog = JOptionPane.showInternalConfirmDialog(remittancesInternalOrderView, "¿Desea borrar esta programación?", "Borrar programación", JOptionPane.YES_NO_OPTION);
            if(showInternalConfirmDialog == 0) {
                int valueAt = (int)remittancesCaffeeCovered.getValueAt(e.getFirstIndex(), 0);
                edit(valueAt);
                remittancesInternalOrderView.jTable2.clearSelection();
                remittancesInternalOrderView.jTable2.repaint();
            }
    }
    
    private String username;
    private Object[][] rowDataCovered;
    private DefaultTableModel defaultTableModel;
    private DefaultTableModel remittancesCaffeeCovered;
    private Object[][] rowDataProcess;
    private List<Clients> listClients;
    private List<ShippingLines> listShippingLines;
    private List<PortOperators> listPortOperators;
    private List<NavyAgent> listNavyAgent;
    private NavyAgentJpaController navyAgentJpaController;
    private RemittancesCaffeeJpaController remittancesCaffeeJpaController;
    private ShippingLinesJpaController shippingLinesJpaController;
    private PortOperatorsJpaController portOperatorsJpaController;
    private RemittancesInternalOrderView remittancesInternalOrderView;
    private ClientsJpaController clientsJpaController;
    private MyComboBoxModel exportersComboxModel;
    private MyComboBoxModel shippingLinesComboxModel;
    private MyComboBoxModel portOperatorComboxModel;
    private MyComboBoxModel navyAgentComboxModel;
    private PackagingCaffeeJpaController packagingCaffeeJpaController;
    
}
