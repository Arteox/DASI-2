/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Employe;

/**
 *
 * @author Baptiste
 */
public class ActionVerificationSession extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        
        HttpSession session = request.getSession(false);
        Employe employe = (Employe) session.getAttribute("employe");
        Client client = (Client) session.getAttribute("client");
   
        if(employe== null && client == null){
            return false;
        }
        return true;
    }
    
}
