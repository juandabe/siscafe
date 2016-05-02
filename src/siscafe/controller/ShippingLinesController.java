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
import siscafe.model.ShippingLines;
import siscafe.persistence.ShippingLinesJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.ShippingLinesView;

/**
 *
 * @author Administrador
 */
public class ShippingLinesController  implements ActionListener, ListSelectionListener{
    
    public ShippingLinesController(ShippingLines shippingLines, ShippingLinesView shippingLinesView){
        this.shippingLines = shippingLines;
        this.shippingLinesView = shippingLinesView;
        this.shippingLinesJpaController = new ShippingLinesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listShippingLines = shippingLinesJpaController.findShippingLinesEntities();
        GenericListModel genericListModel = new GenericListModel();
        listShippingLines.stream().forEach((shippingLinesFor) -> {
            genericListModel.add(shippingLinesFor.getBusinessName());
        });
        this.shippingLinesView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.shippingLinesView.jButton4.addActionListener(this);
        this.shippingLinesView.jButton2.addActionListener(this);
        this.shippingLinesView.jButton3.addActionListener(this);
        this.shippingLinesView.jButton5.addActionListener(this);
        this.shippingLinesView.jList3.addListSelectionListener(this);
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
        this.shippingLines.setCreatedDate(dNow);
        this.shippingLines.setUpdatedDate(dNow);
        this.shippingLines.setBusinessName(this.shippingLinesView.jTextField3.getText());
        this.shippingLines.setDescription(this.shippingLinesView.jTextArea1.getText());
        this.shippingLinesJpaController.create(shippingLines);
        JOptionPane.showInternalMessageDialog(shippingLinesView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        refresh();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.shippingLinesSelected.setCreatedDate(dNow);
        this.shippingLinesSelected.setUpdatedDate(dNow);
        this.shippingLinesSelected.setBusinessName(null);
        this.shippingLinesSelected.setBusinessName(this.shippingLinesView.jTextField3.getText());
        this.shippingLinesSelected.setDescription(this.shippingLinesView.jTextArea1.getText());
        try {
            shippingLinesJpaController.edit(shippingLinesSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ShippingLinesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ShippingLinesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(shippingLinesView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
        }
    }
    
    private void clear() {
        shippingLinesView.jTextField3.setText("");
        shippingLinesView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.shippingLinesSelected = findshippingLinesByNameLocal(this.shippingLinesView.jList3.getSelectedValue());
            this.shippingLinesView.jTextField3.setText(shippingLinesSelected.getBusinessName());
            this.shippingLinesView.jTextArea1.setText(shippingLinesSelected.getDescription());
        }
    }
    
    private ShippingLines findshippingLinesByNameLocal(String name) {
        Iterator<ShippingLines> iterator = this.listShippingLines.iterator();
        ShippingLines shippingLines = null;
        while(iterator.hasNext()) {
            shippingLines = iterator.next();
            if(shippingLines.getBusinessName().matches(name)) {
                return shippingLines;
            }
        }
        return shippingLines;
    }
    
    private List <ShippingLines> listShippingLines;
    private ShippingLines shippingLines;
    private ShippingLines shippingLinesSelected;
    private final ShippingLinesView shippingLinesView;
    private final ShippingLinesJpaController shippingLinesJpaController;
}
