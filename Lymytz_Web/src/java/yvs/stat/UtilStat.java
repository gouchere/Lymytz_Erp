/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.stat;

import yvs.entity.param.YvsSocietes;
import yvs.entity.stat.export.YvsStatExportColonne;
import yvs.entity.stat.export.YvsStatExportEtat;
import yvs.entity.users.YvsUsersAgence;
import yvs.stat.export.ExportColonne;
import yvs.stat.export.ExportEtat;

/**
 *
 * @author Lymytz Dowes
 */
public class UtilStat {

    public static ExportEtat buildBeanExportEtat(YvsStatExportEtat y) {
        ExportEtat r = new ExportEtat();
        if (y != null) {
            r.setId(y.getId());
            r.setCode(y.getCode());
            r.setReference(y.getReference());
            r.setFileName(y.getFileName());
            r.setFormat(y.getFormat());
            r.setSeparateur(y.getSeparateur());
            r.setTypeFormule(y.getTypeFormule());
            r.setFormule(y.getFormule());
            r.setColonnes(y.getColonnes());
            r.setDateSave(y.getDateSave());
        }
        return r;
    }

    public static YvsStatExportEtat buildExportEtat(ExportEtat y, YvsUsersAgence ua, YvsSocietes s) {
        YvsStatExportEtat r = new YvsStatExportEtat();
        if (y != null) {
            r.setId(y.getId());
            r.setCode(y.getCode());
            r.setReference(y.getReference());
            r.setFileName(y.getFileName());
            r.setFormat(y.getFormat());
            r.setSeparateur(y.getSeparateur());
            r.setTypeFormule(y.getTypeFormule());
            r.setFormule(y.getFormule());
            r.setDateSave(y.getDateSave());
            r.setAuthor(ua);
            r.setSociete(s);
            r.setNew_(true);
        }
        return r;
    }

    public static ExportColonne buildBeanExportColonne(YvsStatExportColonne y) {
        ExportColonne r = new ExportColonne();
        if (y != null) {
            r.setId(y.getId());
            r.setColonne(y.getColonne());
            r.setColonneLiee(y.getColonneLiee());
            r.setContrainte(y.getContrainte());
            r.setIntegrer(y.getIntegrer());
            r.setLibelle(y.getLibelle());
            r.setOrdre(y.getOrdre());
            r.setTableName(y.getTableName());
            r.setTableNameLiee(y.getTableNameLiee());
            r.setVisible(y.getVisible());
            r.setOrderBy(y.getOrderBy());
            r.setSensContrainte(y.getSensContrainte());
            r.setType(y.getType());
            r.setDefautValeur(y.getDefautValeur());
            r.setKey(y.getKey());
            r.setJointure(y.getJointure());
            r.setCondition(y.getCondition());
            r.setConditionOperator(y.getConditionOperator());
            r.setConditionExpression(y.getConditionExpression());
            r.setConditionExpressionOther(y.getConditionExpressionOther());
            r.setDateSave(y.getDateSave());
            r.setEtat(buildBeanExportEtat(y.getEtat()));
        }
        return r;
    }

    public static YvsStatExportColonne buildExportColonne(ExportColonne y, YvsUsersAgence ua) {
        YvsStatExportColonne r = new YvsStatExportColonne();
        if (y != null) {
            r.setId(y.getId());
            r.setColonne(y.getColonne());
            r.setColonneLiee(y.getColonneLiee());
            r.setContrainte(y.isContrainte());
            r.setIntegrer(y.isIntegrer());
            r.setLibelle(y.getLibelle());
            r.setOrdre(y.getOrdre());
            r.setTableName(y.getTableName());
            r.setTableNameLiee(y.getTableNameLiee());
            r.setVisible(y.isVisible());
            r.setType(y.getType());
            r.setOrderBy(y.getOrderBy());
            r.setSensContrainte(y.getSensContrainte());
            r.setDefautValeur(y.getDefautValeur());
            r.setKey(y.isKey());
            r.setJointure(y.getJointure());
            r.setCondition(y.isCondition());
            r.setConditionOperator(y.getConditionOperator());
            r.setConditionExpression(y.getConditionExpression());
            r.setConditionExpressionOther(y.getConditionExpressionOther());
            r.setDateSave(y.getDateSave());
            if (y.getEtat() != null ? y.getEtat().getId() > 0 : false) {
                r.setEtat(new YvsStatExportEtat(y.getEtat().getId()));
            }
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
}
