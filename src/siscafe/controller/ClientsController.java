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
import siscafe.persistence.ClientsJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.ClientsView;

/**
 *
 * @author Administrador
 */
public class ClientsController  implements ActionListener, ListSelectionListener{

    public ClientsController (Clients clients, ClientsView clientView) {
        this.clients = clients;
        this.clientView = clientView;
        this.clientsJpaController = new ClientsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    public void initListener() {
        this.clientView.jButton4.addActionListener(this);
        this.clientView.jButton2.addActionListener(this);
        this.clientView.jButton3.addActionListener(this);
        this.clientView.jButton5.addActionListener(this);
        this.clientView.jList3.addListSelectionListener(this);
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
    
    private void refresh() {
        this.listClients = clientsJpaController.findClientsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listClients.stream().forEach((clientsFor) -> {
            genericListModel.add(clientsFor.getBusinessName());
        });
        this.clientView.jList3.setModel(genericListModel);
    }
    
    public void add() {
        Date dNow = new Date( );
        this.clients.setCreatedDate(dNow);
        this.clients.setUpdatedDate(dNow);
        this.clients.setBusinessName(this.clientView.jTextField3.getText());
        this.clients.setEmails(this.clientView.jTextField4.getText());
        this.clients.setPhone(this.clientView.jTextField7.getText());
        this.clients.setExporterCode(this.clientView.jTextField5.getText());
        this.clients.setCityLocation(this.clientView.jComboBox1.getSelectedItem().toString());
        this.clientsJpaController.create(clients);
        JOptionPane.showInternalMessageDialog(clientView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        refresh();
    }
    
    public void edit() {
        Date dNow = new Date( );
        this.clientsSelected.setUpdatedDate(dNow);
        this.clientsSelected.setBusinessName(clientsSelected.getBusinessName());
        this.clientsSelected.setEmails(this.clientView.jTextField4.getText());
        this.clientsSelected.setPhone(this.clientView.jTextField7.getText());
        this.clientsSelected.setExporterCode(this.clientView.jTextField5.getText());
        this.clientsSelected.setCityLocation(this.clientView.jComboBox1.getSelectedItem().toString());
        try {
            this.clientsJpaController.edit(clientsSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(clientView, "Registro actualizado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    public void clear() {
        this.clientView.jTextField3.setText("");
        this.clientView.jTextField4.setText("");
        this.clientView.jTextField7.setText("");
        this.clientView.jTextField5.setText("");
        this.clientView.jComboBox1.setSelectedItem("");
        this.clientView.jTextField6.setText("");
        this.clientView.jTextField8.setText("");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.clientsSelected = findclientsByNameLocal(this.clientView.jList3.getSelectedValue());
            this.clientView.jTextField3.setText(clientsSelected.getBusinessName());
            this.clientView.jTextField4.setText(clientsSelected.getEmails());
            this.clientView.jTextField7.setText(clientsSelected.getPhone());
            this.clientView.jTextField5.setText(String.valueOf(clientsSelected.getExporterCode()));
            this.clientView.jComboBox1.setSelectedItem(clientsSelected.getCityLocation());
            this.clientView.jTextField6.setText(clientsSelected.getCreatedDate().toString());
            this.clientView.jTextField8.setText(clientsSelected.getUpdatedDate().toString());
        }
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
    
    private List<Clients> listClients;
    private final Clients clients;
    private Clients clientsSelected;
    private final ClientsView clientView;
    private ClientsJpaController clientsJpaController;
}
