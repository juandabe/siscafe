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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
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
@Table(name = "detail_packaging_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailPackagingCaffee.findAll", query = "SELECT d FROM DetailPackagingCaffee d"),
    @NamedQuery(name = "DetailPackagingCaffee.findByRemittancesCaffeeId", query = "SELECT d FROM DetailPackagingCaffee d WHERE d.detailPackagingCaffeePK.remittancesCaffeeId = :remittancesCaffeeId"),
    @NamedQuery(name = "DetailPackagingCaffee.findByPackagingCaffeeId", query = "SELECT d FROM DetailPackagingCaffee d WHERE d.detailPackagingCaffeePK.packagingCaffeeId = :packagingCaffeeId"),
    @NamedQuery(name = "DetailPackagingCaffee.findByQuantityRadicatedBagOut", query = "SELECT d FROM DetailPackagingCaffee d WHERE d.quantityRadicatedBagOut = :quantityRadicatedBagOut"),
    @NamedQuery(name = "DetailPackagingCaffee.findByCreatedDate", query = "SELECT d FROM DetailPackagingCaffee d WHERE d.createdDate = :createdDate"),
    @NamedQuery(name = "DetailPackagingCaffee.findByUpdatedDate", query = "SELECT d FROM DetailPackagingCaffee d WHERE d.updatedDate = :updatedDate")})
public class DetailPackagingCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailPackagingCaffeePK detailPackagingCaffeePK;
    @Basic(optional = false)
    @Column(name = "quantity_radicated_bag_out")
    private int quantityRadicatedBagOut;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "remittances_caffee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCaffee;
    @JoinColumn(name = "packaging_caffee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PackagingCaffee packagingCaffee;

    public DetailPackagingCaffee() {
    }

    public DetailPackagingCaffee(DetailPackagingCaffeePK detailPackagingCaffeePK) {
        this.detailPackagingCaffeePK = detailPackagingCaffeePK;
    }

    public DetailPackagingCaffee(DetailPackagingCaffeePK detailPackagingCaffeePK, int quantityRadicatedBagOut, Date createdDate, Date updatedDate) {
        this.detailPackagingCaffeePK = detailPackagingCaffeePK;
        this.quantityRadicatedBagOut = quantityRadicatedBagOut;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public DetailPackagingCaffee(int remittancesCaffeeId, int packagingCaffeeId) {
        this.detailPackagingCaffeePK = new DetailPackagingCaffeePK(remittancesCaffeeId, packagingCaffeeId);
    }

    public DetailPackagingCaffeePK getDetailPackagingCaffeePK() {
        return detailPackagingCaffeePK;
    }

    public void setDetailPackagingCaffeePK(DetailPackagingCaffeePK detailPackagingCaffeePK) {
        this.detailPackagingCaffeePK = detailPackagingCaffeePK;
    }

    public int getQuantityRadicatedBagOut() {
        return quantityRadicatedBagOut;
    }

    public void setQuantityRadicatedBagOut(int quantityRadicatedBagOut) {
        this.quantityRadicatedBagOut = quantityRadicatedBagOut;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public RemittancesCaffee getRemittancesCaffee() {
        return remittancesCaffee;
    }

    public void setRemittancesCaffee(RemittancesCaffee remittancesCaffee) {
        this.remittancesCaffee = remittancesCaffee;
    }

    public PackagingCaffee getPackagingCaffee() {
        return packagingCaffee;
    }

    public void setPackagingCaffee(PackagingCaffee packagingCaffee) {
        this.packagingCaffee = packagingCaffee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailPackagingCaffeePK != null ? detailPackagingCaffeePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailPackagingCaffee)) {
            return false;
        }
        DetailPackagingCaffee other = (DetailPackagingCaffee) object;
        if ((this.detailPackagingCaffeePK == null && other.detailPackagingCaffeePK != null) || (this.detailPackagingCaffeePK != null && !this.detailPackagingCaffeePK.equals(other.detailPackagingCaffeePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.DetailPackagingCaffee[ detailPackagingCaffeePK=" + detailPackagingCaffeePK + " ]";
    }
    
}
