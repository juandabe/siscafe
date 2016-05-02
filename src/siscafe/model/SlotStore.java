/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "slot_store")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SlotStore.findAll", query = "SELECT s FROM SlotStore s"),
    @NamedQuery(name = "SlotStore.findById", query = "SELECT s FROM SlotStore s WHERE s.id = :id"),
    @NamedQuery(name = "SlotStore.findByNameSpace", query = "SELECT s FROM SlotStore s WHERE s.nameSpace = :nameSpace")})
public class SlotStore implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "name_space")
    private String nameSpace;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "slotStoreId")
    private List<RemittancesCaffee> remittancesCaffeeList;
    @JoinColumn(name = "stores_caffee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StoresCaffee storesCaffeeId;

    public SlotStore() {
    }

    public SlotStore(Integer id) {
        this.id = id;
    }

    public SlotStore(Integer id, String nameSpace) {
        this.id = id;
        this.nameSpace = nameSpace;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
    }

    public StoresCaffee getStoresCaffeeId() {
        return storesCaffeeId;
    }

    public void setStoresCaffeeId(StoresCaffee storesCaffeeId) {
        this.storesCaffeeId = storesCaffeeId;
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
        if (!(object instanceof SlotStore)) {
            return false;
        }
        SlotStore other = (SlotStore) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nameSpace;
    }
    
}
