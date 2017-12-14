/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.gtm.dao.ConseillerJpaController;
import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author adminl
 */
public class Service_Conseiller {

    public static String listerClientsConseiller(Conseiller conseiller) {

        List<Client> clients;
        EntityManagerFactory emf;
        String lcjson = "";
        //import
        emf = Persistence.createEntityManagerFactory("fr.gtm_domaine_jar_1.0-SNAPSHOTPU");
        ConseillerJpaController cjc = new ConseillerJpaController(emf);

        Conseiller cons = cjc.findConseiller(conseiller.getIdconseiller());
        clients = cons.getClientList();

        //json-isation
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            lcjson = objectMapper.writeValueAsString(clients);

        } catch (JsonProcessingException ex) {
            Logger.getLogger(Service_Conseiller.class.getName()).log(Level.SEVERE, null, ex);

        }

        return lcjson;

    }

}
