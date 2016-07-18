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
import siscafe.model.NavyAgent;
import siscafe.persistence.NavyAgentJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.NavyAgentView;

/**
 *
 * @author Administrador
 */
public class NavyAgentController  implements ActionListener, ListSelectionListener{
    
    public NavyAgentController(NavyAgent navyAgent, NavyAgentView navyAgentView){
        this.navyAgent = navyAgent;
        this.navyAgentView = navyAgentView;
        navyAgentJpaController = new NavyAgentJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listNavyAgent = navyAgentJpaController.findNavyAgentEntities();
        GenericListModel genericListModel = new GenericListModel();
        listNavyAgent.stream().forEach((navyAgentFor) -> {
            genericListModel.add(navyAgentFor.getName());
        });
        this.navyAgentView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        navyAgentView.jButton4.addActionListener(this);
        navyAgentView.jButton2.addActionListener(this);
        navyAgentView.jButton3.addActionListener(this);
        navyAgentView.jButton5.addActionListener(this);
        navyAgentView.jList3.addListSelectionListener(this);
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
        this.navyAgent.setCreatedDate(dNow);
        this.navyAgent.setUpdatedDate(dNow);
        this.navyAgent.setName(navyAgentView.jTextField3.getText());
        try {
            navyAgentJpaController.create(navyAgent);
        } catch (Exception ex) {
            Logger.getLogger(CitySourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(navyAgentView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        navyAgentSelected.setCreatedDate(dNow);
        navyAgentSelected.setUpdatedDate(dNow);
        navyAgentSelected.setName(navyAgentView.jTextField3.getText());
        try {
            navyAgentJpaController.edit(navyAgentSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CitySourceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CitySourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(navyAgentView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        navyAgentView.jTextField3.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.navyAgentSelected = findNavyAgentByNameLocal(this.navyAgentView.jList3.getSelectedValue());
            this.navyAgentView.jTextField3.setText(navyAgentSelected.getName());
        }
    }
    
    private NavyAgent findNavyAgentByNameLocal(String name) {
        Iterator<NavyAgent> iterator = this.listNavyAgent.iterator();
        NavyAgent navyAgentLocal = null;
        while(iterator.hasNext()) {
            navyAgentLocal = iterator.next();
            if(navyAgentLocal.getName().matches(name)) {
                return navyAgentLocal;
            }
        }
        return navyAgentLocal;
    }
    
    private List <NavyAgent> listNavyAgent;
    private NavyAgent navyAgentSelected;
    private NavyAgent navyAgent;
    private NavyAgentView navyAgentView;
    private NavyAgentJpaController navyAgentJpaController;
}
