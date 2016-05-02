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
import siscafe.model.RemittancesCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.MarkCaffee;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class MarkCaffeeJpaController implements Serializable {

    public MarkCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MarkCaffee markCaffee) {
        if (markCaffee.getRemittancesCaffeeList() == null) {
            markCaffee.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : markCaffee.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            markCaffee.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(markCaffee);
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : markCaffee.getRemittancesCaffeeList()) {
                MarkCaffee oldMarkCafeeIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getMarkCafeeId();
                remittancesCaffeeListRemittancesCaffee.setMarkCafeeId(markCaffee);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldMarkCafeeIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldMarkCafeeIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldMarkCafeeIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldMarkCafeeIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MarkCaffee markCaffee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MarkCaffee persistentMarkCaffee = em.find(MarkCaffee.class, markCaffee.getId());
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentMarkCaffee.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = markCaffee.getRemittancesCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffee " + remittancesCaffeeListOldRemittancesCaffee + " since its markCafeeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RemittancesCaffee> attachedRemittancesCaffeeListNew = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffeeToAttach : remittancesCaffeeListNew) {
                remittancesCaffeeListNewRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListNewRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListNewRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeListNew.add(remittancesCaffeeListNewRemittancesCaffeeToAttach);
            }
            remittancesCaffeeListNew = attachedRemittancesCaffeeListNew;
            markCaffee.setRemittancesCaffeeList(remittancesCaffeeListNew);
            markCaffee = em.merge(markCaffee);
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    MarkCaffee oldMarkCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getMarkCafeeId();
                    remittancesCaffeeListNewRemittancesCaffee.setMarkCafeeId(markCaffee);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldMarkCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldMarkCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(markCaffee)) {
                        oldMarkCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldMarkCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldMarkCafeeIdOfRemittancesCaffeeListNewRemittancesCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = markCaffee.getId();
                if (findMarkCaffee(id) == null) {
                    throw new NonexistentEntityException("The markCaffee with id " + id + " no longer exists.");
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
            MarkCaffee markCaffee;
            try {
                markCaffee = em.getReference(MarkCaffee.class, id);
                markCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The markCaffee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RemittancesCaffee> remittancesCaffeeListOrphanCheck = markCaffee.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListOrphanCheckRemittancesCaffee : remittancesCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MarkCaffee (" + markCaffee + ") cannot be destroyed since the RemittancesCaffee " + remittancesCaffeeListOrphanCheckRemittancesCaffee + " in its remittancesCaffeeList field has a non-nullable markCafeeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(markCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MarkCaffee> findMarkCaffeeEntities() {
        return findMarkCaffeeEntities(true, -1, -1);
    }

    public List<MarkCaffee> findMarkCaffeeEntities(int maxResults, int firstResult) {
        return findMarkCaffeeEntities(false, maxResults, firstResult);
    }

    private List<MarkCaffee> findMarkCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MarkCaffee.class));
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

    public MarkCaffee findMarkCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MarkCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getMarkCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MarkCaffee> rt = cq.from(MarkCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
