/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta.divers;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity; import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_compta_caisse_mensualite_doc_divers")
@NamedQueries({
    @NamedQuery(name = "YvsComptaCaisseMensualiteDocDivers.findAll", query = "SELECT y FROM YvsComptaCaisseMensualiteDocDivers y WHERE y.docDivers.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaCaisseMensualiteDocDivers.findById", query = "SELECT y FROM YvsComptaCaisseMensualiteDocDivers y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaCaisseMensualiteDocDivers.findByDateReglement", query = "SELECT y FROM YvsComptaCaisseMensualiteDocDivers y WHERE y.dateMensualite = :dateReglement"),
    @NamedQuery(name = "YvsComptaCaisseMensualiteDocDivers.findByMontant", query = "SELECT y FROM YvsComptaCaisseMensualiteDocDivers y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComptaCaisseMensualiteDocDivers.findByEtat", query = "SELECT y FROM YvsComptaCaisseMensualiteDocDivers y WHERE y.etat = :etat"),

    @NamedQuery(name = "YvsComptaCaisseMensualiteDocDivers.findByDocDivers", query = "SELECT y FROM YvsComptaCaisseMensualiteDocDivers y WHERE y.docDivers = :docDivers")})
public class YvsComptaCaisseMensualiteDocDivers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_caisse_mensualite_doc_divers_id_seq", name = "yvs_compta_caisse_mensualite_doc_divers_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_caisse_mensualite_doc_divers_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_reglement")
    @Temporal(TemporalType.DATE)
    private Date dateMensualite;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @Column(name = "mode_paiement")
    private String modePaiement;
    @JoinColumn(name = "doc_divers", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docDivers;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsComptaCaisseMensualiteDocDivers() {
    }

    public YvsComptaCaisseMensualiteDocDivers(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateMensualite() {
        return dateMensualite != null ? dateMensualite : new Date();
    }

    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public YvsComptaCaisseDocDivers getDocDivers() {
        return docDivers;
    }

    public void setDocDivers(YvsComptaCaisseDocDivers docDivers) {
        this.docDivers = docDivers;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsComptaCaisseMensualiteDocDivers)) {
            return false;
        }
        YvsComptaCaisseMensualiteDocDivers other = (YvsComptaCaisseMensualiteDocDivers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseMensualiteDocDivers[ id=" + id + " ]";
    }

    String getDateToString(Date date) {
        SimpleDateFormat formater = new SimpleDateFormat("dd MMMM yyyy", Locale.FRANCE);
        return formater.format(date);
    }

    Calendar dateToCalendar(Date date) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            return cal;
        }
        return Calendar.getInstance();
    }

    String getDouble(double montant) {
        double value = Double.parseDouble("" + montant);
        DecimalFormat df = new DecimalFormat("#,###,##0.##");
        if (value == 0) {
            return "0";
        } else {
            return df.format(value);
        }
    }

}
