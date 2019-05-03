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
public class Astrologue extends Medium{
    protected String formation;
    protected String promotion;

    public Astrologue(String formation, String promotion, String nom, String descriptif, Integer NbAffectations) {
        super(nom, descriptif, NbAffectations);
        this.formation = formation;
        this.promotion = promotion;
    }
    
    

    public Astrologue() {
    }

    @Override
    public String toString() {
        return "Astrologue{" +super.toString()+ "formation=" + formation + ", promotion=" + promotion + '}';
    }
    
    public String getType()
    {
        return "Astrologue";
    }
}
