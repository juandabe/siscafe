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
import siscafe.controller.exceptions.NonexistentEntityException;
import siscafe.controller.exceptions.PreexistingEntityException;
import siscafe.model.AdictionalElements;
import siscafe.model.AdictionalElementsHasPackagingCaffee;
import siscafe.model.AdictionalElementsHasPackagingCaffeePK;
import siscafe.model.PackagingCaffee;

/**
 *
 * @author Administrador
 */
public class AdictionalElementsHasPackagingCaffeeJpaController implements Serializable {

    public AdictionalElementsHasPackagingCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffee) throws PreexistingEntityException, Exception {
        if (adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK() == null) {
            adictionalElementsHasPackagingCaffee.setAdictionalElementsHasPackagingCaffeePK(new AdictionalElementsHasPackagingCaffeePK());
        }
        adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK().setPackagingCaffeeId(adictionalElementsHasPackagingCaffee.getPackagingCaffee().getId());
        adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK().setAdictionalElementsId(adictionalElementsHasPackagingCaffee.getAdictionalElements().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdictionalElements adictionalElements = adictionalElementsHasPackagingCaffee.getAdictionalElements();
            if (adictionalElements != null) {
                adictionalElements = em.getReference(adictionalElements.getClass(), adictionalElements.getId());
                adictionalElementsHasPackagingCaffee.setAdictionalElements(adictionalElements);
            }
            PackagingCaffee packagingCaffee = adictionalElementsHasPackagingCaffee.getPackagingCaffee();
            if (packagingCaffee != null) {
                packagingCaffee = em.getReference(packagingCaffee.getClass(), packagingCaffee.getId());
                adictionalElementsHasPackagingCaffee.setPackagingCaffee(packagingCaffee);
            }
            em.persist(adictionalElementsHasPackagingCaffee);
            if (adictionalElements != null) {
                adictionalElements.getAdictionalElementsHasPackagingCaffeeList().add(adictionalElementsHasPackagingCaffee);
                adictionalElements = em.merge(adictionalElements);
            }
            if (packagingCaffee != null) {
                packagingCaffee.getAdictionalElementsHasPackagingCaffeeList().add(adictionalElementsHasPackagingCaffee);
                packagingCaffee = em.merge(packagingCaffee);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAdictionalElementsHasPackagingCaffee(adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK()) != null) {
                throw new PreexistingEntityException("AdictionalElementsHasPackagingCaffee " + adictionalElementsHasPackagingCaffee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffee) throws NonexistentEntityException, Exception {
        adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK().setPackagingCaffeeId(adictionalElementsHasPackagingCaffee.getPackagingCaffee().getId());
        adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK().setAdictionalElementsId(adictionalElementsHasPackagingCaffee.getAdictionalElements().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdictionalElementsHasPackagingCaffee persistentAdictionalElementsHasPackagingCaffee = em.find(AdictionalElementsHasPackagingCaffee.class, adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK());
            AdictionalElements adictionalElementsOld = persistentAdictionalElementsHasPackagingCaffee.getAdictionalElements();
            AdictionalElements adictionalElementsNew = adictionalElementsHasPackagingCaffee.getAdictionalElements();
            PackagingCaffee packagingCaffeeOld = persistentAdictionalElementsHasPackagingCaffee.getPackagingCaffee();
            PackagingCaffee packagingCaffeeNew = adictionalElementsHasPackagingCaffee.getPackagingCaffee();
            if (adictionalElementsNew != null) {
                adictionalElementsNew = em.getReference(adictionalElementsNew.getClass(), adictionalElementsNew.getId());
                adictionalElementsHasPackagingCaffee.setAdictionalElements(adictionalElementsNew);
            }
            if (packagingCaffeeNew != null) {
                packagingCaffeeNew = em.getReference(packagingCaffeeNew.getClass(), packagingCaffeeNew.getId());
                adictionalElementsHasPackagingCaffee.setPackagingCaffee(packagingCaffeeNew);
            }
            adictionalElementsHasPackagingCaffee = em.merge(adictionalElementsHasPackagingCaffee);
            if (adictionalElementsOld != null && !adictionalElementsOld.equals(adictionalElementsNew)) {
                adictionalElementsOld.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffee);
                adictionalElementsOld = em.merge(adictionalElementsOld);
            }
            if (adictionalElementsNew != null && !adictionalElementsNew.equals(adictionalElementsOld)) {
                adictionalElementsNew.getAdictionalElementsHasPackagingCaffeeList().add(adictionalElementsHasPackagingCaffee);
                adictionalElementsNew = em.merge(adictionalElementsNew);
            }
            if (packagingCaffeeOld != null && !packagingCaffeeOld.equals(packagingCaffeeNew)) {
                packagingCaffeeOld.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffee);
                packagingCaffeeOld = em.merge(packagingCaffeeOld);
            }
            if (packagingCaffeeNew != null && !packagingCaffeeNew.equals(packagingCaffeeOld)) {
                packagingCaffeeNew.getAdictionalElementsHasPackagingCaffeeList().add(adictionalElementsHasPackagingCaffee);
                packagingCaffeeNew = em.merge(packagingCaffeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                AdictionalElementsHasPackagingCaffeePK id = adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK();
                if (findAdictionalElementsHasPackagingCaffee(id) == null) {
                    throw new NonexistentEntityException("The adictionalElementsHasPackagingCaffee with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(AdictionalElementsHasPackagingCaffeePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffee;
            try {
                adictionalElementsHasPackagingCaffee = em.getReference(AdictionalElementsHasPackagingCaffee.class, id);
                adictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adictionalElementsHasPackagingCaffee with id " + id + " no longer exists.", enfe);
            }
            AdictionalElements adictionalElements = adictionalElementsHasPackagingCaffee.getAdictionalElements();
            if (adictionalElements != null) {
                adictionalElements.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffee);
                adictionalElements = em.merge(adictionalElements);
            }
            PackagingCaffee packagingCaffee = adictionalElementsHasPackagingCaffee.getPackagingCaffee();
            if (packagingCaffee != null) {
                packagingCaffee.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffee);
                packagingCaffee = em.merge(packagingCaffee);
            }
            em.remove(adictionalElementsHasPackagingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AdictionalElementsHasPackagingCaffee> findAdictionalElementsHasPackagingCaffeeEntities() {
        return findAdictionalElementsHasPackagingCaffeeEntities(true, -1, -1);
    }

    public List<AdictionalElementsHasPackagingCaffee> findAdictionalElementsHasPackagingCaffeeEntities(int maxResults, int firstResult) {
        return findAdictionalElementsHasPackagingCaffeeEntities(false, maxResults, firstResult);
    }

    private List<AdictionalElementsHasPackagingCaffee> findAdictionalElementsHasPackagingCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AdictionalElementsHasPackagingCaffee.class));
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

    public AdictionalElementsHasPackagingCaffee findAdictionalElementsHasPackagingCaffee(AdictionalElementsHasPackagingCaffeePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AdictionalElementsHasPackagingCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdictionalElementsHasPackagingCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AdictionalElementsHasPackagingCaffee> rt = cq.from(AdictionalElementsHasPackagingCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
