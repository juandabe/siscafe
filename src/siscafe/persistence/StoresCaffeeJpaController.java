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
import siscafe.model.SlotStore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.StoresCaffee;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class StoresCaffeeJpaController implements Serializable {

    public StoresCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StoresCaffee storesCaffee) throws PreexistingEntityException, Exception {
        if (storesCaffee.getSlotStoreList() == null) {
            storesCaffee.setSlotStoreList(new ArrayList<SlotStore>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<SlotStore> attachedSlotStoreList = new ArrayList<SlotStore>();
            for (SlotStore slotStoreListSlotStoreToAttach : storesCaffee.getSlotStoreList()) {
                slotStoreListSlotStoreToAttach = em.getReference(slotStoreListSlotStoreToAttach.getClass(), slotStoreListSlotStoreToAttach.getId());
                attachedSlotStoreList.add(slotStoreListSlotStoreToAttach);
            }
            storesCaffee.setSlotStoreList(attachedSlotStoreList);
            em.persist(storesCaffee);
            for (SlotStore slotStoreListSlotStore : storesCaffee.getSlotStoreList()) {
                StoresCaffee oldStoresCaffeeIdOfSlotStoreListSlotStore = slotStoreListSlotStore.getStoresCaffeeId();
                slotStoreListSlotStore.setStoresCaffeeId(storesCaffee);
                slotStoreListSlotStore = em.merge(slotStoreListSlotStore);
                if (oldStoresCaffeeIdOfSlotStoreListSlotStore != null) {
                    oldStoresCaffeeIdOfSlotStoreListSlotStore.getSlotStoreList().remove(slotStoreListSlotStore);
                    oldStoresCaffeeIdOfSlotStoreListSlotStore = em.merge(oldStoresCaffeeIdOfSlotStoreListSlotStore);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStoresCaffee(storesCaffee.getId()) != null) {
                throw new PreexistingEntityException("StoresCaffee " + storesCaffee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StoresCaffee storesCaffee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StoresCaffee persistentStoresCaffee = em.find(StoresCaffee.class, storesCaffee.getId());
            List<SlotStore> slotStoreListOld = persistentStoresCaffee.getSlotStoreList();
            List<SlotStore> slotStoreListNew = storesCaffee.getSlotStoreList();
            List<String> illegalOrphanMessages = null;
            for (SlotStore slotStoreListOldSlotStore : slotStoreListOld) {
                if (!slotStoreListNew.contains(slotStoreListOldSlotStore)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain SlotStore " + slotStoreListOldSlotStore + " since its storesCaffeeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<SlotStore> attachedSlotStoreListNew = new ArrayList<SlotStore>();
            for (SlotStore slotStoreListNewSlotStoreToAttach : slotStoreListNew) {
                slotStoreListNewSlotStoreToAttach = em.getReference(slotStoreListNewSlotStoreToAttach.getClass(), slotStoreListNewSlotStoreToAttach.getId());
                attachedSlotStoreListNew.add(slotStoreListNewSlotStoreToAttach);
            }
            slotStoreListNew = attachedSlotStoreListNew;
            storesCaffee.setSlotStoreList(slotStoreListNew);
            storesCaffee = em.merge(storesCaffee);
            for (SlotStore slotStoreListNewSlotStore : slotStoreListNew) {
                if (!slotStoreListOld.contains(slotStoreListNewSlotStore)) {
                    StoresCaffee oldStoresCaffeeIdOfSlotStoreListNewSlotStore = slotStoreListNewSlotStore.getStoresCaffeeId();
                    slotStoreListNewSlotStore.setStoresCaffeeId(storesCaffee);
                    slotStoreListNewSlotStore = em.merge(slotStoreListNewSlotStore);
                    if (oldStoresCaffeeIdOfSlotStoreListNewSlotStore != null && !oldStoresCaffeeIdOfSlotStoreListNewSlotStore.equals(storesCaffee)) {
                        oldStoresCaffeeIdOfSlotStoreListNewSlotStore.getSlotStoreList().remove(slotStoreListNewSlotStore);
                        oldStoresCaffeeIdOfSlotStoreListNewSlotStore = em.merge(oldStoresCaffeeIdOfSlotStoreListNewSlotStore);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = storesCaffee.getId();
                if (findStoresCaffee(id) == null) {
                    throw new NonexistentEntityException("The storesCaffee with id " + id + " no longer exists.");
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
            StoresCaffee storesCaffee;
            try {
                storesCaffee = em.getReference(StoresCaffee.class, id);
                storesCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The storesCaffee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<SlotStore> slotStoreListOrphanCheck = storesCaffee.getSlotStoreList();
            for (SlotStore slotStoreListOrphanCheckSlotStore : slotStoreListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StoresCaffee (" + storesCaffee + ") cannot be destroyed since the SlotStore " + slotStoreListOrphanCheckSlotStore + " in its slotStoreList field has a non-nullable storesCaffeeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(storesCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StoresCaffee> findStoresCaffeeEntities() {
        return findStoresCaffeeEntities(true, -1, -1);
    }

    public List<StoresCaffee> findStoresCaffeeEntities(int maxResults, int firstResult) {
        return findStoresCaffeeEntities(false, maxResults, firstResult);
    }

    private List<StoresCaffee> findStoresCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StoresCaffee.class));
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

    public StoresCaffee findStoresCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StoresCaffee.class, id);
        } finally {
            em.close();
        }
    }
    
     public StoresCaffee findStoresCaffeebyName(String storeName) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT s FROM StoresCaffee s WHERE s.storeName = :storeName");
        query.setParameter("storeName", storeName);
        try {
            return (StoresCaffee)query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getStoresCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StoresCaffee> rt = cq.from(StoresCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
