/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import siscafe.model.StoresCaffee;
import siscafe.model.RemittancesCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.SlotStore;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class SlotStoreJpaController implements Serializable {

    public SlotStoreJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SlotStore slotStore) {
        if (slotStore.getRemittancesCaffeeList() == null) {
            slotStore.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StoresCaffee storesCaffeeId = slotStore.getStoresCaffeeId();
            if (storesCaffeeId != null) {
                storesCaffeeId = em.getReference(storesCaffeeId.getClass(), storesCaffeeId.getId());
                slotStore.setStoresCaffeeId(storesCaffeeId);
            }
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : slotStore.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            slotStore.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(slotStore);
            if (storesCaffeeId != null) {
                storesCaffeeId.getSlotStoreList().add(slotStore);
                storesCaffeeId = em.merge(storesCaffeeId);
            }
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : slotStore.getRemittancesCaffeeList()) {
                SlotStore oldSlotStoreIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getSlotStoreId();
                remittancesCaffeeListRemittancesCaffee.setSlotStoreId(slotStore);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldSlotStoreIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldSlotStoreIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldSlotStoreIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldSlotStoreIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SlotStore slotStore) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SlotStore persistentSlotStore = em.find(SlotStore.class, slotStore.getId());
            StoresCaffee storesCaffeeIdOld = persistentSlotStore.getStoresCaffeeId();
            StoresCaffee storesCaffeeIdNew = slotStore.getStoresCaffeeId();
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentSlotStore.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = slotStore.getRemittancesCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffee " + remittancesCaffeeListOldRemittancesCaffee + " since its slotStoreId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (storesCaffeeIdNew != null) {
                storesCaffeeIdNew = em.getReference(storesCaffeeIdNew.getClass(), storesCaffeeIdNew.getId());
                slotStore.setStoresCaffeeId(storesCaffeeIdNew);
            }
            List<RemittancesCaffee> attachedRemittancesCaffeeListNew = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffeeToAttach : remittancesCaffeeListNew) {
                remittancesCaffeeListNewRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListNewRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListNewRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeListNew.add(remittancesCaffeeListNewRemittancesCaffeeToAttach);
            }
            remittancesCaffeeListNew = attachedRemittancesCaffeeListNew;
            slotStore.setRemittancesCaffeeList(remittancesCaffeeListNew);
            slotStore = em.merge(slotStore);
            if (storesCaffeeIdOld != null && !storesCaffeeIdOld.equals(storesCaffeeIdNew)) {
                storesCaffeeIdOld.getSlotStoreList().remove(slotStore);
                storesCaffeeIdOld = em.merge(storesCaffeeIdOld);
            }
            if (storesCaffeeIdNew != null && !storesCaffeeIdNew.equals(storesCaffeeIdOld)) {
                storesCaffeeIdNew.getSlotStoreList().add(slotStore);
                storesCaffeeIdNew = em.merge(storesCaffeeIdNew);
            }
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    SlotStore oldSlotStoreIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getSlotStoreId();
                    remittancesCaffeeListNewRemittancesCaffee.setSlotStoreId(slotStore);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldSlotStoreIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldSlotStoreIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(slotStore)) {
                        oldSlotStoreIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldSlotStoreIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldSlotStoreIdOfRemittancesCaffeeListNewRemittancesCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = slotStore.getId();
                if (findSlotStore(id) == null) {
                    throw new NonexistentEntityException("The slotStore with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SlotStore slotStore;
            try {
                slotStore = em.getReference(SlotStore.class, id);
                slotStore.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The slotStore with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RemittancesCaffee> remittancesCaffeeListOrphanCheck = slotStore.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListOrphanCheckRemittancesCaffee : remittancesCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This SlotStore (" + slotStore + ") cannot be destroyed since the RemittancesCaffee " + remittancesCaffeeListOrphanCheckRemittancesCaffee + " in its remittancesCaffeeList field has a non-nullable slotStoreId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            StoresCaffee storesCaffeeId = slotStore.getStoresCaffeeId();
            if (storesCaffeeId != null) {
                storesCaffeeId.getSlotStoreList().remove(slotStore);
                storesCaffeeId = em.merge(storesCaffeeId);
            }
            em.remove(slotStore);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public List <SlotStore> findSlotStoreEntitiesByStore (StoresCaffee storesCaffee) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("Select ss from SlotStore ss where ss.storesCaffeeId=:storesCaffeeId");
        query.setParameter("storesCaffeeId", storesCaffee);
        try {
            return (List<SlotStore>) query.getResultList();
        }
        catch(Exception e){
            return null;
        }
    }
    
    public List <SlotStore> getSlotStoreEmptyByStoreCaffee (StoresCaffee storesCaffee) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("Select ss from SlotStore ss where ss.storesCaffeeId=:storesCaffee "
                + "and ss.id NOT IN (select rc.slotStoreId.id from RemittancesCaffee rc where rc.isActive=true)");
        query.setParameter("storesCaffee", storesCaffee);
        try {
            return (List <SlotStore>) query.getResultList();
        }
        catch(Exception e){
            return null;
        }
    }

    public List<SlotStore> findSlotStoreEntities() {
        return findSlotStoreEntities(true, -1, -1);
    }

    public List<SlotStore> findSlotStoreEntities(int maxResults, int firstResult) {
        return findSlotStoreEntities(false, maxResults, firstResult);
    }

    private List<SlotStore> findSlotStoreEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SlotStore.class));
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

    public SlotStore findSlotStore(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SlotStore.class, id);
        } finally {
            em.close();
        }
    }

    public int getSlotStoreCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SlotStore> rt = cq.from(SlotStore.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
