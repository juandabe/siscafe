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
@Table(name = "shippers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Shippers.findAll", query = "SELECT s FROM Shippers s"),
    @NamedQuery(name = "Shippers.findById", query = "SELECT s FROM Shippers s WHERE s.id = :id"),
    @NamedQuery(name = "Shippers.findByBusinessName", query = "SELECT s FROM Shippers s WHERE s.businessName = :businessName"),
    @NamedQuery(name = "Shippers.findByCreatedDate", query = "SELECT s FROM Shippers s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "Shippers.findByUpdatedDate", query = "SELECT s FROM Shippers s WHERE s.updatedDate = :updatedDate"),
    @NamedQuery(name = "Shippers.findByDescription", query = "SELECT s FROM Shippers s WHERE s.description = :description")})
public class Shippers implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "business_name")
    private String businessName;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "shippersId")
    private List<RemittancesCaffee> remittancesCaffeeList;

    public Shippers() {
    }

    public Shippers(Integer id) {
        this.id = id;
    }

    public Shippers(Integer id, String businessName, Date createdDate, Date updatedDate, String description) {
        this.id = id;
        this.businessName = businessName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
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
        if (!(object instanceof Shippers)) {
            return false;
        }
        Shippers other = (Shippers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return businessName;
    }
    
}
