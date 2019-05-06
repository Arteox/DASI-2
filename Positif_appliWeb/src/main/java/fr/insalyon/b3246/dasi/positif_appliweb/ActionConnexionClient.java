/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb;

import javax.servlet.http.HttpServletRequest;
import metier.modele.Client;
import metier.modele.Employe;

import metier.service.Service;

/**
 *
 * @author bpauletto
 */
public class ActionConnexionClient extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        String mail = request.getParameter("login");
        String password = request.getParameter("password");
            
        Client client = Service.trouverClient(mail, password);  
        request.setAttribute("client", client);
        
        if (client == null){
            return false;
        }
        //Employe employe = Service.trouverEmploye(mail, password);
        //request.setAttribute("employe", employe);
        return true;
           
    }
    
}
