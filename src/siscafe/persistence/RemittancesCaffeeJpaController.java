/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.persistence;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import siscafe.model.PackingCaffee;
import siscafe.model.MarkCaffee;
import siscafe.model.CitySource;
import siscafe.model.Clients;
import siscafe.model.RemittancesCaffee;
import siscafe.model.Shippers;
import siscafe.model.SlotStore;
import siscafe.model.StateOperation;
import siscafe.model.StoresCaffee;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class RemittancesCaffeeJpaController implements Serializable {

    public RemittancesCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RemittancesCaffee remittancesCaffee) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PackingCaffee packingCafeeId = remittancesCaffee.getPackingCafeeId();
            if (packingCafeeId != null) {
                packingCafeeId = em.getReference(packingCafeeId.getClass(), packingCafeeId.getId());
                remittancesCaffee.setPackingCafeeId(packingCafeeId);
            }
            MarkCaffee markCafeeId = remittancesCaffee.getMarkCafeeId();
            if (markCafeeId != null) {
                markCafeeId = em.getReference(markCafeeId.getClass(), markCafeeId.getId());
                remittancesCaffee.setMarkCafeeId(markCafeeId);
            }
            CitySource citySourceId = remittancesCaffee.getCitySourceId();
            if (citySourceId != null) {
                citySourceId = em.getReference(citySourceId.getClass(), citySourceId.getId());
                remittancesCaffee.setCitySourceId(citySourceId);
            }
            Shippers shippersId = remittancesCaffee.getShippersId();
            if (shippersId != null) {
                shippersId = em.getReference(shippersId.getClass(), shippersId.getId());
                remittancesCaffee.setShippersId(shippersId);
            }
            SlotStore slotStoreId = remittancesCaffee.getSlotStoreId();
            if (slotStoreId != null) {
                slotStoreId = em.getReference(slotStoreId.getClass(), slotStoreId.getId());
                remittancesCaffee.setSlotStoreId(slotStoreId);
            }
            em.persist(remittancesCaffee);
            if (packingCafeeId != null) {
                packingCafeeId.getRemittancesCaffeeList().add(remittancesCaffee);
                packingCafeeId = em.merge(packingCafeeId);
            }
            if (markCafeeId != null) {
                markCafeeId.getRemittancesCaffeeList().add(remittancesCaffee);
                markCafeeId = em.merge(markCafeeId);
            }
            if (citySourceId != null) {
                citySourceId.getRemittancesCaffeeList().add(remittancesCaffee);
                citySourceId = em.merge(citySourceId);
            }
            if (shippersId != null) {
                shippersId.getRemittancesCaffeeList().add(remittancesCaffee);
                shippersId = em.merge(shippersId);
            }
            if (slotStoreId != null) {
                slotStoreId.getRemittancesCaffeeList().add(remittancesCaffee);
                slotStoreId = em.merge(slotStoreId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRemittancesCaffee(remittancesCaffee.getId()) != null) {
                throw new PreexistingEntityException("RemittancesCaffee " + remittancesCaffee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RemittancesCaffee remittancesCaffee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RemittancesCaffee persistentRemittancesCaffee = em.find(RemittancesCaffee.class, remittancesCaffee.getId());
            PackingCaffee packingCafeeIdOld = persistentRemittancesCaffee.getPackingCafeeId();
            PackingCaffee packingCafeeIdNew = remittancesCaffee.getPackingCafeeId();
            MarkCaffee markCafeeIdOld = persistentRemittancesCaffee.getMarkCafeeId();
            MarkCaffee markCafeeIdNew = remittancesCaffee.getMarkCafeeId();
            CitySource citySourceIdOld = persistentRemittancesCaffee.getCitySourceId();
            CitySource citySourceIdNew = remittancesCaffee.getCitySourceId();
            Shippers shippersIdOld = persistentRemittancesCaffee.getShippersId();
            Shippers shippersIdNew = remittancesCaffee.getShippersId();
            SlotStore slotStoreIdOld = persistentRemittancesCaffee.getSlotStoreId();
            SlotStore slotStoreIdNew = remittancesCaffee.getSlotStoreId();
            if (packingCafeeIdNew != null) {
                packingCafeeIdNew = em.getReference(packingCafeeIdNew.getClass(), packingCafeeIdNew.getId());
                remittancesCaffee.setPackingCafeeId(packingCafeeIdNew);
            }
            if (markCafeeIdNew != null) {
                markCafeeIdNew = em.getReference(markCafeeIdNew.getClass(), markCafeeIdNew.getId());
                remittancesCaffee.setMarkCafeeId(markCafeeIdNew);
            }
            if (citySourceIdNew != null) {
                citySourceIdNew = em.getReference(citySourceIdNew.getClass(), citySourceIdNew.getId());
                remittancesCaffee.setCitySourceId(citySourceIdNew);
            }
            if (shippersIdNew != null) {
                shippersIdNew = em.getReference(shippersIdNew.getClass(), shippersIdNew.getId());
                remittancesCaffee.setShippersId(shippersIdNew);
            }
            if (slotStoreIdNew != null) {
                slotStoreIdNew = em.getReference(slotStoreIdNew.getClass(), slotStoreIdNew.getId());
                remittancesCaffee.setSlotStoreId(slotStoreIdNew);
            }
            remittancesCaffee = em.merge(remittancesCaffee);
            if (packingCafeeIdOld != null && !packingCafeeIdOld.equals(packingCafeeIdNew)) {
                packingCafeeIdOld.getRemittancesCaffeeList().remove(remittancesCaffee);
                packingCafeeIdOld = em.merge(packingCafeeIdOld);
            }
            if (packingCafeeIdNew != null && !packingCafeeIdNew.equals(packingCafeeIdOld)) {
                packingCafeeIdNew.getRemittancesCaffeeList().add(remittancesCaffee);
                packingCafeeIdNew = em.merge(packingCafeeIdNew);
            }
            if (markCafeeIdOld != null && !markCafeeIdOld.equals(markCafeeIdNew)) {
                markCafeeIdOld.getRemittancesCaffeeList().remove(remittancesCaffee);
                markCafeeIdOld = em.merge(markCafeeIdOld);
            }
            if (markCafeeIdNew != null && !markCafeeIdNew.equals(markCafeeIdOld)) {
                markCafeeIdNew.getRemittancesCaffeeList().add(remittancesCaffee);
                markCafeeIdNew = em.merge(markCafeeIdNew);
            }
            if (citySourceIdOld != null && !citySourceIdOld.equals(citySourceIdNew)) {
                citySourceIdOld.getRemittancesCaffeeList().remove(remittancesCaffee);
                citySourceIdOld = em.merge(citySourceIdOld);
            }
            if (citySourceIdNew != null && !citySourceIdNew.equals(citySourceIdOld)) {
                citySourceIdNew.getRemittancesCaffeeList().add(remittancesCaffee);
                citySourceIdNew = em.merge(citySourceIdNew);
            }
            if (shippersIdOld != null && !shippersIdOld.equals(shippersIdNew)) {
                shippersIdOld.getRemittancesCaffeeList().remove(remittancesCaffee);
                shippersIdOld = em.merge(shippersIdOld);
            }
            if (shippersIdNew != null && !shippersIdNew.equals(shippersIdOld)) {
                shippersIdNew.getRemittancesCaffeeList().add(remittancesCaffee);
                shippersIdNew = em.merge(shippersIdNew);
            }
            if (slotStoreIdOld != null && !slotStoreIdOld.equals(slotStoreIdNew)) {
                slotStoreIdOld.getRemittancesCaffeeList().remove(remittancesCaffee);
                slotStoreIdOld = em.merge(slotStoreIdOld);
            }
            if (slotStoreIdNew != null && !slotStoreIdNew.equals(slotStoreIdOld)) {
                slotStoreIdNew.getRemittancesCaffeeList().add(remittancesCaffee);
                slotStoreIdNew = em.merge(slotStoreIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = remittancesCaffee.getId();
                if (findRemittancesCaffee(id) == null) {
                    throw new NonexistentEntityException("The remittancesCaffee with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RemittancesCaffee remittancesCaffee;
            try {
                remittancesCaffee = em.getReference(RemittancesCaffee.class, id);
                remittancesCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The remittancesCaffee with id " + id + " no longer exists.", enfe);
            }
            PackingCaffee packingCafeeId = remittancesCaffee.getPackingCafeeId();
            if (packingCafeeId != null) {
                packingCafeeId.getRemittancesCaffeeList().remove(remittancesCaffee);
                packingCafeeId = em.merge(packingCafeeId);
            }
            MarkCaffee markCafeeId = remittancesCaffee.getMarkCafeeId();
            if (markCafeeId != null) {
                markCafeeId.getRemittancesCaffeeList().remove(remittancesCaffee);
                markCafeeId = em.merge(markCafeeId);
            }
            CitySource citySourceId = remittancesCaffee.getCitySourceId();
            if (citySourceId != null) {
                citySourceId.getRemittancesCaffeeList().remove(remittancesCaffee);
                citySourceId = em.merge(citySourceId);
            }
            Shippers shippersId = remittancesCaffee.getShippersId();
            if (shippersId != null) {
                shippersId.getRemittancesCaffeeList().remove(remittancesCaffee);
                shippersId = em.merge(shippersId);
            }
            SlotStore slotStoreId = remittancesCaffee.getSlotStoreId();
            if (slotStoreId != null) {
                slotStoreId.getRemittancesCaffeeList().remove(remittancesCaffee);
                slotStoreId = em.merge(slotStoreId);
            }
            em.remove(remittancesCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RemittancesCaffee> findRemittancesCaffeeEntities() {
        return findRemittancesCaffeeEntities(true, -1, -1);
    }

    public List<RemittancesCaffee> findRemittancesCaffeeEntities(int maxResults, int firstResult) {
        return findRemittancesCaffeeEntities(false, maxResults, firstResult);
    }

    private List<RemittancesCaffee> findRemittancesCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RemittancesCaffee.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public RemittancesCaffee findRemittancesCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RemittancesCaffee.class, id);
        } finally {
            em.close();
        }
    }
    public List<RemittancesCaffee> findRemittancesCaffeeByLot(String lotCaffee) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.lotCaffee=:loteCaffee");
        query.setParameter("loteCaffee", lotCaffee);
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    public RemittancesCaffee findRemittancesCaffeeByGuidedAndLot(String guide, String lotCaffee) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.guideId=:guide and r.lotCaffee=:loteCaffee and r.isActive=true");
        query.setParameter("guide", guide);
        query.setParameter("loteCaffee", lotCaffee);
        try {
            return (RemittancesCaffee) query.getSingleResult();
        }
        catch(Exception e) {
            return null;
        }
    }
    public List<RemittancesCaffee> findRemittancesCaffeeBypackagingCaffeeId(Integer id) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.id = :id");
        query.setParameter("id", id);        
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    public List<RemittancesCaffee> findRemittancesCaffeePendientWeight() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.statusOperation=1 and r.isActive=true");
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<RemittancesCaffee> findRemittancesCaffeeInProcessWieght() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.statusOperation=2 and r.isActive=true");
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<RemittancesCaffee> findRemittancesCaffeeOrdered() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.statusOperation=4 and r.isActive=true");
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<RemittancesCaffee> findRemittancesCaffeeProcessPckaging() {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.statusOperation=5 and r.isActive=true");
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<RemittancesCaffee> findRemittancesCaffeeByExporter(Clients client) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r  WHERE r.clientId=:clientId");
        query.setParameter("clientId", client);
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<RemittancesCaffee> findRemittancesCaffeeByExporterandDate(Clients client,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.clientId=:clientId and r.createdDate between :dateStart and :dateEnd");
     //   "SELECT r FROM RemittancesCaffee r WHERE r.createdDate between :dateStart and :dateEnd"
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("clientId", client);
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    public List<RemittancesCaffee> findRemittancesCaffeeByStateandDate(StateOperation state,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.stateOperationId=:statusOperation and r.createdDate between :dateStart and :dateEnd");
     //   "SELECT r FROM RemittancesCaffee r WHERE r.createdDate between :dateStart and :dateEnd"
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("statusOperation", state);
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    

   public List<RemittancesCaffee> findRemittancesCaffeeByConsultGeneral(Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.createdDate between :dateStart and :dateEnd");
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
   
   public List<RemittancesCaffee> findRemittancesCaffeeByStore(StoresCaffee StoreStart,StoresCaffee StoreEnd,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.slotStoreId.storesCaffeeId.id between :storeStart and :storeEnd and r.createdDate between :dateStart and :dateEnd");
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("storeStart", StoreStart.getId());
        query.setParameter("storeEnd", StoreEnd.getId()); 
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
   public List<RemittancesCaffee> findRemittancesCaffeeByClientandState(StateOperation state,Clients client,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.stateOperationId=:statusOperation and r.clientId=:clientId and r.createdDate between :dateStart and :dateEnd");
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("statusOperation", state); 
        query.setParameter("clientId", client); 
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
   
   public List<RemittancesCaffee> findRemittancesCaffeeByStoreandState(StateOperation state,StoresCaffee StoreStart,StoresCaffee StoreEnd,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.stateOperationId=:statusOperation and r.slotStoreId.storesCaffeeId.id between :storeStart and :storeEnd and r.createdDate between :dateStart and :dateEnd");
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("storeStart", StoreStart.getId());
        query.setParameter("storeEnd", StoreEnd.getId()); 
        query.setParameter("statusOperation", state); 
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    public List<RemittancesCaffee> findRemittancesCaffeeByStoreandClient(Clients client,StoresCaffee StoreStart,StoresCaffee StoreEnd,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.clientId=:clientId and r.slotStoreId.storesCaffeeId.id between :storeStart and :storeEnd and r.createdDate between :dateStart and :dateEnd");
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("storeStart", StoreStart.getId());
        query.setParameter("storeEnd", StoreEnd.getId());
        query.setParameter("clientId", client);        
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
   
   public List<RemittancesCaffee> findRemittancesCaffeeByStoreandClientandState(StateOperation state,Clients client,StoresCaffee StoreStart,StoresCaffee StoreEnd,Date fechaStart,Date fechaEnd) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT r FROM RemittancesCaffee r WHERE r.clientId=:clientId and r.stateOperationId=:statusOperation and r.slotStoreId.storesCaffeeId.id between :storeStart and :storeEnd and r.createdDate between :dateStart and :dateEnd");
        query.setParameter("dateStart", fechaStart);
        query.setParameter("dateEnd", fechaEnd); 
        query.setParameter("storeStart", StoreStart.getId());
        query.setParameter("storeEnd", StoreEnd.getId());
        query.setParameter("clientId", client);
        query.setParameter("statusOperation", state); 
        try {
            return (List<RemittancesCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }

    public int getRemittancesCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RemittancesCaffee> rt = cq.from(RemittancesCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
