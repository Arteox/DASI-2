/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.persistence.EntityManager;
import metier.modele.DemandeDeVoyance;

/**
 *
 * @author jcharnay1
 */
public class DemandeDeVoyanceDao {

    public static void createDemandeDeVoyance(DemandeDeVoyance d) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(d);
    }

    public static void updateDemandeDeVoyance(DemandeDeVoyance d) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        d = em.merge(d);
    }
}
