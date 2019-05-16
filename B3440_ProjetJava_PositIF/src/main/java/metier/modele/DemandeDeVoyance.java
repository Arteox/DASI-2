/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metier.modele;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jcharnay1
 */
@Entity
public class DemandeDeVoyance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date_demande;
    @Temporal(TemporalType.TIMESTAMP)
    protected Date date_fin;
    protected Boolean accepte;
    protected String commentaire;
    protected Integer etape;
    //etape = 1 --> La demande vient d'être crée
    //      = 2 --> L'employé vient de débuter la séance de voyance
    //      = 3 --> L'employé a terminé la séance de voyance
    //      = 4 --> L'employé a fait son compte rendu sur la séance de voyance
    @ManyToOne
    Employe employeAffecte;
    @ManyToOne
    Client client;
    @ManyToOne
    Medium medium;

    public DemandeDeVoyance(Date date_demande, Integer etape, Client client, Medium medium) {
        this.date_demande = date_demande;
        this.etape = etape;
        this.client = client;
        this.medium = medium;
        this.commentaire = "";
    }

    public DemandeDeVoyance() {
        this.commentaire = "";
    }

    
    
    public Long getId() {
        return id;
    }

    public Date getDate_demande() {
        return date_demande;
    }

    public void setDate_demande(Date date_demande) {
        this.date_demande = date_demande;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    public Boolean getAccepte() {
        return accepte;
    }

    public void setAccepte(Boolean accepte) {
        this.accepte = accepte;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String description) {
        this.commentaire = description;
    }

    public Integer getEtape() {
        return etape;
    }

    public void setEtape(Integer etape) {
        this.etape = etape;
    }

    public Employe getEmployeAffecte() {
        return employeAffecte;
    }

    public void setEmployeAffecte(Employe employeAffecte) {
        this.employeAffecte = employeAffecte;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Medium getMedium() {
        return medium;
    }

    public void setMedium(Medium medium) {
        this.medium = medium;
    }

    @Override
    public String toString() {
        return "DemandeDeVoyance{" + "id=" + id + ", date_demande=" + date_demande + ", date_fin=" + date_fin + ", accepte=" + accepte + ", commentaire=" + commentaire + ", etape=" + etape + ", employeAffecte=" + employeAffecte + ", client=" + client + ", medium=" + medium + '}';
    }
    
     
}
