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
@Table(name = "sessions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sessions.findAll", query = "SELECT s FROM Sessions s"),
    @NamedQuery(name = "Sessions.findById", query = "SELECT s FROM Sessions s WHERE s.id = :id"),
    @NamedQuery(name = "Sessions.findByHash", query = "SELECT s FROM Sessions s WHERE s.hash = :hash"),
    @NamedQuery(name = "Sessions.findByLastAccess", query = "SELECT s FROM Sessions s WHERE s.lastAccess = :lastAccess"),
    @NamedQuery(name = "Sessions.findByIpRemote", query = "SELECT s FROM Sessions s WHERE s.ipRemote = :ipRemote"),
    @NamedQuery(name = "Sessions.findByActive", query = "SELECT s FROM Sessions s WHERE s.active = :active")})
public class Sessions implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "hash")
    private String hash;
    @Basic(optional = false)
    @Column(name = "last_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Basic(optional = false)
    @Column(name = "ip_remote")
    private String ipRemote;
    @Basic(optional = false)
    @Column(name = "active")
    private boolean active;
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users usersId;

    public Sessions() {
    }

    public Sessions(Integer id) {
        this.id = id;
    }

    public Sessions(Integer id, String hash, Date lastAccess, String ipRemote, boolean active) {
        this.id = id;
        this.hash = hash;
        this.lastAccess = lastAccess;
        this.ipRemote = ipRemote;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getIpRemote() {
        return ipRemote;
    }

    public void setIpRemote(String ipRemote) {
        this.ipRemote = ipRemote;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Users getUsersId() {
        return usersId;
    }

    public void setUsersId(Users usersId) {
        this.usersId = usersId;
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
        if (!(object instanceof Sessions)) {
            return false;
        }
        Sessions other = (Sessions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.Sessions[ id=" + id + " ]";
    }
    
}
