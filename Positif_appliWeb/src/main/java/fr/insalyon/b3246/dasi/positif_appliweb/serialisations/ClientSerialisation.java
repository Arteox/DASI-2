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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Client;
import org.apache.http.HttpRequest;

/**
 *
 * @author Louis Ung
 */
public class ClientSerialisation extends Serialisation {

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        JsonObject jsonContainer = new JsonObject();
        
        JsonObject jsonClient = new JsonObject();
        Client client = (Client) request.getAttribute("client");
        
        jsonClient.addProperty("adresse-postale", client.getAdressePostale());
        jsonClient.addProperty("totem", client.getAnimalTotem());
        jsonClient.addProperty("civilite", client.getCivilite());
        jsonClient.addProperty("bonheur", client.getCouleurPorteBonheur());
        jsonClient.addProperty("email", client.getEmail());
        jsonClient.addProperty("id", client.getId());
        jsonClient.addProperty("mot de passe", client.getMdp());
        jsonClient.addProperty("nom", client.getNom());
        jsonClient.addProperty("telephone", client.getNumtel());
        jsonClient.addProperty("prenom", client.getPrenom());
        jsonClient.addProperty("chinois", client.getSigneChinois());
        jsonClient.addProperty("zodiaque", client.getSigneZodiaque());
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
        String dateNaissance = dateFormat.format(client.getDateNaissance());  
        jsonClient.addProperty("naissance", dateNaissance);
        
        jsonContainer.add("client", jsonClient);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
}
