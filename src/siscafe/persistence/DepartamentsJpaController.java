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
import siscafe.model.Users;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.Departaments;
import siscafe.persistence.exceptions.IllegalOrphanException;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class DepartamentsJpaController implements Serializable {

    public DepartamentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departaments departaments) {
        if (departaments.getUsersList() == null) {
            departaments.setUsersList(new ArrayList<Users>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : departaments.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getId());
                attachedUsersList.add(usersListUsersToAttach);
            }
            departaments.setUsersList(attachedUsersList);
            em.persist(departaments);
            for (Users usersListUsers : departaments.getUsersList()) {
                Departaments oldDepartamentsIdOfUsersListUsers = usersListUsers.getDepartamentsId();
                usersListUsers.setDepartamentsId(departaments);
                usersListUsers = em.merge(usersListUsers);
                if (oldDepartamentsIdOfUsersListUsers != null) {
                    oldDepartamentsIdOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldDepartamentsIdOfUsersListUsers = em.merge(oldDepartamentsIdOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departaments departaments) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departaments persistentDepartaments = em.find(Departaments.class, departaments.getId());
            List<Users> usersListOld = persistentDepartaments.getUsersList();
            List<Users> usersListNew = departaments.getUsersList();
            List<String> illegalOrphanMessages = null;
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Users " + usersListOldUsers + " since its departamentsId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getId());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            departaments.setUsersList(usersListNew);
            departaments = em.merge(departaments);
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Departaments oldDepartamentsIdOfUsersListNewUsers = usersListNewUsers.getDepartamentsId();
                    usersListNewUsers.setDepartamentsId(departaments);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldDepartamentsIdOfUsersListNewUsers != null && !oldDepartamentsIdOfUsersListNewUsers.equals(departaments)) {
                        oldDepartamentsIdOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldDepartamentsIdOfUsersListNewUsers = em.merge(oldDepartamentsIdOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departaments.getId();
                if (findDepartaments(id) == null) {
                    throw new NonexistentEntityException("The departaments with id " + id + " no longer exists.");
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
            Departaments departaments;
            try {
                departaments = em.getReference(Departaments.class, id);
                departaments.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departaments with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Users> usersListOrphanCheck = departaments.getUsersList();
            for (Users usersListOrphanCheckUsers : usersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departaments (" + departaments + ") cannot be destroyed since the Users " + usersListOrphanCheckUsers + " in its usersList field has a non-nullable departamentsId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departaments);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departaments> findDepartamentsEntities() {
        return findDepartamentsEntities(true, -1, -1);
    }

    public List<Departaments> findDepartamentsEntities(int maxResults, int firstResult) {
        return findDepartamentsEntities(false, maxResults, firstResult);
    }

    private List<Departaments> findDepartamentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departaments.class));
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

    public Departaments findDepartaments(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departaments.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departaments> rt = cq.from(Departaments.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
