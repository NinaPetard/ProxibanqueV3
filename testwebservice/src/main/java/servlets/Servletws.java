/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@WebServlet(urlPatterns = {"/Servletws"})
public class Servletws extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response){
        
        try {
            String login = request.getParameter("loginuser");   
            Client client = ClientBuilder.newClient();
            //Chemin à atteindre pour utiliser le Web Service 
            WebTarget webTarget = client.target("http://localhost:8081/proxibanquev3/proxapi");
            WebTarget resourceWebTarget = webTarget.path("conseiller");
            WebTarget giveObjectGetTextWebTarget = resourceWebTarget.path("/listeclients");

            Invocation.Builder invocationBuilder = giveObjectGetTextWebTarget.request();

            //Appel de la méthode post : envoi de l'objet objetAEnvoyer, et on déclare que c'est un json.
            Response responseb = invocationBuilder.post(Entity.entity(login, MediaType.TEXT_PLAIN));

            if (responseb.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + responseb.getStatus());
            }


            String responseText = invocationBuilder.post(Entity.entity(login, MediaType.TEXT_PLAIN), String.class);
            System.out.println(responseText);           
        } 
        catch(Exception e){
            System.out.println("ça a pas marché");

        }
    }

        @Override
        protected void doGet
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

            processRequest(request, response);
        }

        /**
         * Handles the HTTP <code>POST</code> method.
         *
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
        @Override
        protected void doPost
        (HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            processRequest(request, response);
        }

        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo
        
            () {
        return "Short description";
        }// </editor-fold>

    }
