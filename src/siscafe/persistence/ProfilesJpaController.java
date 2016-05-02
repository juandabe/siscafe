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
import siscafe.model.Permits;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.Profiles;
import siscafe.model.Users;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class ProfilesJpaController implements Serializable {

    public ProfilesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profiles profiles) {
        if (profiles.getPermitsList() == null) {
            profiles.setPermitsList(new ArrayList<Permits>());
        }
        if (profiles.getUsersList() == null) {
            profiles.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Permits> attachedPermitsList = new ArrayList<Permits>();
            for (Permits permitsListPermitsToAttach : profiles.getPermitsList()) {
                permitsListPermitsToAttach = em.getReference(permitsListPermitsToAttach.getClass(), permitsListPermitsToAttach.getId());
                attachedPermitsList.add(permitsListPermitsToAttach);
            }
            profiles.setPermitsList(attachedPermitsList);
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : profiles.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getId());
                attachedUsersList.add(usersListUsersToAttach);
            }
            profiles.setUsersList(attachedUsersList);
            em.persist(profiles);
            for (Permits permitsListPermits : profiles.getPermitsList()) {
                permitsListPermits.getProfilesList().add(profiles);
                permitsListPermits = em.merge(permitsListPermits);
            }
            for (Users usersListUsers : profiles.getUsersList()) {
                Profiles oldProfilesIdOfUsersListUsers = usersListUsers.getProfilesId();
                usersListUsers.setProfilesId(profiles);
                usersListUsers = em.merge(usersListUsers);
                if (oldProfilesIdOfUsersListUsers != null) {
                    oldProfilesIdOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldProfilesIdOfUsersListUsers = em.merge(oldProfilesIdOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profiles profiles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profiles persistentProfiles = em.find(Profiles.class, profiles.getId());
            List<Permits> permitsListOld = persistentProfiles.getPermitsList();
            List<Permits> permitsListNew = profiles.getPermitsList();
            List<Users> usersListOld = persistentProfiles.getUsersList();
            List<Users> usersListNew = profiles.getUsersList();
            List<String> illegalOrphanMessages = null;
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Users " + usersListOldUsers + " since its profilesId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permits> attachedPermitsListNew = new ArrayList<Permits>();
            for (Permits permitsListNewPermitsToAttach : permitsListNew) {
                permitsListNewPermitsToAttach = em.getReference(permitsListNewPermitsToAttach.getClass(), permitsListNewPermitsToAttach.getId());
                attachedPermitsListNew.add(permitsListNewPermitsToAttach);
            }
            permitsListNew = attachedPermitsListNew;
            profiles.setPermitsList(permitsListNew);
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getId());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            profiles.setUsersList(usersListNew);
            profiles = em.merge(profiles);
            for (Permits permitsListOldPermits : permitsListOld) {
                if (!permitsListNew.contains(permitsListOldPermits)) {
                    permitsListOldPermits.getProfilesList().remove(profiles);
                    permitsListOldPermits = em.merge(permitsListOldPermits);
                }
            }
            for (Permits permitsListNewPermits : permitsListNew) {
                if (!permitsListOld.contains(permitsListNewPermits)) {
                    permitsListNewPermits.getProfilesList().add(profiles);
                    permitsListNewPermits = em.merge(permitsListNewPermits);
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Profiles oldProfilesIdOfUsersListNewUsers = usersListNewUsers.getProfilesId();
                    usersListNewUsers.setProfilesId(profiles);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldProfilesIdOfUsersListNewUsers != null && !oldProfilesIdOfUsersListNewUsers.equals(profiles)) {
                        oldProfilesIdOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldProfilesIdOfUsersListNewUsers = em.merge(oldProfilesIdOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profiles.getId();
                if (findProfiles(id) == null) {
                    throw new NonexistentEntityException("The profiles with id " + id + " no longer exists.");
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
            Profiles profiles;
            try {
                profiles = em.getReference(Profiles.class, id);
                profiles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profiles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Users> usersListOrphanCheck = profiles.getUsersList();
            for (Users usersListOrphanCheckUsers : usersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Profiles (" + profiles + ") cannot be destroyed since the Users " + usersListOrphanCheckUsers + " in its usersList field has a non-nullable profilesId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Permits> permitsList = profiles.getPermitsList();
            for (Permits permitsListPermits : permitsList) {
                permitsListPermits.getProfilesList().remove(profiles);
                permitsListPermits = em.merge(permitsListPermits);
            }
            em.remove(profiles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profiles> findProfilesEntities() {
        return findProfilesEntities(true, -1, -1);
    }

    public List<Profiles> findProfilesEntities(int maxResults, int firstResult) {
        return findProfilesEntities(false, maxResults, firstResult);
    }

    private List<Profiles> findProfilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profiles.class));
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

    public Profiles findProfiles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profiles> rt = cq.from(Profiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
