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
import metier.modele.DemandeDeVoyance;

/**
 *
 * @author bpauletto
 */
public class DemandeDeVoyanceSerialisation extends Serialisation {

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonContainer = new JsonObject();
        
        JsonObject jsonVoyance = new JsonObject();
        DemandeDeVoyance demandeVoyance = (DemandeDeVoyance) request.getAttribute("voyance");
        if (demandeVoyance != null){
            jsonVoyance.addProperty("id", demandeVoyance.getId());
        }
        jsonContainer.add("voyance", jsonVoyance);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
    
}
