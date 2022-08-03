/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.achat.YvsComDocAchats;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_workflow_valid_facture_achat")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowValidFactureAchat.findAll", query = "SELECT y FROM YvsWorkflowValidFactureAchat y"),
    @NamedQuery(name = "YvsWorkflowValidFactureAchat.findByFacture", query = "SELECT y FROM YvsWorkflowValidFactureAchat y WHERE y.factureAchat = :facture ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidFactureAchat.findByEtapeFacture", query = "SELECT y FROM YvsWorkflowValidFactureAchat y WHERE y.factureAchat = :facture AND y.etape = :etape"),
    @NamedQuery(name = "YvsWorkflowValidFactureAchat.findByEtapeValid", query = "SELECT y FROM YvsWorkflowValidFactureAchat y WHERE y.etapeValid = :etapeValid"),
    @NamedQuery(name = "YvsWorkflowValidFactureAchat.findByIDEtapeFacture", query = "SELECT y.id FROM YvsWorkflowValidFactureAchat y WHERE y.factureAchat = :facture AND y.etape = :etape"),
    @NamedQuery(name = "YvsWorkflowValidFactureAchat.findById", query = "SELECT y FROM YvsWorkflowValidFactureAchat y WHERE y.id = :id")})
public class YvsWorkflowValidFactureAchat extends YvsEntity implements Serializable, Comparable<YvsWorkflowValidFactureAchat> {

    private static final long serialVersionUID = 1L;
    @Column(name = "etape_valid")
    private Boolean etapeValid;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_valid_facture_achat_id_seq", name = "yvs_workflow_valid_facture_achat_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_valid_facture_achat_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "motif")
    private String motif;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "ordre_etape")
    private Integer ordreEtape;
    @JoinColumn(name = "etape", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsWorkflowEtapeValidation etape;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JsonManagedReference
    @JoinColumn(name = "facture_achat", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocAchats factureAchat;
    @Transient
    private YvsWorkflowValidFactureAchat etapeSuivante;
    @Transient
    private boolean etapeActive;
    @Transient
    private int etapeValide;

    public YvsWorkflowValidFactureAchat() {
    }

    public YvsWorkflowValidFactureAchat(Long id) {
        this.id = id;
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

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public Boolean getEtapeValid() {
        return etapeValid != null ? etapeValid : false;
    }

    public void setEtapeValid(Boolean etapeValid) {
        this.etapeValid = etapeValid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdDistant() {
        return idDistant;
    }

    public void setIdDistant(long idDistant) {
        this.idDistant = idDistant;
    }

    public YvsWorkflowEtapeValidation getEtape() {
        return etape;
    }

    public void setEtape(YvsWorkflowEtapeValidation etape) {
        this.etape = etape;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public YvsComDocAchats getFactureAchat() {
        return factureAchat;
    }

    public void setFactureAchat(YvsComDocAchats factureAchat) {
        this.factureAchat = factureAchat;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
    }

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public int getEtapeValide() {
        return etapeValide;
    }

    public void setEtapeValide(int etapeValide) {
        this.etapeValide = etapeValide;
    }

    public Integer getOrdreEtape() {
        return ordreEtape != null ? ordreEtape : 0;
    }

    public void setOrdreEtape(Integer ordreEtape) {
        this.ordreEtape = ordreEtape;
    }

    public YvsWorkflowValidFactureAchat getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(YvsWorkflowValidFactureAchat etapeSuivante) {
        this.etapeSuivante = etapeSuivante;
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
        if (!(object instanceof YvsWorkflowValidFactureAchat)) {
            return false;
        }
        YvsWorkflowValidFactureAchat other = (YvsWorkflowValidFactureAchat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowValidFactureAchat[ id=" + id + " ]";
    }

    @Override
    public int compareTo(YvsWorkflowValidFactureAchat o) {
        if (getOrdreEtape().equals(o.getOrdreEtape())) {
            return getId().compareTo(o.getId());
        }
        return getOrdreEtape().compareTo(o.getOrdreEtape());
    }

    public static List<YvsWorkflowValidFactureAchat> ordonneEtapes(List<YvsWorkflowValidFactureAchat> list) {
        if (list != null ? !list.isEmpty() : false) {
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    list.get(i).setEtapeSuivante(list.get(i + 1));
                }
            }
            int index = 0;
            //toutes les étapes ont été construite
            YvsWorkflowValidFactureAchat first = list.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                list.get(0).setEtapeActive(true);
            }
            for (int i = 0; i < list.size(); i++) {
                if (first.getEtapeSuivante() != null && list.get(i).getEtapeValid()) {
                    index++;
                    if (list.size() > index) {
                        //active l'etape suivante
                        list.get(index).setEtapeActive(true);
                        first = list.get(index);
                    }
                }
                if (list.get(i).getEtape().equals(list.get(list.size() - 1).getEtape()) && !list.get(i).getEtapeValid()) {
                    if ((list.size() - 2) >= 0) {
                        if (list.get(list.size() - 2).getEtapeValid()) {
                            list.get(i).setEtapeActive(true);
                        }
                    }
                }
            }
        }
        return list;
    }

}
