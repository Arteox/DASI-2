/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import metier.modele.Client;
import metier.modele.Employe;
import metier.modele.Medium;

/**
 *
 * @author jcharnay1
 */
public class EmployeDao {

    public static void createEmploye(Employe e) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(e);
    }

    public static void updateEmploye(Employe e) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        e = em.merge(e);
    }

    public static Employe trouverEmploye(long id) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        return em.find(Employe.class, id);
    }

    public static Employe trouverEmploye(String mail, String password) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String request = "Select e from Employe e where e.email = :email and e.mdp = :mdp";
        Query q = em.createQuery(request);
        q.setParameter("email", mail);
        q.setParameter("mdp", password);
        List<Employe> resultat = q.getResultList();
        if (resultat.isEmpty()) {
            //Si aucun employé ne correspond à cet email et ce password
            //alors on renvoit la valeur null
            return null;
        } else {
            //Sinon, on renvoit l'employé qui correspond à ces identifiants
            //(La liste ne comporte forcement qu'un élément compte tenu des contraintes propres à la base de données)
            return resultat.get(0);
        }

    }

    public static List<Employe> trouverEmployesIncarnantMedium(Medium m) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        //On selectionne les employés disponibles et pouvant incarner le médium en argument
        String request = "Select e from Employe e where e.disponible = true and :medium MEMBER OF e.mediumsIncarnes";
        Query q = em.createQuery(request);
        q.setParameter("medium", m);
        return q.getResultList();
    }

    public static List<Employe> getNBClientsParEmployeDesc() {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String request = "Select e from Employe e ORDER BY e.nbAffectations DESC";
        Query q = em.createQuery(request);
        return q.getResultList();
    }
}
