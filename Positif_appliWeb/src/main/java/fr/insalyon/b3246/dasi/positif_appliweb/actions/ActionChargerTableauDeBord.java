/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.service.Service;

/**
 *
 * @author bpauletto
 */
public class ActionChargerTableauDeBord extends Action{

    @Override
    public boolean executer(HttpServletRequest request) {
        
        List<Employe> employes = Service.getNBClientsParEmployeDesc();
        List<Medium> mediums = Service.getNBVoyanceParMediumDesc();
        
        request.setAttribute("employes",employes);
        request.setAttribute("mediums",mediums);
        
        if(employes == null || mediums == null){
            return false;
        }
    
        return true;
    }
    
}
