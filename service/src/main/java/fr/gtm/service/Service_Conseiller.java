package fr.gtm.Service;

import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Service_Conseiller {

    public void Service_Conseiller() {
    }

    public List<Client> listeClientsConseiller(Conseiller conseiller) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("fr.gtm_proxibanquev3_PU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        List<Client> clients = new ArrayList<Client>();

        try {
            clients = conseiller.getClientList();
            for (fr.gtm.domaine.Client client : clients) {
                System.out.println(client.getNom());
            }
        } catch (Exception e) {
            System.out.println("Pas de conseiller avec cet id");
        }

        em.close();
        emf.close();

        return clients;

    }

//    /**
//     * Transfers the authentification demand from the servlet to the DAO
//     *
//     * @param login
//     * @param password
//     * @return
//     */
//    public boolean demanderconnexion(String login, String password) {
//
//        DAO_Verifications dao1 = new DAO_Verifications();
//        boolean valeur = dao1.authConseiller(login, password);
//
//        return valeur;
//
//    }
//
//    /**
//     * Transfers the list of an advisor's clients from DAO to frontend
//     *
//     * @param idconseiller
//     * @return
//     */
//
//    public ArrayList<Client> demanderclients(int idconseiller) {
//        ArrayList<Client> clients = new ArrayList<Client>();
//
//        DAO_Client daoc = new DAO_Client();
//        clients = daoc.getCliConseiller(idconseiller);
//
//        return clients;
//    }
}
