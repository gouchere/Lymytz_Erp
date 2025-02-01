/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.production.planification.view;

/**
 *
 * @author hp Elite 8300
 */
public class CoordoneeTablePic {

    private int indexFamille;
    private int indexRubrique;
    private int indexVal;

    public CoordoneeTablePic() {
    }

    public CoordoneeTablePic(int indexFamille, int indexRubrique, int indexVal) {
        this.indexFamille = indexFamille;
        this.indexRubrique = indexRubrique;
        this.indexVal = indexVal;
    }

    public int getIndexFamille() {
        return indexFamille;
    }

    public void setIndexFamille(int indexFamille) {
        this.indexFamille = indexFamille;
    }

    public int getIndexRubrique() {
        return indexRubrique;
    }

    public void setIndexRubrique(int indexRubrique) {
        this.indexRubrique = indexRubrique;
    }

    public int getIndexVal() {
        return indexVal;
    }

    public void setIndexVal(int indexVal) {
        this.indexVal = indexVal;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + this.indexFamille;
        hash = 61 * hash + this.indexRubrique;
        hash = 61 * hash + this.indexVal;
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
        final CoordoneeTablePic other = (CoordoneeTablePic) obj;
        if (this.indexFamille != other.indexFamille) {
            return false;
        }
        if (this.indexRubrique != other.indexRubrique) {
            return false;
        }
        if (this.indexVal != other.indexVal) {
            return false;
        }
        return true;
    }

}
