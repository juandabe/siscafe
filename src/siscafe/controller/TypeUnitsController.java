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
import siscafe.model.TypeUnits;
import siscafe.persistence.TypeUnitsJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.TypeUnitsView;

/**
 *
 * @author Administrador
 */
public class TypeUnitsController  implements ActionListener, ListSelectionListener{
    
    public TypeUnitsController(TypeUnits typeUnits, TypeUnitsView typeUnitsView){
        this.typeUnits = typeUnits;
        this.typeUnitsView = typeUnitsView;
        this.typeUnitsJpaController = new TypeUnitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listTypeUnits = typeUnitsJpaController.findTypeUnitsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listTypeUnits.stream().forEach((permitsFor) -> {
            genericListModel.add(permitsFor.getName());
        });
        this.typeUnitsView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.typeUnitsView.jButton4.addActionListener(this);
        this.typeUnitsView.jButton2.addActionListener(this);
        this.typeUnitsView.jButton3.addActionListener(this);
        this.typeUnitsView.jButton5.addActionListener(this);
        this.typeUnitsView.jList3.addListSelectionListener(this);
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
        this.typeUnits.setCreatedDate(dNow);
        this.typeUnits.setUpdatedDate(dNow);
        this.typeUnits.setName(typeUnitsView.jTextField3.getText());
        String nameProduct = (String) typeUnitsView.jComboBox1.getSelectedItem();
        this.typeUnits.setDescription(this.typeUnitsView.jTextArea1.getText());
        this.typeUnits.setProductName(nameProduct);
        typeUnitsJpaController.create(typeUnits);
        JOptionPane.showInternalMessageDialog(typeUnitsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        refresh();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.typeUnitsSelected.setCreatedDate(dNow);
        this.typeUnitsSelected.setUpdatedDate(dNow);
        this.typeUnitsSelected.setName(typeUnitsView.jTextField3.getText());
        String nameProduct = (String) typeUnitsView.jComboBox1.getSelectedItem();
        this.typeUnitsSelected.setDescription(this.typeUnitsView.jTextArea1.getText());
        this.typeUnitsSelected.setProductName(nameProduct);
        try {
            typeUnitsJpaController.edit(typeUnitsSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(TypeUnitsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(TypeUnitsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(typeUnitsView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        typeUnitsView.jTextField3.setText("");
        typeUnitsView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.typeUnitsSelected = findDepartamentsByNameLocal(this.typeUnitsView.jList3.getSelectedValue());
            this.typeUnitsView.jTextField3.setText(typeUnitsSelected.getName());
            this.typeUnitsView.jTextArea1.setText(typeUnitsSelected.getDescription());
            this.typeUnitsView.jComboBox1.getModel().setSelectedItem(this.typeUnitsSelected.getProductName());
        }
    }
    
    private TypeUnits findDepartamentsByNameLocal(String name) {
        Iterator<TypeUnits> iterator = this.listTypeUnits.iterator();
        TypeUnits typeUnits = null;
        while(iterator.hasNext()) {
            typeUnits = iterator.next();
            if(typeUnits.getName().matches(name)) {
                return typeUnits;
            }
        }
        return typeUnits;
    }
    
    private List <TypeUnits> listTypeUnits;
    private TypeUnits typeUnits;
    private TypeUnits typeUnitsSelected;
    private final TypeUnitsView typeUnitsView;
    private final TypeUnitsJpaController typeUnitsJpaController;
}
