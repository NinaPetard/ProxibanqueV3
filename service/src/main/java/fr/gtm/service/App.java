package fr.gtm.service;
import fr.gtm.domaine.Conseiller;


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
        System.out.println(Service_Conseiller.listerClientsConseiller(conseiller));        
    }
}
