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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Astrologue;
import metier.modele.Medium;
import metier.modele.Tarologue;
import metier.modele.Voyant;



/**
 *
 * @author Baptiste
 */
public class ListeMediumsSerialisation extends Serialisation {

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
         
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonArrayMediums = new JsonArray();
        List<Medium> mediums = (List<Medium>)request.getAttribute("mediums");
        
        for (Medium medium : mediums){
        
            JsonObject jsonMedium = new JsonObject();

            jsonMedium.addProperty("id", medium.getId());
            jsonMedium.addProperty("nom", medium.getNom());
            jsonMedium.addProperty("descriptif", medium.getDescriptif());
            jsonMedium.addProperty("nbAffectations", medium.getNbAffectations());
            
            switch(medium.getType()){
                case "Astrologue":
                    Astrologue astro = (Astrologue) medium;
                    jsonMedium.addProperty("type", medium.getType());
                    jsonMedium.addProperty("promotion", astro.getPromotion());
                    jsonMedium.addProperty("formation", astro.getFormation());
                    break;
                case "Tarologue":
                    Tarologue taro = (Tarologue) medium;
                    jsonMedium.addProperty("type", medium.getType());
                    break;
                case "Voyant":
                    Voyant voyant = (Voyant) medium;
                    jsonMedium.addProperty("specialite", voyant.getSpecialite());
                    jsonMedium.addProperty("type", medium.getType());
                    break;
            }
            
            jsonArrayMediums.add(jsonMedium);
            
        }
        
        jsonContainer.add("mediums", jsonArrayMediums);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
    
}
