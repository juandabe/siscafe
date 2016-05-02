/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.util;

import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import siscafe.model.CategoryPermits;

/**
 *
 * @author Administrador
 */
public class MyComboBoxModel extends AbstractListModel implements ComboBoxModel {

    public MyComboBoxModel(List<?> lista) {
        this.listObject = lista;
    }
    String selection = null;

    public Object getElementAt(int index) {
      return listObject.get(index);
    }

    public int getSize() {
      return listObject.size();
    }

    public void setSelectedItem(Object anItem) {
      selection = anItem.toString(); // to select and register an
    } // item from the pull-down list

    // Methods implemented from the interface ComboBoxModel
    public Object getSelectedItem() {
      return selection; // to add the selection to the combo box
    }
    
    public Object getElementAtName(String name) {
        CategoryPermits categoryPermits=null;
        Iterator<?> iterator = listObject.iterator();
        while(iterator.hasNext()) {
            categoryPermits = (CategoryPermits) iterator.next();
            if(categoryPermits.getName().matches(name)) {
                return categoryPermits;
            }
        }
        return categoryPermits;
    }
    
    private List<?> listObject;
  }
