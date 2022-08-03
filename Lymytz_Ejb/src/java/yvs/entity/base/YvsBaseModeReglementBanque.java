/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.entity.base;

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
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_base_mode_reglement_banque")
@NamedQueries({
    @NamedQuery(name = "YvsBaseModeReglementBanque.findAll", query = "SELECT y FROM YvsBaseModeReglementBanque y"),
    @NamedQuery(name = "YvsBaseModeReglementBanque.findById", query = "SELECT y FROM YvsBaseModeReglementBanque y WHERE y.id = :id"),
    @NamedQuery(name = "YvsBaseModeReglementBanque.findByDateUpdate", query = "SELECT y FROM YvsBaseModeReglementBanque y WHERE y.dateUpdate = :dateUpdate"),
    @NamedQuery(name = "YvsBaseModeReglementBanque.findByDateSave", query = "SELECT y FROM YvsBaseModeReglementBanque y WHERE y.dateSave = :dateSave"),
    @NamedQuery(name = "YvsBaseModeReglementBanque.findByExecuteTrigger", query = "SELECT y FROM YvsBaseModeReglementBanque y WHERE y.executeTrigger = :executeTrigger")})
public class YvsBaseModeReglementBanque implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_base_mode_reglement_banque_id_seq", name = "yvs_base_mode_reglement_banque_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_base_mode_reglement_banque_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "code_banque")
    private String codeBanque;
    @Size(max = 2147483647)
    @Column(name = "code_guichet")
    private String codeGuichet;
    @Size(max = 2147483647)
    @Column(name = "numero")
    private String numero;
    @Size(max = 2147483647)
    @Column(name = "cle")
    private String cle;
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
    @JoinColumn(name = "mode", referencedColumnName = "id")
    @ManyToOne
    private YvsBaseModeReglement mode;

    public YvsBaseModeReglementBanque() {
    }

    public YvsBaseModeReglementBanque(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeBanque() {
        return codeBanque;
    }

    public void setCodeBanque(String codeBanque) {
        this.codeBanque = codeBanque;
    }

    public String getCodeGuichet() {
        return codeGuichet;
    }

    public void setCodeGuichet(String codeGuichet) {
        this.codeGuichet = codeGuichet;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCle() {
        return cle;
    }

    public void setCle(String cle) {
        this.cle = cle;
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

    public YvsBaseModeReglement getMode() {
        return mode;
    }

    public void setMode(YvsBaseModeReglement mode) {
        this.mode = mode;
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
        if (!(object instanceof YvsBaseModeReglementBanque)) {
            return false;
        }
        YvsBaseModeReglementBanque other = (YvsBaseModeReglementBanque) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.base.YvsBaseModeReglementBanque[ id=" + id + " ]";
    }
    
}
