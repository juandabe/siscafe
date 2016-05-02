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
import siscafe.model.MarkCaffee;
import siscafe.persistence.MarkCaffeeJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.MarkCaffeeView;

/**
 *
 * @author Administrador
 */
public class MarkCaffeeController  implements ActionListener, ListSelectionListener{
    
    public MarkCaffeeController(MarkCaffee markCaffee, MarkCaffeeView markCaffeeView){
        this.markCaffee = markCaffee;
        this.markCaffeeView = markCaffeeView;
        this.markCaffeeJpaController = new MarkCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listMarkCaffee = markCaffeeJpaController.findMarkCaffeeEntities();
        GenericListModel genericListModel = new GenericListModel();
        listMarkCaffee.stream().forEach((markCaffeeFor) -> {
            genericListModel.add(markCaffeeFor.getName());
        });
        this.markCaffeeView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.markCaffeeView.jButton4.addActionListener(this);
        this.markCaffeeView.jButton2.addActionListener(this);
        this.markCaffeeView.jButton3.addActionListener(this);
        this.markCaffeeView.jButton5.addActionListener(this);
        this.markCaffeeView.jList3.addListSelectionListener(this);
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
        this.markCaffee.setCreatedDate(dNow);
        this.markCaffee.setUpdatedDate(dNow);
        this.markCaffee.setName(markCaffeeView.jTextField3.getText());
        this.markCaffee.setDescription(markCaffeeView.jTextArea1.getText());
        markCaffeeJpaController.create(markCaffee);
        JOptionPane.showInternalMessageDialog(markCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.markCaffeeSelected.setCreatedDate(dNow);
        this.markCaffeeSelected.setUpdatedDate(dNow);
        this.markCaffeeSelected.setName(markCaffeeView.jTextField3.getText());
        this.markCaffeeSelected.setDescription(markCaffeeView.jTextArea1.getText());
        try {
            markCaffeeJpaController.edit(markCaffeeSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MarkCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MarkCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(markCaffeeView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        markCaffeeView.jTextField3.setText("");
        markCaffeeView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.markCaffeeSelected = findmarkCaffeeByNameLocal(this.markCaffeeView.jList3.getSelectedValue());
            this.markCaffeeView.jTextField3.setText(markCaffeeSelected.getName());
            this.markCaffeeView.jTextArea1.setText(markCaffeeSelected.getDescription());
        }
    }
    
    private MarkCaffee findmarkCaffeeByNameLocal(String name) {
        Iterator<MarkCaffee> iterator = this.listMarkCaffee.iterator();
        MarkCaffee markCaffee = null;
        while(iterator.hasNext()) {
            markCaffee = iterator.next();
            if(markCaffee.getName().matches(name)) {
                return markCaffee;
            }
        }
        return markCaffee;
    }
    
    private List <MarkCaffee> listMarkCaffee;
    private MarkCaffee markCaffee;
    private MarkCaffee markCaffeeSelected;
    private final MarkCaffeeView markCaffeeView;
    private final MarkCaffeeJpaController markCaffeeJpaController;
}
