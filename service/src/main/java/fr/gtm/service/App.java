package fr.gtm.service;

import fr.gtm.domaine.Client;
import fr.gtm.domaine.Conseiller;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Conseiller conseiller = new Conseiller();
        ArrayList<Client> clients = Service_Conseiller.listerClientsConseiller(conseiller);
    }
}
