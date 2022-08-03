/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.compta.divers.YvsComptaCaisseDocDivers;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_workflow_valid_doc_caisse")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowValidDocCaisse.findAll", query = "SELECT y FROM YvsWorkflowValidDocCaisse y ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidDocCaisse.findByFacture", query = "SELECT y FROM YvsWorkflowValidDocCaisse y WHERE y.docCaisse = :facture ORDER BY y.ordreEtape ASC"),
    @NamedQuery(name = "YvsWorkflowValidDocCaisse.findByEtapeFacture", query = "SELECT y FROM YvsWorkflowValidDocCaisse y WHERE y.docCaisse = :facture AND y.etape = :etape ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidDocCaisse.findByIDEtapeFacture", query = "SELECT y.id FROM YvsWorkflowValidDocCaisse y WHERE y.docCaisse = :facture AND y.etape = :etape ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidDocCaisse.findByEtapeValid", query = "SELECT y FROM YvsWorkflowValidDocCaisse y WHERE y.etapeValid = :etapeValid ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidDocCaisse.findById", query = "SELECT y FROM YvsWorkflowValidDocCaisse y WHERE y.id = :id ORDER BY y.ordreEtape")})
public class YvsWorkflowValidDocCaisse extends YvsEntity implements Serializable, Comparator<YvsWorkflowValidDocCaisse> {

    private static final long serialVersionUID = 1L;
    @Column(name = "etape_valid")
    private Boolean etapeValid;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_valid_doc_caisse_id_seq", name = "yvs_workflow_valid_doc_caisse_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_valid_doc_caisse_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "doc_caisse", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComptaCaisseDocDivers docCaisse;
    @Transient
    private YvsWorkflowValidDocCaisse etapeSuivante;
    @Transient
    private boolean etapeActive;
    @Transient
    private int etapeValide;

    public YvsWorkflowValidDocCaisse() {
    }

    public YvsWorkflowValidDocCaisse(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public Boolean getEtapeValid() {
        return etapeValid;
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

    public YvsComptaCaisseDocDivers getDocCaisse() {
        return docCaisse;
    }

    public void setDocCaisse(YvsComptaCaisseDocDivers docCaisse) {
        this.docCaisse = docCaisse;
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

    @XmlTransient
    @JsonIgnore
    public YvsWorkflowValidDocCaisse getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(YvsWorkflowValidDocCaisse etapeSuivante) {
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
        if (!(object instanceof YvsWorkflowValidDocCaisse)) {
            return false;
        }
        YvsWorkflowValidDocCaisse other = (YvsWorkflowValidDocCaisse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowValidDocCaisse[ id=" + id + " ]";
    }

//    @Override
//    public int compareTo(YvsWorkflowValidDocCaisse o) {
//        if (getOrdreEtape().equals(o.getOrdreEtape())) {
//            return getId().compareTo(o.getId());
//        }
//        return getOrdreEtape().compareTo(o.getOrdreEtape());
//    }
    public static List<YvsWorkflowValidDocCaisse> ordonneEtapes(List<YvsWorkflowValidDocCaisse> list) {
        if (list != null ? !list.isEmpty() : false) {
            Collections.sort(list, new YvsWorkflowValidDocCaisse());
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    list.get(i).setEtapeSuivante(list.get(i + 1));
                }
            }
            int i = 0;
            //toutes les étapes ont été construite
            YvsWorkflowValidDocCaisse first = list.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                list.get(0).setEtapeActive(true);
            }
            for (YvsWorkflowValidDocCaisse vm : list) {
                if (first.getEtapeSuivante() != null && vm.getEtapeValid()) {
                    i++;
                    if (list.size() > i) {
                        //active l'etape suivante
                        list.get(i).setEtapeActive(true);
                        first = list.get(i);
                    }
                }
                if (vm.getEtape().equals(list.get(list.size() - 1).getEtape()) && !vm.getEtapeValid()) {
                    if ((list.size() - 2) >= 0) {
                        if (list.get(list.size() - 2).getEtapeValid()) {
                            vm.setEtapeActive(true);
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public int compare(YvsWorkflowValidDocCaisse o1, YvsWorkflowValidDocCaisse o2) {
        if (o1 != null && o2 != null) {
            return o1.getOrdreEtape() - o2.getOrdreEtape();
        } else if (o1 != null) {
            return 1;
        }
        return -1;
    }

}
