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
@Table(name = "weighing_download_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WeighingDownloadCaffee.findAll", query = "SELECT w FROM WeighingDownloadCaffee w"),
    @NamedQuery(name = "WeighingDownloadCaffee.findById", query = "SELECT w FROM WeighingDownloadCaffee w WHERE w.id = :id"),
    @NamedQuery(name = "WeighingDownloadCaffee.findByWeightPallet", query = "SELECT w FROM WeighingDownloadCaffee w WHERE w.weightPallet = :weightPallet"),
    @NamedQuery(name = "WeighingDownloadCaffee.findByQuantityBagPallet", query = "SELECT w FROM WeighingDownloadCaffee w WHERE w.quantityBagPallet = :quantityBagPallet"),
    @NamedQuery(name = "WeighingDownloadCaffee.findByWeighingDate", query = "SELECT w FROM WeighingDownloadCaffee w WHERE w.weighingDate = :weighingDate")})
public class WeighingDownloadCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "seq_weight_pallet")
    private Integer seqWeightPallet;
    @Column(name = "weight_pallet")
    private double weightPallet;
    @Basic(optional = false)
    @Column(name = "quantity_bag_pallet")
    private int quantityBagPallet;
    @Basic(optional = false)
    @Column(name = "weighing_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weighingDate;
    @JoinColumn(name = "remittances_caffee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCaffeeId;

    public WeighingDownloadCaffee() {
    }

    public WeighingDownloadCaffee(Integer id) {
        this.id = id;
    }

    public Integer getSeqWeightPallet() {
        return seqWeightPallet;
    }

    public void setSeqWeightPallet(Integer seqWeightPallet) {
        this.seqWeightPallet = seqWeightPallet;
    }

    public WeighingDownloadCaffee(Integer id, double weightPallet, int quantityBagPallet, Date weighingDate) {
        this.id = id;
        this.weightPallet = weightPallet;
        this.quantityBagPallet = quantityBagPallet;
        this.weighingDate = weighingDate;
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
        if (!(object instanceof WeighingDownloadCaffee)) {
            return false;
        }
        WeighingDownloadCaffee other = (WeighingDownloadCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.WeighingDownloadCaffee[ id=" + id + " ]";
    }
    
}
