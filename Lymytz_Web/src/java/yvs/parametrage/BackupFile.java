/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.parametrage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import yvs.entity.base.YvsBaseArticleCategorieComptable;
import yvs.entity.base.YvsBaseCategorieComptable;
import yvs.entity.base.YvsBaseClassesStat;
import yvs.entity.base.YvsBaseCodeAcces;
import yvs.entity.base.YvsBaseConditionnement;
import yvs.entity.base.YvsBaseElementReference;
import yvs.entity.base.YvsBaseExercice;
import yvs.entity.base.YvsBaseFournisseur;
import yvs.entity.base.YvsBaseModeleReference;
import yvs.entity.base.YvsBaseParametre;
import yvs.entity.base.YvsBaseTableConversion;
import yvs.entity.base.YvsBaseTaxes;
import yvs.entity.base.YvsBaseUniteMesure;
import yvs.entity.compta.YvsBaseTypeDocDivers;
import yvs.entity.grh.activite.YvsGrhTypeCout;
import yvs.entity.grh.presence.YvsGrhTrancheHoraire;
import yvs.entity.param.YvsAgences;
import yvs.entity.param.YvsDictionnaire;
import yvs.entity.param.YvsDictionnaireInformations;
import yvs.entity.param.YvsSocietes;
import yvs.entity.prod.YvsBaseArticlesTemplate;
import yvs.entity.produits.YvsBaseArticleCodeBarre;
import yvs.entity.produits.YvsBaseArticles;
import yvs.entity.produits.group.YvsBaseFamilleArticle;
import yvs.entity.produits.group.YvsBaseGroupesArticle;
import yvs.entity.tiers.YvsBaseTiers;
import yvs.entity.tiers.YvsBaseTiersTemplate;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author LYMYTZ
 */
@XmlRootElement
public class BackupFile {

    private List<YvsSocietes> YvsSocietes;
    private List<YvsAgences> YvsAgences;
    private List<YvsBaseArticles> YvsBaseArticles;
    private List<YvsBaseElementReference> YvsBaseElementReference;
    private List<YvsBaseModeleReference> YvsBaseModeleReference;
    private List<YvsDictionnaire> YvsDictionnaire;
    private List<YvsDictionnaireInformations> YvsDictionnaireInformations;
    private List<YvsNiveauAcces> YvsNiveauAcces;
    private List<YvsBaseTiersTemplate> YvsBaseTiersTemplate;

    private List<YvsBaseArticlesTemplate> YvsBaseArticlesTemplate;
    private List<YvsBaseFamilleArticle> YvsBaseFamilleArticle;
    private List<YvsBaseUniteMesure> YvsBaseUniteMesure;
    private List<YvsBaseParametre> YvsBaseParametre;
    private List<YvsBaseClassesStat> YvsBaseClassesStat;
    private List<YvsBaseCategorieComptable> YvsBaseCategorieComptable;
    private List<YvsBaseTableConversion> YvsBaseTableConversion;
    private List<YvsBaseTaxes> YvsBaseTaxes;
    private List<YvsBaseGroupesArticle> YvsBaseGroupesArticle;
    private List<YvsGrhTrancheHoraire> YvsGrhTrancheHoraire;
    private List<YvsBaseTiers> YvsBaseTiers;
    private List<YvsBaseFournisseur> YvsBaseFournisseur;
    private List<YvsGrhTypeCout> YvsGrhTypeCout;
    private List<YvsBaseTypeDocDivers> YvsBaseTypeDocDivers;
    private List<YvsBaseExercice> YvsBaseExercice;
    private List<YvsBaseCodeAcces> YvsBaseCodeAcces;
    private List<YvsBaseConditionnement> YvsBaseConditionnement;
    private List<YvsBaseArticleCategorieComptable> YvsBaseArticleCategorieComptable;
    private List<YvsBaseArticleCodeBarre> YvsBaseArticleCodeBarre;

    public BackupFile() {
        YvsSocietes = new ArrayList<>();
        YvsAgences = new ArrayList<>();
        YvsBaseArticles = new ArrayList<>();
        YvsBaseElementReference = new ArrayList<>();
        YvsBaseModeleReference = new ArrayList<>();
        YvsDictionnaire = new ArrayList<>();
        YvsDictionnaireInformations = new ArrayList<>();
        YvsNiveauAcces = new ArrayList<>();
        YvsBaseTiersTemplate = new ArrayList<>();

        YvsBaseArticlesTemplate = new ArrayList<>();
        YvsBaseFamilleArticle = new ArrayList<>();
        YvsBaseUniteMesure = new ArrayList<>();
        YvsBaseParametre = new ArrayList<>();
        YvsBaseClassesStat = new ArrayList<>();
        YvsBaseCategorieComptable = new ArrayList<>();
        YvsBaseTableConversion = new ArrayList<>();
        YvsBaseTaxes = new ArrayList<>();
        YvsBaseGroupesArticle = new ArrayList<>();
        YvsGrhTrancheHoraire = new ArrayList<>();
        YvsBaseTiers = new ArrayList<>();
        YvsBaseFournisseur = new ArrayList<>();
        YvsGrhTypeCout = new ArrayList<>();
        YvsBaseTypeDocDivers = new ArrayList<>();
        YvsBaseExercice = new ArrayList<>();
        YvsBaseCodeAcces = new ArrayList<>();
        YvsBaseConditionnement = new ArrayList<>();
        YvsBaseArticleCategorieComptable = new ArrayList<>();
        YvsBaseArticleCodeBarre = new ArrayList<>();
    }

