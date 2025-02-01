/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.salaire.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;
import yvs.entity.compta.YvsComptaContentJournal;

/**
 *
 * @author hp Elite 8300
 * @param <T>
 */
public class ResultatAction<T extends Serializable> implements Serializable {

    private boolean result = false;
    private int codeInfo;
    private Long idEntity;
    private Long source;
    private String message;
    private String module;
    private String fonctionalite;
    private Object data;
    private T entity;
    private boolean continu = true;
    /**/
    protected List<YvsComptaContentJournal> listContent = new ArrayList<>();

    public ResultatAction() {
    }

    public ResultatAction(boolean result, Object data) {
        this();
        this.result = result;
        this.data = data;
    }

    public ResultatAction(boolean result, Object data, Long idEntity, String message) {
        this(result, data);
        this.idEntity = idEntity;
        this.message = message;
    }

    public ResultatAction(boolean result, Object data, Long idEntity, String message, boolean continu) {
        this(result, data, idEntity, message);
        this.continu = continu;
    }

    public ResultatAction(boolean result, Object data, Long idEntity, String message, T entity) {
        this(result, data, idEntity, message);
        this.entity = entity;
    }

    public ResultatAction(boolean result, Object data, Long idEntity, Long source, String message) {
        this(result, data, idEntity, message);
        this.source = source;
    }

    public ResultatAction(boolean result, int codeInfo, String message) {
        this();
        this.result = result;
        this.codeInfo = codeInfo;
        this.message = message;
    }

