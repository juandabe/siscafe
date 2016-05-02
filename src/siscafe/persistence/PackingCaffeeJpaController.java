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
import siscafe.model.RemittancesCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.PackingCaffee;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class PackingCaffeeJpaController implements Serializable {

    public PackingCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PackingCaffee packingCaffee) {
        if (packingCaffee.getRemittancesCaffeeList() == null) {
            packingCaffee.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : packingCaffee.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            packingCaffee.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(packingCaffee);
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : packingCaffee.getRemittancesCaffeeList()) {
                PackingCaffee oldPackingCafeeIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getPackingCafeeId();
                remittancesCaffeeListRemittancesCaffee.setPackingCafeeId(packingCaffee);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldPackingCafeeIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldPackingCafeeIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldPackingCafeeIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldPackingCafeeIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PackingCaffee packingCaffee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PackingCaffee persistentPackingCaffee = em.find(PackingCaffee.class, packingCaffee.getId());
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentPackingCaffee.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = packingCaffee.getRemittancesCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffee " + remittancesCaffeeListOldRemittancesCaffee + " since its packingCafeeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RemittancesCaffee> attachedRemittancesCaffeeListNew = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffeeToAttach : remittancesCaffeeListNew) {
                remittancesCaffeeListNewRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListNewRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListNewRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeListNew.add(remittancesCaffeeListNewRemittancesCaffeeToAttach);
            }
            remittancesCaffeeListNew = attachedRemittancesCaffeeListNew;
            packingCaffee.setRemittancesCaffeeList(remittancesCaffeeListNew);
            packingCaffee = em.merge(packingCaffee);
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    PackingCaffee oldPackingCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getPackingCafeeId();
                    remittancesCaffeeListNewRemittancesCaffee.setPackingCafeeId(packingCaffee);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldPackingCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldPackingCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(packingCaffee)) {
                        oldPackingCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldPackingCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldPackingCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = packingCaffee.getId();
                if (findPackingCaffee(id) == null) {
                    throw new NonexistentEntityException("The packingCaffee with id " + id + " no longer exists.");
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
            PackingCaffee packingCaffee;
            try {
                packingCaffee = em.getReference(PackingCaffee.class, id);
                packingCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The packingCaffee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RemittancesCaffee> remittancesCaffeeListOrphanCheck = packingCaffee.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListOrphanCheckRemittancesCaffee : remittancesCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PackingCaffee (" + packingCaffee + ") cannot be destroyed since the RemittancesCaffee " + remittancesCaffeeListOrphanCheckRemittancesCaffee + " in its remittancesCaffeeList field has a non-nullable packingCafeeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(packingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PackingCaffee> findPackingCaffeeEntities() {
        return findPackingCaffeeEntities(true, -1, -1);
    }

    public List<PackingCaffee> findPackingCaffeeEntities(int maxResults, int firstResult) {
        return findPackingCaffeeEntities(false, maxResults, firstResult);
    }

    private List<PackingCaffee> findPackingCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PackingCaffee.class));
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

    public PackingCaffee findPackingCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PackingCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getPackingCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PackingCaffee> rt = cq.from(PackingCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
