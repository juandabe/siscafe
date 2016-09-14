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
import siscafe.controller.exceptions.IllegalOrphanException;
import siscafe.controller.exceptions.NonexistentEntityException;
import siscafe.model.NavyAgent;

/**
 *
 * @author Administrador
 */
public class NavyAgentJpaController implements Serializable {

    public NavyAgentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NavyAgent navyAgent) {
        if (navyAgent.getPackagingCaffeeList() == null) {
            navyAgent.setPackagingCaffeeList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListPackagingCaffeeToAttach : navyAgent.getPackagingCaffeeList()) {
                packagingCaffeeListPackagingCaffeeToAttach = em.getReference(packagingCaffeeListPackagingCaffeeToAttach.getClass(), packagingCaffeeListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeList.add(packagingCaffeeListPackagingCaffeeToAttach);
            }
            navyAgent.setPackagingCaffeeList(attachedPackagingCaffeeList);
            em.persist(navyAgent);
            for (PackagingCaffee packagingCaffeeListPackagingCaffee : navyAgent.getPackagingCaffeeList()) {
                NavyAgent oldNavyAgentIdOfPackagingCaffeeListPackagingCaffee = packagingCaffeeListPackagingCaffee.getNavyAgentId();
                packagingCaffeeListPackagingCaffee.setNavyAgentId(navyAgent);
                packagingCaffeeListPackagingCaffee = em.merge(packagingCaffeeListPackagingCaffee);
                if (oldNavyAgentIdOfPackagingCaffeeListPackagingCaffee != null) {
                    oldNavyAgentIdOfPackagingCaffeeListPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListPackagingCaffee);
                    oldNavyAgentIdOfPackagingCaffeeListPackagingCaffee = em.merge(oldNavyAgentIdOfPackagingCaffeeListPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NavyAgent navyAgent) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            NavyAgent persistentNavyAgent = em.find(NavyAgent.class, navyAgent.getId());
            List<PackagingCaffee> packagingCaffeeListOld = persistentNavyAgent.getPackagingCaffeeList();
            List<PackagingCaffee> packagingCaffeeListNew = navyAgent.getPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeListOldPackagingCaffee : packagingCaffeeListOld) {
                if (!packagingCaffeeListNew.contains(packagingCaffeeListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeListOldPackagingCaffee + " since its navyAgentId field is not nullable.");
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
            navyAgent.setPackagingCaffeeList(packagingCaffeeListNew);
            navyAgent = em.merge(navyAgent);
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffee : packagingCaffeeListNew) {
                if (!packagingCaffeeListOld.contains(packagingCaffeeListNewPackagingCaffee)) {
                    NavyAgent oldNavyAgentIdOfPackagingCaffeeListNewPackagingCaffee = packagingCaffeeListNewPackagingCaffee.getNavyAgentId();
                    packagingCaffeeListNewPackagingCaffee.setNavyAgentId(navyAgent);
                    packagingCaffeeListNewPackagingCaffee = em.merge(packagingCaffeeListNewPackagingCaffee);
                    if (oldNavyAgentIdOfPackagingCaffeeListNewPackagingCaffee != null && !oldNavyAgentIdOfPackagingCaffeeListNewPackagingCaffee.equals(navyAgent)) {
                        oldNavyAgentIdOfPackagingCaffeeListNewPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListNewPackagingCaffee);
                        oldNavyAgentIdOfPackagingCaffeeListNewPackagingCaffee = em.merge(oldNavyAgentIdOfPackagingCaffeeListNewPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = navyAgent.getId();
                if (findNavyAgent(id) == null) {
                    throw new NonexistentEntityException("The navyAgent with id " + id + " no longer exists.");
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
            NavyAgent navyAgent;
            try {
                navyAgent = em.getReference(NavyAgent.class, id);
                navyAgent.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The navyAgent with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PackagingCaffee> packagingCaffeeListOrphanCheck = navyAgent.getPackagingCaffeeList();
            for (PackagingCaffee packagingCaffeeListOrphanCheckPackagingCaffee : packagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NavyAgent (" + navyAgent + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeListOrphanCheckPackagingCaffee + " in its packagingCaffeeList field has a non-nullable navyAgentId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(navyAgent);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NavyAgent> findNavyAgentEntities() {
        return findNavyAgentEntities(true, -1, -1);
    }

    public List<NavyAgent> findNavyAgentEntities(int maxResults, int firstResult) {
        return findNavyAgentEntities(false, maxResults, firstResult);
    }

    private List<NavyAgent> findNavyAgentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NavyAgent.class));
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

    public NavyAgent findNavyAgent(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NavyAgent.class, id);
        } finally {
            em.close();
        }
    }

    public int getNavyAgentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NavyAgent> rt = cq.from(NavyAgent.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
