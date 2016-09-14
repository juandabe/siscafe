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
import siscafe.controller.exceptions.IllegalOrphanException;
import siscafe.controller.exceptions.NonexistentEntityException;
import siscafe.model.ShippingLines;

/**
 *
 * @author Administrador
 */
public class ShippingLinesJpaController implements Serializable {

    public ShippingLinesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ShippingLines shippingLines) {
        if (shippingLines.getPackagingCaffeeList() == null) {
            shippingLines.setPackagingCaffeeList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListPackagingCaffeeToAttach : shippingLines.getPackagingCaffeeList()) {
                packagingCaffeeListPackagingCaffeeToAttach = em.getReference(packagingCaffeeListPackagingCaffeeToAttach.getClass(), packagingCaffeeListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeList.add(packagingCaffeeListPackagingCaffeeToAttach);
            }
            shippingLines.setPackagingCaffeeList(attachedPackagingCaffeeList);
            em.persist(shippingLines);
            for (PackagingCaffee packagingCaffeeListPackagingCaffee : shippingLines.getPackagingCaffeeList()) {
                ShippingLines oldShippingLinesIdOfPackagingCaffeeListPackagingCaffee = packagingCaffeeListPackagingCaffee.getShippingLinesId();
                packagingCaffeeListPackagingCaffee.setShippingLinesId(shippingLines);
                packagingCaffeeListPackagingCaffee = em.merge(packagingCaffeeListPackagingCaffee);
                if (oldShippingLinesIdOfPackagingCaffeeListPackagingCaffee != null) {
                    oldShippingLinesIdOfPackagingCaffeeListPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListPackagingCaffee);
                    oldShippingLinesIdOfPackagingCaffeeListPackagingCaffee = em.merge(oldShippingLinesIdOfPackagingCaffeeListPackagingCaffee);
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
            List<PackagingCaffee> packagingCaffeeListOld = persistentShippingLines.getPackagingCaffeeList();
            List<PackagingCaffee> packagingCaffeeListNew = shippingLines.getPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeListOldPackagingCaffee : packagingCaffeeListOld) {
                if (!packagingCaffeeListNew.contains(packagingCaffeeListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeListOldPackagingCaffee + " since its shippingLinesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PackagingCaffee> attachedPackagingCaffeeListNew = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffeeToAttach : packagingCaffeeListNew) {
                packagingCaffeeListNewPackagingCaffeeToAttach = em.getReference(packagingCaffeeListNewPackagingCaffeeToAttach.getClass(), packagingCaffeeListNewPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeListNew.add(packagingCaffeeListNewPackagingCaffeeToAttach);
            }
            packagingCaffeeListNew = attachedPackagingCaffeeListNew;
            shippingLines.setPackagingCaffeeList(packagingCaffeeListNew);
            shippingLines = em.merge(shippingLines);
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffee : packagingCaffeeListNew) {
                if (!packagingCaffeeListOld.contains(packagingCaffeeListNewPackagingCaffee)) {
                    ShippingLines oldShippingLinesIdOfPackagingCaffeeListNewPackagingCaffee = packagingCaffeeListNewPackagingCaffee.getShippingLinesId();
                    packagingCaffeeListNewPackagingCaffee.setShippingLinesId(shippingLines);
                    packagingCaffeeListNewPackagingCaffee = em.merge(packagingCaffeeListNewPackagingCaffee);
                    if (oldShippingLinesIdOfPackagingCaffeeListNewPackagingCaffee != null && !oldShippingLinesIdOfPackagingCaffeeListNewPackagingCaffee.equals(shippingLines)) {
                        oldShippingLinesIdOfPackagingCaffeeListNewPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListNewPackagingCaffee);
                        oldShippingLinesIdOfPackagingCaffeeListNewPackagingCaffee = em.merge(oldShippingLinesIdOfPackagingCaffeeListNewPackagingCaffee);
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
            List<PackagingCaffee> packagingCaffeeListOrphanCheck = shippingLines.getPackagingCaffeeList();
            for (PackagingCaffee packagingCaffeeListOrphanCheckPackagingCaffee : packagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ShippingLines (" + shippingLines + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeListOrphanCheckPackagingCaffee + " in its packagingCaffeeList field has a non-nullable shippingLinesId field.");
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
