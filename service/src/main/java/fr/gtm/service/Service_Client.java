package fr.gtm.Service;

import java.util.ArrayList;

import fr.gtm.dao.ClientJpaController;
import fr.gtm.dao.ConseillerJpaController;

import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Service_Client {

// 
//
//    /**
//     * Transfers the information to be modified in the base to the DAO layer
//     *
//     * @param info
//     * @param replace
//     * @param idclient
//     */
//    public void changerInfoClient(String info, String replace, int idclient) {
//
//        DAO_Client dao1 = new DAO_Client();
//
//        dao1.updateClientId(info, replace, idclient);
//
//    }
//
//    /**
//     * Checks if a client exists in the database Copy of
//     * DAO_Verification.authClient, should be fusionned in further versions
//     *
//     * @param idclient
//     * @return
//     */
//    public boolean verifIdClient(int idclient) {
//
//        DAO_Client dao1 = new DAO_Client();
//
//        boolean valeur = dao1.checkClientwId(idclient);
//
//        return valeur;
//
//    }

}
