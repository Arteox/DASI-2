/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.DemandeDeVoyance;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.service.Service;

/**
 *
 * @author Louis Ung
 */
public class ActionAfficherVoyanceEnCours extends Action{

    @Override
    public boolean executer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Employe employe = (Employe) session.getAttribute("employe");
        DemandeDeVoyance voyance = employe.getVoyanceActuelle();
        if (voyance == null){
            request.setAttribute("voyanceActuelle", voyance); //pour debugger plus facilement
            return false;
        }
        Medium medium = voyance.getMedium();
        if (medium == null){
            request.setAttribute("medium", medium); //pour debuger plus facilement
            return false;
        }
        request.setAttribute("medium", medium);
        request.setAttribute("voyanceActuelle", voyance);
        
        List<String> predictions = Service.genererPredictions(voyance.getClient());
        request.setAttribute("predictions", predictions);
        
        return true;
    }
    
}
