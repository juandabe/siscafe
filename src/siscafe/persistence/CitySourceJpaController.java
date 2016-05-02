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
import siscafe.model.CitySource;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author Administrador
 */
public class CitySourceJpaController implements Serializable {

    public CitySourceJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CitySource citySource) throws PreexistingEntityException, Exception {
        if (citySource.getRemittancesCaffeeList() == null) {
            citySource.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : citySource.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            citySource.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(citySource);
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : citySource.getRemittancesCaffeeList()) {
                CitySource oldCitySourceIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getCitySourceId();
                remittancesCaffeeListRemittancesCaffee.setCitySourceId(citySource);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldCitySourceIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldCitySourceIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldCitySourceIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldCitySourceIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCitySource(citySource.getId()) != null) {
                throw new PreexistingEntityException("CitySource " + citySource + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CitySource citySource) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CitySource persistentCitySource = em.find(CitySource.class, citySource.getId());
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentCitySource.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = citySource.getRemittancesCaffeeList();
            List<String> illegalOrphanMessages = null;
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain RemittancesCaffee " + remittancesCaffeeListOldRemittancesCaffee + " since its citySourceId field is not nullable.");
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
            citySource.setRemittancesCaffeeList(remittancesCaffeeListNew);
            citySource = em.merge(citySource);
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    CitySource oldCitySourceIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getCitySourceId();
                    remittancesCaffeeListNewRemittancesCaffee.setCitySourceId(citySource);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldCitySourceIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldCitySourceIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(citySource)) {
                        oldCitySourceIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldCitySourceIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldCitySourceIdOfRemittancesCaffeeListNewRemittancesCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = citySource.getId();
                if (findCitySource(id) == null) {
                    throw new NonexistentEntityException("The citySource with id " + id + " no longer exists.");
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
            CitySource citySource;
            try {
                citySource = em.getReference(CitySource.class, id);
                citySource.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The citySource with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<RemittancesCaffee> remittancesCaffeeListOrphanCheck = citySource.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListOrphanCheckRemittancesCaffee : remittancesCaffeeListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CitySource (" + citySource + ") cannot be destroyed since the RemittancesCaffee " + remittancesCaffeeListOrphanCheckRemittancesCaffee + " in its remittancesCaffeeList field has a non-nullable citySourceId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(citySource);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CitySource> findCitySourceEntities() {
        return findCitySourceEntities(true, -1, -1);
    }

    public List<CitySource> findCitySourceEntities(int maxResults, int firstResult) {
        return findCitySourceEntities(false, maxResults, firstResult);
    }

    private List<CitySource> findCitySourceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CitySource.class));
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

    public CitySource findCitySource(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CitySource.class, id);
        } finally {
            em.close();
        }
    }

    public int getCitySourceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CitySource> rt = cq.from(CitySource.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
