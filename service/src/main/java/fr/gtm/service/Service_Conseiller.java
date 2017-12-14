/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.service;

import fr.gtm.dao.ConseillerJpaController;
import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author adminl
 */
public class Service_Conseiller {

    public static List<Client> listerClientsConseiller(Conseiller conseiller) {

       List<Client> clients;        
        EntityManagerFactory emf;
        
        emf = Persistence.createEntityManagerFactory("fr.gtm_domaine_jar_1.0-SNAPSHOTPU");
        ConseillerJpaController cjc = new ConseillerJpaController(emf);
        Conseiller cons = cjc.findConseiller(conseiller.getIdconseiller());
        clients = cons.getClientList();

        return clients;

    }

}
