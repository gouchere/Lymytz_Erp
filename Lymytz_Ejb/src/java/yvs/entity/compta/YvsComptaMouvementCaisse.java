
package yvs.entity.compta;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import yvs.dao.salaire.service.Constantes;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.base.YvsBaseModeReglement;
import yvs.entity.grh.personnel.YvsGrhEmployes;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsSocietes;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.users.YvsUsers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_compta_mouvement_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsComptaMouvementCaisse.findAll", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findAllC", query = "SELECT COUNT(y) FROM YvsComptaMouvementCaisse y WHERE y.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findIdExterneById", query = "SELECT y.idExterne FROM YvsComptaMouvementCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findById", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByIds", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findAPayer", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.statutPiece=:statut AND y.societe=:societe ORDER BY y.datePaimentPrevu DESC"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByExterne", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.idExterne =:idExterne AND y.tableExterne=:table"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByIdsExterne", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.idExterne IN :idExterne AND y.tableExterne=:table"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findPieceLie", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.idExterne IN :listId AND y.tableExterne=:table AND y.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findPieceLieNoId", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.idExterne = :externe AND y.id != :id AND y.tableExterne=:table"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByNumero", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.numero = :numero"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByNumeroPiece", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.agence.societe=:societe AND y.numero LIKE :numero ORDER BY y.numero DESC"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByIdExterne", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.idExterne = :idExterne"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByNote", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.note = :note"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findPieceLies", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.tableExterne=:table AND y.idExterne IN :listIds ORDER BY y.datePaimentPrevu DESC"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByYvsTableExterne", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.tableExterne = :tableExterne ORDER BY y.datePaimentPrevu DESC"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByTableExterne", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.tableExterne = :tableExterne AND y.societe=:societe ORDER BY y.datePaimentPrevu DESC"),
    @NamedQuery(name = "YvsComptaMouvementCaisse.findByTableStatut", query = "SELECT y FROM YvsComptaMouvementCaisse y WHERE y.tableExterne = :tableExterne AND y.statutPiece=:statut AND y.agence.societe=:societe ORDER BY y.datePaimentPrevu DESC"),
    
    @NamedQuery(name = "YvsComptaMouvementCaisse.findFirstDate", query = "SELECT y.dateMvt FROM YvsComptaMouvementCaisse y WHERE y.agence.societe=:societe ORDER BY y.dateMvt ASC"),

    @NamedQuery(name = "YvsComptaMouvementCaisse.findSumByCaisseDate", query = "SELECT SUM(y.montant) FROM YvsComptaMouvementCaisse y WHERE y.datePaye <= :date AND y.caisse = :caisse AND y.statutPiece = 'P' AND COALESCE(y.mouvement, 'R') = :mouvement")})
