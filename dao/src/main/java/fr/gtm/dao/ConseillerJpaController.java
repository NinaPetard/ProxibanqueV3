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
        if (conseiller.getClientList() == null) {
            conseiller.setClientList(new ArrayList<Client>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Client> attachedClientList = new ArrayList<Client>();
            for (Client clientListClientToAttach : conseiller.getClientList()) {
                clientListClientToAttach = em.getReference(clientListClientToAttach.getClass(), clientListClientToAttach.getIdclient());
                attachedClientList.add(clientListClientToAttach);
            }
            conseiller.setClientList(attachedClientList);
            em.persist(conseiller);
            for (Client clientListClient : conseiller.getClientList()) {
                Conseiller oldIdconseillerOfClientListClient = clientListClient.getIdconseiller();
                clientListClient.setIdconseiller(conseiller);
                clientListClient = em.merge(clientListClient);
                if (oldIdconseillerOfClientListClient != null) {
                    oldIdconseillerOfClientListClient.getClientList().remove(clientListClient);
                    oldIdconseillerOfClientListClient = em.merge(oldIdconseillerOfClientListClient);
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
            List<Client> clientListOld = persistentConseiller.getClientList();
            List<Client> clientListNew = conseiller.getClientList();
            List<Client> attachedClientListNew = new ArrayList<Client>();
            for (Client clientListNewClientToAttach : clientListNew) {
                clientListNewClientToAttach = em.getReference(clientListNewClientToAttach.getClass(), clientListNewClientToAttach.getIdclient());
                attachedClientListNew.add(clientListNewClientToAttach);
            }
            clientListNew = attachedClientListNew;
            conseiller.setClientList(clientListNew);
            conseiller = em.merge(conseiller);
            for (Client clientListOldClient : clientListOld) {
                if (!clientListNew.contains(clientListOldClient)) {
                    clientListOldClient.setIdconseiller(null);
                    clientListOldClient = em.merge(clientListOldClient);
                }
            }
            for (Client clientListNewClient : clientListNew) {
                if (!clientListOld.contains(clientListNewClient)) {
                    Conseiller oldIdconseillerOfClientListNewClient = clientListNewClient.getIdconseiller();
                    clientListNewClient.setIdconseiller(conseiller);
                    clientListNewClient = em.merge(clientListNewClient);
                    if (oldIdconseillerOfClientListNewClient != null && !oldIdconseillerOfClientListNewClient.equals(conseiller)) {
                        oldIdconseillerOfClientListNewClient.getClientList().remove(clientListNewClient);
                        oldIdconseillerOfClientListNewClient = em.merge(oldIdconseillerOfClientListNewClient);
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
            List<Client> clientList = conseiller.getClientList();
            for (Client clientListClient : clientList) {
                clientListClient.setIdconseiller(null);
                clientListClient = em.merge(clientListClient);
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
