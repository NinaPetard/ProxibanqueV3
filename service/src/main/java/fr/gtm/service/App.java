package fr.gtm.service;

import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.ArrayList;



public class App {
    public static void main( String[] args ){
        Conseiller conseiller = new Conseiller();
        conseiller.setIdconseiller(2L);
        
        
        ArrayList<Client> clients;
        Service_Conseiller sc = new Service_Conseiller();
        clients = sc.listeClientConseiller(conseiller);
        
    }
}
