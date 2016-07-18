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
import siscafe.model.NavyAgent;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class NavyAgentJpaController1 implements Serializable {

    public NavyAgentJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NavyAgent navyAgent) throws PreexistingEntityException, Exception {
        if (navyAgent.getPackagingCaffeeoldList() == null) {
            navyAgent.setPackagingCaffeeoldList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeoldList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeoldListPackagingCaffeeToAttach : navyAgent.getPackagingCaffeeoldList()) {
                packagingCaffeeoldListPackagingCaffeeToAttach = em.getReference(packagingCaffeeoldListPackagingCaffeeToAttach.getClass(), packagingCaffeeoldListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeoldList.add(packagingCaffeeoldListPackagingCaffeeToAttach);
            }
            navyAgent.setPackagingCaffeeoldList(attachedPackagingCaffeeoldList);
            em.persist(navyAgent);
            for (PackagingCaffee packagingCaffeeoldListPackagingCaffee : navyAgent.getPackagingCaffeeoldList()) {
                NavyAgent oldNavyAgentIdOfPackagingCaffeeoldListPackagingCaffee = packagingCaffeeoldListPackagingCaffee.getNavyAgentId();
                packagingCaffeeoldListPackagingCaffee.setNavyAgentId(navyAgent);
                packagingCaffeeoldListPackagingCaffee = em.merge(packagingCaffeeoldListPackagingCaffee);
                if (oldNavyAgentIdOfPackagingCaffeeoldListPackagingCaffee != null) {
                    oldNavyAgentIdOfPackagingCaffeeoldListPackagingCaffee.getPackagingCaffeeoldList().remove(packagingCaffeeoldListPackagingCaffee);
                    oldNavyAgentIdOfPackagingCaffeeoldListPackagingCaffee = em.merge(oldNavyAgentIdOfPackagingCaffeeoldListPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findNavyAgent(navyAgent.getId()) != null) {
                throw new PreexistingEntityException("NavyAgent " + navyAgent + " already exists.", ex);
            }
            throw ex;
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
            List<PackagingCaffee> packagingCaffeeoldListOld = persistentNavyAgent.getPackagingCaffeeoldList();
            List<PackagingCaffee> packagingCaffeeoldListNew = navyAgent.getPackagingCaffeeoldList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeoldListOldPackagingCaffee : packagingCaffeeoldListOld) {
                if (!packagingCaffeeoldListNew.contains(packagingCaffeeoldListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeoldListOldPackagingCaffee + " since its navyAgentId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PackagingCaffee> attachedPackagingCaffeeoldListNew = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeoldListNewPackagingCaffeeToAttach : packagingCaffeeoldListNew) {
                packagingCaffeeoldListNewPackagingCaffeeToAttach = em.getReference(packagingCaffeeoldListNewPackagingCaffeeToAttach.getClass(), packagingCaffeeoldListNewPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeoldListNew.add(packagingCaffeeoldListNewPackagingCaffeeToAttach);
            }
            packagingCaffeeoldListNew = attachedPackagingCaffeeoldListNew;
            navyAgent.setPackagingCaffeeoldList(packagingCaffeeoldListNew);
            navyAgent = em.merge(navyAgent);
            for (PackagingCaffee packagingCaffeeoldListNewPackagingCaffee : packagingCaffeeoldListNew) {
                if (!packagingCaffeeoldListOld.contains(packagingCaffeeoldListNewPackagingCaffee)) {
                    NavyAgent oldNavyAgentIdOfPackagingCaffeeoldListNewPackagingCaffee = packagingCaffeeoldListNewPackagingCaffee.getNavyAgentId();
                    packagingCaffeeoldListNewPackagingCaffee.setNavyAgentId(navyAgent);
                    packagingCaffeeoldListNewPackagingCaffee = em.merge(packagingCaffeeoldListNewPackagingCaffee);
                    if (oldNavyAgentIdOfPackagingCaffeeoldListNewPackagingCaffee != null && !oldNavyAgentIdOfPackagingCaffeeoldListNewPackagingCaffee.equals(navyAgent)) {
                        oldNavyAgentIdOfPackagingCaffeeoldListNewPackagingCaffee.getPackagingCaffeeoldList().remove(packagingCaffeeoldListNewPackagingCaffee);
                        oldNavyAgentIdOfPackagingCaffeeoldListNewPackagingCaffee = em.merge(oldNavyAgentIdOfPackagingCaffeeoldListNewPackagingCaffee);
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
            List<PackagingCaffee> packagingCaffeeoldListOrphanCheck = navyAgent.getPackagingCaffeeoldList();
            for (PackagingCaffee packagingCaffeeoldListOrphanCheckPackagingCaffee : packagingCaffeeoldListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NavyAgent (" + navyAgent + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeoldListOrphanCheckPackagingCaffee + " in its packagingCaffeeoldList field has a non-nullable navyAgentId field.");
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
