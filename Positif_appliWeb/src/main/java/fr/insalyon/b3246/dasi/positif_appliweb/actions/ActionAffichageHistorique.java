/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Client;
import metier.modele.DemandeDeVoyance;
import metier.modele.Medium;
import metier.service.Service;

/**
 *
 * @author Baptiste
 */
public class ActionAffichageHistorique extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {

        Client client = (Client) request.getAttribute("client");
        
        //Récupération de tous les médiums appartenant à l'historique du client
        List<DemandeDeVoyance> demandeDeVoyances = Service.getHistoriqueDemandes(client);
        request.setAttribute("demandeDeVoyances", demandeDeVoyances);
        
        if(demandeDeVoyances == null){
            return false;
        }
        
        return true;
    }
    
}
