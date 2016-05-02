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
import siscafe.model.ItemsServices;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class ItemsServicesJpaController implements Serializable {

    public ItemsServicesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItemsServices itemsServices) {
        if (itemsServices.getRelatedServicesList() == null) {
            itemsServices.setRelatedServicesList(new ArrayList<RelatedServices>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RelatedServices> attachedRelatedServicesList = new ArrayList<RelatedServices>();
            for (RelatedServices relatedServicesListRelatedServicesToAttach : itemsServices.getRelatedServicesList()) {
                relatedServicesListRelatedServicesToAttach = em.getReference(relatedServicesListRelatedServicesToAttach.getClass(), relatedServicesListRelatedServicesToAttach.getId());
                attachedRelatedServicesList.add(relatedServicesListRelatedServicesToAttach);
            }
            itemsServices.setRelatedServicesList(attachedRelatedServicesList);
            em.persist(itemsServices);
            for (RelatedServices relatedServicesListRelatedServices : itemsServices.getRelatedServicesList()) {
                ItemsServices oldItemsServicesIdOfRelatedServicesListRelatedServices = relatedServicesListRelatedServices.getItemsServicesId();
                relatedServicesListRelatedServices.setItemsServicesId(itemsServices);
                relatedServicesListRelatedServices = em.merge(relatedServicesListRelatedServices);
                if (oldItemsServicesIdOfRelatedServicesListRelatedServices != null) {
                    oldItemsServicesIdOfRelatedServicesListRelatedServices.getRelatedServicesList().remove(relatedServicesListRelatedServices);
                    oldItemsServicesIdOfRelatedServicesListRelatedServices = em.merge(oldItemsServicesIdOfRelatedServicesListRelatedServices);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItemsServices itemsServices) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ItemsServices persistentItemsServices = em.find(ItemsServices.class, itemsServices.getId());
            List<RelatedServices> relatedServicesListOld = persistentItemsServices.getRelatedServicesList();
            List<RelatedServices> relatedServicesListNew = itemsServices.getRelatedServicesList();
            List<String> illegalOrphanMessages = null;
            for (RelatedServices relatedServicesListOldRelatedServices : relatedServicesListOld) {
                if (!relatedServicesListNew.contains(relatedServicesListOldRelatedServices)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RelatedServices " + relatedServicesListOldRelatedServices + " since its itemsServicesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<RelatedServices> attachedRelatedServicesListNew = new ArrayList<RelatedServices>();
            for (RelatedServices relatedServicesListNewRelatedServicesToAttach : relatedServicesListNew) {
                relatedServicesListNewRelatedServicesToAttach = em.getReference(relatedServicesListNewRelatedServicesToAttach.getClass(), relatedServicesListNewRelatedServicesToAttach.getId());
                attachedRelatedServicesListNew.add(relatedServicesListNewRelatedServicesToAttach);
            }
            relatedServicesListNew = attachedRelatedServicesListNew;
            itemsServices.setRelatedServicesList(relatedServicesListNew);
            itemsServices = em.merge(itemsServices);
            for (RelatedServices relatedServicesListNewRelatedServices : relatedServicesListNew) {
                if (!relatedServicesListOld.contains(relatedServicesListNewRelatedServices)) {
                    ItemsServices oldItemsServicesIdOfRelatedServicesListNewRelatedServices = relatedServicesListNewRelatedServices.getItemsServicesId();
                    relatedServicesListNewRelatedServices.setItemsServicesId(itemsServices);
                    relatedServicesListNewRelatedServices = em.merge(relatedServicesListNewRelatedServices);
                    if (oldItemsServicesIdOfRelatedServicesListNewRelatedServices != null && !oldItemsServicesIdOfRelatedServicesListNewRelatedServices.equals(itemsServices)) {
                        oldItemsServicesIdOfRelatedServicesListNewRelatedServices.getRelatedServicesList().remove(relatedServicesListNewRelatedServices);
                        oldItemsServicesIdOfRelatedServicesListNewRelatedServices = em.merge(oldItemsServicesIdOfRelatedServicesListNewRelatedServices);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = itemsServices.getId();
                if (findItemsServices(id) == null) {
                    throw new NonexistentEntityException("The itemsServices with id " + id + " no longer exists.");
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
            ItemsServices itemsServices;
            try {
                itemsServices = em.getReference(ItemsServices.class, id);
                itemsServices.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itemsServices with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RelatedServices> relatedServicesListOrphanCheck = itemsServices.getRelatedServicesList();
            for (RelatedServices relatedServicesListOrphanCheckRelatedServices : relatedServicesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ItemsServices (" + itemsServices + ") cannot be destroyed since the RelatedServices " + relatedServicesListOrphanCheckRelatedServices + " in its relatedServicesList field has a non-nullable itemsServicesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(itemsServices);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ItemsServices> findItemsServicesEntities() {
        return findItemsServicesEntities(true, -1, -1);
    }

    public List<ItemsServices> findItemsServicesEntities(int maxResults, int firstResult) {
        return findItemsServicesEntities(false, maxResults, firstResult);
    }

    private List<ItemsServices> findItemsServicesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItemsServices.class));
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

    public ItemsServices findItemsServices(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItemsServices.class, id);
        } finally {
            em.close();
        }
    }

    public int getItemsServicesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItemsServices> rt = cq.from(ItemsServices.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
