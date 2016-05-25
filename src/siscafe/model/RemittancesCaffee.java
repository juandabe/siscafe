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
@Table(name = "remittances_caffee")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RemittancesCaffee.findAll", query = "SELECT r FROM RemittancesCaffee r"),
    @NamedQuery(name = "RemittancesCaffee.findById", query = "SELECT r FROM RemittancesCaffee r WHERE r.id = :id"),
    @NamedQuery(name = "RemittancesCaffee.findByLotCaffee", query = "SELECT r FROM RemittancesCaffee r WHERE r.lotCaffee = :lotCaffee"),
    @NamedQuery(name = "RemittancesCaffee.findByQuantityBagInStore", query = "SELECT r FROM RemittancesCaffee r WHERE r.quantityBagInStore = :quantityBagInStore"),
    @NamedQuery(name = "RemittancesCaffee.findByQuantityBagOutStore", query = "SELECT r FROM RemittancesCaffee r WHERE r.quantityBagOutStore = :quantityBagOutStore"),
    @NamedQuery(name = "RemittancesCaffee.findByTotalWeightNetNominal", query = "SELECT r FROM RemittancesCaffee r WHERE r.totalWeightNetNominal = :totalWeightNetNominal"),
    @NamedQuery(name = "RemittancesCaffee.findByTotalWeightNetReal", query = "SELECT r FROM RemittancesCaffee r WHERE r.totalWeightNetReal = :totalWeightNetReal"),
    @NamedQuery(name = "RemittancesCaffee.findByTotalTare", query = "SELECT r FROM RemittancesCaffee r WHERE r.totalTare = :totalTare"),
    @NamedQuery(name = "RemittancesCaffee.findBySourceLocation", query = "SELECT r FROM RemittancesCaffee r WHERE r.sourceLocation = :sourceLocation"),
    @NamedQuery(name = "RemittancesCaffee.findByAutoOtm", query = "SELECT r FROM RemittancesCaffee r WHERE r.autoOtm = :autoOtm"),
    @NamedQuery(name = "RemittancesCaffee.findByVehiclePlate", query = "SELECT r FROM RemittancesCaffee r WHERE r.vehiclePlate = :vehiclePlate"),
    @NamedQuery(name = "RemittancesCaffee.findByDownloadCaffeeDate", query = "SELECT r FROM RemittancesCaffee r WHERE r.downloadCaffeeDate = :downloadCaffeeDate"),
    @NamedQuery(name = "RemittancesCaffee.findByPackagingCaffeeDate", query = "SELECT r FROM RemittancesCaffee r WHERE r.packagingCaffeeDate = :packagingCaffeeDate"),
    @NamedQuery(name = "RemittancesCaffee.findByCreatedDate", query = "SELECT r FROM RemittancesCaffee r WHERE r.createdDate = :createdDate"),
    @NamedQuery(name = "RemittancesCaffee.findByUpdatedDated", query = "SELECT r FROM RemittancesCaffee r WHERE r.updatedDated = :updatedDated"),
    @NamedQuery(name = "RemittancesCaffee.findByIsActive", query = "SELECT r FROM RemittancesCaffee r WHERE r.isActive = :isActive"),
    @NamedQuery(name = "RemittancesCaffee.findByGuideId", query = "SELECT r FROM RemittancesCaffee r WHERE r.guideId = :guideId")})
