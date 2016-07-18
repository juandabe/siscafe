
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import siscafe.model.CitySource;
import siscafe.model.Customs;
import siscafe.model.MarkCaffee;
import siscafe.model.RemittancesCaffee;
import siscafe.model.Shippers;
import siscafe.model.UnitsCaffee;
import siscafe.model.Users;
import siscafe.model.WeighingDownloadCaffee;
import siscafe.model.WeighingPackagingCaffee;
import siscafe.persistence.CitySourceJpaController;
import siscafe.persistence.CustomsJpaController;
import siscafe.persistence.MarkCaffeeJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.ShippersJpaController;
import siscafe.persistence.WeighingDownloadCaffeeJpaController;
import siscafe.persistence.WeighingPackagingCaffeeJpaController;
import siscafe.report.Reports;
import siscafe.util.BasculeSerialPort;
import siscafe.util.MyComboBoxModel;
import siscafe.util.ReaderProperties;
import siscafe.view.frontend.WeighingView;

/**
 *
 * @author jecheverri
 */
public class WeighingController implements ActionListener{

    public WeighingController(WeighingView weighingView, WeighingDownloadCaffee weighingDownloadCaffee, WeighingPackagingCaffee weighingPackagingCaffee, Users userLoged) {
        this.weighingView = weighingView;
        this.basculeUser = userLoged;
        this.weighingDownloadCaffee = weighingDownloadCaffee;
        this.weighingPackagingCaffee = weighingPackagingCaffee;
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        weighingDownloadCaffeeJpaController = new WeighingDownloadCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        weighingPackagingCaffeeJpaController = new WeighingPackagingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        markCaffeeJpaController = new MarkCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        shippersJpaController = new ShippersJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        customsJpaController = new CustomsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        citySourceJpaController = new CitySourceJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
     }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Class classUI = e.getSource().getClass();
        if(classUI.equals(JButton.class)) {
            String btn = ((JButton)e.getSource()).getName();
            switch (btn){
                case "toWeigh":
                    toWeight();
                break;
                case "save":
                    saveDocumentationRemettancesCaffee();
                break;
                case "confirm":
                    endProcessWeight();
                break;
                case "print":
                    print();
                break;
            }
        }
        else if (classUI.equals(JMenuItem.class)) {
            String btn = ((JMenuItem)e.getSource()).getName();
            switch (btn){
                case "refresh":
                    refresh();
                break;
            }
        }
    }
    
    public void activeBascule() {
        try {
            this.basculeSerialPort = new BasculeSerialPort(weighingView.jLabel1);
            this.basculeSerialPort.connect();
        } catch (Exception ex) {
            Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void endProcessWeight() {
        String operationType = new ReaderProperties().getProperties("WEIGHTOPERATION");
        switch(operationType) {
            case "ENTRADA":
                int bagsRadicated = remittancesCaffeeProcess.getQuantityBagRadicatedIn();
                int bagsInStore = remittancesCaffeeProcess.getQuantityBagInStore();
                if(bagsInStore == bagsRadicated) {
                    remittancesCaffeeProcess.setStatusOperation(Integer.valueOf(new ReaderProperties().getProperties("STATUS_CAFFEE_WEIGHTCOMPLETED")));
                    try {
                        remittancesCaffeeJpaController.edit(remittancesCaffeeProcess);
                    } catch (Exception ex) {
                        Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    JOptionPane.showMessageDialog(weighingView, "Finalizo proceso pesaje de la remesa "+remittancesCaffeeProcess.getId(), "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                    refreshListInProcessWight(operationType);
                }
                else {
                    int balanceBags = bagsRadicated -= bagsInStore;
                    JOptionPane.showMessageDialog(weighingView, "No se puede finalizar el proceso de pesaje de la remesa "+remittancesCaffeeProcess.getId()+
                            "; hacen falta pesar "+balanceBags+" sacos!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                }
            break;
            case "SALIDA":
                try {
                    int bagsInRemittancesCaffee = remittancesCaffeeProcess.getQuantityBagInStore();
                    int quantityBagsRemittancesCaffee = weighingPackagingCaffeeJpaController.countBagsByRemettencesCaffee(remittancesCaffeeProcess.getId());
                    if(bagsInRemittancesCaffee == quantityBagsRemittancesCaffee) {
                        remittancesCaffeeProcess.setStatusOperation(6);
                        remittancesCaffeeJpaController.edit(remittancesCaffeeProcess);
                        JOptionPane.showMessageDialog(weighingView, "Se embalo completamente la remesa "+remittancesCaffeeProcess.getId(), "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        
                    }
                } catch (Exception ex) {
                    Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            break;
        }
    }
    
    private void print() {
        new Reports().showReportDownloadCaffee(remittancesCaffeeProcess);
    }
    
    private void toWeight() {
        String operationType = new ReaderProperties().getProperties("WEIGHTOPERATION");
        Date dNow = new Date();
        int selectedRow=weighingView.jTable2.getSelectedRow();
        String value = String.valueOf(rowDataProcess[selectedRow][2]);
        int remettancesId = Integer.valueOf(value);
        remittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
        if(weighingView.jCheckBox1.isSelected()) {     
            //double newTare = Double.valueOf(weighingView.jLabel1.getText());
            double newTare = 816.0;
            if("ENTRADA".equals(operationType)) {
                double oldTare = remittancesCaffee.getTareDownload();
                if(oldTare != 0) {
                    remittancesCaffee.setTareDownload(newTare);
                    JOptionPane.showInternalMessageDialog(weighingView, "Se actualizo la tara correctamente", "Actualización exitoso", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    remittancesCaffee.setTareDownload(newTare);
                    JOptionPane.showInternalMessageDialog(weighingView, "Se registro la tara correctamente", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            else {
                double oldTare = remittancesCaffee.getTarePackaging();
                if(oldTare != 0) {
                    remittancesCaffee.setTarePackaging(newTare);
                    JOptionPane.showInternalMessageDialog(weighingView, "Se actualizo la tara correctamente", "Actualización exitoso", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    remittancesCaffee.setTarePackaging(newTare);
                    JOptionPane.showInternalMessageDialog(weighingView, "Se registro la tara correctamente", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            try {
                System.out.println(remittancesCaffee.getId());
                remittancesCaffeeJpaController.edit(remittancesCaffee);
                refresh();
            } catch (Exception ex) {
                Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            switch(operationType) {
                case "ENTRADA":
                    remittancesCaffeeProcess = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                    //double newWeight = Double.valueOf(weighingView.jLabel1.getText());
                    double newWeight = 1855.0;
                    double numBagsInJpinner = Double.parseDouble(weighingView.jSpinner1.getValue().toString());
                    int bagsStoreNewIn = (int)(numBagsInJpinner);
                    int bagsInStore = remittancesCaffeeProcess.getQuantityBagInStore();
                    int bagsRadicatedStore = remittancesCaffeeProcess.getQuantityBagRadicatedIn();
                    bagsInStore += bagsStoreNewIn;
                    if(bagsInStore <= bagsRadicatedStore) {
                        weighingDownloadCaffee.setWeighingDate(dNow);
                        weighingDownloadCaffee.setRemittancesCaffeeId(remittancesCaffeeProcess);
                        weighingDownloadCaffee.setQuantityBagPallet(bagsStoreNewIn);
                        weighingDownloadCaffee.setWeightPallet(newWeight);
                        String numPalletString = String.valueOf(weighingDownloadCaffeeJpaController.countPalletByRemettencesCaffee(remittancesCaffee.getId()));
                        int numPalletInt = Integer.valueOf(numPalletString);
                        remittancesCaffeeProcess.setQuantityInPalletCaffee(numPalletInt+1);
                        remittancesCaffeeProcess.setQuantityBagInStore(bagsInStore);
                        weighingDownloadCaffee.setSeqWeightPallet(numPalletInt+1);
                        remittancesCaffeeProcess.setDownloadCaffeeDate(new Date());
                        weighingDownloadCaffeeJpaController.create(weighingDownloadCaffee);
                        try {
                            double totalWeight = weighingDownloadCaffeeJpaController.countWeightByRemettencesCaffee(remettancesId);
                            remittancesCaffeeProcess.setTotalWeightNetReal(totalWeight);
                            remittancesCaffeeJpaController.edit(remittancesCaffeeProcess);
                        } catch (Exception ex) {
                            Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(weighingView, "Peso registrado!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                        RemittancesCaffee remittancesCaffeeSelected = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                        refreshListWeightingRemettances(remittancesCaffeeSelected, operationType);
                        refreshListInProcessWight(operationType);  
                    }
                    else {
                        JOptionPane.showMessageDialog(weighingView, "No se registro el peso. Sucedido lo siguiente:\n"
                                + "1. El pesaje de la remesa esta completa con el # de sacos\n"
                                + "2. Se quiere pesar mas sacos de los que estan radicados.\n"
                                + "Verifique por favor la informacion de la remesa.", "Novedad registro pesaje", JOptionPane.INFORMATION_MESSAGE);
                    }
                break;
                case "SALIDA":
                    remittancesCaffeeProcess = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                    //double newWeightOut = Double.valueOf(weighingView.jLabel1.getText());
                    double newWeightOut = 1855.0;
                    double numBagsOutJpinner = Double.parseDouble(weighingView.jSpinner1.getValue().toString());
                    int bagsStoreRetireNow = (int)(numBagsOutJpinner);
                    int bagsInStoreNow = remittancesCaffeeProcess.getQuantityBagInStore();
                    int bagsInStoreBalance = (bagsInStoreNow - bagsStoreRetireNow);
                    if(bagsInStoreBalance >= 0) {
                        weighingPackagingCaffee.setWeighingDate(dNow);
                        weighingPackagingCaffee.setRemittancesCafeeId(remittancesCaffeeProcess);
                        weighingPackagingCaffee.setQuantityBagPallet(bagsStoreRetireNow);
                        weighingPackagingCaffee.setWeightPallet(newWeightOut);
                        String numPalletStringOut = String.valueOf(weighingPackagingCaffeeJpaController.countPalletByRemettencesCaffee(remittancesCaffee.getId()));
                        int numPalletOut = Integer.valueOf(numPalletStringOut);
                        int numPalletInNow = remittancesCaffeeProcess.getQuantityInPalletCaffee();
                        remittancesCaffeeProcess.setQuantityOutPalletCaffee(numPalletOut+1);
                        remittancesCaffeeProcess.setQuantityInPalletCaffee(numPalletInNow-1);
                        remittancesCaffeeProcess.setQuantityBagInStore(bagsInStoreBalance);
                        int bagsStoreOut = remittancesCaffeeProcess.getQuantityBagOutStore();
                        remittancesCaffeeProcess.setQuantityBagOutStore(bagsStoreOut+bagsStoreRetireNow);
                        weighingPackagingCaffee.setSeqWeightPallet(numPalletOut+1);
                        remittancesCaffeeProcess.setPackagingCaffeeDate(new Date());
                        weighingPackagingCaffeeJpaController.create(weighingPackagingCaffee);
                        try {
                            double weightPackaging = weighingPackagingCaffeeJpaController.countWeightByRemettencesCaffee(remittancesCaffeeProcess.getId());
                            remittancesCaffeeProcess.setWeightPalletPackagingTotal(weightPackaging);
                            remittancesCaffeeJpaController.edit(remittancesCaffeeProcess);
                        } catch (Exception ex) {
                            Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        JOptionPane.showMessageDialog(weighingView, "Peso registrado!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                        RemittancesCaffee remittancesCaffeeSelected = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                        refreshListWeightingRemettances(remittancesCaffeeSelected, operationType);
                        refreshListInProcessWight(operationType);  
                    }
                    else {
                        JOptionPane.showMessageDialog(weighingView, "No se registro el peso. Pudo haber sucedido lo siguiente:\n"
                                + "1. El pesaje de salida de la remesa esta completa con el # de sacos\n"
                                + "2. Se quiere repesar mas sacos de los que la remesa tiene en bodega.\n"
                                + "Verifique por favor la informacion de la remesa.", "Novedad registro pesaje", JOptionPane.INFORMATION_MESSAGE);
                    }
                break;
            }
            
        }
    }
    
    private void saveDocumentationRemettancesCaffee() {
        String shipper = (String) weighingView.jComboBox1.getSelectedItem();
        remittancesCaffeeProcess.setShippersId(findShippersByNameLocal(shipper));
        String citySource = (String) weighingView.jComboBox2.getSelectedItem();
        remittancesCaffeeProcess.setCitySourceId(findCitySourceByNameLocal(citySource));
        String markCaffee = (String) weighingView.jComboBox4.getSelectedItem();
        remittancesCaffeeProcess.setMarkCafeeId(findMarkCaffeeByNameLocal(markCaffee));
        remittancesCaffeeProcess.setDetailsWeight(weighingView.jTextArea1.getText());
        remittancesCaffeeProcess.setObservation(weighingView.jTextArea2.getText());
        try {
            remittancesCaffeeJpaController.edit(remittancesCaffeeProcess);
        } catch (Exception ex) {
            Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            JOptionPane.showMessageDialog(weighingView, "Información actualizada!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void initListener() {
        weighingView.jButton3.addActionListener(this);
        weighingView.jButton4.addActionListener(this);
        weighingView.jMenuItem4.addActionListener(this);
        weighingView.jButton1.addActionListener(this);
        weighingView.jButton2.addActionListener(this);
        initListenerInPendientWeight();
        initListenerInProcessWeight();
        refresh();
    }
    
    private void initListenerInProcessWeight() {
        String operationType = new ReaderProperties().getProperties("WEIGHTOPERATION");
        switch(operationType) {
            case "ENTRADA":
                ListSelectionModel model=weighingView.jTable2.getSelectionModel();
                model.addListSelectionListener((ListSelectionEvent e) -> {
                    if(!model.isSelectionEmpty()){
                        int selectedRow=weighingView.jTable2.getSelectedRow();
                        String value = String.valueOf(rowDataProcess[selectedRow][2]);
                        int remettancesId = Integer.valueOf(value);
                        weighingView.jTextField1.setText(value);
                        remittancesCaffeeProcess = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                        UnitsCaffee unitsCaffee = remittancesCaffeeProcess.getUnitsCafeeId();
                        //double newWeight = Double.valueOf(weighingView.jLabel1.getText());
                        double newWeight = 1855.0;
                        double newBagsPallet = newWeight / unitsCaffee.getQuantity();
                        this.weighingView.jSpinner1.setValue(Math.ceil(newBagsPallet));
                        refreshListWeightingRemettances(remittancesCaffeeProcess, operationType);
                        CitySource citySource = remittancesCaffeeProcess.getCitySourceId();
                        weighingView.jTextField3.setText("");
                        weighingView.jComboBox1.setModel(myComboBoxModelShippers);
                        weighingView.jComboBox1.repaint();
                        weighingView.jComboBox2.setModel(myComboBoxModelCitySource);
                        weighingView.jComboBox2.repaint();
                        weighingView.jComboBox4.setModel(myComboBoxModelMarkCaffee);
                        weighingView.jComboBox4.repaint();
                        weighingView.jTextArea1.setText("");
                        weighingView.jTextArea2.setText("");
                        //weighingView.jTextField3.setText(remittancesCaffeeProcess.getDownloadCaffeeDate().toLocaleString());
                        if(citySource != null) {
                            weighingView.jComboBox1.getModel().setSelectedItem(remittancesCaffeeProcess.getShippersId().getBusinessName());
                            weighingView.jComboBox2.getModel().setSelectedItem(remittancesCaffeeProcess.getCitySourceId().getCityName());
                            weighingView.jComboBox4.getModel().setSelectedItem(remittancesCaffeeProcess.getMarkCafeeId().getName());
                            weighingView.jTextArea1.setText(remittancesCaffeeProcess.getDetailsWeight());
                            weighingView.jTextArea2.setText(remittancesCaffeeProcess.getObservation());
                        }
                    }
                });
            break;
            case "SALIDA":
                ListSelectionModel modelOrdered=weighingView.jTable2.getSelectionModel();
                modelOrdered.addListSelectionListener((ListSelectionEvent e) -> {
                    if(!modelOrdered.isSelectionEmpty()){
                        int selectedRow=weighingView.jTable2.getSelectedRow();
                        String value = String.valueOf(rowDataProcess[selectedRow][2]);
                        int remettancesId = Integer.valueOf(value);
                        weighingView.jTextField1.setText(value);
                        remittancesCaffeeProcess = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                        UnitsCaffee unitsCaffee = remittancesCaffeeProcess.getUnitsCafeeId();
                        //double newWeight = Double.valueOf(weighingView.jLabel1.getText());
                        double newWeight = 1855.0;
                        double newBagsPallet = newWeight / unitsCaffee.getQuantity();
                        this.weighingView.jSpinner1.setValue(Math.ceil(newBagsPallet));
                        refreshListWeightingRemettances(remittancesCaffeeProcess, operationType);
                        CitySource citySource = remittancesCaffeeProcess.getCitySourceId();
                        weighingView.jTextField3.setText("");
                        weighingView.jComboBox1.setModel(myComboBoxModelShippers);
                        weighingView.jComboBox1.repaint();
                        weighingView.jComboBox2.setModel(myComboBoxModelCitySource);
                        weighingView.jComboBox2.repaint();
                        weighingView.jComboBox4.setModel(myComboBoxModelMarkCaffee);
                        weighingView.jComboBox4.repaint();
                        weighingView.jTextArea1.setText("");
                        weighingView.jTextArea2.setText("");
                        //weighingView.jTextField3.setText(remittancesCaffeeProcess.getDownloadCaffeeDate().toLocaleString());
                        if(citySource != null) {
                            weighingView.jComboBox1.getModel().setSelectedItem(remittancesCaffeeProcess.getShippersId().getBusinessName());
                            weighingView.jComboBox2.getModel().setSelectedItem(remittancesCaffeeProcess.getCitySourceId().getCityName());
                            weighingView.jComboBox4.getModel().setSelectedItem(remittancesCaffeeProcess.getMarkCafeeId().getName());
                            weighingView.jTextArea1.setText(remittancesCaffeeProcess.getDetailsWeight());
                            weighingView.jTextArea2.setText(remittancesCaffeeProcess.getObservation());
                        }
                    }
                });
            break;
        }
    }
    
    private void initListenerInPendientWeight() {
        String operationType = new ReaderProperties().getProperties("WEIGHTOPERATION");
        switch(operationType) {
            case "ENTRADA":
                ListSelectionModel model=weighingView.jTable1.getSelectionModel();
                model.addListSelectionListener((ListSelectionEvent e) -> {
                    if(!model.isSelectionEmpty()){
                        int selectedRow=weighingView.jTable1.getSelectedRow();
                        String value = String.valueOf(rowDataPendient[selectedRow][1]);
                        int remettancesId = Integer.valueOf(value);
                        int selectionAnswer = JOptionPane.showInternalConfirmDialog(weighingView, "¿Desea procesar esta remesa "+remettancesId+"?", "Confirmación", JOptionPane.YES_NO_OPTION);
                        if(selectionAnswer == 0) {
                            try {
                                RemittancesCaffee remittancesCaffeeSelected = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                                remittancesCaffeeSelected.setStatusOperation(Integer.valueOf("2"));
                                remittancesCaffeeSelected.setStaffWtInId(basculeUser);
                                remittancesCaffeeJpaController.edit(remittancesCaffeeSelected);
                                refresh();
                            } catch (Exception ex) {
                                Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else {
                            model.clearSelection();
                        }
                    }
                });
            break;
            case "SALIDA":
                ListSelectionModel modelOredered=weighingView.jTable1.getSelectionModel();
                modelOredered.addListSelectionListener((ListSelectionEvent e) -> {
                    if(!modelOredered.isSelectionEmpty()){
                        int selectedRow=weighingView.jTable1.getSelectedRow();
                        String value = String.valueOf(rowDataPendient[selectedRow][1]);
                        int remettancesId = Integer.valueOf(value);
                        int selectionAnswer = JOptionPane.showInternalConfirmDialog(weighingView, "¿Desea procesar esta remesa "+remettancesId+"?", "Confirmación", JOptionPane.YES_NO_OPTION);
                        if(selectionAnswer == 0) {
                            try {
                                RemittancesCaffee remittancesCaffeeSelected = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                                remittancesCaffeeSelected.setStatusOperation(5);
                                remittancesCaffeeSelected.setStaffWtInId(basculeUser);
                                remittancesCaffeeJpaController.edit(remittancesCaffeeSelected);
                                refresh();
                            } catch (Exception ex) {
                                Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else {
                            modelOredered.clearSelection();
                        }
                    }
                });
        }
    }

    private void refresh() {
        String operationType = new ReaderProperties().getProperties("WEIGHTOPERATION");
        refreshListPendientWieght(operationType);
        refreshListInProcessWight(operationType);
    }
    
    private void refreshListWeightingRemettances(RemittancesCaffee remittancesCaffee, String operationType) {
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        switch(operationType) {
            case "ENTRADA":
                List <WeighingDownloadCaffee> listWeighingDownloadCaffee = weighingDownloadCaffeeJpaController.findWeighingDownloadCaffeeEntitiesByRemettances(remittancesCaffee);
                Iterator <WeighingDownloadCaffee> iteratorWeighingDownloadCaffee = listWeighingDownloadCaffee.iterator();
                int sizeList = listWeighingDownloadCaffee.size();
                int indexRow=0;
                rowDatalistWeighingCaffee = new Object[sizeList][5];
                Object columnNames[] = { "Fecha","Seq", "Cantidad Sacos","Peso", "Remesa"};
                while(iteratorWeighingDownloadCaffee.hasNext()) {
                    WeighingDownloadCaffee weighingDownloadCaffeeLocal = iteratorWeighingDownloadCaffee.next();
                    rowDatalistWeighingCaffee[indexRow][0] = dt1.format(weighingDownloadCaffeeLocal.getWeighingDate());
                    rowDatalistWeighingCaffee[indexRow][1] = weighingDownloadCaffeeLocal.getSeqWeightPallet();
                    rowDatalistWeighingCaffee[indexRow][2] = weighingDownloadCaffeeLocal.getQuantityBagPallet();
                    rowDatalistWeighingCaffee[indexRow][3] = weighingDownloadCaffeeLocal.getWeightPallet();
                    rowDatalistWeighingCaffee[indexRow][4] = weighingDownloadCaffeeLocal.getRemittancesCaffeeId().getId();
                    indexRow++;
                }
                DefaultTableModel defaultTableModel = new DefaultTableModel(rowDatalistWeighingCaffee,columnNames);
                weighingView.jTable3.setModel(defaultTableModel);
                weighingView.jTable3.repaint();
            break;
            case "SALIDA":
                List <WeighingPackagingCaffee> listWeighingPackagingCaffee = weighingPackagingCaffeeJpaController.findWeighingPackagingCaffeeEntitiesByRemettances(remittancesCaffee);
                Iterator <WeighingPackagingCaffee> iteratorWeighingPackagingCaffee = listWeighingPackagingCaffee.iterator();
                int sizeListOrdered = listWeighingPackagingCaffee.size();
                int indexRowOrdered=0;
                rowDatalistWeighingCaffee = new Object[sizeListOrdered][5];
                Object columnNamesOrdered[] = { "Fecha","Seq", "Cantidad Sacos","Peso", "Remesa"};
                while(iteratorWeighingPackagingCaffee.hasNext()) {
                    WeighingPackagingCaffee weighingPackagingCaffeeLocal = iteratorWeighingPackagingCaffee.next();
                    rowDatalistWeighingCaffee[indexRowOrdered][0] = dt1.format(weighingPackagingCaffeeLocal.getWeighingDate());
                    rowDatalistWeighingCaffee[indexRowOrdered][1] = weighingPackagingCaffeeLocal.getSeqWeightPallet();
                    rowDatalistWeighingCaffee[indexRowOrdered][2] = weighingPackagingCaffeeLocal.getQuantityBagPallet();
                    rowDatalistWeighingCaffee[indexRowOrdered][3] = weighingPackagingCaffeeLocal.getWeightPallet();
                    rowDatalistWeighingCaffee[indexRowOrdered][4] = weighingPackagingCaffeeLocal.getRemittancesCafeeId().getId();
                    indexRowOrdered++;
                }
                DefaultTableModel defaultTableModelOrdered = new DefaultTableModel(rowDatalistWeighingCaffee,columnNamesOrdered);
                weighingView.jTable3.setModel(defaultTableModelOrdered);
                weighingView.jTable3.repaint();
            break;
        }
    }
    
    private void refreshListInProcessWight(String operationType) {
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        switch(operationType) {
            case "ENTRADA":
                List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeInProcessWieght();
                Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
                int sizeList = listRemittancesCaffee.size();
                int indexRow=0;
                rowDataProcess = new Object[sizeList][11];
                Object columnNames[] = { "Fecha","Motorista","Remesa", "Lote", "Exportador","Sacos Radicados", "Sacos In","Pallet In", "Sacos Out","Pallet Out","Total Tara"};
                while(iteratorRemittancesCaffee.hasNext()) {
                    RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
                    rowDataProcess[indexRow][0] = dt1.format(remittance.getCreatedDate());
                    rowDataProcess[indexRow][1] = remittance.getStaffDriverId().getFirstName();
                    rowDataProcess[indexRow][2] = remittance.getId();
                    rowDataProcess[indexRow][3] = remittance.getLotCaffee();
                    rowDataProcess[indexRow][4] = remittance.getClientId().getExporterCode();
                    rowDataProcess[indexRow][5] = remittance.getQuantityBagRadicatedIn();
                    rowDataProcess[indexRow][6] = remittance.getQuantityBagInStore();
                    rowDataProcess[indexRow][7] = remittance.getQuantityInPalletCaffee();
                    rowDataProcess[indexRow][8] = remittance.getQuantityBagOutStore();
                    rowDataProcess[indexRow][9] = remittance.getQuantityOutPalletCaffee();
                    rowDataProcess[indexRow][10] = remittance.getTareDownload();
                    indexRow++;

                }
                DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataProcess,columnNames);
                weighingView.jTable2.setModel(defaultTableModel);
                weighingView.jTable2.repaint();
                listCitySource = citySourceJpaController.findCitySourceEntities();
                myComboBoxModelCitySource = new MyComboBoxModel(listCitySource);
                weighingView.jComboBox2.setModel(myComboBoxModelCitySource);
                weighingView.jComboBox2.repaint();
                listShippers = shippersJpaController.findShippersEntities();
                myComboBoxModelShippers = new MyComboBoxModel(listShippers);
                weighingView.jComboBox1.setModel(myComboBoxModelShippers);
                weighingView.jComboBox1.repaint();
                listCustoms = customsJpaController.findCustomsEntities();
                myComboBoxModelCustoms = new MyComboBoxModel(listCustoms);
                //weighingView.jComboBox3.setModel(myComboBoxModelCustoms);
                listMarkCaffee = markCaffeeJpaController.findMarkCaffeeEntities();
                myComboBoxModelMarkCaffee = new MyComboBoxModel(listMarkCaffee);
                weighingView.jComboBox4.setModel(myComboBoxModelMarkCaffee);
                weighingView.jComboBox4.repaint();
                break;
            case "SALIDA":
                List <RemittancesCaffee> listRemittancesCaffeeProcessPckaging = remittancesCaffeeJpaController.findRemittancesCaffeeProcessPckaging();
                Iterator <RemittancesCaffee> iteratorRemittancesCaffeeProcessPckaging = listRemittancesCaffeeProcessPckaging.iterator();
                int sizeListProcessPckaging = listRemittancesCaffeeProcessPckaging.size();
                int indexRowProcessPckaging=0;
                rowDataProcess = new Object[sizeListProcessPckaging][11];
                Object columnNamesProcessPckaging[] = { "Fecha","Motorista","Remesa", "Lote", "Exportador","Sacos Radicados", "Sacos In","Pallet In", "Sacos Out","Pallet Out","Total Tara"};
                while(iteratorRemittancesCaffeeProcessPckaging.hasNext()) {
                    RemittancesCaffee remittance = iteratorRemittancesCaffeeProcessPckaging.next();
                    rowDataProcess[indexRowProcessPckaging][0] = dt1.format(remittance.getCreatedDate());
                    rowDataProcess[indexRowProcessPckaging][1] = remittance.getStaffDriverId().getFirstName();
                    rowDataProcess[indexRowProcessPckaging][2] = remittance.getId();
                    rowDataProcess[indexRowProcessPckaging][3] = remittance.getLotCaffee();
                    rowDataProcess[indexRowProcessPckaging][4] = remittance.getClientId().getExporterCode();
                    rowDataProcess[indexRowProcessPckaging][5] = remittance.getQuantityBagRadicatedIn();
                    rowDataProcess[indexRowProcessPckaging][6] = remittance.getQuantityBagInStore();
                    rowDataProcess[indexRowProcessPckaging][7] = remittance.getQuantityInPalletCaffee();
                    rowDataProcess[indexRowProcessPckaging][8] = remittance.getQuantityBagOutStore();
                    rowDataProcess[indexRowProcessPckaging][9] = remittance.getQuantityOutPalletCaffee();
                    rowDataProcess[indexRowProcessPckaging][10] = remittance.getTarePackaging();
                    indexRowProcessPckaging++;
                }
                DefaultTableModel defaultTableModelProcessPckaging = new DefaultTableModel(rowDataProcess,columnNamesProcessPckaging);
                weighingView.jTable2.setModel(defaultTableModelProcessPckaging);
                weighingView.jTable2.repaint();
                listCitySource = citySourceJpaController.findCitySourceEntities();
                myComboBoxModelCitySource = new MyComboBoxModel(listCitySource);
                weighingView.jComboBox2.setModel(myComboBoxModelCitySource);
                weighingView.jComboBox2.repaint();
                listShippers = shippersJpaController.findShippersEntities();
                myComboBoxModelShippers = new MyComboBoxModel(listShippers);
                weighingView.jComboBox1.setModel(myComboBoxModelShippers);
                weighingView.jComboBox1.repaint();
                listCustoms = customsJpaController.findCustomsEntities();
                myComboBoxModelCustoms = new MyComboBoxModel(listCustoms);
                //weighingView.jComboBox3.setModel(myComboBoxModelCustoms);
                listMarkCaffee = markCaffeeJpaController.findMarkCaffeeEntities();
                myComboBoxModelMarkCaffee = new MyComboBoxModel(listMarkCaffee);
                weighingView.jComboBox4.setModel(myComboBoxModelMarkCaffee);
                weighingView.jComboBox4.repaint();
                break;
        }
    }
    
    private void refreshListPendientWieght(String operationType) {
        switch (operationType) {
            case "ENTRADA":
                List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeePendientWeight();
                Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
                int sizeList = listRemittancesCaffee.size();
                int indexRow=0;
                rowDataPendient = new Object[sizeList][8];
                Object columnNames[] = { "Fecha", "Remesa", "Lote", "Exportador","Sacos Radicados","Sacos In","Motorista","Muestreador"};
                while(iteratorRemittancesCaffee.hasNext()) {
                    RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
                    rowDataPendient[indexRow][0] = remittance.getCreatedDate();
                    rowDataPendient[indexRow][1] = remittance.getId();
                    rowDataPendient[indexRow][2] = remittance.getLotCaffee();
                    rowDataPendient[indexRow][3] = remittance.getClientId().getBusinessName();
                    rowDataPendient[indexRow][4] = remittance.getQuantityBagRadicatedIn();
                    rowDataPendient[indexRow][6] = remittance.getQuantityBagInStore();
                    rowDataPendient[indexRow][5] = remittance.getStaffDriverId().getFirstName();
                    rowDataPendient[indexRow][7] = remittance.getStaffSampleId().getFirstName();
                    indexRow++;

                }
                DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataPendient,columnNames);
                weighingView.jTable1.setModel(defaultTableModel);
                weighingView.jTable1.repaint();
            break;
            case "SALIDA":
                List <RemittancesCaffee> listRemittancesCaffeeOrdered = remittancesCaffeeJpaController.findRemittancesCaffeeOrdered();
                Iterator <RemittancesCaffee> iteratorRemittancesCaffeeOrdered = listRemittancesCaffeeOrdered.iterator();
                int sizeListOrdered = listRemittancesCaffeeOrdered.size();
                int indexRowOrdered=0;
                rowDataPendient = new Object[sizeListOrdered][8];
                Object columnNamesOrdered[] = { "Fecha", "Remesa", "Lote", "Exportador","Sacos Radicados","Sacos In","Motorista","Muestreador"};
                while(iteratorRemittancesCaffeeOrdered.hasNext()) {
                    RemittancesCaffee remittance = iteratorRemittancesCaffeeOrdered.next();
                    rowDataPendient[indexRowOrdered][0] = remittance.getCreatedDate();
                    rowDataPendient[indexRowOrdered][1] = remittance.getId();
                    rowDataPendient[indexRowOrdered][2] = remittance.getLotCaffee();
                    rowDataPendient[indexRowOrdered][3] = remittance.getClientId().getBusinessName();
                    rowDataPendient[indexRowOrdered][4] = remittance.getQuantityBagRadicatedIn();
                    rowDataPendient[indexRowOrdered][5] = remittance.getQuantityBagInStore();
                    rowDataPendient[indexRowOrdered][6] = remittance.getStaffDriverId().getFirstName();
                    rowDataPendient[indexRowOrdered][7] = remittance.getStaffSampleId().getFirstName();
                    indexRowOrdered++;
                }
                DefaultTableModel defaultTableModelOrdered = new DefaultTableModel(rowDataPendient,columnNamesOrdered);
                weighingView.jTable1.setModel(defaultTableModelOrdered);
                weighingView.jTable1.repaint();
        }
        
    }

    private CitySource findCitySourceByNameLocal(String name) {
        Iterator<CitySource> iterator = this.listCitySource.iterator();
        CitySource citySource = null;
        while(iterator.hasNext()) {
            citySource = iterator.next();
            String fullNameUser = citySource.getCityName();
            if(fullNameUser.matches(name)) {
                return citySource;
            }
        }
        return citySource;
    }
    
    private Shippers findShippersByNameLocal(String name) {
        Iterator<Shippers> iterator = this.listShippers.iterator();
        Shippers shippers = null;
        while(iterator.hasNext()) {
            shippers = iterator.next();
            String fullNameUser = shippers.getBusinessName();
            if(fullNameUser.matches(name)) {
                return shippers;
            }
        }
        return shippers;
    }
    
    private Customs findCustomsByNameLocal(String name) {
        Iterator<Customs> iterator = this.listCustoms.iterator();
        Customs customs = null;
        while(iterator.hasNext()) {
            customs = iterator.next();
            String fullNameUser = customs.getCiaName();
            if(fullNameUser.matches(name)) {
                return customs;
            }
        }
        return customs;
    }
    
    private MarkCaffee findMarkCaffeeByNameLocal(String name) {
        Iterator<MarkCaffee> iterator = this.listMarkCaffee.iterator();
        MarkCaffee markCaffee = null;
        while(iterator.hasNext()) {
            markCaffee = iterator.next();
            String fullNameUser = markCaffee.getName();
            if(fullNameUser.matches(name)) {
                return markCaffee;
            }
        }
        return markCaffee;
    }
    
    private Object [][] rowDataPendient;
    private Object [][] rowDataProcess;
    private Object [][] rowDatalistWeighingCaffee;
    private Users basculeUser;
    private List <CitySource> listCitySource;
    private MyComboBoxModel myComboBoxModelCitySource;
    private List <Shippers> listShippers;
    private MyComboBoxModel myComboBoxModelShippers;
    private List <Customs> listCustoms;
    private MyComboBoxModel myComboBoxModelCustoms;
    private List <MarkCaffee> listMarkCaffee;
    private MyComboBoxModel myComboBoxModelMarkCaffee;
    private WeighingDownloadCaffeeJpaController weighingDownloadCaffeeJpaController;
    private WeighingPackagingCaffeeJpaController weighingPackagingCaffeeJpaController;
    private RemittancesCaffeeJpaController remittancesCaffeeJpaController;
    private WeighingDownloadCaffee weighingDownloadCaffee;
    private WeighingPackagingCaffee weighingPackagingCaffee;
    private RemittancesCaffee remittancesCaffee;
    private final WeighingView weighingView;
    private BasculeSerialPort basculeSerialPort;
    private MarkCaffeeJpaController markCaffeeJpaController;
    private ShippersJpaController shippersJpaController;
    private CitySourceJpaController citySourceJpaController;
    private CustomsJpaController customsJpaController;
    private RemittancesCaffee remittancesCaffeeProcess;
    //double newWeight = 1816.0;
}
