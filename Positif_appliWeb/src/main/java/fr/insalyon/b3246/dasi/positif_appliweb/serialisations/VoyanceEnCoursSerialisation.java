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
import metier.modele.Astrologue;
import metier.modele.Client;
import metier.modele.DemandeDeVoyance;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.modele.Voyant;

/**
 *
 * @author Louis Ung
 */
public class VoyanceEnCoursSerialisation extends Serialisation {

    @Override
    public void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject jsonContainer = new JsonObject();
        DateFormat dateFormatDemande = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateFormat dateFormatNaissance = new SimpleDateFormat("yyyy-mm-dd");  
        
        //on recupere le profil du medium incarne par l'employe
        JsonObject jsonMedium = new JsonObject();
        Medium medium = (Medium) request.getAttribute("medium");
        
        jsonMedium.addProperty("nom", medium.getNom());
        jsonMedium.addProperty("descriptif", medium.getDescriptif());
        jsonMedium.addProperty("type", medium.getType());
        if(medium.getType()=="Voyant"){
            Voyant voyant = (Voyant) medium;
            jsonMedium.addProperty("specialite", voyant.getSpecialite());
        }
        else if (medium.getType()=="Astrologue"){
            Astrologue astro = (Astrologue) medium;
            jsonMedium.addProperty("promotion", astro.getPromotion());
            jsonMedium.addProperty("formation", astro.getFormation());
        }
        jsonContainer.add("medium", jsonMedium);
        
        //on recupere les informations sur la demande de voyance
        Employe employe = (Employe) request.getAttribute("employe");
        JsonObject jsonVoyance = new JsonObject();
        DemandeDeVoyance demandeVoyance = (DemandeDeVoyance) request.getAttribute("voyanceActuelle");
        jsonVoyance.addProperty("etape", demandeVoyance.getEtape());
        jsonContainer.add("voyance", jsonVoyance);
        
        //on recupere les informations pour afficher le profil du client
        JsonObject jsonClient = new JsonObject();
        Client client = (Client) demandeVoyance.getClient();
        jsonClient.addProperty("totem", client.getAnimalTotem());
        jsonClient.addProperty("civilite", client.getCivilite());
        jsonClient.addProperty("bonheur", client.getCouleurPorteBonheur());
        jsonClient.addProperty("nom", client.getNom());
        jsonClient.addProperty("prenom", client.getPrenom());
        String dateNaissance = dateFormatNaissance.format(client.getDateNaissance());  
        jsonClient.addProperty("naissance", dateNaissance);
        jsonClient.addProperty("chinois", client.getSigneChinois());
        jsonClient.addProperty("zodiaque", client.getSigneZodiaque());
        jsonContainer.add("client", jsonClient);
        
        //on recupere les informations pour afficher l'historique du client
        JsonArray jsonArrayVoyances = new JsonArray();
        List<DemandeDeVoyance> voyances = client.getHistorique();
        for (DemandeDeVoyance voyance : voyances){
            String dateDemande = dateFormatDemande.format(voyance.getDate_demande());
            String dateFin = "";
            if (voyance.getDate_fin() != null) {
                dateFin = dateFormatDemande.format(voyance.getDate_fin());
            }
            JsonObject jsonVoyancetmp = new JsonObject();

            jsonVoyancetmp.addProperty("id", voyance.getId());
            jsonVoyancetmp.addProperty("dateDemande", dateDemande);
            jsonVoyancetmp.addProperty("dateFin", dateFin);
            jsonVoyancetmp.addProperty("accepte", voyance.getAccepte());
            jsonVoyancetmp.addProperty("commentaire", voyance.getCommentaire());
            jsonVoyancetmp.addProperty("mediumNom", voyance.getMedium().getNom());
            
            jsonArrayVoyances.add(jsonVoyancetmp);
        }
        jsonContainer.add("historiqueClient", jsonArrayVoyances);
        
        //on recupere les predictions generees
        List<String> predictions = (List<String>) request.getAttribute("predictions");
        JsonObject jsonPredictions = new JsonObject();
        jsonPredictions.addProperty("amour", predictions.get(0));
        jsonPredictions.addProperty("sante", predictions.get(1));
        jsonPredictions.addProperty("travail", predictions.get(2));
        jsonContainer.add("predictions", jsonPredictions);
        
        //Formatage et écriture sur la sortie
        PrintWriter out = this.getWriterWithJsonHeader(response);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(jsonContainer, out);
    }
    
}
