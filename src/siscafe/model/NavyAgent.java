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
@Table(name = "navy_agent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NavyAgent.findAll", query = "SELECT n FROM NavyAgent n"),
    @NamedQuery(name = "NavyAgent.findById", query = "SELECT n FROM NavyAgent n WHERE n.id = :id"),
    @NamedQuery(name = "NavyAgent.findByName", query = "SELECT n FROM NavyAgent n WHERE n.name = :name"),
    @NamedQuery(name = "NavyAgent.findByCreatedDate", query = "SELECT n FROM NavyAgent n WHERE n.createdDate = :createdDate"),
    @NamedQuery(name = "NavyAgent.findByUpdatedDate", query = "SELECT n FROM NavyAgent n WHERE n.updatedDate = :updatedDate")})
public class NavyAgent implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "navyAgentId")
    private List<PackagingCaffee> packagingCaffeeList;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
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

    public NavyAgent() {
    }

    public NavyAgent(Integer id) {
        this.id = id;
    }

    public NavyAgent(Integer id, String name, Date createdDate, Date updatedDate) {
        this.id = id;
        this.name = name;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NavyAgent)) {
            return false;
        }
        NavyAgent other = (NavyAgent) object;
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
    public List<PackagingCaffee> getPackagingCaffeeList() {
        return packagingCaffeeList;
    }

    public void setPackagingCaffeeList(List<PackagingCaffee> packagingCaffeeList) {
        this.packagingCaffeeList = packagingCaffeeList;
    }
    
}
