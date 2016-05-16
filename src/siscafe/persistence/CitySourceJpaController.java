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
import siscafe.model.CitySource;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class CitySourceJpaController implements Serializable {

    public CitySourceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CitySource citySource) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(citySource);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCitySource(citySource.getId()) != null) {
                throw new PreexistingEntityException("CitySource " + citySource + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CitySource citySource) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            citySource = em.merge(citySource);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = citySource.getId();
                if (findCitySource(id) == null) {
                    throw new NonexistentEntityException("The citySource with id " + id + " no longer exists.");
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
            CitySource citySource;
            try {
                citySource = em.getReference(CitySource.class, id);
                citySource.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citySource with id " + id + " no longer exists.", enfe);
            }
            em.remove(citySource);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CitySource> findCitySourceEntities() {
        return findCitySourceEntities(true, -1, -1);
    }

    public List<CitySource> findCitySourceEntities(int maxResults, int firstResult) {
        return findCitySourceEntities(false, maxResults, firstResult);
    }

    private List<CitySource> findCitySourceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CitySource.class));
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

    public CitySource findCitySource(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CitySource.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitySourceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CitySource> rt = cq.from(CitySource.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
