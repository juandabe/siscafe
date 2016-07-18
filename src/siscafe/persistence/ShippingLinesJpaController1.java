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
import siscafe.model.PackagingCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.ShippingLines;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class ShippingLinesJpaController1 implements Serializable {

    public ShippingLinesJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ShippingLines shippingLines) {
        if (shippingLines.getPackagingCaffeeoldList() == null) {
            shippingLines.setPackagingCaffeeoldList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeoldList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeoldListPackagingCaffeeToAttach : shippingLines.getPackagingCaffeeoldList()) {
                packagingCaffeeoldListPackagingCaffeeToAttach = em.getReference(packagingCaffeeoldListPackagingCaffeeToAttach.getClass(), packagingCaffeeoldListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeoldList.add(packagingCaffeeoldListPackagingCaffeeToAttach);
            }
            shippingLines.setPackagingCaffeeoldList(attachedPackagingCaffeeoldList);
            em.persist(shippingLines);
            for (PackagingCaffee packagingCaffeeoldListPackagingCaffee : shippingLines.getPackagingCaffeeoldList()) {
                ShippingLines oldShippingLinesIdOfPackagingCaffeeoldListPackagingCaffee = packagingCaffeeoldListPackagingCaffee.getShippingLinesId();
                packagingCaffeeoldListPackagingCaffee.setShippingLinesId(shippingLines);
                packagingCaffeeoldListPackagingCaffee = em.merge(packagingCaffeeoldListPackagingCaffee);
                if (oldShippingLinesIdOfPackagingCaffeeoldListPackagingCaffee != null) {
                    oldShippingLinesIdOfPackagingCaffeeoldListPackagingCaffee.getPackagingCaffeeoldList().remove(packagingCaffeeoldListPackagingCaffee);
                    oldShippingLinesIdOfPackagingCaffeeoldListPackagingCaffee = em.merge(oldShippingLinesIdOfPackagingCaffeeoldListPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ShippingLines shippingLines) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ShippingLines persistentShippingLines = em.find(ShippingLines.class, shippingLines.getId());
            List<PackagingCaffee> packagingCaffeeoldListOld = persistentShippingLines.getPackagingCaffeeoldList();
            List<PackagingCaffee> packagingCaffeeoldListNew = shippingLines.getPackagingCaffeeoldList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeoldListOldPackagingCaffee : packagingCaffeeoldListOld) {
                if (!packagingCaffeeoldListNew.contains(packagingCaffeeoldListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeoldListOldPackagingCaffee + " since its shippingLinesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PackagingCaffee> attachedPackagingCaffeeoldListNew = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeoldListNewPackagingCaffeeToAttach : packagingCaffeeoldListNew) {
                packagingCaffeeoldListNewPackagingCaffeeToAttach = em.getReference(packagingCaffeeoldListNewPackagingCaffeeToAttach.getClass(), packagingCaffeeoldListNewPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeoldListNew.add(packagingCaffeeoldListNewPackagingCaffeeToAttach);
            }
            packagingCaffeeoldListNew = attachedPackagingCaffeeoldListNew;
            shippingLines.setPackagingCaffeeoldList(packagingCaffeeoldListNew);
            shippingLines = em.merge(shippingLines);
            for (PackagingCaffee packagingCaffeeoldListNewPackagingCaffee : packagingCaffeeoldListNew) {
                if (!packagingCaffeeoldListOld.contains(packagingCaffeeoldListNewPackagingCaffee)) {
                    ShippingLines oldShippingLinesIdOfPackagingCaffeeoldListNewPackagingCaffee = packagingCaffeeoldListNewPackagingCaffee.getShippingLinesId();
                    packagingCaffeeoldListNewPackagingCaffee.setShippingLinesId(shippingLines);
                    packagingCaffeeoldListNewPackagingCaffee = em.merge(packagingCaffeeoldListNewPackagingCaffee);
                    if (oldShippingLinesIdOfPackagingCaffeeoldListNewPackagingCaffee != null && !oldShippingLinesIdOfPackagingCaffeeoldListNewPackagingCaffee.equals(shippingLines)) {
                        oldShippingLinesIdOfPackagingCaffeeoldListNewPackagingCaffee.getPackagingCaffeeoldList().remove(packagingCaffeeoldListNewPackagingCaffee);
                        oldShippingLinesIdOfPackagingCaffeeoldListNewPackagingCaffee = em.merge(oldShippingLinesIdOfPackagingCaffeeoldListNewPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = shippingLines.getId();
                if (findShippingLines(id) == null) {
                    throw new NonexistentEntityException("The shippingLines with id " + id + " no longer exists.");
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
            ShippingLines shippingLines;
            try {
                shippingLines = em.getReference(ShippingLines.class, id);
                shippingLines.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The shippingLines with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PackagingCaffee> packagingCaffeeoldListOrphanCheck = shippingLines.getPackagingCaffeeoldList();
            for (PackagingCaffee packagingCaffeeoldListOrphanCheckPackagingCaffee : packagingCaffeeoldListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ShippingLines (" + shippingLines + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeoldListOrphanCheckPackagingCaffee + " in its packagingCaffeeoldList field has a non-nullable shippingLinesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(shippingLines);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ShippingLines> findShippingLinesEntities() {
        return findShippingLinesEntities(true, -1, -1);
    }

    public List<ShippingLines> findShippingLinesEntities(int maxResults, int firstResult) {
        return findShippingLinesEntities(false, maxResults, firstResult);
    }

    private List<ShippingLines> findShippingLinesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ShippingLines.class));
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

    public ShippingLines findShippingLines(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ShippingLines.class, id);
        } finally {
            em.close();
        }
    }

    public int getShippingLinesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ShippingLines> rt = cq.from(ShippingLines.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
