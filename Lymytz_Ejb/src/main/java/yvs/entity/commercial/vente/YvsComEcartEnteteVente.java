/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.vente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.vente.YvsComptaCaissePieceEcartVente;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_ecart_entete_vente", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsComEcartEnteteVente.findAll", query = "SELECT y FROM YvsComEcartEnteteVente y"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findById", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findByIds", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findLikeNumero", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.numero like :numero AND y.users.agence.societe = :societe ORDER BY y.numero DESC"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findByDateUpdate", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findByAuthor", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.author = :author"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findByUsers", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.users = :users"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findByNoNumero", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.users.agence.societe = :societe AND COALESCE(y.numero, '') = ''"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findManquantByUsersNotFinish", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.users = :users AND y.statut != 'P' AND y.montant > 0.0"),
    @NamedQuery(name = "YvsComEcartEnteteVente.findExcedentByUsersNotFinish", query = "SELECT y FROM YvsComEcartEnteteVente y WHERE y.users = :users AND y.statut != 'P' AND y.montant < 0.0"),

    @NamedQuery(name = "YvsComEcartEnteteVente.findSumByUsers", query = "SELECT SUM(y.montant) FROM YvsComEcartEnteteVente y WHERE y.users = :users"),

    @NamedQuery(name = "YvsComEcartEnteteVente.counByUsersAfter", query = "SELECT COUNT(y.id) FROM YvsComEcartEnteteVente y WHERE y.users = :users AND y.dateEcart >= :date"),
    @NamedQuery(name = "YvsComEcartEnteteVente.counByUsersStatutAfter", query = "SELECT COUNT(y.id) FROM YvsComEcartEnteteVente y WHERE y.users = :users AND y.statut = :statut AND y.dateEcart >= :date"),

    @NamedQuery(name = "YvsComEcartEnteteVente.findLastDateByUsers", query = "SELECT y.dateEcart FROM YvsComEcartEnteteVente y WHERE y.users = :users ORDER BY y.dateEcart DESC")
})
public class YvsComEcartEnteteVente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_ecart_entete_vente_id_seq", name = "yvs_com_ecart_entete_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_ecart_entete_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "date_ecart")
    @Temporal(TemporalType.DATE)
    private Date dateEcart;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "statut")
    private Character statut;
    @Column(name = "statut_regle")
    private Character statutRegle;
    @Column(name = "numero")
    private String numero;
    @JoinColumn(name = "users", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers users;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToOne(mappedBy = "piece")
    private YvsComptaCaissePieceEcartVente caisse;

    @OneToMany(mappedBy = "piece")
    private List<YvsComReglementEcartVente> reglements;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean inclure = true;
    @Transient
    public static long ids = -1;

    public YvsComEcartEnteteVente() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
        reglements = new ArrayList<>();
    }

    public YvsComEcartEnteteVente(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateEcart() {
        return dateEcart != null ? dateEcart : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateEcart(Date dateEcart) {
        this.dateEcart = dateEcart;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : getDateEcart();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Character getStatut() {
        return statut != null ? statut : 'E';
    }

    public void setStatut(Character statut) {
        this.statut = statut;
    }

    public Character getStatutRegle() {
        return statutRegle != null ? statutRegle : 'W';
    }

    public void setStatutRegle(Character statutRegle) {
        this.statutRegle = statutRegle;
    }

    @XmlTransient
    @JsonIgnore
    public YvsUsers getUsers() {
        return users;
    }

    public void setUsers(YvsUsers users) {
        this.users = users;
    }

    public boolean isInclure() {
        return inclure;
    }

    public void setInclure(boolean inclure) {
        this.inclure = inclure;
    }

    @XmlTransient
    @JsonIgnore
    public YvsComptaCaissePieceEcartVente getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsComptaCaissePieceEcartVente caisse) {
        this.caisse = caisse;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComReglementEcartVente> getReglements() {
        return reglements;
    }

    public void setReglements(List<YvsComReglementEcartVente> reglements) {
        this.reglements = reglements;
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
        if (!(object instanceof YvsComEcartEnteteVente)) {
            return false;
        }
        YvsComEcartEnteteVente other = (YvsComEcartEnteteVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.vente.YvsComEcartEnteteVente[ id=" + id + " ]";
    }

}
