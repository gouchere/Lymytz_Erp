/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.etats;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import yvs.dao.salaire.service.Constantes;
import yvs.entity.commercial.YvsComParametre;
import yvs.etats.commercial.InventairePreparatoire;
import yvs.etats.commercial.JournalVendeur;
import yvs.util.Util;

/**
 *
 * @author Lymytz Dowes
 */
public class Gestionnaire implements Serializable, Cloneable {

    protected DateFormat fmdMy = new SimpleDateFormat("dd MMM yyyy");

    protected List<String> principaux = new ArrayList<>(); //Modélise les indicateurs
    protected List<String> titres = new ArrayList<>();
    protected List<String> periodes = new ArrayList<>();  //Modélise les colonnes du tableau à n dimension
    protected List<Long> elements = new ArrayList<>();    //Modélise les lignes du tableau à n dimension
    protected List<Long> elementsFinds = new ArrayList<>();
    protected List<Long> elementsTotaux = new ArrayList<>();
    protected List<Rows> lignes = new ArrayList<>();
    protected List<Rows> lignesFind = new ArrayList<>();
    protected List<Rows> lignesTotaux = new ArrayList<>();
    protected List<Columns> groupes = new ArrayList<>();
    protected List<Columns> colonnes = new ArrayList<>();
    protected List<JournalVendeur> valeurs = new ArrayList<>();
    protected List<JournalVendeur> valeursTotaux = new ArrayList<>(), valeursTotauxRow = new ArrayList<>(), valeursTotauxColumn = new ArrayList<>();
    protected List<JournalVendeur> valeursTotauxSave = new ArrayList<>(), valeursTotauxRowSave = new ArrayList<>(), valeursTotauxColumnSave = new ArrayList<>();
    protected List<InventairePreparatoire> inventaires = new ArrayList<>(), filterInventaires = new ArrayList<>();
    protected List<ValeurComptable> comptables = new ArrayList<>();
    protected ValeurComptable totalPeriodique = new ValeurComptable();
    protected ValeurComptable totalGeneral = new ValeurComptable();
    protected List<Dashboards> sous = new ArrayList<>();
    protected String type = "ca", titre = "Tableau de bord", typesTotaux, tauxTotaux;
    protected String varSearch;
    protected double debitComptaResume, creditComptaResume, valueSumRow = 0, intermediaire;

    protected JournalVendeur currentValue;

    protected double totaux = 0;
    protected double sumProg = 0;

    protected long societe, agence, service, employe, point, site, depot, vendeur, client, fournisseur, classe, article, saison, objectif, journal, commercial, famille, groupe;
    protected double coefficient = 1;
    protected long zone;
    protected int ecart, colonne, limit = 0, offset, length;
    protected String editeurs, periode = "M", nature = "A", agences, ordres, descOrdre = "desc", depots, categorie, compteDebut, compteFin, comptes, valorise_by = "V", valoriseMp = "PUA", valoriseMs = "PUV", valorisePf = "PUV", valorisePsf = "PR";
    protected Date dateDebut = new Date(), dateFin = new Date();
    protected boolean displayName, displayAnotherName, containsValue, cumule, byValue, valoriseExcedent;
    protected boolean displayCA = true, displayQte = true, displayMarge = true, displayRevient = true, displayTaux = true;
    protected int cumulBy = 0, vueType;
    protected Boolean lettrer, addTotal;

    protected static YvsComParametre parametreCom;

    public Gestionnaire() {

    }

    public String getEditeurs() {
        return editeurs;
    }

    public void setEditeurs(String editeurs) {
        this.editeurs = editeurs;
    }

    public String getValoriseMp() {
        return valoriseMp;
    }

    public void setValoriseMp(String valoriseMp) {
        this.valoriseMp = valoriseMp;
    }

    public String getValoriseMs() {
        return valoriseMs;
    }

    public void setValoriseMs(String valoriseMs) {
        this.valoriseMs = valoriseMs;
    }

    public String getValorisePf() {
        return valorisePf;
    }

    public void setValorisePf(String valorisePf) {
        this.valorisePf = valorisePf;
    }

    public String getValorisePsf() {
        return valorisePsf;
    }

    public void setValorisePsf(String valorisePsf) {
        this.valorisePsf = valorisePsf;
    }

    public boolean isValoriseExcedent() {
        return valoriseExcedent;
    }

    public void setValoriseExcedent(boolean valoriseExcedent) {
        this.valoriseExcedent = valoriseExcedent;
    }

    public YvsComParametre getParametreCom() {
        return parametreCom;
    }

