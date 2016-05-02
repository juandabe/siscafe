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
import siscafe.model.TypeContainer;
import siscafe.persistence.TypeContainerJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.TypeContainerView;

/**
 *
 * @author Administrador
 */
public class TypeContainerController  implements ActionListener, ListSelectionListener{
    
    public TypeContainerController(TypeContainer typeContainer, TypeContainerView typeContainerView){
        this.typeContainer = typeContainer;
        this.typeContainerView = typeContainerView;
        this.typeContainerJpaController = new TypeContainerJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listTypeContainer = typeContainerJpaController.findTypeContainerEntities();
        GenericListModel genericListModel = new GenericListModel();
        listTypeContainer.stream().forEach((typeContainerFor) -> {
            genericListModel.add(typeContainerFor.getName());
        });
        this.typeContainerView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.typeContainerView.jButton4.addActionListener(this);
        this.typeContainerView.jButton2.addActionListener(this);
        this.typeContainerView.jButton3.addActionListener(this);
        this.typeContainerView.jButton5.addActionListener(this);
        this.typeContainerView.jList3.addListSelectionListener(this);
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
        this.typeContainer.setCreatedDate(dNow);
        this.typeContainer.setUpdatedDate(dNow);
        this.typeContainer.setName(typeContainerView.jTextField3.getText());
        this.typeContainer.setDescription(typeContainerView.jTextArea1.getText());
        typeContainerJpaController.create(typeContainer);
        JOptionPane.showInternalMessageDialog(typeContainerView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.typeContainerSelected.setCreatedDate(dNow);
        this.typeContainerSelected.setUpdatedDate(dNow);
        this.typeContainerSelected.setName(typeContainerView.jTextField3.getText());
        this.typeContainerSelected.setDescription(typeContainerView.jTextArea1.getText());
        try {
            typeContainerJpaController.edit(typeContainerSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TypeContainerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TypeContainerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(typeContainerView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        typeContainerView.jTextField3.setText("");
        typeContainerView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.typeContainerSelected = findTypeContainerByNameLocal(this.typeContainerView.jList3.getSelectedValue());
            this.typeContainerView.jTextField3.setText(typeContainerSelected.getName());
            this.typeContainerView.jTextArea1.setText(typeContainerSelected.getDescription());
        }
    }
    
    private TypeContainer findTypeContainerByNameLocal(String name) {
        Iterator<TypeContainer> iterator = this.listTypeContainer.iterator();
        TypeContainer typeContainer = null;
        while(iterator.hasNext()) {
            typeContainer = iterator.next();
            if(typeContainer.getName().matches(name)) {
                return typeContainer;
            }
        }
        return typeContainer;
    }
    
    private List <TypeContainer> listTypeContainer;
    private TypeContainer typeContainerSelected;
    private TypeContainer typeContainer;
    private final TypeContainerView typeContainerView;
    private final TypeContainerJpaController typeContainerJpaController;
}
