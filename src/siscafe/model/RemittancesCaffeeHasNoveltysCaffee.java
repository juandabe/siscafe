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
@Table(name = "remittances_caffee_has_noveltys_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemittancesCaffeeHasNoveltysCaffee.findAll", query = "SELECT r FROM RemittancesCaffeeHasNoveltysCaffee r"),
    @NamedQuery(name = "RemittancesCaffeeHasNoveltysCaffee.findByRemittancesCaffeeId", query = "SELECT r FROM RemittancesCaffeeHasNoveltysCaffee r WHERE r.remittancesCaffeeHasNoveltysCaffeePK.remittancesCaffeeId = :remittancesCaffeeId"),
    @NamedQuery(name = "RemittancesCaffeeHasNoveltysCaffee.findByNoveltysCaffeeId", query = "SELECT r FROM RemittancesCaffeeHasNoveltysCaffee r WHERE r.remittancesCaffeeHasNoveltysCaffeePK.noveltysCaffeeId = :noveltysCaffeeId"),
    @NamedQuery(name = "RemittancesCaffeeHasNoveltysCaffee.findByCreatedDate", query = "SELECT r FROM RemittancesCaffeeHasNoveltysCaffee r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "RemittancesCaffeeHasNoveltysCaffee.findByActive", query = "SELECT r FROM RemittancesCaffeeHasNoveltysCaffee r WHERE r.active = :active")})
public class RemittancesCaffeeHasNoveltysCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected RemittancesCaffeeHasNoveltysCaffeePK remittancesCaffeeHasNoveltysCaffeePK;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @JoinColumn(name = "noveltys_caffee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private NoveltysCaffee noveltysCaffee;
    @JoinColumn(name = "remittances_caffee_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCaffee;

    public RemittancesCaffeeHasNoveltysCaffee() {
    }

    public RemittancesCaffeeHasNoveltysCaffee(RemittancesCaffeeHasNoveltysCaffeePK remittancesCaffeeHasNoveltysCaffeePK) {
        this.remittancesCaffeeHasNoveltysCaffeePK = remittancesCaffeeHasNoveltysCaffeePK;
    }

    public RemittancesCaffeeHasNoveltysCaffee(RemittancesCaffeeHasNoveltysCaffeePK remittancesCaffeeHasNoveltysCaffeePK, Date createdDate, boolean active) {
        this.remittancesCaffeeHasNoveltysCaffeePK = remittancesCaffeeHasNoveltysCaffeePK;
        this.createdDate = createdDate;
        this.active = active;
    }

    public RemittancesCaffeeHasNoveltysCaffee(int remittancesCaffeeId, int noveltysCaffeeId) {
        this.remittancesCaffeeHasNoveltysCaffeePK = new RemittancesCaffeeHasNoveltysCaffeePK(remittancesCaffeeId, noveltysCaffeeId);
    }

    public RemittancesCaffeeHasNoveltysCaffeePK getRemittancesCaffeeHasNoveltysCaffeePK() {
        return remittancesCaffeeHasNoveltysCaffeePK;
    }

    public void setRemittancesCaffeeHasNoveltysCaffeePK(RemittancesCaffeeHasNoveltysCaffeePK remittancesCaffeeHasNoveltysCaffeePK) {
        this.remittancesCaffeeHasNoveltysCaffeePK = remittancesCaffeeHasNoveltysCaffeePK;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public NoveltysCaffee getNoveltysCaffee() {
        return noveltysCaffee;
    }

    public void setNoveltysCaffee(NoveltysCaffee noveltysCaffee) {
        this.noveltysCaffee = noveltysCaffee;
    }

    public RemittancesCaffee getRemittancesCaffee() {
        return remittancesCaffee;
    }

    public void setRemittancesCaffee(RemittancesCaffee remittancesCaffee) {
        this.remittancesCaffee = remittancesCaffee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (remittancesCaffeeHasNoveltysCaffeePK != null ? remittancesCaffeeHasNoveltysCaffeePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RemittancesCaffeeHasNoveltysCaffee)) {
            return false;
        }
        RemittancesCaffeeHasNoveltysCaffee other = (RemittancesCaffeeHasNoveltysCaffee) object;
        if ((this.remittancesCaffeeHasNoveltysCaffeePK == null && other.remittancesCaffeeHasNoveltysCaffeePK != null) || (this.remittancesCaffeeHasNoveltysCaffeePK != null && !this.remittancesCaffeeHasNoveltysCaffeePK.equals(other.remittancesCaffeeHasNoveltysCaffeePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.RemittancesCaffeeHasNoveltysCaffee[ remittancesCaffeeHasNoveltysCaffeePK=" + remittancesCaffeeHasNoveltysCaffeePK + " ]";
    }
    
}