    public long getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(long fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<Long> getElementsTotaux() {
        return elementsTotaux;
    }

    public void setElementsTotaux(List<Long> elementsTotaux) {
        this.elementsTotaux = elementsTotaux;
    }

    public List<Rows> getLignesTotaux() {
        return lignesTotaux;
    }

    public void setLignesTotaux(List<Rows> lignesTotaux) {
        this.lignesTotaux = lignesTotaux;
    }

    public List<JournalVendeur> getValeursTotauxSave() {
        return valeursTotauxSave;
    }

    public void setValeursTotauxSave(List<JournalVendeur> valeursTotauxSave) {
        this.valeursTotauxSave = valeursTotauxSave;
    }

    public List<JournalVendeur> getValeursTotauxRowSave() {
        return valeursTotauxRowSave;
    }

    public void setValeursTotauxRowSave(List<JournalVendeur> valeursTotauxRowSave) {
        this.valeursTotauxRowSave = valeursTotauxRowSave;
    }

    public List<JournalVendeur> getValeursTotauxColumnSave() {
        return valeursTotauxColumnSave;
    }

    public void setValeursTotauxColumnSave(List<JournalVendeur> valeursTotauxColumnSave) {
        this.valeursTotauxColumnSave = valeursTotauxColumnSave;
    }

    public List<JournalVendeur> getValeursTotaux() {
        return valeursTotaux;
    }

    public void setValeursTotaux(List<JournalVendeur> valeursTotaux) {
        this.valeursTotaux = valeursTotaux;
    }

    public List<JournalVendeur> getValeursTotauxColumn() {
        return valeursTotauxColumn;
    }

    public void setValeursTotauxColumn(List<JournalVendeur> valeursTotauxColumn) {
        this.valeursTotauxColumn = valeursTotauxColumn;
    }

    public List<JournalVendeur> getValeursTotauxRow() {
        return valeursTotauxRow;
    }

    public void setValeursTotauxRow(List<JournalVendeur> valeursTotauxRow) {
        this.valeursTotauxRow = valeursTotauxRow;
    }

    public long getSite() {
        return site;
    }

    public void setSite(long site) {
        this.site = site;
    }

    public double getIntermediaire() {
        return intermediaire;
    }

    public void setIntermediaire(double intermediaire) {
        this.intermediaire = intermediaire;
    }

    public long getClasse() {
        return classe;
    }

    public void setClasse(long classe) {
        this.classe = classe;
    }

    public boolean isDisplayCA() {
        return displayCA;
    }

    public void setDisplayCA(boolean displayCA) {
        this.displayCA = displayCA;
    }

    public boolean isDisplayQte() {
        return displayQte;
    }

    public void setDisplayQte(boolean displayQte) {
        this.displayQte = displayQte;
    }

    public boolean isDisplayMarge() {
        return displayMarge;
    }

    public void setDisplayMarge(boolean displayMarge) {
        this.displayMarge = displayMarge;
    }

    public boolean isDisplayRevient() {
        return displayRevient;
    }

    public void setDisplayRevient(boolean displayRevient) {
        this.displayRevient = displayRevient;
    }

    public boolean isDisplayTaux() {
        return displayTaux;
    }

    public void setDisplayTaux(boolean displayTaux) {
        this.displayTaux = displayTaux;
    }

    public String getValorise_by() {
        return valorise_by;
    }

    public void setValorise_by(String valorise_by) {
        this.valorise_by = valorise_by;
    }

    public boolean isDisplayAnotherName() {
        return displayAnotherName;
    }

    public void setDisplayAnotherName(boolean displayAnotherName) {
        this.displayAnotherName = displayAnotherName;
    }

    public int getVueType() {
        return vueType;
    }

    public void setVueType(int vueType) {
        this.vueType = vueType;
    }

    public List<Long> getElementsFinds() {
        return elementsFinds;
    }

    public void setElementsFinds(List<Long> elementsFinds) {
        this.elementsFinds = elementsFinds;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getValueSumRow() {
        return valueSumRow;
    }

    public void setValueSumRow(double valueSumRow) {
        this.valueSumRow = valueSumRow;
    }

    public JournalVendeur getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(JournalVendeur currentValue) {
        this.currentValue = currentValue;
    }

    public double getDebitComptaResume() {
        return debitComptaResume;
    }

    public void setDebitComptaResume(double debitComptaResume) {
        this.debitComptaResume = debitComptaResume;
    }

    public double getCreditComptaResume() {
        return creditComptaResume;
    }

    public void setCreditComptaResume(double creditComptaResume) {
        this.creditComptaResume = creditComptaResume;
    }

    public List<InventairePreparatoire> getInventaires() {
        return inventaires;
    }

    public void setInventaires(List<InventairePreparatoire> inventaires) {
        this.inventaires = inventaires;
    }

    public List<InventairePreparatoire> getFilterInventaires() {
        return filterInventaires;
    }

    public void setFilterInventaires(List<InventairePreparatoire> filterInventaires) {
        this.filterInventaires = filterInventaires;
    }

    public Boolean getAddTotal() {
        return addTotal;
    }

    public void setAddTotal(Boolean addTotal) {
        this.addTotal = addTotal;
    }

    public long getFamille() {
        return famille;
    }

    public void setFamille(long famille) {
        this.famille = famille;
    }

    public long getGroupe() {
        return groupe;
    }

    public void setGroupe(long groupe) {
        this.groupe = groupe;
    }

    public long getCommercial() {
        return commercial;
    }

    public void setCommercial(long commercial) {
        this.commercial = commercial;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public List<Columns> getGroupes() {
        return groupes;
    }

    public void setGroupes(List<Columns> groupes) {
        this.groupes = groupes;
    }

    public long getDepot() {
        return depot;
    }

    public void setDepot(long depot) {
        this.depot = depot;
    }

    public String getComptes() {
        return comptes;
    }

    public void setComptes(String comptes) {
        this.comptes = comptes;
    }

    public List<ValeurComptable> getComptables() {
        return comptables;
    }

    public void setComptables(List<ValeurComptable> comptables) {
        this.comptables = comptables;
    }

    public ValeurComptable getTotalPeriodique() {
        return totalPeriodique;
    }

    public void setTotalPeriodique(ValeurComptable totalPeriodique) {
        this.totalPeriodique = totalPeriodique;
    }

    public ValeurComptable getTotalGeneral() {
        return totalGeneral;
    }

    public void setTotalGeneral(ValeurComptable totalGeneral) {
        this.totalGeneral = totalGeneral;
    }

    public long getJournal() {
        return journal;
    }

    public void setJournal(long journal) {
        this.journal = journal;
    }

    public boolean isCumule() {
        return cumule;
    }

    public boolean isByValue() {
        return byValue;
    }

    public void setByValue(boolean byValue) {
        this.byValue = byValue;
    }

    public void setCumule(boolean cumule) {
        this.cumule = cumule;
    }

    public String getDescOrdre() {
        return descOrdre;
    }

    public void setDescOrdre(String descOrdre) {
        this.descOrdre = descOrdre;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Boolean getLettrer() {
        return lettrer;
    }

    public void setLettrer(Boolean lettrer) {
        this.lettrer = lettrer;
    }

    public List<Dashboards> getSous() {
        return sous;
    }

    public void setSous(List<Dashboards> sous) {
        this.sous = sous;
    }

    public List<String> getTitres() {
        return titres;
    }

    public void setTitres(List<String> titres) {
        this.titres = titres;
    }

    public String getCompteDebut() {
        return compteDebut;
    }

    public void setCompteDebut(String compteDebut) {
        this.compteDebut = compteDebut;
    }

    public String getCompteFin() {
        return compteFin;
    }

    public void setCompteFin(String compteFin) {
        this.compteFin = compteFin;
    }

    public String getDepots() {
        return depots;
    }

    public void setDepots(String depots) {
        this.depots = depots;
    }

    public String getCategorie() {
        return categorie != null ? categorie : "";
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<String> getPrincipaux() {
        return principaux;
    }

    public void setPrincipaux(List<String> principaux) {
        this.principaux = principaux;
    }

    public double getTotaux() {
        return totaux;
    }

    public void setTotaux(double totaux) {
        this.totaux = totaux;
    }

    public String getOrdres() {
        return ordres;
    }

    public void setOrdres(String ordres) {
        this.ordres = ordres;
    }

    public String getAgences() {
        return agences;
    }

    public void setAgences(String agences) {
        this.agences = agences;
    }

    public boolean isContainsValue() {
        return containsValue;
    }

    public void setContainsValue(boolean containsValue) {
        this.containsValue = containsValue;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public long getSaison() {
        return saison;
    }

    public void setSaison(long saison) {
        this.saison = saison;
    }

    public long getObjectif() {
        return objectif;
    }

    public void setObjectif(long objectif) {
        this.objectif = objectif;
    }

    public boolean isDisplayName() {
        return displayName;
    }

    public void setDisplayName(boolean displayName) {
        this.displayName = displayName;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public long getEmploye() {
        return employe;
    }

    public void setEmploye(long employe) {
        this.employe = employe;
    }

    public long getService() {
        return service;
    }

    public void setService(long service) {
        this.service = service;
    }

    public int getEcart() {
        return ecart;
    }

    public void setEcart(int ecart) {
        this.ecart = ecart;
    }

    public int getColonne() {
        return colonne;
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
    }

    public long getSociete() {
        return societe;
    }

    public void setSociete(long societe) {
        this.societe = societe;
    }

    public long getAgence() {
        return agence;
    }

    public void setAgence(long agence) {
        this.agence = agence;
    }

    public long getZone() {
        return zone;
    }

    public void setZone(long zone) {
        this.zone = zone;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public long getVendeur() {
        return vendeur;
    }

    public void setVendeur(long vendeur) {
        this.vendeur = vendeur;
    }

    public long getClient() {
        return client;
    }

    public void setClient(long client) {
        this.client = client;
    }

    public long getArticle() {
        return article;
    }

    public void setArticle(long article) {
        this.article = article;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public List<Long> getElements() {
        return elements;
    }

    public void setElements(List<Long> elements) {
        this.elements = elements;
    }

    public List<Columns> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<Columns> colonnes) {
        this.colonnes = colonnes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<String> periodes) {
        this.periodes = periodes;
    }

    public List<Rows> getLignes() {
        return lignes;
    }

    public void setLignes(List<Rows> lignes) {
        this.lignes = lignes;
    }

    public List<Rows> getLignesFind() {
        return lignesFind;
    }

    public void setLignesFind(List<Rows> lignesFind) {
        this.lignesFind = lignesFind;
    }

    public List<JournalVendeur> getValeurs() {
        return valeurs;
    }

    public void setValeurs(List<JournalVendeur> valeurs) {
        this.valeurs = valeurs;
    }

    public String getVarSearch() {
        return varSearch;
    }

    public void setVarSearch(String varSearch) {
        this.varSearch = varSearch;
    }

    public int getCumulBy() {
        return cumulBy;
    }

    public void setCumulBy(int cumulBy) {
        this.cumulBy = cumulBy;
    }

    public double getSumProg() {
        return sumProg;
    }

    public void setSumProg(double sumProg) {
        this.sumProg = sumProg;
    }

    public int nombreColonneMarge() {
        int nombre = 0;
        if (displayCA) {
            nombre++;
        }
        if (displayQte) {
            nombre++;
        }
        if (displayMarge) {
            nombre++;
        }
        if (displayRevient) {
            nombre++;
        }
        if (displayTaux) {
            nombre++;
        }
        return nombre;
    }

    public double getTotalProg(int row, int col, String type) {
        if (col < 1) {
            sumProg = 0;
        }
        sumProg += Double.valueOf(valeur(row, col, type).toString());
        return sumProg;
    }

    public JournalVendeur iGet(JournalVendeur valeur, int row, int col) {
        if ((valeur != null ? valeur.getElements() != null : false) && periodes != null) {
            try {
                if (valeur.getElements().size() > row && colonnes.size() > col) {
                    Long element = valeur.getElements().get(row);
                    Columns periode = colonnes.get(col);
                    return iSGet(valeur, element, periode.getValeur().toString());
                }
            } catch (Exception ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new JournalVendeur();
    }

    public JournalVendeur iSGet(JournalVendeur valeur, Long element, String periode) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                int idx = valeur.getSous().indexOf(new JournalVendeur(element, periode));
                if (idx > -1) {
                    return valeur.getSous().get(idx);
                }
            } catch (Exception ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new JournalVendeur();
    }

    public JournalVendeur get(int row, int col) {
        currentValue = null;
        if (elements != null && periodes != null) {
            try {
                if (elements.size() > row && colonnes.size() > col) {
                    Long element = elements.get(row);
                    Columns periode = colonnes.get(col);
                    currentValue = iGet(element, periode.getValeur().toString());
                }
            } catch (Exception ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return currentValue;
    }

    public JournalVendeur iGet(Long element, String periode) {
        JournalVendeur v = null;
        if (elements != null && periodes != null) {
            try {
                int idx = valeurs.indexOf(new JournalVendeur(element, periode));
                if (idx > -1) {
                    v = valeurs.get(idx);
                }
            } catch (Exception ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return v;
    }

    public Object sValeur(JournalVendeur valeur, long element, int col) {
        return sValeur(valeur, element, col, type);
    }

    public Object sValeur(JournalVendeur valeur, long element, int col, String type) {
        if ((valeur != null ? valeur.getElements() != null : false) && periodes != null) {
            try {
                if (colonnes.size() > col) {
                    Columns periode = colonnes.get(col);
                    return iSValeur(valeur, element, periode.getValeur().toString(), type);
                }
            } catch (Exception ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Object valeur(int row, int col) {
        return valeur(row, col, type);
    }

    public Object valeur(int row, int col, String type) {
        return valeur(row, col, "", type);
    }

    public Object valeur(int row, int col, String titre, String type) {
        if (elements != null && periodes != null) {
            try {
                if (elements.size() > row && colonnes.size() > col) {
                    Long element = elements.get(row);
                    Columns periode = colonnes.get(col);
                    return iValeur(element, periode.getValeur().toString(), titre, type);
                }
            } catch (Exception ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Object iValeur(long element, String periode, String type) {
        return iValeur(element, periode, "", type);
    }

    public Object iValeur(long element, String periode, String titre, String type) {
        if (valeurs != null) {
            try {
                int idx = valeurs.indexOf(new JournalVendeur(element, periode, titre));
                if (idx > -1) {
                    return valeurs.get(idx).get(type);
                }
                return 0;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public Object iSValeur(JournalVendeur valeur, long element, String periode, String type) {
        return iSValeur(valeur, element, periode, "", type);
    }

    public Object iSValeur(JournalVendeur valeur, long element, String periode, String titre, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                int idx = valeur.getSous().indexOf(new JournalVendeur(element, periode, titre));
                if (idx > -1) {
                    return valeur.getSous().get(idx).get(type);
                }
                return 0;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double progression(int row) {
        if (elements != null) {
            try {
                if (elements.size() > row) {
                    Long element = elements.get(row);
                    return avg(element, "taux");
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0.0;
    }

    public double avg(long element, String type) {
        if (valeurs != null) {
            try {
                return Double.valueOf(iValeur(element, "TOTAUX", type).toString());
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSum(String type) {
        if (valeurs != null) {
            try {
                double result = 0;
                for (JournalVendeur valeur : valeurs) {
                    result += sSum(valeur, type);
                }
                return result;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSum(JournalVendeur valeur, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                double sum = 0;
                for (int i = 0; i < valeur.getElements().size(); i++) {
                    Long element = valeur.getElements().get(i);
                    Rows e = (Rows) valeur.getLignes().get(i);
                    if (e.getTitres().isEmpty()) {
                        for (Columns periode : colonnes) {
                            sum += Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), type).toString());
                        }
                    } else {
                        for (String titre : e.getTitres()) {
                            for (Columns periode : colonnes) {
                                sum += Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), titre, type).toString());
                            }
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double aSum(String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                for (JournalVendeur v : valeurs) {
                    sum += aSum(v, type);
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double aSum(JournalVendeur valeur, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                double sum = 0;
                for (JournalVendeur v : valeur.getSous()) {
                    sum += Double.valueOf(v.get(type).toString());
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSumProd(JournalVendeur valeur, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                double sum = 0;
                for (int i = 0; i < valeur.getElements().size(); i++) {
                    Long element = valeur.getElements().get(i);
                    Rows e = (Rows) valeur.getLignes().get(i);
                    if (e.getTitres().isEmpty()) {
                        for (Columns periode : colonnes) {
                            double cumule = Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), type).toString());
                            if (e.getTitre().equals(Constantes.CAT_PSF)) {
                                cumule = e.isFooter() ? cumule : -cumule;
                            }
                            sum += cumule;
                        }
                    } else {
                        for (String titre : e.getTitres()) {
                            for (Columns periode : colonnes) {
                                double cumule = Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), titre, type).toString());
                                if (e.getTitre().equals(Constantes.CAT_PSF)) {
                                    cumule = e.isFooter() ? cumule : -cumule;
                                }
                                sum += cumule;
                            }
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sum() {
        return sum(type);
    }

    public double sum(String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                for (int i = 0; i < elements.size(); i++) {
                    Long element = elements.get(i);
                    Rows e = (Rows) lignes.get(i);
                    if (e.getTitres().isEmpty()) {
                        for (Columns periode : colonnes) {
                            if (periode.getValeur() == null) {
                                continue;
                            }
                            sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), type).toString());
                        }
                    } else {
                        for (String titre : e.getTitres()) {
                            for (Columns periode : colonnes) {
                            if (periode.getValeur() == null) {
                                continue;
                            }
                                sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString());
                            }
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sum(String type, String type2) {
        if (valeurs != null) {
            try {
                double sum = 0;
                for (int i = 0; i < elements.size(); i++) {
                    Rows e = (Rows) lignes.get(i);
                    if (e.getTitres().isEmpty()) {
                        sum += sumRow2(i, type, type2);
                    } else {
                        for (String titre : e.getTitres()) {
                            sum += sumRow(i, titre, type, type2);
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSumRow(JournalVendeur valeur, long element) {
        return sSumRow(valeur, element, type);
    }

    public double sSumRow(JournalVendeur valeur, long element, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                double valueSumRow = 0;
                for (int i = 0; i < colonnes.size(); i++) {
                    Columns periode = colonnes.get(i);
                    if (periode.getTitres().isEmpty()) {
                        valueSumRow += Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), type).toString());
                    } else {
                        for (String titre : periode.getTitres()) {
                            valueSumRow += Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), titre, type).toString());
                        }
                    }
                }
                return valueSumRow;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sumRow(int row) {
        return sumRow(row, type);
    }

    public double sumRow(int row, String type) {
        valueSumRow = 0;
        try {
            if (valeurs != null ? !valeurs.isEmpty() : false) {
                Long element = elements.get(row);
                for (int i = 0; i < colonnes.size(); i++) {
                    Columns periode = colonnes.get(i);
                    if (periode.getValeur() == null) {
                        continue;
                    }
                    if (periode.getTitres().isEmpty()) {
                        Object value = iValeur(element, periode.getValeur().toString(), type);
                        valueSumRow += (value != null ? Double.valueOf(value.toString()) : 0);
                    } else {
                        for (String titre : periode.getTitres()) {
                            Object value = iValeur(element, periode.getValeur().toString(), titre, type);
                            valueSumRow += (value != null ? Double.valueOf(value.toString()) : 0);
                        }
                    }
                }
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valueSumRow;
    }

    public double sumRow(int row, String titre, String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Long element = elements.get(row);
                for (Columns periode : colonnes) {
                    sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString());
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sumRow(int row, String titre, String type, String type2) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Long element = elements.get(row);
                for (Columns periode : colonnes) {
                    sum += (Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString()) * Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type2).toString()));
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sumRow2(int row, String type, String type2) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Long element = elements.get(row);
                for (int i = 0; i < colonnes.size(); i++) {
                    Columns periode = colonnes.get(i);
                    if (periode.getTitres().isEmpty()) {
                        sum += (Double.valueOf(iValeur(element, periode.getValeur().toString(), type).toString()) * Double.valueOf(iValeur(element, periode.getValeur().toString(), type2).toString()));
                    } else {
                        for (String titre : periode.getTitres()) {
                            sum += (Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString()) * Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type2).toString()));
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double totalRow(int row, String type) {
        double total = 0;
        try {
            if (valeursTotauxRow != null ? !valeursTotauxRow.isEmpty() : false) {
                if (valeursTotauxRow.size() > row) {
                    total = Double.valueOf(valeursTotauxRow.get(row).get(type).toString());
                }
            } else {
                total = sumRow(row, type);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public double totalColonne(int col, String type) {
        double total = 0;
        try {
            if (valeursTotauxColumn != null ? !valeursTotauxColumn.isEmpty() : false) {
                if (valeursTotauxColumn.size() > col) {
                    total = Double.valueOf(valeursTotauxColumn.get(col).get(type).toString());
                }
            } else {
                total = sumColonne(col, type);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public double total(String type) {
        double total = 0;
        try {
            if (valeursTotaux != null ? !valeursTotaux.isEmpty() : false) {
                total = Double.valueOf(valeursTotaux.get(0).get(type).toString());
            } else {
                total = sum(type);
            }
        } catch (NumberFormatException ex) {
            Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        return total;
    }

    public double tauxRow(int row, String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Long element = elements.get(row);
                for (Columns periode : colonnes) {
                    sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), type).toString());
                }
                if (totaux == 0) {
                    totaux = sum(type);
                }
                if (sum != 0 && totaux != 0) {
                    return (sum / totaux) * 100;
                }
                return 0;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSumColonne(JournalVendeur valeur, int col, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                double sum = 0;
                Columns periode = colonnes.get(col);
                for (int i = 0; i < valeur.getElements().size(); i++) {
                    Long element = valeur.getElements().get(i);
                    Rows e = (Rows) valeur.getLignes().get(i);
                    if (e.getTitres().isEmpty()) {
                        sum += Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), type).toString());
                    } else {
                        for (String titre : e.getTitres()) {
                            sum += Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), titre, type).toString());
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSumColonneProd(JournalVendeur valeur, int col, String type) {
        if (valeur != null ? valeur.getSous() != null : false) {
            try {
                double sum = 0;
                Columns periode = colonnes.get(col);
                for (int i = 0; i < valeur.getElements().size(); i++) {
                    Long element = valeur.getElements().get(i);
                    Rows e = (Rows) valeur.getLignes().get(i);
                    if (e.getTitres().isEmpty()) {
                        double cumule = Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), type).toString());
                        if (e.getTitre().equals(Constantes.CAT_PSF)) {
                            cumule = e.isFooter() ? cumule : -cumule;
                        }
                        sum += cumule;
                    } else {
                        for (String titre : e.getTitres()) {
                            double cumule = Double.valueOf(iSValeur(valeur, element, periode.getValeur().toString(), titre, type).toString());
                            if (e.getTitre().equals(Constantes.CAT_PSF)) {
                                cumule = e.isFooter() ? cumule : -cumule;
                            }
                            sum += cumule;
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sSumColonne(int col, String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                for (JournalVendeur valeur : valeurs) {
                    sum += sSumColonne(valeur, col, type);
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sumColonne(int col) {
        return sumColonne(col, type);
    }

    public double sumColonne(int col, String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Columns periode = colonnes.get(col);
                for (int i = 0; i < elements.size(); i++) {
                    Long element = elements.get(i);
                    Rows e = (Rows) lignes.get(i);
                    if (e.getTitres().isEmpty()) {
                        sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), type).toString());
                    } else {
                        for (String titre : e.getTitres()) {
                            sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString());
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sumColonne(int col, String titre, String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Columns periode = colonnes.get(col);
                for (Long element : elements) {
                    //iValeur(element, periode.getValeur().toString(), titre, type);
                    sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString());
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double sumColonne2(int col, String type, String type2) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Columns periode = colonnes.get(col);
                for (int i = 0; i < elements.size(); i++) {
                    Long element = elements.get(i);
                    Rows e = (Rows) lignes.get(i);
                    if (e.getTitres().isEmpty()) {
                        sum += (Double.valueOf(iValeur(element, periode.getValeur().toString(), type).toString()) * Double.valueOf(iValeur(element, periode.getValeur().toString(), type2).toString()));
                    } else {
                        for (String titre : e.getTitres()) {
                            sum += (Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type).toString()) * Double.valueOf(iValeur(element, periode.getValeur().toString(), titre, type2).toString()));
                        }
                    }
                }
                return sum;
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public double tauxColonne(int col, String type) {
        if (valeurs != null) {
            try {
                double sum = 0;
                Columns periode = colonnes.get(col);
                for (Long element : elements) {
                    sum += Double.valueOf(iValeur(element, periode.getValeur().toString(), type).toString());
                }
                if (totaux == 0) {
                    totaux = sum(type);
                }
                if (sum != 0 && totaux != 0) {
                    return (sum / totaux) * 100;
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(Gestionnaire.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }

    public boolean isTotalCol(int iCol) {
        return colonnes.size() - 1 == iCol;
    }

    public boolean isTotalRow(int iRow) {
        return lignes.size() - 1 == iRow;
    }

    public boolean isTotal(int iRow, int iCol) {
        return isTotalRow(iRow) || isTotalCol(iCol);
    }

    public Object tauxTotal(int iRow) {
        return tauxTotal(iRow, "ca");
    }

    public Object tauxTotal(int iRow, String type) {
        Long element = elements.get(iRow);
        double sum = (double) avg(element, type);
        double totaux = (double) avg(0, type);
        if (sum != 0 && totaux != 0) {
            return (sum / totaux) * 100;
        }
        return 0;
    }

    public double summaryGroup(String groupe, String type) {
        double sum = 0;
        for (JournalVendeur j : valeurs) {
            if (j.getPrincipal().equals(groupe)) {
                sum += (Double) j.get(type);
            }
        }
        return sum;
    }

    public double summaryGroupResume(String type) {
        return summaryGroupResume(valeurs, type);
    }

    public double summaryComptaResume(List<ValeurComptable> valeurs, String type) {
        double sum = 0;
        for (ValeurComptable j : valeurs) {
            sum += (Double) j.valeur(type);
        }
        return sum;
    }

    public double summaryPeriodComptaResume(long id, ValeurComptable valeur, List<ValeurComptable> valeurs, String type) {
        double sum = 0;
        int index = valeurs.indexOf(new ValeurComptable(id, valeur.getCode(), valeur.getIntitule()));
        if (index > -1) {
            if (type.equals("credit")) {
                sum = valeurs.get(index).getCredit();
            } else {
                sum = valeurs.get(index).getDebit();
            }
        } else {
            double value;
            if (type.equals("credit")) {
                valeur.setSoldeCredit(0);
            } else {
                valeur.setSoldeDebit(0);
            }
            for (ValeurComptable j : valeurs) {
                value = (Double) j.valeur(type);
                if (!j.isSolde()) {
                    sum += value;
                }
                if (type.equals("credit")) {
                    valeur.setSoldeCredit(valeur.getSoldeCredit() + value);
                } else if (type.equals("debit")) {
                    valeur.setSoldeDebit(valeur.getSoldeDebit() + value);
                }
            }
            if (type.equals("credit")) {
                valeur.setCreditPeriodComptaResume(sum);
            } else {
                valeur.setDebitPeriodComptaResume(sum);
            }
        }
        return sum;
    }

    public double summaryPeriodComptaResume(ValeurComptable valeur, List<ValeurComptable> valeurs, String type) {
        return summaryPeriodComptaResume(-1000, valeur, valeurs, type);
    }

    public double summaryComptaResume(ValeurComptable valeur, List<ValeurComptable> valeurs, String type) {
        double sum = summaryComptaResume(valeurs, type);
        if (type.equals("credit")) {
            valeur.setSoldeCredit(sum);
        } else if (type.equals("debit")) {
            valeur.setSoldeDebit(sum);
        }
        return sum;
    }

    public double summaryComptaResume(String type) {
        double sum = 0;
        for (ValeurComptable c : comptables) {
            sum += summaryComptaResume(c.getSous(), type);
        }
        if (type.equals("debit")) {
            debitComptaResume = sum;
        } else if (type.equals("credit")) {
            creditComptaResume = sum;
        }
        return sum;
    }

    public double summaryGroupResume(List<JournalVendeur> valeurs, String type) {
        double sum = 0;
        for (JournalVendeur j : valeurs) {
            sum += (Double) j.get(type);
        }
        return sum;
    }

    public double summaryGroupPeriode(String type) {
        return summaryGroupPeriode(valeurs, type);
    }

    public double summaryGroupPeriode(List<JournalVendeur> valeurs, String type) {
        double sum = 0;
        for (JournalVendeur j : valeurs) {
            if (!j.isOnTotal()) {
                sum += (Double) j.get(type);
            }
        }
        return sum;
    }

    public double summaryGroupsResume(List<JournalVendeur> valeurs, String type) {
        double sum = 0;
        for (JournalVendeur j : valeurs) {
            sum += summaryGroupResume(j.getSous(), type);
        }
        return sum;
    }

    public double summaryGroupsResume(String type) {
        return summaryGroupsResume(valeurs, type);
    }

    public double summaryGroup(String type) {
        double sum = 0;
        switch (type) {
            case "di": {
                double di = summaryGroupResume("di");
                double ci = summaryGroupResume("ci");
                sum = (di - ci) > 0 ? (di - ci) : 0;
                break;
            }
            case "ci": {
                double di = summaryGroupResume("di");
                double ci = summaryGroupResume("ci");
                sum = (di - ci) < 0 ? -(di - ci) : 0;
                break;
            }
            case "dsp": {
                double dsp = summaryGroupResume("dsp");
                double csp = summaryGroupResume("csp");
                sum = (dsp - csp) > 0 ? (dsp - csp) : 0;
                break;
            }
            case "csp": {
                double dsp = summaryGroupResume("dsp");
                double csp = summaryGroupResume("csp");
                sum = (dsp - csp) < 0 ? -(dsp - csp) : 0;
                break;
            }
            case "dsc": {
                double dsc = summaryGroupResume("dsc");
                double csc = summaryGroupResume("csc");
                sum = (dsc - csc) > 0 ? (dsc - csc) : 0;
                break;
            }
            case "csc": {
                double dsc = summaryGroupResume("dsc");
                double csc = summaryGroupResume("csc");
                sum = (dsc - csc) < 0 ? -(dsc - csc) : 0;
                break;
            }
            default:
                sum = summaryGroupResume(type);
                break;
        }
        return sum;
    }

    protected void limitValue(int limit) {
        if (limit > 0) {
            List<Long> elements = new ArrayList<>(this.elements);
            this.elements = elements.subList(0, (elements.size() >= limit ? limit : elements.size()));

            List<Rows> lignes = new ArrayList<>(this.lignes);
            this.lignes = lignes.subList(0, (lignes.size() >= limit ? limit : lignes.size()));

            Rows ligne;
            JournalVendeur row;
            for (Rows y : lignes) {
                ligne = (Rows) y;
                if (ligne.isFooter()) {
                    for (String head : periodes) {
                        double ttc = 0;
                        double quantite = 0;
                        for (Long i : this.elements) {
                            row = iGet(i, head);
                            if (row != null ? !row.isOnFooter() : false) {
                                ttc += row.getTtc();
                                quantite += row.getQuantite();
                            }
                        }
                        row = iGet(ligne.getPrimaire(), head);
                        if (row != null) {
                            row.setTtc(ttc);
                            row.setQuantite(quantite);
                            int idx = valeurs.indexOf(row);
                            if (idx > -1) {
                                valeurs.set(idx, row);
                            }
                        }
                    }
                    if (!this.lignes.contains(y)) {
                        this.lignes.add(y);
                    }
                    if (!this.elements.contains(ligne.getPrimaire())) {
                        this.elements.add(ligne.getPrimaire());
                    }
                    break;
                }
            }
        }
    }

    public void filter() {
        filter(typesTotaux, tauxTotaux, false);
    }

    public void filter(boolean with_sous) {
        filter(typesTotaux, tauxTotaux, with_sous);
    }

    public void filter(String types, String taux, boolean with_sous) {
        List<Rows> temps = new ArrayList<>();
        List<Long> tempsIds = new ArrayList<>();
        Rows line;
        int choix = 2;
        if (varSearch != null ? !varSearch.trim().isEmpty() : false) {
            if (varSearch.trim().charAt(0) == '%') {
                choix = 1;
                if (varSearch.trim().charAt(varSearch.trim().length() - 1) == '%') {
                    choix = 3;
                }
            } else {
                if (varSearch.trim().charAt(varSearch.trim().length() - 1) == '%') {
                    choix = 2;
                }
            }
            for (Rows o : lignesFind) {
                line = (Rows) o;
                switch (choix) {
                    case 1:
                        if (((String) line.getTitre()).toUpperCase().endsWith(varSearch.trim().toUpperCase())
                                || ((String) line.getLibelle()).toUpperCase().endsWith(varSearch.trim().toUpperCase())) {
                            temps.add(line);
                            tempsIds.add(line.getPrimaire());
                        }
                        break;
                    case 2:
                        if (((String) line.getTitre()).toUpperCase().startsWith(varSearch.trim().toUpperCase())
                                || ((String) line.getLibelle()).toUpperCase().startsWith(varSearch.trim().toUpperCase())) {
                            temps.add(line);
                            tempsIds.add(line.getPrimaire());
                        }
                        break;
                    case 3:
                        if (((String) line.getTitre()).toUpperCase().contains(varSearch.trim().toUpperCase())
                                || ((String) line.getLibelle()).toUpperCase().contains(varSearch.trim().toUpperCase())) {
                            temps.add(line);
                            tempsIds.add(line.getPrimaire());
                        }
                        break;
                    default:
                        break;
                }
            }
            lignes = new ArrayList<>(temps);
            elements = new ArrayList<>(tempsIds);
            if (with_sous) {
                for (JournalVendeur valeur : valeurs) {
                    valeur.setLignes(new ArrayList<>(lignes));
                    valeur.setElements(new ArrayList<>(elements));
                }
            }
        } else {
            lignes = new ArrayList<>(lignesFind);
            elements = new ArrayList<>(elementsFinds);
            if (with_sous) {
                for (JournalVendeur valeur : valeurs) {
                    valeur.setLignes(new ArrayList<>(valeur.getLignesFind()));
                    valeur.setElements(new ArrayList<>(valeur.getElementsFinds()));
                }
            }
        }
        lignesTotaux = new ArrayList<>(lignes);
        elementsTotaux = new ArrayList<>(elements);
        //Recalcul des totaux des colonnes
        addTotaux(types, taux);
        ordres = null;
    }

    protected void saveRows() {
        lignesFind = new ArrayList<>(lignes);
        elementsFinds = new ArrayList<>(elements);
        lignesTotaux = new ArrayList<>(lignes);
        elementsTotaux = new ArrayList<>(elements);
    }

    protected void addTotaux(String types) {
        addTotaux(types, "");
    }

    protected void addTotaux(String types, String taux) {
        this.typesTotaux = types;
        this.tauxTotaux = taux;
        addTotauxRow(types, taux);
        addTotauxColumn(types, taux);
        valeursTotaux.clear();
        if (Util.asString(types)) {
            List<String> tab_taux = new ArrayList<>();
            if (Util.asString(taux)) {
                tab_taux = Arrays.asList(taux.split(";"));
            }
            JournalVendeur value = new JournalVendeur(0, "TOTAUX");
            for (String type : types.split(";")) {
                double valeur = sum(type);
                if (tab_taux.contains(type)) {
                    valeur = valeur / (lignes.size() * colonnes.size());
                }
                value.set(type, valeur);
            }
            valeursTotaux.add(value);
        }
        valeursTotauxSave = new ArrayList<>(valeursTotaux);
    }

    protected void addTotauxColumn(String types, String taux) {
        this.typesTotaux = types;
        this.tauxTotaux = taux;
        valeursTotauxColumn.clear();
        if (Util.asString(types)) {
            JournalVendeur value;
            Columns column;
            List<String> tab_taux = new ArrayList<>();
            if (Util.asString(taux)) {
                tab_taux = Arrays.asList(taux.split(";"));
            }
            for (int i = 0; i < colonnes.size(); i++) {
                column = colonnes.get(i);
                if (column.getValeur() == null) {
                    continue;
                }
                if (!column.getValeur().toString().equals("TOTAUX")) {
                    value = new JournalVendeur(0, column.getValeur().toString());
                    for (String type : types.split(";")) {
                        double valeur = sumColonne(i, type);
                        if (tab_taux.contains(type)) {
                            valeur = valeur / lignes.size();
                        }
                        value.set(type, valeur);
                    }
                    valeursTotauxColumn.add(value);
                }
            }
        }
        valeursTotauxColumnSave = new ArrayList<>(valeursTotauxColumn);
    }

    protected void addTotauxRow(String types, String taux) {
        this.typesTotaux = types;
        valeursTotauxRow.clear();
        if (Util.asString(types)) {
            JournalVendeur value;
            Rows row;
            List<String> tab_taux = new ArrayList<>();
            if (Util.asString(taux)) {
                tab_taux = Arrays.asList(taux.split(";"));
            }
            for (int i = 0; i < lignes.size(); i++) {
                row = lignes.get(i);
                if (!row.getTitre().equals("TOTAUX")) {
                    value = new JournalVendeur(row.getPrimaire(), "TOTAUX");
                    for (String type : types.split(";")) {
                        double valeur = sumRow(i, type);
                        if (tab_taux.contains(type)) {
                            valeur = valeur / colonnes.size();
                        }
                        value.set(type, valeur);
                    }
                    valeursTotauxRow.add(value);
                }
            }
        }
        valeursTotauxRowSave = new ArrayList<>(valeursTotauxRow);
    }

    public void sort() {
        if (Util.asString(ordres) ? ordres.equals("default") : true) {
            lignes = new ArrayList<>(lignesTotaux);
            elements = new ArrayList<>(elementsTotaux);
            valeursTotaux = new ArrayList<>(valeursTotauxSave);
            valeursTotauxRow = new ArrayList<>(valeursTotauxRowSave);
            valeursTotauxColumn = new ArrayList<>(valeursTotauxColumnSave);
        } else {
            if (ordres.startsWith("row")) {
                Collections.sort(lignesTotaux, new Comparator<Rows>() {

                    @Override
                    public int compare(Rows s, Rows t) {
                        Object valeur_s = s.get(ordres);
                        Object valeur_t = t.get(ordres);
                        if (valeur_s == null || valeur_t == null) {
                            return 0;
                        }
                        if (descOrdre.equals("desc")) {
                            if (Util.isNumeric(valeur_t.toString())) {
                                return Double.valueOf(valeur_t.toString()).compareTo(Double.valueOf(valeur_s.toString()));
                            } else {
                                return valeur_t.toString().trim().compareToIgnoreCase(valeur_s.toString().trim());
                            }
                        } else {
                            if (Util.isNumeric(valeur_s.toString())) {
                                return Double.valueOf(valeur_s.toString()).compareTo(Double.valueOf(valeur_t.toString()));
                            } else {
                                return valeur_s.toString().trim().compareToIgnoreCase(valeur_t.toString().trim());
                            }
                        }
                    }
                });
                lignes = new ArrayList<>(lignesTotaux);
                elements.clear();
                valeursTotauxRow.clear();
                for (Rows row : lignesTotaux) {
                    elements.add(row.getPrimaire());
                    int index = valeursTotauxRowSave.indexOf(new JournalVendeur(row.getPrimaire(), "TOTAUX"));
                    if (index > -1) {
                        valeursTotauxRow.add(valeursTotauxRowSave.get(index));
                    }
                }
            } else if (ordres.startsWith("column")) {
                Collections.sort(lignesTotaux, new Comparator<Rows>() {

                    @Override
                    public int compare(Rows s, Rows t) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                });
            } else {
                Collections.sort(valeursTotauxRow, new Comparator<JournalVendeur>() {
                    @Override
                    public int compare(JournalVendeur s, JournalVendeur t) {
                        Object valeur_s = s.get(ordres);
                        Object valeur_t = t.get(ordres);
                        if (descOrdre.equals("desc")) {
                            if (Util.isNumeric(valeur_t.toString())) {
                                return Double.valueOf(valeur_t.toString()).compareTo(Double.valueOf(valeur_s.toString()));
                            } else {
                                return valeur_t.toString().compareToIgnoreCase(valeur_s.toString());
                            }
                        } else {
                            if (Util.isNumeric(valeur_s.toString())) {
                                return Double.valueOf(valeur_s.toString()).compareTo(Double.valueOf(valeur_t.toString()));
                            } else {
                                return valeur_s.toString().compareToIgnoreCase(valeur_t.toString());
                            }
                        }
                    }
                });
                lignes.clear();
                elements.clear();
                for (JournalVendeur row : valeursTotauxRow) {
                    int index = elementsTotaux.indexOf(row.getElement());
                    if (index > -1) {
                        elements.add(row.getElement());
                        lignes.add(lignesTotaux.get(index));
                    }
                }
            }
        }
    }

    public static class InstanceDashboards {

        protected String element = "";
        protected String type = "";
        protected String categorie = "";
        protected Dashboards save;

        public InstanceDashboards(String element, String type, String categorie) {
            this.element = element;
            this.type = type;
            this.categorie = categorie;
        }

        public InstanceDashboards(String element, String type, String categorie, Dashboards save) {
            this(element, type, categorie);
            this.save = save;
        }

        public String getElement() {
            return element;
        }

        public void setElement(String element) {
            this.element = element;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCategorie() {
            return categorie;
        }

        public void setCategorie(String categorie) {
            this.categorie = categorie;
        }

        public Dashboards getSave() {
            return save;
        }

        public void setSave(Dashboards save) {
            this.save = save;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 11 * hash + Objects.hashCode(this.element);
            hash = 11 * hash + Objects.hashCode(this.type);
            if (categorie != null) {
                hash = 11 * hash + Objects.hashCode(this.categorie);
            }
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
            final InstanceDashboards other = (InstanceDashboards) obj;
            if (!Objects.equals(this.element, other.element)) {
                return false;
            }
            if (!Objects.equals(this.type, other.type)) {
                return false;
            }
            if (categorie != null) {
                if (!Objects.equals(this.categorie, other.categorie)) {
                    return false;
                }
            }
            return true;
        }

    }

}
