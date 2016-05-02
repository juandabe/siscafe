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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "Users.findByPhone", query = "SELECT u FROM Users u WHERE u.phone = :phone"),
    @NamedQuery(name = "Users.findByExtension", query = "SELECT u FROM Users u WHERE u.extension = :extension"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByCreatedDate", query = "SELECT u FROM Users u WHERE u.createdDate = :createdDate"),
    @NamedQuery(name = "Users.findByUpdatedDate", query = "SELECT u FROM Users u WHERE u.updatedDate = :updatedDate"),
    @NamedQuery(name = "Users.findByActive", query = "SELECT u FROM Users u WHERE u.active = :active")})
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "first_name")
    private String firstName;
    @Basic(optional = false)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @Column(name = "phone")
    private String phone;
    @Column(name = "extension")
    private String extension;
    @Basic(optional = false)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staffSampleId")
    private List<RemittancesCaffee> remittancesCaffeeList;
    @OneToMany(mappedBy = "staffWtInId")
    private List<RemittancesCaffee> remittancesCaffeeList1;
    @OneToMany(mappedBy = "staffWtOutId")
    private List<RemittancesCaffee> remittancesCaffeeList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "staffDriverId")
    private List<RemittancesCaffee> remittancesCaffeeList3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usersId")
    private List<Sessions> sessionsList;
    @JoinColumn(name = "departaments_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Departaments departamentsId;
    @JoinColumn(name = "profiles_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Profiles profilesId;

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public Users(Integer id, String firstName, String lastName, String phone, String username, String password, Date createdDate, Date updatedDate, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList1() {
        return remittancesCaffeeList1;
    }

    public void setRemittancesCaffeeList1(List<RemittancesCaffee> remittancesCaffeeList1) {
        this.remittancesCaffeeList1 = remittancesCaffeeList1;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList2() {
        return remittancesCaffeeList2;
    }

    public void setRemittancesCaffeeList2(List<RemittancesCaffee> remittancesCaffeeList2) {
        this.remittancesCaffeeList2 = remittancesCaffeeList2;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList3() {
        return remittancesCaffeeList3;
    }

    public void setRemittancesCaffeeList3(List<RemittancesCaffee> remittancesCaffeeList3) {
        this.remittancesCaffeeList3 = remittancesCaffeeList3;
    }

    @XmlTransient
    public List<Sessions> getSessionsList() {
        return sessionsList;
    }

    public void setSessionsList(List<Sessions> sessionsList) {
        this.sessionsList = sessionsList;
    }

    public Departaments getDepartamentsId() {
        return departamentsId;
    }

    public void setDepartamentsId(Departaments departamentsId) {
        this.departamentsId = departamentsId;
    }

    public Profiles getProfilesId() {
        return profilesId;
    }

    public void setProfilesId(Profiles profilesId) {
        this.profilesId = profilesId;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
    
}
