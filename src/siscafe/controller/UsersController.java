
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
import siscafe.model.Departaments;
import siscafe.model.Profiles;
import siscafe.model.Users;
import siscafe.persistence.DepartamentsJpaController;
import siscafe.persistence.ProfilesJpaController;
import siscafe.persistence.UsersJpaController;
import siscafe.util.GenericListModel;
import siscafe.util.HashSecurityGenerator;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.UsersView;
import siscafe.view.frontend.Frontend;
import siscafe.view.frontend.LoginView;

/**
 *
 * @author jecheverri
 */
public class UsersController implements ActionListener,ListSelectionListener{
    
    public UsersController(LoginView loginView, Frontend miFrontend, UsersView userView, Users userModel) {
        this.userView = userView;
        this.userModel = userModel;
        this.miFrontend = miFrontend;
        this.loginView = loginView;
        this.usersJpaController = new UsersJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.departamentsJpaController = new DepartamentsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        this.profilesJpaController = new ProfilesJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        refresh();
    }
    
    public void initListener() {
        this.loginView.jButton1.addActionListener(this);
        this.userView.jButton4.addActionListener(this);
        this.userView.jButton2.addActionListener(this);
        this.userView.jButton3.addActionListener(this);
        this.userView.jButton5.addActionListener(this);
        this.userView.jComboBox1.setModel(myComboBoxModelDepartaments);
        this.userView.jComboBox2.setModel(myComboBoxModelProfiles);
        this.userView.jList3.addListSelectionListener(this);
    }
    
    private void login() {
        String username = this.loginView.jTextField1.getText();
        String password = HashSecurityGenerator.MD5(this.loginView.jPasswordField1.getText());
        userLogin = usersJpaController.findUsersByAccess(username, password);
        if(userLogin != null) {
            this.miFrontend.initPermitsByProfile(userLogin);
            this.loginView.dispose();
        }
        else
            JOptionPane.showInternalMessageDialog(loginView, "Datos de acceso errados!", "Información",JOptionPane.ERROR_MESSAGE);
    }
    
    private Departaments findDepartamentsByNameLocal(String name) {
        Iterator<Departaments> iterator = this.listDepartaments.iterator();
        Departaments departaments = null;
        while(iterator.hasNext()) {
            departaments = iterator.next();
            if(departaments.getName().matches(name)) {
                return departaments;
            }
        }
        return departaments;
    }
    
