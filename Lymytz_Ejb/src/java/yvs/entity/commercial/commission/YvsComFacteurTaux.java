/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.entity.commercial.commission;

import java.io.Serializable;
import java.util.ArrayList; 
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.dao.services.DateTimeAdapter; import com.fasterxml.jackson.annotation.JsonFormat;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author Lymytz Dowes
 */
@Entity
@Table(name = "yvs_com_facteur_taux")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "YvsComFacteurTaux.findAll", query = "SELECT y FROM YvsComFacteurTaux y"),
    @NamedQuery(name = "YvsComFacteurTaux.findById", query = "SELECT y FROM YvsComFacteurTaux y WHERE y.id = :id"),
    @NamedQuery(name = "YvsComFacteurTaux.findByGeneral", query = "SELECT y FROM YvsComFacteurTaux y WHERE y.general = :general"),
    @NamedQuery(name = "YvsComFacteurTaux.findByNouveauClient", query = "SELECT y FROM YvsComFacteurTaux y WHERE y.nouveauClient = :nouveauClient"),
    @NamedQuery(name = "YvsComFacteurTaux.findByTaux", query = "SELECT y FROM YvsComFacteurTaux y WHERE y.taux = :taux"),
    @NamedQuery(name = "YvsComFacteurTaux.findByBase", query = "SELECT y FROM YvsComFacteurTaux y WHERE y.base = :base"),
    @NamedQuery(name = "YvsComFacteurTaux.findByChampApplication", query = "SELECT y FROM YvsComFacteurTaux y WHERE y.champApplication = :champApplication")})
public class YvsComFacteurTaux implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(sequenceName = "yvs_com_facteur_taux_id_seq", name = "yvs_com_facteur_taux_id_seq_name", allocationSize = 1)
    @GeneratedValue(generator = "yvs_com_facteur_taux_id_seq_name", strategy = GenerationType.SEQUENCE)  
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUpdate;
    @Column(name = "date_save")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSave;
    @Column(name = "general")
    private Boolean general;
    @Column(name = "permanent")
    private Boolean permanent;
    @Column(name = "nouveau_client")
    private Boolean nouveauClient;
    @Column(name = "actif")
    private Boolean actif;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "base")
    private Character base;
    @Column(name = "champ_application")
    private Character champApplication;
    @JoinColumn(name = "plan", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComPlanCommission plan;
    @JoinColumn(name = "author", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsUsersAgence author;
    @JoinColumn(name = "type_grille", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private YvsComTypeGrille typeGrille;
    @OneToMany(mappedBy = "facteur")
    private List<YvsComCibleFacteurTaux> cibles;
    @OneToMany(mappedBy = "facteur")
    private List<YvsComPeriodeValiditeCommision> periodicites;
    @Transient
    private boolean new_;
    @Transient
    private boolean select;
    @Transient
    private boolean byGrille;
    @Transient
    private Date dateDebut = new Date();
    @Transient
    private Date dateFin = new Date();
    @Transient
    private char periodicite;

    public YvsComFacteurTaux() {
        cibles = new ArrayList<>();
        periodicites = new ArrayList<>();
    }

    public YvsComFacteurTaux(Long id) {
        this();
        this.id = id;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateUpdate() {
        return dateUpdate != null ? dateUpdate : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateSave() {
        return dateSave != null ? dateSave : new Date();
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getPermanent() {
        return permanent != null ? permanent : false;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public Boolean getGeneral() {
        return general != null ? general : false;
    }

    public void setGeneral(Boolean general) {
        this.general = general;
    }

    public Boolean getNouveauClient() {
        return nouveauClient != null ? nouveauClient : false;
    }

    public void setNouveauClient(Boolean nouveauClient) {
        this.nouveauClient = nouveauClient;
    }

    public Boolean getActif() {
        return actif != null ? actif : false;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Double getTaux() {
        return taux != null ? taux : 0;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Character getBase() {
        return base != null ? String.valueOf(base).trim().length() > 0 ? base : 'M' : 'M';
    }

    public void setBase(Character base) {
        this.base = base;
    }

    public Character getChampApplication() {
        return champApplication != null ? String.valueOf(champApplication).trim().length() > 0 ? champApplication : 'R' : 'R';
    }

    public void setChampApplication(Character champApplication) {
        this.champApplication = champApplication;
    }

    public YvsUsersAgence getAuthor() {
        return author;
    }

    public void setAuthor(YvsUsersAgence author) {
        this.author = author;
    }

    public boolean isByGrille() {
        byGrille = false;
        if (typeGrille != null ? typeGrille.getId() > 0 : false) {
            byGrille = true;
        }
        return byGrille;
    }

    public void setByGrille(boolean byGrille) {
        this.byGrille = byGrille;
    }

    public YvsComTypeGrille getTypeGrille() {
        return typeGrille;
    }

    public void setTypeGrille(YvsComTypeGrille typeGrille) {
        this.typeGrille = typeGrille;
    }

    public YvsComPlanCommission getPlan() {
        return plan;
    }

    public void setPlan(YvsComPlanCommission plan) {
        this.plan = plan;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public char getPeriodicite() {
        periodicite = 'P';
        if (!getPermanent()) {
            if (periodicites != null ? !periodicites.isEmpty() : false) {
                periodicite = periodicites.get(0).getPeriodicite();
            }
        }
        return periodicite;
    }

    public void setPeriodicite(char periodicite) {
        this.periodicite = periodicite;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateDebut() {
        dateDebut = new Date();
        if (!getPermanent()) {
            if (periodicites != null ? !periodicites.isEmpty() : false) {
                dateDebut = periodicites.get(0).getDateDebut();
            }
        }
        return dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getDateFin() {
        dateFin = new Date();
        if (!getPermanent()) {
            if (periodicites != null ? !periodicites.isEmpty() : false) {
                dateFin = periodicites.get(0).getDateFin();
            }
        }
        return dateFin;
    }

    @XmlJavaTypeAdapter(DateTimeAdapter.class) @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<YvsComPeriodeValiditeCommision> getPeriodicites() {
        return periodicites;
    }

    public void setPeriodicites(List<YvsComPeriodeValiditeCommision> periodicites) {
        this.periodicites = periodicites;
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
        if (!(object instanceof YvsComFacteurTaux)) {
            return false;
        }
        YvsComFacteurTaux other = (YvsComFacteurTaux) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "yvs.entity.commercial.commission.YvsComFacteurTaux[ id=" + id + " ]";
    }

    public List<YvsComCibleFacteurTaux> getCibles() {
        return cibles;
    }

    public void setCibles(List<YvsComCibleFacteurTaux> cibles) {
        this.cibles = cibles;
    }

}
