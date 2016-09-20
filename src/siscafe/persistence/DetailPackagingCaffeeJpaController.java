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
import siscafe.model.DetailPackagingCaffee;
import siscafe.model.DetailPackagingCaffeePK;
import siscafe.model.RemittancesCaffee;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class DetailPackagingCaffeeJpaController implements Serializable {

    public DetailPackagingCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetailPackagingCaffee detailPackagingCaffee) throws PreexistingEntityException, Exception {
        if (detailPackagingCaffee.getDetailPackagingCaffeePK() == null) {
            detailPackagingCaffee.setDetailPackagingCaffeePK(new DetailPackagingCaffeePK());
        }
        detailPackagingCaffee.getDetailPackagingCaffeePK().setPackagingCaffeeId(detailPackagingCaffee.getPackagingCaffee().getId());
        detailPackagingCaffee.getDetailPackagingCaffeePK().setRemittancesCaffeeId(detailPackagingCaffee.getRemittancesCaffee().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RemittancesCaffee remittancesCaffee = detailPackagingCaffee.getRemittancesCaffee();
            if (remittancesCaffee != null) {
                remittancesCaffee = em.getReference(remittancesCaffee.getClass(), remittancesCaffee.getId());
                detailPackagingCaffee.setRemittancesCaffee(remittancesCaffee);
            }
            em.persist(detailPackagingCaffee);
            if (remittancesCaffee != null) {
                remittancesCaffee.getDetailPackagingCaffeeList().add(detailPackagingCaffee);
                remittancesCaffee = em.merge(remittancesCaffee);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetailPackagingCaffee(detailPackagingCaffee.getDetailPackagingCaffeePK()) != null) {
                throw new PreexistingEntityException("DetailPackagingCaffee " + detailPackagingCaffee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetailPackagingCaffee detailPackagingCaffee) throws NonexistentEntityException, Exception {
        detailPackagingCaffee.getDetailPackagingCaffeePK().setPackagingCaffeeId(detailPackagingCaffee.getPackagingCaffee().getId());
        detailPackagingCaffee.getDetailPackagingCaffeePK().setRemittancesCaffeeId(detailPackagingCaffee.getRemittancesCaffee().getId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetailPackagingCaffee persistentDetailPackagingCaffee = em.find(DetailPackagingCaffee.class, detailPackagingCaffee.getDetailPackagingCaffeePK());
            RemittancesCaffee remittancesCaffeeOld = persistentDetailPackagingCaffee.getRemittancesCaffee();
            RemittancesCaffee remittancesCaffeeNew = detailPackagingCaffee.getRemittancesCaffee();
            if (remittancesCaffeeNew != null) {
                remittancesCaffeeNew = em.getReference(remittancesCaffeeNew.getClass(), remittancesCaffeeNew.getId());
                detailPackagingCaffee.setRemittancesCaffee(remittancesCaffeeNew);
            }
            detailPackagingCaffee = em.merge(detailPackagingCaffee);
            if (remittancesCaffeeOld != null && !remittancesCaffeeOld.equals(remittancesCaffeeNew)) {
                remittancesCaffeeOld.getDetailPackagingCaffeeList().remove(detailPackagingCaffee);
                remittancesCaffeeOld = em.merge(remittancesCaffeeOld);
            }
            if (remittancesCaffeeNew != null && !remittancesCaffeeNew.equals(remittancesCaffeeOld)) {
                remittancesCaffeeNew.getDetailPackagingCaffeeList().add(detailPackagingCaffee);
                remittancesCaffeeNew = em.merge(remittancesCaffeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                DetailPackagingCaffeePK id = detailPackagingCaffee.getDetailPackagingCaffeePK();
                if (findDetailPackagingCaffee(id) == null) {
                    throw new NonexistentEntityException("The detailPackagingCaffee with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(DetailPackagingCaffeePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetailPackagingCaffee detailPackagingCaffee;
            try {
                detailPackagingCaffee = em.getReference(DetailPackagingCaffee.class, id);
                detailPackagingCaffee.getDetailPackagingCaffeePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detailPackagingCaffee with id " + id + " no longer exists.", enfe);
            }
            RemittancesCaffee remittancesCaffee = detailPackagingCaffee.getRemittancesCaffee();
            if (remittancesCaffee != null) {
                remittancesCaffee.getDetailPackagingCaffeeList().remove(detailPackagingCaffee);
                remittancesCaffee = em.merge(remittancesCaffee);
            }
            em.remove(detailPackagingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetailPackagingCaffee> findDetailPackagingCaffeeEntities() {
        return findDetailPackagingCaffeeEntities(true, -1, -1);
    }

    public List<DetailPackagingCaffee> findDetailPackagingCaffeeEntities(int maxResults, int firstResult) {
        return findDetailPackagingCaffeeEntities(false, maxResults, firstResult);
    }

    private List<DetailPackagingCaffee> findDetailPackagingCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetailPackagingCaffee.class));
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
    
    public List<DetailPackagingCaffee> findListDetailPackagingCaffeeByOIE(int oie){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT dpc FROM DetailPackagingCaffee dpc  WHERE dpc.packagingCaffee.id=:oiefind");
        query.setParameter("oiefind", oie);
        try {
            return (List<DetailPackagingCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    public List<DetailPackagingCaffee> findListDetailPackagingCaffeeByExportStatement(String exportStatement){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT dpc FROM DetailPackagingCaffee dpc  WHERE dpc.packagingCaffee.exportStatement=:exportstatement");
        query.setParameter("exportstatement", exportStatement);
        try {
            return (List<DetailPackagingCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    public List<DetailPackagingCaffee> findListDetailPackagingCaffeeByContainer(String bicContainer){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT dpc FROM DetailPackagingCaffee dpc  WHERE dpc.packagingCaffee.bicContainer=:bicContainer");
        query.setParameter("bicContainer", bicContainer);
        try {
            return (List<DetailPackagingCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List<DetailPackagingCaffee> findListDetailPackagingCaffeeByBooking(String booking){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT dpc FROM DetailPackagingCaffee dpc  WHERE dpc.packagingCaffee.bookingExpo=:booking");
        query.setParameter("booking", booking);
        try {
            return (List<DetailPackagingCaffee>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }

    public DetailPackagingCaffee findDetailPackagingCaffee(DetailPackagingCaffeePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetailPackagingCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetailPackagingCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetailPackagingCaffee> rt = cq.from(DetailPackagingCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
