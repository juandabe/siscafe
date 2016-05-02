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
import siscafe.model.CitySource;
import siscafe.persistence.CitySourceJpaController;
import siscafe.persistence.TypeContainerJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.CitySourceView;

/**
 *
 * @author Administrador
 */
public class CitySourceController  implements ActionListener, ListSelectionListener{
    
    public CitySourceController(CitySource citySource, CitySourceView citySourceView){
        this.citySource = citySource;
        this.citySourceView = citySourceView;
        this.citySourceJpaController = new CitySourceJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listCitySource = citySourceJpaController.findCitySourceEntities();
        GenericListModel genericListModel = new GenericListModel();
        listCitySource.stream().forEach((citySourceFor) -> {
            genericListModel.add(citySourceFor.getCityName());
        });
        this.citySourceView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.citySourceView.jButton4.addActionListener(this);
        this.citySourceView.jButton2.addActionListener(this);
        this.citySourceView.jButton3.addActionListener(this);
        this.citySourceView.jButton5.addActionListener(this);
        this.citySourceView.jList3.addListSelectionListener(this);
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
        this.citySource.setCreatedDate(dNow);
        this.citySource.setUpdatedDate(dNow);
        this.citySource.setCityName(citySourceView.jTextField3.getText());
        this.citySource.setCountryName(citySourceView.jTextField4.getText());
        try {
            citySourceJpaController.create(citySource);
        } catch (Exception ex) {
            Logger.getLogger(CitySourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        JOptionPane.showInternalMessageDialog(citySourceView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.citySourceSelected.setCreatedDate(dNow);
        this.citySourceSelected.setUpdatedDate(dNow);
        this.citySourceSelected.setCityName(citySourceView.jTextField3.getText());
        this.citySourceSelected.setCountryName(citySourceView.jTextField4.getText());
        try {
            citySourceJpaController.edit(citySourceSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CitySourceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CitySourceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(citySourceView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        citySourceView.jTextField3.setText("");
        citySourceView.jTextField4.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.citySourceSelected = findTypeContainerByNameLocal(this.citySourceView.jList3.getSelectedValue());
            this.citySourceView.jTextField3.setText(citySourceSelected.getCityName());
            this.citySourceView.jTextField4.setText(citySourceSelected.getCountryName());
        }
    }
    
    private CitySource findTypeContainerByNameLocal(String name) {
        Iterator<CitySource> iterator = this.listCitySource.iterator();
        CitySource citySource = null;
        while(iterator.hasNext()) {
            citySource = iterator.next();
            if(citySource.getCityName().matches(name)) {
                return citySource;
            }
        }
        return citySource;
    }
    
    private List <CitySource> listCitySource;
    private CitySource citySourceSelected;
    private final CitySource citySource;
    private final CitySourceView citySourceView;
    private final CitySourceJpaController citySourceJpaController;
}
