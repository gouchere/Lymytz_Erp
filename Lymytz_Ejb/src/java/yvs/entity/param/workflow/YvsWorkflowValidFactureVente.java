/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.param.workflow;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.io.Serializable;
import java.util.Collections;
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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.YvsEntity;
import yvs.dao.services.DateTimeAdapter;
import yvs.entity.commercial.vente.YvsComDocVentes;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author LENOVO
 */
@Entity
@Table(name = "yvs_workflow_valid_facture_vente")
@NamedQueries({
    @NamedQuery(name = "YvsWorkflowValidFactureVente.findAll", query = "SELECT y FROM YvsWorkflowValidFactureVente y"),
    @NamedQuery(name = "YvsWorkflowValidFactureVente.findByFacture", query = "SELECT DISTINCT y FROM YvsWorkflowValidFactureVente y "
            + "JOIN FETCH y.etape LEFT JOIN FETCH y.etape.etapeSuivante WHERE y.factureVente = :facture ORDER BY y.ordreEtape"),
    @NamedQuery(name = "YvsWorkflowValidFactureVente.findByEtapeFacture", query = "SELECT y FROM YvsWorkflowValidFactureVente y WHERE y.factureVente = :facture AND y.etape = :etape"),
    @NamedQuery(name = "YvsWorkflowValidFactureVente.findByIDEtapeFacture", query = "SELECT y.id FROM YvsWorkflowValidFactureVente y WHERE y.factureVente = :facture AND y.etape = :etape"),
    @NamedQuery(name = "YvsWorkflowValidFactureVente.findByEtapeValid", query = "SELECT y FROM YvsWorkflowValidFactureVente y WHERE y.etapeValid = :etapeValid"),
    @NamedQuery(name = "YvsWorkflowValidFactureVente.findById", query = "SELECT y FROM YvsWorkflowValidFactureVente y WHERE y.id = :id")})
public class YvsWorkflowValidFactureVente extends YvsEntity implements Serializable, Comparable<YvsWorkflowValidFactureVente> {

    private static final long serialVersionUID = 1L;
    @Column(name = "etape_valid")
    private Boolean etapeValid;
    @Id
    @SequenceGenerator(sequenceName = "yvs_workflow_valid_facture_vente_id_seq", name = "yvs_workflow_valid_facture_vente_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_workflow_valid_facture_vente_id_seq_name", strategy = GenerationType.SEQUENCE)
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
    @JoinColumn(name = "facture_vente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComDocVentes factureVente;
    @Transient
    private YvsWorkflowValidFactureVente etapeSuivante;
    @Transient
    private boolean etapeActive;
    @Transient
    private int etapeValide;
    
    public YvsWorkflowValidFactureVente() {
    }

    public YvsWorkflowValidFactureVente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateUpdate() {
        return dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public Date getDateSave() {
        return dateSave;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class)
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Boolean getEtapeValid() {
        return etapeValid;
    }

    public void setEtapeValid(Boolean etapeValid) {
        this.etapeValid = etapeValid;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
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

    public YvsComDocVentes getFactureVente() {
        return factureVente;
    }

    public void setFactureVente(YvsComDocVentes factureVente) {
        this.factureVente = factureVente;
    }

    public boolean isEtapeActive() {
        return etapeActive;
    }

    public void setEtapeActive(boolean etapeActive) {
        this.etapeActive = etapeActive;
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

    public YvsWorkflowValidFactureVente getEtapeSuivante() {
        return etapeSuivante;
    }

    public void setEtapeSuivante(YvsWorkflowValidFactureVente etapeSuivante) {
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
        if (!(object instanceof YvsWorkflowValidFactureVente)) {
            return false;
        }
        YvsWorkflowValidFactureVente other = (YvsWorkflowValidFactureVente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.param.workflow.YvsWorkflowValidFactureVente[ id=" + id + " ]";
    }

    @Override
    public int compareTo(YvsWorkflowValidFactureVente o) {
        if (getOrdreEtape().equals(o.getOrdreEtape())) {
            return getId().compareTo(o.getId());
        }
        return getOrdreEtape().compareTo(o.getOrdreEtape());
    }

    public static List<YvsWorkflowValidFactureVente> ordonneEtapes(List<YvsWorkflowValidFactureVente> list) {
        if (list != null ? !list.isEmpty() : false) {
            Collections.sort(list);
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    list.get(i).setEtapeSuivante(list.get(i + 1));
                }
            }
            int i = 0;
            //toutes les étapes ont été construite
            YvsWorkflowValidFactureVente first = list.get(0);
            //si la première étape n'est pas validé, on l'active
            if (!first.getEtapeValid()) {
                list.get(0).setEtapeActive(true);
            }
            for (YvsWorkflowValidFactureVente vm : list) {
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

}
