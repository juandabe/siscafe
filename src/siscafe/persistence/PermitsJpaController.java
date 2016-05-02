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
import siscafe.model.CategoryPermits;
import siscafe.model.Profiles;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.Permits;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class PermitsJpaController implements Serializable {

    public PermitsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Permits permits) {
        if (permits.getProfilesList() == null) {
            permits.setProfilesList(new ArrayList<Profiles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CategoryPermits categoryPermitsId = permits.getCategoryPermitsId();
            if (categoryPermitsId != null) {
                categoryPermitsId = em.getReference(categoryPermitsId.getClass(), categoryPermitsId.getId());
                permits.setCategoryPermitsId(categoryPermitsId);
            }
            List<Profiles> attachedProfilesList = new ArrayList<Profiles>();
            for (Profiles profilesListProfilesToAttach : permits.getProfilesList()) {
                profilesListProfilesToAttach = em.getReference(profilesListProfilesToAttach.getClass(), profilesListProfilesToAttach.getId());
                attachedProfilesList.add(profilesListProfilesToAttach);
            }
            permits.setProfilesList(attachedProfilesList);
            em.persist(permits);
            if (categoryPermitsId != null) {
                categoryPermitsId.getPermitsList().add(permits);
                categoryPermitsId = em.merge(categoryPermitsId);
            }
            for (Profiles profilesListProfiles : permits.getProfilesList()) {
                profilesListProfiles.getPermitsList().add(permits);
                profilesListProfiles = em.merge(profilesListProfiles);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Permits permits) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Permits persistentPermits = em.find(Permits.class, permits.getId());
            CategoryPermits categoryPermitsIdOld = persistentPermits.getCategoryPermitsId();
            CategoryPermits categoryPermitsIdNew = permits.getCategoryPermitsId();
            List<Profiles> profilesListOld = persistentPermits.getProfilesList();
            List<Profiles> profilesListNew = permits.getProfilesList();
            if (categoryPermitsIdNew != null) {
                categoryPermitsIdNew = em.getReference(categoryPermitsIdNew.getClass(), categoryPermitsIdNew.getId());
                permits.setCategoryPermitsId(categoryPermitsIdNew);
            }
            List<Profiles> attachedProfilesListNew = new ArrayList<Profiles>();
            for (Profiles profilesListNewProfilesToAttach : profilesListNew) {
                profilesListNewProfilesToAttach = em.getReference(profilesListNewProfilesToAttach.getClass(), profilesListNewProfilesToAttach.getId());
                attachedProfilesListNew.add(profilesListNewProfilesToAttach);
            }
            profilesListNew = attachedProfilesListNew;
            permits.setProfilesList(profilesListNew);
            permits = em.merge(permits);
            if (categoryPermitsIdOld != null && !categoryPermitsIdOld.equals(categoryPermitsIdNew)) {
                categoryPermitsIdOld.getPermitsList().remove(permits);
                categoryPermitsIdOld = em.merge(categoryPermitsIdOld);
            }
            if (categoryPermitsIdNew != null && !categoryPermitsIdNew.equals(categoryPermitsIdOld)) {
                categoryPermitsIdNew.getPermitsList().add(permits);
                categoryPermitsIdNew = em.merge(categoryPermitsIdNew);
            }
            for (Profiles profilesListOldProfiles : profilesListOld) {
                if (!profilesListNew.contains(profilesListOldProfiles)) {
                    profilesListOldProfiles.getPermitsList().remove(permits);
                    profilesListOldProfiles = em.merge(profilesListOldProfiles);
                }
            }
            for (Profiles profilesListNewProfiles : profilesListNew) {
                if (!profilesListOld.contains(profilesListNewProfiles)) {
                    profilesListNewProfiles.getPermitsList().add(permits);
                    profilesListNewProfiles = em.merge(profilesListNewProfiles);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = permits.getId();
                if (findPermits(id) == null) {
                    throw new NonexistentEntityException("The permits with id " + id + " no longer exists.");
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
            Permits permits;
            try {
                permits = em.getReference(Permits.class, id);
                permits.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The permits with id " + id + " no longer exists.", enfe);
            }
            CategoryPermits categoryPermitsId = permits.getCategoryPermitsId();
            if (categoryPermitsId != null) {
                categoryPermitsId.getPermitsList().remove(permits);
                categoryPermitsId = em.merge(categoryPermitsId);
            }
            List<Profiles> profilesList = permits.getProfilesList();
            for (Profiles profilesListProfiles : profilesList) {
                profilesListProfiles.getPermitsList().remove(permits);
                profilesListProfiles = em.merge(profilesListProfiles);
            }
            em.remove(permits);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Permits> findPermitsEntities() {
        return findPermitsEntities(true, -1, -1);
    }

    public List<Permits> findPermitsEntities(int maxResults, int firstResult) {
        return findPermitsEntities(false, maxResults, firstResult);
    }

    private List<Permits> findPermitsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Permits.class));
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

    public Permits findPermits(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Permits.class, id);
        } finally {
            em.close();
        }
    }

    public int getPermitsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Permits> rt = cq.from(Permits.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
