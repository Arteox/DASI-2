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
import metier.modele.Employe;
import metier.modele.Medium;


/**
 *
 * @author bpauletto
 */
public class TableauDeBordSerialisation extends Serialisation {

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
         JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonArrayEmployes = new JsonArray();
        JsonArray jsonArrayMediums = new JsonArray();
        List<Medium> mediums = (List<Medium>) request.getAttribute("mediums");
        List<Employe> employes = (List<Employe>) request.getAttribute("employes");

         for (Employe employe : employes){
        
            JsonObject jsonEmploye = new JsonObject();

            jsonEmploye.addProperty("id", employe.getId());
            jsonEmploye.addProperty("nombre affectations", employe.getNbAffectations());
            jsonEmploye.addProperty("nom", employe.getNom());
            jsonEmploye.addProperty("prenom", employe.getPrenom());

            jsonArrayEmployes.add(jsonEmploye);
        }
         
        for (Medium medium : mediums){
        
            JsonObject jsonMedium = new JsonObject();

            jsonMedium.addProperty("id", medium.getId());
            jsonMedium.addProperty("nom", medium.getNom());
            jsonMedium.addProperty("nbAffectations", medium.getNbAffectations());
            
            jsonArrayMediums.add(jsonMedium);
            
        }
        
        jsonContainer.add("mediums", jsonArrayMediums);
        jsonContainer.add("employes", jsonArrayEmployes);

        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
    
}
