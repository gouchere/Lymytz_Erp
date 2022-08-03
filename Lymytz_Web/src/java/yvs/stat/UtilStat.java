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
            r.setLibelle(y.getLibelle());
            r.setTablePrincipal(y.getTablePrincipal());
            r.setColonnePrincipal(y.getColonnePrincipal());
            r.setSeparateur(y.getSeparateur());
            r.setOrderBy(y.getOrderBy());
            r.setTypeFormule(y.getTypeFormule());
            r.setFormule(y.getFormule());
            r.setColonnes(y.getColonnes());
            r.setForExportation(y.getForExportation());
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
            r.setLibelle(y.getLibelle());
            r.setTablePrincipal(y.getTablePrincipal());
            r.setColonnePrincipal(y.getColonnePrincipal());
            r.setSeparateur(y.getSeparateur());
            r.setOrderBy(y.getOrderBy());
            r.setTypeFormule(y.getTypeFormule());
            r.setFormule(y.getFormule());
            r.setAuthor(ua);
            r.setSociete(s);
            r.setForExportation(y.isForExportation());
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
            r.setEtat(buildBeanExportEtat(y.getEtat()));
            r.setIntegrer(y.getIntegrer());
            r.setLibelle(y.getLibelle());
            r.setOrdre(y.getOrdre());
            r.setTableName(y.getTableName());
            r.setTableNameLiee(y.getTableNameLiee());
            r.setVisible(y.getVisible());
            r.setOrderBy(y.getOrderBy());
            r.setSensContrainte(y.getSensContrainte());
            r.setColonneDate(y.getColonneDate());
            r.setFormatDate(y.getFormatDate());
            r.setDefautValeur(y.getDefautValeur());
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
            if (y.getEtat() != null ? y.getEtat().getId() > 0 : false) {
                r.setEtat(new YvsStatExportEtat(y.getEtat().getId()));
            }
            r.setIntegrer(y.isIntegrer());
            r.setLibelle(y.getLibelle());
            r.setOrdre(y.getOrdre());
            r.setTableName(y.getTableName());
            r.setTableNameLiee(y.getTableNameLiee());
            r.setVisible(y.isVisible());
            r.setColonneDate(y.isColonneDate());
            r.setFormatDate(y.getFormatDate());
            r.setOrderBy(y.getOrderBy());
            r.setSensContrainte(y.getSensContrainte());
            r.setDefautValeur(y.getDefautValeur());
            r.setAuthor(ua);
            r.setNew_(true);
        }
        return r;
    }
}
