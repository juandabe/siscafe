/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "fraction_pallet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FractionPallet.findAll", query = "SELECT f FROM FractionPallet f"),
    @NamedQuery(name = "FractionPallet.findById", query = "SELECT f FROM FractionPallet f WHERE f.id = :id"),
    @NamedQuery(name = "FractionPallet.findByQuantityBags", query = "SELECT f FROM FractionPallet f WHERE f.quantityBags = :quantityBags"),
    @NamedQuery(name = "FractionPallet.findByCreatedDate", query = "SELECT f FROM FractionPallet f WHERE f.createdDate = :createdDate")})
public class FractionPallet implements Serializable {
    @OneToMany(mappedBy = "fractionPalletId")
    private List<ContainerFilling> containerFillingList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "quantity_bags")
    private Integer quantityBags;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "weighing_packaging_caffee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private WeighingPackagingCaffee weighingPackagingCaffeeId;

    public FractionPallet() {
    }

    public FractionPallet(Integer id) {
        this.id = id;
    }

    public FractionPallet(Integer id, Date createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantityBags() {
        return quantityBags;
    }

    public void setQuantityBags(Integer quantityBags) {
        this.quantityBags = quantityBags;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public WeighingPackagingCaffee getWeighingPackagingCaffeeId() {
        return weighingPackagingCaffeeId;
    }

    public void setWeighingPackagingCaffeeId(WeighingPackagingCaffee weighingPackagingCaffeeId) {
        this.weighingPackagingCaffeeId = weighingPackagingCaffeeId;
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
        if (!(object instanceof FractionPallet)) {
            return false;
        }
        FractionPallet other = (FractionPallet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.temp.FractionPallet[ id=" + id + " ]";
    }

    @XmlTransient
    public List<ContainerFilling> getContainerFillingList() {
        return containerFillingList;
    }

    public void setContainerFillingList(List<ContainerFilling> containerFillingList) {
        this.containerFillingList = containerFillingList;
    }
    
}
