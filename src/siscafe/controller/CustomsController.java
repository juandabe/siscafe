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
import siscafe.model.Customs;
import siscafe.persistence.CustomsJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.CustomsView;

/**
 *
 * @author Administrador
 */
public class CustomsController  implements ActionListener, ListSelectionListener{
    
    public CustomsController(Customs customs, CustomsView customsView){
        this.customs = customs;
        this.customsView = customsView;
        this.customsJpaController = new CustomsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listCustoms = customsJpaController.findCustomsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listCustoms.stream().forEach((customsFor) -> {
            genericListModel.add(customsFor.getCiaName());
        });
        this.customsView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.customsView.jButton4.addActionListener(this);
        this.customsView.jButton2.addActionListener(this);
        this.customsView.jButton3.addActionListener(this);
        this.customsView.jButton5.addActionListener(this);
        this.customsView.jList3.addListSelectionListener(this);
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
        this.customs.setCreatedDate(dNow);
        this.customs.setUpdatedDate(dNow);
        this.customs.setCiaName(customsView.jTextField3.getText());
        this.customs.setDescription(customsView.jTextArea1.getText());
        customsJpaController.create(customs);
        JOptionPane.showInternalMessageDialog(customsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.customsSelected.setCreatedDate(dNow);
        this.customsSelected.setUpdatedDate(dNow);
        this.customsSelected.setCiaName(customsView.jTextField3.getText());
        this.customsSelected.setDescription(customsView.jTextArea1.getText());
        try {
            customsJpaController.edit(customsSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CustomsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CustomsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(customsView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        customsView.jTextField3.setText("");
        customsView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.customsSelected = findCustomsByNameLocal(this.customsView.jList3.getSelectedValue());
            this.customsView.jTextField3.setText(customsSelected.getCiaName());
            this.customsView.jTextArea1.setText(customsSelected.getDescription());
        }
    }
    
    private Customs findCustomsByNameLocal(String name) {
        Iterator<Customs> iterator = this.listCustoms.iterator();
        Customs customs = null;
        while(iterator.hasNext()) {
            customs = iterator.next();
            if(customs.getCiaName().matches(name)) {
                return customs;
            }
        }
        return customs;
    }
    
    private List <Customs> listCustoms;
    private Customs customsSelected;
    private Customs customs;
    private final CustomsView customsView;
    private final CustomsJpaController customsJpaController;
}
