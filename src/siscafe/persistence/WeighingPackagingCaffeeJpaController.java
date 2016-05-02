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
import siscafe.model.WeighingPackagingCaffee;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class WeighingPackagingCaffeeJpaController implements Serializable {

    public WeighingPackagingCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WeighingPackagingCaffee weighingPackagingCaffee) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(weighingPackagingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WeighingPackagingCaffee weighingPackagingCaffee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            weighingPackagingCaffee = em.merge(weighingPackagingCaffee);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = weighingPackagingCaffee.getId();
                if (findWeighingPackagingCaffee(id) == null) {
                    throw new NonexistentEntityException("The weighingPackagingCaffee with id " + id + " no longer exists.");
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
            WeighingPackagingCaffee weighingPackagingCaffee;
            try {
                weighingPackagingCaffee = em.getReference(WeighingPackagingCaffee.class, id);
                weighingPackagingCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The weighingPackagingCaffee with id " + id + " no longer exists.", enfe);
            }
            em.remove(weighingPackagingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WeighingPackagingCaffee> findWeighingPackagingCaffeeEntities() {
        return findWeighingPackagingCaffeeEntities(true, -1, -1);
    }

    public List<WeighingPackagingCaffee> findWeighingPackagingCaffeeEntities(int maxResults, int firstResult) {
        return findWeighingPackagingCaffeeEntities(false, maxResults, firstResult);
    }

    private List<WeighingPackagingCaffee> findWeighingPackagingCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WeighingPackagingCaffee.class));
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

    public WeighingPackagingCaffee findWeighingPackagingCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WeighingPackagingCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getWeighingPackagingCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WeighingPackagingCaffee> rt = cq.from(WeighingPackagingCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
