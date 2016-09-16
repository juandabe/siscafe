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
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.model.Clients;

/**
 *
 * @author Administrador
 */
public class ClientsJpaController implements Serializable {

    public ClientsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clients clients) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(clients);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clients clients) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            clients = em.merge(clients);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clients.getId();
                if (findClients(id) == null) {
                    throw new NonexistentEntityException("The clients with id " + id + " no longer exists.");
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
            Clients clients;
            try {
                clients = em.getReference(Clients.class, id);
                clients.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clients with id " + id + " no longer exists.", enfe);
            }
            em.remove(clients);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clients> findClientsEntities() {
        return findClientsEntities(true, -1, -1);
    }

    public List<Clients> findClientsEntities(int maxResults, int firstResult) {
        return findClientsEntities(false, maxResults, firstResult);
    }

    private List<Clients> findClientsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clients.class));
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

    public Clients findClients(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clients.class, id);
        } finally {
            em.close();
        }
    }
     public Clients findClientesByBusinessName(String businessName) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT c FROM Clients c WHERE c.businessName = :businessName");
        query.setParameter("businessName", businessName);
        try {
            return (Clients)query.getSingleResult();
        } catch(Exception e) {
            return null;
        }
    }

    public int getClientsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clients> rt = cq.from(Clients.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
