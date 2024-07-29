/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.entity.compta.YvsBaseRadicalCompte;
import yvs.util.Constantes;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class NatureCompte implements Serializable {

    private long id;
    private String designation;
    private String typeReport;
    private String sensCompte = "A";
    private String nature = Constantes.NAT_AUTRE;
    private boolean saisieAnal;
    private boolean saisieEcheance;
    private boolean saisieCompteTiers;
    private boolean lettrable;
    private boolean actif = true;
    private Date dateSave = new Date();
    private List<YvsBaseRadicalCompte> radicals;

    public NatureCompte() {
        radicals = new ArrayList<>();
    }

    public NatureCompte(long id, String designation) {
        this.id = id;
        this.designation = designation;
        radicals = new ArrayList<>();
    }

    public String getDesignation() {
        return designation;
    }

    public String getNature() {
        return nature != null ? !nature.trim().isEmpty() ? nature : Constantes.NAT_AUTRE : Constantes.NAT_AUTRE;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public void setDesignation(String desination) {
        this.designation = desination;
    }

    public boolean isLettrable() {
        return lettrable;
    }

    public void setLettrable(boolean lettrable) {
        this.lettrable = lettrable;
    }

    public List<YvsBaseRadicalCompte> getRadicals() {
        return radicals;
    }

    public void setRadicals(List<YvsBaseRadicalCompte> radicals) {
        this.radicals = radicals;
    }

    public boolean isSaisieAnal() {
        return saisieAnal;
    }

    public void setSaisieAnal(boolean saisieAnal) {
        this.saisieAnal = saisieAnal;
    }

    public boolean isSaisieCompteTiers() {
        return saisieCompteTiers;
    }

    public void setSaisieCompteTiers(boolean saisieCompteTiers) {
        this.saisieCompteTiers = saisieCompteTiers;
    }

    public boolean isSaisieEcheance() {
        return saisieEcheance;
    }

    public void setSaisieEcheance(boolean saisieEcheance) {
        this.saisieEcheance = saisieEcheance;
    }

    public String getTypeReport() {
        return typeReport;
    }

    public void setTypeReport(String typeReport) {
        this.typeReport = typeReport;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSensCompte() {
        return sensCompte;
    }

    public void setSensCompte(String abbreviation) {
        this.sensCompte = abbreviation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    /**
     * *Logique metier*
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NatureCompte other = (NatureCompte) obj;
        if (!Objects.equals(this.designation, other.designation)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 31 * hash + Objects.hashCode(this.designation);
        return hash;
    }
}
