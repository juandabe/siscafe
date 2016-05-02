/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrador
 */
@Entity
@Table(name = "packaging_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PackagingCaffee.findAll", query = "SELECT p FROM PackagingCaffee p"),
    @NamedQuery(name = "PackagingCaffee.findById", query = "SELECT p FROM PackagingCaffee p WHERE p.id = :id"),
    @NamedQuery(name = "PackagingCaffee.findByContainerBicCode", query = "SELECT p FROM PackagingCaffee p WHERE p.containerBicCode = :containerBicCode"),
    @NamedQuery(name = "PackagingCaffee.findByPackagingType", query = "SELECT p FROM PackagingCaffee p WHERE p.packagingType = :packagingType"),
    @NamedQuery(name = "PackagingCaffee.findByExportStatement", query = "SELECT p FROM PackagingCaffee p WHERE p.exportStatement = :exportStatement")})
public class PackagingCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "container_bic_code")
    private String containerBicCode;
    @Basic(optional = false)
    @Column(name = "packaging_type")
    private String packagingType;
    @Basic(optional = false)
    @Column(name = "export_statement")
    private String exportStatement;
    @JoinColumn(name = "remittances_cafee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private RemittancesCaffee remittancesCafeeId;
    @JoinColumn(name = "type_container_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TypeContainer typeContainerId;
    @JoinColumn(name = "customs_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customs customsId;

    public PackagingCaffee() {
    }

    public PackagingCaffee(Integer id) {
        this.id = id;
    }

    public PackagingCaffee(Integer id, String containerBicCode, String packagingType, String exportStatement) {
        this.id = id;
        this.containerBicCode = containerBicCode;
        this.packagingType = packagingType;
        this.exportStatement = exportStatement;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContainerBicCode() {
        return containerBicCode;
    }

    public void setContainerBicCode(String containerBicCode) {
        this.containerBicCode = containerBicCode;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public void setPackagingType(String packagingType) {
        this.packagingType = packagingType;
    }

    public String getExportStatement() {
        return exportStatement;
    }

    public void setExportStatement(String exportStatement) {
        this.exportStatement = exportStatement;
    }

    public RemittancesCaffee getRemittancesCafeeId() {
        return remittancesCafeeId;
    }

    public void setRemittancesCafeeId(RemittancesCaffee remittancesCafeeId) {
        this.remittancesCafeeId = remittancesCafeeId;
    }

    public TypeContainer getTypeContainerId() {
        return typeContainerId;
    }

    public void setTypeContainerId(TypeContainer typeContainerId) {
        this.typeContainerId = typeContainerId;
    }

    public Customs getCustomsId() {
        return customsId;
    }

    public void setCustomsId(Customs customsId) {
        this.customsId = customsId;
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
        if (!(object instanceof PackagingCaffee)) {
            return false;
        }
        PackagingCaffee other = (PackagingCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.PackagingCaffee[ id=" + id + " ]";
    }
    
}
