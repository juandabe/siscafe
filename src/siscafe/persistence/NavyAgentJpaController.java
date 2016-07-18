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
import siscafe.model.NavyAgent;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class NavyAgentJpaController implements Serializable {

    public NavyAgentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NavyAgent navyAgent) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(navyAgent);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNavyAgent(navyAgent.getId()) != null) {
                throw new PreexistingEntityException("NavyAgent " + navyAgent + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NavyAgent navyAgent) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            navyAgent = em.merge(navyAgent);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = navyAgent.getId();
                if (findNavyAgent(id) == null) {
                    throw new NonexistentEntityException("The navyAgent with id " + id + " no longer exists.");
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
            NavyAgent navyAgent;
            try {
                navyAgent = em.getReference(NavyAgent.class, id);
                navyAgent.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The navyAgent with id " + id + " no longer exists.", enfe);
            }
            em.remove(navyAgent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NavyAgent> findNavyAgentEntities() {
        return findNavyAgentEntities(true, -1, -1);
    }

    public List<NavyAgent> findNavyAgentEntities(int maxResults, int firstResult) {
        return findNavyAgentEntities(false, maxResults, firstResult);
    }

    private List<NavyAgent> findNavyAgentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NavyAgent.class));
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

    public NavyAgent findNavyAgent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NavyAgent.class, id);
        } finally {
            em.close();
        }
    }

    public int getNavyAgentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NavyAgent> rt = cq.from(NavyAgent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
