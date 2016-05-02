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
import siscafe.model.UnitsCaffee;
import siscafe.persistence.TypeUnitsJpaController;
import siscafe.persistence.UnitsCaffeeJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.UnitsCaffeeView;

/**
 *
 * @author Administrador
 */
public class UnitsCaffeeController  implements ActionListener, ListSelectionListener{
    
    public UnitsCaffeeController(UnitsCaffee unitsCaffee, UnitsCaffeeView unitsCaffeeView){
        this.unitsCaffee = unitsCaffee;
        this.unitsCaffeeView = unitsCaffeeView;
        this.unitsCaffeeJpaController = new UnitsCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.typeUnitsJpaController = new TypeUnitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listTypeUnits = typeUnitsJpaController.findTypeUnitsEntities();
        this.listUnitsCaffee = unitsCaffeeJpaController.findUnitsCaffeeEntities();
        this.myComboBoxModelTypeUnits = new MyComboBoxModel(listTypeUnits);
        GenericListModel genericListModel = new GenericListModel();
        listUnitsCaffee.stream().forEach((unitsCaffeeFor) -> {
            genericListModel.add(unitsCaffeeFor.getNameUnit());
        });
        this.unitsCaffeeView.jList3.setModel(genericListModel);
        this.unitsCaffeeView.jComboBox1.setModel(myComboBoxModelTypeUnits);
    }
    
    public void initListener() {
        this.unitsCaffeeView.jButton4.addActionListener(this);
        this.unitsCaffeeView.jButton2.addActionListener(this);
        this.unitsCaffeeView.jButton3.addActionListener(this);
        this.unitsCaffeeView.jButton5.addActionListener(this);
        this.unitsCaffeeView.jList3.addListSelectionListener(this);
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
        this.unitsCaffee.setCreatedDate(dNow);
        this.unitsCaffee.setUpdatedDate(dNow);
        this.unitsCaffee.setNameUnit(unitsCaffeeView.jTextField5.getText());
        String nameTypeUnit = (String) unitsCaffeeView.jComboBox1.getSelectedItem();
        this.unitsCaffee.setTypeUnitsId(findTypeUnitsByNameLocal(nameTypeUnit));
        Double quantity = Double.parseDouble(unitsCaffeeView.jTextField4.getText());
        this.unitsCaffee.setQuantity(quantity);
        unitsCaffeeJpaController.create(unitsCaffee);
        JOptionPane.showInternalMessageDialog(unitsCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        refresh();
    }
    
    private void edit() {
        Date dNow = new Date( );
        unitsCaffeeSelected.setUpdatedDate(dNow);
        unitsCaffeeSelected.setNameUnit(unitsCaffeeView.jTextField5.getText());
        String nameTypeUnit = (String) unitsCaffeeView.jComboBox1.getSelectedItem();
        unitsCaffeeSelected.setTypeUnitsId(findTypeUnitsByNameLocal(nameTypeUnit));
        Double quantity = Double.parseDouble(unitsCaffeeView.jTextField4.getText());
        unitsCaffeeSelected.setQuantity(quantity);
        try {
            unitsCaffeeJpaController.edit(unitsCaffeeSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(UnitsCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(UnitsCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(unitsCaffeeView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        }
    }
    
    private void clear() {
        unitsCaffeeView.jTextField5.setText("");
        unitsCaffeeView.jTextField4.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.unitsCaffeeSelected = findUnitsCaffeeByNameLocal(this.unitsCaffeeView.jList3.getSelectedValue());
            this.unitsCaffeeView.jTextField5.setText(unitsCaffeeSelected.getNameUnit());
            this.unitsCaffeeView.jComboBox1.getModel().setSelectedItem(this.unitsCaffeeSelected.getTypeUnitsId().getName());
             this.unitsCaffeeView.jComboBox1.repaint();
            this.unitsCaffeeView.jTextField4.setText(String.valueOf(unitsCaffeeSelected.getQuantity()));
        }
    }
    
    private UnitsCaffee findUnitsCaffeeByNameLocal(String name) {
        Iterator<UnitsCaffee> iterator = this.listUnitsCaffee.iterator();
        UnitsCaffee unitsCaffee = null;
        while(iterator.hasNext()) {
            unitsCaffee = iterator.next();
            if(unitsCaffee.getNameUnit().matches(name)) {
                return unitsCaffee;
            }
        }
        return unitsCaffee;
    }
    
    private TypeUnits findTypeUnitsByNameLocal(String name) {
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
    
    private List <UnitsCaffee> listUnitsCaffee;
    private List <TypeUnits> listTypeUnits;
    private UnitsCaffee unitsCaffee;
    private UnitsCaffee unitsCaffeeSelected;
    private final UnitsCaffeeView unitsCaffeeView;
    private final UnitsCaffeeJpaController unitsCaffeeJpaController;
    private TypeUnitsJpaController typeUnitsJpaController;
    private MyComboBoxModel myComboBoxModelTypeUnits;
}
