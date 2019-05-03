/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.List;
import javax.persistence.Entity;

/**
 *
 * @author jcharnay1
 */
@Entity
public class Voyant extends Medium{
    protected String specialite;

    public Voyant(String specialite, String nom, String descriptif, Integer NbAffectations) {
        super(nom, descriptif, NbAffectations);
        this.specialite = specialite;
    }
    
    

    public Voyant() {
    }

    @Override
    public String toString() {
        return "Voyant{" +super.toString()+ "specialite=" + specialite + '}';
    }
    
    public String getType()
    {
        return "Voyant";
    }
}
