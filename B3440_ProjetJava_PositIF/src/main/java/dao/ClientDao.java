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

/**
 *
 * @author jcharnay1
 */
public class ClientDao {

    public static void createClient(Client c) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        em.persist(c);
    }

    public static void updateClient(Client c) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        c = em.merge(c);
    }

    public static Client trouverClient(String mail, String password) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String request = "Select c from Client c where c.email = :email and c.mdp = :mdp";
        Query q = em.createQuery(request);
        q.setParameter("email", mail);
        q.setParameter("mdp", password);
        List<Client> resultat = q.getResultList();
        if (resultat.isEmpty()) {
            //Si aucun client ne correspond à cet email et ce password
            //alors on renvoit la valeur null
            return null;
        } else {
            //Sinon, on renvoit le client qui correspond à ces identifiants
            //(La liste ne comporte forcement qu'un élément compte tenu des contraintes propres à la base de données)
            return resultat.get(0);
        }

    }

    public static boolean trouverClient(String email) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        String request = "Select c from Client c where c.email = :email";
        Query q = em.createQuery(request);
        q.setParameter("email", email);
        List<Client> resultat = q.getResultList();
        //Si on trouve un client possedant l'email en argument
        //alors on renvoit la valeur True
        return !resultat.isEmpty();
    }

    public static Client trouverClient(long id) {
        EntityManager em = JpaUtil.obtenirEntityManager();
        return em.find(Client.class, id);
    }
}
