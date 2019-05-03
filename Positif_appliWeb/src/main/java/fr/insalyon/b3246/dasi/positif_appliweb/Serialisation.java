/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Client;

/**
 *
 * @author bpauletto
 */
public class Serialisation {
    
    public JsonObject serialiserClient(Client c){
        JsonObject jsonContainer = new JsonObject();
        jsonContainer.addProperty("nom", c.getNom());
        jsonContainer.addProperty("prenom", c.getPrenom());
        
    }
}
