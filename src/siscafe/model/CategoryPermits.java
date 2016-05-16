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
@Table(name = "category_permits")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoryPermits.findAll", query = "SELECT c FROM CategoryPermits c"),
    @NamedQuery(name = "CategoryPermits.findById", query = "SELECT c FROM CategoryPermits c WHERE c.id = :id"),
    @NamedQuery(name = "CategoryPermits.findByName", query = "SELECT c FROM CategoryPermits c WHERE c.name = :name"),
    @NamedQuery(name = "CategoryPermits.findByCreatedDate", query = "SELECT c FROM CategoryPermits c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CategoryPermits.findByUpdatedDate", query = "SELECT c FROM CategoryPermits c WHERE c.updatedDate = :updatedDate")})
public class CategoryPermits implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoryPermitsId")
    private List<Permits> permitsList;

    public CategoryPermits() {
    }

    public CategoryPermits(Integer id) {
        this.id = id;
    }

    public CategoryPermits(Integer id, Date createdDate, Date updatedDate) {
        this.id = id;
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
    public List<Permits> getPermitsList() {
        return permitsList;
    }

    public void setPermitsList(List<Permits> permitsList) {
        this.permitsList = permitsList;
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
        if (!(object instanceof CategoryPermits)) {
            return false;
        }
        CategoryPermits other = (CategoryPermits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }
    
}
