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
import siscafe.model.RelatedServices;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.ServicesOrders;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class ServicesOrdersJpaController implements Serializable {

    public ServicesOrdersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ServicesOrders servicesOrders) throws PreexistingEntityException, Exception {
        if (servicesOrders.getRelatedServicesList() == null) {
            servicesOrders.setRelatedServicesList(new ArrayList<RelatedServices>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RelatedServices> attachedRelatedServicesList = new ArrayList<RelatedServices>();
            for (RelatedServices relatedServicesListRelatedServicesToAttach : servicesOrders.getRelatedServicesList()) {
                relatedServicesListRelatedServicesToAttach = em.getReference(relatedServicesListRelatedServicesToAttach.getClass(), relatedServicesListRelatedServicesToAttach.getId());
                attachedRelatedServicesList.add(relatedServicesListRelatedServicesToAttach);
            }
            servicesOrders.setRelatedServicesList(attachedRelatedServicesList);
            em.persist(servicesOrders);
            for (RelatedServices relatedServicesListRelatedServices : servicesOrders.getRelatedServicesList()) {
                ServicesOrders oldServicesOrdersIdOfRelatedServicesListRelatedServices = relatedServicesListRelatedServices.getServicesOrdersId();
                relatedServicesListRelatedServices.setServicesOrdersId(servicesOrders);
                relatedServicesListRelatedServices = em.merge(relatedServicesListRelatedServices);
                if (oldServicesOrdersIdOfRelatedServicesListRelatedServices != null) {
                    oldServicesOrdersIdOfRelatedServicesListRelatedServices.getRelatedServicesList().remove(relatedServicesListRelatedServices);
                    oldServicesOrdersIdOfRelatedServicesListRelatedServices = em.merge(oldServicesOrdersIdOfRelatedServicesListRelatedServices);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findServicesOrders(servicesOrders.getId()) != null) {
                throw new PreexistingEntityException("ServicesOrders " + servicesOrders + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ServicesOrders servicesOrders) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ServicesOrders persistentServicesOrders = em.find(ServicesOrders.class, servicesOrders.getId());
            List<RelatedServices> relatedServicesListOld = persistentServicesOrders.getRelatedServicesList();
            List<RelatedServices> relatedServicesListNew = servicesOrders.getRelatedServicesList();
            List<RelatedServices> attachedRelatedServicesListNew = new ArrayList<RelatedServices>();
            for (RelatedServices relatedServicesListNewRelatedServicesToAttach : relatedServicesListNew) {
                relatedServicesListNewRelatedServicesToAttach = em.getReference(relatedServicesListNewRelatedServicesToAttach.getClass(), relatedServicesListNewRelatedServicesToAttach.getId());
                attachedRelatedServicesListNew.add(relatedServicesListNewRelatedServicesToAttach);
            }
            relatedServicesListNew = attachedRelatedServicesListNew;
            servicesOrders.setRelatedServicesList(relatedServicesListNew);
            servicesOrders = em.merge(servicesOrders);
            for (RelatedServices relatedServicesListOldRelatedServices : relatedServicesListOld) {
                if (!relatedServicesListNew.contains(relatedServicesListOldRelatedServices)) {
                    relatedServicesListOldRelatedServices.setServicesOrdersId(null);
                    relatedServicesListOldRelatedServices = em.merge(relatedServicesListOldRelatedServices);
                }
            }
            for (RelatedServices relatedServicesListNewRelatedServices : relatedServicesListNew) {
                if (!relatedServicesListOld.contains(relatedServicesListNewRelatedServices)) {
                    ServicesOrders oldServicesOrdersIdOfRelatedServicesListNewRelatedServices = relatedServicesListNewRelatedServices.getServicesOrdersId();
                    relatedServicesListNewRelatedServices.setServicesOrdersId(servicesOrders);
                    relatedServicesListNewRelatedServices = em.merge(relatedServicesListNewRelatedServices);
                    if (oldServicesOrdersIdOfRelatedServicesListNewRelatedServices != null && !oldServicesOrdersIdOfRelatedServicesListNewRelatedServices.equals(servicesOrders)) {
                        oldServicesOrdersIdOfRelatedServicesListNewRelatedServices.getRelatedServicesList().remove(relatedServicesListNewRelatedServices);
                        oldServicesOrdersIdOfRelatedServicesListNewRelatedServices = em.merge(oldServicesOrdersIdOfRelatedServicesListNewRelatedServices);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = servicesOrders.getId();
                if (findServicesOrders(id) == null) {
                    throw new NonexistentEntityException("The servicesOrders with id " + id + " no longer exists.");
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
            ServicesOrders servicesOrders;
            try {
                servicesOrders = em.getReference(ServicesOrders.class, id);
                servicesOrders.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The servicesOrders with id " + id + " no longer exists.", enfe);
            }
            List<RelatedServices> relatedServicesList = servicesOrders.getRelatedServicesList();
            for (RelatedServices relatedServicesListRelatedServices : relatedServicesList) {
                relatedServicesListRelatedServices.setServicesOrdersId(null);
                relatedServicesListRelatedServices = em.merge(relatedServicesListRelatedServices);
            }
            em.remove(servicesOrders);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ServicesOrders> findServicesOrdersEntities() {
        return findServicesOrdersEntities(true, -1, -1);
    }

    public List<ServicesOrders> findServicesOrdersEntities(int maxResults, int firstResult) {
        return findServicesOrdersEntities(false, maxResults, firstResult);
    }

    private List<ServicesOrders> findServicesOrdersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServicesOrders.class));
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

    public ServicesOrders findServicesOrders(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServicesOrders.class, id);
        } finally {
            em.close();
        }
    }

    public int getServicesOrdersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServicesOrders> rt = cq.from(ServicesOrders.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
