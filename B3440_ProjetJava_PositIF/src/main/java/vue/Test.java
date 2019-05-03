/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import dao.JpaUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import metier.modele.Astrologue;
import metier.modele.Client;
import metier.modele.DemandeDeVoyance;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.modele.Tarologue;
import metier.modele.Voyant;
import metier.service.Service;
import util.Saisie;

/**
 *
 * @author jcharnay1
 */
public class Test {
    private final static SimpleDateFormat HORODATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");
    private final static SimpleDateFormat BIRTHDAY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    
    public static void main(String[] args){
        // Initialisation du JpaUtil
        JpaUtil.init();
        
        System.out.println("");
        System.out.println("### Bienvenue sur la démo de Posit'IF ###");
        System.out.println("Codée par Jacques CHARNAY et Sofiane BENSLIMANE");
        System.out.println("");
        
        //Choix de l'affichage des logs
        Integer input = -1;
        System.out.println("Voulez vous afficher les logs ?");
        System.out.println("(1=oui,0=non)");
        input = lireIntegerEvo("", Arrays.asList(0,1));
        System.out.println("");
        if(input==0)
        {
            //On ferme le flux de sortie des erreurs
            System.err.close();
        }
        
        //Appel au service d'initialisation des mediums et des employes
        Service.initialisation();
        System.out.println("Appel au service d'initialisation des mediums et des employés ...");
        
        input = -1;
        while(input != 3)
        {
            System.out.println("");
            System.out.println("Souhaitez vous accéder ...");
            System.out.println("1 --> aux tests de chaque service");
            System.out.println("2 --> à la simulation du site");
            System.out.println("(Tapez 3 pour quitter la démo)");
            input = lireIntegerEvo("", Arrays.asList(1,2,3));
            if(input == 1)
            {
                testServices();
            }
            else if(input == 2)
            {
                simulationSite();
            }
        }
        
        System.out.println("");
        System.out.println("A bientôt :) !");
        System.out.println("");
        
        // Libération du JpaUtil
        JpaUtil.destroy();
    }
    
