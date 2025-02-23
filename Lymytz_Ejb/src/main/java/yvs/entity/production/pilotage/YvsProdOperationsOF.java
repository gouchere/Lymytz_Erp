/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.production.pilotage;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.production.base.YvsProdPosteCharge;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
@Entity
@Table(name = "yvs_prod_operations_of")
@NamedQueries({
    @NamedQuery(name = "YvsProdOperationsOF.findAll", query = "SELECT y FROM YvsProdOperationsOF y"),
    @NamedQuery(name = "YvsProdOperationsOF.findById", query = "SELECT y FROM YvsProdOperationsOF y WHERE y.id = :id"),
    @NamedQuery(name = "YvsProdOperationsOF.findByOF", query = "SELECT DISTINCT y FROM YvsProdOperationsOF y JOIN FETCH y.ordreFabrication LEFT JOIN y.composants CO WHERE y.ordreFabrication = :ordre ORDER BY y.numero ASC"),
    @NamedQuery(name = "YvsProdOperationsOF.findByCommentaire", query = "SELECT y FROM YvsProdOperationsOF y WHERE y.commentaire = :commentaire")})
public class YvsProdOperationsOF implements Serializable, Comparator<YvsProdOperationsOF> {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_prod_phase_ordre_fabrication_id_seq", name = "yvs_prod_phase_ordre_fabrication_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_prod_phase_ordre_fabrication_id_seq_name", strategy = GenerationType.SEQUENCE)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "temps_reglage")
    private Double tempsReglage;
    @Column(name = "temps_operation")
    private Double tempsOperation;
    @Column(name = "date_debut")
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    @Column(name = "date_fin")
    @Temporal(TemporalType.DATE)
    private Date dateFin;
    @Column(name = "heure_debut")
    @Temporal(TemporalType.TIME)
    private Date heureDebut;
    @Column(name = "heure_fin")
    @Temporal(TemporalType.TIME)
    private Date heureFin;
    @Column(name = "numero")
    private Integer numero;
    @Size(max = 2147483647)
    @Column(name = "code_ref")
    private String codeRef;
    @Size(max = 2147483647)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "statut_op")
    private String statutOp;
    @Column(name = "nb_machine")
    private Integer nbMachine;
    @Column(name = "nb_main_oeuvre")
    private Integer nbMainOeuvre;
    @Column(name = "type_cout")
    private Character typeCout = 'T';
   
    @JoinColumn(name = "main_oeuvre", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge mainOeuvre;
    @JoinColumn(name = "machine", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdPosteCharge machine;
    @JoinColumn(name = "ordre_fabrication", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsProdOrdreFabrication ordreFabrication;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;

    @OneToMany(mappedBy = "operation", fetch = FetchType.LAZY, cascade = {})
    private List<YvsProdFluxComposant> composants;

    @Transient
    private List<YvsProdSuiviOperations> suiviOperations;
    @Transient
    private YvsProdOfIndicateurSuivi indicateur;

    public YvsProdOperationsOF() {
        composants = new ArrayList<>();
    }

    public YvsProdOperationsOF(Long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id != null ? id : 0;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public String getCommentaire() {
        return commentaire != null ? commentaire : "";
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public YvsProdOrdreFabrication getOrdreFabrication() {
        return ordreFabrication;
    }

    public void setOrdreFabrication(YvsProdOrdreFabrication ordreFabrication) {
        this.ordreFabrication = ordreFabrication;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public String getStatutOp() {
        return statutOp != null ? statutOp.trim().length() > 0 ? statutOp : Constantes.ETAT_ATTENTE : Constantes.ETAT_ATTENTE;
    }

    public void setStatutOp(String statutOp) {
        this.statutOp = statutOp;
    }

    public List<YvsProdFluxComposant> getComposants() {
        return composants;
    }

    public void setComposants(List<YvsProdFluxComposant> composants) {
        this.composants = composants;
    }

    public Double getTempsReglage() {
        return tempsReglage != null ? tempsReglage : 0;
    }

    public void setTempsReglage(Double tempsReglage) {
        this.tempsReglage = tempsReglage;
    }

    public Double getTempsOperation() {
        return tempsOperation != null ? tempsOperation : 0;
    }

    public void setTempsOperation(Double tempsOperation) {
        this.tempsOperation = tempsOperation;
    }

    public Date getDateDebut() {
        return dateDebut != null ? dateDebut : new Date();
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin != null ? dateFin : new Date();
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getHeureDebut() {
        return heureDebut != null ? heureDebut : new Date();
    }

    public void setHeureDebut(Date heureDebut) {
        this.heureDebut = heureDebut;
    }

    public Date getHeureFin() {
        return heureFin != null ? heureFin : new Date();
    }

    public void setHeureFin(Date heureFin) {
        this.heureFin = heureFin;
    }

    public Integer getNumero() {
        return numero != null ? numero : 0;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCodeRef() {
        return codeRef != null ? codeRef : "";
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public YvsProdPosteCharge getMainOeuvre() {
        return mainOeuvre;
    }

    public void setMainOeuvre(YvsProdPosteCharge mainOeuvre) {
        this.mainOeuvre = mainOeuvre;
        if (mainOeuvre == null) {
            setNbMainOeuvre(0);
        }
    }

    public YvsProdPosteCharge getMachine() {
        return machine;
    }

    public void setMachine(YvsProdPosteCharge machine) {
        this.machine = machine;
        if (machine == null) {
            setNbMachine(0);
        }
    }

    public Integer getNbMachine() {
        return nbMachine != null ? nbMachine : 0;
    }

    public void setNbMachine(Integer nbMachine) {
        this.nbMachine = nbMachine;
    }

    public Integer getNbMainOeuvre() {
        return nbMainOeuvre != null ? nbMainOeuvre : 0;
    }

    public void setNbMainOeuvre(Integer nbMainOeuvre) {
        this.nbMainOeuvre = nbMainOeuvre;
    }

    public YvsProdOfIndicateurSuivi getIndicateur() {
        return indicateur;
    }

    public void setIndicateur(YvsProdOfIndicateurSuivi indicateur) {
        this.indicateur = indicateur;
    }

    public List<YvsProdSuiviOperations> getSuiviOperations() {
        return suiviOperations;
    }

    public void setSuiviOperations(List<YvsProdSuiviOperations> suiviOperations) {
        this.suiviOperations = suiviOperations;
    }

    /*Renvoie le niveau où l'on se trouve en fonction des indicateurs*/
    public YvsProdOfIndicateurSuivi getIndicateurOp() {
        for (YvsProdFluxComposant flux : composants) {
            if (flux.getIndicateursSuivis() != null) {
                int line = 0;
                if (flux.getIndicateursSuivis() != null) {
                    Collections.sort(flux.getIndicateursSuivis(), new YvsProdOfIndicateurSuivi());
                }
                for (YvsProdOfIndicateurSuivi i : flux.getIndicateursSuivis()) {
                    double quatiteSave = flux.getQuantiteFlux();
                    if (quatiteSave <= i.getQuantiteBorne()) {
                        return i;
                    }
                    line++;
                }
                if (line == flux.getIndicateursSuivis().size() && flux.getIndicateursSuivis().size() > 0) {
                    if (flux.getQuantiteFlux() > flux.getIndicateursSuivis().get(line - 1).getQuantiteBorne()) {
                        return flux.getIndicateursSuivis().get(line - 1);
                    }
                }
            }
        }
        return null;
    }

    public String writeTexteStatut() {
        if (statutOp != null) {
            switch (statutOp) {
                case Constantes.ETAT_ATTENTE:
                    return "En Attente";
                case Constantes.ETAT_ENCOURS:
                    return "En Cours";
                case Constantes.ETAT_TERMINE:
                    return "Terminé";
                default:
                    return "";
            }
        } else {
            return "";
        }
    }

    public Character getTypeCout() {
        return typeCout != null ? typeCout : 'T';
    }

    public void setTypeCout(Character typeCout) {
        this.typeCout = typeCout;
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
        if (!(object instanceof YvsProdOperationsOF)) {
            return false;
        }
        YvsProdOperationsOF other = (YvsProdOperationsOF) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.production.pilotage.YvsProdPhaseOrdreFabrication[ id=" + id + " ]";
    }

    @Override
    public int compare(YvsProdOperationsOF o1, YvsProdOperationsOF o2) {
        if (o1 != null && o2 != null) {
            return o1.getNumero().compareTo(o2.getNumero());
        } else if (o1 != null) {
            return 1;
        } else {
            return -1;

        }
    }

}
