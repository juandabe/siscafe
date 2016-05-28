
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
import siscafe.persistence.CitySourceJpaController;
import siscafe.persistence.CustomsJpaController;
import siscafe.persistence.MarkCaffeeJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.ShippersJpaController;
import siscafe.persistence.WeighingDownloadCaffeeJpaController;
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

    public WeighingController(WeighingView weighingView, WeighingDownloadCaffee weighingDownloadCaffee, Users userLoged) {
        this.weighingView = weighingView;
        this.basculeUser = userLoged;
        this.weighingDownloadCaffee = weighingDownloadCaffee;
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        weighingDownloadCaffeeJpaController = new WeighingDownloadCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
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
            refreshListInProcessWight();
        }
        else {
            int balanceBags = bagsRadicated -= bagsInStore;
            JOptionPane.showMessageDialog(weighingView, "No se puede finalizar el proceso de pesaje de la remesa "+remittancesCaffeeProcess.getId()+
                    "; hacen falta pesar "+balanceBags+" sacos!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void print() {
        new Reports().showReportDownloadCaffee(remittancesCaffeeProcess);
    }
    
    private void toWeight() {
        Date dNow = new Date();
        int selectedRow=weighingView.jTable2.getSelectedRow();
        String value = String.valueOf(rowDataProcess[selectedRow][2]);
        int remettancesId = Integer.valueOf(value);
        remittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
        if(weighingView.jCheckBox1.isSelected()) {
            double oldTare = remittancesCaffee.getTotalTare();
            //double newTare = Double.valueOf(weighingView.jLabel1.getText());
            double newTare = 816.0;
            if(oldTare != 0) {
                int selectionAnswer = JOptionPane.showInternalConfirmDialog(weighingView, "¿La remesa #"+remettancesId+" tiene una tara registrada de "+oldTare+"; ¿Desea modificarla?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if(selectionAnswer == 0) {
                    remittancesCaffee.setTotalTare(newTare);
                    try {
                        remittancesCaffeeJpaController.edit(remittancesCaffee);
                    } catch (Exception ex) {
                        Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    finally {
                        JOptionPane.showMessageDialog(weighingView, "Tara actualizada!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                        weighingView.jCheckBox1.setSelected(false);
                        refreshListInProcessWight();
                    }
                }
            }
            else {
                remittancesCaffee.setTotalTare(newTare);
                try {
                    remittancesCaffeeJpaController.edit(remittancesCaffee);
                } catch (Exception ex) {
                    Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                }
                finally {
                        JOptionPane.showMessageDialog(weighingView, "Tara registrada!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                        weighingView.jCheckBox1.setSelected(false);
                        refreshListInProcessWight();
                    }
            }
        }
        else {
            //double newWeight = Double.valueOf(weighingView.jLabel1.getText());
            double numBagsJpinner = Double.parseDouble(weighingView.jSpinner1.getValue().toString());
            int bagsStoreNew = (int)(numBagsJpinner);
            int bagsInStore = remittancesCaffeeProcess.getQuantityBagInStore();
            int bagsRadicatedStore = remittancesCaffeeProcess.getQuantityBagRadicatedIn();
            if(bagsInStore < bagsRadicatedStore) {
                bagsInStore += bagsStoreNew;
                weighingDownloadCaffee.setWeighingDate(dNow);
                weighingDownloadCaffee.setRemittancesCaffeeId(remittancesCaffeeProcess);
                weighingDownloadCaffee.setQuantityBagPallet(bagsStoreNew);
                weighingDownloadCaffee.setWeightPallet(newWeight);
                String numPalletString = String.valueOf(weighingDownloadCaffeeJpaController.countPalletByRemettencesCaffee(remittancesCaffee.getId()));
                int numPalletInt = Integer.valueOf(numPalletString);
                remittancesCaffeeProcess.setQuantityInPalletCaffee(numPalletInt+1);
                remittancesCaffeeProcess.setQuantityBagInStore(bagsInStore);
                weighingDownloadCaffee.setSeqWeightPallet(numPalletInt+1);
                remittancesCaffeeProcess.setDownloadCaffeeDate(new Date());
                weighingDownloadCaffeeJpaController.create(weighingDownloadCaffee);
                try {
                    remittancesCaffeeJpaController.edit(remittancesCaffeeProcess);
                } catch (Exception ex) {
                    Logger.getLogger(WeighingController.class.getName()).log(Level.SEVERE, null, ex);
                }
                JOptionPane.showMessageDialog(weighingView, "Peso registrado!", "Confirmación", JOptionPane.INFORMATION_MESSAGE);
                RemittancesCaffee remittancesCaffeeSelected = remittancesCaffeeJpaController.findRemittancesCaffee(remettancesId);
                refreshListWightingRemettances(remittancesCaffeeSelected);
                refreshListInProcessWight();  
            }
            else {
                JOptionPane.showMessageDialog(weighingView, "No se registro peso!", "Pesaje completo", JOptionPane.INFORMATION_MESSAGE);
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
                double newBagsPallet = newWeight / unitsCaffee.getQuantity();
                this.weighingView.jSpinner1.setValue(Math.ceil(newBagsPallet));
                refreshListWightingRemettances(remittancesCaffeeProcess);
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
                weighingView.jTextField3.setText(remittancesCaffeeProcess.getDownloadCaffeeDate().toLocaleString());
                if(citySource != null) {
                    weighingView.jComboBox1.getModel().setSelectedItem(remittancesCaffeeProcess.getShippersId().getBusinessName());
                    weighingView.jComboBox2.getModel().setSelectedItem(remittancesCaffeeProcess.getCitySourceId().getCityName());
                    weighingView.jComboBox4.getModel().setSelectedItem(remittancesCaffeeProcess.getMarkCafeeId().getName());
                    weighingView.jTextArea1.setText(remittancesCaffeeProcess.getDetailsWeight());
                    weighingView.jTextArea2.setText(remittancesCaffeeProcess.getObservation());
                }
            }
        });
    }
    
    private void initListenerInPendientWeight() {
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
                        remittancesCaffeeSelected.setStatusOperation(Integer.valueOf(new ReaderProperties().getProperties("STATUS_CAFFFE_WEIGHTPROCESSING")));
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
    }

    private void refresh() {
        refreshListPendientWieght();
        refreshListInProcessWight();
    }
    
    private void refreshListWightingRemettances(RemittancesCaffee remittancesCaffee) {
        List <WeighingDownloadCaffee> listWeighingDownloadCaffee = weighingDownloadCaffeeJpaController.findWeighingDownloadCaffeeEntitiesByRemettances(remittancesCaffee);
        Iterator <WeighingDownloadCaffee> iteratorWeighingDownloadCaffee = listWeighingDownloadCaffee.iterator();
        int sizeList = listWeighingDownloadCaffee.size();
        int indexRow=0;
        rowDatalistWeighingDownloadCaffee = new Object[sizeList][5];
        Object columnNames[] = { "Fecha","Seq", "Cantidad Sacos","Peso", "Remesa"};
        while(iteratorWeighingDownloadCaffee.hasNext()) {
            WeighingDownloadCaffee weighingDownloadCaffeeLocal = iteratorWeighingDownloadCaffee.next();
            rowDatalistWeighingDownloadCaffee[indexRow][0] = weighingDownloadCaffeeLocal.getWeighingDate();
            rowDatalistWeighingDownloadCaffee[indexRow][1] = weighingDownloadCaffeeLocal.getSeqWeightPallet();
            rowDatalistWeighingDownloadCaffee[indexRow][2] = weighingDownloadCaffeeLocal.getQuantityBagPallet();
            rowDatalistWeighingDownloadCaffee[indexRow][3] = weighingDownloadCaffeeLocal.getWeightPallet();
            rowDatalistWeighingDownloadCaffee[indexRow][4] = weighingDownloadCaffeeLocal.getRemittancesCaffeeId().getId();
            indexRow++;
            
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDatalistWeighingDownloadCaffee,columnNames);
        weighingView.jTable3.setModel(defaultTableModel);
        weighingView.jTable3.repaint();
    }
    
    private void refreshListInProcessWight() {
        List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeInProcessWieght();
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int sizeList = listRemittancesCaffee.size();
        int indexRow=0;
        rowDataProcess = new Object[sizeList][9];
        Object columnNames[] = { "Fecha","Motorista","Remesa", "Lote", "Exportador","Sacos Radicados", "Sacos Descargados","In Pallet", "Total Tara"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
            rowDataProcess[indexRow][0] = remittance.getCreatedDate();
            rowDataProcess[indexRow][1] = remittance.getStaffDriverId().getFirstName();
            rowDataProcess[indexRow][2] = remittance.getId();
            rowDataProcess[indexRow][3] = remittance.getLotCaffee();
            rowDataProcess[indexRow][4] = remittance.getClientId().getBusinessName();
            rowDataProcess[indexRow][5] = remittance.getQuantityBagRadicatedIn();
            rowDataProcess[indexRow][6] = remittance.getQuantityBagInStore();
            rowDataProcess[indexRow][7] = remittance.getQuantityInPalletCaffee();
            rowDataProcess[indexRow][8] = remittance.getTotalTare();
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
    }
    
    private void refreshListPendientWieght() {
        List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeePendientWeight();
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int sizeList = listRemittancesCaffee.size();
        int indexRow=0;
        rowDataPendient = new Object[sizeList][7];
        Object columnNames[] = { "Fecha", "Remesa", "Lote", "Exportador","Sacos Radicados","Motorista","Muestreador"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittance = iteratorRemittancesCaffee.next();
            rowDataPendient[indexRow][0] = remittance.getCreatedDate();
            rowDataPendient[indexRow][1] = remittance.getId();
            rowDataPendient[indexRow][2] = remittance.getLotCaffee();
            rowDataPendient[indexRow][3] = remittance.getClientId().getBusinessName();
            rowDataPendient[indexRow][4] = remittance.getQuantityBagRadicatedIn();
            rowDataPendient[indexRow][5] = remittance.getStaffDriverId().getFirstName();
            rowDataPendient[indexRow][6] = remittance.getStaffSampleId().getFirstName();
            indexRow++;
            
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataPendient,columnNames);
        weighingView.jTable1.setModel(defaultTableModel);
        weighingView.jTable1.repaint();
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
    private Object [][] rowDatalistWeighingDownloadCaffee;
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
    private RemittancesCaffeeJpaController remittancesCaffeeJpaController;
    private WeighingDownloadCaffee weighingDownloadCaffee;
    private RemittancesCaffee remittancesCaffee;
    private final WeighingView weighingView;
    private BasculeSerialPort basculeSerialPort;
    private MarkCaffeeJpaController markCaffeeJpaController;
    private ShippersJpaController shippersJpaController;
    private CitySourceJpaController citySourceJpaController;
    private CustomsJpaController customsJpaController;
    private RemittancesCaffee remittancesCaffeeProcess;
    double newWeight = 1816.0;
}
