/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "fumigation_services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FumigationServices.findAll", query = "SELECT f FROM FumigationServices f"),
    @NamedQuery(name = "FumigationServices.findById", query = "SELECT f FROM FumigationServices f WHERE f.id = :id"),
    @NamedQuery(name = "FumigationServices.findByCertificateNumber", query = "SELECT f FROM FumigationServices f WHERE f.certificateNumber = :certificateNumber"),
    @NamedQuery(name = "FumigationServices.findByQuantityPoisonUsed", query = "SELECT f FROM FumigationServices f WHERE f.quantityPoisonUsed = :quantityPoisonUsed"),
    @NamedQuery(name = "FumigationServices.findByStartDate", query = "SELECT f FROM FumigationServices f WHERE f.startDate = :startDate"),
    @NamedQuery(name = "FumigationServices.findByFinishedDate", query = "SELECT f FROM FumigationServices f WHERE f.finishedDate = :finishedDate"),
    @NamedQuery(name = "FumigationServices.findByVolumenFumigated", query = "SELECT f FROM FumigationServices f WHERE f.volumenFumigated = :volumenFumigated"),
    @NamedQuery(name = "FumigationServices.findByResquestServiceDate", query = "SELECT f FROM FumigationServices f WHERE f.resquestServiceDate = :resquestServiceDate"),
    @NamedQuery(name = "FumigationServices.findByResquestCertificateDate", query = "SELECT f FROM FumigationServices f WHERE f.resquestCertificateDate = :resquestCertificateDate")})
public class FumigationServices implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "certificate_number")
    private String certificateNumber;
    @Basic(optional = false)
    @Lob
    @Column(name = "product_fumigation")
    private String productFumigation;
    @Basic(optional = false)
    @Column(name = "quantity_poison_used")
    private int quantityPoisonUsed;
    @Basic(optional = false)
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Basic(optional = false)
    @Column(name = "finished_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedDate;
    @Basic(optional = false)
    @Column(name = "volumen_fumigated")
    private double volumenFumigated;
    @Column(name = "resquest_service_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resquestServiceDate;
    @Column(name = "resquest_certificate_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resquestCertificateDate;
    @JoinColumn(name = "remittances_caffee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCaffeeId;

    public FumigationServices() {
    }

    public FumigationServices(Integer id) {
        this.id = id;
    }

    public FumigationServices(Integer id, String certificateNumber, String productFumigation, int quantityPoisonUsed, Date startDate, Date finishedDate, double volumenFumigated) {
        this.id = id;
        this.certificateNumber = certificateNumber;
        this.productFumigation = productFumigation;
        this.quantityPoisonUsed = quantityPoisonUsed;
        this.startDate = startDate;
        this.finishedDate = finishedDate;
        this.volumenFumigated = volumenFumigated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getProductFumigation() {
        return productFumigation;
    }

    public void setProductFumigation(String productFumigation) {
        this.productFumigation = productFumigation;
    }

    public int getQuantityPoisonUsed() {
        return quantityPoisonUsed;
    }

    public void setQuantityPoisonUsed(int quantityPoisonUsed) {
        this.quantityPoisonUsed = quantityPoisonUsed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(Date finishedDate) {
        this.finishedDate = finishedDate;
    }

    public double getVolumenFumigated() {
        return volumenFumigated;
    }

    public void setVolumenFumigated(double volumenFumigated) {
        this.volumenFumigated = volumenFumigated;
    }

    public Date getResquestServiceDate() {
        return resquestServiceDate;
    }

    public void setResquestServiceDate(Date resquestServiceDate) {
        this.resquestServiceDate = resquestServiceDate;
    }

    public Date getResquestCertificateDate() {
        return resquestCertificateDate;
    }

    public void setResquestCertificateDate(Date resquestCertificateDate) {
        this.resquestCertificateDate = resquestCertificateDate;
    }

    public RemittancesCaffee getRemittancesCaffeeId() {
        return remittancesCaffeeId;
    }

    public void setRemittancesCaffeeId(RemittancesCaffee remittancesCaffeeId) {
        this.remittancesCaffeeId = remittancesCaffeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FumigationServices)) {
            return false;
        }
        FumigationServices other = (FumigationServices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.FumigationServices[ id=" + id + " ]";
    }
    
}
