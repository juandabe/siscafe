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
@Table(name = "type_units")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypeUnits.findAll", query = "SELECT t FROM TypeUnits t"),
    @NamedQuery(name = "TypeUnits.findById", query = "SELECT t FROM TypeUnits t WHERE t.id = :id"),
    @NamedQuery(name = "TypeUnits.findByName", query = "SELECT t FROM TypeUnits t WHERE t.name = :name"),
    @NamedQuery(name = "TypeUnits.findByProductName", query = "SELECT t FROM TypeUnits t WHERE t.productName = :productName"),
    @NamedQuery(name = "TypeUnits.findByDescription", query = "SELECT t FROM TypeUnits t WHERE t.description = :description"),
    @NamedQuery(name = "TypeUnits.findByCreatedDate", query = "SELECT t FROM TypeUnits t WHERE t.createdDate = :createdDate"),
    @NamedQuery(name = "TypeUnits.findByUpdatedDate", query = "SELECT t FROM TypeUnits t WHERE t.updatedDate = :updatedDate")})
public class TypeUnits implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "product_name")
    private String productName;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeUnitsId")
    private List<UnitsCaffee> unitsCaffeeList;

    public TypeUnits() {
    }

    public TypeUnits(Integer id) {
        this.id = id;
    }

    public TypeUnits(Integer id, String name, String productName, String description, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
        this.productName = productName;
        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public List<UnitsCaffee> getUnitsCaffeeList() {
        return unitsCaffeeList;
    }

    public void setUnitsCaffeeList(List<UnitsCaffee> unitsCaffeeList) {
        this.unitsCaffeeList = unitsCaffeeList;
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
        if (!(object instanceof TypeUnits)) {
            return false;
        }
        TypeUnits other = (TypeUnits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return productName;
    }
    
}
