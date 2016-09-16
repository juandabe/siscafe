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
import javax.swing.JFrame;
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
    
    public Reports () {
        
    }
    
    public void showReportDaylyDownloadCaffee(String startDate, String endDate, String startStore, String endStore, String username) {
        try {
            HashMap map = new HashMap();
            map.put("START_DATE",startDate);
            map.put("END_DATE",endDate);
            map.put("START_STORE",startStore);
            map.put("END_STORE",endStore);
            map.put("username",username);
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("DAYLYDOWNLOADCAFFEE");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Descargue de Café en Bodega - Descargue");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte: \n"+e.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showReportDownloadCaffee(RemittancesCaffee remittancesCaffee) {
        try {
            String remittancesCaffeeId = String.valueOf(remittancesCaffee.getId());
            HashMap map = new HashMap();
            map.put("REMITTANCE_ID",remittancesCaffeeId);
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("REPORTDOWNLOADDIR");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Papeleta de repeso - Descargue");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte: \n"+e.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showReportRemittancesCaffeeRadicated(RemittancesCaffee remittancesCaffee, String username) {
        try {
            String remittancesCaffeeId = String.valueOf(remittancesCaffee.getId());
            HashMap map = new HashMap();
            map.put("REMITTANCES_ID",remittancesCaffeeId);
            map.put("username",username);
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("REPORTREMITTANCESCAFFEERADICATED");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Documento de radicación Café - Descargue");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte:\n"+e.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void showFullConsulttRemittancesCaffee(String remittancesCaffee, String username) {
        try {
            HashMap map = new HashMap();
            map.put("REMMITANCE_ID",remittancesCaffee);
            map.put("username",username);
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("REMITTANCESCAFFE_DETAILS");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Detalle operación "+remittancesCaffee+" - Consulta");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte:\n"+e.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void reportCommodityCaffeeDeliver(String OIE, String username) {
        try {
            HashMap map = new HashMap();
            map.put("OIE",OIE);
            map.put("username",username);
            Connection conn =null;
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/schema_siscafe?zeroDateTimeBehavior=convertToNull", "sop_user", "123");
            String path = new ReaderProperties().getProperties("COMMODITYCAFFEEDLIVER");
            JasperReport report = null;
            report = (JasperReport) JRLoader.loadObjectFromFile(path);
            JasperPrint print = JasperFillManager.fillReport(report, map, conn);
            JasperViewer viewer = new JasperViewer(print,false);
            viewer.setExtendedState(JFrame.MAXIMIZED_BOTH);
            viewer.setTitle("Order Interna de Entrega "+OIE+" - Reporte");
            viewer.setVisible(true);
        }
        catch (SQLException | JRException e){
            JOptionPane.showMessageDialog(null, "Error al general el reporte:\n"+e.getMessage(), "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
