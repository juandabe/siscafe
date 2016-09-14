/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Administrador
 */
@Embeddable
public class AdictionalElementsHasPackagingCaffeePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "adictional_elements_id")
    private int adictionalElementsId;
    @Basic(optional = false)
    @Column(name = "packaging_caffee_id")
    private int packagingCaffeeId;

    public AdictionalElementsHasPackagingCaffeePK() {
    }

    public AdictionalElementsHasPackagingCaffeePK(int adictionalElementsId, int packagingCaffeeId) {
        this.adictionalElementsId = adictionalElementsId;
        this.packagingCaffeeId = packagingCaffeeId;
    }

    public int getAdictionalElementsId() {
        return adictionalElementsId;
    }

    public void setAdictionalElementsId(int adictionalElementsId) {
        this.adictionalElementsId = adictionalElementsId;
    }

    public int getPackagingCaffeeId() {
        return packagingCaffeeId;
    }

    public void setPackagingCaffeeId(int packagingCaffeeId) {
        this.packagingCaffeeId = packagingCaffeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) adictionalElementsId;
        hash += (int) packagingCaffeeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdictionalElementsHasPackagingCaffeePK)) {
            return false;
        }
        AdictionalElementsHasPackagingCaffeePK other = (AdictionalElementsHasPackagingCaffeePK) object;
        if (this.adictionalElementsId != other.adictionalElementsId) {
            return false;
        }
        if (this.packagingCaffeeId != other.packagingCaffeeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.AdictionalElementsHasPackagingCaffeePK[ adictionalElementsId=" + adictionalElementsId + ", packagingCaffeeId=" + packagingCaffeeId + " ]";
    }
    
}
