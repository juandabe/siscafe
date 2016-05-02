/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siscafe.persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import siscafe.model.ShippingLines;
import siscafe.persistence.exceptions.NonexistentEntityException;

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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(shippingLines);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ShippingLines shippingLines) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            shippingLines = em.merge(shippingLines);
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
