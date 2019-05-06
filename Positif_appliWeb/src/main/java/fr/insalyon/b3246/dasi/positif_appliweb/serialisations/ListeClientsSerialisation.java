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
import metier.modele.Client;

/**
 *
 * @author Louis Ung
 */
public class ListeClientsSerialisation extends Serialisation{

    // A TESTER
    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        JsonObject jsonContainer = new JsonObject();
        
        JsonArray jsonArrayClients = new JsonArray();
        List<Client> clients = (List<Client>)request.getAttribute("clients");
        clients = (List<Client>)request.getAttribute("clients");
        
        for (Client client : clients){
        
            JsonObject jsonClient = new JsonObject();
            client = (Client) request.getAttribute("client");

            jsonClient.addProperty("adresse postale", client.getAdressePostale());
            jsonClient.addProperty("animal totem", client.getAnimalTotem());
            jsonClient.addProperty("civilite", client.getCivilite());
            jsonClient.addProperty("couleur porte bonheur", client.getCouleurPorteBonheur());
            jsonClient.addProperty("email", client.getEmail());
            jsonClient.addProperty("id", client.getId());
            jsonClient.addProperty("mot de passe", client.getMdp());
            jsonClient.addProperty("nom", client.getNom());
            jsonClient.addProperty("telephone", client.getNumtel());
            jsonClient.addProperty("prenom", client.getPrenom());
            jsonClient.addProperty("signe chinois", client.getSigneChinois());
            jsonClient.addProperty("signe zodiaque", client.getSigneZodiaque());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");  
            String dateNaissance = dateFormat.format(client.getDateNaissance());  
            jsonClient.addProperty("date de naissance", dateNaissance);

            jsonArrayClients.add(jsonClient);
        }
        
        jsonContainer.add("clients", jsonArrayClients);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
    
}
