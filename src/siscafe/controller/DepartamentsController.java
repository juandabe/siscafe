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
import siscafe.model.Departaments;
import siscafe.persistence.DepartamentsJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.DepartamentsView;

/**
 *
 * @author Administrador
 */
public class DepartamentsController  implements ActionListener, ListSelectionListener{
    
    public DepartamentsController(Departaments departaments, DepartamentsView departamentsView){
        this.departamentsSelected = departaments;
        this.departamentsView = departamentsView;
        this.departamentsJpaController = new DepartamentsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listDepartaments = departamentsJpaController.findDepartamentsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listDepartaments.stream().forEach((permitsFor) -> {
            genericListModel.add(permitsFor.getName());
        });
        this.departamentsView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.departamentsView.jButton4.addActionListener(this);
        this.departamentsView.jButton2.addActionListener(this);
        this.departamentsView.jButton3.addActionListener(this);
        this.departamentsView.jButton5.addActionListener(this);
        this.departamentsView.jList3.addListSelectionListener(this);
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
        this.departamentsSelected.setCreatedDate(dNow);
        this.departamentsSelected.setUpdatedDate(dNow);
        this.departamentsSelected.setName(departamentsView.jTextField3.getText());
        this.departamentsSelected.setDescription(departamentsView.jTextArea1.getText());
        departamentsJpaController.create(departamentsSelected);
        JOptionPane.showInternalMessageDialog(departamentsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.departamentsSelected.setCreatedDate(dNow);
        this.departamentsSelected.setUpdatedDate(dNow);
        this.departamentsSelected.setName(departamentsView.jTextField3.getText());
        this.departamentsSelected.setDescription(departamentsView.jTextArea1.getText());
        try {
            departamentsJpaController.edit(departamentsSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DepartamentsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DepartamentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(departamentsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        departamentsView.jTextField3.setText("");
        departamentsView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.departamentsSelected = findDepartamentsByNameLocal(this.departamentsView.jList3.getSelectedValue());
            this.departamentsView.jTextField3.setText(departamentsSelected.getName());
            this.departamentsView.jTextArea1.setText(departamentsSelected.getDescription());
        }
    }
    
    private Departaments findDepartamentsByNameLocal(String name) {
        Iterator<Departaments> iterator = this.listDepartaments.iterator();
        Departaments departaments = null;
        while(iterator.hasNext()) {
            departaments = iterator.next();
            if(departaments.getName().matches(name)) {
                return departaments;
            }
        }
        return departaments;
    }
    
    private List <Departaments> listDepartaments;
    private Departaments departamentsSelected;
    private final DepartamentsView departamentsView;
    private final DepartamentsJpaController departamentsJpaController;
}
