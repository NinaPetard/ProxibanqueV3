
package fr.gtm.service;

import java.util.ArrayList;
import java.util.HashMap;

import fr.gtm.dao.ClientJpaController;
import fr.gtm.dao.CompteJpaController;
import fr.gtm.domaine.Client        ;
import fr.gtm.domaine.Compte;

/**
 *
 *Transform objects obtained through otherclass' methods into printable text
 *@version In further version should be moved to a layer dedicated to presentation.
 */
public class Affichages {
//	/**
//	 * Transform a Client object into printable text.
//	 * @param client Client whose information should be displayed.
//	 * @return String describing the Client object.
//	 */
//
//    public String NPClient(Client client) {
//        String text = client.getIdClient() + " - " + client.getNom() + " " + client.getPrenom();
//        return text;
//    }
//    
//    public String tableauClient(Client client) {
//
//        String table = "<tr><th>" + client.getIdClient() + " </th><th> " + client.getNom() + "</th> <th> " 
//        + client.getPrenom() + "</th><th>"+ client.getAdresse() + " " + client.getCodepostal() + " " + client.getVille() 
//        +"</th><th>"+ client.getTelephone() + "</th> </tr>";
//       
//        return table;
//    }
//    
//    public HashMap <String,String> texteClients(ArrayList<Client> clients) {
//    	HashMap<String,String> txtClients = new HashMap<String, String>();
//        for (Client client : clients) {
//            String txtClient = NPClient(client);
//            String idcli = Integer.toString(client.getIdClient());
//            txtClients.put(idcli , txtClient);
//        }
//        return txtClients;
//    }
//    /**
//     * Transform a Client ArrayList into printable text.
//     * @param clients List of Clients to turn into text.
//     * @return String describing each client.
//     */
//
//    public ArrayList<String> tableauClients(ArrayList<Client> clients) {
//        ArrayList<String> txtClients = new ArrayList<String>();
//        for (Client client : clients) {
//            String txtClient = tableauClient(client);
//            txtClients.add(txtClient);
//        }
//        return txtClients;
//    }
//    
//    /**
//     * Transform a Compte object into printable text.
//     * @param compte Compte object of which informations should be displayed
//     * @return String decribing the Compte object
//     */
//
//    public String texteCompte(Compte compte) {
//    	String parametre= "";
//    	if(compte.getType().equals("courant"))
//    	{parametre = "D�couvert:" +compte.getDecouvert();} 
//    	else {parametre = "Taux d'int�r�t:" +compte.getInteret();} 
//    	
//
//        String text = "Num�ro de compte: " + compte.getIdCompte() + "<br>"
//                + "Type de compte: " + compte.getType() + "<br>"
//                + "Solde: " + compte.getSolde()+"<br>"
//                + parametre +"<br>";
//        return text;
//    }
//    /**
//     * Transform all accounts of a Client object into printable text
//     * @param client Client whose account should be displayed
//     * @return ArrayList of String, each describing an account.
//     */
//    
//    public ArrayList<String> txtCpteCli(int idClient) {
//    	
//    	DAO_Compte daoc = new DAO_Compte();    	
//    	
//    	ArrayList<Compte> comptes = new ArrayList<Compte>();
//    	comptes= daoc.getCpteClient(idClient);
//    	
//    	ArrayList<String> txtcomptes = new ArrayList<String>();
//    	
//    	for (Compte compte : comptes) {
//            String txtCompte = texteCompte(compte);
//            txtcomptes.add(txtCompte);
//        }          
//        return txtcomptes;
//    }

}
