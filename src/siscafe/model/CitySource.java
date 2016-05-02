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
@Table(name = "city_source")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CitySource.findAll", query = "SELECT c FROM CitySource c"),
    @NamedQuery(name = "CitySource.findById", query = "SELECT c FROM CitySource c WHERE c.id = :id"),
    @NamedQuery(name = "CitySource.findByCityName", query = "SELECT c FROM CitySource c WHERE c.cityName = :cityName"),
    @NamedQuery(name = "CitySource.findByCountryName", query = "SELECT c FROM CitySource c WHERE c.countryName = :countryName"),
    @NamedQuery(name = "CitySource.findByCreatedDate", query = "SELECT c FROM CitySource c WHERE c.createdDate = :createdDate"),
    @NamedQuery(name = "CitySource.findByUpdatedDate", query = "SELECT c FROM CitySource c WHERE c.updatedDate = :updatedDate")})
public class CitySource implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "city_name")
    private String cityName;
    @Basic(optional = false)
    @Column(name = "country_name")
    private String countryName;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @OneToMany(mappedBy = "citySourceId")
    private List<RemittancesCaffee> remittancesCaffeeList;

    public CitySource() {
    }

    public CitySource(Integer id) {
        this.id = id;
    }

    public CitySource(Integer id, String countryName, Date createdDate, Date updatedDate) {
        this.id = id;
        this.countryName = countryName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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
        if (!(object instanceof CitySource)) {
            return false;
        }
        CitySource other = (CitySource) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.CitySource[ id=" + id + " ]";
    }
    
}
