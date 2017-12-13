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
import fr.gtm.domaine.Client;
import fr.gtm.domaine.Compte;
import fr.gtm.domaine.Virement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nina
 */
public class CompteJpaController implements Serializable {

    public CompteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compte compte) throws PreexistingEntityException, Exception {
        if (compte.getVirementCollection() == null) {
            compte.setVirementCollection(new ArrayList<Virement>());
        }
        if (compte.getVirementCollection1() == null) {
            compte.setVirementCollection1(new ArrayList<Virement>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client idclient = compte.getIdclient();
            if (idclient != null) {
                idclient = em.getReference(idclient.getClass(), idclient.getIdclient());
                compte.setIdclient(idclient);
            }
            Collection<Virement> attachedVirementCollection = new ArrayList<Virement>();
            for (Virement virementCollectionVirementToAttach : compte.getVirementCollection()) {
                virementCollectionVirementToAttach = em.getReference(virementCollectionVirementToAttach.getClass(), virementCollectionVirementToAttach.getIdvirement());
                attachedVirementCollection.add(virementCollectionVirementToAttach);
            }
            compte.setVirementCollection(attachedVirementCollection);
            Collection<Virement> attachedVirementCollection1 = new ArrayList<Virement>();
            for (Virement virementCollection1VirementToAttach : compte.getVirementCollection1()) {
                virementCollection1VirementToAttach = em.getReference(virementCollection1VirementToAttach.getClass(), virementCollection1VirementToAttach.getIdvirement());
                attachedVirementCollection1.add(virementCollection1VirementToAttach);
            }
            compte.setVirementCollection1(attachedVirementCollection1);
            em.persist(compte);
            if (idclient != null) {
                idclient.getCompteCollection().add(compte);
                idclient = em.merge(idclient);
            }
            for (Virement virementCollectionVirement : compte.getVirementCollection()) {
                Compte oldIdcomptedebitOfVirementCollectionVirement = virementCollectionVirement.getIdcomptedebit();
                virementCollectionVirement.setIdcomptedebit(compte);
                virementCollectionVirement = em.merge(virementCollectionVirement);
                if (oldIdcomptedebitOfVirementCollectionVirement != null) {
                    oldIdcomptedebitOfVirementCollectionVirement.getVirementCollection().remove(virementCollectionVirement);
                    oldIdcomptedebitOfVirementCollectionVirement = em.merge(oldIdcomptedebitOfVirementCollectionVirement);
                }
            }
            for (Virement virementCollection1Virement : compte.getVirementCollection1()) {
                Compte oldIdcomptecreditOfVirementCollection1Virement = virementCollection1Virement.getIdcomptecredit();
                virementCollection1Virement.setIdcomptecredit(compte);
                virementCollection1Virement = em.merge(virementCollection1Virement);
                if (oldIdcomptecreditOfVirementCollection1Virement != null) {
                    oldIdcomptecreditOfVirementCollection1Virement.getVirementCollection1().remove(virementCollection1Virement);
                    oldIdcomptecreditOfVirementCollection1Virement = em.merge(oldIdcomptecreditOfVirementCollection1Virement);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompte(compte.getIdcompte()) != null) {
                throw new PreexistingEntityException("Compte " + compte + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compte compte) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compte persistentCompte = em.find(Compte.class, compte.getIdcompte());
            Client idclientOld = persistentCompte.getIdclient();
            Client idclientNew = compte.getIdclient();
            Collection<Virement> virementCollectionOld = persistentCompte.getVirementCollection();
            Collection<Virement> virementCollectionNew = compte.getVirementCollection();
            Collection<Virement> virementCollection1Old = persistentCompte.getVirementCollection1();
            Collection<Virement> virementCollection1New = compte.getVirementCollection1();
            if (idclientNew != null) {
                idclientNew = em.getReference(idclientNew.getClass(), idclientNew.getIdclient());
                compte.setIdclient(idclientNew);
            }
            Collection<Virement> attachedVirementCollectionNew = new ArrayList<Virement>();
            for (Virement virementCollectionNewVirementToAttach : virementCollectionNew) {
                virementCollectionNewVirementToAttach = em.getReference(virementCollectionNewVirementToAttach.getClass(), virementCollectionNewVirementToAttach.getIdvirement());
                attachedVirementCollectionNew.add(virementCollectionNewVirementToAttach);
            }
            virementCollectionNew = attachedVirementCollectionNew;
            compte.setVirementCollection(virementCollectionNew);
            Collection<Virement> attachedVirementCollection1New = new ArrayList<Virement>();
            for (Virement virementCollection1NewVirementToAttach : virementCollection1New) {
                virementCollection1NewVirementToAttach = em.getReference(virementCollection1NewVirementToAttach.getClass(), virementCollection1NewVirementToAttach.getIdvirement());
                attachedVirementCollection1New.add(virementCollection1NewVirementToAttach);
            }
            virementCollection1New = attachedVirementCollection1New;
            compte.setVirementCollection1(virementCollection1New);
            compte = em.merge(compte);
            if (idclientOld != null && !idclientOld.equals(idclientNew)) {
                idclientOld.getCompteCollection().remove(compte);
                idclientOld = em.merge(idclientOld);
            }
            if (idclientNew != null && !idclientNew.equals(idclientOld)) {
                idclientNew.getCompteCollection().add(compte);
                idclientNew = em.merge(idclientNew);
            }
            for (Virement virementCollectionOldVirement : virementCollectionOld) {
                if (!virementCollectionNew.contains(virementCollectionOldVirement)) {
                    virementCollectionOldVirement.setIdcomptedebit(null);
                    virementCollectionOldVirement = em.merge(virementCollectionOldVirement);
                }
            }
            for (Virement virementCollectionNewVirement : virementCollectionNew) {
                if (!virementCollectionOld.contains(virementCollectionNewVirement)) {
                    Compte oldIdcomptedebitOfVirementCollectionNewVirement = virementCollectionNewVirement.getIdcomptedebit();
                    virementCollectionNewVirement.setIdcomptedebit(compte);
                    virementCollectionNewVirement = em.merge(virementCollectionNewVirement);
                    if (oldIdcomptedebitOfVirementCollectionNewVirement != null && !oldIdcomptedebitOfVirementCollectionNewVirement.equals(compte)) {
                        oldIdcomptedebitOfVirementCollectionNewVirement.getVirementCollection().remove(virementCollectionNewVirement);
                        oldIdcomptedebitOfVirementCollectionNewVirement = em.merge(oldIdcomptedebitOfVirementCollectionNewVirement);
                    }
                }
            }
            for (Virement virementCollection1OldVirement : virementCollection1Old) {
                if (!virementCollection1New.contains(virementCollection1OldVirement)) {
                    virementCollection1OldVirement.setIdcomptecredit(null);
                    virementCollection1OldVirement = em.merge(virementCollection1OldVirement);
                }
            }
            for (Virement virementCollection1NewVirement : virementCollection1New) {
                if (!virementCollection1Old.contains(virementCollection1NewVirement)) {
                    Compte oldIdcomptecreditOfVirementCollection1NewVirement = virementCollection1NewVirement.getIdcomptecredit();
                    virementCollection1NewVirement.setIdcomptecredit(compte);
                    virementCollection1NewVirement = em.merge(virementCollection1NewVirement);
                    if (oldIdcomptecreditOfVirementCollection1NewVirement != null && !oldIdcomptecreditOfVirementCollection1NewVirement.equals(compte)) {
                        oldIdcomptecreditOfVirementCollection1NewVirement.getVirementCollection1().remove(virementCollection1NewVirement);
                        oldIdcomptecreditOfVirementCollection1NewVirement = em.merge(oldIdcomptecreditOfVirementCollection1NewVirement);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = compte.getIdcompte();
                if (findCompte(id) == null) {
                    throw new NonexistentEntityException("The compte with id " + id + " no longer exists.");
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
            Compte compte;
            try {
                compte = em.getReference(Compte.class, id);
                compte.getIdcompte();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compte with id " + id + " no longer exists.", enfe);
            }
            Client idclient = compte.getIdclient();
            if (idclient != null) {
                idclient.getCompteCollection().remove(compte);
                idclient = em.merge(idclient);
            }
            Collection<Virement> virementCollection = compte.getVirementCollection();
            for (Virement virementCollectionVirement : virementCollection) {
                virementCollectionVirement.setIdcomptedebit(null);
                virementCollectionVirement = em.merge(virementCollectionVirement);
            }
            Collection<Virement> virementCollection1 = compte.getVirementCollection1();
            for (Virement virementCollection1Virement : virementCollection1) {
                virementCollection1Virement.setIdcomptecredit(null);
                virementCollection1Virement = em.merge(virementCollection1Virement);
            }
            em.remove(compte);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compte> findCompteEntities() {
        return findCompteEntities(true, -1, -1);
    }

    public List<Compte> findCompteEntities(int maxResults, int firstResult) {
        return findCompteEntities(false, maxResults, firstResult);
    }

    private List<Compte> findCompteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compte.class));
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

    public Compte findCompte(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compte.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compte> rt = cq.from(Compte.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
