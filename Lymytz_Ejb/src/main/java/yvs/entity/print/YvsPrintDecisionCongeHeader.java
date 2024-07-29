/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.print;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.param.YvsSocietes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LYMYTZ
 */
@Entity
@Table(name = "yvs_print_decision_conge_header", catalog = "lymytz_demo_0", schema = "public")
@NamedQueries({
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findAll", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.societe = :societe"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findById", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.id = :id"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findByNom", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.nom = :nom AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findByModel", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.model = :model AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findByDefaut", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.defaut = :defaut AND y.societe = :societe"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findByDateUpdate", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findByDateSave", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsPrintDecisionCongeHeader.findByExecuteTrigger", query = "SELECT y FROM YvsPrintDecisionCongeHeader y WHERE y.executeTrigger = :executeTrigger")})
public class YvsPrintDecisionCongeHeader implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_print_decision_conge_header_id_seq", name = "yvs_print_decision_conge_header_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_print_decision_conge_header_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nom")
    private String nom;
    @Size(max = 2147483647)
    @Column(name = "model")
    private String model;
    @Size(max = 2147483647)
    @Column(name = "titre")
    private String titre;
    @Column(name = "defaut")
    private Boolean defaut;
    @Size(max = 2147483647)
    @Column(name = "introduction")
    private String introduction;
    @Size(max = 2147483647)
    @Column(name = "definition_conventive")
    private String definitionConventive;
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

    public YvsPrintDecisionCongeHeader() {
        this.dateSave = new Date();
        this.dateUpdate = new Date();
    }

    public YvsPrintDecisionCongeHeader(Long id) {
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
    
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDefinitionConventive() {
        return definitionConventive;
    }

    public void setDefinitionConventive(String definitionConventive) {
        this.definitionConventive = definitionConventive;
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
        if (!(object instanceof YvsPrintDecisionCongeHeader)) {
            return false;
        }
        YvsPrintDecisionCongeHeader other = (YvsPrintDecisionCongeHeader) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.print.YvsPrintDecisionCongeHeader[ id=" + id + " ]";
    }

}
