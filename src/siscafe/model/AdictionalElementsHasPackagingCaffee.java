/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "adictional_elements_has_packaging_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdictionalElementsHasPackagingCaffee.findAll", query = "SELECT a FROM AdictionalElementsHasPackagingCaffee a"),
    @NamedQuery(name = "AdictionalElementsHasPackagingCaffee.findByAdictionalElementsId", query = "SELECT a FROM AdictionalElementsHasPackagingCaffee a WHERE a.adictionalElementsHasPackagingCaffeePK.adictionalElementsId = :adictionalElementsId"),
    @NamedQuery(name = "AdictionalElementsHasPackagingCaffee.findByPackagingCaffeeId", query = "SELECT a FROM AdictionalElementsHasPackagingCaffee a WHERE a.adictionalElementsHasPackagingCaffeePK.packagingCaffeeId = :packagingCaffeeId"),
    @NamedQuery(name = "AdictionalElementsHasPackagingCaffee.findByQuantity", query = "SELECT a FROM AdictionalElementsHasPackagingCaffee a WHERE a.quantity = :quantity"),
    @NamedQuery(name = "AdictionalElementsHasPackagingCaffee.findByTypeUnit", query = "SELECT a FROM AdictionalElementsHasPackagingCaffee a WHERE a.typeUnit = :typeUnit")})
public class AdictionalElementsHasPackagingCaffee implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AdictionalElementsHasPackagingCaffeePK adictionalElementsHasPackagingCaffeePK;
    @Basic(optional = false)
    @Column(name = "quantity")
    private int quantity;
    @Basic(optional = false)
    @Column(name = "type_unit")
    private String typeUnit;
    @JoinColumn(name = "adictional_elements_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private AdictionalElements adictionalElements;
    @JoinColumn(name = "packaging_caffee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private PackagingCaffee packagingCaffee;

    public AdictionalElementsHasPackagingCaffee() {
    }

    public AdictionalElementsHasPackagingCaffee(AdictionalElementsHasPackagingCaffeePK adictionalElementsHasPackagingCaffeePK) {
        this.adictionalElementsHasPackagingCaffeePK = adictionalElementsHasPackagingCaffeePK;
    }

    public AdictionalElementsHasPackagingCaffee(AdictionalElementsHasPackagingCaffeePK adictionalElementsHasPackagingCaffeePK, int quantity, String typeUnit) {
        this.adictionalElementsHasPackagingCaffeePK = adictionalElementsHasPackagingCaffeePK;
        this.quantity = quantity;
        this.typeUnit = typeUnit;
    }

    public AdictionalElementsHasPackagingCaffee(int adictionalElementsId, int packagingCaffeeId) {
        this.adictionalElementsHasPackagingCaffeePK = new AdictionalElementsHasPackagingCaffeePK(adictionalElementsId, packagingCaffeeId);
    }

    public AdictionalElementsHasPackagingCaffeePK getAdictionalElementsHasPackagingCaffeePK() {
        return adictionalElementsHasPackagingCaffeePK;
    }

    public void setAdictionalElementsHasPackagingCaffeePK(AdictionalElementsHasPackagingCaffeePK adictionalElementsHasPackagingCaffeePK) {
        this.adictionalElementsHasPackagingCaffeePK = adictionalElementsHasPackagingCaffeePK;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTypeUnit() {
        return typeUnit;
    }

    public void setTypeUnit(String typeUnit) {
        this.typeUnit = typeUnit;
    }

    public AdictionalElements getAdictionalElements() {
        return adictionalElements;
    }

    public void setAdictionalElements(AdictionalElements adictionalElements) {
        this.adictionalElements = adictionalElements;
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
        hash += (adictionalElementsHasPackagingCaffeePK != null ? adictionalElementsHasPackagingCaffeePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdictionalElementsHasPackagingCaffee)) {
            return false;
        }
        AdictionalElementsHasPackagingCaffee other = (AdictionalElementsHasPackagingCaffee) object;
        if ((this.adictionalElementsHasPackagingCaffeePK == null && other.adictionalElementsHasPackagingCaffeePK != null) || (this.adictionalElementsHasPackagingCaffeePK != null && !this.adictionalElementsHasPackagingCaffeePK.equals(other.adictionalElementsHasPackagingCaffeePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.AdictionalElementsHasPackagingCaffee[ adictionalElementsHasPackagingCaffeePK=" + adictionalElementsHasPackagingCaffeePK + " ]";
    }
    
}
