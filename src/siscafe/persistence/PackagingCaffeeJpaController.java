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
import siscafe.model.TypeContainer;
import siscafe.model.Customs;
import siscafe.model.NavyAgent;
import siscafe.model.ShippingLines;
import siscafe.model.StatePackaging;
import siscafe.model.AdictionalElementsHasPackagingCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.PackagingCaffee;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class PackagingCaffeeJpaController implements Serializable {

    public PackagingCaffeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public int create(PackagingCaffee packagingCaffee) {
        if (packagingCaffee.getAdictionalElementsHasPackagingCaffeeList() == null) {
            packagingCaffee.setAdictionalElementsHasPackagingCaffeeList(new ArrayList<AdictionalElementsHasPackagingCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TypeContainer typeContainerId = packagingCaffee.getTypeContainerId();
            if (typeContainerId != null) {
                typeContainerId = em.getReference(typeContainerId.getClass(), typeContainerId.getId());
                packagingCaffee.setTypeContainerId(typeContainerId);
            }
            Customs customsId = packagingCaffee.getCustomsId();
            if (customsId != null) {
                customsId = em.getReference(customsId.getClass(), customsId.getId());
                packagingCaffee.setCustomsId(customsId);
            }
            NavyAgent navyAgentId = packagingCaffee.getNavyAgentId();
            if (navyAgentId != null) {
                navyAgentId = em.getReference(navyAgentId.getClass(), navyAgentId.getId());
                packagingCaffee.setNavyAgentId(navyAgentId);
            }
            ShippingLines shippingLinesId = packagingCaffee.getShippingLinesId();
            if (shippingLinesId != null) {
                shippingLinesId = em.getReference(shippingLinesId.getClass(), shippingLinesId.getId());
                packagingCaffee.setShippingLinesId(shippingLinesId);
            }
            StatePackaging statePackagingId = packagingCaffee.getStatePackagingId();
            if (statePackagingId != null) {
                statePackagingId = em.getReference(statePackagingId.getClass(), statePackagingId.getId());
                packagingCaffee.setStatePackagingId(statePackagingId);
            }
            List<AdictionalElementsHasPackagingCaffee> attachedAdictionalElementsHasPackagingCaffeeList = new ArrayList<AdictionalElementsHasPackagingCaffee>();
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach : packagingCaffee.getAdictionalElementsHasPackagingCaffeeList()) {
                adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach = em.getReference(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach.getClass(), adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach.getAdictionalElementsHasPackagingCaffeePK());
                attachedAdictionalElementsHasPackagingCaffeeList.add(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffeeToAttach);
            }
            packagingCaffee.setAdictionalElementsHasPackagingCaffeeList(attachedAdictionalElementsHasPackagingCaffeeList);
            em.persist(packagingCaffee);
            if (typeContainerId != null) {
                typeContainerId.getPackagingCaffeeList().add(packagingCaffee);
                typeContainerId = em.merge(typeContainerId);
            }
            if (customsId != null) {
                customsId.getPackagingCaffeeList().add(packagingCaffee);
                customsId = em.merge(customsId);
            }
            if (navyAgentId != null) {
                navyAgentId.getPackagingCaffeeList().add(packagingCaffee);
                navyAgentId = em.merge(navyAgentId);
            }
            if (shippingLinesId != null) {
                shippingLinesId.getPackagingCaffeeList().add(packagingCaffee);
                shippingLinesId = em.merge(shippingLinesId);
            }
            if (statePackagingId != null) {
                statePackagingId.getPackagingCaffeeList().add(packagingCaffee);
                statePackagingId = em.merge(statePackagingId);
            }
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee : packagingCaffee.getAdictionalElementsHasPackagingCaffeeList()) {
                PackagingCaffee oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee = adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee.getPackagingCaffee();
                adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee.setPackagingCaffee(packagingCaffee);
                adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee = em.merge(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee);
                if (oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee != null) {
                    oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee);
                    oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee = em.merge(oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListAdictionalElementsHasPackagingCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return packagingCaffee.getId();
    }

    public void edit(PackagingCaffee packagingCaffee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PackagingCaffee persistentPackagingCaffee = em.find(PackagingCaffee.class, packagingCaffee.getId());
            TypeContainer typeContainerIdOld = persistentPackagingCaffee.getTypeContainerId();
            TypeContainer typeContainerIdNew = packagingCaffee.getTypeContainerId();
            Customs customsIdOld = persistentPackagingCaffee.getCustomsId();
            Customs customsIdNew = packagingCaffee.getCustomsId();
            NavyAgent navyAgentIdOld = persistentPackagingCaffee.getNavyAgentId();
            NavyAgent navyAgentIdNew = packagingCaffee.getNavyAgentId();
            ShippingLines shippingLinesIdOld = persistentPackagingCaffee.getShippingLinesId();
            ShippingLines shippingLinesIdNew = packagingCaffee.getShippingLinesId();
            StatePackaging statePackagingIdOld = persistentPackagingCaffee.getStatePackagingId();
            StatePackaging statePackagingIdNew = packagingCaffee.getStatePackagingId();
            List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeListOld = persistentPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList();
            List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeListNew = packagingCaffee.getAdictionalElementsHasPackagingCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListOldAdictionalElementsHasPackagingCaffee : adictionalElementsHasPackagingCaffeeListOld) {
                if (!adictionalElementsHasPackagingCaffeeListNew.contains(adictionalElementsHasPackagingCaffeeListOldAdictionalElementsHasPackagingCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AdictionalElementsHasPackagingCaffee " + adictionalElementsHasPackagingCaffeeListOldAdictionalElementsHasPackagingCaffee + " since its packagingCaffee field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (typeContainerIdNew != null) {
                typeContainerIdNew = em.getReference(typeContainerIdNew.getClass(), typeContainerIdNew.getId());
                packagingCaffee.setTypeContainerId(typeContainerIdNew);
            }
            if (customsIdNew != null) {
                customsIdNew = em.getReference(customsIdNew.getClass(), customsIdNew.getId());
                packagingCaffee.setCustomsId(customsIdNew);
            }
            if (navyAgentIdNew != null) {
                navyAgentIdNew = em.getReference(navyAgentIdNew.getClass(), navyAgentIdNew.getId());
                packagingCaffee.setNavyAgentId(navyAgentIdNew);
            }
            if (shippingLinesIdNew != null) {
                shippingLinesIdNew = em.getReference(shippingLinesIdNew.getClass(), shippingLinesIdNew.getId());
                packagingCaffee.setShippingLinesId(shippingLinesIdNew);
            }
            if (statePackagingIdNew != null) {
                statePackagingIdNew = em.getReference(statePackagingIdNew.getClass(), statePackagingIdNew.getId());
                packagingCaffee.setStatePackagingId(statePackagingIdNew);
            }
            List<AdictionalElementsHasPackagingCaffee> attachedAdictionalElementsHasPackagingCaffeeListNew = new ArrayList<AdictionalElementsHasPackagingCaffee>();
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach : adictionalElementsHasPackagingCaffeeListNew) {
                adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach = em.getReference(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach.getClass(), adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach.getAdictionalElementsHasPackagingCaffeePK());
                attachedAdictionalElementsHasPackagingCaffeeListNew.add(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffeeToAttach);
            }
            adictionalElementsHasPackagingCaffeeListNew = attachedAdictionalElementsHasPackagingCaffeeListNew;
            packagingCaffee.setAdictionalElementsHasPackagingCaffeeList(adictionalElementsHasPackagingCaffeeListNew);
            packagingCaffee = em.merge(packagingCaffee);
            if (typeContainerIdOld != null && !typeContainerIdOld.equals(typeContainerIdNew)) {
                typeContainerIdOld.getPackagingCaffeeList().remove(packagingCaffee);
                typeContainerIdOld = em.merge(typeContainerIdOld);
            }
            if (typeContainerIdNew != null && !typeContainerIdNew.equals(typeContainerIdOld)) {
                typeContainerIdNew.getPackagingCaffeeList().add(packagingCaffee);
                typeContainerIdNew = em.merge(typeContainerIdNew);
            }
            if (customsIdOld != null && !customsIdOld.equals(customsIdNew)) {
                customsIdOld.getPackagingCaffeeList().remove(packagingCaffee);
                customsIdOld = em.merge(customsIdOld);
            }
            if (customsIdNew != null && !customsIdNew.equals(customsIdOld)) {
                customsIdNew.getPackagingCaffeeList().add(packagingCaffee);
                customsIdNew = em.merge(customsIdNew);
            }
            if (navyAgentIdOld != null && !navyAgentIdOld.equals(navyAgentIdNew)) {
                navyAgentIdOld.getPackagingCaffeeList().remove(packagingCaffee);
                navyAgentIdOld = em.merge(navyAgentIdOld);
            }
            if (navyAgentIdNew != null && !navyAgentIdNew.equals(navyAgentIdOld)) {
                navyAgentIdNew.getPackagingCaffeeList().add(packagingCaffee);
                navyAgentIdNew = em.merge(navyAgentIdNew);
            }
            if (shippingLinesIdOld != null && !shippingLinesIdOld.equals(shippingLinesIdNew)) {
                shippingLinesIdOld.getPackagingCaffeeList().remove(packagingCaffee);
                shippingLinesIdOld = em.merge(shippingLinesIdOld);
            }
            if (shippingLinesIdNew != null && !shippingLinesIdNew.equals(shippingLinesIdOld)) {
                shippingLinesIdNew.getPackagingCaffeeList().add(packagingCaffee);
                shippingLinesIdNew = em.merge(shippingLinesIdNew);
            }
            if (statePackagingIdOld != null && !statePackagingIdOld.equals(statePackagingIdNew)) {
                statePackagingIdOld.getPackagingCaffeeList().remove(packagingCaffee);
                statePackagingIdOld = em.merge(statePackagingIdOld);
            }
            if (statePackagingIdNew != null && !statePackagingIdNew.equals(statePackagingIdOld)) {
                statePackagingIdNew.getPackagingCaffeeList().add(packagingCaffee);
                statePackagingIdNew = em.merge(statePackagingIdNew);
            }
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee : adictionalElementsHasPackagingCaffeeListNew) {
                if (!adictionalElementsHasPackagingCaffeeListOld.contains(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee)) {
                    PackagingCaffee oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee = adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.getPackagingCaffee();
                    adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.setPackagingCaffee(packagingCaffee);
                    adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee = em.merge(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee);
                    if (oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee != null && !oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.equals(packagingCaffee)) {
                        oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee.getAdictionalElementsHasPackagingCaffeeList().remove(adictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee);
                        oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee = em.merge(oldPackagingCaffeeOfAdictionalElementsHasPackagingCaffeeListNewAdictionalElementsHasPackagingCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = packagingCaffee.getId();
                if (findPackagingCaffee(id) == null) {
                    throw new NonexistentEntityException("The packagingCaffee with id " + id + " no longer exists.");
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
            PackagingCaffee packagingCaffee;
            try {
                packagingCaffee = em.getReference(PackagingCaffee.class, id);
                packagingCaffee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The packagingCaffee with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AdictionalElementsHasPackagingCaffee> adictionalElementsHasPackagingCaffeeListOrphanCheck = packagingCaffee.getAdictionalElementsHasPackagingCaffeeList();
            for (AdictionalElementsHasPackagingCaffee adictionalElementsHasPackagingCaffeeListOrphanCheckAdictionalElementsHasPackagingCaffee : adictionalElementsHasPackagingCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PackagingCaffee (" + packagingCaffee + ") cannot be destroyed since the AdictionalElementsHasPackagingCaffee " + adictionalElementsHasPackagingCaffeeListOrphanCheckAdictionalElementsHasPackagingCaffee + " in its adictionalElementsHasPackagingCaffeeList field has a non-nullable packagingCaffee field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TypeContainer typeContainerId = packagingCaffee.getTypeContainerId();
            if (typeContainerId != null) {
                typeContainerId.getPackagingCaffeeList().remove(packagingCaffee);
                typeContainerId = em.merge(typeContainerId);
            }
            Customs customsId = packagingCaffee.getCustomsId();
            if (customsId != null) {
                customsId.getPackagingCaffeeList().remove(packagingCaffee);
                customsId = em.merge(customsId);
            }
            NavyAgent navyAgentId = packagingCaffee.getNavyAgentId();
            if (navyAgentId != null) {
                navyAgentId.getPackagingCaffeeList().remove(packagingCaffee);
                navyAgentId = em.merge(navyAgentId);
            }
            ShippingLines shippingLinesId = packagingCaffee.getShippingLinesId();
            if (shippingLinesId != null) {
                shippingLinesId.getPackagingCaffeeList().remove(packagingCaffee);
                shippingLinesId = em.merge(shippingLinesId);
            }
            StatePackaging statePackagingId = packagingCaffee.getStatePackagingId();
            if (statePackagingId != null) {
                statePackagingId.getPackagingCaffeeList().remove(packagingCaffee);
                statePackagingId = em.merge(statePackagingId);
            }
            em.remove(packagingCaffee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PackagingCaffee> findPackagingCaffeeEntities() {
        return findPackagingCaffeeEntities(true, -1, -1);
    }

    public List<PackagingCaffee> findPackagingCaffeeEntities(int maxResults, int firstResult) {
        return findPackagingCaffeeEntities(false, maxResults, firstResult);
    }

    private List<PackagingCaffee> findPackagingCaffeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PackagingCaffee.class));
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

    public PackagingCaffee findPackagingCaffee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PackagingCaffee.class, id);
        } finally {
            em.close();
        }
    }

    public int getPackagingCaffeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PackagingCaffee> rt = cq.from(PackagingCaffee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
