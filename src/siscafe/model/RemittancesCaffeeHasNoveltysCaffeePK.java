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
public class RemittancesCaffeeHasNoveltysCaffeePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "remittances_caffee_id")
    private int remittancesCaffeeId;
    @Basic(optional = false)
    @Column(name = "noveltys_caffee_id")
    private int noveltysCaffeeId;

    public RemittancesCaffeeHasNoveltysCaffeePK() {
    }

    public RemittancesCaffeeHasNoveltysCaffeePK(int remittancesCaffeeId, int noveltysCaffeeId) {
        this.remittancesCaffeeId = remittancesCaffeeId;
        this.noveltysCaffeeId = noveltysCaffeeId;
    }

    public int getRemittancesCaffeeId() {
        return remittancesCaffeeId;
    }

    public void setRemittancesCaffeeId(int remittancesCaffeeId) {
        this.remittancesCaffeeId = remittancesCaffeeId;
    }

    public int getNoveltysCaffeeId() {
        return noveltysCaffeeId;
    }

    public void setNoveltysCaffeeId(int noveltysCaffeeId) {
        this.noveltysCaffeeId = noveltysCaffeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) remittancesCaffeeId;
        hash += (int) noveltysCaffeeId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemittancesCaffeeHasNoveltysCaffeePK)) {
            return false;
        }
        RemittancesCaffeeHasNoveltysCaffeePK other = (RemittancesCaffeeHasNoveltysCaffeePK) object;
        if (this.remittancesCaffeeId != other.remittancesCaffeeId) {
            return false;
        }
        if (this.noveltysCaffeeId != other.noveltysCaffeeId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.RemittancesCaffeeHasNoveltysCaffeePK[ remittancesCaffeeId=" + remittancesCaffeeId + ", noveltysCaffeeId=" + noveltysCaffeeId + " ]";
    }
    
}
