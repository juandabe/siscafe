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
import siscafe.model.NoveltysCaffee;
import siscafe.persistence.NoveltysCaffeeJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.NoveltysCaffeeView;

/**
 *
 * @author Administrador
 */
public class NoveltysCaffeeController  implements ActionListener, ListSelectionListener{
    
    public NoveltysCaffeeController(NoveltysCaffee noveltysCaffee, NoveltysCaffeeView noveltysCaffeeView){
        this.noveltysCaffee = noveltysCaffee;
        this.noveltysCaffeeView = noveltysCaffeeView;
        this.noveltysCaffeeJpaController = new NoveltysCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    private void refresh() {
        this.listNoveltysCaffee = noveltysCaffeeJpaController.findNoveltysCaffeeEntities();
        GenericListModel genericListModel = new GenericListModel();
        listNoveltysCaffee.stream().forEach((noveltysCaffeeFor) -> {
            genericListModel.add(noveltysCaffeeFor.getName());
        });
        this.noveltysCaffeeView.jList3.setModel(genericListModel);
    }
    
    public void initListener() {
        this.noveltysCaffeeView.jButton4.addActionListener(this);
        this.noveltysCaffeeView.jButton2.addActionListener(this);
        this.noveltysCaffeeView.jButton3.addActionListener(this);
        this.noveltysCaffeeView.jButton5.addActionListener(this);
        this.noveltysCaffeeView.jList3.addListSelectionListener(this);
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
        this.noveltysCaffee.setCreatedDate(dNow);
        this.noveltysCaffee.setUpdatedDate(dNow);
        this.noveltysCaffee.setName(noveltysCaffeeView.jTextField3.getText());
        this.noveltysCaffee.setDescription(noveltysCaffeeView.jTextArea1.getText());
        noveltysCaffeeJpaController.create(noveltysCaffee);
        JOptionPane.showInternalMessageDialog(noveltysCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        clear();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.noveltysCaffeeSelected.setCreatedDate(dNow);
        this.noveltysCaffeeSelected.setUpdatedDate(dNow);
        this.noveltysCaffeeSelected.setName(noveltysCaffeeView.jTextField3.getText());
        this.noveltysCaffeeSelected.setDescription(noveltysCaffeeView.jTextArea1.getText());
        try {
            noveltysCaffeeJpaController.edit(noveltysCaffeeSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(NoveltysCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(NoveltysCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(noveltysCaffeeView, "Registro editado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
        }
    }
    
    private void clear() {
        noveltysCaffeeView.jTextField3.setText("");
        noveltysCaffeeView.jTextArea1.setText("");
        refresh();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.noveltysCaffeeSelected = findNoveltysCaffeeByNameLocal(this.noveltysCaffeeView.jList3.getSelectedValue());
            this.noveltysCaffeeView.jTextField3.setText(noveltysCaffeeSelected.getName());
            this.noveltysCaffeeView.jTextArea1.setText(noveltysCaffeeSelected.getDescription());
        }
    }
    
    private NoveltysCaffee findNoveltysCaffeeByNameLocal(String name) {
        Iterator<NoveltysCaffee> iterator = this.listNoveltysCaffee.iterator();
        NoveltysCaffee noveltysCaffee = null;
        while(iterator.hasNext()) {
            noveltysCaffee = iterator.next();
            if(noveltysCaffee.getName().matches(name)) {
                return noveltysCaffee;
            }
        }
        return noveltysCaffee;
    }
    
    private List <NoveltysCaffee> listNoveltysCaffee;
    private NoveltysCaffee noveltysCaffeeSelected;
    private final NoveltysCaffee noveltysCaffee;
    private final NoveltysCaffeeView noveltysCaffeeView;
    private final NoveltysCaffeeJpaController noveltysCaffeeJpaController;
}
