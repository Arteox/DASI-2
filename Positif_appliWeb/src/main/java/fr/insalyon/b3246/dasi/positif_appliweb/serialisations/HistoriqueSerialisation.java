/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.serialisations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.DemandeDeVoyance;


/**
 *
 * @author Baptiste
 */
public class HistoriqueSerialisation extends Serialisation {

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
                
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonArrayVoyances = new JsonArray();
        List<DemandeDeVoyance> voyances = (List<DemandeDeVoyance>)request.getAttribute("demandeDeVoyances");
        
        for (DemandeDeVoyance voyance : voyances){
            String dateDemande = dateFormat.format(voyance.getDate_demande());
            String dateFin = dateFormat.format(voyance.getDate_fin());
        
            JsonObject jsonVoyance = new JsonObject();

            jsonVoyance.addProperty("id", voyance.getId());
            jsonVoyance.addProperty("date-demande", dateDemande);
            jsonVoyance.addProperty("date-fin", dateFin);
            jsonVoyance.addProperty("accepte", voyance.getAccepte());
            jsonVoyance.addProperty("commentaire", voyance.getCommentaire());
            
            jsonArrayVoyances.add(jsonVoyance);
            
        }
        
        jsonContainer.add("voyances", jsonArrayVoyances);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
    
}
