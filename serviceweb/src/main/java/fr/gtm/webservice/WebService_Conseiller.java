/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.webservice;

import fr.gtm.service.Service_Conseiller;
import static fr.gtm.service.Service_Conseiller.listerClientsConseiller;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;


import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("conseiller")
public class WebService_Conseiller {

    @POST
    @Path("/listeclients")
    @Consumes(MediaType.TEXT_PLAIN)    
    @Produces(MediaType.TEXT_PLAIN)    
    public String sendCliData(String userIdCons) {
        String clijson ;
        Long idcons = Long.parseLong(userIdCons, 10);
        
        clijson = listerClientsConseiller(idcons);
        return clijson;
    }

}
