/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.service;

import dao.ClientDao;
import dao.DemandeDeVoyanceDao;
import dao.EmployeDao;
import dao.JpaUtil;
import dao.MediumDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Astrologue;
import metier.modele.Client;
import metier.modele.DemandeDeVoyance;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.modele.Tarologue;
import metier.modele.Voyant;
import util.AstroTest;
import util.Message;

/**
 *
 * @author jcharnay1
 */
public class Service {
    private final static SimpleDateFormat HORODATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy à HH:mm:ss");

    public static boolean inscrireClient(Client c) {
        //On récupère le profil astrologique
        mettreAJourProfilAstrologique(c);

        boolean clientExisteDeja = trouverClient(c.getEmail());
        if (clientExisteDeja) {
            //Si un client a déjà cet email
            //L'inscription ne peut se faire et on envoit un email pour le signaler
            StringWriter corps = new StringWriter();
            PrintWriter mailWriter = new PrintWriter(corps);

            mailWriter.println("Bonjour " + c.getPrenom() + ",");
            mailWriter.println("votre inscription au service POSIT'IF a malencontreusement échoué ...");
            mailWriter.println("Merci de recommencer ultérieurement");

            Message.envoyerMail("contact@posif.if", c.getEmail(), "Bienvenue chez POSIT'IF", corps.toString());
        } else {
            //Sinon, le client est effectivement inscrit
            //et on lui envoit un email pour lui annoncer la bonne nouvelle !
            JpaUtil.creerEntityManager();
            JpaUtil.ouvrirTransaction();
            try {
                ClientDao.createClient(c);
                JpaUtil.validerTransaction();
            } catch (Exception e) {
                e.printStackTrace();
                JpaUtil.annulerTransaction();
            }
            JpaUtil.fermerEntityManager();

            StringWriter corps = new StringWriter();
            PrintWriter mailWriter = new PrintWriter(corps);

            mailWriter.println("Bonjour " + c.getPrenom() + ",");
            mailWriter.println("nous vous confirmons votre inscription au service POSIT'IF.");
            mailWriter.println("Votre numéro de client est : " + c.getId());

            Message.envoyerMail("contact@posif.if", c.getEmail(), "Bienvenue chez POSIT'IF", corps.toString());
        }

        return !clientExisteDeja;

    }

