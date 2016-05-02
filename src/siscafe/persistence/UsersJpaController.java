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
import siscafe.persistence.exceptions.NonexistentEntityException;
import siscafe.model.Departaments;
import siscafe.model.Profiles;
import siscafe.model.Users;

/**
 *
 * @author Administrador
 */
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departaments departamentsId = users.getDepartamentsId();
            if (departamentsId != null) {
                departamentsId = em.getReference(departamentsId.getClass(), departamentsId.getId());
                users.setDepartamentsId(departamentsId);
            }
            Profiles profilesId = users.getProfilesId();
            if (profilesId != null) {
                profilesId = em.getReference(profilesId.getClass(), profilesId.getId());
                users.setProfilesId(profilesId);
            }
            em.persist(users);
            if (departamentsId != null) {
                departamentsId.getUsersList().add(users);
                departamentsId = em.merge(departamentsId);
            }
            if (profilesId != null) {
                profilesId.getUsersList().add(users);
                profilesId = em.merge(profilesId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Departaments departamentsIdOld = persistentUsers.getDepartamentsId();
            Departaments departamentsIdNew = users.getDepartamentsId();
            Profiles profilesIdOld = persistentUsers.getProfilesId();
            Profiles profilesIdNew = users.getProfilesId();
            if (departamentsIdNew != null) {
                departamentsIdNew = em.getReference(departamentsIdNew.getClass(), departamentsIdNew.getId());
                users.setDepartamentsId(departamentsIdNew);
            }
            if (profilesIdNew != null) {
                profilesIdNew = em.getReference(profilesIdNew.getClass(), profilesIdNew.getId());
                users.setProfilesId(profilesIdNew);
            }
            users = em.merge(users);
            if (departamentsIdOld != null && !departamentsIdOld.equals(departamentsIdNew)) {
                departamentsIdOld.getUsersList().remove(users);
                departamentsIdOld = em.merge(departamentsIdOld);
            }
            if (departamentsIdNew != null && !departamentsIdNew.equals(departamentsIdOld)) {
                departamentsIdNew.getUsersList().add(users);
                departamentsIdNew = em.merge(departamentsIdNew);
            }
            if (profilesIdOld != null && !profilesIdOld.equals(profilesIdNew)) {
                profilesIdOld.getUsersList().remove(users);
                profilesIdOld = em.merge(profilesIdOld);
            }
            if (profilesIdNew != null && !profilesIdNew.equals(profilesIdOld)) {
                profilesIdNew.getUsersList().add(users);
                profilesIdNew = em.merge(profilesIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Departaments departamentsId = users.getDepartamentsId();
            if (departamentsId != null) {
                departamentsId.getUsersList().remove(users);
                departamentsId = em.merge(departamentsId);
            }
            Profiles profilesId = users.getProfilesId();
            if (profilesId != null) {
                profilesId.getUsersList().remove(users);
                profilesId = em.merge(profilesId);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public Users findUsersByAccess(String username, String password) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM Users u  WHERE u.username=:username and u.password=:password and u.active=true");
        query.setParameter("username", username);
        query.setParameter("password", password);
        try {
            return (Users) query.getSingleResult();
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public List <Users> findUsersByProfiles(Profiles profiles) {
        EntityManager em = getEntityManager();
        Query query = em.createQuery("SELECT u FROM Users u  WHERE u.profilesId=:profiles and u.active=true");
        query.setParameter("profiles", profiles);
        try {
            return (List<Users>) query.getResultList();
        }
        catch(Exception e) {
            return null;
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
