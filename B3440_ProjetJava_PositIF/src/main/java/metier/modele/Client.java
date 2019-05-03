/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author jcharnay1
 */

@Entity
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String civilite;
    @Column(nullable = false)
    protected String nom;
    @Column(nullable = false)
    protected String prenom;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    protected Date dateNaissance;
    
    protected String adressePostale;
    protected String numtel;
    @Column(nullable = false,unique = true)
    protected String email;
    @Column(nullable = false)
    protected String mdp;
    
    protected String signeZodiaque;
    protected String signeChinois;
    protected String couleurPorteBonheur;
    protected String animalTotem;
    
    @OneToMany(mappedBy="client")
    protected List<DemandeDeVoyance> historique;

    public Client() {
    }

    public Client(String civilite, String nom, String prenom, Date dateNaissance, String adressePostale, String numtel, String email, String mdp) {
        this.civilite = civilite;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.adressePostale = adressePostale;
        this.numtel = numtel;
        this.email = email;
        this.mdp = mdp;
        historique = new ArrayList<DemandeDeVoyance>();
    }
    
    public void ajouterDemandeAHistorique(DemandeDeVoyance d)
    {
        historique.add(d);
    }

    public long getId() {
        return id;
    }

    public String getCivilite() {
        return civilite;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getAdressePostale() {
        return adressePostale;
    }

    public void setAdressePostale(String adressePostale) {
        this.adressePostale = adressePostale;
    }

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getSigneZodiaque() {
        return signeZodiaque;
    }

    public void setSigneZodiaque(String signeZodiaque) {
        this.signeZodiaque = signeZodiaque;
    }

    public String getSigneChinois() {
        return signeChinois;
    }

    public void setSigneChinois(String signeChinois) {
        this.signeChinois = signeChinois;
    }

    public String getCouleurPorteBonheur() {
        return couleurPorteBonheur;
    }

    public void setCouleurPorteBonheur(String couleurPorteBonheur) {
        this.couleurPorteBonheur = couleurPorteBonheur;
    }

    public String getAnimalTotem() {
        return animalTotem;
    }

    public void setAnimalTotem(String animalTotem) {
        this.animalTotem = animalTotem;
    }

    public List<DemandeDeVoyance> getHistorique() {
        return historique;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", civilite=" + civilite + ", nom=" + nom + ", prenom=" + prenom + ", dateNaissance=" + dateNaissance + ", adressePostale=" + adressePostale + ", numtel=" + numtel + ", email=" + email + ", mdp=" + mdp + ", signeZodiaque=" + signeZodiaque + ", signeChinois=" + signeChinois + ", couleurPorteBonheur=" + couleurPorteBonheur + ", animalTotem=" + animalTotem + '}';
    }
    
    
}
