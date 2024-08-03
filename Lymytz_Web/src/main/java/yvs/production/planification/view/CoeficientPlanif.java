/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

import java.io.Serializable;import yvs.entity.compta.YvsBaseCaisse;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author hp Elite 8300
 */
@ManagedBean
@SessionScoped
public class CoeficientPlanif implements Serializable {

    private long id;
    private double value;
    private TypeCoeficient type = new TypeCoeficient();
    private ObjectData valeurPic;
    private boolean apply; //indique si le coefficient a déjà été appliqué au montant de la vente
    private boolean percent = true; //précise si le coeficient est une valeur ou un pourcentage!
    private boolean alreadySave;

    public CoeficientPlanif() {
    }

    public CoeficientPlanif(long id, double value) {
        this.id = id;
        this.value = value;
    }

    public CoeficientPlanif(long id, double value, TypeCoeficient type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public TypeCoeficient getType() {
        return type;
    }

    public void setType(TypeCoeficient type) {
        this.type = type;
    }

    public ObjectData getValeurPic() {
        return valeurPic;
    }

    public void setValeurPic(ObjectData valeurPic) {
        this.valeurPic = valeurPic;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public boolean isPercent() {
        return percent;
    }

    public void setPercent(boolean percent) {
        this.percent = percent;
    }

    public boolean isAlreadySave() {
        return alreadySave;
    }

    public void setAlreadySave(boolean alreadySave) {
        this.alreadySave = alreadySave;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CoeficientPlanif other = (CoeficientPlanif) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