public class RemittancesCaffee implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "lot_caffee")
    private String lotCaffee;
    @Column(name = "quantity_radicated_bag_in")
    private Integer quantityBagRadicatedIn;
    @Column(name = "quantity_bag_in_store")
    private Integer quantityBagInStore;
    @Column(name = "quantity_bag_out_store")
    private Integer quantityBagOutStore;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "total_weight_net_nominal")
    private Double totalWeightNetNominal;
    @Column(name = "total_weight_net_real")
    private Double totalWeightNetReal;
    @Column(name = "total_tare")
    private Double totalTare;
    @Column(name = "quantity_in_pallet_caffee")
    private Integer  quantityInPalletCaffee;
    @Column(name = "quantity_out_pallet_caffee")
    private Integer  quantityOutPalletCaffee;
    @Column(name = "source_location")
    private Integer sourceLocation;
    @Basic(optional = false)
    @Column(name = "auto_otm")
    private String autoOtm;
    @Column(name = "vehicle_plate")
    private String vehiclePlate;
    @Column(name = "download_caffee_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date downloadCaffeeDate;
    @Column(name = "packaging_caffee_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date packagingCaffeeDate;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @Column(name = "updated_dated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDated;
    @Basic(optional = false)
    @Column(name = "is_active")
    private boolean isActive;
    @Basic(optional = false)
    @Column(name = "guide_id")
    private String guideId;
    @Basic(optional = false)
    @Column(name = "details_weight")
    private String detailsWeight;
    @Basic(optional = false)
    @Column(name = "observation")
    private String observation;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "closed_download_caffee")
    private Date closedDownloadCaffee;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCaffeeId")
    private List<WeighingDownloadCaffee> weighingDownloadCaffeeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCaffeeId")
    private List<FumigationServices> fumigationServicesList;
    @JoinColumn(name = "packing_cafee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PackingCaffee packingCafeeId;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clients clientId;
    @JoinColumn(name = "port_operators_id", referencedColumnName = "id")
    @ManyToOne
    private PortOperators portOperatorsId;
    @JoinColumn(name = "motor_ships_id", referencedColumnName = "id")
    @ManyToOne
    private MotorShips motorShipsId;
    @JoinColumn(name = "units_cafee_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UnitsCaffee unitsCafeeId;
    @JoinColumn(name = "mark_cafee_id", referencedColumnName = "id")
    @ManyToOne
    private MarkCaffee markCafeeId;
    @JoinColumn(name = "city_source_id", referencedColumnName = "id")
    @ManyToOne
    private CitySource citySourceId;
    @JoinColumn(name = "shippers_id", referencedColumnName = "id")
    @ManyToOne
    private Shippers shippersId;
    @JoinColumn(name = "slot_store_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private SlotStore slotStoreId;
    @JoinColumn(name = "staff_sample_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users staffSampleId;
    @JoinColumn(name = "staff_wt_in_id", referencedColumnName = "id")
    @ManyToOne
    private Users staffWtInId;
    @JoinColumn(name = "staff_wt_out_id", referencedColumnName = "id")
    @ManyToOne
    private Users staffWtOutId;
    @JoinColumn(name = "staff_driver_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users staffDriverId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCafeeId")
    private List<ReturnsCaffees> returnsCaffeesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCafeeId")
    private List<PackagingCaffee> packagingCaffeeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCaffeeId")
    private List<RelatedServices> relatedServicesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCafeeId")
    private List<WeighingPackagingCaffee> weighingPackagingCaffeeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "remittancesCaffee")
    private List<RemittancesCaffeeHasNoveltysCaffee> remittancesCaffeeHasNoveltysCaffeeList;

    public RemittancesCaffee() {
    }

    public RemittancesCaffee(Integer id) {
        this.id = id;
    }

    public Date getClosedDownloadCaffee() {
        return closedDownloadCaffee;
    }

    public void setClosedDownloadCaffee(Date closedDownloadCaffee) {
        this.closedDownloadCaffee = closedDownloadCaffee;
    }

    public RemittancesCaffee(Integer id, String lotCaffee, String autoOtm, Date createdDate, Date updatedDated, boolean isActive, String guideId) {
        this.id = id;
        this.lotCaffee = lotCaffee;
        this.autoOtm = autoOtm;
        this.createdDate = createdDate;
        this.updatedDated = updatedDated;
        this.isActive = isActive;
        this.guideId = guideId;
    }

    public String getDetailsWeight() {
        return detailsWeight;
    }

    public void setDetailsWeight(String detailsWeight) {
        this.detailsWeight = detailsWeight;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Integer getId() {
        return id;
    }

    public Integer getQuantityBagRadicatedIn() {
        return quantityBagRadicatedIn;
    }

    public void setQuantityBagRadicatedIn(Integer quantityBagRadicatedIn) {
        this.quantityBagRadicatedIn = quantityBagRadicatedIn;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLotCaffee() {
        return lotCaffee;
    }

    public void setLotCaffee(String lotCaffee) {
        this.lotCaffee = lotCaffee;
    }

    public Integer getQuantityBagInStore() {
        return quantityBagInStore;
    }

    public Integer getQuantityInPalletCaffee() {
        return quantityInPalletCaffee;
    }

    public void setQuantityInPalletCaffee(Integer quantityInPalletCaffee) {
        this.quantityInPalletCaffee = quantityInPalletCaffee;
    }

    public Integer getQuantityOutPalletCaffee() {
        return quantityOutPalletCaffee;
    }

    public void setQuantityOutPalletCaffee(Integer quantityOutPalletCaffee) {
        this.quantityOutPalletCaffee = quantityOutPalletCaffee;
    }

    public void setQuantityBagInStore(Integer quantityBagInStore) {
        this.quantityBagInStore = quantityBagInStore;
    }

    public Integer getQuantityBagOutStore() {
        return quantityBagOutStore;
    }

    public void setQuantityBagOutStore(Integer quantityBagOutStore) {
        this.quantityBagOutStore = quantityBagOutStore;
    }

    public Double getTotalWeightNetNominal() {
        return totalWeightNetNominal;
    }

    public void setTotalWeightNetNominal(Double totalWeightNetNominal) {
        this.totalWeightNetNominal = totalWeightNetNominal;
    }

    public Double getTotalWeightNetReal() {
        return totalWeightNetReal;
    }

    public void setTotalWeightNetReal(Double totalWeightNetReal) {
        this.totalWeightNetReal = totalWeightNetReal;
    }

    public Double getTotalTare() {
        return totalTare;
    }

    public void setTotalTare(Double totalTare) {
        this.totalTare = totalTare;
    }

    public Integer getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(Integer sourceLocation) {
        this.sourceLocation = sourceLocation;
    }

    public String getAutoOtm() {
        return autoOtm;
    }

    public void setAutoOtm(String autoOtm) {
        this.autoOtm = autoOtm;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public Date getDownloadCaffeeDate() {
        return downloadCaffeeDate;
    }

    public void setDownloadCaffeeDate(Date downloadCaffeeDate) {
        this.downloadCaffeeDate = downloadCaffeeDate;
    }

    public Date getPackagingCaffeeDate() {
        return packagingCaffeeDate;
    }

    public void setPackagingCaffeeDate(Date packagingCaffeeDate) {
        this.packagingCaffeeDate = packagingCaffeeDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDated() {
        return updatedDated;
    }

    public void setUpdatedDated(Date updatedDated) {
        this.updatedDated = updatedDated;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getGuideId() {
        return guideId;
    }

    public void setGuideId(String guideId) {
        this.guideId = guideId;
    }

    @XmlTransient
    public List<WeighingDownloadCaffee> getWeighingDownloadCaffeeList() {
        return weighingDownloadCaffeeList;
    }

    public void setWeighingDownloadCaffeeList(List<WeighingDownloadCaffee> weighingDownloadCaffeeList) {
        this.weighingDownloadCaffeeList = weighingDownloadCaffeeList;
    }

    @XmlTransient
    public List<FumigationServices> getFumigationServicesList() {
        return fumigationServicesList;
    }

    public void setFumigationServicesList(List<FumigationServices> fumigationServicesList) {
        this.fumigationServicesList = fumigationServicesList;
    }

    public PackingCaffee getPackingCafeeId() {
        return packingCafeeId;
    }

    public void setPackingCafeeId(PackingCaffee packingCafeeId) {
        this.packingCafeeId = packingCafeeId;
    }

    public Clients getClientId() {
        return clientId;
    }

    public void setClientId(Clients clientId) {
        this.clientId = clientId;
    }

    public PortOperators getPortOperatorsId() {
        return portOperatorsId;
    }

    public void setPortOperatorsId(PortOperators portOperatorsId) {
        this.portOperatorsId = portOperatorsId;
    }

    public MotorShips getMotorShipsId() {
        return motorShipsId;
    }

    public void setMotorShipsId(MotorShips motorShipsId) {
        this.motorShipsId = motorShipsId;
    }

    public UnitsCaffee getUnitsCafeeId() {
        return unitsCafeeId;
    }

    public void setUnitsCafeeId(UnitsCaffee unitsCafeeId) {
        this.unitsCafeeId = unitsCafeeId;
    }

    public MarkCaffee getMarkCafeeId() {
        return markCafeeId;
    }

    public void setMarkCafeeId(MarkCaffee markCafeeId) {
        this.markCafeeId = markCafeeId;
    }

    public CitySource getCitySourceId() {
        return citySourceId;
    }

    public void setCitySourceId(CitySource citySourceId) {
        this.citySourceId = citySourceId;
    }

    public Shippers getShippersId() {
        return shippersId;
    }

    public void setShippersId(Shippers shippersId) {
        this.shippersId = shippersId;
    }

    public SlotStore getSlotStoreId() {
        return slotStoreId;
    }

    public void setSlotStoreId(SlotStore slotStoreId) {
        this.slotStoreId = slotStoreId;
    }

    public Users getStaffSampleId() {
        return staffSampleId;
    }

    public void setStaffSampleId(Users staffSampleId) {
        this.staffSampleId = staffSampleId;
    }

    public Users getStaffWtInId() {
        return staffWtInId;
    }

    public void setStaffWtInId(Users staffWtInId) {
        this.staffWtInId = staffWtInId;
    }

    public Users getStaffWtOutId() {
        return staffWtOutId;
    }

    public void setStaffWtOutId(Users staffWtOutId) {
        this.staffWtOutId = staffWtOutId;
    }

    public Users getStaffDriverId() {
        return staffDriverId;
    }

    public void setStaffDriverId(Users staffDriverId) {
        this.staffDriverId = staffDriverId;
    }

    @XmlTransient
    public List<ReturnsCaffees> getReturnsCaffeesList() {
        return returnsCaffeesList;
    }

    public void setReturnsCaffeesList(List<ReturnsCaffees> returnsCaffeesList) {
        this.returnsCaffeesList = returnsCaffeesList;
    }

    @XmlTransient
    public List<PackagingCaffee> getPackagingCaffeeList() {
        return packagingCaffeeList;
    }

    public void setPackagingCaffeeList(List<PackagingCaffee> packagingCaffeeList) {
        this.packagingCaffeeList = packagingCaffeeList;
    }

    @XmlTransient
    public List<RelatedServices> getRelatedServicesList() {
        return relatedServicesList;
    }

    public void setRelatedServicesList(List<RelatedServices> relatedServicesList) {
        this.relatedServicesList = relatedServicesList;
    }

    @XmlTransient
    public List<WeighingPackagingCaffee> getWeighingPackagingCaffeeList() {
        return weighingPackagingCaffeeList;
    }

    public void setWeighingPackagingCaffeeList(List<WeighingPackagingCaffee> weighingPackagingCaffeeList) {
        this.weighingPackagingCaffeeList = weighingPackagingCaffeeList;
    }

    @XmlTransient
    public List<RemittancesCaffeeHasNoveltysCaffee> getRemittancesCaffeeHasNoveltysCaffeeList() {
        return remittancesCaffeeHasNoveltysCaffeeList;
    }

    public void setRemittancesCaffeeHasNoveltysCaffeeList(List<RemittancesCaffeeHasNoveltysCaffee> remittancesCaffeeHasNoveltysCaffeeList) {
        this.remittancesCaffeeHasNoveltysCaffeeList = remittancesCaffeeHasNoveltysCaffeeList;
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
        if (!(object instanceof RemittancesCaffee)) {
            return false;
        }
        RemittancesCaffee other = (RemittancesCaffee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "siscafe.model.RemittancesCaffee[ id=" + id + " ]";
    }
    
}