public class YvsComptaMouvementCaisse extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_mouvement_caisse_id_seq", name = "yvs_compta_mouvement_caisse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_mouvement_caisse_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "numero")
    private String numero;
    @Column(name = "id_externe")
    private Long idExterne;
    @Size(max = 2147483647)
    @Column(name = "note")
    private String note;
    @Size(max = 15)
    @Column(name = "mouvement")
    private String mouvement;
    @Size(max = 2147483647)
    @Column(name = "table_externe")
    private String tableExterne;
    @Column(name = "reference_externe")
    private String referenceExterne;
    @Column(name = "numero_externe")
    private String numeroExterne;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "statut_piece")
    private Character statutPiece;
    @Column(name = "date_mvt")
    @Temporal(TemporalType.DATE)
    private Date dateMvt;
    @Column(name = "date_paiment_prevu")
    @Temporal(TemporalType.DATE)
    private Date datePaimentPrevu;
    @Column(name = "date_paye")
    @Temporal(TemporalType.DATE)
    private Date datePaye;
    @JoinColumn(name = "caissier", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers caissier;
    @JoinColumn(name = "caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCaisse caisse;
    @JoinColumn(name = "tiers_interne", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsers tiersInterne;
    @JoinColumn(name = "tiers_externe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseTiers tiersExterne;
    @JoinColumn(name = "tiers_employe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsGrhEmployes tiersEmploye;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @JoinColumn(name = "model", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseModeReglement model;
    @Column(name = "name_tiers")
    private String nameTiers;
    @Column(name = "etape_valide")
    private Integer etapeValide;
    @Column(name = "etape_total")
    private Integer etapeTotal;

    @Transient
    private Object soureceMvt;
    @Transient
    private Double montantTotal;
    @Transient
    private Double montantAvance;
    @Transient
    private boolean onComment;
    @Transient
    private boolean comptabilise;
    @Transient
    private boolean select;

    public YvsComptaMouvementCaisse() {
    }

    public YvsComptaMouvementCaisse(Long id) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isComptabilise() {
        return comptabilise;
    }

    public void setComptabilise(boolean comptabilise) {
        this.comptabilise = comptabilise;
    }

    public boolean isOnComment() {
        onComment = false;
        if (note != null ? note.trim().length() > 0 : false) {
            onComment = true;
        }
        return onComment;
    }

    public void setOnComment(boolean onComment) {
        this.onComment = onComment;
    }

    public YvsBaseTiers getTiersExterne() {
        return tiersExterne;
    }

    public void setTiersExterne(YvsBaseTiers tiersExterne) {
        this.tiersExterne = tiersExterne;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Long getIdExterne() {
        return idExterne != null ? idExterne : 0L;
    }

    public void setIdExterne(Long idExterne) {
        this.idExterne = idExterne;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTableExterne() {
        return tableExterne != null ? tableExterne.trim().length() > 0 ? tableExterne : Constantes.SCR_VENTE : Constantes.SCR_VENTE;
    }

    public void setTableExterne(String tableExterne) {
        this.tableExterne = tableExterne;
    }

    public Double getMontant() {
        return montant != null ? montant : 0;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Object getSoureceMvt() {
        return soureceMvt;
    }

    public void setSoureceMvt(Object soureceMvt) {
        this.soureceMvt = soureceMvt;
    }

    public String getReferenceExterne() {
        return referenceExterne != null ? referenceExterne : "";
    }

    public void setReferenceExterne(String referenceExterne) {
        this.referenceExterne = referenceExterne;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsAgences getAgence() {
        return agence;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Character getStatutPiece() {
        return statutPiece != null ? statutPiece : ' ';
    }

    public void setStatutPiece(Character statutPiece) {
        this.statutPiece = statutPiece;
    }

    public Double getMontantAvance() {
        return montantAvance;
    }

    public void setMontantAvance(Double montantAvance) {
        this.montantAvance = montantAvance;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public YvsBaseCaisse getCaisse() {
        return caisse;
    }

    public void setCaisse(YvsBaseCaisse caisse) {
        this.caisse = caisse;
    }

    public String getMouvement() {
        return mouvement != null ? mouvement : "";
    }

    public void setMouvement(String mouvement) {
        this.mouvement = mouvement;
    }

    public YvsGrhEmployes getTiersEmploye() {
        return tiersEmploye;
    }

    public void setTiersEmploye(YvsGrhEmployes tiersEmploye) {
        this.tiersEmploye = tiersEmploye;
    }

    public String getNameTiers() {
        return nameTiers;
    }

    public void setNameTiers(String nameTiers) {
        this.nameTiers = nameTiers;
    }

    public YvsBaseModeReglement getModel() {
        return model;
    }

    public void setModel(YvsBaseModeReglement model) {
        this.model = model;
    }

    public Integer getEtapeTotal() {
        return etapeTotal != null ? etapeTotal : 0;
    }

    public void setEtapeTotal(Integer etapeTotal) {
        this.etapeTotal = etapeTotal;
    }

    public Integer getEtapeValide() {
        return etapeValide != null ? etapeValide : 0;
    }

    public void setEtapeValide(Integer etapeValide) {
        this.etapeValide = etapeValide;
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
        if (!(object instanceof YvsComptaMouvementCaisse)) {
            return false;
        }
        YvsComptaMouvementCaisse other = (YvsComptaMouvementCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaMouvementCaisse[ id=" + id + " ]";
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateMvt() {
        return dateMvt;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateMvt(Date dateMvt) {
        this.dateMvt = dateMvt;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePaimentPrevu() {
        return datePaimentPrevu;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaimentPrevu(Date datePaimentPrevu) {
        this.datePaimentPrevu = datePaimentPrevu;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDatePaye() {
        return datePaye;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDatePaye(Date datePaye) {
        this.datePaye = datePaye;
    }

    public YvsUsers getCaissier() {
        return caissier;
    }

    public void setCaissier(YvsUsers caissier) {
        this.caissier = caissier;
    }

    public YvsUsers getTiersInterne() {
        return tiersInterne;
    }

    public void setTiersInterne(YvsUsers tiersInterne) {
        this.tiersInterne = tiersInterne;
    }

    public String getNumeroExterne() {
        return numeroExterne != null ? numeroExterne : "";
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

}
