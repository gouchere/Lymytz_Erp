/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.print;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_print_contrat_employe_header", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findAll", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findById", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByNom", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.nom = :nom AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByModel", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.model = :model AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByDefaut", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.defaut = :defaut AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByPartieSociete", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.partieSociete = :partieSociete"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByPartiePrestataire", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.partiePrestataire = :partiePrestataire"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByPreambule", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.preambule = :preambule"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByDefinition", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.definition = :definition"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByDateUpdate", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByDateSave", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsPrintContratEmployeHeader.findByExecuteTrigger", query = "SELECT y FROM YvsPrintContratEmployeHeader y WHERE y.executeTrigger = :executeTrigger")})
public class YvsPrintContratEmployeHeader implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_print_contrat_employe_header_id_seq", name = "yvs_print_contrat_employe_header_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_print_contrat_employe_header_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Size(max = 2147483647)
    @Column(name = "model")
    private String model;
    @Column(name = "defaut")
    private Boolean defaut;
    @Size(max = 2147483647)
    @Column(name = "partie_societe")
    private String partieSociete;
    @Size(max = 2147483647)
    @Column(name = "partie_prestataire")
    private String partiePrestataire;
    @Size(max = 2147483647)
    @Column(name = "preambule")
    private String preambule;
    @Size(max = 2147483647)
    @Column(name = "definition")
    private String definition;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "execute_trigger")
    private String executeTrigger;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne
    private YvsUsersAgence author;
    @JoinColumn(name = "societe", referencedColumnName = "id")
    @ManyToOne
    private YvsSocietes societe;

    public YvsPrintContratEmployeHeader() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsPrintContratEmployeHeader(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getDefaut() {
        return defaut != null ? defaut : false;
    }

    public void setDefaut(Boolean defaut) {
        this.defaut = defaut;
    }

    public String getPartieSociete() {
        return partieSociete;
    }

    public void setPartieSociete(String partieSociete) {
        this.partieSociete = partieSociete;
    }

    public String getPartiePrestataire() {
        return partiePrestataire;
    }

    public void setPartiePrestataire(String partiePrestataire) {
        this.partiePrestataire = partiePrestataire;
    }

    public String getPreambule() {
        return preambule;
    }

    public void setPreambule(String preambule) {
        this.preambule = preambule;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
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

    public String getExecuteTrigger() {
        return executeTrigger;
    }

    public void setExecuteTrigger(String executeTrigger) {
        this.executeTrigger = executeTrigger;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof YvsPrintContratEmployeHeader)) {
            return false;
        }
        YvsPrintContratEmployeHeader other = (YvsPrintContratEmployeHeader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.print.YvsPrintContratEmployeHeader[ id=" + id + " ]";
    }

}
