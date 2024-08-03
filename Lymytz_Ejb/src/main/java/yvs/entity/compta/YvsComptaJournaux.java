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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.YvsEntity;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz
 */
@Entity
@Table(name = "yvs_compta_journaux")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComptaJournaux.findAll", query = "SELECT y FROM YvsComptaJournaux y WHERE y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaJournaux.findById", query = "SELECT y FROM YvsComptaJournaux y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComptaJournaux.findByCodejournal", query = "SELECT y FROM YvsComptaJournaux y WHERE y.codeJournal = :codeJournal"),
    @NamedQuery(name = "YvsComptaJournaux.findIdByCodejournal", query = "SELECT y.id FROM YvsComptaJournaux y WHERE y.codeJournal = :code AND y.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaJournaux.findIdByCodejournale", query = "SELECT y FROM YvsComptaJournaux y WHERE y.codeJournal = :code AND y.agence.societe=:societe"),
    @NamedQuery(name = "YvsComptaJournaux.findByActif", query = "SELECT y FROM YvsComptaJournaux y WHERE y.actif = :actif AND y.agence.societe = :societe"),
    @NamedQuery(name = "YvsComptaJournaux.findByLibele", query = "SELECT y FROM YvsComptaJournaux y WHERE y.intitule = :intitule"),
    @NamedQuery(name = "YvsComptaJournaux.findByTypeJournal", query = "SELECT y FROM YvsComptaJournaux y WHERE y.typeJournal = :typeJournal"),
    @NamedQuery(name = "YvsComptaJournaux.findByDefaut", query = "SELECT y FROM YvsComptaJournaux y WHERE y.typeJournal = :type AND y.agence =:agence AND y.defaultFor = :default"),
    @NamedQuery(name = "YvsComptaJournaux.findByAgence", query = "SELECT y FROM YvsComptaJournaux y WHERE y.agence = :agence")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class YvsComptaJournaux extends YvsEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_compta_journaux_id_seq", name = "yvs_compta_journaux_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_compta_journaux_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 255)
    @Column(name = "code_journal")
    private String codeJournal;
    @Column(name = "actif")
    private Boolean actif;
    @Size(max = 255)
    @Column(name = "intitule")
    private String intitule;
    @Column(name = "default_for")
    private boolean defaultFor;
    @Size(max = 255)
    @Column(name = "type_journal")
    private String typeJournal;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "code_acces", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsBaseCodeAcces codeAcces;
    @JoinColumn(name = "agence", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsAgences agence;
    @Transient
    private boolean new_;

    public YvsComptaJournaux() {
    }

    public YvsComptaJournaux(Long id) {
        this.id = id;
    }
    
    public YvsComptaJournaux(Long id, YvsAgences agence) {
        this(id);
        this.agence = agence;
    }

    public YvsComptaJournaux(Long id, String codeJournal) {
        this(id);
        this.codeJournal = codeJournal;
    }

    public YvsComptaJournaux(Long id, String codeJournal, String intitule) {
        this(id,codeJournal);
        this.intitule = intitule;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeJournal() {
        return codeJournal != null ? codeJournal : "";
    }

    public void setCodeJournal(String codeJournal) {
        this.codeJournal = codeJournal;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getTypeJournal() {
        return typeJournal != null ? typeJournal.trim().length() > 0 ? typeJournal : "ACHAT" : "ACHAT";
    }

    public void setTypeJournal(String typeJournal) {
        this.typeJournal = typeJournal;
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

    public void setAgence(YvsAgences agence) {
        this.agence = agence;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isNew_() {
        return new_;
    }

    public boolean getDefaultFor() {
        return defaultFor;
    }

    public void setDefaultFor(boolean defaultFor) {
        this.defaultFor = defaultFor;
    }

    public YvsBaseCodeAcces getCodeAcces() {
        return codeAcces;
    }

    public void setCodeAcces(YvsBaseCodeAcces codeAcces) {
        this.codeAcces = codeAcces;
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
        if (!(object instanceof YvsComptaJournaux)) {
            return false;
        }
        YvsComptaJournaux other = (YvsComptaJournaux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.compta.YvsComptaJournaux[ id=" + id + " ]";
    }
}
