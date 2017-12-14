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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 * @author Nina et Robinson
 * Codage de la classe CompteJpaController
 * Cette classe permet la connexion a la base de données et la rédaction de transactions
 * en JPA avec Hibernate.
 * Permet de mettre en place le CRUD ainsi que des méthodes de récupération de données des comptes d'un client.
 */
public class CompteJpaController implements Serializable {

    //Ouverture de la connexion grace a l'entity manager factory
    public CompteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    //Ouverture des transactions grace à l'entity manager
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //Methode du CRUD avec JPA Hibernate
    public void create(Compte compte) throws PreexistingEntityException, Exception {
        if (compte.getVirementList() == null) {
            compte.setVirementList(new ArrayList<Virement>());
        }
        if (compte.getVirementList1() == null) {
            compte.setVirementList1(new ArrayList<Virement>());
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
            List<Virement> attachedVirementList = new ArrayList<Virement>();
            for (Virement virementListVirementToAttach : compte.getVirementList()) {
                virementListVirementToAttach = em.getReference(virementListVirementToAttach.getClass(), virementListVirementToAttach.getIdvirement());
                attachedVirementList.add(virementListVirementToAttach);
            }
            compte.setVirementList(attachedVirementList);
            List<Virement> attachedVirementList1 = new ArrayList<Virement>();
            for (Virement virementList1VirementToAttach : compte.getVirementList1()) {
                virementList1VirementToAttach = em.getReference(virementList1VirementToAttach.getClass(), virementList1VirementToAttach.getIdvirement());
                attachedVirementList1.add(virementList1VirementToAttach);
            }
            compte.setVirementList1(attachedVirementList1);
            em.persist(compte);
            if (idclient != null) {
                idclient.getCompteList().add(compte);
                idclient = em.merge(idclient);
            }
            for (Virement virementListVirement : compte.getVirementList()) {
                Compte oldIdcomptedebitOfVirementListVirement = virementListVirement.getIdcomptedebit();
                virementListVirement.setIdcomptedebit(compte);
                virementListVirement = em.merge(virementListVirement);
                if (oldIdcomptedebitOfVirementListVirement != null) {
                    oldIdcomptedebitOfVirementListVirement.getVirementList().remove(virementListVirement);
                    oldIdcomptedebitOfVirementListVirement = em.merge(oldIdcomptedebitOfVirementListVirement);
                }
            }
            for (Virement virementList1Virement : compte.getVirementList1()) {
                Compte oldIdcomptecreditOfVirementList1Virement = virementList1Virement.getIdcomptecredit();
                virementList1Virement.setIdcomptecredit(compte);
                virementList1Virement = em.merge(virementList1Virement);
                if (oldIdcomptecreditOfVirementList1Virement != null) {
                    oldIdcomptecreditOfVirementList1Virement.getVirementList1().remove(virementList1Virement);
                    oldIdcomptecreditOfVirementList1Virement = em.merge(oldIdcomptecreditOfVirementList1Virement);
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
            List<Virement> virementListOld = persistentCompte.getVirementList();
            List<Virement> virementListNew = compte.getVirementList();
            List<Virement> virementList1Old = persistentCompte.getVirementList1();
            List<Virement> virementList1New = compte.getVirementList1();
            if (idclientNew != null) {
                idclientNew = em.getReference(idclientNew.getClass(), idclientNew.getIdclient());
                compte.setIdclient(idclientNew);
            }
            List<Virement> attachedVirementListNew = new ArrayList<Virement>();
            for (Virement virementListNewVirementToAttach : virementListNew) {
                virementListNewVirementToAttach = em.getReference(virementListNewVirementToAttach.getClass(), virementListNewVirementToAttach.getIdvirement());
                attachedVirementListNew.add(virementListNewVirementToAttach);
            }
            virementListNew = attachedVirementListNew;
            compte.setVirementList(virementListNew);
            List<Virement> attachedVirementList1New = new ArrayList<Virement>();
            for (Virement virementList1NewVirementToAttach : virementList1New) {
                virementList1NewVirementToAttach = em.getReference(virementList1NewVirementToAttach.getClass(), virementList1NewVirementToAttach.getIdvirement());
                attachedVirementList1New.add(virementList1NewVirementToAttach);
            }
            virementList1New = attachedVirementList1New;
            compte.setVirementList1(virementList1New);
            compte = em.merge(compte);
            if (idclientOld != null && !idclientOld.equals(idclientNew)) {
                idclientOld.getCompteList().remove(compte);
                idclientOld = em.merge(idclientOld);
            }
            if (idclientNew != null && !idclientNew.equals(idclientOld)) {
                idclientNew.getCompteList().add(compte);
                idclientNew = em.merge(idclientNew);
            }
            for (Virement virementListOldVirement : virementListOld) {
                if (!virementListNew.contains(virementListOldVirement)) {
                    virementListOldVirement.setIdcomptedebit(null);
                    virementListOldVirement = em.merge(virementListOldVirement);
                }
            }
            for (Virement virementListNewVirement : virementListNew) {
                if (!virementListOld.contains(virementListNewVirement)) {
                    Compte oldIdcomptedebitOfVirementListNewVirement = virementListNewVirement.getIdcomptedebit();
                    virementListNewVirement.setIdcomptedebit(compte);
                    virementListNewVirement = em.merge(virementListNewVirement);
                    if (oldIdcomptedebitOfVirementListNewVirement != null && !oldIdcomptedebitOfVirementListNewVirement.equals(compte)) {
                        oldIdcomptedebitOfVirementListNewVirement.getVirementList().remove(virementListNewVirement);
                        oldIdcomptedebitOfVirementListNewVirement = em.merge(oldIdcomptedebitOfVirementListNewVirement);
                    }
                }
            }
            for (Virement virementList1OldVirement : virementList1Old) {
                if (!virementList1New.contains(virementList1OldVirement)) {
                    virementList1OldVirement.setIdcomptecredit(null);
                    virementList1OldVirement = em.merge(virementList1OldVirement);
                }
            }
            for (Virement virementList1NewVirement : virementList1New) {
                if (!virementList1Old.contains(virementList1NewVirement)) {
                    Compte oldIdcomptecreditOfVirementList1NewVirement = virementList1NewVirement.getIdcomptecredit();
                    virementList1NewVirement.setIdcomptecredit(compte);
                    virementList1NewVirement = em.merge(virementList1NewVirement);
                    if (oldIdcomptecreditOfVirementList1NewVirement != null && !oldIdcomptecreditOfVirementList1NewVirement.equals(compte)) {
                        oldIdcomptecreditOfVirementList1NewVirement.getVirementList1().remove(virementList1NewVirement);
                        oldIdcomptecreditOfVirementList1NewVirement = em.merge(oldIdcomptecreditOfVirementList1NewVirement);
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
                idclient.getCompteList().remove(compte);
                idclient = em.merge(idclient);
            }
            List<Virement> virementList = compte.getVirementList();
            for (Virement virementListVirement : virementList) {
                virementListVirement.setIdcomptedebit(null);
                virementListVirement = em.merge(virementListVirement);
            }
            List<Virement> virementList1 = compte.getVirementList1();
            for (Virement virementList1Virement : virementList1) {
                virementList1Virement.setIdcomptecredit(null);
                virementList1Virement = em.merge(virementList1Virement);
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

    //Methode pour recuperer les comptes associes a un identifiant
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
