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
import siscafe.model.StateOperation;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class StateOperationJpaController implements Serializable {

    public StateOperationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StateOperation stateOperation) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(stateOperation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StateOperation stateOperation) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            stateOperation = em.merge(stateOperation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stateOperation.getId();
                if (findStateOperation(id) == null) {
                    throw new NonexistentEntityException("The stateOperation with id " + id + " no longer exists.");
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
            StateOperation stateOperation;
            try {
                stateOperation = em.getReference(StateOperation.class, id);
                stateOperation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stateOperation with id " + id + " no longer exists.", enfe);
            }
            em.remove(stateOperation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StateOperation> findStateOperationEntities() {
        return findStateOperationEntities(true, -1, -1);
    }

    public List<StateOperation> findStateOperationEntities(int maxResults, int firstResult) {
        return findStateOperationEntities(false, maxResults, firstResult);
    }

    private List<StateOperation> findStateOperationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StateOperation.class));
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

    public StateOperation findStateOperation(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StateOperation.class, id);
        } finally {
            em.close();
        }
    }

    public int getStateOperationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StateOperation> rt = cq.from(StateOperation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
