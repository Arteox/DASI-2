/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author jcharnay1
 */
@Entity
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String nom;
    protected String prenom;
    @Column(nullable = false,unique = true)
    protected String email;
    protected String mdp;
    protected Boolean disponible;
    protected Integer nbAffectations;
    protected String tel;
    
    @OneToMany
    protected List<DemandeDeVoyance> voyancesRealises;
    @OneToOne
    protected DemandeDeVoyance voyanceActuelle;
    // null si l'employé n'a actuellement aucune voyance en cours
    @ManyToMany
    protected List<Medium> mediumsIncarnes;

    public Employe(String nom,String prenom,String email, String mdp, Boolean disponible, Integer nbAffectations, String tel,List<Medium> mediumsIncarnes) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
        this.disponible = disponible;
        this.nbAffectations = nbAffectations;
        this.tel = tel;
        this.mediumsIncarnes = mediumsIncarnes;
        voyancesRealises = new ArrayList<DemandeDeVoyance>();
    }
    
    public void ajouterVoyance(DemandeDeVoyance d)
    {
        voyancesRealises.add(d);
    }

    public Employe(){
    }

    public Long getId() {
        return id;
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

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Integer getNbAffectations() {
        return nbAffectations;
    }

    public void setNbAffectations(Integer nbAffectations) {
        this.nbAffectations = nbAffectations;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public DemandeDeVoyance getVoyanceActuelle() {
        return voyanceActuelle;
    }

    public void setVoyanceActuelle(DemandeDeVoyance voyanceActuelle) {
        this.voyanceActuelle = voyanceActuelle;
    }
    
    public void incrementerNbAffectations()
    {
        nbAffectations++;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return "Employe{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", email=" + email + ", mdp=" + mdp + ", disponible=" + disponible + ", nbAffectations=" + nbAffectations + ", tel=" + tel + ", mediumsIncarnes=" + mediumsIncarnes + '}';
    }
    
    
}
