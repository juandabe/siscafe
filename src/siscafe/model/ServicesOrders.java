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
@Table(name = "services_orders")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServicesOrders.findAll", query = "SELECT s FROM ServicesOrders s"),
    @NamedQuery(name = "ServicesOrders.findById", query = "SELECT s FROM ServicesOrders s WHERE s.id = :id"),
    @NamedQuery(name = "ServicesOrders.findByCreatedDate", query = "SELECT s FROM ServicesOrders s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "ServicesOrders.findByClosedDate", query = "SELECT s FROM ServicesOrders s WHERE s.closedDate = :closedDate")})
public class ServicesOrders implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "closed_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date closedDate;
    @OneToMany(mappedBy = "servicesOrdersId")
    private List<RelatedServices> relatedServicesList;

    public ServicesOrders() {
    }

    public ServicesOrders(Integer id) {
        this.id = id;
    }

    public ServicesOrders(Integer id, Date createdDate, Date closedDate) {
        this.id = id;
        this.createdDate = createdDate;
        this.closedDate = closedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
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
        if (!(object instanceof ServicesOrders)) {
            return false;
        }
        ServicesOrders other = (ServicesOrders) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.ServicesOrders[ id=" + id + " ]";
    }
    
}
