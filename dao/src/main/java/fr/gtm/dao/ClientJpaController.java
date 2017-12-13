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
        if (client.getCompteList() == null) {
            client.setCompteList(new ArrayList<Compte>());
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
            List<Compte> attachedCompteList = new ArrayList<Compte>();
            for (Compte compteListCompteToAttach : client.getCompteList()) {
                compteListCompteToAttach = em.getReference(compteListCompteToAttach.getClass(), compteListCompteToAttach.getIdcompte());
                attachedCompteList.add(compteListCompteToAttach);
            }
            client.setCompteList(attachedCompteList);
            em.persist(client);
            if (idconseiller != null) {
                idconseiller.getClientList().add(client);
                idconseiller = em.merge(idconseiller);
            }
            for (Compte compteListCompte : client.getCompteList()) {
                Client oldIdclientOfCompteListCompte = compteListCompte.getIdclient();
                compteListCompte.setIdclient(client);
                compteListCompte = em.merge(compteListCompte);
                if (oldIdclientOfCompteListCompte != null) {
                    oldIdclientOfCompteListCompte.getCompteList().remove(compteListCompte);
                    oldIdclientOfCompteListCompte = em.merge(oldIdclientOfCompteListCompte);
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
            List<Compte> compteListOld = persistentClient.getCompteList();
            List<Compte> compteListNew = client.getCompteList();
            if (idconseillerNew != null) {
                idconseillerNew = em.getReference(idconseillerNew.getClass(), idconseillerNew.getIdconseiller());
                client.setIdconseiller(idconseillerNew);
            }
            List<Compte> attachedCompteListNew = new ArrayList<Compte>();
            for (Compte compteListNewCompteToAttach : compteListNew) {
                compteListNewCompteToAttach = em.getReference(compteListNewCompteToAttach.getClass(), compteListNewCompteToAttach.getIdcompte());
                attachedCompteListNew.add(compteListNewCompteToAttach);
            }
            compteListNew = attachedCompteListNew;
            client.setCompteList(compteListNew);
            client = em.merge(client);
            if (idconseillerOld != null && !idconseillerOld.equals(idconseillerNew)) {
                idconseillerOld.getClientList().remove(client);
                idconseillerOld = em.merge(idconseillerOld);
            }
            if (idconseillerNew != null && !idconseillerNew.equals(idconseillerOld)) {
                idconseillerNew.getClientList().add(client);
                idconseillerNew = em.merge(idconseillerNew);
            }
            for (Compte compteListOldCompte : compteListOld) {
                if (!compteListNew.contains(compteListOldCompte)) {
                    compteListOldCompte.setIdclient(null);
                    compteListOldCompte = em.merge(compteListOldCompte);
                }
            }
            for (Compte compteListNewCompte : compteListNew) {
                if (!compteListOld.contains(compteListNewCompte)) {
                    Client oldIdclientOfCompteListNewCompte = compteListNewCompte.getIdclient();
                    compteListNewCompte.setIdclient(client);
                    compteListNewCompte = em.merge(compteListNewCompte);
                    if (oldIdclientOfCompteListNewCompte != null && !oldIdclientOfCompteListNewCompte.equals(client)) {
                        oldIdclientOfCompteListNewCompte.getCompteList().remove(compteListNewCompte);
                        oldIdclientOfCompteListNewCompte = em.merge(oldIdclientOfCompteListNewCompte);
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
                idconseiller.getClientList().remove(client);
                idconseiller = em.merge(idconseiller);
            }
            List<Compte> compteList = client.getCompteList();
            for (Compte compteListCompte : compteList) {
                compteListCompte.setIdclient(null);
                compteListCompte = em.merge(compteListCompte);
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
