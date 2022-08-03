/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.compta;

/**
 *
 * @author hp Elite 8300 récupère de manière temporaire les éléments de la
 * requete JDBC pour la construction du contenu journal
 */
public class TempContent {

    private long id;
    private Long externe;
    private Double montantGains;
    private Double retenueS;
    private Double retenueP;
    private Long compteCollectif;
    private Long compteTiers;
    private Long compteCharge;
    private Long compteCotisation;
    private String element, autre, mode;
    private Boolean saisieTiers;
    private Boolean saisieAnalytique;

    public TempContent() {
    }

    public TempContent(long id, Double montantGains, Double retenueS, Double retenueP, Long compteCollectif, Long compteTiers, Long compteCharge, Long compteCotisation, String element, Boolean saisieTiers) {
        this(id, montantGains, retenueS, retenueP, compteCollectif, compteTiers, compteCharge, compteCotisation, element, saisieTiers, false);
    }

    public TempContent(long id, Double montantGains, Double retenueS, Double retenueP, Long compteCollectif, Long compteTiers, Long compteCharge, Long compteCotisation, String element, Boolean saisieTiers, Boolean saisieAnalytique) {
        this.id = id;
        this.montantGains = montantGains;
        this.retenueS = retenueS;
        this.retenueP = retenueP;
        this.compteCollectif = compteCollectif;
        this.compteTiers = compteTiers;
        this.compteCharge = compteCharge;
        this.compteCotisation = compteCotisation;
        this.element = element;
        this.saisieTiers = saisieTiers;
        this.saisieAnalytique = saisieAnalytique;
    }

    public Long getExterne() {
        return externe != null ? externe : 0;
    }

    public void setExterne(Long externe) {
        this.externe = externe;
    }

    public String getMode() {
        return mode != null ? mode : "";
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getAutre() {
        return autre;
    }

    public void setAutre(String autre) {
        this.autre = autre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getMontantGains() {
        return montantGains != null ? montantGains : 0.0;
    }

    public void setMontantGains(Double montantGains) {
        this.montantGains = montantGains;
    }

    public Double getRetenueS() {
        return retenueS != null ? retenueS : 0.0;
    }

    public void setRetenueS(Double retenueS) {
        this.retenueS = retenueS;
    }

    public Double getRetenueP() {
        return retenueP != null ? retenueP : 0.0;
    }

    public void setRetenueP(Double retenueP) {
        this.retenueP = retenueP;
    }

    public Long getCompteCollectif() {
        return compteCollectif != null ? compteCollectif : 0;
    }

    public void setCompteCollectif(Long compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public Long getCompteTiers() {
        return compteTiers != null ? compteTiers : 0;
    }

    public void setCompteTiers(Long compteTiers) {
        this.compteTiers = compteTiers;
    }

    public Long getCompteCharge() {
        return compteCharge != null ? compteCharge : 0;
    }

    public void setCompteCharge(Long compteCharge) {
        this.compteCharge = compteCharge;
    }

    public Long getCompteCotisation() {
        return compteCotisation != null ? compteCotisation : 0;
    }

    public void setCompteCotisation(Long compteCotisation) {
        this.compteCotisation = compteCotisation;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public Boolean isSaisieTiers() {
        return saisieTiers != null ? saisieTiers : false;
    }

    public void setSaisieTiers(Boolean saisieTiers) {
        this.saisieTiers = saisieTiers;
    }

    public Boolean isSaisieAnalytique() {
        return saisieAnalytique != null ? saisieAnalytique : false;
    }

    public void setSaisieAnalytique(Boolean saisieAnalytique) {
        this.saisieAnalytique = saisieAnalytique;
    }

}
