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
import siscafe.model.PackagingCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.TypeContainer;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class TypeContainerJpaController1 implements Serializable {

    public TypeContainerJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TypeContainer typeContainer) {
        if (typeContainer.getPackagingCaffeeList() == null) {
            typeContainer.setPackagingCaffeeList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListPackagingCaffeeToAttach : typeContainer.getPackagingCaffeeList()) {
                packagingCaffeeListPackagingCaffeeToAttach = em.getReference(packagingCaffeeListPackagingCaffeeToAttach.getClass(), packagingCaffeeListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeList.add(packagingCaffeeListPackagingCaffeeToAttach);
            }
            typeContainer.setPackagingCaffeeList(attachedPackagingCaffeeList);
            em.persist(typeContainer);
            for (PackagingCaffee packagingCaffeeListPackagingCaffee : typeContainer.getPackagingCaffeeList()) {
                TypeContainer oldTypeContainerIdOfPackagingCaffeeListPackagingCaffee = packagingCaffeeListPackagingCaffee.getTypeContainerId();
                packagingCaffeeListPackagingCaffee.setTypeContainerId(typeContainer);
                packagingCaffeeListPackagingCaffee = em.merge(packagingCaffeeListPackagingCaffee);
                if (oldTypeContainerIdOfPackagingCaffeeListPackagingCaffee != null) {
                    oldTypeContainerIdOfPackagingCaffeeListPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListPackagingCaffee);
                    oldTypeContainerIdOfPackagingCaffeeListPackagingCaffee = em.merge(oldTypeContainerIdOfPackagingCaffeeListPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TypeContainer typeContainer) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeContainer persistentTypeContainer = em.find(TypeContainer.class, typeContainer.getId());
            List<PackagingCaffee> packagingCaffeeListOld = persistentTypeContainer.getPackagingCaffeeList();
            List<PackagingCaffee> packagingCaffeeListNew = typeContainer.getPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeListOldPackagingCaffee : packagingCaffeeListOld) {
                if (!packagingCaffeeListNew.contains(packagingCaffeeListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeListOldPackagingCaffee + " since its typeContainerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PackagingCaffee> attachedPackagingCaffeeListNew = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffeeToAttach : packagingCaffeeListNew) {
                packagingCaffeeListNewPackagingCaffeeToAttach = em.getReference(packagingCaffeeListNewPackagingCaffeeToAttach.getClass(), packagingCaffeeListNewPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeListNew.add(packagingCaffeeListNewPackagingCaffeeToAttach);
            }
            packagingCaffeeListNew = attachedPackagingCaffeeListNew;
            typeContainer.setPackagingCaffeeList(packagingCaffeeListNew);
            typeContainer = em.merge(typeContainer);
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffee : packagingCaffeeListNew) {
                if (!packagingCaffeeListOld.contains(packagingCaffeeListNewPackagingCaffee)) {
                    TypeContainer oldTypeContainerIdOfPackagingCaffeeListNewPackagingCaffee = packagingCaffeeListNewPackagingCaffee.getTypeContainerId();
                    packagingCaffeeListNewPackagingCaffee.setTypeContainerId(typeContainer);
                    packagingCaffeeListNewPackagingCaffee = em.merge(packagingCaffeeListNewPackagingCaffee);
                    if (oldTypeContainerIdOfPackagingCaffeeListNewPackagingCaffee != null && !oldTypeContainerIdOfPackagingCaffeeListNewPackagingCaffee.equals(typeContainer)) {
                        oldTypeContainerIdOfPackagingCaffeeListNewPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListNewPackagingCaffee);
                        oldTypeContainerIdOfPackagingCaffeeListNewPackagingCaffee = em.merge(oldTypeContainerIdOfPackagingCaffeeListNewPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = typeContainer.getId();
                if (findTypeContainer(id) == null) {
                    throw new NonexistentEntityException("The typeContainer with id " + id + " no longer exists.");
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
            TypeContainer typeContainer;
            try {
                typeContainer = em.getReference(TypeContainer.class, id);
                typeContainer.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The typeContainer with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PackagingCaffee> packagingCaffeeListOrphanCheck = typeContainer.getPackagingCaffeeList();
            for (PackagingCaffee packagingCaffeeListOrphanCheckPackagingCaffee : packagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TypeContainer (" + typeContainer + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeListOrphanCheckPackagingCaffee + " in its packagingCaffeeList field has a non-nullable typeContainerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(typeContainer);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TypeContainer> findTypeContainerEntities() {
        return findTypeContainerEntities(true, -1, -1);
    }

    public List<TypeContainer> findTypeContainerEntities(int maxResults, int firstResult) {
        return findTypeContainerEntities(false, maxResults, firstResult);
    }

    private List<TypeContainer> findTypeContainerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TypeContainer.class));
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

    public TypeContainer findTypeContainer(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TypeContainer.class, id);
        } finally {
            em.close();
        }
    }

    public int getTypeContainerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TypeContainer> rt = cq.from(TypeContainer.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
