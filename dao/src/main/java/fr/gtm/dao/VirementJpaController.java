/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.dao;

import fr.gtm.dao.exceptions.NonexistentEntityException;
import fr.gtm.dao.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import fr.gtm.domaine.Compte;
import fr.gtm.domaine.Virement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nina
 */
public class VirementJpaController implements Serializable {

    public VirementJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Virement virement) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compte idcomptedebit = virement.getIdcomptedebit();
            if (idcomptedebit != null) {
                idcomptedebit = em.getReference(idcomptedebit.getClass(), idcomptedebit.getIdcompte());
                virement.setIdcomptedebit(idcomptedebit);
            }
            Compte idcomptecredit = virement.getIdcomptecredit();
            if (idcomptecredit != null) {
                idcomptecredit = em.getReference(idcomptecredit.getClass(), idcomptecredit.getIdcompte());
                virement.setIdcomptecredit(idcomptecredit);
            }
            em.persist(virement);
            if (idcomptedebit != null) {
                idcomptedebit.getVirementCollection().add(virement);
                idcomptedebit = em.merge(idcomptedebit);
            }
            if (idcomptecredit != null) {
                idcomptecredit.getVirementCollection().add(virement);
                idcomptecredit = em.merge(idcomptecredit);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVirement(virement.getIdvirement()) != null) {
                throw new PreexistingEntityException("Virement " + virement + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Virement virement) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Virement persistentVirement = em.find(Virement.class, virement.getIdvirement());
            Compte idcomptedebitOld = persistentVirement.getIdcomptedebit();
            Compte idcomptedebitNew = virement.getIdcomptedebit();
            Compte idcomptecreditOld = persistentVirement.getIdcomptecredit();
            Compte idcomptecreditNew = virement.getIdcomptecredit();
            if (idcomptedebitNew != null) {
                idcomptedebitNew = em.getReference(idcomptedebitNew.getClass(), idcomptedebitNew.getIdcompte());
                virement.setIdcomptedebit(idcomptedebitNew);
            }
            if (idcomptecreditNew != null) {
                idcomptecreditNew = em.getReference(idcomptecreditNew.getClass(), idcomptecreditNew.getIdcompte());
                virement.setIdcomptecredit(idcomptecreditNew);
            }
            virement = em.merge(virement);
            if (idcomptedebitOld != null && !idcomptedebitOld.equals(idcomptedebitNew)) {
                idcomptedebitOld.getVirementCollection().remove(virement);
                idcomptedebitOld = em.merge(idcomptedebitOld);
            }
            if (idcomptedebitNew != null && !idcomptedebitNew.equals(idcomptedebitOld)) {
                idcomptedebitNew.getVirementCollection().add(virement);
                idcomptedebitNew = em.merge(idcomptedebitNew);
            }
            if (idcomptecreditOld != null && !idcomptecreditOld.equals(idcomptecreditNew)) {
                idcomptecreditOld.getVirementCollection().remove(virement);
                idcomptecreditOld = em.merge(idcomptecreditOld);
            }
            if (idcomptecreditNew != null && !idcomptecreditNew.equals(idcomptecreditOld)) {
                idcomptecreditNew.getVirementCollection().add(virement);
                idcomptecreditNew = em.merge(idcomptecreditNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = virement.getIdvirement();
                if (findVirement(id) == null) {
                    throw new NonexistentEntityException("The virement with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Virement virement;
            try {
                virement = em.getReference(Virement.class, id);
                virement.getIdvirement();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The virement with id " + id + " no longer exists.", enfe);
            }
            Compte idcomptedebit = virement.getIdcomptedebit();
            if (idcomptedebit != null) {
                idcomptedebit.getVirementCollection().remove(virement);
                idcomptedebit = em.merge(idcomptedebit);
            }
            Compte idcomptecredit = virement.getIdcomptecredit();
            if (idcomptecredit != null) {
                idcomptecredit.getVirementCollection().remove(virement);
                idcomptecredit = em.merge(idcomptecredit);
            }
            em.remove(virement);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Virement> findVirementEntities() {
        return findVirementEntities(true, -1, -1);
    }

    public List<Virement> findVirementEntities(int maxResults, int firstResult) {
        return findVirementEntities(false, maxResults, firstResult);
    }

    private List<Virement> findVirementEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Virement.class));
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

    public Virement findVirement(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Virement.class, id);
        } finally {
            em.close();
        }
    }

    public int getVirementCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Virement> rt = cq.from(Virement.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
