/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import siscafe.model.Clients;
import siscafe.model.RemittancesCaffee;
import siscafe.model.StateOperation;
import siscafe.model.StoresCaffee;
import siscafe.persistence.ClientsJpaController;
import siscafe.persistence.RemittancesCaffeeJpaController;
import siscafe.persistence.StateOperationJpaController;
import siscafe.persistence.StoresCaffeeJpaController;
import siscafe.report.Reports;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.FullConsultStateCaffeeView;

/**
 *
 * @author Administrador
 */
public class FullConsultStateCaffeeController implements ActionListener{
    
    public FullConsultStateCaffeeController(FullConsultStateCaffeeView fullConsultStateCaffeeView, String username) {
        this.fullConsultStateCaffeeView = fullConsultStateCaffeeView;
        this.username = username;
        clientsJpaController = new ClientsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        storesCaffeeJpaController = new StoresCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        remittancesCaffeeJpaController = new RemittancesCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        stateOperationJpaController = new StateOperationJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        listClients = clientsJpaController.findClientsEntities();
        listStoresCaffeeStart = storesCaffeeJpaController.findStoresCaffeeEntities();
        listStoresCaffeeEnd = storesCaffeeJpaController.findStoresCaffeeEntities();
        listStateOperation = stateOperationJpaController.findStateOperationEntities();
        myComboBoxModelExporter = new MyComboBoxModel(listClients);
        myComboBoxModelStoresCaffeeStart = new MyComboBoxModel(listStoresCaffeeStart);
        myComboBoxModelStoresCaffeeEnd = new MyComboBoxModel(listStoresCaffeeEnd);
        myComboBoxModelStateOperation = new MyComboBoxModel(listStateOperation);
    }
    
