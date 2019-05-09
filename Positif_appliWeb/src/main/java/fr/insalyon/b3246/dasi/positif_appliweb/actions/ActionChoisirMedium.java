/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;


import javax.servlet.http.HttpServletRequest;
import metier.modele.Client;
import metier.modele.Medium;
import metier.service.Service;


/**
 *
 * @author Baptiste
 */
public class ActionChoisirMedium extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        
        String numeroMedium = request.getParameter("numMedium");
        
        Client client = (Client) request.getAttribute("client");
        Medium medium = Service.trouverMedium(Long.parseLong(numeroMedium));
        
        client.getAdressePostale();
        medium.getDescriptif();
        //Récupération de la disponibilité de ce médium pour cette demande de voyance et retour de booléen
        return Service.demanderVoyance(client, medium);
    }
    
}
