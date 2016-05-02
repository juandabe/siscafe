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
import siscafe.model.PortOperators;
import siscafe.persistence.PortOperatorsJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.PortOperatorsView;

/**
 *
 * @author Administrador
 */
public class PortOperatorsController  implements ActionListener, ListSelectionListener{
    
    public PortOperatorsController(PortOperators portOperators, PortOperatorsView portOperatorsView){
        this.portOperators = portOperators;
        this.portOperatorsView = portOperatorsView;
        this.portOperatorsJpaController = new PortOperatorsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listPortOperators = portOperatorsJpaController.findPortOperatorsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listPortOperators.stream().forEach((portOperatorsFor) -> {
            genericListModel.add(portOperatorsFor.getName());
        });
        this.portOperatorsView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.portOperatorsView.jButton4.addActionListener(this);
        this.portOperatorsView.jButton2.addActionListener(this);
        this.portOperatorsView.jButton3.addActionListener(this);
        this.portOperatorsView.jButton5.addActionListener(this);
        this.portOperatorsView.jList3.addListSelectionListener(this);
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
    
    private void add() {
        Date dNow = new Date( );
        this.portOperators.setCreatedDate(dNow);
        this.portOperators.setUpdatedDate(dNow);
        this.portOperators.setName(portOperatorsView.jTextField3.getText());
        this.portOperators.setDescription(portOperatorsView.jTextArea1.getText());
        portOperatorsJpaController.create(portOperators);
        JOptionPane.showInternalMessageDialog(portOperatorsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.portOperatorsSelected.setCreatedDate(dNow);
        this.portOperatorsSelected.setUpdatedDate(dNow);
        this.portOperatorsSelected.setName(portOperatorsView.jTextField3.getText());
        this.portOperatorsSelected.setDescription(portOperatorsView.jTextArea1.getText());
        try {
            portOperatorsJpaController.edit(portOperatorsSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PortOperatorsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PortOperatorsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(portOperatorsView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        portOperatorsView.jTextField3.setText("");
        portOperatorsView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.portOperatorsSelected = findPortOperatorsByNameLocal(this.portOperatorsView.jList3.getSelectedValue());
            this.portOperatorsView.jTextField3.setText(portOperatorsSelected.getName());
            this.portOperatorsView.jTextArea1.setText(portOperatorsSelected.getDescription());
        }
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
    
    private List <PortOperators> listPortOperators;
    private PortOperators portOperators;
    private PortOperators portOperatorsSelected;
    private final PortOperatorsView portOperatorsView;
    private final PortOperatorsJpaController portOperatorsJpaController;
}
