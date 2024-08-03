/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production;

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
import yvs.dao.Util;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_prod_parametre")
@NamedQueries({
    @NamedQuery(name = "YvsProdParametre.findAll", query = "SELECT y FROM YvsProdParametre y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsProdParametre.findById", query = "SELECT y FROM YvsProdParametre y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdParametre.findByConverter", query = "SELECT y FROM YvsProdParametre y WHERE y.converter = :converter"),
    @NamedQuery(name = "YvsProdParametre.findByDateUpdate", query = "SELECT y FROM YvsProdParametre y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsProdParametre.findByDateSave", query = "SELECT y FROM YvsProdParametre y WHERE y.dateSave = :dateSave")})
public class YvsProdParametre implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_parametre_id_seq", name = "yvs_prod_parametre_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_parametre_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "converter")
    private Integer converter;
    @Column(name = "converter_pf")
    private Integer converterPF;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "suivi_op_requis")
    private Boolean suiviOpRequis;
    @Column(name = "close_decl_auto")
    private Boolean closeDeclAuto;
    @Column(name = "num_cmde_requis")
    private Boolean numCmdeRequis;
    @Column(name = "declaration_continue")
    private Boolean declarationContinue;
    @Column(name = "declare_when_finish_of")
    private Boolean declareWhenFinishOf;
    @Column(name = "limite_vu_of")
    private Integer limiteVuOf;
    @Column(name = "limite_create_of")
    private Integer limiteCreateOf;
    @Column(name = "declaration_proportionnel")
    private Boolean declarationProportionnel;
    @Column(name = "valorise_from_of")
    private Boolean valoriseFromOf;
    @Column(name = "valoriser_by")
    private String valoriserBy = "V";

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;

    public YvsProdParametre() {
    }

    public YvsProdParametre(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValoriserBy() {
        return Util.asString(valoriserBy) ? valoriserBy : "A";
    }

    public void setValoriserBy(String valoriserBy) {
        this.valoriserBy = valoriserBy;
    }

    public Integer getConverter() {
        return converter != null ? converter : 0;
    }

    public void setConverter(Integer converter) {
        this.converter = converter;
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

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public Boolean getValoriseFromOf() {
        return valoriseFromOf != null ? valoriseFromOf : false;
    }

    public void setValoriseFromOf(Boolean valoriseFromOf) {
        this.valoriseFromOf = valoriseFromOf;
    }

    public Boolean getSuiviOpRequis() {
        return suiviOpRequis == null ? true : suiviOpRequis;
    }

    public void setSuiviOpRequis(Boolean suiviOpRequis) {
        this.suiviOpRequis = suiviOpRequis;
    }

    public Boolean getNumCmdeRequis() {
        return numCmdeRequis != null ? numCmdeRequis : false;
    }

    public void setNumCmdeRequis(Boolean numCmdeRequis) {
        this.numCmdeRequis = numCmdeRequis;
    }

    public Integer getLimiteVuOf() {
        return limiteVuOf != null ? limiteVuOf : 1;
    }

    public void setLimiteVuOf(Integer limiteVuOf) {
        this.limiteVuOf = limiteVuOf;
    }

    public Integer getLimiteCreateOf() {
        return limiteCreateOf != null ? limiteCreateOf : 1;
    }

    public void setLimiteCreateOf(Integer limiteCreateOf) {
        this.limiteCreateOf = limiteCreateOf;
    }

    public Boolean getCloseDeclAuto() {
        return closeDeclAuto != null ? closeDeclAuto : false;
    }

    public void setCloseDeclAuto(Boolean closeDeclAuto) {
        this.closeDeclAuto = closeDeclAuto;
    }

    public Boolean getDeclarationContinue() {
        return declarationContinue != null ? declarationContinue : true;
    }

    public void setDeclarationContinue(Boolean declarationContinue) {
        this.declarationContinue = declarationContinue;
    }

    public Boolean getDeclarationProportionnel() {
        return declarationProportionnel != null ? declarationProportionnel : false;
    }

    public void setDeclarationProportionnel(Boolean declarationProportionnel) {
        this.declarationProportionnel = declarationProportionnel;
    }

    public Integer getConverterPF() {
        return converterPF != null ? converterPF : 0;
    }

    public void setConverterPF(Integer converterPF) {
        this.converterPF = converterPF;
    }

    public Boolean getDeclareWhenFinishOf() {
        return declareWhenFinishOf != null ? declareWhenFinishOf : false;
    }

    public void setDeclareWhenFinishOf(Boolean declareWhenFinishOf) {
        this.declareWhenFinishOf = declareWhenFinishOf;
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
        if (!(object instanceof YvsProdParametre)) {
            return false;
        }
        YvsProdParametre other = (YvsProdParametre) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.YvsProdParametre[ id=" + id + " ]";
    }

}
