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
@Table(name = "weighing_packaging_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeighingPackagingCaffee.findAll", query = "SELECT w FROM WeighingPackagingCaffee w"),
    @NamedQuery(name = "WeighingPackagingCaffee.findById", query = "SELECT w FROM WeighingPackagingCaffee w WHERE w.id = :id"),
    @NamedQuery(name = "WeighingPackagingCaffee.findByWeightPallet", query = "SELECT w FROM WeighingPackagingCaffee w WHERE w.weightPallet = :weightPallet"),
    @NamedQuery(name = "WeighingPackagingCaffee.findByQuantityBagPallet", query = "SELECT w FROM WeighingPackagingCaffee w WHERE w.quantityBagPallet = :quantityBagPallet"),
    @NamedQuery(name = "WeighingPackagingCaffee.findByWeighingDate", query = "SELECT w FROM WeighingPackagingCaffee w WHERE w.weighingDate = :weighingDate")})
public class WeighingPackagingCaffee implements Serializable {
    @Basic(optional = false)
    @Column(name = "seq_weight_pallet")
    private int seqWeightPallet;
    @Basic(optional = false)
    @Column(name = "is_fraction")
    private boolean isFraction;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "weighingPackagingCaffeeId")
    private List<FractionPallet> fractionPalletList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "weight_pallet")
    private double weightPallet;
    @Basic(optional = false)
    @Column(name = "quantity_bag_pallet")
    private int quantityBagPallet;
    @Basic(optional = false)
    @Column(name = "weighing_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weighingDate;
    @JoinColumn(name = "remittances_cafee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCafeeId;

    public WeighingPackagingCaffee() {
    }

    public WeighingPackagingCaffee(Integer id) {
        this.id = id;
    }

    public WeighingPackagingCaffee(Integer id, double weightPallet, int quantityBagPallet, Date weighingDate) {
        this.id = id;
        this.weightPallet = weightPallet;
        this.quantityBagPallet = quantityBagPallet;
        this.weighingDate = weighingDate;
    }
    
    public Integer getSeqWeightPallet() {
        return seqWeightPallet;
    }

    public void setSeqWeightPallet(Integer seqWeightPallet) {
        this.seqWeightPallet = seqWeightPallet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getWeightPallet() {
        return weightPallet;
    }

    public void setWeightPallet(double weightPallet) {
        this.weightPallet = weightPallet;
    }

    public int getQuantityBagPallet() {
        return quantityBagPallet;
    }

    public void setQuantityBagPallet(int quantityBagPallet) {
        this.quantityBagPallet = quantityBagPallet;
    }

    public Date getWeighingDate() {
        return weighingDate;
    }

    public void setWeighingDate(Date weighingDate) {
        this.weighingDate = weighingDate;
    }

    public RemittancesCaffee getRemittancesCafeeId() {
        return remittancesCafeeId;
    }

    public void setRemittancesCafeeId(RemittancesCaffee remittancesCafeeId) {
        this.remittancesCafeeId = remittancesCafeeId;
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
        if (!(object instanceof WeighingPackagingCaffee)) {
            return false;
        }
        WeighingPackagingCaffee other = (WeighingPackagingCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.WeighingPackagingCaffee[ id=" + id + " ]";
    }

    public boolean getIsFraction() {
        return isFraction;
    }

    public void setIsFraction(boolean isFraction) {
        this.isFraction = isFraction;
    }

    @XmlTransient
    public List<FractionPallet> getFractionPalletList() {
        return fractionPalletList;
    }

    public void setFractionPalletList(List<FractionPallet> fractionPalletList) {
        this.fractionPalletList = fractionPalletList;
    }

    public void setSeqWeightPallet(int seqWeightPallet) {
        this.seqWeightPallet = seqWeightPallet;
    }
    
}
