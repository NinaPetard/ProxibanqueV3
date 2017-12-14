/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.domaine;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nina
 */
@Entity
@Table(name = "CONSEILLER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conseiller.findAll", query = "SELECT c FROM Conseiller c")
    , @NamedQuery(name = "Conseiller.findByIdconseiller", query = "SELECT c FROM Conseiller c WHERE c.idconseiller = :idconseiller")
    , @NamedQuery(name = "Conseiller.findByUserid", query = "SELECT c FROM Conseiller c WHERE c.userid = :userid")
    , @NamedQuery(name = "Conseiller.findByNom", query = "SELECT c FROM Conseiller c WHERE c.nom = :nom")
    , @NamedQuery(name = "Conseiller.findByPrenom", query = "SELECT c FROM Conseiller c WHERE c.prenom = :prenom")
    , @NamedQuery(name = "Conseiller.findByPassword", query = "SELECT c FROM Conseiller c WHERE c.password = :password")})
public class Conseiller implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDCONSEILLER")
    private Long idconseiller;
    @Column(name = "USERID")
    private String userid;
    @Basic(optional = false)
    @Column(name = "NOM")
    private String nom;
    @Basic(optional = false)
    @Column(name = "PRENOM")
    private String prenom;
    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;
    @OneToMany(mappedBy = "idconseiller")
    private List<Client> clientList;

    public Conseiller() {
    }

    public Conseiller(Long idconseiller) {
        this.idconseiller = idconseiller;
    }

    public Conseiller(Long idconseiller, String nom, String prenom, String password) {
        this.idconseiller = idconseiller;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
    }

    public Long getIdconseiller() {
        return idconseiller;
    }

    public void setIdconseiller(Long idconseiller) {
        this.idconseiller = idconseiller;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idconseiller != null ? idconseiller.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conseiller)) {
            return false;
        }
        Conseiller other = (Conseiller) object;
        if ((this.idconseiller == null && other.idconseiller != null) || (this.idconseiller != null && !this.idconseiller.equals(other.idconseiller))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.gtm.domaine.Conseiller[ idconseiller=" + idconseiller + " ]";
    }
    
}
