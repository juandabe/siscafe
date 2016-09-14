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
@Table(name = "state_packaging")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "StatePackaging.findAll", query = "SELECT s FROM StatePackaging s"),
    @NamedQuery(name = "StatePackaging.findById", query = "SELECT s FROM StatePackaging s WHERE s.id = :id"),
    @NamedQuery(name = "StatePackaging.findByName", query = "SELECT s FROM StatePackaging s WHERE s.name = :name"),
    @NamedQuery(name = "StatePackaging.findByCreateDate", query = "SELECT s FROM StatePackaging s WHERE s.createDate = :createDate"),
    @NamedQuery(name = "StatePackaging.findByUpdateDate", query = "SELECT s FROM StatePackaging s WHERE s.updateDate = :updateDate")})
public class StatePackaging implements Serializable {

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
    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @Basic(optional = false)
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statePackagingId")
    private List<PackagingCaffee> packagingCaffeeList;

    public StatePackaging() {
    }

    public StatePackaging(Integer id) {
        this.id = id;
    }

    public StatePackaging(Integer id, String name, Date createDate, Date updateDate) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.updateDate = updateDate;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @XmlTransient
    public List<PackagingCaffee> getPackagingCaffeeList() {
        return packagingCaffeeList;
    }

    public void setPackagingCaffeeList(List<PackagingCaffee> packagingCaffeeList) {
        this.packagingCaffeeList = packagingCaffeeList;
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
        if (!(object instanceof StatePackaging)) {
            return false;
        }
        StatePackaging other = (StatePackaging) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.StatePackaging[ id=" + id + " ]";
    }
    
}
