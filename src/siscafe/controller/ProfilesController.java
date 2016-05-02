
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import siscafe.model.Permits;
import siscafe.model.Profiles;
import siscafe.persistence.PermitsJpaController;
import siscafe.persistence.ProfilesJpaController;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.util.GenericListModel;
import siscafe.view.frontend.ProfilesView;

/**
 *
 * @author jecheverri
 */
public class ProfilesController implements ActionListener, ListSelectionListener{
    
    /**
    *
    * @author jecheverri
    *  @param profilesView
     * @param profile
    */
    public ProfilesController(Profiles profile, ProfilesView profilesView) {
        this.profilesView = profilesView;
        this.profile = profile;
        this.profilesJpaController = new ProfilesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.permitsJpaController = new PermitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        refresh();
    }
    
    public void initListener() {
        this.profilesView.jButton4.addActionListener(this);
        this.profilesView.jButton2.addActionListener(this);
        this.profilesView.jButton1.addActionListener(this);
        this.profilesView.jButton6.addActionListener(this);
        this.profilesView.jButton7.addActionListener(this);
        this.profilesView.jButton3.addActionListener(this);
        this.profilesView.jButton5.addActionListener(this);
        this.profilesView.jList1.addListSelectionListener(this);
        this.profilesView.jList3.addListSelectionListener(this);
        this.profilesView.jList4.addListSelectionListener(this);
    }
    
    private void refresh() {
        this.listPermits = permitsJpaController.findPermitsEntities();
        this.profilesView.jList4.setListData(this.getArrayListModelPermits(listPermits));
        this.listProfiles= profilesJpaController.findProfilesEntities();
        GenericListModel genericListModel = new GenericListModel();
        listProfiles.stream().forEach((permitsFor) -> {
            genericListModel.add(permitsFor.getName());
        });
        this.profilesView.jList3.setModel(genericListModel);
        this.profileSelected = null;
    }
    
    private String[] getArrayListModelPermits(List<Permits> listPermits) {
        Permits [] array = listPermits.toArray(new Permits[listPermits.size()]);
        String[] arrayData = new String[array.length];
        for(int i=0; i<array.length; i++) {
            arrayData[i] = array[i].getName();
        }
        return arrayData;
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
            case "add_permit":
                addPermit();
            break;
            case "deletepermits":
                deletePermits();
            break;    
        }
    }
    
    private void add() {
        Date dNow = new Date( );
        profile.setName(profilesView.jTextField3.getText());
        profile.setDescription(profilesView.jTextArea1.getText());
        profile.setCreatedDate(dNow);
        profile.setUpdatedDate(dNow);
        ListModel <String> listModel = this.profilesView.jList1.getModel();
        profile.setPermitsList(getListPermitsLoaded(listModel));
        profilesJpaController.create(profile);
        profilesView.jTextField3.setText("");
        profilesView.jTextArea1.setText("");
        JOptionPane.showInternalMessageDialog(profilesView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    private void addPermit() {
        if(permitsToLoad != null){
            ListModel<String> listModel = this.profilesView.jList1.getModel();
            int sizeList = listModel.getSize()+1;
            String[] defaultList = new String[sizeList];
            for (int i=0; i<listModel.getSize(); i++) {
                defaultList[i]=listModel.getElementAt(i);
            }
            defaultList[sizeList-1]=permitsToLoad;
            this.profilesView.jList1.setListData(defaultList);
            this.profilesView.jList1.repaint();
            ListModel listmodelPermits = this.profilesView.jList4.getModel();
            this.profilesView.jList4.setModel(listmodelPermits);
            permitsToLoad=null;
        }
    }
    
    private void deletePermits() {
        if(permitToDelete != null ) {
            ListModel<String> listModel = this.profilesView.jList1.getModel();
            int sizeList = listModel.getSize()-1;
            String[] defaultList = new String[sizeList];
            for (int i=0; i<listModel.getSize(); i++) {
                if(!listModel.getElementAt(i).matches(permitToDelete)) {
                    defaultList[i]=listModel.getElementAt(i);
                }
            }
            this.profilesView.jList1.setListData(defaultList);
            this.profilesView.jList1.repaint();
            this.permitToDelete=null;
        }
    }
    
    private void edit() {
        Date dNow = new Date( );
        profileSelected.setName(this.profilesView.jTextField3.getText());
        profileSelected.setDescription(this.profilesView.jTextArea1.getText());
        profileSelected.setUpdatedDate(dNow);
        ListModel <String> listModel = this.profilesView.jList1.getModel();
        profileSelected.setPermitsList(getListPermitsLoaded(listModel));
        try {
            profilesJpaController.edit(profileSelected);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProfilesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ProfilesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            JOptionPane.showInternalMessageDialog(profilesView, "Confirmación", "Registro actualizado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void delete() {
        System.out.println("asdasd");
    }
    
    private void clear() {
        System.out.println("asdasd");
    }
    
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            JList list = (JList)e.getSource();
            if(list.getName().matches("listprofiles")) {
                this.profileSelected = findProfilesByNameLocal(this.profilesView.jList3.getSelectedValue());
                this.profilesView.jTextField3.setText(profileSelected.getName());
                this.profilesView.jTextArea1.setText(profileSelected.getDescription());
                this.profilesView.jList1.setListData(getArrayListModelPermits(this.profileSelected.getPermitsList()));
            }
            else if(list.getName().matches("listpermits")) {
                this.permitsToLoad = this.profilesView.jList4.getSelectedValue();
            }
            else{
                this.permitToDelete = this.profilesView.jList1.getSelectedValue();
                System.out.println(this.permitToDelete);
            }
        }
    }
    
    private Profiles findProfilesByNameLocal(String name) {
        Iterator<Profiles> iterator = this.listProfiles.iterator();
        Profiles profile = null;
        while(iterator.hasNext()) {
            profile = iterator.next();
            if(profile.getName().matches(name)) {
                return profile;
            }
        }
        return profile;
    }
    
    private List<Permits> getListPermitsLoaded(ListModel<String> listModel) {
        List<Permits> listPermitLoaded = new ArrayList();
        List<Permits> listPermitsLocal = permitsJpaController.findPermitsEntities();
        for(int i=0; i<listModel.getSize(); i++) {
            Iterator<Permits> iterator = listPermitsLocal.iterator();
            while(iterator.hasNext()) {
                Permits permit = iterator.next();
                if(permit.getName().matches(listModel.getElementAt(i))) {
                    listPermitLoaded.add(permit);
                    break;
                }
            }
        }
        return listPermitLoaded;
    }
    
    private List<Profiles> listProfiles;
    private List<Permits> listPermits;
    private final ProfilesJpaController profilesJpaController;
    private final PermitsJpaController permitsJpaController;
    private Profiles profile;
    private Profiles profileSelected;
    private String permitsToLoad;
    private String permitToDelete;
    private final ProfilesView profilesView;

}
