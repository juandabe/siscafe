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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

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
    @NamedQuery(name = "PackagingCaffee.findByPackagingType", query = "SELECT p FROM PackagingCaffee p WHERE p.packagingType = :packagingType"),
    @NamedQuery(name = "PackagingCaffee.findByExportStatement", query = "SELECT p FROM PackagingCaffee p WHERE p.exportStatement = :exportStatement")})
public class PackagingCaffee implements Serializable {

    @Basic(optional = false)
    @Column(name = "weight_to_out")
    private boolean weightToOut;
    @Basic(optional = false)
    @Column(name = "booking_expo")
    private String bookingExpo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packagingCaffee")
    private List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeList;
    @JoinColumn(name = "state_packaging_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private StatePackaging statePackagingId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packagingCaffee")
    private List<DetailPackagingCaffee> detailPackagingCaffeeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "packagingCaffeeId")
    private List<RemittancesCaffee> remittancesCaffeeList;
    @JoinColumn(name = "navy_agent_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private NavyAgent navyAgentId;
    @Basic(optional = false)
    @Column(name = "motor_ship_name")
    private String motorShipId;
    @JoinColumn(name = "shipping_lines_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ShippingLines shippingLinesId;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "iso_container")
    private String isoCtn;
    @Basic(optional = false)
    @Column(name = "packaging_mode")
    private String packagingMode;
    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate  ;
    @Basic(optional = false)
    @Column(name = "packaging_type")
    private String packagingType;
    @Basic(optional = false)
    @Column(name = "bic_container")
    private String bicContainer;
    @Basic(optional = false)
    @Column(name = "export_statement")
    private String exportStatement;
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

    public PackagingCaffee(Integer id, String packagingType, String exportStatement) {
        this.id = id;
        this.packagingType = packagingType;
        this.exportStatement = exportStatement;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getPackagingMode() {
        return packagingMode;
    }  
        
    public void setPackagingMode(String packagingMode) {
        this.packagingMode = packagingMode;
    }
    public String getBicContainer() {
        return bicContainer;
    }
    public void setBigContainer(String bicContainer) {
        this.bicContainer = bicContainer;
    }

    public Integer getId() {
        return id;
    }

    public String getMotorShipName() {
        return motorShipId;
    }

    public void setMotorShipName(String motorShipId) {
        this.motorShipId = motorShipId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsoCtn() {
        return isoCtn;
    }

    public void setIsoCtn(String isoCtn) {
        this.isoCtn = isoCtn;
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

    public NavyAgent getNavyAgentId() {
        return navyAgentId;
    }

    public void setNavyAgentId(NavyAgent navyAgentId) {
        this.navyAgentId = navyAgentId;
    }

    public ShippingLines getShippingLinesId() {
        return shippingLinesId;
    }

    public void setShippingLinesId(ShippingLines shippingLinesId) {
        this.shippingLinesId = shippingLinesId;
    }

    @XmlTransient
    public List<RemittancesCaffee> getRemittancesCaffeeList() {
        return remittancesCaffeeList;
    }

    public void setRemittancesCaffeeList(List<RemittancesCaffee> remittancesCaffeeList) {
        this.remittancesCaffeeList = remittancesCaffeeList;
    }

    @XmlTransient
    public List<DetailPackagingCaffee> getDetailPackagingCaffeeList() {
        return detailPackagingCaffeeList;
    }

    public void setDetailPackagingCaffeeList(List<DetailPackagingCaffee> detailPackagingCaffeeList) {
        this.detailPackagingCaffeeList = detailPackagingCaffeeList;
    }

    public boolean getWeightToOut() {
        return weightToOut;
    }

    public void setWeightToOut(boolean weightToOut) {
        this.weightToOut = weightToOut;
    }

    public String getBookingExpo() {
        return bookingExpo;
    }

    public void setBookingExpo(String bookingExpo) {
        this.bookingExpo = bookingExpo;
    }

    @XmlTransient
    public List<AdictionalElementsHasPackagingCaffee> getAdictionalElementsHasPackagingCaffeeList() {
        return adictionalElementsHasPackagingCaffeeList;
    }

    public void setAdictionalElementsHasPackagingCaffeeList(List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeList) {
        this.adictionalElementsHasPackagingCaffeeList = adictionalElementsHasPackagingCaffeeList;
    }

    public StatePackaging getStatePackagingId() {
        return statePackagingId;
    }

    public void setStatePackagingId(StatePackaging statePackagingId) {
        this.statePackagingId = statePackagingId;
    }
    
}
