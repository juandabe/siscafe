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
import siscafe.model.RemittancesCaffee;
import siscafe.model.WeighingDownloadCaffee;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class WeighingDownloadCaffeeJpaController implements Serializable {

    public WeighingDownloadCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WeighingDownloadCaffee weighingDownloadCaffee) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RemittancesCaffee remittancesCaffeeId = weighingDownloadCaffee.getRemittancesCaffeeId();
            if (remittancesCaffeeId != null) {
                remittancesCaffeeId = em.getReference(remittancesCaffeeId.getClass(), remittancesCaffeeId.getId());
                weighingDownloadCaffee.setRemittancesCaffeeId(remittancesCaffeeId);
            }
            em.persist(weighingDownloadCaffee);
            if (remittancesCaffeeId != null) {
                remittancesCaffeeId.getWeighingDownloadCaffeeList().add(weighingDownloadCaffee);
                remittancesCaffeeId = em.merge(remittancesCaffeeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WeighingDownloadCaffee weighingDownloadCaffee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WeighingDownloadCaffee persistentWeighingDownloadCaffee = em.find(WeighingDownloadCaffee.class, weighingDownloadCaffee.getId());
            RemittancesCaffee remittancesCaffeeIdOld = persistentWeighingDownloadCaffee.getRemittancesCaffeeId();
            RemittancesCaffee remittancesCaffeeIdNew = weighingDownloadCaffee.getRemittancesCaffeeId();
            if (remittancesCaffeeIdNew != null) {
                remittancesCaffeeIdNew = em.getReference(remittancesCaffeeIdNew.getClass(), remittancesCaffeeIdNew.getId());
                weighingDownloadCaffee.setRemittancesCaffeeId(remittancesCaffeeIdNew);
            }
            weighingDownloadCaffee = em.merge(weighingDownloadCaffee);
            if (remittancesCaffeeIdOld != null && !remittancesCaffeeIdOld.equals(remittancesCaffeeIdNew)) {
                remittancesCaffeeIdOld.getWeighingDownloadCaffeeList().remove(weighingDownloadCaffee);
                remittancesCaffeeIdOld = em.merge(remittancesCaffeeIdOld);
            }
            if (remittancesCaffeeIdNew != null && !remittancesCaffeeIdNew.equals(remittancesCaffeeIdOld)) {
                remittancesCaffeeIdNew.getWeighingDownloadCaffeeList().add(weighingDownloadCaffee);
                remittancesCaffeeIdNew = em.merge(remittancesCaffeeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = weighingDownloadCaffee.getId();
                if (findWeighingDownloadCaffee(id) == null) {
                    throw new NonexistentEntityException("The weighingDownloadCaffee with id " + id + " no longer exists.");
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
            WeighingDownloadCaffee weighingDownloadCaffee;
            try {
                weighingDownloadCaffee = em.getReference(WeighingDownloadCaffee.class, id);
                weighingDownloadCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The weighingDownloadCaffee with id " + id + " no longer exists.", enfe);
            }
            RemittancesCaffee remittancesCaffeeId = weighingDownloadCaffee.getRemittancesCaffeeId();
            if (remittancesCaffeeId != null) {
                remittancesCaffeeId.getWeighingDownloadCaffeeList().remove(weighingDownloadCaffee);
                remittancesCaffeeId = em.merge(remittancesCaffeeId);
            }
            em.remove(weighingDownloadCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WeighingDownloadCaffee> findWeighingDownloadCaffeeEntities() {
        return findWeighingDownloadCaffeeEntities(true, -1, -1);
    }

    public List<WeighingDownloadCaffee> findWeighingDownloadCaffeeEntities(int maxResults, int firstResult) {
        return findWeighingDownloadCaffeeEntities(false, maxResults, firstResult);
    }

    public List<WeighingDownloadCaffee> findWeighingDownloadCaffeeEntitiesByRemettances(RemittancesCaffee remittancesCaffee) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT wrc FROM WeighingDownloadCaffee wrc where wrc.remittancesCaffeeId=:remittancesCaffee");
        query.setParameter("remittancesCaffee", remittancesCaffee);
        try {
            return (List<WeighingDownloadCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public Object countPalletByRemettencesCaffee(int remittancesCaffeeId) {
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT COUNT(wrc.id) FROM WeighingDownloadCaffee wrc WHERE wrc.remittancesCaffeeId.id=:remittancesCaffeeId");
        query.setParameter("remittancesCaffeeId", remittancesCaffeeId);
        try {
            System.out.println("data: "+query.getSingleResult());
            return query.getSingleResult();
        }
        catch(Exception e) {
            return 0;
        }
    }
    
    public Object countBagsByRemettencesCaffee(int remittancesCaffeeId) {
        
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT SUM(wrc.quantityBagPallet) FROM WeighingDownloadCaffee wrc WHERE wrc.remittancesCaffeeId.id=:remittancesCaffeeId");
        query.setParameter("remittancesCaffeeId", remittancesCaffeeId);
        try {
            return query.getSingleResult();
        }
        catch(Exception e) {
            return 0;
        }
    }

    private List<WeighingDownloadCaffee> findWeighingDownloadCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WeighingDownloadCaffee.class));
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

    public WeighingDownloadCaffee findWeighingDownloadCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WeighingDownloadCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getWeighingDownloadCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WeighingDownloadCaffee> rt = cq.from(WeighingDownloadCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
