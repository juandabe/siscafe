
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
import siscafe.model.PackingCaffee;
import siscafe.persistence.PackingCaffeeJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.PackingCaffeeView;

/**
 *
 * @author jecheverri
 */
public class PackingCaffeeController implements ActionListener, ListSelectionListener{
    
    /**
    *
    * @author jecheverri
    *  @param packingCaffee
     * @param packingCaffeeView
    */
    public PackingCaffeeController(PackingCaffee packingCaffee, PackingCaffeeView packingCaffeeView) {
        this.packingCaffee = packingCaffee;
        this.packingCaffeeView = packingCaffeeView;
        packingCaffeeJpaController = new PackingCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    public void initListener() {
        packingCaffeeView.jButton4.addActionListener(this);
        packingCaffeeView.jButton2.addActionListener(this);
        packingCaffeeView.jButton3.addActionListener(this);
        packingCaffeeView.jButton5.addActionListener(this);
        packingCaffeeView.jList3.addListSelectionListener(this);
        refresh();
    }
    
    private void refresh() {
        listPackingCaffee = packingCaffeeJpaController.findPackingCaffeeEntities();
        GenericListModel genericListModel = new GenericListModel();
        listPackingCaffee.stream().forEach((packingCaffeeFor) -> {
            genericListModel.add(packingCaffeeFor.getName());
        });
        this.packingCaffeeView.jList3.setModel(genericListModel);;
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
    
    private void add() {
        Date dNow = new Date( );
        packingCaffee.setDateCreated(dNow);
        packingCaffee.setDateUpdated(dNow);
        packingCaffee.setName(this.packingCaffeeView.jTextField5.getText());
        packingCaffee.setDescription(this.packingCaffeeView.jTextArea1.getText());
        packingCaffee.setWeight(Double.parseDouble(packingCaffeeView.jTextField4.getText()));
        packingCaffeeJpaController.create(packingCaffee);
        JOptionPane.showInternalMessageDialog(packingCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    private void edit() {
        Date dNow = new Date( );
        packingCaffeeSelected.setDateUpdated(dNow);
        packingCaffeeSelected.setName(this.packingCaffeeView.jTextField5.getText());
        packingCaffeeSelected.setDescription(this.packingCaffeeView.jTextArea1.getText());
        packingCaffeeSelected.setWeight(Double.parseDouble(packingCaffeeView.jTextField4.getText()));
        try {
            packingCaffeeJpaController.edit(packingCaffeeSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PackingCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PackingCaffeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            JOptionPane.showInternalMessageDialog(packingCaffeeView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void clear() {
        packingCaffeeView.jTextField5.setText("");
        packingCaffeeView.jTextField4.setText("");
        packingCaffeeView.jTextArea1.setText("");
        refresh();
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            packingCaffeeSelected = findPackingCaffeeByNameLocal(packingCaffeeView.jList3.getSelectedValue());
            packingCaffeeView.jTextField5.setText(packingCaffeeSelected.getName());
            packingCaffeeView.jTextArea1.setText(packingCaffeeSelected.getDescription());
            packingCaffeeView.jTextField4.setText(String.valueOf(packingCaffeeSelected.getWeight()));
        }
    }
    
    private PackingCaffee findPackingCaffeeByNameLocal(String name) {
        Iterator<PackingCaffee> iterator = listPackingCaffee.iterator();
        PackingCaffee packingCaffee = null;
        while(iterator.hasNext()) {
            packingCaffee = iterator.next();
            if(packingCaffee.getName().matches(name)) {
                return packingCaffee;
            }
        }
        return packingCaffee;
    }
    
    private List<PackingCaffee> listPackingCaffee;
    private final PackingCaffeeJpaController packingCaffeeJpaController;
    private final PackingCaffee packingCaffee;
    private PackingCaffee packingCaffeeSelected;
    private final PackingCaffeeView packingCaffeeView;

}