    public void initListener() {
        fullConsultStateCaffeeView.consultar.addActionListener(this);
        fullConsultStateCaffeeView.jButton1.addActionListener(this);
        fullConsultStateCaffeeView.jComboBox1.setModel(myComboBoxModelStoresCaffeeStart);
        fullConsultStateCaffeeView.jComboBox2.setModel(myComboBoxModelStoresCaffeeEnd);
        fullConsultStateCaffeeView.jComboBox3.setModel(myComboBoxModelExporter);
        fullConsultStateCaffeeView.jComboBox4.setModel(myComboBoxModelStateOperation);
        initSelectionTableConsultGeneral();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "consultgeneral":
                consultGeneral();
            break;
        }
    }
    
    private void consultGeneral() {
        String datos="";
        boolean datosErroneos = false;
        Date dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
        Date dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
        boolean findByStoreIndependient = fullConsultStateCaffeeView.jCheckBox1.isSelected();
        boolean findByAllClients = fullConsultStateCaffeeView.jCheckBox2.isSelected();
        if(dateStart == null || dateEnd == null) {
            JOptionPane.showInternalMessageDialog(fullConsultStateCaffeeView, 
                    "Consulta por rango de fechas.\nFechas invalidas. Por favor seleccione un rango de fechas correctas!", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
        }
       if(findByStoreIndependient == false) {
            datos="rangBodegas";
            String storeCaffeeSelectedStart = (String) fullConsultStateCaffeeView.jComboBox1.getSelectedItem();
            String storeCaffeeSelectedEnd = (String) fullConsultStateCaffeeView.jComboBox2.getSelectedItem();
            if(storeCaffeeSelectedStart == null || storeCaffeeSelectedEnd == null) {
                datosErroneos= true;
                JOptionPane.showInternalMessageDialog(fullConsultStateCaffeeView, 
                    "Consulta por rango de bodegas.\nBodegas no seleccionadas. Por favor seleccione un rango de bodegas correctas!", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            datos="allBodegas";
            String storeCaffeeSelectedStart = (String) fullConsultStateCaffeeView.jComboBox1.getSelectedItem();
            if(storeCaffeeSelectedStart == null) {
                datosErroneos= true;
                JOptionPane.showInternalMessageDialog(fullConsultStateCaffeeView, 
                    "Consulta por una bodega.\nBodega no seleccionada. Por favor seleccione una bodega correcta!", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(findByAllClients == false) {
            datos=datos + " "+"espClient";
            String exporterSelected = (String) fullConsultStateCaffeeView.jComboBox3.getSelectedItem();
            if(exporterSelected == null) {
                datosErroneos= true;
                JOptionPane.showInternalMessageDialog(fullConsultStateCaffeeView, 
                    "Consulta por exportador.\nExportador no seleccionado. Por favor seleccione una exportador!", "Datos invalidos", JOptionPane.ERROR_MESSAGE);
            }
        }else{
        datos=datos + " "+"allClient";
        }
        String stateSelected = (String)fullConsultStateCaffeeView.jComboBox4.getSelectedItem();
        if(stateSelected != null){
        datos=datos + " "+"state";
        }
        System.out.println(datos);
        if (datosErroneos == false){
            switch (datos){
            case "allBodegas allClient":
               dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
               dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
               List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByConsultGeneral(dateStart,dateEnd);
               refreshListRemittancesConsult(listRemittancesCaffee);
               //funciona Perfecto.
            break;
            case "allBodegas allClient state":
               dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
               dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
               stateSelected = (String)fullConsultStateCaffeeView.jComboBox4.getSelectedItem(); 
               StateOperation state=stateOperationJpaController.findStateOperationName(stateSelected);
               listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByStateandDate(state,dateStart,dateEnd);
               refreshListRemittancesConsult(listRemittancesCaffee);
               //funciona Perfecto.
            break;
            case "rangBodegas allClient state":
                dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
                dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
                String storeCaffeeSelectedStart = (String) fullConsultStateCaffeeView.jComboBox1.getSelectedItem();
                StoresCaffee StoreStart=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedStart);
                String storeCaffeeSelectedEnd = (String) fullConsultStateCaffeeView.jComboBox2.getSelectedItem();
                StoresCaffee StoreEnd=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedEnd);
                stateSelected = (String)fullConsultStateCaffeeView.jComboBox4.getSelectedItem(); 
                state=stateOperationJpaController.findStateOperationName(stateSelected);
                listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByStoreandState(state,StoreStart,StoreEnd,dateStart,dateEnd);
                refreshListRemittancesConsult(listRemittancesCaffee);
                //Listo pero no funciona por este error: The association field 'r.slotStoreId' cannot be used as a state field path.
            break;
            case "rangBodegas espClient state":
                dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
                dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
                String exporterSelected = (String) fullConsultStateCaffeeView.jComboBox3.getSelectedItem();
                Clients clients=clientsJpaController.findClientesByBusinessName(exporterSelected);
                stateSelected = (String)fullConsultStateCaffeeView.jComboBox4.getSelectedItem(); 
                state=stateOperationJpaController.findStateOperationName(stateSelected);
                storeCaffeeSelectedStart = (String) fullConsultStateCaffeeView.jComboBox1.getSelectedItem();
                StoreStart=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedStart);
                storeCaffeeSelectedEnd = (String) fullConsultStateCaffeeView.jComboBox2.getSelectedItem();
                StoreEnd=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedEnd);
                listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByStoreandClientandState(state,clients,StoreStart,StoreEnd,dateStart,dateEnd);
                refreshListRemittancesConsult(listRemittancesCaffee);
                //Listo pero no funciona por este error: The association field 'r.slotStoreId' cannot be used as a state field path.
            break;
            case "rangBodegas allClient":
                dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
                dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
                storeCaffeeSelectedStart = (String) fullConsultStateCaffeeView.jComboBox1.getSelectedItem();
                StoreStart=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedStart);
                storeCaffeeSelectedEnd = (String) fullConsultStateCaffeeView.jComboBox2.getSelectedItem();
                StoreEnd=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedEnd);
                listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByStore(StoreStart,StoreEnd,dateStart,dateEnd);
                refreshListRemittancesConsult(listRemittancesCaffee);
                //Listo pero no funciona por este error: The association field 'r.slotStoreId' cannot be used as a state field path.
            break;
            case "rangBodegas espClient":
                dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
                dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
                storeCaffeeSelectedStart = (String) fullConsultStateCaffeeView.jComboBox1.getSelectedItem();
                StoreStart=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedStart);
                storeCaffeeSelectedEnd = (String) fullConsultStateCaffeeView.jComboBox2.getSelectedItem();
                StoreEnd=storesCaffeeJpaController.findStoresCaffeebyName(storeCaffeeSelectedEnd);
                exporterSelected = (String) fullConsultStateCaffeeView.jComboBox3.getSelectedItem();
                clients=clientsJpaController.findClientesByBusinessName(exporterSelected);
                listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByStoreandClient(clients,StoreStart,StoreEnd,dateStart,dateEnd);
                refreshListRemittancesConsult(listRemittancesCaffee);                
                //Listo pero no funciona por este error: The association field 'r.slotStoreId' cannot be used as a state field path.
            break;
            case "allBodegas espClient state":
                dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
                dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
                exporterSelected = (String) fullConsultStateCaffeeView.jComboBox3.getSelectedItem();
                clients=clientsJpaController.findClientesByBusinessName(exporterSelected);
                stateSelected = (String)fullConsultStateCaffeeView.jComboBox4.getSelectedItem(); 
                state=stateOperationJpaController.findStateOperationName(stateSelected);
                listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByClientandState(state,clients,dateStart,dateEnd);
                refreshListRemittancesConsult(listRemittancesCaffee);
                //funciona Perfecto.
            break;
            case "allBodegas espClient":
                dateStart = fullConsultStateCaffeeView.jXDatePicker1.getDate();
                dateEnd = fullConsultStateCaffeeView.jXDatePicker2.getDate();
                exporterSelected = (String) fullConsultStateCaffeeView.jComboBox3.getSelectedItem();
                clients=clientsJpaController.findClientesByBusinessName(exporterSelected);
                listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByExporterandDate(clients,dateStart,dateEnd);
                refreshListRemittancesConsult(listRemittancesCaffee);
                //funciona Perfecto.
            break;
            
            
        }
            
        }
        else{
        JOptionPane.showInternalMessageDialog(fullConsultStateCaffeeView, 
                    "No se puede realizar la consulta !", "Faltan algunos  datos", JOptionPane.ERROR_MESSAGE);
            }
       
    }
    
    private void refreshListRemittancesConsult(List <RemittancesCaffee> listRemittancesCaffee) {
        //List <RemittancesCaffee> listRemittancesCaffee = remittancesCaffeeJpaController.findRemittancesCaffeeByConsultGeneral(dateStart,dateEnd,clientes);
        if(listRemittancesCaffee == null) {
        System.out.println(listRemittancesCaffee);
        JOptionPane.showInternalMessageDialog(fullConsultStateCaffeeView, 
                    "Su consulta no arrojo resultados!", "Resultado de Busqueda", JOptionPane.ERROR_MESSAGE);
        }
        Iterator <RemittancesCaffee> iteratorRemittancesCaffee = listRemittancesCaffee.iterator();
        int sizeList = listRemittancesCaffee.size();
        int indexRow=0;
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy/MM/dd");
        rowDataRemittances = new Object[sizeList][9];
        Object columnNames[] = { "Remesa","Fecha Radicado","Estado Actual","Activo", "Vehiculo", "Lote","En Bodega (Sacos)", "Peso Real (Kg)", "Tara (Kg)"};
        while(iteratorRemittancesCaffee.hasNext()) {
            RemittancesCaffee remittance = (RemittancesCaffee)iteratorRemittancesCaffee.next();
            rowDataRemittances[indexRow][0] = remittance.getId();
            rowDataRemittances[indexRow][1] = dt1.format(remittance.getCreatedDate());
            rowDataRemittances[indexRow][2] = remittance.getStatusOperation().getName();
            rowDataRemittances[indexRow][3] = (remittance.getIsActive() == true) ? "SI" : "NO";
            rowDataRemittances[indexRow][4] = remittance.getVehiclePlate();
            rowDataRemittances[indexRow][5] = remittance.getLotCaffee();
            rowDataRemittances[indexRow][6] = remittance.getQuantityBagInStore();
            rowDataRemittances[indexRow][7] = remittance.getTotalWeightNetReal();
            rowDataRemittances[indexRow][8] = remittance.getTotalTare();
            indexRow++;
        }
        DefaultTableModel defaultTableModel = new DefaultTableModel(rowDataRemittances,columnNames);
        fullConsultStateCaffeeView.jTable1.setModel(defaultTableModel);
        fullConsultStateCaffeeView.jTable1.repaint();
    }
    
    private void initSelectionTableConsultGeneral() {
        ListSelectionModel model=fullConsultStateCaffeeView.jTable1.getSelectionModel();
        model.addListSelectionListener((ListSelectionEvent e) -> {
            if(!model.isSelectionEmpty()){
                int selectedRow=fullConsultStateCaffeeView.jTable1.getSelectedRow();
                String remettancesId = String.valueOf(rowDataRemittances[selectedRow][0]);
                int selectionAnswer = JOptionPane.showInternalConfirmDialog(fullConsultStateCaffeeView, 
                        "¿Desea imprimir de esta remesa "+remettancesId+" su información en detalle?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if(selectionAnswer == 0) {
                    model.clearSelection();
                    new Reports().showFullConsulttRemittancesCaffee(remettancesId, username);
                }
            }
        });
    }
    
    private Clients findCitySourceByNameLocal(String name) {
        Iterator<Clients> iterator = this.listClients.iterator();
        Clients clients = null;
        while(iterator.hasNext()) {
            clients = iterator.next();
            String fullNameClients = clients.getBusinessName();
            if(fullNameClients.matches(name)) {
                return clients;
            }
        }
        return clients;
    }
    
    private StateOperationJpaController stateOperationJpaController;
    private String username;
    private Object [][] rowDataRemittances;
    private RemittancesCaffeeJpaController remittancesCaffeeJpaController;
    private FullConsultStateCaffeeView fullConsultStateCaffeeView;
    private MyComboBoxModel myComboBoxModelExporter;
    private MyComboBoxModel myComboBoxModelStateOperation;
    private MyComboBoxModel myComboBoxModelStoresCaffeeStart;
    private MyComboBoxModel myComboBoxModelStoresCaffeeEnd;
    private ClientsJpaController clientsJpaController;
    private StoresCaffeeJpaController storesCaffeeJpaController;
    private List<Clients> listClients;
    private List<StateOperation> listStateOperation;
    private List<StoresCaffee> listStoresCaffeeStart;
    private List<StoresCaffee> listStoresCaffeeEnd;
}