    private Profiles findProfilesByNameLocal(String name) {
        Iterator<Profiles> iterator = this.listProfiles.iterator();
        Profiles profiles = null;
        while(iterator.hasNext()) {
            profiles = iterator.next();
            if(profiles.getName().matches(name)) {
                return profiles;
            }
        }
        return profiles;
    }
    
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(e.getValueIsAdjusting()) {
            this.userModel = findUsersPermitsByNameLocal(this.userView.jList3.getSelectedValue());
            this.userView.jComboBox1.getModel().setSelectedItem(this.userModel.getDepartamentsId().getName());
            this.userView.jComboBox2.getModel().setSelectedItem(this.userModel.getProfilesId().getName());
            this.userView.jComboBox1.repaint();
            this.userView.jComboBox2.repaint();
            this.userView.jTextField8.setText(userModel.getExtension());
            this.userView.jTextField3.setText(userModel.getFirstName());
            this.userView.jTextField4.setText(userModel.getLastName());
            this.userView.jTextField7.setText(userModel.getPhone());
            this.userView.jTextField11.setText(userModel.getUsername());
            this.userView.jCheckBox1.setSelected(userModel.getActive());
            this.userView.jTextField12.setText(userModel.getPassword());
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "login":
                login();
            break;
            case "add":
                add();
            break;
            case "edit":
                edit();
            break;
            case "refresh":
                 refresh();
            case "clear":
                 clear();
            break;
        }
    }
    
    private void refresh() {
        this.listProfiles = profilesJpaController.findProfilesEntities();
        this.listDepartaments = departamentsJpaController.findDepartamentsEntities();
        this.listUsers = usersJpaController.findUsersEntities();
        this.myComboBoxModelDepartaments = new MyComboBoxModel(listDepartaments);
        this.myComboBoxModelProfiles = new MyComboBoxModel(listProfiles);
        GenericListModel genericListModel = new GenericListModel();
        listUsers.stream().forEach((permitsFor) -> {
            genericListModel.add(permitsFor.getFirstName()+" "+permitsFor.getLastName());
        });
        this.userView.jList3.setModel(genericListModel);
    }
    
    private void add() {
        Date dNow = new Date( );
        userModel.setCreatedDate(dNow);
        userModel.setUpdatedDate(dNow);
        userModel.setActive(this.userView.jCheckBox1.isSelected());
        userModel.setFirstName(this.userView.jTextField3.getText());
        userModel.setLastName(this.userView.jTextField4.getText());
        userModel.setPhone(this.userView.jTextField7.getText());
        userModel.setExtension((this.userView.jTextField8.getText()));
        String departaments = (String) this.userView.jComboBox1.getSelectedItem();
        userModel.setDepartamentsId(findDepartamentsByNameLocal(departaments));
        String profile = (String) this.userView.jComboBox2.getSelectedItem();
        userModel.setProfilesId(findProfilesByNameLocal(profile));
        userModel.setUsername(this.userView.jTextField11.getText());
        userModel.setPassword(HashSecurityGenerator.MD5(this.userView.jTextField12.getText()));
        usersJpaController.create(userModel);
        this.userView.jTextField3.setText("");
        this.userView.jTextField4.setText("");
        this.userView.jTextField7.setText("");
        this.userView.jTextField8.setText("");
        this.userView.jTextField11.setText("");
        this.userView.jTextField12.setText("");
        JOptionPane.showInternalMessageDialog(this.userView, "Registro creado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        refresh();
    }
    
    private void clear() {
        this.userView.jTextField8.setText("");
        this.userView.jTextField3.setText("");
        this.userView.jTextField4.setText("");
        this.userView.jTextField7.setText("");
        this.userView.jTextField11.setText("");
        this.userView.jTextField12.setText("");
    }
    
    private void edit() {
        Date dNow = new Date( );
        userModel.setUpdatedDate(dNow);
        userModel.setActive(this.userView.jCheckBox1.isSelected());
        userModel.setFirstName(this.userView.jTextField3.getText());
        userModel.setLastName(this.userView.jTextField4.getText());
        userModel.setPhone(this.userView.jTextField7.getText());
        userModel.setExtension((this.userView.jTextField8.getText()));
        String departaments = (String) this.userView.jComboBox1.getSelectedItem();
        userModel.setDepartamentsId(findDepartamentsByNameLocal(departaments));
        String profile = (String) this.userView.jComboBox2.getSelectedItem();
        userModel.setProfilesId(findProfilesByNameLocal(profile));
        userModel.setProfilesId(findProfilesByNameLocal(profile));
        userModel.setUsername(this.userView.jTextField11.getText());
        userModel.setPassword(HashSecurityGenerator.MD5(this.userView.jTextField12.getText()));
        try {
            usersJpaController.edit(userModel);
        } catch (Exception ex) {
            Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            JOptionPane.showInternalMessageDialog(this.userView, "Registro actualizado", 
                "Confirmación", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private Users findUsersPermitsByNameLocal(String name) {
        Iterator<Users> iterator = this.listUsers.iterator();
        Users user = null;
        while(iterator.hasNext()) {
            user = iterator.next();
            String nameFull = user.getFirstName()+" "+user.getLastName();
            if(nameFull.equals(name)) {
                return user;
            }
        }
        return user;
    }

    private MyComboBoxModel myComboBoxModelDepartaments;
    private MyComboBoxModel myComboBoxModelProfiles;
    private final ProfilesJpaController profilesJpaController;
    private final DepartamentsJpaController departamentsJpaController;
    private List <Departaments> listDepartaments;
    private List <Profiles> listProfiles;
    private List <Users> listUsers;
    private Users userLogin;
    private Users userModel;
    private UsersView userView;
    private final LoginView loginView;
    private UsersJpaController usersJpaController;
    private Frontend miFrontend;

}
