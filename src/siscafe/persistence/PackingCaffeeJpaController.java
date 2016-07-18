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
import siscafe.model.PackingCaffee;
import siscafe.model.RemittancesCaffee;
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(packingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PackingCaffee packingCaffee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            packingCaffee = em.merge(packingCaffee);
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
