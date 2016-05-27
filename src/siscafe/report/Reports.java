/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import siscafe.model.RemittancesCaffee;
import siscafe.util.ReaderProperties;

/**
 *
 * @author Administrador
 */
public class Reports {
    
    public void showReportDownloadCaffee(RemittancesCaffee remittancesCaffee) {
        try {
            System.out.println(remittancesCaffee.getId());
            String remittancesCaffeeId = String.valueOf(remittancesCaffee.getId());
            HashMap map = new HashMap();
            map.put("REMITTANCE_ID",remittancesCaffeeId); 
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://192.168.35.213:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("REPORTDOWNLOADDIR");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setTitle("Papeleta de repeso - Descargue");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showReportRemittancesCaffeeRadicated(RemittancesCaffee remittancesCaffee) {
        try {
            String remittancesCaffeeId = String.valueOf(remittancesCaffee.getId());
            HashMap map = new HashMap();
            map.put("REMITTANCES_ID",remittancesCaffeeId); 
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://192.168.35.213:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("REPORTREMITTANCESCAFFEERADICATED");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setTitle("Documento de radicación Café - Descargue");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
