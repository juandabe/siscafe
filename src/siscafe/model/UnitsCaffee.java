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
@Table(name = "units_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnitsCaffee.findAll", query = "SELECT u FROM UnitsCaffee u"),
    @NamedQuery(name = "UnitsCaffee.findById", query = "SELECT u FROM UnitsCaffee u WHERE u.id = :id"),
    @NamedQuery(name = "UnitsCaffee.findByNameUnit", query = "SELECT u FROM UnitsCaffee u WHERE u.nameUnit = :nameUnit"),
    @NamedQuery(name = "UnitsCaffee.findByQuantity", query = "SELECT u FROM UnitsCaffee u WHERE u.quantity = :quantity"),
    @NamedQuery(name = "UnitsCaffee.findByCreatedDate", query = "SELECT u FROM UnitsCaffee u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "UnitsCaffee.findByUpdatedDate", query = "SELECT u FROM UnitsCaffee u WHERE u.updatedDate = :updatedDate")})
public class UnitsCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name_unit")
    private String nameUnit;
    @Basic(optional = false)
    @Column(name = "quantity")
    private double quantity;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "unitsCafeeId")
    private List<RemittancesCaffee> remittancesCaffeeList;
    @JoinColumn(name = "type_units_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TypeUnits typeUnitsId;

    public UnitsCaffee() {
    }

    public UnitsCaffee(Integer id) {
        this.id = id;
    }

    public UnitsCaffee(Integer id, String nameUnit, double quantity, Date createdDate, Date updatedDate) {
        this.id = id;
        this.nameUnit = nameUnit;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameUnit() {
        return nameUnit;
    }

    public void setNameUnit(String nameUnit) {
        this.nameUnit = nameUnit;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
    }

    public TypeUnits getTypeUnitsId() {
        return typeUnitsId;
    }

    public void setTypeUnitsId(TypeUnits typeUnitsId) {
        this.typeUnitsId = typeUnitsId;
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
        if (!(object instanceof UnitsCaffee)) {
            return false;
        }
        UnitsCaffee other = (UnitsCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nameUnit;
    }
    
}
