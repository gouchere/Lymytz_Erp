/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage.doc;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author GOUCHERE YVES
 */
public abstract class ConfigDoc {

    public ConfigDoc() {
    }        
    /*
     * le format doit être de la forme: PREFIXE-?MOIS-?ANNE-NUMERO
     */
    @XStreamAlias("format")    
    private String format;
    /*
     * le format doit être de la forme: NUMPIECE?(/FOURNISSEUR)?(/AGENCE)
     */
    @XStreamAlias("formatRef")    
    private String formatRef;
    @XStreamAlias("escompte")
    private double escompte;
    @XStreamAlias("prefix")
    private String prefixe="FA";
    @XStreamAlias("nbDigit")
    private int nbDigit=4;
    @XStreamAlias("month")
    private boolean month=true;
    @XStreamAlias("year")
    private boolean year=true;
    @XStreamAlias("fournisseur")
    private boolean fournisseur=true;    

    public double getEscompte() {
        return escompte;
    }

    public void setEscompte(double escompte) {
        this.escompte = escompte;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormatRef() {
        return formatRef;
    }

    public void setFormatRef(String formatRef) {
        this.formatRef = formatRef;
    }

    public int getNbDigit() {
        return nbDigit;
    }

    public String getPrefixe() {
        return prefixe;
    }

    public void setFournisseur(boolean fournisseur) {
        this.fournisseur = fournisseur;
    }

    public void setMonth(boolean month) {
        this.month = month;
    }

    public void setNbDigit(int nbDigit) {
        this.nbDigit = nbDigit;
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
    }

    public void setYear(boolean year) {
        this.year = year;
    }

    public boolean isFournisseur() {
        return fournisseur;
    }

    public boolean isMonth() {
        return month;
    }

    public boolean isYear() {
        return year;
    }
}
