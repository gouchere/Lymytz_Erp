/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author GOUCHERE YVES
 */
@Entity
@Table(name = "yvs_crenaux_horaire")
@NamedQueries({
    @NamedQuery(name = "YvsCrenauxHoraire.findAll", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.actif=true AND y.idAgence.id=:agence"),
    @NamedQuery(name = "YvsCrenauxHoraire.findById", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.id = :id"),
    @NamedQuery(name = "YvsCrenauxHoraire.findByCode", query = "SELECT y FROM YvsCrenauxHoraire y WHERE (y.codeTranche = :code OR y.libelle=:code) AND y.idAgence.id=:agence"),
    @NamedQuery(name = "YvsCrenauxHoraire.findByLibelle", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.libelle = :libelle"),
    @NamedQuery(name = "YvsCrenauxHoraire.findByTypeDeJournee", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.typeDeJournee = :typeDeJournee"),
    @NamedQuery(name = "YvsCrenauxHoraire.findByHeureDeb", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.heureDeb = :heureDeb"),
    @NamedQuery(name = "YvsCrenauxHoraire.findByHeureFin", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.heureFin = :heureFin"),
    @NamedQuery(name = "YvsCrenauxHoraire.findByOrdre", query = "SELECT y FROM YvsCrenauxHoraire y WHERE y.ordre = :ordre")})
public class YvsCrenauxHoraire implements Serializable {

    @Column(name = "supp")
    private Boolean supp;
    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "heure_deb")
    @Temporal(TemporalType.TIME)
    private Date heureDeb;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id", columnDefinition = "BIGSERIAL")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "yvs_tranche_horaire_id_seq")
    @SequenceGenerator(sequenceName = "yvs_tranche_horaire_id_seq", allocationSize = 1, name = "yvs_tranche_horaire_id_seq")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "code_tranche")
    private String codeTranche;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 2147483647)
    @Column(name = "type_de_journee")
    private String typeDeJournee;
    @Column(name = "ordre")
    private Integer ordre;
    @JoinColumn(name = "id_agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences idAgence;

    public YvsCrenauxHoraire() {
    }

    public YvsCrenauxHoraire(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeDeJournee() {
        return typeDeJournee;
    }

    public void setTypeDeJournee(String typeDeJournee) {
        this.typeDeJournee = typeDeJournee;
    }

    public Date getHeureDeb() {
        return heureDeb;
    }

    public void setHeureDeb(Date heureDeb) {
        this.heureDeb = heureDeb;
    }

    public Date getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public YvsAgences getIdAgence() {
        return idAgence;
    }

    public void setIdAgence(YvsAgences idAgence) {
        this.idAgence = idAgence;
    }

    public String getCodeTranche() {
        return codeTranche;
    }

    public void setCodeTranche(String codeTranche) {
        this.codeTranche = codeTranche;
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
        if (!(object instanceof YvsCrenauxHoraire)) {
            return false;
        }
        YvsCrenauxHoraire other = (YvsCrenauxHoraire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.YvsTrancheHoraire[ id=" + id + " ]";
    }

    public Boolean getSupp() {
        return supp;
    }

    public void setSupp(Boolean supp) {
        this.supp = supp;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }
}
