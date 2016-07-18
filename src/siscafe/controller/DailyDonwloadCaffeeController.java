/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Persistence;
import javax.swing.JButton;
import siscafe.model.StoresCaffee;
import siscafe.persistence.StoresCaffeeJpaController;
import siscafe.report.Reports;
import siscafe.util.MyComboBoxModel;
import siscafe.view.frontend.DailyDownloadCaffeeView;

/**
 *
 * @author Administrador
 */
public class DailyDonwloadCaffeeController implements ActionListener{
    
    public DailyDonwloadCaffeeController(DailyDownloadCaffeeView dailyDownloadCaffee, String username) {
        this.dailyDownloadCaffee = dailyDownloadCaffee;
        this.username = username;
        storesCaffeeJpaController = new StoresCaffeeJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
    }
    
    public void initListener() {
        listStoresCaffee = storesCaffeeJpaController.findStoresCaffeeEntities();
        myComboBoxModelStoresCaffeeStart = new MyComboBoxModel(listStoresCaffee);
        myComboBoxModelStoresCaffeeEnd = new MyComboBoxModel(listStoresCaffee);
        dailyDownloadCaffee.jButton1.addActionListener(this);
        dailyDownloadCaffee.jButton2.addActionListener(this);
        dailyDownloadCaffee.jComboBox1.setModel(myComboBoxModelStoresCaffeeStart);
        dailyDownloadCaffee.jComboBox2.setModel(myComboBoxModelStoresCaffeeEnd);
    }
    
    @Override
     public void actionPerformed(ActionEvent e) {
        String btn = ((JButton)e.getSource()).getName();
        switch (btn){
            case "dailydownloadcaffee":
                dailyDownloadCaffeeReport();
            break;
        }
     }
     
    private void dailyDownloadCaffeeReport() {
        StoresCaffee storesStart = findStoresCaffeeByNameLocal((String)dailyDownloadCaffee.jComboBox1.getSelectedItem());
        StoresCaffee storesEnd = findStoresCaffeeByNameLocal((String)dailyDownloadCaffee.jComboBox2.getSelectedItem());
        Date dateStart = dailyDownloadCaffee.jXDatePicker1.getDate();
        Date dateEnd = dailyDownloadCaffee.jXDatePicker2.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        new Reports().showReportDaylyDownloadCaffee(dateFormat.format(dateStart), dateFormat.format(dateEnd), storesStart.getId().toString(), storesEnd.getId().toString(), username);
    }
    
    private StoresCaffee findStoresCaffeeByNameLocal(String name) {
        Iterator<StoresCaffee> iterator = this.listStoresCaffee.iterator();
        StoresCaffee storesCaffee = null;
        while(iterator.hasNext()) {
            storesCaffee = iterator.next();
            if(storesCaffee.getStoreName().matches(name)) {
                return storesCaffee;
            }
        }
        return storesCaffee;
    }
    
    private String username;
    private MyComboBoxModel myComboBoxModelStoresCaffeeStart;
    private MyComboBoxModel myComboBoxModelStoresCaffeeEnd;
    private StoresCaffeeJpaController storesCaffeeJpaController;
    private DailyDownloadCaffeeView dailyDownloadCaffee;
    private List<StoresCaffee> listStoresCaffee;
}
