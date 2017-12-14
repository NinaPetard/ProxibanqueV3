/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.gtm.domaine;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nina
 */
@Entity
@Table(name = "VIREMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Virement.findAll", query = "SELECT v FROM Virement v")
    , @NamedQuery(name = "Virement.findByIdvirement", query = "SELECT v FROM Virement v WHERE v.idvirement = :idvirement")
    , @NamedQuery(name = "Virement.findByMontant", query = "SELECT v FROM Virement v WHERE v.montant = :montant")
    , @NamedQuery(name = "Virement.findByDatev", query = "SELECT v FROM Virement v WHERE v.datev = :datev")})
public class Virement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDVIREMENT")
    private Long idvirement;
    @Column(name = "MONTANT")
    private BigInteger montant;
    @Column(name = "DATEV")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datev;
    @JoinColumn(name = "IDCOMPTEDEBIT", referencedColumnName = "IDCOMPTE")
    @ManyToOne
    private Compte idcomptedebit;
    @JoinColumn(name = "IDCOMPTECREDIT", referencedColumnName = "IDCOMPTE")
    @ManyToOne
    private Compte idcomptecredit;

    public Virement() {
    }

    public Virement(Long idvirement) {
        this.idvirement = idvirement;
    }

    public Long getIdvirement() {
        return idvirement;
    }

    public void setIdvirement(Long idvirement) {
        this.idvirement = idvirement;
    }

    public BigInteger getMontant() {
        return montant;
    }

    public void setMontant(BigInteger montant) {
        this.montant = montant;
    }

    public Date getDatev() {
        return datev;
    }

    public void setDatev(Date datev) {
        this.datev = datev;
    }

    public Compte getIdcomptedebit() {
        return idcomptedebit;
    }

    public void setIdcomptedebit(Compte idcomptedebit) {
        this.idcomptedebit = idcomptedebit;
    }

    public Compte getIdcomptecredit() {
        return idcomptecredit;
    }

    public void setIdcomptecredit(Compte idcomptecredit) {
        this.idcomptecredit = idcomptecredit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idvirement != null ? idvirement.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Virement)) {
            return false;
        }
        Virement other = (Virement) object;
        if ((this.idvirement == null && other.idvirement != null) || (this.idvirement != null && !this.idvirement.equals(other.idvirement))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fr.gtm.domaine.Virement[ idvirement=" + idvirement + " ]";
    }
    
}
