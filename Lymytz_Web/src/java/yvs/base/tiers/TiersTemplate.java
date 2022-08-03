/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.base.tiers;

import java.io.Serializable;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import yvs.base.compta.CategorieComptable;
import yvs.base.compta.Comptes;
import yvs.base.compta.ModeDeReglement;
import yvs.commercial.commission.PlanCommission;
import yvs.commercial.client.PlanTarifaireClient;
import yvs.commercial.rrr.PlanRistourne;
import yvs.parametrage.agence.Agence;
import yvs.parametrage.dico.Dictionnaire;

/**
 *
 * @author GOUCHERE YVES
 */
@ManagedBean
@SessionScoped
public class TiersTemplate implements Serializable {

    private long id;
    private String apercu;
    private Comptes compteCollectif = new Comptes();
    private Dictionnaire secteur = new Dictionnaire();
    private Dictionnaire pays = new Dictionnaire();
    private Dictionnaire ville = new Dictionnaire();
    private Agence agence = new Agence();
    private PlanTarifaireClient categorieTarifaire = new PlanTarifaireClient();
    private CategorieComptable categorieComptable = new CategorieComptable();
    private PlanRistourne ristourne = new PlanRistourne();
    private PlanCommission commission = new PlanCommission();
    private ModeDeReglement model = new ModeDeReglement();
    private boolean addSeparateur;
    private String separateur;
    private String libelle;
    private boolean addSecteur;
    private Date dateSave = new Date();
    private Date dateUpdate = new Date();
    private int tailleSecteur;
    private boolean addNom;
    private int tailleNom;
    private boolean addPrenom;
    private int taillePrenom;
    private char type = 'M';
    private String nom = "FOTSO", prenom = "ALAIN";
    private boolean select, new_, update, choose;

    public TiersTemplate() {

    }

    public TiersTemplate(long id) {
        this.id = id;
    }

    public Date getDateSave() {
        return dateSave;
    }

    public void setDateSave(Date dateSave) {
        this.dateSave = dateSave;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public char getType() {
        return String.valueOf(type) != null ? (String.valueOf(type).trim().length() > 0 ? type : 'M') : 'M';
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean isChoose() {
        return choose;
    }

    public void setChoose(boolean choose) {
        this.choose = choose;
    }

    public Dictionnaire getPays() {
        return pays;
    }

    public Dictionnaire getPays_() {
        return pays != null ? pays : new Dictionnaire();
    }

    public void setPays(Dictionnaire pays) {
        this.pays = pays;
    }

    public Dictionnaire getVille() {
        return ville;
    }

    public Dictionnaire getVille_() {
        return ville != null ? ville.getId() > 0 ? ville : getPays_() : getPays_();
    }

    public void setVille(Dictionnaire ville) {
        this.ville = ville;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getApercu() {
        apercu = "";
        if (isAddSeparateur()) {
            if (isAddSecteur()) {
                apercu = getSecteur_().getAbreviation().length() > getTailleSecteur() ? getSecteur_().getAbreviation().substring(0, getTailleSecteur()) : getSecteur_().getAbreviation();
                if (isAddNom()) {
                    apercu += getSeparateur() + ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (isAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (isAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            } else {
                if (isAddNom()) {
                    apercu += getSeparateur() + ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (isAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (isAddPrenom()) {
                        apercu += getSeparateur() + ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            }
        } else {
            if (isAddSecteur()) {
                apercu = getSecteur_().getAbreviation().length() > getTailleSecteur() ? getSecteur_().getAbreviation().substring(0, getTailleSecteur()) : getSecteur_().getAbreviation();
                if (isAddNom()) {
                    apercu += ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (isAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (isAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            } else {
                if (isAddNom()) {
                    apercu += ((nom.length() > getTailleNom()) ? nom.substring(0, getTailleNom()) : nom);
                    if (isAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                } else {
                    if (isAddPrenom()) {
                        apercu += ((prenom.length() > getTaillePrenom()) ? prenom.substring(0, getTaillePrenom()) : prenom);
                    }
                }
            }
        }
        return apercu;
    }

    public void setApercu(String apercu) {
        this.apercu = apercu;
    }

    public Comptes getCompteCollectif() {
        return compteCollectif;
    }

    public void setCompteCollectif(Comptes compteCollectif) {
        this.compteCollectif = compteCollectif;
    }

    public Dictionnaire getSecteur() {
        return secteur;
    }

    public Dictionnaire getSecteur_() {
        return secteur != null ? secteur.getId() > 0 ? secteur : getVille_() : getVille_();
    }

    public void setSecteur(Dictionnaire secteur) {
        this.secteur = secteur;
    }

    public Agence getAgence() {
        return agence;
    }

    public void setAgence(Agence agence) {
        this.agence = agence;
    }

    public PlanTarifaireClient getCategorieTarifaire() {
        return categorieTarifaire;
    }

    public void setCategorieTarifaire(PlanTarifaireClient categorieTarifaire) {
        this.categorieTarifaire = categorieTarifaire;
    }

    public CategorieComptable getCategorieComptable() {
        return categorieComptable;
    }

    public void setCategorieComptable(CategorieComptable categorieComptable) {
        this.categorieComptable = categorieComptable;
    }

    public PlanRistourne getRistourne() {
        return ristourne;
    }

    public void setRistourne(PlanRistourne ristourne) {
        this.ristourne = ristourne;
    }

    public PlanCommission getCommission() {
        return commission;
    }

    public void setCommission(PlanCommission commission) {
        this.commission = commission;
    }

    public ModeDeReglement getModel() {
        return model;
    }

    public void setModel(ModeDeReglement model) {
        this.model = model;
    }

    public boolean isAddSeparateur() {
        return addSeparateur;
    }

    public void setAddSeparateur(boolean addSeparateur) {
        this.addSeparateur = addSeparateur;
    }

    public String getSeparateur() {
        return separateur != null ? ((separateur.trim().length() > 0) ? separateur : "-") : "-";
    }

    public void setSeparateur(String Separateur) {
        this.separateur = Separateur;
    }

    public boolean isAddSecteur() {
        return addSecteur;
    }

    public void setAddSecteur(boolean addSecteur) {
        this.addSecteur = addSecteur;
    }

    public int getTailleSecteur() {
        return tailleSecteur;
    }

    public void setTailleSecteur(int tailleSecteur) {
        this.tailleSecteur = tailleSecteur;
    }

    public boolean isAddNom() {
        return addNom;
    }

    public void setAddNom(boolean addNom) {
        this.addNom = addNom;
    }

    public int getTailleNom() {
        return tailleNom;
    }

    public void setTailleNom(int tailleNom) {
        this.tailleNom = tailleNom;
    }

    public boolean isAddPrenom() {
        return addPrenom;
    }

    public void setAddPrenom(boolean addPrenom) {
        this.addPrenom = addPrenom;
    }

    public int getTaillePrenom() {
        return taillePrenom;
    }

    public void setTaillePrenom(int taillePrenom) {
        this.taillePrenom = taillePrenom;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isNew_() {
        return new_;
    }

    public void setNew_(boolean new_) {
        this.new_ = new_;
    }

    public boolean isUpdate() {
        return id > 0;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TiersTemplate other = (TiersTemplate) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

}
