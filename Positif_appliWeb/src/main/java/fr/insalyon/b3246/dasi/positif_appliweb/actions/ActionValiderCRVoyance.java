/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.DemandeDeVoyance;
import metier.modele.Employe;
import metier.service.Service;

/**
 *
 * @author Louis Ung
 */
public class ActionValiderCRVoyance extends Action{

    @Override
    public boolean executer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Employe employe = (Employe) session.getAttribute("employe");
        DemandeDeVoyance voyance = employe.getVoyanceActuelle();
        String heureFin = request.getParameter("heureFin");  
        Date heureFin2 = null;
        try {
            heureFin2 = new SimpleDateFormat("hh:mm").parse(heureFin);
        } catch (ParseException ex) {
            Logger.getLogger(ActionValiderCRVoyance.class.getName()).log(Level.SEVERE, null, ex);
        }
        Service.faireCRVoyance(voyance, heureFin2, request.getParameter("commentaire"));
        return true;    
    }
    
}
