/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.domaine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "COMPTE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compte.findAll", query = "SELECT c FROM Compte c")
    , @NamedQuery(name = "Compte.findByIdcompte", query = "SELECT c FROM Compte c WHERE c.idcompte = :idcompte")
    , @NamedQuery(name = "Compte.findBySolde", query = "SELECT c FROM Compte c WHERE c.solde = :solde")
    , @NamedQuery(name = "Compte.findByTypec", query = "SELECT c FROM Compte c WHERE c.typec = :typec")
    , @NamedQuery(name = "Compte.findByInteret", query = "SELECT c FROM Compte c WHERE c.interet = :interet")
    , @NamedQuery(name = "Compte.findByDecouvert", query = "SELECT c FROM Compte c WHERE c.decouvert = :decouvert")})
public class Compte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDCOMPTE")
    private Long idcompte;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "SOLDE")
    private BigDecimal solde;
    @Basic(optional = false)
    @Column(name = "TYPEC")
    private String typec;
    @Basic(optional = false)
    @Column(name = "INTERET")
    private BigDecimal interet;
    @Basic(optional = false)
    @Column(name = "DECOUVERT")
    private BigDecimal decouvert;
    @JoinColumn(name = "IDCLIENT", referencedColumnName = "IDCLIENT")
    @ManyToOne
    private Client idclient;
    @OneToMany(mappedBy = "idcomptedebit")
    private List<Virement> virementList;
    @OneToMany(mappedBy = "idcomptecredit")
    private List<Virement> virementList1;

    public Compte() {
    }

    public Compte(Long idcompte) {
        this.idcompte = idcompte;
    }

    public Compte(Long idcompte, BigDecimal solde, String typec, BigDecimal interet, BigDecimal decouvert) {
        this.idcompte = idcompte;
        this.solde = solde;
        this.typec = typec;
        this.interet = interet;
        this.decouvert = decouvert;
    }

    public Long getIdcompte() {
        return idcompte;
    }

    public void setIdcompte(Long idcompte) {
        this.idcompte = idcompte;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getTypec() {
        return typec;
    }

    public void setTypec(String typec) {
        this.typec = typec;
    }

    public BigDecimal getInteret() {
        return interet;
    }

    public void setInteret(BigDecimal interet) {
        this.interet = interet;
    }

    public BigDecimal getDecouvert() {
        return decouvert;
    }

    public void setDecouvert(BigDecimal decouvert) {
        this.decouvert = decouvert;
    }

    public Client getIdclient() {
        return idclient;
    }

    public void setIdclient(Client idclient) {
        this.idclient = idclient;
    }

    @XmlTransient
    public List<Virement> getVirementList() {
        return virementList;
    }

    public void setVirementList(List<Virement> virementList) {
        this.virementList = virementList;
    }

    @XmlTransient
    public List<Virement> getVirementList1() {
        return virementList1;
    }

    public void setVirementList1(List<Virement> virementList1) {
        this.virementList1 = virementList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcompte != null ? idcompte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compte)) {
            return false;
        }
        Compte other = (Compte) object;
        if ((this.idcompte == null && other.idcompte != null) || (this.idcompte != null && !this.idcompte.equals(other.idcompte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.gtm.domaine.Compte[ idcompte=" + idcompte + " ]";
    }
    
}
