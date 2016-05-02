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
@Table(name = "items_services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemsServices.findAll", query = "SELECT i FROM ItemsServices i"),
    @NamedQuery(name = "ItemsServices.findById", query = "SELECT i FROM ItemsServices i WHERE i.id = :id"),
    @NamedQuery(name = "ItemsServices.findByUnoeeRef", query = "SELECT i FROM ItemsServices i WHERE i.unoeeRef = :unoeeRef"),
    @NamedQuery(name = "ItemsServices.findByServicesName", query = "SELECT i FROM ItemsServices i WHERE i.servicesName = :servicesName"),
    @NamedQuery(name = "ItemsServices.findByCreatedDate", query = "SELECT i FROM ItemsServices i WHERE i.createdDate = :createdDate"),
    @NamedQuery(name = "ItemsServices.findByUpdatedDate", query = "SELECT i FROM ItemsServices i WHERE i.updatedDate = :updatedDate")})
public class ItemsServices implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "unoee_ref")
    private int unoeeRef;
    @Basic(optional = false)
    @Column(name = "services_name")
    private String servicesName;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "itemsServicesId")
    private List<RelatedServices> relatedServicesList;

    public ItemsServices() {
    }

    public ItemsServices(Integer id) {
        this.id = id;
    }

    public ItemsServices(Integer id, int unoeeRef, String servicesName, Date createdDate, Date updatedDate) {
        this.id = id;
        this.unoeeRef = unoeeRef;
        this.servicesName = servicesName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUnoeeRef() {
        return unoeeRef;
    }

    public void setUnoeeRef(int unoeeRef) {
        this.unoeeRef = unoeeRef;
    }

    public String getServicesName() {
        return servicesName;
    }

    public void setServicesName(String servicesName) {
        this.servicesName = servicesName;
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
    public List<RelatedServices> getRelatedServicesList() {
        return relatedServicesList;
    }

    public void setRelatedServicesList(List<RelatedServices> relatedServicesList) {
        this.relatedServicesList = relatedServicesList;
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
        if (!(object instanceof ItemsServices)) {
            return false;
        }
        ItemsServices other = (ItemsServices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.ItemsServices[ id=" + id + " ]";
    }
    
}
