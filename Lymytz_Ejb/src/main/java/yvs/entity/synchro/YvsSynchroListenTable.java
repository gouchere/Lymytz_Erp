/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.synchro;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_synchro_listen_table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsSynchroListenTable.findAll", query = "SELECT y FROM YvsSynchroListenTable y"),
    @NamedQuery(name = "YvsSynchroListenTable.findById", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.id = :id"),
    @NamedQuery(name = "YvsSynchroListenTable.findByNameTable", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.nameTable = :nameTable"),
    @NamedQuery(name = "YvsSynchroListenTable.findByIdSource", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.idSource = :idSource"),
    @NamedQuery(name = "YvsSynchroListenTable.findByDateSave", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsSynchroListenTable.findByToListen", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.toListen = :toListen ORDER BY y.id"),
    @NamedQuery(name = "YvsSynchroListenTable.findByActionName", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.actionName = :actionName"),
    @NamedQuery(name = "YvsSynchroListenTable.findByOrdre", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.ordre = :ordre"),

    @NamedQuery(name = "YvsSynchroListenTable.findBySource", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.idSource = :idSource AND y.nameTable = :nameTable"),
    @NamedQuery(name = "YvsSynchroListenTable.findByActionSource", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.idSource = :idSource AND y.nameTable = :nameTable AND y.actionName = :action"),
    @NamedQuery(name = "YvsSynchroListenTable.findLastBySource", query = "SELECT y FROM YvsSynchroListenTable y WHERE y.idSource = :idSource AND y.nameTable = :nameTable ORDER BY y.ordre DESC")})
public class YvsSynchroListenTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_synchro_listen_table_id_seq", name = "yvs_synchro_listen_table_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_synchro_listen_table_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "name_table")
    private String nameTable;
    @Column(name = "id_source")
    private Long idSource;
    @Column(name = "author")
    private Long author;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Size(max = 2147483647)
    @Column(name = "message")
    private String message;
    @Column(name = "to_listen")
    private Boolean toListen;
    @Size(max = 2147483647)
    @Column(name = "action_name")
    private String actionName;
    @Basic(optional = false)
    @Column(name = "ordre")
    private long ordre;
    @OneToMany(mappedBy = "idListen")
    @JoinColumn(name = "serveur", referencedColumnName = "id")
    @ManyToOne
    private YvsSynchroServeurs serveur;

    public YvsSynchroListenTable() {
        this.dateSave = new Date();
    }

    public YvsSynchroListenTable(Long id) {
        this();
        this.id = id;
    }

    public YvsSynchroListenTable(Long id, long ordre) {
        this.id = id;
        this.ordre = ordre;
    }

    public Long getId() {
        return id!=null?id:0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTable() {
        return nameTable;
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public Long getIdSource() {
        return idSource;
    }

    public void setIdSource(Long idSource) {
        this.idSource = idSource;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getToListen() {
        return toListen;
    }

    public void setToListen(Boolean toListen) {
        this.toListen = toListen;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public long getOrdre() {
        return ordre;
    }

    public void setOrdre(long ordre) {
        this.ordre = ordre;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    public YvsSynchroServeurs getServeur() {
        return serveur;
    }

    public void setServeur(YvsSynchroServeurs serveur) {
        this.serveur = serveur;
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
        if (!(object instanceof YvsSynchroListenTable)) {
            return false;
        }
        YvsSynchroListenTable other = (YvsSynchroListenTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.synchro.YvsSynchroListenTable[ id=" + id + " ]";
    }

}
