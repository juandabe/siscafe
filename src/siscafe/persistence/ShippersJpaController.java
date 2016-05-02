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
import siscafe.model.Shippers;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class ShippersJpaController implements Serializable {

    public ShippersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Shippers shippers) {
        if (shippers.getRemittancesCaffeeList() == null) {
            shippers.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : shippers.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            shippers.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(shippers);
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : shippers.getRemittancesCaffeeList()) {
                Shippers oldShippersIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getShippersId();
                remittancesCaffeeListRemittancesCaffee.setShippersId(shippers);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldShippersIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldShippersIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldShippersIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldShippersIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Shippers shippers) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Shippers persistentShippers = em.find(Shippers.class, shippers.getId());
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentShippers.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = shippers.getRemittancesCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffee " + remittancesCaffeeListOldRemittancesCaffee + " since its shippersId field is not nullable.");
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
            shippers.setRemittancesCaffeeList(remittancesCaffeeListNew);
            shippers = em.merge(shippers);
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    Shippers oldShippersIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getShippersId();
                    remittancesCaffeeListNewRemittancesCaffee.setShippersId(shippers);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldShippersIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldShippersIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(shippers)) {
                        oldShippersIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldShippersIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldShippersIdOfRemittancesCaffeeListNewRemittancesCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = shippers.getId();
                if (findShippers(id) == null) {
                    throw new NonexistentEntityException("The shippers with id " + id + " no longer exists.");
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
            Shippers shippers;
            try {
                shippers = em.getReference(Shippers.class, id);
                shippers.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The shippers with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RemittancesCaffee> remittancesCaffeeListOrphanCheck = shippers.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListOrphanCheckRemittancesCaffee : remittancesCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Shippers (" + shippers + ") cannot be destroyed since the RemittancesCaffee " + remittancesCaffeeListOrphanCheckRemittancesCaffee + " in its remittancesCaffeeList field has a non-nullable shippersId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(shippers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Shippers> findShippersEntities() {
        return findShippersEntities(true, -1, -1);
    }

    public List<Shippers> findShippersEntities(int maxResults, int firstResult) {
        return findShippersEntities(false, maxResults, firstResult);
    }

    private List<Shippers> findShippersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Shippers.class));
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

    public Shippers findShippers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Shippers.class, id);
        } finally {
            em.close();
        }
    }

    public int getShippersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Shippers> rt = cq.from(Shippers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
