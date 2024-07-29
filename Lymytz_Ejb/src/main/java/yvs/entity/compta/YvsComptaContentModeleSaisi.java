/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.compta;

import java.io.Serializable;
import java.util.Date;
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
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_content_modele_saisi")
@NamedQueries({
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findAll", query = "SELECT y FROM YvsComptaContentModeleSaisi y"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findById", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByJour", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.jour = :jour"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByModeSaisieJour", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.modeSaisieJour = :modeSaisieJour"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByNumPiece", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.numPiece = :numPiece"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsNumPiece", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsNumPiece = :mdsNumPiece"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByNumRef", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.numRef = :numRef"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsNumRef", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsNumRef = :mdsNumRef"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByCompteGeneral", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.compteGeneral = :compteGeneral"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsCompteGeneral", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsCompteGeneral = :mdsCompteGeneral"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByCompteTiers", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.compteTiers = :compteTiers"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsCompteTiers", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsCompteTiers = :mdsCompteTiers"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByLibelle", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsLibelle", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsLibelle = :mdsLibelle"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByDebit", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.debit = :debit"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsDebit", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsDebit = :mdsDebit"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByCredit", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.credit = :credit"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsCredit", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsCredit = :mdsCredit"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByEcheance", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.echeance = :echeance"),
    @NamedQuery(name = "YvsComptaContentModeleSaisi.findByMdsEcheance", query = "SELECT y FROM YvsComptaContentModeleSaisi y WHERE y.mdsEcheance = :mdsEcheance")})
public class YvsComptaContentModeleSaisi implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_content_modele_saisi_id_seq", name = "yvs_compta_content_modele_saisi_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_content_modele_saisi_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "jour")
    private Integer jour;
    @Column(name = "mode_saisie_jour")
    private Integer modeSaisieJour;
    @Size(max = 2147483647)
    @Column(name = "num_piece")
    private String numPiece;
    @Column(name = "mds_num_piece")
    private Integer mdsNumPiece;
    @Size(max = 2147483647)
    @Column(name = "num_ref")
    private String numRef;
    @Column(name = "mds_num_ref")
    private Integer mdsNumRef;
    @Size(max = 2147483647)
    @Column(name = "compte_general")
    private String compteGeneral;
    @Column(name = "mds_compte_general")
    private Integer mdsCompteGeneral;
    @Size(max = 2147483647)
    @Column(name = "compte_tiers")
    private String compteTiers;
    @Column(name = "mds_compte_tiers")
    private Integer mdsCompteTiers;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "mds_libelle")
    private Integer mdsLibelle;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "debit")
    private Double debit;
    @Column(name = "mds_debit")
    private Integer mdsDebit;
    @Column(name = "credit")
    private Double credit;
    @Column(name = "mds_credit")
    private Integer mdsCredit;
    @Column(name = "echeance")
    @Temporal(TemporalType.DATE)
    private Date echeance;
    @Column(name = "mds_echeance")
    private Integer mdsEcheance;
    @JoinColumn(name = "modele_saisie", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaModeleSaisie modeleSaisie;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
//    @Column(name = "ordre")
    private int ordre;

    public YvsComptaContentModeleSaisi() {
    }

    public YvsComptaContentModeleSaisi(Long id) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getJour() {
        return jour != null ? jour : 0;
    }

    public void setJour(Integer jour) {
        this.jour = jour;
    }

    public Integer getModeSaisieJour() {
        return modeSaisieJour != null ? modeSaisieJour : 0;
    }

    public void setModeSaisieJour(Integer modeSaisieJour) {
        this.modeSaisieJour = modeSaisieJour;
    }

    public String getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(String numPiece) {
        this.numPiece = numPiece;
    }

    public Integer getMdsNumPiece() {
        return mdsNumPiece != null ? mdsNumPiece : 0;
    }

    public void setMdsNumPiece(Integer mdsNumPiece) {
        this.mdsNumPiece = mdsNumPiece;
    }

    public String getNumRef() {
        return numRef;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public Integer getMdsNumRef() {
        return mdsNumRef;
    }

    public void setMdsNumRef(Integer mdsNumRef) {
        this.mdsNumRef = mdsNumRef;
    }

    public String getCompteGeneral() {
        return compteGeneral;
    }

    public void setCompteGeneral(String compteGeneral) {
        this.compteGeneral = compteGeneral;
    }

    public Integer getMdsCompteGeneral() {
        return mdsCompteGeneral;
    }

    public void setMdsCompteGeneral(Integer mdsCompteGeneral) {
        this.mdsCompteGeneral = mdsCompteGeneral;
    }

    public String getCompteTiers() {
        return compteTiers;
    }

    public void setCompteTiers(String compteTiers) {
        this.compteTiers = compteTiers;
    }

    public Integer getMdsCompteTiers() {
        return mdsCompteTiers;
    }

    public void setMdsCompteTiers(Integer mdsCompteTiers) {
        this.mdsCompteTiers = mdsCompteTiers;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Integer getMdsLibelle() {
        return mdsLibelle;
    }

    public void setMdsLibelle(Integer mdsLibelle) {
        this.mdsLibelle = mdsLibelle;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Integer getMdsDebit() {
        return mdsDebit;
    }

    public void setMdsDebit(Integer mdsDebit) {
        this.mdsDebit = mdsDebit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Integer getMdsCredit() {
        return mdsCredit;
    }

    public void setMdsCredit(Integer mdsCredit) {
        this.mdsCredit = mdsCredit;
    }

    public Date getEcheance() {
        return echeance;
    }

    public void setEcheance(Date echeance) {
        this.echeance = echeance;
    }

    public Integer getMdsEcheance() {
        return mdsEcheance;
    }

    public void setMdsEcheance(Integer mdsEcheance) {
        this.mdsEcheance = mdsEcheance;
    }

    public YvsComptaModeleSaisie getModeleSaisie() {
        return modeleSaisie;
    }

    public void setModeleSaisie(YvsComptaModeleSaisie modeleSaisie) {
        this.modeleSaisie = modeleSaisie;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
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
        if (!(object instanceof YvsComptaContentModeleSaisi)) {
            return false;
        }
        YvsComptaContentModeleSaisi other = (YvsComptaContentModeleSaisi) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaContentModeleSaisi[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        YvsComptaContentModeleSaisi c = (YvsComptaContentModeleSaisi) o;
        return c.getId().compareTo(id);
    }

}
