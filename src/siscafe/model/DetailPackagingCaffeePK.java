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
public class DetailPackagingCaffeePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "remittances_caffee_id")
    private int remittancesCaffeeId;
    @Basic(optional = false)
    @Column(name = "packaging_caffee_id")
    private int packagingCaffeeId;

    public DetailPackagingCaffeePK() {
    }

    public DetailPackagingCaffeePK(int remittancesCaffeeId, int packagingCaffeeId) {
        this.remittancesCaffeeId = remittancesCaffeeId;
        this.packagingCaffeeId = packagingCaffeeId;
    }

    public int getRemittancesCaffeeId() {
        return remittancesCaffeeId;
    }

    public void setRemittancesCaffeeId(int remittancesCaffeeId) {
        this.remittancesCaffeeId = remittancesCaffeeId;
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
        hash += (int) remittancesCaffeeId;
        hash += (int) packagingCaffeeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailPackagingCaffeePK)) {
            return false;
        }
        DetailPackagingCaffeePK other = (DetailPackagingCaffeePK) object;
        if (this.remittancesCaffeeId != other.remittancesCaffeeId) {
            return false;
        }
        if (this.packagingCaffeeId != other.packagingCaffeeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.DetailPackagingCaffeePK[ remittancesCaffeeId=" + remittancesCaffeeId + ", packagingCaffeeId=" + packagingCaffeeId + " ]";
    }
    
}
