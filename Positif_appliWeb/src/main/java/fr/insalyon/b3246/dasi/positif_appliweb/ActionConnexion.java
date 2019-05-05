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
public class ActionConnexion extends Action {

    @Override
    public boolean executer(HttpServletRequest request) {
        String mail = request.getParameter("login");
        String password = request.getParameter("password");
            
        //il faudra définir un paramètre permettant de distinguer si c'est un client ou un employe qui se connecte
        /*Client client = Service.trouverClient(mail, password);  
        request.setAttribute("client", client);*/
        
        Employe employe = Service.trouverEmploye(mail, password);  
        request.setAttribute("employe", employe);
        return true;
           
    }
    
}
