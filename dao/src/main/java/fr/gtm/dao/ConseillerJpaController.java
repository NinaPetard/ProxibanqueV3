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
import fr.gtm.domaine.Conseiller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Nina
 */
public class ConseillerJpaController implements Serializable {

    public ConseillerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Conseiller conseiller) throws PreexistingEntityException, Exception {
        if (conseiller.getClientCollection() == null) {
            conseiller.setClientCollection(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Client> attachedClientCollection = new ArrayList<Client>();
            for (Client clientCollectionClientToAttach : conseiller.getClientCollection()) {
                clientCollectionClientToAttach = em.getReference(clientCollectionClientToAttach.getClass(), clientCollectionClientToAttach.getIdclient());
                attachedClientCollection.add(clientCollectionClientToAttach);
            }
            conseiller.setClientCollection(attachedClientCollection);
            em.persist(conseiller);
            for (Client clientCollectionClient : conseiller.getClientCollection()) {
                Conseiller oldIdconseillerOfClientCollectionClient = clientCollectionClient.getIdconseiller();
                clientCollectionClient.setIdconseiller(conseiller);
                clientCollectionClient = em.merge(clientCollectionClient);
                if (oldIdconseillerOfClientCollectionClient != null) {
                    oldIdconseillerOfClientCollectionClient.getClientCollection().remove(clientCollectionClient);
                    oldIdconseillerOfClientCollectionClient = em.merge(oldIdconseillerOfClientCollectionClient);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findConseiller(conseiller.getIdconseiller()) != null) {
                throw new PreexistingEntityException("Conseiller " + conseiller + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Conseiller conseiller) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Conseiller persistentConseiller = em.find(Conseiller.class, conseiller.getIdconseiller());
            Collection<Client> clientCollectionOld = persistentConseiller.getClientCollection();
            Collection<Client> clientCollectionNew = conseiller.getClientCollection();
            Collection<Client> attachedClientCollectionNew = new ArrayList<Client>();
            for (Client clientCollectionNewClientToAttach : clientCollectionNew) {
                clientCollectionNewClientToAttach = em.getReference(clientCollectionNewClientToAttach.getClass(), clientCollectionNewClientToAttach.getIdclient());
                attachedClientCollectionNew.add(clientCollectionNewClientToAttach);
            }
            clientCollectionNew = attachedClientCollectionNew;
            conseiller.setClientCollection(clientCollectionNew);
            conseiller = em.merge(conseiller);
            for (Client clientCollectionOldClient : clientCollectionOld) {
                if (!clientCollectionNew.contains(clientCollectionOldClient)) {
                    clientCollectionOldClient.setIdconseiller(null);
                    clientCollectionOldClient = em.merge(clientCollectionOldClient);
                }
            }
            for (Client clientCollectionNewClient : clientCollectionNew) {
                if (!clientCollectionOld.contains(clientCollectionNewClient)) {
                    Conseiller oldIdconseillerOfClientCollectionNewClient = clientCollectionNewClient.getIdconseiller();
                    clientCollectionNewClient.setIdconseiller(conseiller);
                    clientCollectionNewClient = em.merge(clientCollectionNewClient);
                    if (oldIdconseillerOfClientCollectionNewClient != null && !oldIdconseillerOfClientCollectionNewClient.equals(conseiller)) {
                        oldIdconseillerOfClientCollectionNewClient.getClientCollection().remove(clientCollectionNewClient);
                        oldIdconseillerOfClientCollectionNewClient = em.merge(oldIdconseillerOfClientCollectionNewClient);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = conseiller.getIdconseiller();
                if (findConseiller(id) == null) {
                    throw new NonexistentEntityException("The conseiller with id " + id + " no longer exists.");
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
            Conseiller conseiller;
            try {
                conseiller = em.getReference(Conseiller.class, id);
                conseiller.getIdconseiller();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conseiller with id " + id + " no longer exists.", enfe);
            }
            Collection<Client> clientCollection = conseiller.getClientCollection();
            for (Client clientCollectionClient : clientCollection) {
                clientCollectionClient.setIdconseiller(null);
                clientCollectionClient = em.merge(clientCollectionClient);
            }
            em.remove(conseiller);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Conseiller> findConseillerEntities() {
        return findConseillerEntities(true, -1, -1);
    }

    public List<Conseiller> findConseillerEntities(int maxResults, int firstResult) {
        return findConseillerEntities(false, maxResults, firstResult);
    }

    private List<Conseiller> findConseillerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Conseiller.class));
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

    public Conseiller findConseiller(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Conseiller.class, id);
        } finally {
            em.close();
        }
    }

    public int getConseillerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Conseiller> rt = cq.from(Conseiller.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
