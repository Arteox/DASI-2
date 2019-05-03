/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.List;
import javax.persistence.*;

/**
 *
 * @author jcharnay1
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Medium {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String nom;
    protected String descriptif;
    protected Integer nbAffectations;

    public Medium(String nom, String descriptif, Integer nbAffectations) {
        this.nom = nom;
        this.descriptif = descriptif;
        this.nbAffectations = nbAffectations;
    }

    protected Medium() {
    }
    
    public void incrementerNbAffectations()
    {
        nbAffectations++;
    }

    @Override
    public String toString() {
        return "{" + "id=" + id + ", nom=" + nom + ", descriptif=" + descriptif + ", nbAffectations=" + nbAffectations + '}';
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescriptif() {
        return descriptif;
    }

    public void setDescriptif(String descriptif) {
        this.descriptif = descriptif;
    }

    public Integer getNbAffectations() {
        return nbAffectations;
    }

    public void setNbAffectations(Integer nbAffectations) {
        this.nbAffectations = nbAffectations;
    }
    
    public abstract String getType();

}