    public static void mettreAJourProfilAstrologique(Client c) {
        //Sous-service associé à ajouterClient
        
        //On se connecte à l'api AstroNet
        AstroTest astroApi = new AstroTest();
        try {
            //On récupère le profil astrologique correspondant au prenom et à la date de naissance du client
            List<String> profil = astroApi.getProfil(c.getPrenom(), c.getDateNaissance());
            c.setSigneZodiaque(profil.get(0));
            c.setSigneChinois(profil.get(1));
            c.setCouleurPorteBonheur(profil.get(2));
            c.setAnimalTotem(profil.get(3));
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static boolean trouverClient(String email) {
        //Sous-service associé à ajouterClient
        //Renvoit True si un client possède déjà ce mail
        JpaUtil.creerEntityManager();
        boolean res = ClientDao.trouverClient(email);
        JpaUtil.fermerEntityManager();
        return res;
    }

    public static Client trouverClient(String email, String password) {
        //Cherche dans la base de données si le couple (login, password) est associé à un client connu
        //Retourne le dit client s'il existe, null sinon
        JpaUtil.creerEntityManager();
        Client res = ClientDao.trouverClient(email, password);
        JpaUtil.fermerEntityManager();
        return res;
    }

    public static Client trouverClient(long id) {
        //Renvoie le client associé à cet Id
        JpaUtil.creerEntityManager();
        Client res = ClientDao.trouverClient(id);
        JpaUtil.fermerEntityManager();
        return res;
    }
    
    public static Employe trouverEmploye(long id) {
        //Renvoie l'employé associé à cet Id
        JpaUtil.creerEntityManager();
        Employe res = EmployeDao.trouverEmploye(id);
        JpaUtil.fermerEntityManager();
        return res;
    }

    public static Employe trouverEmploye(String email, String password) {
        //Cherche dans la base de données si le couple (login, password) est associé à un employé connu
        //Retourne le dit employé s'il existe, null sinon
        JpaUtil.creerEntityManager();
        Employe res = EmployeDao.trouverEmploye(email, password);
        JpaUtil.fermerEntityManager();
        return res;
    }
    
    public static Medium trouverMedium(long id) {
        //Renvoie l'employé associé à cet Id
        JpaUtil.creerEntityManager();
        Medium res = MediumDao.trouverMedium(id);
        JpaUtil.fermerEntityManager();
        return res;
    }

    public static List<Medium> trouverTousLesMedium() {
        JpaUtil.creerEntityManager();
        List<Medium> res = MediumDao.recuperTousLesMedium();
        JpaUtil.fermerEntityManager();
        return res;
    }

    public static boolean demanderVoyance(Client c, Medium m) {
        //On crée la demande de voyance:
        DemandeDeVoyance demandeActu = new DemandeDeVoyance(new Date(), 1, c, m);
        //On l'ajoute à l'historique du client:
        c.ajouterDemandeAHistorique(demandeActu);
        boolean resultat = false;

        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            //On cherche tous les employés pouvant incarner le médium et étant disponible:
            List<Employe> employesDispos = EmployeDao.trouverEmployesIncarnantMedium(m);
            if (employesDispos.isEmpty()) {
                //Si aucun employé n'est disponible pour incarner le médium
                //Alors la demande est marqué comme refusé et la fonction renvoit False
                demandeActu.setAccepte(false);
                resultat = false;

                DemandeDeVoyanceDao.createDemandeDeVoyance(demandeActu);
                ClientDao.updateClient(c);
            } else {
                //S'il existe des employés disponibles et pouvant incarner le médium
                demandeActu.setAccepte(true);
                resultat = true;
                int minAffectations = employesDispos.get(0).getNbAffectations();
                int indice = 0;
                for (int i = 0; i < employesDispos.size(); i++) {
                    if (employesDispos.get(i).getNbAffectations() < minAffectations) {
                        indice = i;
                        minAffectations = employesDispos.get(i).getNbAffectations();
                    }
                }
                //Alors on choisit celui ayant le plus petit nombre d'affectations:
                Employe choisi = employesDispos.get(indice);
                demandeActu.setEmployeAffecte(choisi);
                choisi.ajouterVoyance(demandeActu);
                choisi.setVoyanceActuelle(demandeActu);
                choisi.setDisponible(false);
                choisi.incrementerNbAffectations();
                m.incrementerNbAffectations();

                DemandeDeVoyanceDao.createDemandeDeVoyance(demandeActu);
                ClientDao.updateClient(c);
                EmployeDao.updateEmploye(choisi);
                MediumDao.updateMedium(m);
                
                //Enfin, on envoit un SMS à l'employé affecté pour le prevenir de la
                //séance de voyance qu'il doit effectuer
                StringWriter message = new StringWriter();
                PrintWriter notificationWriter = new PrintWriter(message);

                notificationWriter.println("Voyance demandée le "+HORODATE_FORMAT.format(demandeActu.getDate_demande())+" pour "+c.getPrenom()+" "+c.getNom()+" (#"+c.getId()+")");
                notificationWriter.println("Médium à incarner : "+m.getNom());

                Message.envoyerNotification(choisi.getTel(),message.toString());
            }
            JpaUtil.validerTransaction();
        } catch (Exception ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
            JpaUtil.annulerTransaction();
        }
        
        JpaUtil.fermerEntityManager();
        
        return resultat;
    }
    
    public static List<DemandeDeVoyance> getHistoriqueDemandes(Client c)
    {
        return c.getHistorique();
    }
    
    public static DemandeDeVoyance chercherDemandeVoyance(Employe e)
    {
        //Renvoit null si l'employe n'est actuellement associé à
        //aucune demande de voyance
        return e.getVoyanceActuelle();
    }

    public static void debuterVoyance(DemandeDeVoyance d)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            d.setEtape(2);//etape=2 signifie que la voyance a débuté
            DemandeDeVoyanceDao.updateDemandeDeVoyance(d);
            
            StringWriter message = new StringWriter();
            PrintWriter notificationWriter = new PrintWriter(message);

            //On envoit un SMS au client pour le prévenir que le médium est prêt et lui communiquer son numéro
            notificationWriter.println("Votre demande de voyance du "+HORODATE_FORMAT.format(d.getDate_demande())+" a bien été enregistrée.");
            notificationWriter.println("Vous pouvez dès à présent me contacter au "+d.getEmployeAffecte().getTel()+". A tout de suite !");
            notificationWriter.println("Posit'ifement vôtre, "+d.getMedium().getNom());

            Message.envoyerNotification(d.getClient().getNumtel(),message.toString());
            
            JpaUtil.validerTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }
        JpaUtil.fermerEntityManager();
    }
    
    public static void terminerVoyance(DemandeDeVoyance d)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            d.setEtape(3);//etape=3 signifie que la voyance est terminé
            DemandeDeVoyanceDao.updateDemandeDeVoyance(d);
            JpaUtil.validerTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }
        JpaUtil.fermerEntityManager();
    }
    
    public static void faireCRVoyance(DemandeDeVoyance d,Date heureFin,String commentaire)
    {
        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            d.setEtape(4);//etape=4 signifie que la voyance est terminé et que l'employé a fait son compte rendu
            d.setDate_fin(heureFin);
            d.setCommentaire(commentaire);
            Employe choisi = d.getEmployeAffecte();
            //Une fois sa séance de voyance terminée,
            //L'employé redevient disponible et il n'a plus de DemandeDeVoyance actuelle qui lui est associée
            choisi.setDisponible(true);
            choisi.setVoyanceActuelle(null);
            
            EmployeDao.updateEmploye(choisi);
            DemandeDeVoyanceDao.updateDemandeDeVoyance(d);
            
            JpaUtil.validerTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }
        JpaUtil.fermerEntityManager();
    }
    
    public static List<Medium> getNBVoyanceParMediumDesc()
    {
        JpaUtil.creerEntityManager();
        List<Medium> res = MediumDao.getNBVoyanceParMediumDesc();
        JpaUtil.fermerEntityManager();
        return res;
    }
    
    public static List<Employe> getNBClientsParEmployeDesc()
    {
        JpaUtil.creerEntityManager();
        List<Employe> res = EmployeDao.getNBClientsParEmployeDesc();
        JpaUtil.fermerEntityManager();
        return res;
    }
    
    public static List<String> genererPredictions(Client c)
    {
        //On génère aléatoirement un entier entre 1 (mauvais) à 4(excellent)
        //pour définir si les prédictions dans chaque domaine sont
        //bonnes ou mauvaises
        int amour = (int)(Math.random()*4) +1;
        int sante = (int)(Math.random()*4) +1;
        int travail = (int)(Math.random()*4) +1;
        List<String> resultat = null;
        try {
            //On se connecte à l'api AstroNet et on récupère les prédictions adaptées au client
            AstroTest astroApi = new AstroTest();
            resultat = astroApi.getPredictions(c.getCouleurPorteBonheur(),c.getAnimalTotem(),amour,sante,travail);
        } catch (IOException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }
    
    public static void initialisation() {
        //Créer tous les médiums et les employés "en dur" dans la base de données

        JpaUtil.creerEntityManager();
        JpaUtil.ouvrirTransaction();
        try {
            Voyant a1 = new Voyant("Boule de cristal", "Gwenaël", "Specialistes des grandes conversations au-delà de TOUTES les frontières.", 0);
            Voyant a2 = new Voyant("Marc de Café", "Professeur Maxwell", "Votre avenir est devant vous: regardons le ensemble !", 0);
            Voyant a3 = new Voyant("L'espace et au delà", "Albator", "Venez explorer de nouveaux horizons avec moi !", 0);
            Voyant a4 = new Voyant("Décrypter les augures", "Ulysse", "J'irai jusqu'à défier les dieux pour changer nos destinées !", 0);
            Voyant a5 = new Voyant("L'amour", "Kallen", "Votre coeur n'a aucun secret pour moi !", 0);
            Voyant a6 = new Voyant("Science", "Shiro", "Laissez moi calculer le chemin qui menera à vos rêves !", 0);
            Tarologue a7 = new Tarologue("Mme Irma", "Comprenez votre entourage grâce à mes cartes ! Résultats rapides.", 0);
            Tarologue a8 = new Tarologue("Endora", "Mes cartes répondront à toutes vos questions personnelles.", 0);
            Tarologue a9 = new Tarologue("Sofia", "Mes cartes me permettront de trouver le coupable", 0);
            Tarologue a10 = new Tarologue("Paul", "L'irremplacable, le magistral, Paul.", 0);
            Tarologue a11 = new Tarologue("JDG", "Lira votre avenir dans les vieilles cartouches", 0);
            Tarologue a12 = new Tarologue("Lelouche", "Champion de poker et Tarologue accessoirement", 0);
            Astrologue a13 = new Astrologue("école Normale Supérieure d'Astrologie (ENS-Astro)", "2006", "Serena", "Basée à Champigny-sur-Marne, Serena vous révèlera votre avenir pour éclairer votre passée.", 0);
            Astrologue a14 = new Astrologue("Institut des Nouveaux Savoirs Astrologiques", "2008", "Armin", "Se battra jusqu'au bout pour un futur meilleur!", 0);
            Astrologue a15 = new Astrologue("Institut national du pays champignon", "2453", "Mario", "J'irai jusqu'au bout de la galaxie pour vous sauver!", 0);
            Astrologue a16 = new Astrologue("Ecole polytechnique d'Hyrule", "1125", "link", "Sait lire l'avenir dans les étoiles et les pots cassés", 0);
            Astrologue a17 = new Astrologue("Institut National des Saintes Vérites", "2018", "Mr Jack", "Partiel, partiel, que nous réserves-tu?", 0);
            Astrologue a18 = new Astrologue("Institut des Nouveaux Savoirs Astrologiques", "2010", "Mr M. Histaire-Hyeux", "Avenir, avenir, que nous réserves-tu? N'attendez plus, demandez à me consulter!", 0);

            MediumDao.createMedium(a1);
            MediumDao.createMedium(a7);
            MediumDao.createMedium(a13);
            MediumDao.createMedium(a2);
            MediumDao.createMedium(a8);
            MediumDao.createMedium(a14);
            MediumDao.createMedium(a3);
            MediumDao.createMedium(a9);
            MediumDao.createMedium(a15);
            MediumDao.createMedium(a4);
            MediumDao.createMedium(a10);
            MediumDao.createMedium(a16);
            MediumDao.createMedium(a5);
            MediumDao.createMedium(a11);
            MediumDao.createMedium(a17);
            MediumDao.createMedium(a6);
            MediumDao.createMedium(a12);
            MediumDao.createMedium(a18);

            //On construit les listes indiquant les médiums que chaque employé
            //est capable d'incarner
            ArrayList<Medium> l1 = new ArrayList<Medium>();
            l1.add(a4);
            l1.add(a5);
            ArrayList<Medium> l2 = new ArrayList<Medium>();
            l2.add(a5);
            l2.add(a6);
            ArrayList<Medium> l3 = new ArrayList<Medium>();
            l3.add(a4);
            l3.add(a6);
            ArrayList<Medium> l4 = new ArrayList<Medium>();
            ArrayList<Medium> l5 = new ArrayList<Medium>();
            l5.add(a1);
            l5.add(a3);
            
            ArrayList<Medium> l6 = new ArrayList<Medium>();
            l6.add(a7);
            ArrayList<Medium> l7 = new ArrayList<Medium>();
            l7.add(a10);
            l7.add(a11);
            ArrayList<Medium> l8 = new ArrayList<Medium>();
            l8.add(a11);
            l8.add(a12);
            ArrayList<Medium> l9 = new ArrayList<Medium>();
            l9.add(a10);
            l9.add(a12);
            ArrayList<Medium> l10 = new ArrayList<Medium>();
            l10.add(a8);
            
            ArrayList<Medium> l11 = new ArrayList<Medium>();
            l11.add(a13);
            l11.add(a16);
            ArrayList<Medium> l12 = new ArrayList<Medium>();
            l12.add(a14);
            l12.add(a16);
            ArrayList<Medium> l13 = new ArrayList<Medium>();
            l13.add(a16);
            l13.add(a17);
            ArrayList<Medium> l14 = new ArrayList<Medium>();
            l14.add(a17);
            l14.add(a18);
            ArrayList<Medium> l15 = new ArrayList<Medium>();
            l15.add(a15);
            l15.add(a16);
            l15.add(a18);

            Employe e1 = new Employe("Avuleur","Edith","edith.avuleur@gmail.com", "1234", true, 0, "0668744778", l1);
            Employe e2 = new Employe("Bonneau","Jean","jean.bonneau@gmail.com", "1234", true, 0, "068475474", l2);
            Employe e3 = new Employe("Dissoir","Alain","alain.dissoir@gmail.com", "1234", true, 0, "0654782541", l3);
            Employe e4 = new Employe("Deuf","John","john.deuf@gmail.com", "1234", true, 0, "06547825323", l4);
            Employe e5 = new Employe("Eperil","Asterix","asterix.eperil@gmail.com", "1234", true, 0, "0632541754", l5);
            Employe e6 = new Employe("Goudy","Debby","debby.goudy@gmail.com", "1234", true, 0, "0632541751", l6);
            Employe e7 = new Employe("Mensoif","Gérard","gerard.mensoif@gmail.com", "1234", true, 0, "0632541752", l7);
            Employe e8 = new Employe("Naimes","Aimée","aimee.naimes@gmail.com", "1234", true, 0, "0632541753", l8);
            Employe e9 = new Employe("Onette","Camille","camille.onette@gmail.com", "1234", true, 0, "0632541754", l9);
            Employe e10 = new Employe("Pote","Jessie","jessie.pote@gmail.com", "1234", true, 0, "0632541755", l10);
            Employe e11 = new Employe("Slatable","Déborha","deborha.slatable@gmail.com", "1234", true, 0, "0632541756", l11);
            Employe e12 = new Employe("Yar","Aline","aline.yar@gmail.com", "1234", true, 0, "0632541757", l12);
            Employe e13 = new Employe("Zola","Gordon","gordon.zola@gmail.com", "1234", true, 0, "0632541758", l13);
            Employe e14 = new Employe("Huleur","Octave","octave.huleur@gmail.com", "1234", true, 0, "0632541759", l14);
            Employe e15 = new Employe("Coptaire","Elie","elie.coptaire@gmail.com", "1234", true, 0, "0632541777", l15);
            

            EmployeDao.createEmploye(e1);
            EmployeDao.createEmploye(e2);
            EmployeDao.createEmploye(e3);
            EmployeDao.createEmploye(e4);
            EmployeDao.createEmploye(e5);
            EmployeDao.createEmploye(e6);
            EmployeDao.createEmploye(e7);
            EmployeDao.createEmploye(e8);
            EmployeDao.createEmploye(e9);
            EmployeDao.createEmploye(e10);
            EmployeDao.createEmploye(e11);
            EmployeDao.createEmploye(e12);
            EmployeDao.createEmploye(e13);
            EmployeDao.createEmploye(e14);
            EmployeDao.createEmploye(e15);

            JpaUtil.validerTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }
        JpaUtil.fermerEntityManager();
    }

    // next thing to do : probleme association medium/employe
}
