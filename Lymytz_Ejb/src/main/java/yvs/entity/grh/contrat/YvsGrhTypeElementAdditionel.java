/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.grh.contrat;

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
import yvs.entity.compta.YvsBasePlanComptable;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ-PC
 */
@Entity
@Table(name = "yvs_grh_type_element_additionel")
@NamedQueries({
    @NamedQuery(name = "YvsGrhTypeElementAdditionel.findAll", query = "SELECT y FROM YvsGrhTypeElementAdditionel y WHERE y.societe=:societe AND y.actif=true AND y.retenue=:retenue"),
    @NamedQuery(name = "YvsGrhTypeElementAdditionel.findAllCom", query = "SELECT y FROM YvsGrhTypeElementAdditionel y WHERE y.societe=:societe AND y.actif=true AND y.retenue=:retenue AND y.visibleEnGescom=true"),
    @NamedQuery(name = "YvsGrhTypeElementAdditionel.findById", query = "SELECT y FROM YvsGrhTypeElementAdditionel y WHERE y.id = :id"),
    @NamedQuery(name = "YvsGrhTypeElementAdditionel.findByCodeElement", query = "SELECT y FROM YvsGrhTypeElementAdditionel y WHERE y.codeElement = :codeElement AND y.societe=:societe AND y.retenue=false "),
    @NamedQuery(name = "YvsGrhTypeElementAdditionel.findByCodeElementRetenu", query = "SELECT y FROM YvsGrhTypeElementAdditionel y WHERE y.codeElement = :codeElement AND y.societe=:societe AND y.retenue=true "),
    @NamedQuery(name = "YvsGrhTypeElementAdditionel.findByLibelle", query = "SELECT y FROM YvsGrhTypeElementAdditionel y WHERE y.libelle = :libelle")})
public class YvsGrhTypeElementAdditionel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_grh_type_element_additionel_id_seq", name = "yvs_grh_type_element_additionel_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_grh_type_element_additionel_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "retenu")
    private Boolean retenue;    
    @Size(max = 2147483647)
    @Column(name = "code_element")
    private String codeElement;
    @Size(max = 2147483647)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "actif")
    private Boolean actif;
    @Column(name = "visible_en_gescom")
    private Boolean visibleEnGescom;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    
    @JoinColumn(name = "compte", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBasePlanComptable compte;    
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsSocietes societe;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    public YvsGrhTypeElementAdditionel() {
    }

    public YvsGrhTypeElementAdditionel(Long id) {
        this.id = id;
    }

    public YvsGrhTypeElementAdditionel(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public YvsGrhTypeElementAdditionel(Long id, String codeElement, String libelle) {
        this.id = id;
        this.codeElement = codeElement;
        this.libelle = libelle;
    }

    public YvsGrhTypeElementAdditionel(YvsGrhTypeElementAdditionel t) {
        this.retenue = t.retenue;
        this.id = t.id;
        this.codeElement = t.codeElement;
        this.libelle = t.libelle;
        this.societe = t.societe;
        this.actif = t.actif;
        this.author = t.author;
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

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeElement() {
        return codeElement;
    }

    public void setCodeElement(String codeElement) {
        this.codeElement = codeElement;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public YvsSocietes getSociete() {
        return societe;
    }

    public void setSociete(YvsSocietes societe) {
        this.societe = societe;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public Boolean getVisibleEnGescom() {
        return visibleEnGescom != null ? visibleEnGescom : false;
    }

    public void setVisibleEnGescom(Boolean visibleEnGescom) {
        this.visibleEnGescom = visibleEnGescom;
    }

    public YvsBasePlanComptable getCompte() {
        return compte;
    }

    public void setCompte(YvsBasePlanComptable compte) {
        this.compte = compte;
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
        if (!(object instanceof YvsGrhTypeElementAdditionel)) {
            return false;
        }
        YvsGrhTypeElementAdditionel other = (YvsGrhTypeElementAdditionel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.grh.personnel.YvsGrhTypeElementAdditionel[ id=" + id + " ]";
    }

    public Boolean getRetenue() {
        return retenue != null ? retenue : false;
    }

    public void setRetenue(Boolean retenue) {
        this.retenue = retenue;
    }

}
