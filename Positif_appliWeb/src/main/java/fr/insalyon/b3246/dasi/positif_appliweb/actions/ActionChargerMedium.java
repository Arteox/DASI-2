/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Medium;
import metier.service.Service;

/**
 *
 * @author Baptiste
 */
public class ActionChargerMedium extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        
        //Récupération de tous les médiums
        List<Medium> mediums = Service.trouverTousLesMedium();
        request.setAttribute("mediums", mediums);
        
        if(mediums == null){
            return false;
        }
        
        return true;
    }
    
}
