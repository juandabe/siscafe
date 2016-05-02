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
import siscafe.model.Permits;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.CategoryPermits;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class CategoryPermitsJpaController implements Serializable {

    public CategoryPermitsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CategoryPermits categoryPermits) {
        if (categoryPermits.getPermitsList() == null) {
            categoryPermits.setPermitsList(new ArrayList<Permits>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permits> attachedPermitsList = new ArrayList<Permits>();
            for (Permits permitsListPermitsToAttach : categoryPermits.getPermitsList()) {
                permitsListPermitsToAttach = em.getReference(permitsListPermitsToAttach.getClass(), permitsListPermitsToAttach.getId());
                attachedPermitsList.add(permitsListPermitsToAttach);
            }
            categoryPermits.setPermitsList(attachedPermitsList);
            em.persist(categoryPermits);
            for (Permits permitsListPermits : categoryPermits.getPermitsList()) {
                CategoryPermits oldCategoryPermitsIdOfPermitsListPermits = permitsListPermits.getCategoryPermitsId();
                permitsListPermits.setCategoryPermitsId(categoryPermits);
                permitsListPermits = em.merge(permitsListPermits);
                if (oldCategoryPermitsIdOfPermitsListPermits != null) {
                    oldCategoryPermitsIdOfPermitsListPermits.getPermitsList().remove(permitsListPermits);
                    oldCategoryPermitsIdOfPermitsListPermits = em.merge(oldCategoryPermitsIdOfPermitsListPermits);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CategoryPermits categoryPermits) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoryPermits persistentCategoryPermits = em.find(CategoryPermits.class, categoryPermits.getId());
            List<Permits> permitsListOld = persistentCategoryPermits.getPermitsList();
            List<Permits> permitsListNew = categoryPermits.getPermitsList();
            List<String> illegalOrphanMessages = null;
            for (Permits permitsListOldPermits : permitsListOld) {
                if (!permitsListNew.contains(permitsListOldPermits)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Permits " + permitsListOldPermits + " since its categoryPermitsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permits> attachedPermitsListNew = new ArrayList<Permits>();
            for (Permits permitsListNewPermitsToAttach : permitsListNew) {
                permitsListNewPermitsToAttach = em.getReference(permitsListNewPermitsToAttach.getClass(), permitsListNewPermitsToAttach.getId());
                attachedPermitsListNew.add(permitsListNewPermitsToAttach);
            }
            permitsListNew = attachedPermitsListNew;
            categoryPermits.setPermitsList(permitsListNew);
            categoryPermits = em.merge(categoryPermits);
            for (Permits permitsListNewPermits : permitsListNew) {
                if (!permitsListOld.contains(permitsListNewPermits)) {
                    CategoryPermits oldCategoryPermitsIdOfPermitsListNewPermits = permitsListNewPermits.getCategoryPermitsId();
                    permitsListNewPermits.setCategoryPermitsId(categoryPermits);
                    permitsListNewPermits = em.merge(permitsListNewPermits);
                    if (oldCategoryPermitsIdOfPermitsListNewPermits != null && !oldCategoryPermitsIdOfPermitsListNewPermits.equals(categoryPermits)) {
                        oldCategoryPermitsIdOfPermitsListNewPermits.getPermitsList().remove(permitsListNewPermits);
                        oldCategoryPermitsIdOfPermitsListNewPermits = em.merge(oldCategoryPermitsIdOfPermitsListNewPermits);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoryPermits.getId();
                if (findCategoryPermits(id) == null) {
                    throw new NonexistentEntityException("The categoryPermits with id " + id + " no longer exists.");
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
            CategoryPermits categoryPermits;
            try {
                categoryPermits = em.getReference(CategoryPermits.class, id);
                categoryPermits.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoryPermits with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Permits> permitsListOrphanCheck = categoryPermits.getPermitsList();
            for (Permits permitsListOrphanCheckPermits : permitsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CategoryPermits (" + categoryPermits + ") cannot be destroyed since the Permits " + permitsListOrphanCheckPermits + " in its permitsList field has a non-nullable categoryPermitsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoryPermits);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CategoryPermits> findCategoryPermitsEntities() {
        return findCategoryPermitsEntities(true, -1, -1);
    }

    public List<CategoryPermits> findCategoryPermitsEntities(int maxResults, int firstResult) {
        return findCategoryPermitsEntities(false, maxResults, firstResult);
    }

    private List<CategoryPermits> findCategoryPermitsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CategoryPermits.class));
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

    public CategoryPermits findCategoryPermits(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CategoryPermits.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryPermitsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CategoryPermits> rt = cq.from(CategoryPermits.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
