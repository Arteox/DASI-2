/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Employe;
import metier.modele.Medium;

/**
 *
 * @author jcharnay1
 */
public class MediumDao {
    
    public static Medium trouverMedium(long id) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        return em.find(Medium.class, id);
    }

    public static void createMedium(Medium m) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(m);
    }

    public static void updateMedium(Medium m) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        m = em.merge(m);
    }

    public static List<Medium> recuperTousLesMedium() {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String request = "Select m from Medium m ORDER BY m.id";
        Query q = em.createQuery(request);
        return q.getResultList();
    }

    public static List<Medium> getNBVoyanceParMediumDesc() {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String request = "Select m from Medium m ORDER BY m.nbAffectations DESC";
        Query q = em.createQuery(request);
        return q.getResultList();
    }

}
