/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import siscafe.model.PackagingCaffee;
import siscafe.model.RemittancesCaffee;
import siscafe.model.WeighingPackagingCaffee;
import siscafe.persistence.PackagingCaffeeJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.WeighingPackagingCaffeeJpaController;
import siscafe.view.frontend.CommodityCaffeeDeliverView;

/**
 *
 * @author Administrador
 */
public class CommodityCaffeeDeliverController implements ActionListener{
    
    public CommodityCaffeeDeliverController(CommodityCaffeeDeliverView commodityCaffeeDeliverView) {
        packagingCaffeeJpaController = new PackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        weighingPackagingCaffeeJpaController = new WeighingPackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        listToAddWeighingPackagingCaffee = new ArrayList();
        this.commodityCaffeeDeliverView = commodityCaffeeDeliverView;
    }
    
    public void initListener() {
        commodityCaffeeDeliverView.jButton6.addActionListener(this);
        commodityCaffeeDeliverView.jTextField3.addKeyListener(new KeyAdapter() {
          @Override
          public void keyTyped(KeyEvent e) {
            char keyChar = e.getKeyChar();
            if (Character.isLowerCase(keyChar)) {
              e.setKeyChar(Character.toUpperCase(keyChar));
            }
          }
        });
        initSelectionTableConsultPallet();
        initSelectionTableAddPallet();
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
        }
    }
    
    private void add() {
//        if(remittancesCaffeeId != null) {
//            Object rowData [][] = new Object[listToAddCaffee.size()][2];
//            Object columnNames[] = { "id", "Peso"};
//            int indexObject=0;
//            List<WeighingPackagingCaffee> listMaster = weighingPackagingCaffeeJpaController.
//                    findWeighingPackagingCaffeeEntitiesByRemettances(remittancesCaffeeId);
//            Iterator <WeighingPackagingCaffee> iteratorListMaster = listMaster.iterator();
//            Iterator <WeighingPackagingCaffee> iteratorListTarget = listToAddCaffee.iterator();
//            while(iteratorListTarget.hasNext()) {
//                WeighingPackagingCaffee target = iteratorListTarget.next();
//                while(iteratorListMaster.hasNext()){
//                    WeighingPackagingCaffee master = iteratorListMaster.next();
//                    if(Objects.equals(master.getId(), target.getId())) {
//                        rowData[indexObject][0] = master.getId();
//                        rowData[indexObject][1] = master.getId();
//                    }
//                }
//                indexObject++;
//            }
//            DefaultTableModel defaultTableModel = new DefaultTableModel(rowData,columnNames);
//            commodityCaffeeDeliverView.jTable2.setModel(defaultTableModel);
//            commodityCaffeeDeliverView.jTable2.repaint();
//        }
    }
    
    private void find(){
        String DEX = commodityCaffeeDeliverView.jTextField1.getText();
        PackagingCaffee findPackingCaffeeByDEX = packagingCaffeeJpaController.findPackingCaffeeByDEX(DEX);
        if(findPackingCaffeeByDEX != null){
            commodityCaffeeDeliverView.jLabel12.setText(findPackingCaffeeByDEX.getRemittancesCaffeeList().get(0).getClientId().getBusinessName());
            commodityCaffeeDeliverView.jLabel13.setText(findPackingCaffeeByDEX.getShippingLinesId().getBusinessName());
            commodityCaffeeDeliverView.jLabel14.setText(findPackingCaffeeByDEX.getPortOperatorsId().getName());
            commodityCaffeeDeliverView.jLabel15.setText(findPackingCaffeeByDEX.getNavyAgentId().getName());
            commodityCaffeeDeliverView.jLabel16.setText(findPackingCaffeeByDEX.getMotorShipId());
            commodityCaffeeDeliverView.jLabel11.setText(findPackingCaffeeByDEX.getPackagingMode());
            commodityCaffeeDeliverView.jLabel10.setText(findPackingCaffeeByDEX.getPackagingType());
            repaintListRemittancesCaffee(findPackingCaffeeByDEX.getRemittancesCaffeeList());
        }
        else {
            JOptionPane.showMessageDialog(commodityCaffeeDeliverView, "No se encontro ningun registro!", "Error información", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void initSelectionTableConsultPallet() {
        ListSelectionModel model=commodityCaffeeDeliverView.jTable1.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(!model.isSelectionEmpty()){
                int selectedRow=commodityCaffeeDeliverView.jTable1.getSelectedRow();
                String remettancesId = String.valueOf(rowDataRemittancesCaffee[selectedRow][0]);
                remittancesCaffeeId = Integer.parseInt(remettancesId);
                listWeightPackagingByRemittancesCaffee = weighingPackagingCaffeeJpaController.
                        findWeighingPackagingCaffeeEntitiesByRemettances(remittancesCaffeeId);
                repaintListWeighingPackagingCaffee(listWeightPackagingByRemittancesCaffee);
            }
        });
    }
    
    private void initSelectionTableAddPallet() {
        ListSelectionModel model=commodityCaffeeDeliverView.jTable3.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(!model.isSelectionEmpty()){
                int selectedRow=commodityCaffeeDeliverView.jTable3.getSelectedRow();
                String weighingPackagingCaffeeId = String.valueOf(rowDataWeighingPackagingCaffee[selectedRow][0]);
                WeighingPackagingCaffee findWeighingPackagingCaffeeToAdd = weighingPackagingCaffeeJpaController.
                        findWeighingPackagingCaffee(Integer.parseInt(weighingPackagingCaffeeId));
                int showConfirmDialog = JOptionPane.showConfirmDialog(commodityCaffeeDeliverView, 
                        "¿Desea agregar este pallet al embalaje?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if(showConfirmDialog == 0) {
                    listWeightPackagingByRemittancesCaffee.remove(selectedRow);
                    listToAddWeighingPackagingCaffee.add(findWeighingPackagingCaffeeToAdd);
                    repaintListWeightCaffeeAdded(listToAddWeighingPackagingCaffee);
                    repaintListWeighingPackagingCaffee(listWeightPackagingByRemittancesCaffee);
                    resumenPackagingCaffee(listToAddWeighingPackagingCaffee);
                }
            }
        });
    }
    
    private void resumenPackagingCaffee(List<WeighingPackagingCaffee> listWeightCaffeeAdded){
        Double totalLoadCaffee;
        Double netWeight;
        Integer numBagsPackaging=0;
        Double grossWeight=0.0;
        Iterator <WeighingPackagingCaffee> iteratorWeightPackagingCaffee = listWeightCaffeeAdded.iterator();
        RemittancesCaffee findRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(remittancesCaffeeId);
        while(iteratorWeightPackagingCaffee.hasNext()){
            WeighingPackagingCaffee weighingPackagingCaffee = iteratorWeightPackagingCaffee.next();
            grossWeight+=weighingPackagingCaffee.getWeightPallet();
            numBagsPackaging+=weighingPackagingCaffee.getQuantityBagPallet();
        }
        Double taraPackaging=findRemittancesCaffee.getTarePackaging();
        Double taraPacking=(numBagsPackaging*findRemittancesCaffee.getPackingCafeeId().getWeight());
        netWeight=(grossWeight)-(taraPackaging+taraPacking);
        totalLoadCaffee = grossWeight-taraPackaging;
        commodityCaffeeDeliverView.jTextField2.setText(String.valueOf(numBagsPackaging));
        commodityCaffeeDeliverView.jTextField4.setText(String.valueOf(netWeight));
        commodityCaffeeDeliverView.jTextField5.setText(String.valueOf(grossWeight));
        commodityCaffeeDeliverView.jTextField6.setText(String.valueOf(taraPackaging));
        commodityCaffeeDeliverView.jTextField7.setText(String.valueOf(taraPacking));
        commodityCaffeeDeliverView.jTextField8.setText(String.valueOf(totalLoadCaffee));
    }
    
    private void repaintListRemittancesCaffee(List<RemittancesCaffee> listRemittancesCaffee) {
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int sizeList = listRemittancesCaffee.size();
        int indexRow=0;
        rowDataRemittancesCaffee = new Object[sizeList][1];
        Object columnNames[] = { "Remesa"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittancesCaffee = iteratorRemittancesCaffee.next();
            rowDataRemittancesCaffee[indexRow][0] = remittancesCaffee.getId();
            indexRow++;
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataRemittancesCaffee,columnNames);
        commodityCaffeeDeliverView.jTable1.setModel(defaultTableModel);
        commodityCaffeeDeliverView.jTable1.repaint();
    }
    
    private void repaintListWeightCaffeeAdded(List<WeighingPackagingCaffee> listWeightCaffeeAdded) {
        Iterator <WeighingPackagingCaffee> iteratorRemittancesCaffee = listWeightCaffeeAdded.iterator();
        int sizeList = listWeightCaffeeAdded.size();
        int indexRow=0;
        rowDataWieghtCaffeeAdded = new Object[sizeList][2];
        Object columnNames[] = { "Id","Peso"};
        while(iteratorRemittancesCaffee.hasNext()) {
            WeighingPackagingCaffee weighingPackagingCaffee = iteratorRemittancesCaffee.next();
            rowDataWieghtCaffeeAdded[indexRow][0] = weighingPackagingCaffee.getSeqWeightPallet();
            rowDataWieghtCaffeeAdded[indexRow][1] = weighingPackagingCaffee.getWeightPallet();
            indexRow++;
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataWieghtCaffeeAdded,columnNames);
        commodityCaffeeDeliverView.jTable2.setModel(defaultTableModel);
        commodityCaffeeDeliverView.jTable2.repaint();
    }
    
    private void repaintListWeighingPackagingCaffee(List <WeighingPackagingCaffee> listWeightPackagingByRemittancesCaffee) {
        Iterator <WeighingPackagingCaffee> iteratorWeighingPackagingCaffee = listWeightPackagingByRemittancesCaffee.iterator();
        int sizeList = listWeightPackagingByRemittancesCaffee.size();
        int indexRow=0;
        rowDataWeighingPackagingCaffee = new Object[sizeList][4];
        Object columnNames[] = { "id","Sacos","Peso"};
        while(iteratorWeighingPackagingCaffee.hasNext()) {
            WeighingPackagingCaffee weighingPackagingCaffee = iteratorWeighingPackagingCaffee.next();
            rowDataWeighingPackagingCaffee[indexRow][0] = weighingPackagingCaffee.getSeqWeightPallet();
            rowDataWeighingPackagingCaffee[indexRow][1] = weighingPackagingCaffee.getQuantityBagPallet();
            rowDataWeighingPackagingCaffee[indexRow][2] = weighingPackagingCaffee.getWeightPallet();
            indexRow++;
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataWeighingPackagingCaffee,columnNames);
        commodityCaffeeDeliverView.jTable3.setModel(defaultTableModel);
        commodityCaffeeDeliverView.jTable3.repaint();
    }
    
    private List <WeighingPackagingCaffee> listToAddWeighingPackagingCaffee;
    private List<WeighingPackagingCaffee> listWeightPackagingByRemittancesCaffee;
    private Integer remittancesCaffeeId;
    private Object rowDataRemittancesCaffee[][];
    private Object rowDataWieghtCaffeeAdded[][];
    private Object rowDataWeighingPackagingCaffee [][];
    private PackagingCaffeeJpaController packagingCaffeeJpaController;
    private CommodityCaffeeDeliverView commodityCaffeeDeliverView;
    private WeighingPackagingCaffeeJpaController weighingPackagingCaffeeJpaController;
    private RemittancesCaffeeJpaController remittancesCaffeeJpaController;
}