    public static void testServices()
    {
        System.out.println("");
        System.out.println("<<< Tests des services >>>");
        System.out.println("");
        
        //Variables nécessaires aux tests
        Long id;
        Long idmed;
        Client c;
        Employe e;
        DemandeDeVoyance d;
        String mail;
        String mdp;
        
        Integer input = -1;
        while(input != 0)
        {
            System.out.println("");
            System.out.println("Quel service souhaitez vous tester ?");
            System.out.println("");
            System.out.println("SITE CLIENT:");
            System.out.println("1 --> trouverClient(Id) : Client");
            System.out.println("2 --> TrouverClient(login,password) : Client");
            System.out.println("3 --> inscrireClient(client) : Boolean");
            System.out.println("4 --> trouverTousLesMedium() : List<Medium>");
            System.out.println("5 --> demanderVoyance(Client,Medium) : Boolean");
            System.out.println("6 --> getHistoriqueDemandes(client) : List<DemandeDeVoyance>");
            System.out.println("");
            System.out.println("SITE EMPLOYE:");
            System.out.println("7 --> trouverEmploye(Id) : Employe");
            System.out.println("8 --> trouverEmploye(login,password) : Employe");
            System.out.println("9 --> ChercherDemandeVoyance(Employe) : DemandeDeVoyance");
            System.out.println("10 --> debuterVoyance(DemandeDeVoyance)");
            System.out.println("11 --> terminerVoyance(DemandeDeVoyance)");
            System.out.println("12 --> faireCRVoyance(DemandeDeVoyance, heureFin,commentaire)");
            System.out.println("13 --> genererPrediction(Client) : List<String>");
            System.out.println("14 --> getNBVoyanceParMediumDesc() : List<Medium>");
            System.out.println("15 --> getNBClientsParEmployeDesc() : List<Employe>");
            System.out.println("");
            System.out.println("(Tapez 0 pour quitter)");
            input = -1;
            while(!(input>=0 && input <= 15))
            {
                input = lireIntegerEvo("");
                if(!(input>=0 && input <= 15))
                {
                    System.out.println("/!\\ Erreur de saisie - Nombre entre 0 et 15 attendu /!\\");
                }
            }
            
            switch(input) {
                case 1:
                    id = (long)lireIntegerEvo("Id client=");
                    c = Service.trouverClient(id);
                    if(c==null)
                    {
                        System.out.println("Aucun client trouvé");
                    }
                    else
                    {
                        System.out.println("Client trouvé:");
                        System.out.println(c);
                    }
                    break;
                case 2:
                    mail = lireChaineEvo("Mail: ");
                    mdp = lireChaineEvo("Mdp: ");
                    c = Service.trouverClient(mail,mdp);
                    if(c != null)
                    {
                        System.out.println("Connexion réussie !");
                        System.out.println("Client correspondant:");
                        System.out.println(c);
                    }
                    else
                    {
                        System.out.println("Echec de la connexion... Email ou mdp incorrecte");
                    }
                  break;
                case 3:
                  inscriptionClient();
                  break;
                case 4:
                    List<Medium> mediums = Service.trouverTousLesMedium();
                    String[] legende = {"Id","Nom","Talent","Descriptif",};
                    int[] size = {4,20,12,80};
                    String[][] data = new String[mediums.size()][4];
                    for(int i=0;i<mediums.size();i++)
                    {
                        data[i][0] = mediums.get(i).getId().toString();
                        data[i][1] = mediums.get(i).getNom();
                        data[i][2] = mediums.get(i).getType();
                        data[i][3] = mediums.get(i).getDescriptif();
                    }
                    afficherData(legende,size,data);
                    break;
                case 5:
                    id = -2l;
                    c = null;
                    while(c==null && id != -1)
                    {
                        id = (long)lireIntegerEvo("Id client=");
                        c = Service.trouverClient(id);
                        if(c==null)
                        {
                            System.out.println("Aucun client trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    
                    List<Medium> mediums2 = Service.trouverTousLesMedium();
                    String[] legende2 = {"Id","Nom","Talent","Descriptif",};
                    int[] size2 = {4,20,12,80};
                    String[][] data2 = new String[mediums2.size()][4];
                    for(int i=0;i<mediums2.size();i++)
                    {
                        data2[i][0] = mediums2.get(i).getId().toString();
                        data2[i][1] = mediums2.get(i).getNom();
                        data2[i][2] = mediums2.get(i).getType();
                        data2[i][3] = mediums2.get(i).getDescriptif();
                    }
                    afficherData(legende2,size2,data2);
                    
                    input = -1;
                    while(!(input>=1 && input <= mediums2.size()))
                    {
                        input = lireIntegerEvo("Id medium =");
                        if(!(input>=1 && input <= mediums2.size()))
                        {
                            System.out.println("/!\\ Erreur de saisie - Nombre entre 1 et "+mediums2.size()+" attendu /!\\");
                        }
                    }
                    idmed = (long)input;
                    
                    boolean res = Service.demanderVoyance(c,mediums2.get(input-1));
                    System.out.println("");
                    if(res)
                    {
                        System.out.println("Reservation effectuée avec succès");
                    }
                    else
                    {
                        System.out.println("Echec de la reservation");
                    }
                    break;
                case 6:
                    id = -2l;
                    c = null;
                    while(c==null && id != -1)
                    {
                        id = (long)lireIntegerEvo("Id client=");
                        c = Service.trouverClient(id);
                        if(c==null)
                        {
                            System.out.println("Aucun client trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    pageHistorique(id);
                    break;
                case 7:
                    id = (long)lireIntegerEvo("Id employé=");
                    e = Service.trouverEmploye(id);
                    if(e==null)
                    {
                        System.out.println("Aucun employé trouvé");
                    }
                    else
                    {
                        System.out.println("Employé trouvé:");
                        System.out.println(e);
                    }
                    break;
                case 8:
                    mail = lireChaineEvo("Mail: ");
                    mdp = lireChaineEvo("Mdp: ");
                    e = Service.trouverEmploye(mail,mdp);
                    if(e != null)
                    {
                        System.out.println("Connexion réussie !");
                        System.out.println("Employé correspondant:");
                        System.out.println(e);
                    }
                    else
                    {
                        System.out.println("Echec de la connexion... Email ou mdp incorrecte");
                    }
                    break;
                case 9:
                    id = -2l;
                    e = null;
                    while(e==null && id != -1)
                    {
                        id = (long)lireIntegerEvo("Id employé=");
                        e = Service.trouverEmploye(id);
                        if(e==null)
                        {
                            System.out.println("Aucun employé trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    d = Service.chercherDemandeVoyance(e);
                    if(d==null)
                    {
                        System.out.println("Aucun demande de voyance assignée à l'employé");
                    }
                    else
                    {
                        System.out.println("Demande de voyance actuelle");
                        System.out.println(d);
                    }
                    break;
                case 10:
                    id = -2l;
                    e = null;
                    while(id != -1 && (e==null || Service.chercherDemandeVoyance(e) == null))
                    {
                        id = (long)lireIntegerEvo("Id employé=");
                        e = Service.trouverEmploye(id);
                        if(e==null)
                        {
                            System.out.println("Aucun employé trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                        else if(Service.chercherDemandeVoyance(e) == null)
                        {
                            System.out.println("Cet empployé n'a pas actuellement de demande");
                        }
                            
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    
                    d = Service.chercherDemandeVoyance(e);
                    Service.debuterVoyance(d);
                    System.out.println("Voyance débutée");
                    
                  break;
                case 11:
                    id = -2l;
                    e = null;
                    while(id != -1 && (e==null || Service.chercherDemandeVoyance(e) == null))
                    {
                        id = (long)lireIntegerEvo("Id employé=");
                        e = Service.trouverEmploye(id);
                        if(e==null)
                        {
                            System.out.println("Aucun employé trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                        else if(Service.chercherDemandeVoyance(e) == null)
                        {
                            System.out.println("Cet empployé n'a pas actuellement de demande");
                        }
                            
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    
                    d = Service.chercherDemandeVoyance(e);
                    Service.terminerVoyance(d);
                    System.out.println("Voyance terminée");
                    break;
                case 12:
                    id = -2l;
                    e = null;
                    while(id != -1 && (e==null || Service.chercherDemandeVoyance(e) == null))
                    {
                        id = (long)lireIntegerEvo("Id employé=");
                        e = Service.trouverEmploye(id);
                        if(e==null)
                        {
                            System.out.println("Aucun employé trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                        else if(Service.chercherDemandeVoyance(e) == null)
                        {
                            System.out.println("Cet empployé n'a pas actuellement de demande");
                        }
                            
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    
                    d = Service.chercherDemandeVoyance(e);
                    System.out.println("");
                    System.out.println("Commentaire:");
                    String commentaire = lireChaineEvo("");
                    Service.faireCRVoyance(d,new Date(), commentaire);
                    System.out.println("Compte rendu sauvegardé !");
                  break;
                case 13:
                    id = -2l;
                    c = null;
                    while(c==null && id != -1)
                    {
                        id = (long)lireIntegerEvo("Id client=");
                        c = Service.trouverClient(id);
                        if(c==null)
                        {
                            System.out.println("Aucun client trouvé");
                            System.out.println("Réessayer (ou tapez -1 pour quitter):");
                        }
                    }
                    if(id == -1)
                    {
                        break;
                    }
                    
                    String[] libelle = {"Amour","Santé","Travail"};
                    System.out.println("");
                    System.out.println("PREDICTIONS SUR MESURE");
                    List<String> predictions = Service.genererPredictions(c);
                    String[] legende3 = {"Domaine","Prediction"};
                    int[] size3 = {10,90};
                    String[][] data3 = new String[3][2];
                    for(int i=0;i<3;i++)
                    {
                        data3[i][0] = libelle[i];
                        data3[i][1] = predictions.get(i);
                    }
                    afficherData(legende3,size3,data3);
                  break;
                case 14:
                  pageTableauDeBord(2l);
                  break;
                case 15:
                  pageTableauDeBord(2l);
                  break;
            }
        }
    }
    
    public static void simulationSite()
    {
        System.out.println("");
        System.out.println("<<< Version complète du site en mode console >>>");
        System.out.println("");
        
        Integer input = -1;
        while(input != 3)
        {
            System.out.println("");
            System.out.println("Souhaitez vous accéder à la version du site pour ...");
            System.out.println("1 --> Les clients");
            System.out.println("2 --> Les employés");
            System.out.println("(Tapez 3 pour quitter la simulation)");
            input = lireIntegerEvo("", Arrays.asList(1,2,3));
            if(input == 1)
            {
                accueilClient();
            }
            else if(input == 2)
            {
                accueilEmploye();
            }
        }
        
    }
    
    public static void accueilClient()
    {
        Integer input = -1;
        System.out.println("");
        System.out.println("Bienvenue sur Posit'IF !");
        System.out.println("Connectez vous ou inscrivez vous et découvrez vite votre avenir !");
        while(input != 3)
        {
            
            System.out.println("");
            System.out.println("Que souhaitez vous faire ?");
            System.out.println("1 --> Se connecter");
            System.out.println("2 --> S'inscrire");
            System.out.println("(Tapez 3 pour quitter le site)");
            input = lireIntegerEvo("", Arrays.asList(1,2,3));
            if(input == 1)
            {
                connexionClient();
            }
            else if(input == 2)
            {
                inscriptionClient();
            }
        }
        
    }
    
    public static void connexionClient()
    {
        System.out.println("");
        System.out.println("Veuillez rentrer votre...");
        String mail = lireChaineEvo("Mail: ");
        String mdp = lireChaineEvo("Mdp: ");
        Client clientActu = Service.trouverClient(mail,mdp);
        if(clientActu != null)
        {
            System.out.println("Connexion réussie !");
            long id = clientActu.getId();
            
            //Lancement du site
            siteClient(id);
        }
        else
        {
            System.out.println("Echec de la connexion... Email ou mdp incorrecte");
        }
    }
    
    public static void inscriptionClient()
    {
        System.out.println("");
        System.out.println("Pour vous inscrire, veuillez rentrer les informations suivantes");
        String nom = lireChaineEvo("Nom: ");
        String prenom = lireChaineEvo("Prenom: ");
        String numtel = lireChaineEvo("Numtel: ");
        System.out.println("Passons à votre date de naissance ...");
        Integer jour;
        do{
            jour = lireIntegerEvo("jour (1-31): ");
            if(!(1<=jour && jour <= 31))
            {
                System.out.println("/!\\ Erreur de saisie - Nombre entre 1 et 31 attendu /!\\");
            }
        }while(!(1<=jour && jour <= 31));
        Integer mois;
        do{
            mois = lireIntegerEvo("mois (1-12): ");
            if(!(1<=mois && mois <= 12))
            {
                System.out.println("/!\\ Erreur de saisie - Nombre entre 1 et 12 attendu /!\\");
            }
        }while(!(1<=mois && mois <= 12));
        Integer annee;
        do{
            annee = lireIntegerEvo("annee: ");
            if(!(1000<=annee && annee <= 9999))
            {
                System.out.println("/!\\ Erreur de saisie - Nombre entre 1000 et 9999 attendu /!\\");
            }
        }while(!(1000<=annee && annee <= 9999));
        System.out.println("Merci ! Passons aux dernières formalités ...");
        String email = lireChaineEvo("Email: ");
        String mdp = lireChaineEvo("Mdp: ");
        System.out.println("");
        
        //Création du nouvel objet client
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = new Date();
        try {
            d = sdf.parse(leftPad(jour.toString(),2,"0")+"/"+leftPad(mois.toString(),2,"0")+"/"+annee.toString());
        } catch (ParseException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        Client nouveau = new Client("M",nom,prenom,d,"7 Avenue Jean Capelle 69100 Villeurbanne",numtel,email,mdp);
        
        //Tentative de création effective dans la base de données
        if(Service.inscrireClient(nouveau))
        {
            System.out.println("Inscription réussie ! Bienvenue dans la communauté de Posit'IF !");
        }
        else
        {
            System.out.println("Echec de l'inscription... Votre email est certainement déjà utilisé par un autre compte.");
        }
    }
    
    public static void siteClient(long id)
    {
        Client c = Service.trouverClient(id);
        System.out.println("");
        System.out.println("Bienvenue "+c.getPrenom()+",");
        Integer input = -1;
        boolean first = true;
        while(input != 4)
        {
            if(!first)
            {
                System.out.println("");
            }
            first = false;
            System.out.println("Quel page souhaitez vous visiter ?");
            System.out.println("1--> Voyance");
            System.out.println("2--> Historique");
            System.out.println("3--> Profil astrologique");
            System.out.println("pour vous déconnecter, tapez 4");
            input = lireIntegerEvo("",Arrays.asList(1,2,3,4));
            if(input == 1)
            {
                pageVoyanceClient(id);
            }
            else if(input == 2)
            {
                pageHistorique(id);
            }
            else if(input == 3)
            {
                pageProfilAstrologique(id);
            }
        }
        
    }
    
    public static void pageVoyanceClient(long id)
    {
        Client c = Service.trouverClient(id);
        System.out.println("");
        System.out.println("Voici tous nos médiums:");
        System.out.println("");
        
        List<Medium> mediums = Service.trouverTousLesMedium();
        String[] legende = {"Id","Nom","Talent","Descriptif",};
        int[] size = {4,20,12,80};
        String[][] data = new String[mediums.size()][4];
        for(int i=0;i<mediums.size();i++)
        {
            data[i][0] = mediums.get(i).getId().toString();
            data[i][1] = mediums.get(i).getNom();
            data[i][2] = mediums.get(i).getType();
            data[i][3] = mediums.get(i).getDescriptif();
        }
        afficherData(legende,size,data);
        
        System.out.println("");
        System.out.println("Tapez l'Id de celui avec qui vous souhaitez prendre rendez-vous");
        System.out.println("(Ou 0 pour quitter)");
        Integer input = -1;
        while(!(input>=0 && input <= mediums.size()))
        {
            input = lireIntegerEvo("");
            if(!(input>=0 && input <= mediums.size()))
            {
                System.out.println("/!\\ Erreur de saisie - Nombre entre 0 et "+mediums.size()+" attendu /!\\");
            }
        }
        
        if(input > 0)
        {
            //On tente de réserver le médium
            boolean res = Service.demanderVoyance(c,mediums.get(input-1));
            System.out.println("");
            if(res)
            {
                System.out.println("Reservation effectuée avec succès !");
                System.out.println("Vous allez recevoir sous peu un SMS vous indiquant comment contacter le médium");
            }
            else
            {
                System.out.println("Désolé, ce médium est actuellement indisponible.");
            }
        }
    }
    
    public static void pageHistorique(long id)
    {
        Client c = Service.trouverClient(id);
        System.out.println("");
        System.out.println("Voici l'historique de vos demandes de voyance:");
        System.out.println("");
        
        //TIMESTAMP_FORMAT.format()
        List<DemandeDeVoyance> demandes = Service.getHistoriqueDemandes(c);
        String[] legende = {"Date","Accepté","Medium","Talent"};
        int[] size = {22,8,20,12};
        String[][] data = new String[demandes.size()][4];
        for(int i=0;i<demandes.size();i++)
        {
            data[i][0] = HORODATE_FORMAT.format(demandes.get(i).getDate_demande());
            data[i][1] = demandes.get(i).getAccepte().toString();
            data[i][2] = demandes.get(i).getMedium().getNom();
            data[i][3] = demandes.get(i).getMedium().getType();
        }
        afficherData(legende,size,data);
    }
    
    public static void pageProfilAstrologique(long id)
    {
        Client c = Service.trouverClient(id);
        System.out.println("");
        System.out.println("Vous êtes né le "+BIRTHDAY_FORMAT.format(c.getDateNaissance()));
        System.out.println("Voici le profil astrologique qui vous correspond:");
        System.out.println("");
        System.out.println("Signe du zodiaque: "+c.getSigneZodiaque());
        System.out.println("Signe chinois: "+c.getSigneChinois());
        System.out.println("Couleur porte-bonheur: "+c.getCouleurPorteBonheur());
        System.out.println("Animal totem: "+c.getAnimalTotem());
    }
    
    public static void accueilEmploye()
    {
        Integer input = -1;
        while(input != 2)
        {
            System.out.println("");
            System.out.println("<<< Posit'IF Version employé >>>");
            System.out.println("Depechez vous de vous connecter et au boulot !");
            System.out.println("");
            System.out.println("Que souhaitez vous faire ?");
            System.out.println("1 --> Se connecter");
            System.out.println("(Tapez 2 pour quitter le site)");
            input = lireIntegerEvo("", Arrays.asList(1,2));
            if(input == 1)
            {
                connexionEmploye();
            }
        }
        
    }
    
    public static void connexionEmploye()
    {
        System.out.println("");
        System.out.println("Veuillez rentrer votre...");
        String mail = lireChaineEvo("Mail: ");
        String mdp = lireChaineEvo("Mdp: ");
        Employe employeActu = Service.trouverEmploye(mail,mdp);
        if(employeActu != null)
        {
            System.out.println("Connexion réussie !");
            long id = employeActu.getId();
            
            //Lancement du site
            siteEmploye(id);
        }
        else
        {
            System.out.println("Echec de la connexion... Email ou mdp incorrecte");
        }
    }
    
    public static void siteEmploye(long id)
    {
        Employe e = Service.trouverEmploye(id);
        System.out.println("");
        System.out.println("Bienvenue "+e.getPrenom()+" "+e.getNom()+",");
        Integer input = -1;
        boolean first = true;
        while(input != 3)
        {
            if(!first)
            {
                System.out.println("");
            }
            first = false;
            System.out.println("Quel page souhaitez vous visiter ?");
            System.out.println("1--> Client actuel");
            System.out.println("2--> Tableau de bord");
            System.out.println("pour vous déconnecter, tapez 3");
            input = lireIntegerEvo("",Arrays.asList(1,2,3));
            if(input == 1)
            {
                pageClientActuel(id);
            }
            else if(input == 2)
            {
                pageTableauDeBord(id);
            }
        }
        
    }
    
    public static void pageClientActuel(long id)
    {
        Employe e = Service.trouverEmploye(id);
        DemandeDeVoyance actu = e.getVoyanceActuelle();
        if(actu != null)
        {
            Integer etape = actu.getEtape();
            if(etape <= 2)
            {
                //Page de présentation du client
                System.out.println("");
                System.out.println("Vous incarnez "+actu.getMedium().getNom()+" ("+actu.getMedium().getType()+")");
                System.out.println("");
                System.out.println("###Profil du client###");
                System.out.println("");
                System.out.println("INFOS DE BASE");
                System.out.println("Civilité: "+actu.getClient().getCivilite());
                System.out.println("Nom: "+actu.getClient().getNom());
                System.out.println("Prenom: "+actu.getClient().getPrenom());
                System.out.println("Naissance: "+BIRTHDAY_FORMAT.format(actu.getClient().getDateNaissance()));
                System.out.println("");
                System.out.println("PROFIL ASTROLOGIQUE");
                System.out.println("Signe du zodiaque: "+actu.getClient().getSigneZodiaque());
                System.out.println("Signe chinois: "+actu.getClient().getSigneChinois());
                System.out.println("Couleur porte-bonheur: "+actu.getClient().getCouleurPorteBonheur());
                System.out.println("Animal totem: "+actu.getClient().getAnimalTotem());
                System.out.println("");
                System.out.println("HISTORIQUE");
                
                List<DemandeDeVoyance> demandes = Service.getHistoriqueDemandes(actu.getClient());
                String[] legende = {"Date","Accepté","Medium","Talent","Commentaire"};
                int[] size = {22,8,20,12,60};
                String[][] data = new String[demandes.size()][5];
                for(int i=0;i<demandes.size();i++)
                {
                    data[i][0] = HORODATE_FORMAT.format(demandes.get(i).getDate_demande());
                    data[i][1] = demandes.get(i).getAccepte().toString();
                    data[i][2] = demandes.get(i).getMedium().getNom();
                    data[i][3] = demandes.get(i).getMedium().getType();
                    data[i][4] = demandes.get(i).getCommentaire();
                }
                afficherData(legende,size,data);
                
                String[] libelle = {"Amour","Santé","Travail"};
                System.out.println("");
                System.out.println("PREDICTIONS SUR MESURE");
                List<String> predictions = Service.genererPredictions(actu.getClient());
                String[] legende2 = {"Domaine","Prediction"};
                int[] size2 = {10,90};
                String[][] data2 = new String[3][2];
                for(int i=0;i<3;i++)
                {
                    data2[i][0] = libelle[i];
                    data2[i][1] = predictions.get(i);
                }
                afficherData(legende2,size2,data2);
                
                System.out.println("");
                System.out.println("Souhaitez vous ...");
                Integer input;
                if(etape == 1)
                {
                    System.out.println("1--> Commencer la séance de voyance");
                    System.out.println("OU taper 0 pour quitter");
                    input = lireIntegerEvo("",Arrays.asList(0,1));
                    if(input == 1)
                    {
                        Service.debuterVoyance(actu);
                        pageClientActuel(id);
                    }
                }
                else
                {
                    System.out.println("1--> Terminer la séance de voyance");
                    System.out.println("OU taper 0 pour quitter");
                    input = lireIntegerEvo("",Arrays.asList(0,1));
                    if(input == 1)
                    {
                        Service.terminerVoyance(actu);
                        pageClientActuel(id);
                    }
                }
            }
            else
            {
                //Compte rendu
                System.out.println("");
                System.out.println("Veuillez faire votre compte rendu de la séance de voyance");
                System.out.println("");
                
                System.out.println("La séance s'est terminée à ...");
                Integer heure;
                do{
                    heure = lireIntegerEvo("heure (0-23): ");
                    if(!(0<=heure && heure <= 23))
                    {
                        System.out.println("/!\\ Erreur de saisie - Nombre entre 0 et 23 attendu /!\\");
                    }
                }while(!(0<=heure && heure <= 23));
                Integer minute;
                do{
                    minute = lireIntegerEvo("minute (0-59): ");
                    if(!(0<=minute && minute <= 59))
                    {
                        System.out.println("/!\\ Erreur de saisie - Nombre entre 0 et 59 attendu /!\\");
                    }
                }while(!(0<=minute && minute <= 59));
                Date heureFin = new Date();
                heureFin.setHours(heure);
                heureFin.setMinutes(minute);
                
                System.out.println("");
                System.out.println("Un commentaire sur la séance ?");
                String commentaire = lireChaineEvo("");
                
                Service.faireCRVoyance(actu,heureFin,commentaire);
                System.out.println("");
                System.out.println("Merci ! Votre compte rendu a été enregistré.");
            }
        }
        else
        {
            System.out.println("");
            System.out.println("Aucun client ne vous est actuellement assigné ...");
        }
    }
    
    public static void pageTableauDeBord(long id)
    {
        System.out.println("");
        System.out.println("### TABLEAU DE BORD ###");
        System.out.println("");
        System.out.println("Graphique du nombre de voyances par médium:");
        System.out.println("");
        
        List<Medium> mediums = Service.getNBVoyanceParMediumDesc();
        String[] legende = {"Medium","Talent","Nombre de voyances"};
        int[] size = {20,12,20};
        String[][] data = new String[mediums.size()][3];
        for(int i=0;i<mediums.size();i++)
        {
            data[i][0] = mediums.get(i).getNom();
            data[i][1] = mediums.get(i).getType();
            data[i][2] = mediums.get(i).getNbAffectations().toString();
        }
        afficherData(legende,size,data);
        
        System.out.println("");
        System.out.println("Graphique du nombre de clients par employé:");
        System.out.println("");
        
        List<Employe> employes = Service.getNBClientsParEmployeDesc();
        String[] legende2 = {"Employe","Nombre de clients"};
        int[] size2 = {20,10};
        String[][] data2 = new String[employes.size()][2];
        for(int i=0;i<employes.size();i++)
        {
            data2[i][0] = employes.get(i).getPrenom()+" "+employes.get(i).getNom();
            data2[i][1] = employes.get(i).getNbAffectations().toString();
        }
        afficherData(legende2,size2,data2);
    }
    
    public static String lireChaineEvo(String invite)
    {
        boolean interruption = true;
        String res = "";
        while(interruption || res.length() == 0)
        {
            interruption = false;
            res = Saisie.lireChaine(invite);
            if(res == "state")
            {
                interruption = true;
                //Something
            }
            if(res.length() == 0)
            {
                System.out.println("/!\\ Erreur de saisie - Votre réponse doit au moins comporter un caractère /!\\");
            }
        }
        
        return res;
    }
    
    public static Integer lireIntegerEvo(String invite) {
        Integer valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Integer.parseInt(lireChaineEvo(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier attendu /!\\");
            }
        }
        return valeurLue;
    }
    
    public static Integer lireIntegerEvo(String invite, List<Integer> valeursPossibles) {
        Integer valeurLue = null;
        while (valeurLue == null) {
            try {
                valeurLue = Integer.parseInt(lireChaineEvo(invite));
            } catch (NumberFormatException ex) {
                System.out.println("/!\\ Erreur de saisie - Nombre entier attendu /!\\");
            }
            if (!(valeursPossibles.contains(valeurLue))) {
                System.out.println("/!\\ Erreur de saisie - Valeur non-autorisée /!\\");
                valeurLue = null;
            }
        }
        return valeurLue;
    }

    public static String rightPad(String s,int longueur,String c)
    {
        String res = s;
        for(int i=0;i<(longueur-s.length());i++)
        {
            res += c;
        }
        return res;
    }
    
    public static String leftPad(String s,int longueur,String c)
    {
        String res = s;
        for(int i=0;i<(longueur-s.length());i++)
        {
            res = c + res;
        }
        return res;
    }

    public static void afficherData(String[] legende,int[] size,String[][] data)
    {
        if(data.length == 0)
        {
            return;
        }

        //Affichage de la légende
        System.out.print(rightPad(legende[0],size[0]," "));
        for(int i=1;i<legende.length;i++)
        {
            System.out.print(" | ");
            System.out.print(rightPad(legende[i],size[i]," "));
        }
        System.out.println("");
        System.out.print(rightPad("",size[0],"-"));
        for(int i=1;i<legende.length;i++)
        {
            System.out.print("---");
            System.out.print(rightPad("",size[i],"-"));
        }
        System.out.println("");
        
        //Affichage des données
        int[] taille = new int[data[0].length];
        int maxLine;
        int actuLine;
        int begin;
        int end;
        for(int i=0;i<data.length;i++)
        {
            maxLine = 0;
            for(int j=0;j<data[0].length;j++)
            {
                taille[j] = data[i][j].length();
                actuLine = (int)((double)taille[j]/(double)size[j]);
                if((double)taille[j]/(double)size[j] == actuLine && actuLine > 0)
                {
                    actuLine -= 1;
                }
                if(actuLine > maxLine)
                {
                    maxLine = actuLine;
                }
            }
            for(int line=0;line<=maxLine;line++)
            {
                begin = line*size[0];
                end = (line+1)*size[0];
                if(end>taille[0])
                {
                    end = taille[0];
                }
                if(begin<taille[0])
                {
                    System.out.print(rightPad(data[i][0].substring(begin,end),size[0]," "));
                }
                else
                {
                    System.out.print(rightPad("",size[0]," "));
                }
                for(int j=1;j<data[0].length;j++)
                {
                    System.out.print(" | ");
                    begin = line*size[j];
                    end = (line+1)*size[j];
                    if(end>taille[j])
                    {
                        end = taille[j];
                    }
                    if(begin<taille[j])
                    {
                        System.out.print(rightPad(data[i][j].substring(begin,end),size[j]," "));
                    }
                    else
                    {
                        System.out.print(rightPad("",size[j]," "));
                    }
                }
                System.out.println("");
            }
        }
    }
}

