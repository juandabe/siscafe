
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
import siscafe.model.Permits;
import siscafe.persistence.CategoryPermitsJpaController;
import siscafe.persistence.PermitsJpaController;
import siscafe.util.GenericListModel;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.PermitsView;

/**
 *
 * @author jecheverri
 */
public class PermitsController implements ActionListener, ListSelectionListener{
    
    public PermitsController(Permits permits,PermitsView permitsView) {
        this.permitsModel = permits;
        this.permitsView = permitsView;
        this.permitsJpaController = new PermitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.categoryPermitsJpaController = new CategoryPermitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.listCategoryPermits = this.categoryPermitsJpaController.findCategoryPermitsEntities();
        this.myComboBoxModel = new MyComboBoxModel(listCategoryPermits);
        refresh();
    }
    
    public void initListener() {
        this.permitsView.jButton4.addActionListener(this);
        this.permitsView.jButton2.addActionListener(this);
        this.permitsView.jButton3.addActionListener(this);
        this.permitsView.jButton5.addActionListener(this);
        this.permitsView.jList3.addListSelectionListener(this);
        this.permitsView.jComboBox1.setModel(myComboBoxModel);
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
                clear();
            break;
        }
    }
    
    public void add() {
        Date dNow = new Date( );
        permitsModel.setName(permitsView.jTextField3.getText());
        permitsModel.setDescription(permitsView.jTextArea1.getText());
        permitsModel.setCreatedDate(dNow);
        permitsModel.setUpdatedDate(dNow);
        String categoria = (String) this.permitsView.jComboBox1.getSelectedItem();
        categoryPermitsSelected=findCategoryPermitsByNameLocal(categoria);
        permitsModel.setCategoryPermitsId(categoryPermitsSelected);
        permitsJpaController.create(permitsModel);
        permitsView.jTextField3.setText("");
        permitsView.jTextArea1.setText("");
        JOptionPane.showInternalMessageDialog(permitsView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    private void edit() {
        Date dNow = new Date( );
        this.permitSelected.setName(permitsView.jTextField3.getText());
        this.permitSelected.setDescription(permitsView.jTextArea1.getText());
        this.permitSelected.setUpdatedDate(dNow);
        String categoria = (String) this.permitsView.jComboBox1.getSelectedItem();
        categoryPermitsSelected=findCategoryPermitsByNameLocal(categoria);
        permitSelected.setCategoryPermitsId(categoryPermitsSelected);
        try {
            permitsJpaController.edit(permitSelected);
            JOptionPane.showInternalMessageDialog(permitsView, "Registro actualizado", 
                    "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            Logger.getLogger(PermitsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.permitSelected = findPermitByNameLocal(this.permitsView.jList3.getSelectedValue());
            this.permitsView.jComboBox1.getModel().setSelectedItem(this.permitSelected.getCategoryPermitsId().getName());
            this.permitsView.jComboBox1.repaint();
            this.permitsView.jTextField3.setText(permitSelected.getName());
            this.permitsView.jTextArea1.setText(permitSelected.getDescription());
        }
    }
    
    private void refresh() {
        this.listPermits = permitsJpaController.findPermitsEntities();
        GenericListModel genericListModel = new GenericListModel();
        listPermits.stream().forEach((permitsFor) -> {
            genericListModel.add(permitsFor.getName());
        });
        this.permitsView.jList3.setModel(genericListModel);
    }

    private void clear() {
        permitsView.jTextField3.setText("");
        permitsView.jTextArea1.setText("");
        refresh();
    }
    
    private Permits findPermitByNameLocal(String name) {
        Iterator<Permits> iterator = this.listPermits.iterator();
        Permits permit = null;
        while(iterator.hasNext()) {
            permit = iterator.next();
            if(permit.getName().matches(name)) {
                return permit;
            }
        }
        return permit;
    }
    
    private CategoryPermits findCategoryPermitsByNameLocal(String name) {
        Iterator<CategoryPermits> iterator = this.listCategoryPermits.iterator();
        CategoryPermits categoryPermits = null;
        while(iterator.hasNext()) {
            categoryPermits = iterator.next();
            if(categoryPermits.getName().matches(name)) {
                return categoryPermits;
            }
        }
        return categoryPermits;
    }
    
    private final Permits permitsModel;
    private final PermitsView permitsView;
    private final PermitsJpaController permitsJpaController;
    private final CategoryPermitsJpaController categoryPermitsJpaController;
    private List<Permits> listPermits;
    private List<CategoryPermits> listCategoryPermits;
    private Permits permitSelected;
    private CategoryPermits categoryPermitsSelected;
    private MyComboBoxModel myComboBoxModel;
}