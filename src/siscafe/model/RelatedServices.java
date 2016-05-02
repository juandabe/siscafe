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
@Table(name = "related_services")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RelatedServices.findAll", query = "SELECT r FROM RelatedServices r"),
    @NamedQuery(name = "RelatedServices.findById", query = "SELECT r FROM RelatedServices r WHERE r.id = :id"),
    @NamedQuery(name = "RelatedServices.findByQuantity", query = "SELECT r FROM RelatedServices r WHERE r.quantity = :quantity"),
    @NamedQuery(name = "RelatedServices.findByCompleted", query = "SELECT r FROM RelatedServices r WHERE r.completed = :completed"),
    @NamedQuery(name = "RelatedServices.findByCreatedDate", query = "SELECT r FROM RelatedServices r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "RelatedServices.findByUpdatedDate", query = "SELECT r FROM RelatedServices r WHERE r.updatedDate = :updatedDate")})
public class RelatedServices implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "quantity")
    private double quantity;
    @Basic(optional = false)
    @Column(name = "completed")
    private boolean completed;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @JoinColumn(name = "items_services_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ItemsServices itemsServicesId;
    @JoinColumn(name = "remittances_caffee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCaffeeId;
    @JoinColumn(name = "services_orders_id", referencedColumnName = "id")
    @ManyToOne
    private ServicesOrders servicesOrdersId;

    public RelatedServices() {
    }

    public RelatedServices(Integer id) {
        this.id = id;
    }

    public RelatedServices(Integer id, double quantity, boolean completed, Date createdDate, Date updatedDate) {
        this.id = id;
        this.quantity = quantity;
        this.completed = completed;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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

    public ItemsServices getItemsServicesId() {
        return itemsServicesId;
    }

    public void setItemsServicesId(ItemsServices itemsServicesId) {
        this.itemsServicesId = itemsServicesId;
    }

    public RemittancesCaffee getRemittancesCaffeeId() {
        return remittancesCaffeeId;
    }

    public void setRemittancesCaffeeId(RemittancesCaffee remittancesCaffeeId) {
        this.remittancesCaffeeId = remittancesCaffeeId;
    }

    public ServicesOrders getServicesOrdersId() {
        return servicesOrdersId;
    }

    public void setServicesOrdersId(ServicesOrders servicesOrdersId) {
        this.servicesOrdersId = servicesOrdersId;
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
        if (!(object instanceof RelatedServices)) {
            return false;
        }
        RelatedServices other = (RelatedServices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.RelatedServices[ id=" + id + " ]";
    }
    
}
