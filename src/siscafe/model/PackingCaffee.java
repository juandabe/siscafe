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
@Table(name = "packing_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PackingCaffee.findAll", query = "SELECT p FROM PackingCaffee p"),
    @NamedQuery(name = "PackingCaffee.findById", query = "SELECT p FROM PackingCaffee p WHERE p.id = :id"),
    @NamedQuery(name = "PackingCaffee.findByName", query = "SELECT p FROM PackingCaffee p WHERE p.name = :name"),
    @NamedQuery(name = "PackingCaffee.findByDescription", query = "SELECT p FROM PackingCaffee p WHERE p.description = :description"),
    @NamedQuery(name = "PackingCaffee.findByWeight", query = "SELECT p FROM PackingCaffee p WHERE p.weight = :weight"),
    @NamedQuery(name = "PackingCaffee.findByDateCreated", query = "SELECT p FROM PackingCaffee p WHERE p.dateCreated = :dateCreated"),
    @NamedQuery(name = "PackingCaffee.findByDateUpdated", query = "SELECT p FROM PackingCaffee p WHERE p.dateUpdated = :dateUpdated")})
public class PackingCaffee implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packingCafeeId")
    private List<RemittancesCaffee> remittancesCaffeeList;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @Column(name = "weight")
    private double weight;
    @Basic(optional = false)
    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;
    @Basic(optional = false)
    @Column(name = "date_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdated;

    public PackingCaffee() {
    }

    public PackingCaffee(Integer id) {
        this.id = id;
    }

    public PackingCaffee(Integer id, String name, double weight, Date dateCreated, Date dateUpdated) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
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
        if (!(object instanceof PackingCaffee)) {
            return false;
        }
        PackingCaffee other = (PackingCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
    }
    
}
