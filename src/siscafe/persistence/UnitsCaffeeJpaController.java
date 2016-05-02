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
import siscafe.model.TypeUnits;
import siscafe.model.UnitsCaffee;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class UnitsCaffeeJpaController implements Serializable {

    public UnitsCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UnitsCaffee unitsCaffee) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeUnits typeUnitsId = unitsCaffee.getTypeUnitsId();
            if (typeUnitsId != null) {
                typeUnitsId = em.getReference(typeUnitsId.getClass(), typeUnitsId.getId());
                unitsCaffee.setTypeUnitsId(typeUnitsId);
            }
            em.persist(unitsCaffee);
            if (typeUnitsId != null) {
                typeUnitsId.getUnitsCaffeeList().add(unitsCaffee);
                typeUnitsId = em.merge(typeUnitsId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UnitsCaffee unitsCaffee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UnitsCaffee persistentUnitsCaffee = em.find(UnitsCaffee.class, unitsCaffee.getId());
            TypeUnits typeUnitsIdOld = persistentUnitsCaffee.getTypeUnitsId();
            TypeUnits typeUnitsIdNew = unitsCaffee.getTypeUnitsId();
            if (typeUnitsIdNew != null) {
                typeUnitsIdNew = em.getReference(typeUnitsIdNew.getClass(), typeUnitsIdNew.getId());
                unitsCaffee.setTypeUnitsId(typeUnitsIdNew);
            }
            unitsCaffee = em.merge(unitsCaffee);
            if (typeUnitsIdOld != null && !typeUnitsIdOld.equals(typeUnitsIdNew)) {
                typeUnitsIdOld.getUnitsCaffeeList().remove(unitsCaffee);
                typeUnitsIdOld = em.merge(typeUnitsIdOld);
            }
            if (typeUnitsIdNew != null && !typeUnitsIdNew.equals(typeUnitsIdOld)) {
                typeUnitsIdNew.getUnitsCaffeeList().add(unitsCaffee);
                typeUnitsIdNew = em.merge(typeUnitsIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = unitsCaffee.getId();
                if (findUnitsCaffee(id) == null) {
                    throw new NonexistentEntityException("The unitsCaffee with id " + id + " no longer exists.");
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
            UnitsCaffee unitsCaffee;
            try {
                unitsCaffee = em.getReference(UnitsCaffee.class, id);
                unitsCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The unitsCaffee with id " + id + " no longer exists.", enfe);
            }
            TypeUnits typeUnitsId = unitsCaffee.getTypeUnitsId();
            if (typeUnitsId != null) {
                typeUnitsId.getUnitsCaffeeList().remove(unitsCaffee);
                typeUnitsId = em.merge(typeUnitsId);
            }
            em.remove(unitsCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UnitsCaffee> findUnitsCaffeeEntities() {
        return findUnitsCaffeeEntities(true, -1, -1);
    }

    public List<UnitsCaffee> findUnitsCaffeeEntities(int maxResults, int firstResult) {
        return findUnitsCaffeeEntities(false, maxResults, firstResult);
    }

    private List<UnitsCaffee> findUnitsCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UnitsCaffee.class));
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

    public UnitsCaffee findUnitsCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UnitsCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getUnitsCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UnitsCaffee> rt = cq.from(UnitsCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
