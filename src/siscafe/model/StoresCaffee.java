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
@Table(name = "stores_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StoresCaffee.findAll", query = "SELECT s FROM StoresCaffee s"),
    @NamedQuery(name = "StoresCaffee.findById", query = "SELECT s FROM StoresCaffee s WHERE s.id = :id"),
    @NamedQuery(name = "StoresCaffee.findByStoreName", query = "SELECT s FROM StoresCaffee s WHERE s.storeName = :storeName"),
    @NamedQuery(name = "StoresCaffee.findByCityLocation", query = "SELECT s FROM StoresCaffee s WHERE s.cityLocation = :cityLocation"),
    @NamedQuery(name = "StoresCaffee.findByCreatedDate", query = "SELECT s FROM StoresCaffee s WHERE s.createdDate = :createdDate"),
    @NamedQuery(name = "StoresCaffee.findByUpdatedDate", query = "SELECT s FROM StoresCaffee s WHERE s.updatedDate = :updatedDate")})
public class StoresCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "store_name")
    private String storeName;
    @Column(name = "city_location")
    private String cityLocation;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "storesCaffeeId")
    private List<SlotStore> slotStoreList;

    public StoresCaffee() {
    }

    public StoresCaffee(Integer id) {
        this.id = id;
    }

    public StoresCaffee(Integer id, String storeName, Date createdDate, Date updatedDate) {
        this.id = id;
        this.storeName = storeName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
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
    public List<SlotStore> getSlotStoreList() {
        return slotStoreList;
    }

    public void setSlotStoreList(List<SlotStore> slotStoreList) {
        this.slotStoreList = slotStoreList;
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
        if (!(object instanceof StoresCaffee)) {
            return false;
        }
        StoresCaffee other = (StoresCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return storeName;
    }
    
}
