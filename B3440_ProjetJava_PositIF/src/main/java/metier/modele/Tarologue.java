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
public class Tarologue extends Medium{

    public Tarologue(String nom, String descriptif, Integer NbAffectations) {
        super(nom, descriptif, NbAffectations);
    }
    
    

    public Tarologue() {
        super();
    }

    @Override
    public String toString() {
        return "Tarologue{" +super.toString()+ '}';
    }
    
    public String getType()
    {
        return "Tarologue";
    }
    
}
