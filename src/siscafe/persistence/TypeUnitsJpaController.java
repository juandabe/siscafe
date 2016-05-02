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
import siscafe.model.UnitsCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.TypeUnits;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class TypeUnitsJpaController implements Serializable {

    public TypeUnitsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TypeUnits typeUnits) {
        if (typeUnits.getUnitsCaffeeList() == null) {
            typeUnits.setUnitsCaffeeList(new ArrayList<UnitsCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<UnitsCaffee> attachedUnitsCaffeeList = new ArrayList<UnitsCaffee>();
            for (UnitsCaffee unitsCaffeeListUnitsCaffeeToAttach : typeUnits.getUnitsCaffeeList()) {
                unitsCaffeeListUnitsCaffeeToAttach = em.getReference(unitsCaffeeListUnitsCaffeeToAttach.getClass(), unitsCaffeeListUnitsCaffeeToAttach.getId());
                attachedUnitsCaffeeList.add(unitsCaffeeListUnitsCaffeeToAttach);
            }
            typeUnits.setUnitsCaffeeList(attachedUnitsCaffeeList);
            em.persist(typeUnits);
            for (UnitsCaffee unitsCaffeeListUnitsCaffee : typeUnits.getUnitsCaffeeList()) {
                TypeUnits oldTypeUnitsIdOfUnitsCaffeeListUnitsCaffee = unitsCaffeeListUnitsCaffee.getTypeUnitsId();
                unitsCaffeeListUnitsCaffee.setTypeUnitsId(typeUnits);
                unitsCaffeeListUnitsCaffee = em.merge(unitsCaffeeListUnitsCaffee);
                if (oldTypeUnitsIdOfUnitsCaffeeListUnitsCaffee != null) {
                    oldTypeUnitsIdOfUnitsCaffeeListUnitsCaffee.getUnitsCaffeeList().remove(unitsCaffeeListUnitsCaffee);
                    oldTypeUnitsIdOfUnitsCaffeeListUnitsCaffee = em.merge(oldTypeUnitsIdOfUnitsCaffeeListUnitsCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TypeUnits typeUnits) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeUnits persistentTypeUnits = em.find(TypeUnits.class, typeUnits.getId());
            List<UnitsCaffee> unitsCaffeeListOld = persistentTypeUnits.getUnitsCaffeeList();
            List<UnitsCaffee> unitsCaffeeListNew = typeUnits.getUnitsCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (UnitsCaffee unitsCaffeeListOldUnitsCaffee : unitsCaffeeListOld) {
                if (!unitsCaffeeListNew.contains(unitsCaffeeListOldUnitsCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UnitsCaffee " + unitsCaffeeListOldUnitsCaffee + " since its typeUnitsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<UnitsCaffee> attachedUnitsCaffeeListNew = new ArrayList<UnitsCaffee>();
            for (UnitsCaffee unitsCaffeeListNewUnitsCaffeeToAttach : unitsCaffeeListNew) {
                unitsCaffeeListNewUnitsCaffeeToAttach = em.getReference(unitsCaffeeListNewUnitsCaffeeToAttach.getClass(), unitsCaffeeListNewUnitsCaffeeToAttach.getId());
                attachedUnitsCaffeeListNew.add(unitsCaffeeListNewUnitsCaffeeToAttach);
            }
            unitsCaffeeListNew = attachedUnitsCaffeeListNew;
            typeUnits.setUnitsCaffeeList(unitsCaffeeListNew);
            typeUnits = em.merge(typeUnits);
            for (UnitsCaffee unitsCaffeeListNewUnitsCaffee : unitsCaffeeListNew) {
                if (!unitsCaffeeListOld.contains(unitsCaffeeListNewUnitsCaffee)) {
                    TypeUnits oldTypeUnitsIdOfUnitsCaffeeListNewUnitsCaffee = unitsCaffeeListNewUnitsCaffee.getTypeUnitsId();
                    unitsCaffeeListNewUnitsCaffee.setTypeUnitsId(typeUnits);
                    unitsCaffeeListNewUnitsCaffee = em.merge(unitsCaffeeListNewUnitsCaffee);
                    if (oldTypeUnitsIdOfUnitsCaffeeListNewUnitsCaffee != null && !oldTypeUnitsIdOfUnitsCaffeeListNewUnitsCaffee.equals(typeUnits)) {
                        oldTypeUnitsIdOfUnitsCaffeeListNewUnitsCaffee.getUnitsCaffeeList().remove(unitsCaffeeListNewUnitsCaffee);
                        oldTypeUnitsIdOfUnitsCaffeeListNewUnitsCaffee = em.merge(oldTypeUnitsIdOfUnitsCaffeeListNewUnitsCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = typeUnits.getId();
                if (findTypeUnits(id) == null) {
                    throw new NonexistentEntityException("The typeUnits with id " + id + " no longer exists.");
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
            TypeUnits typeUnits;
            try {
                typeUnits = em.getReference(TypeUnits.class, id);
                typeUnits.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The typeUnits with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<UnitsCaffee> unitsCaffeeListOrphanCheck = typeUnits.getUnitsCaffeeList();
            for (UnitsCaffee unitsCaffeeListOrphanCheckUnitsCaffee : unitsCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TypeUnits (" + typeUnits + ") cannot be destroyed since the UnitsCaffee " + unitsCaffeeListOrphanCheckUnitsCaffee + " in its unitsCaffeeList field has a non-nullable typeUnitsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(typeUnits);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TypeUnits> findTypeUnitsEntities() {
        return findTypeUnitsEntities(true, -1, -1);
    }

    public List<TypeUnits> findTypeUnitsEntities(int maxResults, int firstResult) {
        return findTypeUnitsEntities(false, maxResults, firstResult);
    }

    private List<TypeUnits> findTypeUnitsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TypeUnits.class));
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

    public TypeUnits findTypeUnits(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TypeUnits.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeUnitsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TypeUnits> rt = cq.from(TypeUnits.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
