/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.production.pilotage.YvsProdComposantOp;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300 cette classe modélise les phases d'une phase et les
 * opération
 */
@Entity
@Table(name = "yvs_prod_operations_gamme")
@NamedQueries({
    @NamedQuery(name = "YvsProdOperationsGamme.findAll", query = "SELECT y FROM YvsProdOperationsGamme y WHERE y.gammeArticle.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsProdOperationsGamme.findById", query = "SELECT y FROM YvsProdOperationsGamme y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByReference", query = "SELECT y FROM YvsProdOperationsGamme y WHERE y.codeRef = :reference AND y.gammeArticle.article.famille.societe = :societe"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByDescription", query = "SELECT y FROM YvsProdOperationsGamme y WHERE y.description = :description"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByGamme", query = "SELECT y FROM YvsProdOperationsGamme y JOIN FETCH y.gammeArticle WHERE y.gammeArticle = :gamme ORDER BY y.numero"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByGammeArt", query = "SELECT y FROM YvsProdOperationsGamme y JOIN FETCH y.gammeArticle JOIN FETCH y.composants "
            + "WHERE y.gammeArticle = :gamme ORDER BY y.numero ASC"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByArticleP", query = "SELECT y FROM YvsProdOperationsGamme y JOIN FETCH y.gammeArticle.article "
            + "WHERE y.gammeArticle.article = :article AND y.gammeArticle.principal=true AND y.gammeArticle.masquer=false AND y.gammeArticle.actif=true ORDER BY y.numero ASC"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByArticle", query = "SELECT y FROM YvsProdOperationsGamme y JOIN FETCH y.gammeArticle.article "
            + "WHERE y.gammeArticle.article = :article AND y.gammeArticle.masquer=false AND y.gammeArticle.actif=true ORDER BY y.numero ASC"),
    @NamedQuery(name = "YvsProdOperationsGamme.findByNumero", query = "SELECT y FROM YvsProdOperationsGamme y WHERE y.numero = :numero")
})
public class YvsProdOperationsGamme implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_phase_gamme_id_seq", name = "yvs_prod_phase_gamme_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_phase_gamme_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Column(name = "numero")
    private Integer numero;
    @Column(name = "temps_reglage")
    private Double tempsReglage;
    @Column(name = "temps_operation")
    private Double tempsOperation;
    @Column(name = "type_temps")
    private String typeTemps;
    @Column(name = "taux_efficience")
    private Double tauxEfficience;
    @Column(name = "taux_perte")
    private Double tauxPerte;
    @Column(name = "quantite_base")
    private Double quantiteBase;
    @Column(name = "cadence")
    private Double cadence;
    @Column(name = "quantite_min")
    private Double quantiteMin;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "type_cout")
    private Character typeCout = 'P';

    @JoinColumn(name = "gamme_article", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdGammeArticle gammeArticle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "phaseGamme", fetch = FetchType.LAZY)
    private List<YvsProdDocumentTechnique> documents;
    @OneToMany(mappedBy = "operations",fetch = FetchType.LAZY)
    private List<YvsProdPosteOperation> postes;
    @OneToMany(mappedBy = "operation",fetch = FetchType.LAZY)
    private List<YvsProdComposantOp> composants;

    @Transient
    private boolean new_;
    @Transient
    private boolean select;

    public YvsProdOperationsGamme() {
        documents = new ArrayList<>();
        postes = new ArrayList<>();
        composants = new ArrayList<>();
    }

    public YvsProdOperationsGamme(Integer id) {
        this();
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

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Integer getId() {
        return id != null ? id : 0;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeRef() {
        return codeRef;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumero() {
        return numero != null ? numero : 0;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public List<YvsProdDocumentTechnique> getDocuments() {
        return documents;
    }

    public void setDocuments(List<YvsProdDocumentTechnique> documents) {
        this.documents = documents;
    }

    public List<YvsProdPosteOperation> getPostes() {
        return postes;
    }

    public void setPostes(List<YvsProdPosteOperation> postes) {
        this.postes = postes;
    }

    public YvsProdGammeArticle getGammeArticle() {
        return gammeArticle;
    }

    public void setGammeArticle(YvsProdGammeArticle gammeArticle) {
        this.gammeArticle = gammeArticle;
    }

    public Double getTempsReglage() {
        return tempsReglage != null ? tempsReglage : 0;
    }

    public void setTempsReglage(Double tempsReglage) {
        this.tempsReglage = tempsReglage;
    }

    public Double getTempsOperation() {
        return tempsOperation != null ? tempsOperation : 0;
    }

    public void setTempsOperation(Double tempsOperation) {
        this.tempsOperation = tempsOperation;
    }

    public String getTypeTemps() {
        return typeTemps;
    }

    public void setTypeTemps(String typeTemps) {
        this.typeTemps = typeTemps;
    }

    public Double getTauxEfficience() {
        return tauxEfficience != null ? tauxEfficience : 0;
    }

    public void setTauxEfficience(Double tauxEfficience) {
        this.tauxEfficience = tauxEfficience;
    }

    public Double getTauxPerte() {
        return tauxPerte != null ? tauxPerte : 0;
    }

    public void setTauxPerte(Double tauxPerte) {
        this.tauxPerte = tauxPerte;
    }

    public Double getQuantiteBase() {
        return quantiteBase != null ? quantiteBase : 0;
    }

    public void setQuantiteBase(Double quantiteBase) {
        this.quantiteBase = quantiteBase;
    }

    public Double getCadence() {
        return cadence != null ? cadence : 0;
    }

    public void setCadence(Double cadence) {
        this.cadence = cadence;
    }

    public Double getQuantiteMin() {
        return quantiteMin != null ? quantiteMin : 0;
    }

    public void setQuantiteMin(Double quantiteMin) {
        this.quantiteMin = quantiteMin;
    }

    public Character getTypeCout() {
        return typeCout != null ? typeCout : 'P';
    }

    public void setTypeCout(Character typeCout) {
        this.typeCout = typeCout;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public YvsProdPosteOperation givePosteMachine() {
        if (postes != null) {
            for (YvsProdPosteOperation po : postes) {
                if (po.getTypeCharge() != null) {
                    if (po.getTypeCharge().equals(Constantes.PROD_OP_TYPE_CHARGE_MACHINE)) {
                        return po;
                    }
                }
            }
        }
        return null;
    }

    public YvsProdPosteOperation givePosteMo() {
        if (postes != null) {
            for (YvsProdPosteOperation po : postes) {
                if (po.getTypeCharge() != null) {
                    if (po.getTypeCharge().equals(Constantes.PROD_OP_TYPE_CHARGE_MO)) {
                        return po;
                    }
                }
            }
        }
        return null;
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
        if (!(object instanceof YvsProdOperationsGamme)) {
            return false;
        }
        YvsProdOperationsGamme other = (YvsProdOperationsGamme) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "yvs.entity.production.base.YvsProdOperationsGamme[ id=" + id + " ]";
    }

    public List<YvsProdComposantOp> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdComposantOp> composants) {
        this.composants = composants;
    }

    @Override
    public int compareTo(Object o) {
        YvsProdOperationsGamme c = (YvsProdOperationsGamme) o;
        if (Integer.valueOf(numero).equals(c.numero)) {
            return id.compareTo(c.id);
        }
        return Integer.valueOf(numero).compareTo(c.numero);
    }

}
