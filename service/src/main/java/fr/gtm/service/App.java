package fr.gtm.service;

import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Conseiller conseiller = new Conseiller();
        conseiller.setIdconseiller(1L);
        List<Client> clients = Service_Conseiller.listerClientsConseiller(conseiller);
        
    }
}
