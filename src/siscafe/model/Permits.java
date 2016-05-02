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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "permits")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Permits.findAll", query = "SELECT p FROM Permits p"),
    @NamedQuery(name = "Permits.findById", query = "SELECT p FROM Permits p WHERE p.id = :id"),
    @NamedQuery(name = "Permits.findByName", query = "SELECT p FROM Permits p WHERE p.name = :name"),
    @NamedQuery(name = "Permits.findByDescription", query = "SELECT p FROM Permits p WHERE p.description = :description"),
    @NamedQuery(name = "Permits.findByCreatedDate", query = "SELECT p FROM Permits p WHERE p.createdDate = :createdDate"),
    @NamedQuery(name = "Permits.findByUpdatedDate", query = "SELECT p FROM Permits p WHERE p.updatedDate = :updatedDate")})
public class Permits implements Serializable {
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
    @JoinTable(name = "profiles_has_permits", joinColumns = {
        @JoinColumn(name = "permits_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "profiles_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Profiles> profilesList;
    @JoinColumn(name = "category_permits_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CategoryPermits categoryPermitsId;

    public Permits() {
    }

    public Permits(Integer id) {
        this.id = id;
    }

    public Permits(Integer id, String name, String description, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
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
    public List<Profiles> getProfilesList() {
        return profilesList;
    }

    public void setProfilesList(List<Profiles> profilesList) {
        this.profilesList = profilesList;
    }

    public CategoryPermits getCategoryPermitsId() {
        return categoryPermitsId;
    }

    public void setCategoryPermitsId(CategoryPermits categoryPermitsId) {
        this.categoryPermitsId = categoryPermitsId;
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
        if (!(object instanceof Permits)) {
            return false;
        }
        Permits other = (Permits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.Permits[ id=" + id + " ]";
    }
    
}
