/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "container_filling")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContainerFilling.findAll", query = "SELECT c FROM ContainerFilling c"),
    @NamedQuery(name = "ContainerFilling.findById", query = "SELECT c FROM ContainerFilling c WHERE c.id = :id"),
    @NamedQuery(name = "ContainerFilling.findByContainerBicCode", query = "SELECT c FROM ContainerFilling c WHERE c.containerBicCode = :containerBicCode")})
public class ContainerFilling implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "container_bic_code")
    private String containerBicCode;
    @JoinColumn(name = "fraction_pallet_id", referencedColumnName = "id")
    @ManyToOne
    private FractionPallet fractionPalletId;
    @JoinColumn(name = "remittances_caffee_id", referencedColumnName = "id")
    @ManyToOne
    private RemittancesCaffee remittancesCaffeeId;

    public ContainerFilling() {
    }

    public ContainerFilling(Integer id) {
        this.id = id;
    }

    public ContainerFilling(Integer id, String containerBicCode) {
        this.id = id;
        this.containerBicCode = containerBicCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContainerBicCode() {
        return containerBicCode;
    }

    public void setContainerBicCode(String containerBicCode) {
        this.containerBicCode = containerBicCode;
    }

    public FractionPallet getFractionPalletId() {
        return fractionPalletId;
    }

    public void setFractionPalletId(FractionPallet fractionPalletId) {
        this.fractionPalletId = fractionPalletId;
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
        if (!(object instanceof ContainerFilling)) {
            return false;
        }
        ContainerFilling other = (ContainerFilling) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.ContainerFilling[ id=" + id + " ]";
    }
    
}
