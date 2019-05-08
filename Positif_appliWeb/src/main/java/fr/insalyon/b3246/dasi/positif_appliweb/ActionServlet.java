/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb;


import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.Serialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionInscriptionClient;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionConnexionEmploye;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionConnexionClient;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.Action;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.JpaUtil;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionChargerMedium;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.ClientSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.ListeMediumsSerialisation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Employe;
import metier.service.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author fgelus
 */
@WebServlet(urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JpaUtil.init();
        HttpSession session = request.getSession(true);
        request.setCharacterEncoding("UTF-8");
        String todo = request.getParameter("todo");
        Action action = null;
        Serialisation serialisation = null;
        
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        switch (todo) {
            case "connexionClient":
                action = new ActionConnexionClient();

                if(action.executer(request)){
                    Client client = Service.trouverClient(request.getParameter("login"), request.getParameter("password"));
                    session.setAttribute("client", client);
                    out.println("{\"connexion\": true, \"message\":\"OK\"}");

                    //serialisation = new ClientSerialisation();
                    //serialisation = new EmployeSerialisation();
                    //serialisation.serialiser(request, response);
                } else {
                    out.println("{\"connexion\": false, \"message\":\"Erreur de login\"}");
                }
                break;
                
            case "connexionEmploye":
                action = new ActionConnexionEmploye();

                if(action.executer(request)){
                    Employe employe = Service.trouverEmploye(request.getParameter("login"), request.getParameter("password"));
                    session.setAttribute("employe", employe);
                    out.println("{\"connexion\": true, \"message\":\"OK\"}");

                } else {
                    out.println("{\"connexion\": false, \"message\":\"Erreur de login\"}");
                }
                break;
                
            case "inscription":
                action = new ActionInscriptionClient();
                
                if (action.executer(request)){
                    out.println("{\"inscription\": true, \"message\":\"checkez vos mails\"}");
                } else {
                    out.println("{\"inscription\": false, \"message\":\"Erreur dans l'inscription\"}");
                }
                break;
                
            case "profil":
                Client client = (Client) session.getAttribute("client");
                if(client != null){
                    request.setAttribute("client",client);
                    serialisation = new ClientSerialisation();
                    serialisation.serialiser(request,response);
                } else {
                    out.println("{\"profil\": false, \"message\":\"Client introuvable\"}");
                }
                break;
                
            case "chargerMedium":
                action = new ActionChargerMedium();
                
                if (action.executer(request)){
                    serialisation = new ListeMediumsSerialisation();
                    serialisation.serialiser(request, response);
                } else {
                    out.println("{\"chargement\": false, \"message\":\"Erreur dans le chargement des mediums\"}");
                }
        }
        
        
        JpaUtil.destroy();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

