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
@Table(name = "shipping_lines")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShippingLines.findAll", query = "SELECT s FROM ShippingLines s"),
    @NamedQuery(name = "ShippingLines.findById", query = "SELECT s FROM ShippingLines s WHERE s.id = :id"),
    @NamedQuery(name = "ShippingLines.findByBusinessName", query = "SELECT s FROM ShippingLines s WHERE s.businessName = :businessName"),
    @NamedQuery(name = "ShippingLines.findByDescription", query = "SELECT s FROM ShippingLines s WHERE s.description = :description"),
    @NamedQuery(name = "ShippingLines.findByCreatedDate", query = "SELECT s FROM ShippingLines s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "ShippingLines.findByUpdatedDate", query = "SELECT s FROM ShippingLines s WHERE s.updatedDate = :updatedDate")})
public class ShippingLines implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shippingLinesId")
    private List<PackagingCaffee> packagingCaffeeList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "business_name")
    private String businessName;
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

    public ShippingLines() {
    }

    public ShippingLines(Integer id) {
        this.id = id;
    }

    public ShippingLines(Integer id, String businessName, Date createdDate, Date updatedDate) {
        this.id = id;
        this.businessName = businessName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShippingLines)) {
            return false;
        }
        ShippingLines other = (ShippingLines) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return businessName;
    }

    @XmlTransient
    public List<PackagingCaffee> getPackagingCaffeeList() {
        return packagingCaffeeList;
    }

    public void setPackagingCaffeeList(List<PackagingCaffee> packagingCaffeeList) {
        this.packagingCaffeeList = packagingCaffeeList;
    }
    
}