    public ResultatAction(boolean result, int codeInfo, String message, String module, String fonctionalite) {
        this(result, codeInfo, message);
        this.module = module;
        this.fonctionalite = fonctionalite;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getCodeInfo() {
        return codeInfo;
    }

    public void setCodeInfo(int codeInfo) {
        this.codeInfo = codeInfo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFonctionalite() {
        return fonctionalite;
    }

    public void setFonctionalite(String fonctionalite) {
        this.fonctionalite = fonctionalite;
    }

    public Long getIdEntity() {
        return idEntity;
    }

    public void setIdEntity(Long idEntity) {
        this.idEntity = idEntity;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @XmlTransient
    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public Long getSource() {
        return source;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    @XmlTransient
    @JsonIgnore
    public List<YvsComptaContentJournal> getListContent() {
        return listContent;
    }

    public void setListContent(List<YvsComptaContentJournal> listContent) {
        this.listContent = listContent;
    }

    //Module Gescom
    public ResultatAction fail(String erreur) {
        return new ResultatAction(false, -1, erreur, Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction succes() {
        return new ResultatAction(true, 0, "Success", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    /**
     * *
     * @param param: Nom concret de l'entity
     * @return
     */
    public ResultatAction emptyEntity(String param) {
        return new ResultatAction(false, 01, new StringBuilder("Aucun élément de type ").append(param).append(" n'a été trouvé !").toString(), Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction emptyDoc() {
        return new ResultatAction(false, 01, "Aucun document n'a été trouvé", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction documentVerouille() {
        return new ResultatAction(false, 01, "ce document est verouillé", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction docIsNotSubmit() {
        return new ResultatAction(false, 011, "Le document de stock n'est pas transmis", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction emptyDepot(boolean source) {
        return new ResultatAction(false, (source ? 02 : 03), (source ? "Le dépôt source n'a pas été trouvé" : "Le dépôt destination n'a pas été trouvé"), Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction emptyCreneauHoraire(boolean source) {
        return new ResultatAction(false, (source ? 02 : 03), (source ? "Vous devez specifier le créneau source" : "Vous devez specifier le créneau de reception"), Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction operationNotAllow(String depot, String operation) {
        return new ResultatAction(false, 04, "Le depot '" + depot + "' n'est pas paramètré pour les opérations '" + operation + "'", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction operationNotAllowHere() {
        return new ResultatAction(false, 041, "Vous n'êtes pas autorisé à modifier le statut d'une ligne de transfert", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction userNotAbility() {
        return new ResultatAction(false, 05, "Vous ne pouvez pas modifier ce transfert...car vous n'êtes pas habilité à le faire", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction dateNotCorrect() {
        return new ResultatAction(false, 051, "La date de validation ne peut pas etre anterieure a la date d'emission", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction missingStocks(String article, String date) {
        return new ResultatAction(false, 06, "Impossible de valider cette fiche d'inventaire car la ligne d'article " + article + " engendrera une incohérence dans le stock au " + date, Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction docIsAlreadyClose() {
        return new ResultatAction(false, 07, "Ce document est deja cloturer", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction docIsNotEditable() {
        return new ResultatAction(false, 07, "Le document doit être éditable pour être modifié", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction inventaireLock(String depot) {
        return new ResultatAction(false, 8, "Vous ne pouvez créer une fiche de stock à cette date car un ou plusieurs inventaires ont déjà été réalisés après. dépôt: " + depot, Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction canNotSaveInFuture() {
        return new ResultatAction(false, 9, "Vous ne pouvez enregistrer un document à une date future", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction timeIsPast() {
        return new ResultatAction(false, 10, "Le delais de traitement de ce type de document est dépassé !", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction exerciceClose() {
        return new ResultatAction(false, 11, "Le document ne peut pas etre enregistré dans un exercice cloturé", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction exerciceNotFind() {
        return new ResultatAction(false, 12, "Le document doit etre enregistré dans un exercice actif", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    public ResultatAction numDocNotGenerated() {
        return new ResultatAction(false, 13, "Le numéro du document n'a pas pu être généré ", Constantes.MOD_COM, Constantes.TYPE_FT_NAME);
    }

    /*Comptabilité*/
    public ResultatAction emptyCaisse(boolean source) {
        return new ResultatAction(false, 1, "Vous devez selectionner la caisse " + (source ? "source" : "destination"), Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction emptyModeReglement() {
        return new ResultatAction(false, 2, "Vous devez spécifier un mode de virement", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction errorMontant() {
        return new ResultatAction(false, 3, "Le montant du virement est incorrecte !", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction errorCaisse() {
        return new ResultatAction(false, 4, "Redondance cyclique de caisses !", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction requireBielletage() {
        return new ResultatAction(false, 5, "Vous devez specifier le billetage", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction errorEquilibreBielletage() {
        return new ResultatAction(false, 6, "Le billetage n'est pas équilibré au montant du virement", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction emptyUser() {
        return new ResultatAction(false, 7, "Vous ne pouvez pas effectuer cette opération... car vous n'êtes pas utilisateur", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction notAccesCaisse() {
        return new ResultatAction(false, 8, "Vous ne pouvez pas effectuer cette opération... car vous n'avez pas le droit d'action sur cette caisse", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction notAcces() {
        return new ResultatAction(false, 8, "Vous ne pouvez pas effectuer cette opération... car vous n'avez pas le droit", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction emptyHeader() {
        return new ResultatAction(false, 9, "Aucune fiche de vente n'a été trouvé ", Constantes.MOD_COFI, Constantes.SCR_VIREMENT_NAME);
    }

    public ResultatAction badCategorieArticle(String desArticle) {
        return new ResultatAction(false, 100, "Cette facture est rattachée à l'article " + desArticle + " qui n'est pas rattaché à la catégorie de cette facture", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyDocVente() {
        return new ResultatAction(false, 100, "Comptabilisation impossible... Car cette facture de vente n'existe pas", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyCompteGeneral() {
        return new ResultatAction(false, 100, "Vous devez preciser le compte général", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyCompteTiers() {
        return new ResultatAction(false, 100, "Vous devez preciser le compte tiers", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyEcheancier() {
        return new ResultatAction(false, 100, "Vous devez preciser l'échéancier", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyJournal() {
        return new ResultatAction(false, 100, "Comptabilisation impossible...car le journal de comptabilisation n'existe pas", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyExercice(String date) {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car il n'existe pas d'exercice paramètré pour la date du " + date, Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction emptyCaisse() {
        return new ResultatAction(false, 100, "Comptabilisation impossible...car ce reglement n'est associé à aucune caisse", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction exerciceInactif(String exo) {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car l'exerice " + exo + " n'est pas actif", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction exerciceClose(String exo) {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car l'exerice " + exo + " est déjà cloturé", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction periodeInactif(String periode) {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car la période " + periode + " n'est pas active", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction periodeClose(String periode) {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car la période " + periode + " est déjà cloturée", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction journalClose(String journal, String periode) {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car le journal " + journal + " est déjà cloturé pour la période de  " + periode + "", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction alreadyComptabilise() {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car ce document est déjà comptabilisée", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction pieceComptaNotEquilibre() {
        return new ResultatAction(false, 100, "Comptabilisation impossible... La piece n'est pas équilibrée", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction acompteNotComptabilise() {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car l'acompte lié à cette pièce n'est pas comptabilisé", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction documentNotValide() {
        return new ResultatAction(false, 100, "Comptabilisation impossible... car cette pièce de caisse n'est pas validée", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public ResultatAction phaseChequesIsEmpty() {
        return new ResultatAction(false, 100, "Les phases du chèque n'ont pas été trouvé !", Constantes.MOD_COFI, Constantes.TYPE_PIECE_COMPTABLE_NAME);
    }

    public boolean isContinu() {
        return continu;
    }

    public void setContinu(boolean continu) {
        this.continu = continu;
    }

    @Override
    public String toString() {
        return "ResultatAction{" + "message=" + message + '}';
    }

}
