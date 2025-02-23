/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.entity.mutuelle.base.YvsMutMutualiste;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsMutCaisse.findAll", query = "SELECT y FROM YvsMutCaisse y WHERE y.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutCaisse.findByMutuelle", query = "SELECT y FROM YvsMutCaisse y WHERE y.mutuelle = :mutuelle ORDER BY y.principal DESC"),
    @NamedQuery(name = "YvsMutCaisse.findCaissePrincipale", query = "SELECT y FROM YvsMutCaisse y WHERE y.mutuelle = :mutuelle AND y.principal=true ORDER BY y.principal DESC"),
    @NamedQuery(name = "YvsMutCaisse.findByMutuelleByOp", query = "SELECT y FROM YvsMutCaisse y WHERE y.mutuelle=:mutuelle AND y.typeDeFlux=:type AND y.actif=true"),
    @NamedQuery(name = "YvsMutCaisse.findById", query = "SELECT y FROM YvsMutCaisse y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutCaisse.findByReference", query = "SELECT y FROM YvsMutCaisse y WHERE y.referenceCaisse = :reference"),
    @NamedQuery(name = "YvsMutCaisse.findBySolde", query = "SELECT y FROM YvsMutCaisse y WHERE y.solde = :solde"),
    @NamedQuery(name = "YvsMutCaisse.findByResponsable", query = "SELECT y FROM YvsMutCaisse y WHERE y.responsable = :responsable"),
    @NamedQuery(name = "YvsMutCaisse.findByEmploye", query = "SELECT y FROM YvsMutCaisse y WHERE y.responsable.employe = :employe"),
    @NamedQuery(name = "YvsMutCaisse.findByUsers", query = "SELECT y FROM YvsMutCaisse y WHERE y.responsable.employe.codeUsers = :users")})
public class YvsMutCaisse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_caisse_id_seq", name = "yvs_mut_caisse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_caisse_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference_caisse")
    private String referenceCaisse;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "solde")
    private Double solde;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "principal")
    private Boolean principal;  //Une caisse principale par mutuelle
    @Column(name = "type_de_flux")
    private String typeDeFlux;  //PHYSIQUE OU SCRIPTURALE

    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutualiste responsable;
    @JoinColumn(name = "mutuelle", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutMutuelle mutuelle;
    @OneToMany(mappedBy = "caisse")
    private List<YvsMutDefaultUseFor> defaultActivites;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;

    public YvsMutCaisse() {
    }

    public YvsMutCaisse(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
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

    public YvsMutCaisse(Long id, String referenceCaisse) {
        this.id = id;
        this.referenceCaisse = referenceCaisse;
    }

    public boolean isSelectActif() {
        return selectActif;
    }

    public void setSelectActif(boolean selectActif) {
        this.selectActif = selectActif;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public String getReferenceCaisse() {
        return referenceCaisse;
    }

    public void setReferenceCaisse(String referenceCaisse) {
        this.referenceCaisse = referenceCaisse;
    }

    public Double getSolde() {
        return solde != null ? solde : 0;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsMutMutuelle getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(YvsMutMutuelle mutuelle) {
        this.mutuelle = mutuelle;
    }

    public YvsMutMutualiste getResponsable() {
        return responsable;
    }

    public void setResponsable(YvsMutMutualiste responsable) {
        this.responsable = responsable;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public List<YvsMutDefaultUseFor> getDefaultActivites() {
        return defaultActivites;
    }

    public void setDefaultActivites(List<YvsMutDefaultUseFor> defaultActivites) {
        this.defaultActivites = defaultActivites;
    }

    public String getTypeDeFlux() {
        return typeDeFlux;
    }

    public void setTypeDeFlux(String typeDeFlux) {
        this.typeDeFlux = typeDeFlux;
    }

    public Boolean getPrincipal() {
        return principal != null ? principal : false;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
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
        if (!(object instanceof YvsMutCaisse)) {
            return false;
        }
        YvsMutCaisse other = (YvsMutCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.operation.YvsMutCaisse[ id=" + id + " ]";
    }

}
