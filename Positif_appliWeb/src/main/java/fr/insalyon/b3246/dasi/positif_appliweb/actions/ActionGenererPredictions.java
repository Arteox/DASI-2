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
import metier.service.Service;

/**
 *
 * @author Louis Ung
 */
public class ActionGenererPredictions extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Employe employe = (Employe) session.getAttribute("employe");
        DemandeDeVoyance voyance = employe.getVoyanceActuelle();
        
        List<String> predictions = Service.genererPredictions(voyance.getClient());
        request.setAttribute("predictions", predictions);
        return true;
    }
    
}
