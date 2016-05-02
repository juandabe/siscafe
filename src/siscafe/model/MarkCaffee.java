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
@Table(name = "mark_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MarkCaffee.findAll", query = "SELECT m FROM MarkCaffee m"),
    @NamedQuery(name = "MarkCaffee.findById", query = "SELECT m FROM MarkCaffee m WHERE m.id = :id"),
    @NamedQuery(name = "MarkCaffee.findByName", query = "SELECT m FROM MarkCaffee m WHERE m.name = :name"),
    @NamedQuery(name = "MarkCaffee.findByDescription", query = "SELECT m FROM MarkCaffee m WHERE m.description = :description"),
    @NamedQuery(name = "MarkCaffee.findByCreatedDate", query = "SELECT m FROM MarkCaffee m WHERE m.createdDate = :createdDate"),
    @NamedQuery(name = "MarkCaffee.findByUpdatedDate", query = "SELECT m FROM MarkCaffee m WHERE m.updatedDate = :updatedDate")})
public class MarkCaffee implements Serializable {
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
    @OneToMany(mappedBy = "markCafeeId")
    private List<RemittancesCaffee> remittancesCaffeeList;

    public MarkCaffee() {
    }

    public MarkCaffee(Integer id) {
        this.id = id;
    }

    public MarkCaffee(Integer id, String name, String description, Date createdDate, Date updatedDate) {
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
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
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
        if (!(object instanceof MarkCaffee)) {
            return false;
        }
        MarkCaffee other = (MarkCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.MarkCaffee[ id=" + id + " ]";
    }
    
}
