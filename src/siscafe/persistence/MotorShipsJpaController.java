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
import siscafe.model.ShippingLines;
import siscafe.model.RemittancesCaffee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import siscafe.model.MotorShips;
import siscafe.persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Administrador
 */
public class MotorShipsJpaController implements Serializable {

    public MotorShipsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MotorShips motorShips) {
        if (motorShips.getRemittancesCaffeeList() == null) {
            motorShips.setRemittancesCaffeeList(new ArrayList<RemittancesCaffee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ShippingLines linesId = motorShips.getLinesId();
            if (linesId != null) {
                linesId = em.getReference(linesId.getClass(), linesId.getId());
                motorShips.setLinesId(linesId);
            }
            List<RemittancesCaffee> attachedRemittancesCaffeeList = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffeeToAttach : motorShips.getRemittancesCaffeeList()) {
                remittancesCaffeeListRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeList.add(remittancesCaffeeListRemittancesCaffeeToAttach);
            }
            motorShips.setRemittancesCaffeeList(attachedRemittancesCaffeeList);
            em.persist(motorShips);
            if (linesId != null) {
                linesId.getMotorShipsList().add(motorShips);
                linesId = em.merge(linesId);
            }
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : motorShips.getRemittancesCaffeeList()) {
                MotorShips oldMotorShipsIdOfRemittancesCaffeeListRemittancesCaffee = remittancesCaffeeListRemittancesCaffee.getMotorShipsId();
                remittancesCaffeeListRemittancesCaffee.setMotorShipsId(motorShips);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
                if (oldMotorShipsIdOfRemittancesCaffeeListRemittancesCaffee != null) {
                    oldMotorShipsIdOfRemittancesCaffeeListRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListRemittancesCaffee);
                    oldMotorShipsIdOfRemittancesCaffeeListRemittancesCaffee = em.merge(oldMotorShipsIdOfRemittancesCaffeeListRemittancesCaffee);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MotorShips motorShips) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MotorShips persistentMotorShips = em.find(MotorShips.class, motorShips.getId());
            ShippingLines linesIdOld = persistentMotorShips.getLinesId();
            ShippingLines linesIdNew = motorShips.getLinesId();
            List<RemittancesCaffee> remittancesCaffeeListOld = persistentMotorShips.getRemittancesCaffeeList();
            List<RemittancesCaffee> remittancesCaffeeListNew = motorShips.getRemittancesCaffeeList();
            if (linesIdNew != null) {
                linesIdNew = em.getReference(linesIdNew.getClass(), linesIdNew.getId());
                motorShips.setLinesId(linesIdNew);
            }
            List<RemittancesCaffee> attachedRemittancesCaffeeListNew = new ArrayList<RemittancesCaffee>();
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffeeToAttach : remittancesCaffeeListNew) {
                remittancesCaffeeListNewRemittancesCaffeeToAttach = em.getReference(remittancesCaffeeListNewRemittancesCaffeeToAttach.getClass(), remittancesCaffeeListNewRemittancesCaffeeToAttach.getId());
                attachedRemittancesCaffeeListNew.add(remittancesCaffeeListNewRemittancesCaffeeToAttach);
            }
            remittancesCaffeeListNew = attachedRemittancesCaffeeListNew;
            motorShips.setRemittancesCaffeeList(remittancesCaffeeListNew);
            motorShips = em.merge(motorShips);
            if (linesIdOld != null && !linesIdOld.equals(linesIdNew)) {
                linesIdOld.getMotorShipsList().remove(motorShips);
                linesIdOld = em.merge(linesIdOld);
            }
            if (linesIdNew != null && !linesIdNew.equals(linesIdOld)) {
                linesIdNew.getMotorShipsList().add(motorShips);
                linesIdNew = em.merge(linesIdNew);
            }
            for (RemittancesCaffee remittancesCaffeeListOldRemittancesCaffee : remittancesCaffeeListOld) {
                if (!remittancesCaffeeListNew.contains(remittancesCaffeeListOldRemittancesCaffee)) {
                    remittancesCaffeeListOldRemittancesCaffee.setMotorShipsId(null);
                    remittancesCaffeeListOldRemittancesCaffee = em.merge(remittancesCaffeeListOldRemittancesCaffee);
                }
            }
            for (RemittancesCaffee remittancesCaffeeListNewRemittancesCaffee : remittancesCaffeeListNew) {
                if (!remittancesCaffeeListOld.contains(remittancesCaffeeListNewRemittancesCaffee)) {
                    MotorShips oldMotorShipsIdOfRemittancesCaffeeListNewRemittancesCaffee = remittancesCaffeeListNewRemittancesCaffee.getMotorShipsId();
                    remittancesCaffeeListNewRemittancesCaffee.setMotorShipsId(motorShips);
                    remittancesCaffeeListNewRemittancesCaffee = em.merge(remittancesCaffeeListNewRemittancesCaffee);
                    if (oldMotorShipsIdOfRemittancesCaffeeListNewRemittancesCaffee != null && !oldMotorShipsIdOfRemittancesCaffeeListNewRemittancesCaffee.equals(motorShips)) {
                        oldMotorShipsIdOfRemittancesCaffeeListNewRemittancesCaffee.getRemittancesCaffeeList().remove(remittancesCaffeeListNewRemittancesCaffee);
                        oldMotorShipsIdOfRemittancesCaffeeListNewRemittancesCaffee = em.merge(oldMotorShipsIdOfRemittancesCaffeeListNewRemittancesCaffee);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = motorShips.getId();
                if (findMotorShips(id) == null) {
                    throw new NonexistentEntityException("The motorShips with id " + id + " no longer exists.");
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
            MotorShips motorShips;
            try {
                motorShips = em.getReference(MotorShips.class, id);
                motorShips.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motorShips with id " + id + " no longer exists.", enfe);
            }
            ShippingLines linesId = motorShips.getLinesId();
            if (linesId != null) {
                linesId.getMotorShipsList().remove(motorShips);
                linesId = em.merge(linesId);
            }
            List<RemittancesCaffee> remittancesCaffeeList = motorShips.getRemittancesCaffeeList();
            for (RemittancesCaffee remittancesCaffeeListRemittancesCaffee : remittancesCaffeeList) {
                remittancesCaffeeListRemittancesCaffee.setMotorShipsId(null);
                remittancesCaffeeListRemittancesCaffee = em.merge(remittancesCaffeeListRemittancesCaffee);
            }
            em.remove(motorShips);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MotorShips> findMotorShipsEntities() {
        return findMotorShipsEntities(true, -1, -1);
    }

    public List<MotorShips> findMotorShipsEntities(int maxResults, int firstResult) {
        return findMotorShipsEntities(false, maxResults, firstResult);
    }

    private List<MotorShips> findMotorShipsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MotorShips.class));
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

    public MotorShips findMotorShips(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MotorShips.class, id);
        } finally {
            em.close();
        }
    }

    public int getMotorShipsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MotorShips> rt = cq.from(MotorShips.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
