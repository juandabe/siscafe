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
import siscafe.model.RemittancesCaffeeHasNoveltysCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.NoveltysCaffee;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class NoveltysCaffeeJpaController implements Serializable {

    public NoveltysCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(NoveltysCaffee noveltysCaffee) {
        if (noveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList() == null) {
            noveltysCaffee.setRemittancesCaffeeHasNoveltysCaffeeList(new ArrayList<RemittancesCaffeeHasNoveltysCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RemittancesCaffeeHasNoveltysCaffee> attachedRemittancesCaffeeHasNoveltysCaffeeList = new ArrayList<RemittancesCaffeeHasNoveltysCaffee>();
            for (RemittancesCaffeeHasNoveltysCaffee remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffeeToAttach : noveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList()) {
                remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffeeToAttach = em.getReference(remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffeeToAttach.getClass(), remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffeeToAttach.getRemittancesCaffeeHasNoveltysCaffeePK());
                attachedRemittancesCaffeeHasNoveltysCaffeeList.add(remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffeeToAttach);
            }
            noveltysCaffee.setRemittancesCaffeeHasNoveltysCaffeeList(attachedRemittancesCaffeeHasNoveltysCaffeeList);
            em.persist(noveltysCaffee);
            for (RemittancesCaffeeHasNoveltysCaffee remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee : noveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList()) {
                NoveltysCaffee oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee = remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee.getNoveltysCaffee();
                remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee.setNoveltysCaffee(noveltysCaffee);
                remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee = em.merge(remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee);
                if (oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee != null) {
                    oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList().remove(remittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee);
                    oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee = em.merge(oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListRemittancesCaffeeHasNoveltysCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(NoveltysCaffee noveltysCaffee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            NoveltysCaffee persistentNoveltysCaffee = em.find(NoveltysCaffee.class, noveltysCaffee.getId());
            List<RemittancesCaffeeHasNoveltysCaffee> remittancesCaffeeHasNoveltysCaffeeListOld = persistentNoveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList();
            List<RemittancesCaffeeHasNoveltysCaffee> remittancesCaffeeHasNoveltysCaffeeListNew = noveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffeeHasNoveltysCaffee remittancesCaffeeHasNoveltysCaffeeListOldRemittancesCaffeeHasNoveltysCaffee : remittancesCaffeeHasNoveltysCaffeeListOld) {
                if (!remittancesCaffeeHasNoveltysCaffeeListNew.contains(remittancesCaffeeHasNoveltysCaffeeListOldRemittancesCaffeeHasNoveltysCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffeeHasNoveltysCaffee " + remittancesCaffeeHasNoveltysCaffeeListOldRemittancesCaffeeHasNoveltysCaffee + " since its noveltysCaffee field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RemittancesCaffeeHasNoveltysCaffee> attachedRemittancesCaffeeHasNoveltysCaffeeListNew = new ArrayList<RemittancesCaffeeHasNoveltysCaffee>();
            for (RemittancesCaffeeHasNoveltysCaffee remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffeeToAttach : remittancesCaffeeHasNoveltysCaffeeListNew) {
                remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffeeToAttach = em.getReference(remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffeeToAttach.getClass(), remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffeeToAttach.getRemittancesCaffeeHasNoveltysCaffeePK());
                attachedRemittancesCaffeeHasNoveltysCaffeeListNew.add(remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffeeToAttach);
            }
            remittancesCaffeeHasNoveltysCaffeeListNew = attachedRemittancesCaffeeHasNoveltysCaffeeListNew;
            noveltysCaffee.setRemittancesCaffeeHasNoveltysCaffeeList(remittancesCaffeeHasNoveltysCaffeeListNew);
            noveltysCaffee = em.merge(noveltysCaffee);
            for (RemittancesCaffeeHasNoveltysCaffee remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee : remittancesCaffeeHasNoveltysCaffeeListNew) {
                if (!remittancesCaffeeHasNoveltysCaffeeListOld.contains(remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee)) {
                    NoveltysCaffee oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee = remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee.getNoveltysCaffee();
                    remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee.setNoveltysCaffee(noveltysCaffee);
                    remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee = em.merge(remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee);
                    if (oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee != null && !oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee.equals(noveltysCaffee)) {
                        oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList().remove(remittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee);
                        oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee = em.merge(oldNoveltysCaffeeOfRemittancesCaffeeHasNoveltysCaffeeListNewRemittancesCaffeeHasNoveltysCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = noveltysCaffee.getId();
                if (findNoveltysCaffee(id) == null) {
                    throw new NonexistentEntityException("The noveltysCaffee with id " + id + " no longer exists.");
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
            NoveltysCaffee noveltysCaffee;
            try {
                noveltysCaffee = em.getReference(NoveltysCaffee.class, id);
                noveltysCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The noveltysCaffee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RemittancesCaffeeHasNoveltysCaffee> remittancesCaffeeHasNoveltysCaffeeListOrphanCheck = noveltysCaffee.getRemittancesCaffeeHasNoveltysCaffeeList();
            for (RemittancesCaffeeHasNoveltysCaffee remittancesCaffeeHasNoveltysCaffeeListOrphanCheckRemittancesCaffeeHasNoveltysCaffee : remittancesCaffeeHasNoveltysCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This NoveltysCaffee (" + noveltysCaffee + ") cannot be destroyed since the RemittancesCaffeeHasNoveltysCaffee " + remittancesCaffeeHasNoveltysCaffeeListOrphanCheckRemittancesCaffeeHasNoveltysCaffee + " in its remittancesCaffeeHasNoveltysCaffeeList field has a non-nullable noveltysCaffee field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(noveltysCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<NoveltysCaffee> findNoveltysCaffeeEntities() {
        return findNoveltysCaffeeEntities(true, -1, -1);
    }

    public List<NoveltysCaffee> findNoveltysCaffeeEntities(int maxResults, int firstResult) {
        return findNoveltysCaffeeEntities(false, maxResults, firstResult);
    }

    private List<NoveltysCaffee> findNoveltysCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(NoveltysCaffee.class));
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

    public NoveltysCaffee findNoveltysCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(NoveltysCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getNoveltysCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<NoveltysCaffee> rt = cq.from(NoveltysCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
