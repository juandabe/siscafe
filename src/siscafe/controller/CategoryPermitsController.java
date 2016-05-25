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
import siscafe.model.CategoryPermits;
import siscafe.persistence.CategoryPermitsJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.CategoryPermitsView;

/**
 *
 * @author Administrador
 */
public class CategoryPermitsController  implements ActionListener, ListSelectionListener{
    
    public CategoryPermitsController(CategoryPermits categoryPermits, CategoryPermitsView categoryPermitsView){
        this.categoryPermits = categoryPermits;
        this.categoryPermitsView = categoryPermitsView;
        categoryPermitsJpaController = new CategoryPermitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        listCategoryPermits = categoryPermitsJpaController.findCategoryPermitsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listCategoryPermits.stream().forEach((categoryPermitsFor) -> {
            genericListModel.add(categoryPermitsFor.getName());
        });
        categoryPermitsView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        categoryPermitsView.jButton4.addActionListener(this);
        categoryPermitsView.jButton2.addActionListener(this);
        categoryPermitsView.jButton3.addActionListener(this);
        categoryPermitsView.jButton5.addActionListener(this);
        categoryPermitsView.jList3.addListSelectionListener(this);
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
        this.categoryPermits.setCreatedDate(dNow);
        this.categoryPermits.setUpdatedDate(dNow);
        this.categoryPermits.setName(categoryPermitsView.jTextField3.getText());
        try {
            categoryPermitsJpaController.create(categoryPermits);
        } catch (Exception ex) {
            Logger.getLogger(CategoryPermitsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(categoryPermitsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.categoryPermitsSelected.setCreatedDate(dNow);
        this.categoryPermitsSelected.setUpdatedDate(dNow);
        try {
            categoryPermitsJpaController.edit(categoryPermitsSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CategoryPermitsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CategoryPermitsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(categoryPermitsView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        categoryPermitsView.jTextField3.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            categoryPermitsSelected = findCategoryPermitsByNameLocal(categoryPermitsView.jList3.getSelectedValue());
            categoryPermitsView.jTextField3.setText(categoryPermitsSelected.getName());
        }
    }
    
    private CategoryPermits findCategoryPermitsByNameLocal(String name) {
        Iterator<CategoryPermits> iterator = this.listCategoryPermits.iterator();
        CategoryPermits categoryPermitsLocal = null;
        while(iterator.hasNext()) {
            categoryPermitsLocal = iterator.next();
            if(categoryPermitsLocal.getName().matches(name)) {
                return categoryPermitsLocal;
            }
        }
        return categoryPermitsLocal;
    }
    
    private List <CategoryPermits> listCategoryPermits;
    private CategoryPermits categoryPermitsSelected;
    private final CategoryPermits categoryPermits;
    private final CategoryPermitsView categoryPermitsView;
    private final CategoryPermitsJpaController categoryPermitsJpaController;
}
