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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "returns_caffees")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReturnsCaffees.findAll", query = "SELECT r FROM ReturnsCaffees r"),
    @NamedQuery(name = "ReturnsCaffees.findById", query = "SELECT r FROM ReturnsCaffees r WHERE r.id = :id"),
    @NamedQuery(name = "ReturnsCaffees.findByReturnDate", query = "SELECT r FROM ReturnsCaffees r WHERE r.returnDate = :returnDate"),
    @NamedQuery(name = "ReturnsCaffees.findByVehiclePlate", query = "SELECT r FROM ReturnsCaffees r WHERE r.vehiclePlate = :vehiclePlate")})
public class ReturnsCaffees implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "return_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;
    @Basic(optional = false)
    @Lob
    @Column(name = "observation")
    private String observation;
    @Basic(optional = false)
    @Column(name = "vehicle_plate")
    private String vehiclePlate;
    @JoinColumn(name = "remittances_cafee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCafeeId;

    public ReturnsCaffees() {
    }

    public ReturnsCaffees(Integer id) {
        this.id = id;
    }

    public ReturnsCaffees(Integer id, Date returnDate, String observation, String vehiclePlate) {
        this.id = id;
        this.returnDate = returnDate;
        this.observation = observation;
        this.vehiclePlate = vehiclePlate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public RemittancesCaffee getRemittancesCafeeId() {
        return remittancesCafeeId;
    }

    public void setRemittancesCafeeId(RemittancesCaffee remittancesCafeeId) {
        this.remittancesCafeeId = remittancesCafeeId;
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
        if (!(object instanceof ReturnsCaffees)) {
            return false;
        }
        ReturnsCaffees other = (ReturnsCaffees) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.ReturnsCaffees[ id=" + id + " ]";
    }
    
}
