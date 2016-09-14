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
import siscafe.model.StatePackaging;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class StatePackagingJpaController implements Serializable {

    public StatePackagingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StatePackaging statePackaging) {
        if (statePackaging.getPackagingCaffeeList() == null) {
            statePackaging.setPackagingCaffeeList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListPackagingCaffeeToAttach : statePackaging.getPackagingCaffeeList()) {
                packagingCaffeeListPackagingCaffeeToAttach = em.getReference(packagingCaffeeListPackagingCaffeeToAttach.getClass(), packagingCaffeeListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeList.add(packagingCaffeeListPackagingCaffeeToAttach);
            }
            statePackaging.setPackagingCaffeeList(attachedPackagingCaffeeList);
            em.persist(statePackaging);
            for (PackagingCaffee packagingCaffeeListPackagingCaffee : statePackaging.getPackagingCaffeeList()) {
                StatePackaging oldStatePackagingIdOfPackagingCaffeeListPackagingCaffee = packagingCaffeeListPackagingCaffee.getStatePackagingId();
                packagingCaffeeListPackagingCaffee.setStatePackagingId(statePackaging);
                packagingCaffeeListPackagingCaffee = em.merge(packagingCaffeeListPackagingCaffee);
                if (oldStatePackagingIdOfPackagingCaffeeListPackagingCaffee != null) {
                    oldStatePackagingIdOfPackagingCaffeeListPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListPackagingCaffee);
                    oldStatePackagingIdOfPackagingCaffeeListPackagingCaffee = em.merge(oldStatePackagingIdOfPackagingCaffeeListPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StatePackaging statePackaging) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StatePackaging persistentStatePackaging = em.find(StatePackaging.class, statePackaging.getId());
            List<PackagingCaffee> packagingCaffeeListOld = persistentStatePackaging.getPackagingCaffeeList();
            List<PackagingCaffee> packagingCaffeeListNew = statePackaging.getPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeListOldPackagingCaffee : packagingCaffeeListOld) {
                if (!packagingCaffeeListNew.contains(packagingCaffeeListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeListOldPackagingCaffee + " since its statePackagingId field is not nullable.");
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
            statePackaging.setPackagingCaffeeList(packagingCaffeeListNew);
            statePackaging = em.merge(statePackaging);
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffee : packagingCaffeeListNew) {
                if (!packagingCaffeeListOld.contains(packagingCaffeeListNewPackagingCaffee)) {
                    StatePackaging oldStatePackagingIdOfPackagingCaffeeListNewPackagingCaffee = packagingCaffeeListNewPackagingCaffee.getStatePackagingId();
                    packagingCaffeeListNewPackagingCaffee.setStatePackagingId(statePackaging);
                    packagingCaffeeListNewPackagingCaffee = em.merge(packagingCaffeeListNewPackagingCaffee);
                    if (oldStatePackagingIdOfPackagingCaffeeListNewPackagingCaffee != null && !oldStatePackagingIdOfPackagingCaffeeListNewPackagingCaffee.equals(statePackaging)) {
                        oldStatePackagingIdOfPackagingCaffeeListNewPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListNewPackagingCaffee);
                        oldStatePackagingIdOfPackagingCaffeeListNewPackagingCaffee = em.merge(oldStatePackagingIdOfPackagingCaffeeListNewPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = statePackaging.getId();
                if (findStatePackaging(id) == null) {
                    throw new NonexistentEntityException("The statePackaging with id " + id + " no longer exists.");
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
            StatePackaging statePackaging;
            try {
                statePackaging = em.getReference(StatePackaging.class, id);
                statePackaging.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The statePackaging with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PackagingCaffee> packagingCaffeeListOrphanCheck = statePackaging.getPackagingCaffeeList();
            for (PackagingCaffee packagingCaffeeListOrphanCheckPackagingCaffee : packagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This StatePackaging (" + statePackaging + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeListOrphanCheckPackagingCaffee + " in its packagingCaffeeList field has a non-nullable statePackagingId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(statePackaging);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StatePackaging> findStatePackagingEntities() {
        return findStatePackagingEntities(true, -1, -1);
    }

    public List<StatePackaging> findStatePackagingEntities(int maxResults, int firstResult) {
        return findStatePackagingEntities(false, maxResults, firstResult);
    }

    private List<StatePackaging> findStatePackagingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StatePackaging.class));
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

    public StatePackaging findStatePackaging(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StatePackaging.class, id);
        } finally {
            em.close();
        }
    }

    public int getStatePackagingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StatePackaging> rt = cq.from(StatePackaging.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
