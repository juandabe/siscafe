/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import siscafe.model.RemittancesCaffee;
import siscafe.model.WeighingPackagingCaffee;
import siscafe.persistence.WeighingPackagingCaffeeJpaController;
import siscafe.view.frontend.FractionPalletCaffeeView;

/**
 *
 * @author Administrador
 */
public class FractionPalletCaffeeController implements ActionListener{
    
    public FractionPalletCaffeeController(FractionPalletCaffeeView fractionPalletCaffeeView) {
        weighingPackagingCaffeeJpaController = new WeighingPackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.fractionPalletCaffeeView = fractionPalletCaffeeView;
    }
    
    public void initListener() {
        fractionPalletCaffeeView.jButton6.addActionListener(this);
        initSelectionTableToFractionPallet();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "add":
                //add();
            break;
            case "find":
                find();
            break;
        }
    }
    
    private void repaintListWeightCaffee(List<WeighingPackagingCaffee> listWeightCaffeeAdded) {
        Iterator <WeighingPackagingCaffee> iteratorRemittancesCaffee = listWeightCaffeeAdded.iterator();
        int sizeList = listWeightCaffeeAdded.size();
        int indexRow=0;
        rowDataWeightCaffee = new Object[sizeList][3];
        Object columnNames[] = { "Id","Sacos","Peso"};
        while(iteratorRemittancesCaffee.hasNext()) {
            WeighingPackagingCaffee weighingPackagingCaffee = iteratorRemittancesCaffee.next();
            rowDataWeightCaffee[indexRow][0] = weighingPackagingCaffee.getSeqWeightPallet();
            rowDataWeightCaffee[indexRow][1] = weighingPackagingCaffee.getQuantityBagPallet();
            rowDataWeightCaffee[indexRow][2] = weighingPackagingCaffee.getWeightPallet();
            indexRow++;
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataWeightCaffee,columnNames);
        fractionPalletCaffeeView.jTable1.setModel(defaultTableModel);
        fractionPalletCaffeeView.jTable1.repaint();
    }
    
    private void repaintListFractionWeightCaffee(List<WeighingPackagingCaffee> listWeightCaffeeAdded) {
        Iterator <WeighingPackagingCaffee> iteratorRemittancesCaffee = listWeightCaffeeAdded.iterator();
        int sizeList = listWeightCaffeeAdded.size();
        int indexRow=0;
        rowDataWeightCaffee = new Object[sizeList][3];
        Object columnNames[] = { "Id","Sacos","Peso"};
        while(iteratorRemittancesCaffee.hasNext()) {
            WeighingPackagingCaffee weighingPackagingCaffee = iteratorRemittancesCaffee.next();
            rowDataWeightCaffee[indexRow][0] = weighingPackagingCaffee.getSeqWeightPallet();
            rowDataWeightCaffee[indexRow][1] = weighingPackagingCaffee.getQuantityBagPallet();
            rowDataWeightCaffee[indexRow][2] = weighingPackagingCaffee.getWeightPallet();
            indexRow++;
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataWeightCaffee,columnNames);
        fractionPalletCaffeeView.jTable3.setModel(defaultTableModel);
        fractionPalletCaffeeView.jTable3.repaint();
    }
    
    private void find(){
        String remittancesCaffeeId = fractionPalletCaffeeView.jTextField1.getText();
        List<WeighingPackagingCaffee> listWeightPackagingCaffee = weighingPackagingCaffeeJpaController.
                findWeighingPackagingCaffeeEntitiesByRemettances(Integer.parseInt(remittancesCaffeeId));
        if(listWeightPackagingCaffee.size() != 0){
            repaintListWeightCaffee(listWeightPackagingCaffee);
            remittancesCafeeFind = listWeightPackagingCaffee.get(0).getRemittancesCafeeId();
        }
        else{
            JOptionPane.showInternalMessageDialog(fractionPalletCaffeeView, 
                    "No se encontro ninguna remesa", "Ningun registro", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    private void initSelectionTableToFractionPallet() {
        ListSelectionModel model=fractionPalletCaffeeView.jTable1.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(!model.isSelectionEmpty()){
                int selectedRow=fractionPalletCaffeeView.jTable1.getSelectedRow();
                int numFractionCaffee = (int) fractionPalletCaffeeView.jSpinner1.getValue();
                String weighingPackagingCaffeeId = String.valueOf(rowDataWeightCaffee[selectedRow][0]);
                WeighingPackagingCaffee palletCaffeeToFraction = weighingPackagingCaffeeJpaController.
                        findWeighingPackagingCaffee(Integer.parseInt(weighingPackagingCaffeeId));
                int showConfirmDialog = JOptionPane.showConfirmDialog(fractionPalletCaffeeView, 
                        "¿Desea fraccionar el pallet "+palletCaffeeToFraction.getSeqWeightPallet()+"?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if(showConfirmDialog == 0) {
                    if(numFractionCaffee > 0){
                        fractionPalletCaffee(palletCaffeeToFraction,numFractionCaffee);
                    }
                    else{
                        JOptionPane.showInternalMessageDialog(fractionPalletCaffeeView, 
                    "Por favor digite un número de fracciones correcto!", "Ningun registro", JOptionPane.ERROR_MESSAGE);
                        fractionPalletCaffeeView.jTable1.clearSelection();
                    }
                }
                else{
                    fractionPalletCaffeeView.jTable1.clearSelection();
                }
            }
        });
    }
    
    private void fractionPalletCaffee(WeighingPackagingCaffee palletCaffeeToFraction, int numFraction){
        WeighingPackagingCaffee fractionWeightCaffee;
        int nunmBagByPallet = Integer.parseInt(fractionPalletCaffeeView.jTextField2.getText());
        Double weightByBagPackagingCaffee = (palletCaffeeToFraction.getWeightPallet())/nunmBagByPallet;
        int seqFractionPallet = rowDataWeightCaffee.length;
        System.out.println("Antes: "+seqFractionPallet);
        List <WeighingPackagingCaffee> listWeighingPackagingCaffee = new ArrayList();
        int countBagFraction=0;
        for(int i=1; i<=numFraction; i++){
            String strBagsPallet = JOptionPane.showInternalInputDialog(fractionPalletCaffeeView, 
                    "Ingrese el número de sacos para la fracción #"+i+", total sacos fraccionados actualmente "+countBagFraction, 
                    "Ingresar valor", JOptionPane.INFORMATION_MESSAGE);
            Integer numBagsPalletInteger=Integer.parseInt(strBagsPallet);
            while(numBagsPalletInteger > nunmBagByPallet || (countBagFraction+numBagsPalletInteger) > nunmBagByPallet){
                strBagsPallet = JOptionPane.showInternalInputDialog(fractionPalletCaffeeView, 
                    "Ingrese el número de sacos para la fracción #"+i+", total sacos fraccionados actualmente "+countBagFraction, 
                    "Ingresar valor", JOptionPane.INFORMATION_MESSAGE);
                numBagsPalletInteger=Integer.parseInt(strBagsPallet);
            }
            Double weightBagPackagingCaffee = (numBagsPalletInteger*weightByBagPackagingCaffee);
            NumberFormat formater = new DecimalFormat("#0.0");
            countBagFraction+=numBagsPalletInteger;
            fractionWeightCaffee = new WeighingPackagingCaffee();
            fractionWeightCaffee.setIsFraction(false);
            fractionWeightCaffee.setQuantityBagPallet(numBagsPalletInteger);
            fractionWeightCaffee.setRemittancesCafeeId(remittancesCafeeFind);
            fractionWeightCaffee.setSeqWeightPallet(seqFractionPallet+1);
            fractionWeightCaffee.setWeightPallet(Double.parseDouble(formater.format(weightBagPackagingCaffee).replace(",", ".")));
            fractionWeightCaffee.setWeighingDate(new Date());
            seqFractionPallet=fractionWeightCaffee.getSeqWeightPallet();
            listWeighingPackagingCaffee.add(fractionWeightCaffee);
            System.out.println(seqFractionPallet);
        }
        int showConfirmDialog = JOptionPane.showConfirmDialog(fractionPalletCaffeeView, 
        "¿Desea continuar con la fracción del pallet #"+
                palletCaffeeToFraction.getSeqWeightPallet()+", para un total de sacos fraccionados de "+countBagFraction+"?", "Confirmación", 
                JOptionPane.YES_NO_OPTION);
        if(showConfirmDialog == 0){
            try {
                palletCaffeeToFraction.setIsFraction(true);
                weighingPackagingCaffeeJpaController.edit(palletCaffeeToFraction);
            } catch (Exception ex) {
                Logger.getLogger(FractionPalletCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Iterator <WeighingPackagingCaffee> iteratorListWeighingPackagingCaffee = listWeighingPackagingCaffee.iterator();
            while(iteratorListWeighingPackagingCaffee.hasNext()){
                WeighingPackagingCaffee nextWeighingPackagingCaffee = iteratorListWeighingPackagingCaffee.next();
                weighingPackagingCaffeeJpaController.create(nextWeighingPackagingCaffee);
            }
            repaintListFractionWeightCaffee(listWeighingPackagingCaffee);
        }
        
    }
    
    private Object rowDataWeightCaffee [][];
    private WeighingPackagingCaffeeJpaController weighingPackagingCaffeeJpaController;
    private FractionPalletCaffeeView fractionPalletCaffeeView;
    private RemittancesCaffee remittancesCafeeFind;
}
