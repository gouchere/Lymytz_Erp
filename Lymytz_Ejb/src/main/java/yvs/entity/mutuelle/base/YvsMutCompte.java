/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.mutuelle.base;

import yvs.entity.mutuelle.operation.YvsMutOperationCompte;
import java.io.Serializable;
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
import yvs.entity.mutuelle.credit.YvsMutCredit;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author lymytz
 */
@Entity
@Table(name = "yvs_mut_compte")
@NamedQueries({
    @NamedQuery(name = "YvsMutCompte.findAll", query = "SELECT y FROM YvsMutCompte y WHERE y.mutualiste.mutuelle.societe = :societe"),
    @NamedQuery(name = "YvsMutCompte.findByMutuelle", query = "SELECT y FROM YvsMutCompte y WHERE y.mutualiste.mutuelle = :mutuelle"),
    @NamedQuery(name = "YvsMutCompte.findByMutualiste", query = "SELECT y FROM YvsMutCompte y WHERE y.mutualiste = :mutualiste"),
    @NamedQuery(name = "YvsMutCompte.findByMutualisteActif", query = "SELECT y FROM YvsMutCompte y WHERE y.mutualiste = :mutualiste AND y.actif=true"),
    @NamedQuery(name = "YvsMutCompte.findByMutualisteOpInteret", query = "SELECT y FROM YvsMutCompte y WHERE y.mutualiste = :mutualiste AND y.actif=true AND y.interet=true"),
    @NamedQuery(name = "YvsMutCompte.findByMutualisteType", query = "SELECT y FROM YvsMutCompte y WHERE y.mutualiste = :mutualiste AND y.typeCompte.nature = :type AND y.actif=true"),
    @NamedQuery(name = "YvsMutCompte.findById", query = "SELECT y FROM YvsMutCompte y WHERE y.id = :id"),
    @NamedQuery(name = "YvsMutCompte.findByIds", query = "SELECT y FROM YvsMutCompte y WHERE y.id IN :ids"),
    @NamedQuery(name = "YvsMutCompte.findByReference", query = "SELECT y FROM YvsMutCompte y WHERE y.reference = :reference"),
    @NamedQuery(name = "YvsMutCompte.findByType", query = "SELECT y FROM YvsMutCompte y WHERE y.typeCompte.nature = :type AND y.typeCompte.mutuelle=:mutuelle"),
    @NamedQuery(name = "YvsMutCompte.findBySolde", query = "SELECT y FROM YvsMutCompte y WHERE y.solde = :solde")})
public class YvsMutCompte implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_mut_compte_id_seq", name = "yvs_mut_compte_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_mut_compte_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @Column(name = "reference")
    private String reference;
    @Column(name = "solde")
    private Double solde;
    @Column(name = "solde_alerte")
    private Double soldeAlerte;
    @Column(name = "salaire")
    private Boolean salaire;
    @Column(name = "prime")
    private Boolean prime;
    @Column(name = "interet")
    private Boolean interet;
    @Column(name = "actif")
    private Boolean actif;

    @JoinColumn(name = "type_compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsMutTypeCompte typeCompte;
    @JoinColumn(name = "mutualiste", referencedColumnName = "id")
    @ManyToOne
    private YvsMutMutualiste mutualiste;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "compte")
    private List<YvsMutCredit> credits;
    @OneToMany(mappedBy = "compte")
    private List<YvsMutOperationCompte> operations;

    @Transient
    private boolean selectActif;
    @Transient
    private boolean new_;
    @Transient
    private String proprietaire;

    public YvsMutCompte() {
    }

    public YvsMutCompte(Long id) {
        this.id = id;
    }

    public YvsMutCompte(Long id, String reference) {
        this.id = id;
        this.reference = reference;
    }

    public YvsMutCompte(Long id, YvsMutMutualiste mutualiste) {
        this.id = id;
        this.mutualiste = mutualiste;
    }

    public YvsMutCompte(Long id, String reference, YvsMutMutualiste mutualiste) {
        this.id = id;
        this.reference = reference;
        this.mutualiste = mutualiste;
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

    public Boolean getSalaire() {
        return salaire != null ? salaire : false;
    }

    public void setSalaire(Boolean salaire) {
        this.salaire = salaire;
    }

    public Boolean getPrime() {
        return prime != null ? prime : false;
    }

    public void setPrime(Boolean prime) {
        this.prime = prime;
    }

    public Boolean getInteret() {
        return interet != null ? interet : false;
    }

    public void setInteret(Boolean interet) {
        this.interet = interet;
    }

    public String getProprietaire() {
        if ((getMutualiste() != null) ? getMutualiste().getId() > 0 : false) {
            if ((getMutualiste().getEmploye() != null) ? getMutualiste().getEmploye().getId()> 0 : false) {
                    String name = getMutualiste().getEmploye().getNom_prenom();
                    proprietaire = name;
            }
        }
        return proprietaire;
    }

    public void setProprietaire(String proprietaire) {
        this.proprietaire = proprietaire;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getSolde() {
        return solde != null ? solde : 0;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }

    public Double getSoldeAlerte() {
        return soldeAlerte != null ? soldeAlerte : 0;
    }

    public void setSoldeAlerte(Double soldeAlerte) {
        this.soldeAlerte = soldeAlerte;
    }

    public YvsMutTypeCompte getTypeCompte() {
        return typeCompte;
    }

    public void setTypeCompte(YvsMutTypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }

    public YvsMutMutualiste getMutualiste() {
        return mutualiste;
    }

    public void setMutualiste(YvsMutMutualiste mutualiste) {
        this.mutualiste = mutualiste;
    }

    public List<YvsMutOperationCompte> getOperations() {
        return operations;
    }

    public void setOperations(List<YvsMutOperationCompte> operations) {
        this.operations = operations;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public List<YvsMutCredit> getCredits() {
        return credits;
    }

    public void setCredits(List<YvsMutCredit> credits) {
        this.credits = credits;
    }

    public Boolean getActif() {
        return actif!=null?actif:false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
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
        if (!(object instanceof YvsMutCompte)) {
            return false;
        }
        YvsMutCompte other = (YvsMutCompte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.mutuelle.base.YvsMutCompte[ id=" + id + " ]";
    }
}