    public List<YvsSocietes> getYvsSocietes() {
        return YvsSocietes;
    }

    public void setYvsSocietes(List<YvsSocietes> YvsSocietes) {
        this.YvsSocietes = YvsSocietes;
    }

    public List<YvsAgences> getYvsAgences() {
        return YvsAgences;
    }

    public void setYvsAgences(List<YvsAgences> YvsAgences) {
        this.YvsAgences = YvsAgences;
    }

    public List<YvsBaseArticles> getYvsBaseArticles() {
        return YvsBaseArticles;
    }

    public void setYvsBaseArticles(List<YvsBaseArticles> YvsBaseArticles) {
        this.YvsBaseArticles = YvsBaseArticles;
    }

    public List<YvsBaseElementReference> getYvsBaseElementReference() {
        return YvsBaseElementReference;
    }

    public void setYvsBaseElementReference(List<YvsBaseElementReference> YvsBaseElementReference) {
        this.YvsBaseElementReference = YvsBaseElementReference;
    }

    public List<YvsBaseModeleReference> getYvsBaseModeleReference() {
        return YvsBaseModeleReference;
    }

    public void setYvsBaseModeleReference(List<YvsBaseModeleReference> YvsBaseModeleReference) {
        this.YvsBaseModeleReference = YvsBaseModeleReference;
    }

    public List<YvsDictionnaire> getYvsDictionnaire() {
        return YvsDictionnaire;
    }

    public void setYvsDictionnaire(List<YvsDictionnaire> YvsDictionnaire) {
        this.YvsDictionnaire = YvsDictionnaire;
    }

    public List<YvsDictionnaireInformations> getYvsDictionnaireInformations() {
        return YvsDictionnaireInformations;
    }

    public void setYvsDictionnaireInformations(List<YvsDictionnaireInformations> YvsDictionnaireInformations) {
        this.YvsDictionnaireInformations = YvsDictionnaireInformations;
    }

    public List<YvsNiveauAcces> getYvsNiveauAcces() {
        return YvsNiveauAcces;
    }

    public void setYvsNiveauAcces(List<YvsNiveauAcces> YvsNiveauAcces) {
        this.YvsNiveauAcces = YvsNiveauAcces;
    }

    public List<YvsBaseTiersTemplate> getYvsBaseTiersTemplate() {
        return YvsBaseTiersTemplate;
    }

    public void setYvsBaseTiersTemplate(List<YvsBaseTiersTemplate> YvsBaseTiersTemplate) {
        this.YvsBaseTiersTemplate = YvsBaseTiersTemplate;
    }

    public List<YvsBaseArticlesTemplate> getYvsBaseArticlesTemplate() {
        return YvsBaseArticlesTemplate;
    }

    public void setYvsBaseArticlesTemplate(List<YvsBaseArticlesTemplate> YvsBaseArticlesTemplate) {
        this.YvsBaseArticlesTemplate = YvsBaseArticlesTemplate;
    }

    public List<YvsBaseFamilleArticle> getYvsBaseFamilleArticle() {
        return YvsBaseFamilleArticle;
    }

    public void setYvsBaseFamilleArticle(List<YvsBaseFamilleArticle> YvsBaseFamilleArticle) {
        this.YvsBaseFamilleArticle = YvsBaseFamilleArticle;
    }

    public List<YvsBaseUniteMesure> getYvsBaseUniteMesure() {
        return YvsBaseUniteMesure;
    }

    public void setYvsBaseUniteMesure(List<YvsBaseUniteMesure> YvsBaseUniteMesure) {
        this.YvsBaseUniteMesure = YvsBaseUniteMesure;
    }

    public List<YvsBaseParametre> getYvsBaseParametre() {
        return YvsBaseParametre;
    }

    public void setYvsBaseParametre(List<YvsBaseParametre> YvsBaseParametre) {
        this.YvsBaseParametre = YvsBaseParametre;
    }

    public List<YvsBaseClassesStat> getYvsBaseClassesStat() {
        return YvsBaseClassesStat;
    }

