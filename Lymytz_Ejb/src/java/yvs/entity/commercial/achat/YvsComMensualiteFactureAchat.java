/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.achat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_com_mensualite_facture_achat")
@NamedQueries({
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findAll", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.facture.depotReception.agence.societe = :societe"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByFacture", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByFactureS", query = "SELECT SUM(y.montant) FROM YvsComMensualiteFactureAchat y WHERE y.facture = :facture"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findById", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByDateReglement", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.dateMensualite = :dateReglement"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByMontant", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.montant = :montant"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByEtat", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.etat = :etat"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByClientDatesNotEtat", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.facture.fournisseur = :fournisseur AND y.etat != :etat AND y.dateMensualite BETWEEN :dateDebut AND :dateFin ORDER BY y.dateMensualite DESC"),
    @NamedQuery(name = "YvsComMensualiteFactureAchat.findByClientDatesNotEtatAgence", query = "SELECT y FROM YvsComMensualiteFactureAchat y WHERE y.facture.agence = :agence AND y.facture.fournisseur = :fournisseur AND y.etat != :etat AND y.dateMensualite BETWEEN :dateDebut AND :dateFin ORDER BY y.dateMensualite DESC")})
public class YvsComMensualiteFactureAchat extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_mensualite_facture_achat_id_seq", name = "yvs_com_mensualite_facture_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_mensualite_facture_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    private double montant;
    @Size(max = 2147483647)
    @Column(name = "etat")
    private String etat;
    @JsonManagedReference
    @JoinColumn(name = "facture", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocAchats facture;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @Transient
    private long idDistant;
    @Transient
    private boolean selectActif;
    @Transient
    private boolean update;
    @Transient
    private boolean new_;
    @Transient
    private boolean outDelai;
    @Transient
    private double avance;
    @Transient
    private double montantRest;
    @Transient
    private String nameMens;

    public YvsComMensualiteFactureAchat() {
    }

    public YvsComMensualiteFactureAchat(Long id) {
        this.id = id;
    }

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getNameMens() {
        nameMens = getDateToString(getDateMensualite()) + " : " + getDouble(getMontant());
        return nameMens;
    }

    public void setNameMens(String nameMens) {
        this.nameMens = nameMens;
    }

    public double getMontantRest() {
        return montantRest;
    }

    public void setMontantRest(double montantRest) {
        this.montantRest = montantRest;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isOutDelai() {
        outDelai = dateToCalendar(getDateMensualite()).before(dateToCalendar(new Date()));
        return outDelai;
    }

    public void setOutDelai(boolean outDelai) {
        this.outDelai = outDelai;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateMensualite() {
        return dateMensualite != null ? dateMensualite : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateMensualite(Date dateMensualite) {
        this.dateMensualite = dateMensualite;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public YvsComDocAchats getFacture() {
        return facture;
    }

    public void setFacture(YvsComDocAchats facture) {
        this.facture = facture;
    }

    public double getAvance() {
        return avance;
    }

    public void setAvance(double avance) {
        this.avance = avance;
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
        if (!(object instanceof YvsComMensualiteFactureAchat)) {
            return false;
        }
        YvsComMensualiteFactureAchat other = (YvsComMensualiteFactureAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.achat.YvsComMensualiteFactureAchat[ id=" + id + " ]";
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
