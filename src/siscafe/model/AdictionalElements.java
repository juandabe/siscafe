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
@Table(name = "adictional_elements")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdictionalElements.findAll", query = "SELECT a FROM AdictionalElements a"),
    @NamedQuery(name = "AdictionalElements.findById", query = "SELECT a FROM AdictionalElements a WHERE a.id = :id"),
    @NamedQuery(name = "AdictionalElements.findByCodeExt", query = "SELECT a FROM AdictionalElements a WHERE a.codeExt = :codeExt"),
    @NamedQuery(name = "AdictionalElements.findByName", query = "SELECT a FROM AdictionalElements a WHERE a.name = :name"),
    @NamedQuery(name = "AdictionalElements.findByDescription", query = "SELECT a FROM AdictionalElements a WHERE a.description = :description"),
    @NamedQuery(name = "AdictionalElements.findByCreatedDate", query = "SELECT a FROM AdictionalElements a WHERE a.createdDate = :createdDate"),
    @NamedQuery(name = "AdictionalElements.findByUpdatedDate", query = "SELECT a FROM AdictionalElements a WHERE a.updatedDate = :updatedDate")})
public class AdictionalElements implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code_ext")
    private String codeExt;
    @Basic(optional = false)
    @Column(name = "name")
    private String name;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "adictionalElements")
    private List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeList;

    public AdictionalElements() {
    }

    public AdictionalElements(Integer id) {
        this.id = id;
    }

    public AdictionalElements(Integer id, String name, Date createdDate, Date updatedDate) {
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

    public String getCodeExt() {
        return codeExt;
    }

    public void setCodeExt(String codeExt) {
        this.codeExt = codeExt;
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
    public List<AdictionalElementsHasPackagingCaffee> getAdictionalElementsHasPackagingCaffeeList() {
        return adictionalElementsHasPackagingCaffeeList;
    }

    public void setAdictionalElementsHasPackagingCaffeeList(List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeList) {
        this.adictionalElementsHasPackagingCaffeeList = adictionalElementsHasPackagingCaffeeList;
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
        if (!(object instanceof AdictionalElements)) {
            return false;
        }
        AdictionalElements other = (AdictionalElements) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.AdictionalElements[ id=" + id + " ]";
    }
    
}
