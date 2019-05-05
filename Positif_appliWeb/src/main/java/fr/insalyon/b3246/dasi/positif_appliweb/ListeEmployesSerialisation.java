/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb;

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

/**
 *
 * @author Louis Ung
 */
public class ListeEmployesSerialisation extends Serialisation{
    
    // A TESTER
    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonArrayEmployes = new JsonArray();
        List<Employe> employes = (List<Employe>)request.getAttribute("employes");
        employes = (List<Employe>)request.getAttribute("employes");
        
        for (Employe employe : employes){
        
            JsonObject jsonEmploye = new JsonObject();
            employe = (Employe) request.getAttribute("employe");

            jsonEmploye.addProperty("disponible", employe.getDisponible());
            jsonEmploye.addProperty("email", employe.getEmail());
            jsonEmploye.addProperty("id", employe.getId());
            jsonEmploye.addProperty("mot de passe", employe.getMdp());
            jsonEmploye.addProperty("nombre affectations", employe.getNbAffectations());
            jsonEmploye.addProperty("nom", employe.getNom());
            jsonEmploye.addProperty("telephone", employe.getTel());
            jsonEmploye.addProperty("prenom", employe.getPrenom());

            jsonArrayEmployes.add(jsonEmploye);
        }
        
        jsonContainer.add("employes", jsonArrayEmployes);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }

}