    public void setYvsBaseClassesStat(List<YvsBaseClassesStat> YvsBaseClassesStat) {
        this.YvsBaseClassesStat = YvsBaseClassesStat;
    }

    public List<YvsBaseCategorieComptable> getYvsBaseCategorieComptable() {
        return YvsBaseCategorieComptable;
    }

    public void setYvsBaseCategorieComptable(List<YvsBaseCategorieComptable> YvsBaseCategorieComptable) {
        this.YvsBaseCategorieComptable = YvsBaseCategorieComptable;
    }

    public List<YvsBaseTableConversion> getYvsBaseTableConversion() {
        return YvsBaseTableConversion;
    }

    public void setYvsBaseTableConversion(List<YvsBaseTableConversion> YvsBaseTableConversion) {
        this.YvsBaseTableConversion = YvsBaseTableConversion;
    }

    public List<YvsBaseTaxes> getYvsBaseTaxes() {
        return YvsBaseTaxes;
    }

    public void setYvsBaseTaxes(List<YvsBaseTaxes> YvsBaseTaxes) {
        this.YvsBaseTaxes = YvsBaseTaxes;
    }

    public List<YvsBaseGroupesArticle> getYvsBaseGroupesArticle() {
        return YvsBaseGroupesArticle;
    }

    public void setYvsBaseGroupesArticle(List<YvsBaseGroupesArticle> YvsBaseGroupesArticle) {
        this.YvsBaseGroupesArticle = YvsBaseGroupesArticle;
    }

    public List<YvsGrhTrancheHoraire> getYvsGrhTrancheHoraire() {
        return YvsGrhTrancheHoraire;
    }

    public void setYvsGrhTrancheHoraire(List<YvsGrhTrancheHoraire> YvsGrhTrancheHoraire) {
        this.YvsGrhTrancheHoraire = YvsGrhTrancheHoraire;
    }

    public List<YvsBaseTiers> getYvsBaseTiers() {
        return YvsBaseTiers;
    }

    public void setYvsBaseTiers(List<YvsBaseTiers> YvsBaseTiers) {
        this.YvsBaseTiers = YvsBaseTiers;
    }

    public List<YvsBaseFournisseur> getYvsBaseFournisseur() {
        return YvsBaseFournisseur;
    }

    public void setYvsBaseFournisseur(List<YvsBaseFournisseur> YvsBaseFournisseur) {
        this.YvsBaseFournisseur = YvsBaseFournisseur;
    }

    public List<YvsGrhTypeCout> getYvsGrhTypeCout() {
        return YvsGrhTypeCout;
    }

    public void setYvsGrhTypeCout(List<YvsGrhTypeCout> YvsGrhTypeCout) {
        this.YvsGrhTypeCout = YvsGrhTypeCout;
    }

    public List<YvsBaseTypeDocDivers> getYvsBaseTypeDocDivers() {
        return YvsBaseTypeDocDivers;
    }

    public void setYvsBaseTypeDocDivers(List<YvsBaseTypeDocDivers> YvsBaseTypeDocDivers) {
        this.YvsBaseTypeDocDivers = YvsBaseTypeDocDivers;
    }

    public List<YvsBaseExercice> getYvsBaseExercice() {
        return YvsBaseExercice;
    }

    public void setYvsBaseExercice(List<YvsBaseExercice> YvsBaseExercice) {
        this.YvsBaseExercice = YvsBaseExercice;
    }

    public List<YvsBaseCodeAcces> getYvsBaseCodeAcces() {
        return YvsBaseCodeAcces;
    }

    public void setYvsBaseCodeAcces(List<YvsBaseCodeAcces> YvsBaseCodeAcces) {
        this.YvsBaseCodeAcces = YvsBaseCodeAcces;
    }

    public List<YvsBaseConditionnement> getYvsBaseConditionnement() {
        return YvsBaseConditionnement;
    }

    public void setYvsBaseConditionnement(List<YvsBaseConditionnement> YvsBaseConditionnement) {
        this.YvsBaseConditionnement = YvsBaseConditionnement;
    }

    public List<YvsBaseArticleCategorieComptable> getYvsBaseArticleCategorieComptable() {
        return YvsBaseArticleCategorieComptable;
    }

    public void setYvsBaseArticleCategorieComptable(List<YvsBaseArticleCategorieComptable> YvsBaseArticleCategorieComptable) {
        this.YvsBaseArticleCategorieComptable = YvsBaseArticleCategorieComptable;
    }

    public List<YvsBaseArticleCodeBarre> getYvsBaseArticleCodeBarre() {
        return YvsBaseArticleCodeBarre;
    }

    public void setYvsBaseArticleCodeBarre(List<YvsBaseArticleCodeBarre> YvsBaseArticleCodeBarre) {
        this.YvsBaseArticleCodeBarre = YvsBaseArticleCodeBarre;
    }

}
