/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.serialisations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Employe;

/**
 *
 * @author Baptiste
 */
public class SessionSerialisation extends Serialisation{

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
  
        JsonObject jsonResult = new JsonObject();
        HttpSession session = request.getSession(false);
        
        Client client = (Client) session.getAttribute("client");
        Employe employe = (Employe) session.getAttribute("employe");
        
        if(client == null && employe == null){
            jsonResult.addProperty("connecte", false);
            jsonResult.addProperty("message", "Aucun utilisateur trouvé, navigation interdite");
        } else {
            jsonResult.addProperty("connecte", true);
            jsonResult.addProperty("message", "Utilisateur trouvé, navigation autorisée");
        }
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonResult, out);
    }
    
}
