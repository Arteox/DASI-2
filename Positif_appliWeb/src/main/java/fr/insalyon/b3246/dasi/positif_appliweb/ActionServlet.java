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
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionAffichageHistorique;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionAfficherVoyanceEnCours;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionChargerMedium;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionChargerTableauDeBord;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionChoisirMedium;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionCommencerVoyance;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionGenererPredictions;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionRafraichirVoyanceEnCours;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionTerminerVoyance;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionValiderCRVoyance;
import fr.insalyon.b3246.dasi.positif_appliweb.actions.ActionVerificationSession;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.ClientSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.DemandeDeVoyanceSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.EmployeSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.HistoriqueSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.ListeMediumsSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.PredictionsSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.SessionSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.VoyanceEnCoursSerialisation;
import fr.insalyon.b3246.dasi.positif_appliweb.serialisations.TableauDeBordSerialisation;
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
import metier.modele.DemandeDeVoyance;
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
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

                } else {
                    out.println("{\"connexion\": false, \"message\":\"Erreur de login\"}");
                }
                break;
                
            case "connexionEmploye":
                action = new ActionConnexionEmploye();

                if(action.executer(request)){
                    Employe employe = Service.trouverEmploye(request.getParameter("login"), request.getParameter("password"));
                    session.setAttribute("employe", employe);
                    
                    //reponse
                    serialisation = new EmployeSerialisation();
                    serialisation.serialiser(request,response);

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
                
            case "verificationSession":
                action = new ActionVerificationSession();
                
                action.executer(request);
                serialisation = new SessionSerialisation();
                serialisation.serialiser(request, response);
                break;
                
                
            case "profilClient":
                //On récupère l'identité du client depuis la session
                Client clientProfil = (Client) session.getAttribute("client");
                if (clientProfil != null){
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
                break;
                
            case "choisirMedium":
                action = new ActionChoisirMedium();
                
                if (action.executer(request)){
                    out.println("{\"disponible\": true, \"message\":\"Envoi du SMS au client\"}");
                } else {
                    out.println("{\"disponible\": false, \"message\":\"Médium occupé\"}");
                }
                break;
                
            case "chargerHistorique":
                //On récupère l'identité du client depuis la session et on la passe en paramètre de la requête
                Client clientHistorique = (Client) session.getAttribute("client");
                request.setAttribute("client",clientHistorique);
                action = new ActionAffichageHistorique();
                
                if(action.executer(request)){
                    serialisation = new HistoriqueSerialisation();
                    serialisation.serialiser(request, response);
                } else {
                    out.println("{\"chargement\": false, \"message\":\"Erreur dans le chargement de l'historique\"}");
                }
                break;
                
            case "rafraichir":
                action = new ActionRafraichirVoyanceEnCours();
               
                if (action.executer(request)){
                    serialisation = new DemandeDeVoyanceSerialisation();
                    serialisation.serialiser(request, response);
                }
                break;
                
            case "tableauDeBord":
                action = new ActionChargerTableauDeBord();
                
                if(action.executer(request)){
                    serialisation = new TableauDeBordSerialisation();
                    serialisation.serialiser(request, response);
                }
                break;
                
            case "voyanceEnCours" :
                action = new ActionAfficherVoyanceEnCours();
                
                if (action.executer(request)){
                    serialisation = new VoyanceEnCoursSerialisation();
                    serialisation.serialiser(request, response);
                } else {
                    out.println("{\"voyanceEnCours\": false, \"message\":\"Erreur dans le chargement de la voyance en cours\"}");
                }
                break;
                
            case "genererPredictions":
                action = new ActionGenererPredictions();
                
                if (action.executer(request)){
                    serialisation = new PredictionsSerialisation();
                    serialisation.serialiser(request, response);
                }
                break;
                
            case "commencerVoyance":
                action = new ActionCommencerVoyance();
                
                if (action.executer(request)){
                    serialisation = new DemandeDeVoyanceSerialisation();
                    serialisation.serialiser(request, response);
                }
                break;
                
            case "terminerVoyance":
                action = new ActionTerminerVoyance();
                
                if (action.executer(request)){
                    serialisation = new DemandeDeVoyanceSerialisation();
                    serialisation.serialiser(request, response);
                }
                break;
                
            case "validerCRVoyance":
                action = new ActionValiderCRVoyance();
                if (action.executer(request)){
                    out.println("{\"validationVoyance\": true, \"message\":\"Voyance validée\"}");
                }
                break;
                
            case "deconnexion":
                session.invalidate();
                return;
        }
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

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        JpaUtil.init();
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        JpaUtil.destroy();
    }

}

