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
import siscafe.model.PortOperators;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class PortOperatorsJpaController1 implements Serializable {

    public PortOperatorsJpaController1(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PortOperators portOperators) {
        if (portOperators.getPackagingCaffeeList() == null) {
            portOperators.setPackagingCaffeeList(new ArrayList<PackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PackagingCaffee> attachedPackagingCaffeeList = new ArrayList<PackagingCaffee>();
            for (PackagingCaffee packagingCaffeeListPackagingCaffeeToAttach : portOperators.getPackagingCaffeeList()) {
                packagingCaffeeListPackagingCaffeeToAttach = em.getReference(packagingCaffeeListPackagingCaffeeToAttach.getClass(), packagingCaffeeListPackagingCaffeeToAttach.getId());
                attachedPackagingCaffeeList.add(packagingCaffeeListPackagingCaffeeToAttach);
            }
            portOperators.setPackagingCaffeeList(attachedPackagingCaffeeList);
            em.persist(portOperators);
            for (PackagingCaffee packagingCaffeeListPackagingCaffee : portOperators.getPackagingCaffeeList()) {
                PortOperators oldPortOperatorsIdOfPackagingCaffeeListPackagingCaffee = packagingCaffeeListPackagingCaffee.getPortOperatorsId();
                packagingCaffeeListPackagingCaffee.setPortOperatorsId(portOperators);
                packagingCaffeeListPackagingCaffee = em.merge(packagingCaffeeListPackagingCaffee);
                if (oldPortOperatorsIdOfPackagingCaffeeListPackagingCaffee != null) {
                    oldPortOperatorsIdOfPackagingCaffeeListPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListPackagingCaffee);
                    oldPortOperatorsIdOfPackagingCaffeeListPackagingCaffee = em.merge(oldPortOperatorsIdOfPackagingCaffeeListPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PortOperators portOperators) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PortOperators persistentPortOperators = em.find(PortOperators.class, portOperators.getId());
            List<PackagingCaffee> packagingCaffeeListOld = persistentPortOperators.getPackagingCaffeeList();
            List<PackagingCaffee> packagingCaffeeListNew = portOperators.getPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (PackagingCaffee packagingCaffeeListOldPackagingCaffee : packagingCaffeeListOld) {
                if (!packagingCaffeeListNew.contains(packagingCaffeeListOldPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PackagingCaffee " + packagingCaffeeListOldPackagingCaffee + " since its portOperatorsId field is not nullable.");
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
            portOperators.setPackagingCaffeeList(packagingCaffeeListNew);
            portOperators = em.merge(portOperators);
            for (PackagingCaffee packagingCaffeeListNewPackagingCaffee : packagingCaffeeListNew) {
                if (!packagingCaffeeListOld.contains(packagingCaffeeListNewPackagingCaffee)) {
                    PortOperators oldPortOperatorsIdOfPackagingCaffeeListNewPackagingCaffee = packagingCaffeeListNewPackagingCaffee.getPortOperatorsId();
                    packagingCaffeeListNewPackagingCaffee.setPortOperatorsId(portOperators);
                    packagingCaffeeListNewPackagingCaffee = em.merge(packagingCaffeeListNewPackagingCaffee);
                    if (oldPortOperatorsIdOfPackagingCaffeeListNewPackagingCaffee != null && !oldPortOperatorsIdOfPackagingCaffeeListNewPackagingCaffee.equals(portOperators)) {
                        oldPortOperatorsIdOfPackagingCaffeeListNewPackagingCaffee.getPackagingCaffeeList().remove(packagingCaffeeListNewPackagingCaffee);
                        oldPortOperatorsIdOfPackagingCaffeeListNewPackagingCaffee = em.merge(oldPortOperatorsIdOfPackagingCaffeeListNewPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = portOperators.getId();
                if (findPortOperators(id) == null) {
                    throw new NonexistentEntityException("The portOperators with id " + id + " no longer exists.");
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
            PortOperators portOperators;
            try {
                portOperators = em.getReference(PortOperators.class, id);
                portOperators.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The portOperators with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PackagingCaffee> packagingCaffeeListOrphanCheck = portOperators.getPackagingCaffeeList();
            for (PackagingCaffee packagingCaffeeListOrphanCheckPackagingCaffee : packagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PortOperators (" + portOperators + ") cannot be destroyed since the PackagingCaffee " + packagingCaffeeListOrphanCheckPackagingCaffee + " in its packagingCaffeeList field has a non-nullable portOperatorsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(portOperators);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PortOperators> findPortOperatorsEntities() {
        return findPortOperatorsEntities(true, -1, -1);
    }

    public List<PortOperators> findPortOperatorsEntities(int maxResults, int firstResult) {
        return findPortOperatorsEntities(false, maxResults, firstResult);
    }

    private List<PortOperators> findPortOperatorsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PortOperators.class));
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

    public PortOperators findPortOperators(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PortOperators.class, id);
        } finally {
            em.close();
        }
    }

    public int getPortOperatorsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PortOperators> rt = cq.from(PortOperators.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
