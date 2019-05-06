/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Client;
import metier.service.Service;

/**
 *
 * @author bpauletto
 */
public class ActionInscriptionClient extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        String civilite = request.getParameter("civilite");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        
        //on récupère la date comme un string
        String naissance = request.getParameter("naissance");
        Date dateNaissance = null;
        try {
            dateNaissance = new SimpleDateFormat("dd-MM-yyyy").parse(naissance);
        } catch (ParseException ex) {
            Logger.getLogger(ActionInscriptionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String adresse = request.getParameter("adresse");
        String ville = request.getParameter("ville");
        String codePostal = request.getParameter("codePostal");
        String telephone = request.getParameter("telephone");
        String mail = request.getParameter("mail");
        String mdp = request.getParameter("mdp");
        
        Client nouveauClient = new Client(civilite, nom, prenom, dateNaissance, adresse, telephone, mail, mdp);
        
        boolean inscriptionReussie = Service.inscrireClient(nouveauClient);
        
        return inscriptionReussie;
    }
    
}
