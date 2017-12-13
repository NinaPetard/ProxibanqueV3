/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.dao;

import fr.gtm.dao.exceptions.NonexistentEntityException;
import fr.gtm.dao.exceptions.PreexistingEntityException;
import fr.gtm.domaine.Client;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import fr.gtm.domaine.Conseiller;
import fr.gtm.domaine.Compte;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nina
 */
public class ClientJpaController implements Serializable {

    public ClientJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Client client) throws PreexistingEntityException, Exception {
        if (client.getCompteCollection() == null) {
            client.setCompteCollection(new ArrayList<Compte>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Conseiller idconseiller = client.getIdconseiller();
            if (idconseiller != null) {
                idconseiller = em.getReference(idconseiller.getClass(), idconseiller.getIdconseiller());
                client.setIdconseiller(idconseiller);
            }
            Collection<Compte> attachedCompteCollection = new ArrayList<Compte>();
            for (Compte compteCollectionCompteToAttach : client.getCompteCollection()) {
                compteCollectionCompteToAttach = em.getReference(compteCollectionCompteToAttach.getClass(), compteCollectionCompteToAttach.getIdcompte());
                attachedCompteCollection.add(compteCollectionCompteToAttach);
            }
            client.setCompteCollection(attachedCompteCollection);
            em.persist(client);
            if (idconseiller != null) {
                idconseiller.getClientCollection().add(client);
                idconseiller = em.merge(idconseiller);
            }
            for (Compte compteCollectionCompte : client.getCompteCollection()) {
                Client oldIdclientOfCompteCollectionCompte = compteCollectionCompte.getIdclient();
                compteCollectionCompte.setIdclient(client);
                compteCollectionCompte = em.merge(compteCollectionCompte);
                if (oldIdclientOfCompteCollectionCompte != null) {
                    oldIdclientOfCompteCollectionCompte.getCompteCollection().remove(compteCollectionCompte);
                    oldIdclientOfCompteCollectionCompte = em.merge(oldIdclientOfCompteCollectionCompte);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClient(client.getIdclient()) != null) {
                throw new PreexistingEntityException("Client " + client + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Client client) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Client persistentClient = em.find(Client.class, client.getIdclient());
            Conseiller idconseillerOld = persistentClient.getIdconseiller();
            Conseiller idconseillerNew = client.getIdconseiller();
            Collection<Compte> compteCollectionOld = persistentClient.getCompteCollection();
            Collection<Compte> compteCollectionNew = client.getCompteCollection();
            if (idconseillerNew != null) {
                idconseillerNew = em.getReference(idconseillerNew.getClass(), idconseillerNew.getIdconseiller());
                client.setIdconseiller(idconseillerNew);
            }
            Collection<Compte> attachedCompteCollectionNew = new ArrayList<Compte>();
            for (Compte compteCollectionNewCompteToAttach : compteCollectionNew) {
                compteCollectionNewCompteToAttach = em.getReference(compteCollectionNewCompteToAttach.getClass(), compteCollectionNewCompteToAttach.getIdcompte());
                attachedCompteCollectionNew.add(compteCollectionNewCompteToAttach);
            }
            compteCollectionNew = attachedCompteCollectionNew;
            client.setCompteCollection(compteCollectionNew);
            client = em.merge(client);
            if (idconseillerOld != null && !idconseillerOld.equals(idconseillerNew)) {
                idconseillerOld.getClientCollection().remove(client);
                idconseillerOld = em.merge(idconseillerOld);
            }
            if (idconseillerNew != null && !idconseillerNew.equals(idconseillerOld)) {
                idconseillerNew.getClientCollection().add(client);
                idconseillerNew = em.merge(idconseillerNew);
            }
            for (Compte compteCollectionOldCompte : compteCollectionOld) {
                if (!compteCollectionNew.contains(compteCollectionOldCompte)) {
                    compteCollectionOldCompte.setIdclient(null);
                    compteCollectionOldCompte = em.merge(compteCollectionOldCompte);
                }
            }
            for (Compte compteCollectionNewCompte : compteCollectionNew) {
                if (!compteCollectionOld.contains(compteCollectionNewCompte)) {
                    Client oldIdclientOfCompteCollectionNewCompte = compteCollectionNewCompte.getIdclient();
                    compteCollectionNewCompte.setIdclient(client);
                    compteCollectionNewCompte = em.merge(compteCollectionNewCompte);
                    if (oldIdclientOfCompteCollectionNewCompte != null && !oldIdclientOfCompteCollectionNewCompte.equals(client)) {
                        oldIdclientOfCompteCollectionNewCompte.getCompteCollection().remove(compteCollectionNewCompte);
                        oldIdclientOfCompteCollectionNewCompte = em.merge(oldIdclientOfCompteCollectionNewCompte);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = client.getIdclient();
                if (findClient(id) == null) {
                    throw new NonexistentEntityException("The client with id " + id + " no longer exists.");
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
            Client client;
            try {
                client = em.getReference(Client.class, id);
                client.getIdclient();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The client with id " + id + " no longer exists.", enfe);
            }
            Conseiller idconseiller = client.getIdconseiller();
            if (idconseiller != null) {
                idconseiller.getClientCollection().remove(client);
                idconseiller = em.merge(idconseiller);
            }
            Collection<Compte> compteCollection = client.getCompteCollection();
            for (Compte compteCollectionCompte : compteCollection) {
                compteCollectionCompte.setIdclient(null);
                compteCollectionCompte = em.merge(compteCollectionCompte);
            }
            em.remove(client);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Client> findClientEntities() {
        return findClientEntities(true, -1, -1);
    }

    public List<Client> findClientEntities(int maxResults, int firstResult) {
        return findClientEntities(false, maxResults, firstResult);
    }

    private List<Client> findClientEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Client.class));
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

    public Client findClient(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Client.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Client> rt = cq.from(Client.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
