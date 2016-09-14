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
import siscafe.model.AdictionalElementsHasPackagingCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.controller.exceptions.IllegalOrphanException;
import siscafe.controller.exceptions.NonexistentEntityException;
import siscafe.model.AdictionalElements;

/**
 *
 * @author Administrador
 */
public class AdictionalElementsJpaController implements Serializable {

    public AdictionalElementsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AdictionalElements adictionalElements) {
        if (adictionalElements.getAdictionalElementsHasPackagingCaffeeList() == null) {
            adictionalElements.setAdictionalElementsHasPackagingCaffeeList(new ArrayList<AdictionalElementsHasPackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<AdictionalElementsHasPackagingCaffee> attachedAdictionalElementsHasPackagingCaffeeList = new ArrayList<AdictionalElementsHasPackagingCaffee>();
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach : adictionalElements.getAdictionalElementsHasPackagingCaffeeList()) {
                adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach = em.getReference(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach.getClass(), adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach.getAdictionalElementsHasPackagingCaffeePK());
                attachedAdictionalElementsHasPackagingCaffeeList.add(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach);
            }
            adictionalElements.setAdictionalElementsHasPackagingCaffeeList(attachedAdictionalElementsHasPackagingCaffeeList);
            em.persist(adictionalElements);
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee : adictionalElements.getAdictionalElementsHasPackagingCaffeeList()) {
                AdictionalElements oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee = adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee.getAdictionalElements();
                adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee.setAdictionalElements(adictionalElements);
                adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee = em.merge(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee);
                if (oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee != null) {
                    oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee);
                    oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee = em.merge(oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AdictionalElements adictionalElements) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AdictionalElements persistentAdictionalElements = em.find(AdictionalElements.class, adictionalElements.getId());
            List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeListOld = persistentAdictionalElements.getAdictionalElementsHasPackagingCaffeeList();
            List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeListNew = adictionalElements.getAdictionalElementsHasPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListOldAdictionalElementsHasPackagingCaffee : adictionalElementsHasPackagingCaffeeListOld) {
                if (!adictionalElementsHasPackagingCaffeeListNew.contains(adictionalElementsHasPackagingCaffeeListOldAdictionalElementsHasPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AdictionalElementsHasPackagingCaffee " + adictionalElementsHasPackagingCaffeeListOldAdictionalElementsHasPackagingCaffee + " since its adictionalElements field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AdictionalElementsHasPackagingCaffee> attachedAdictionalElementsHasPackagingCaffeeListNew = new ArrayList<AdictionalElementsHasPackagingCaffee>();
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach : adictionalElementsHasPackagingCaffeeListNew) {
                adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach = em.getReference(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach.getClass(), adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach.getAdictionalElementsHasPackagingCaffeePK());
                attachedAdictionalElementsHasPackagingCaffeeListNew.add(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach);
            }
            adictionalElementsHasPackagingCaffeeListNew = attachedAdictionalElementsHasPackagingCaffeeListNew;
            adictionalElements.setAdictionalElementsHasPackagingCaffeeList(adictionalElementsHasPackagingCaffeeListNew);
            adictionalElements = em.merge(adictionalElements);
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee : adictionalElementsHasPackagingCaffeeListNew) {
                if (!adictionalElementsHasPackagingCaffeeListOld.contains(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee)) {
                    AdictionalElements oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee = adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.getAdictionalElements();
                    adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.setAdictionalElements(adictionalElements);
                    adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee = em.merge(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee);
                    if (oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee != null && !oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.equals(adictionalElements)) {
                        oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee);
                        oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee = em.merge(oldAdictionalElementsOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = adictionalElements.getId();
                if (findAdictionalElements(id) == null) {
                    throw new NonexistentEntityException("The adictionalElements with id " + id + " no longer exists.");
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
            AdictionalElements adictionalElements;
            try {
                adictionalElements = em.getReference(AdictionalElements.class, id);
                adictionalElements.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The adictionalElements with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeListOrphanCheck = adictionalElements.getAdictionalElementsHasPackagingCaffeeList();
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListOrphanCheckAdictionalElementsHasPackagingCaffee : adictionalElementsHasPackagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AdictionalElements (" + adictionalElements + ") cannot be destroyed since the AdictionalElementsHasPackagingCaffee " + adictionalElementsHasPackagingCaffeeListOrphanCheckAdictionalElementsHasPackagingCaffee + " in its adictionalElementsHasPackagingCaffeeList field has a non-nullable adictionalElements field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(adictionalElements);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AdictionalElements> findAdictionalElementsEntities() {
        return findAdictionalElementsEntities(true, -1, -1);
    }

    public List<AdictionalElements> findAdictionalElementsEntities(int maxResults, int firstResult) {
        return findAdictionalElementsEntities(false, maxResults, firstResult);
    }

    private List<AdictionalElements> findAdictionalElementsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AdictionalElements.class));
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

    public AdictionalElements findAdictionalElements(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AdictionalElements.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdictionalElementsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AdictionalElements> rt = cq.from(AdictionalElements.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
