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
import siscafe.model.PortOperators;
import siscafe.model.TypeContainer;
import siscafe.model.Customs;
import siscafe.model.NavyAgent;
import siscafe.model.ShippingLines;
import siscafe.model.RemittancesCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.PackagingCaffee;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

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

    public void create(PackagingCaffee packagingCaffee) throws PreexistingEntityException, Exception {
        if (packagingCaffee.getRemittancesCaffeeList() == null) {
            packagingCaffee.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PortOperators portOperatorsId = packagingCaffee.getPortOperatorsId();
            if (portOperatorsId != null) {
                portOperatorsId = em.getReference(portOperatorsId.getClass(), portOperatorsId.getId());
                packagingCaffee.setPortOperatorsId(portOperatorsId);
            }
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
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : packagingCaffee.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            packagingCaffee.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(packagingCaffee);
            if (portOperatorsId != null) {
                portOperatorsId.getPackagingCaffeeList().add(packagingCaffee);
                portOperatorsId = em.merge(portOperatorsId);
            }
            if (typeContainerId != null) {
                typeContainerId.getPackagingCaffeeList().add(packagingCaffee);
                typeContainerId = em.merge(typeContainerId);
            }
            if (customsId != null) {
                customsId.getPackagingCaffeeList().add(packagingCaffee);
                customsId = em.merge(customsId);
            }
            if (navyAgentId != null) {
                navyAgentId.getPackagingCaffeeoldList().add(packagingCaffee);
                navyAgentId = em.merge(navyAgentId);
            }
            if (shippingLinesId != null) {
                shippingLinesId.getPackagingCaffeeoldList().add(packagingCaffee);
                shippingLinesId = em.merge(shippingLinesId);
            }
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : packagingCaffee.getRemittancesCaffeeList()) {
                PackagingCaffee oldPackagingCaffeeIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getPackagingCaffeeId();
                remittancesCaffeeListRemittancesCaffee.setPackagingCaffeeId(packagingCaffee);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldPackagingCaffeeIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldPackagingCaffeeIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldPackagingCaffeeIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldPackagingCaffeeIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPackagingCaffee(packagingCaffee.getId()) != null) {
                throw new PreexistingEntityException("PackagingCaffee " + packagingCaffee + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PackagingCaffee packagingCaffee) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PackagingCaffee persistentPackagingCaffee = em.find(PackagingCaffee.class, packagingCaffee.getId());
            PortOperators portOperatorsIdOld = persistentPackagingCaffee.getPortOperatorsId();
            PortOperators portOperatorsIdNew = packagingCaffee.getPortOperatorsId();
            TypeContainer typeContainerIdOld = persistentPackagingCaffee.getTypeContainerId();
            TypeContainer typeContainerIdNew = packagingCaffee.getTypeContainerId();
            Customs customsIdOld = persistentPackagingCaffee.getCustomsId();
            Customs customsIdNew = packagingCaffee.getCustomsId();
            NavyAgent navyAgentIdOld = persistentPackagingCaffee.getNavyAgentId();
            NavyAgent navyAgentIdNew = packagingCaffee.getNavyAgentId();
            ShippingLines shippingLinesIdOld = persistentPackagingCaffee.getShippingLinesId();
            ShippingLines shippingLinesIdNew = packagingCaffee.getShippingLinesId();
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentPackagingCaffee.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = packagingCaffee.getRemittancesCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffee " + remittancesCaffeeListOldRemittancesCaffee + " since its packagingCaffeeId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (portOperatorsIdNew != null) {
                portOperatorsIdNew = em.getReference(portOperatorsIdNew.getClass(), portOperatorsIdNew.getId());
                packagingCaffee.setPortOperatorsId(portOperatorsIdNew);
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
            List<RemittancesCaffee> attachedRemittancesCaffeeListNew = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffeeToAttach : remittancesCaffeeListNew) {
                remittancesCaffeeListNewRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListNewRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListNewRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeListNew.add(remittancesCaffeeListNewRemittancesCaffeeToAttach);
            }
            remittancesCaffeeListNew = attachedRemittancesCaffeeListNew;
            packagingCaffee.setRemittancesCaffeeList(remittancesCaffeeListNew);
            packagingCaffee = em.merge(packagingCaffee);
            if (portOperatorsIdOld != null && !portOperatorsIdOld.equals(portOperatorsIdNew)) {
                portOperatorsIdOld.getPackagingCaffeeList().remove(packagingCaffee);
                portOperatorsIdOld = em.merge(portOperatorsIdOld);
            }
            if (portOperatorsIdNew != null && !portOperatorsIdNew.equals(portOperatorsIdOld)) {
                portOperatorsIdNew.getPackagingCaffeeList().add(packagingCaffee);
                portOperatorsIdNew = em.merge(portOperatorsIdNew);
            }
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
                navyAgentIdOld.getPackagingCaffeeoldList().remove(packagingCaffee);
                navyAgentIdOld = em.merge(navyAgentIdOld);
            }
            if (navyAgentIdNew != null && !navyAgentIdNew.equals(navyAgentIdOld)) {
                navyAgentIdNew.getPackagingCaffeeoldList().add(packagingCaffee);
                navyAgentIdNew = em.merge(navyAgentIdNew);
            }
            if (shippingLinesIdOld != null && !shippingLinesIdOld.equals(shippingLinesIdNew)) {
                shippingLinesIdOld.getPackagingCaffeeoldList().remove(packagingCaffee);
                shippingLinesIdOld = em.merge(shippingLinesIdOld);
            }
            if (shippingLinesIdNew != null && !shippingLinesIdNew.equals(shippingLinesIdOld)) {
                shippingLinesIdNew.getPackagingCaffeeoldList().add(packagingCaffee);
                shippingLinesIdNew = em.merge(shippingLinesIdNew);
            }
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    PackagingCaffee oldPackagingCaffeeIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getPackagingCaffeeId();
                    remittancesCaffeeListNewRemittancesCaffee.setPackagingCaffeeId(packagingCaffee);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldPackagingCaffeeIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldPackagingCaffeeIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(packagingCaffee)) {
                        oldPackagingCaffeeIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldPackagingCaffeeIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldPackagingCaffeeIdOfRemittancesCaffeeListNewRemittancesCaffee);
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
            List<RemittancesCaffee> remittancesCaffeeListOrphanCheck = packagingCaffee.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListOrphanCheckRemittancesCaffee : remittancesCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PackagingCaffee (" + packagingCaffee + ") cannot be destroyed since the RemittancesCaffee " + remittancesCaffeeListOrphanCheckRemittancesCaffee + " in its remittancesCaffeeList field has a non-nullable packagingCaffeeId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PortOperators portOperatorsId = packagingCaffee.getPortOperatorsId();
            if (portOperatorsId != null) {
                portOperatorsId.getPackagingCaffeeList().remove(packagingCaffee);
                portOperatorsId = em.merge(portOperatorsId);
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
                navyAgentId.getPackagingCaffeeoldList().remove(packagingCaffee);
                navyAgentId = em.merge(navyAgentId);
            }
            ShippingLines shippingLinesId = packagingCaffee.getShippingLinesId();
            if (shippingLinesId != null) {
                shippingLinesId.getPackagingCaffeeoldList().remove(packagingCaffee);
                shippingLinesId = em.merge(shippingLinesId);
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
            System.out.println("Find: "+id);
            return em.find(PackagingCaffee.class, id);
        } finally {
            em.close();
        }
    }
    
    public PackagingCaffee findPackingCaffeeByDEX(String DEX) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT pc FROM PackagingCaffee pc  WHERE pc.exportStatement=:DEX");
        query.setParameter("DEX", DEX);
        try {
            return (PackagingCaffee) query.getSingleResult();
        }
        catch(Exception e) {
            return null;
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
