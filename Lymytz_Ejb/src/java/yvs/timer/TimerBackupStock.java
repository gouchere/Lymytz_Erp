/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import yvs.dao.JDBC;
import yvs.entity.base.YvsBaseMouvementStock;

/**
 *
 * @author Dowes
 */
@Singleton
public class TimerBackupStock implements InterfaceTimerLocal {

    private final Logger logger = Logger.getLogger(TimerBackupStock.class.getName());
    private final int LIMIT = 100;
    private JDBC jdbc;

    public TimerBackupStock() {
        try {
            Context initialContext = new InitialContext();
            DataSource ds = (DataSource) initialContext.lookup("jdbc/lymytz-erp");
            if (ds != null && ds.getConnection() != null) {
                jdbc = new JDBC(new YvsBaseMouvementStock(), ds);
            }
        } catch (NamingException | SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*", minute = "*/2", persistent = false)
    @Override
    public void myTimer() {
        logger.log(Level.INFO, "Début du traitement... (BACKUP)");
        try {
            if (jdbc == null) {
                return;
            }
            List<Map<String, Object>> parametres = new ArrayList<>();
            String query = "SELECT societe, date_backup_stock FROM public.yvs_base_parametre WHERE date_backup_stock IS NOT NULL";
            try (PreparedStatement st = jdbc.getConnect().prepareStatement(query)) {
                try (ResultSet rs = st.executeQuery()) {
                    Map<String, Object> parametre = new HashMap<>();
                    while (rs.next()) {
                        parametre.put("societe", rs.getLong(1));
                        parametre.put("date_backup_stock", rs.getDate(2));
                        parametres.add(parametre);
                    }
                }
            }
            logger.log(Level.INFO, "parametres size = {0}", parametres.size());
            if (!parametres.isEmpty()) {
                for (Map parametre : parametres) {
                    Long count = 0L;
                    query = "SELECT COUNT(m.id) FROM public.yvs_base_mouvement_stock m INNER JOIN yvs_base_articles a ON m.article = a.id INNER JOIN yvs_base_famille_article f ON a.famille = f.id WHERE f.societe = ? AND m.date_doc < ?";
                    try (PreparedStatement st = jdbc.getConnect().prepareStatement(query)) {
                        st.setObject(1, parametre.get("societe"));
                        st.setObject(2, parametre.get("date_backup_stock"));
                        try (ResultSet rs = st.executeQuery()) {
                            while (rs.next()) {
                                count = rs.getLong(1);
                                break;
                            }
                        }
                    }
                    logger.log(Level.INFO, "count = {0} ligne for {1} in {2}", new Object[]{count, parametre.get("societe"), parametre.get("date_backup_stock")});
                    if (count < 1) {
                        disable((Long) parametre.get("societe"));
                        continue;
                    }
                    query = JDBC.select(YvsBaseMouvementStock.class) + " LEFT JOIN yvs_base_famille_article ON yvs_base_articles.famille = yvs_base_famille_article.id WHERE yvs_base_famille_article.societe = ? AND yvs_base_mouvement_stock.date_doc < ?";
                    List<YvsBaseMouvementStock> datas = jdbc.list(query, new Object[]{parametre.get("societe"), parametre.get("date_backup_stock")}, -1, LIMIT);
                    logger.log(Level.INFO, "datas size = {0}", datas.size());
                    if (datas.isEmpty()) {
                        disable((Long) parametre.get("societe"));
                        continue;
                    }
                    List<Long> results = new ArrayList();
                    List<YvsBaseMouvementStock> reports = new ArrayList();
                    for (YvsBaseMouvementStock data : datas) {
                        count = 0L;
                        query = "SELECT COUNT(id) FROM backup.yvs_base_mouvement_stock WHERE id = ?";
                        try (PreparedStatement st = jdbc.getConnect().prepareStatement(query)) {
                            st.setObject(1, data.getId());
                            try (ResultSet rs = st.executeQuery()) {
                                while (rs.next()) {
                                    count = rs.getLong(1);
                                    break;
                                }
                            }
                        }
                        boolean success = count < 1 ? jdbc.insert("backup", data, true) : jdbc.update("backup", data);
                        if (success) {
                            results.add(data.getId());
                            boolean exist = false;
                            for (YvsBaseMouvementStock report : reports) {
                                if (Objects.equals(report.getConditionnement(), data.getConditionnement()) && Objects.equals(report.getDepot(), data.getDepot()) && Objects.equals(report.getTranche(), data.getTranche())) {
                                    exist = true;
                                    report.setQuantite(report.getQuantite() + (data.getMouvement().equals("E") ? data.getQuantite() : -data.getQuantite()));
                                    break;
                                }
                            }
                            if (!exist) {
                                reports.add(copy(data, convert((java.sql.Date) parametre.get("date_backup_stock"))));
                            }
                        }
                    }
                    logger.log(Level.INFO, "reports size = {0}", reports.size());
                    for (YvsBaseMouvementStock report : reports) {
                        double quantite = report.getQuantite();
                        if (quantite != 0) {
                            report.setMouvement(quantite < 0 ? "S" : "E");
                            report.setQuantite(Math.abs(quantite));
                            jdbc.insert("public", report, false);
                        }
                    }
                    logger.log(Level.INFO, "results size = {0}", results.size());
                    for (Long result : results) {
                        delete("public", result);
                    }
                }
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        logger.log(Level.INFO, "Fin du traitement... (BACKUP)");
    }

    @Override
    public void avancement() {
    }

    @Timeout
    public void timeout() {
        logger.log(Level.WARNING, "Timeout du traitement... (BACKUP)");
    }

    private Date convert(java.sql.Date data) {
        if (data == null) {
            return null;
        }
        return new Date(data.getTime());
    }

    private YvsBaseMouvementStock copy(YvsBaseMouvementStock data, Date dateDoc) throws Exception {
        YvsBaseMouvementStock result = null;
        if (data != null) {
            result = new YvsBaseMouvementStock();
            result.setArticle(data.getArticle());
            result.setConditionnement(data.getConditionnement());
            result.setDepot(data.getDepot());
            result.setTranche(data.getTranche());
            result.setQuantite(data.getQuantite());
            result.setCout(data.getCout());
            result.setCoutEntree(data.getCoutEntree());
            result.setCoutStock(data.getCoutStock());
            result.setAuthor(data.getAuthor());
            result.setDateDoc(dateDoc);
            result.setDateMouvement(data.getDateMouvement());
            result.setDateSave(new Date());
            result.setMouvement(data.getMouvement());
            result.setDescription("REPORT");
            result.setNumDoc("REPORT");
            result.setTypeDoc("REPORT");
        }
        return result;
    }

    private boolean disable(Long societe) throws Exception {
        String query = "UPDATE public.yvs_base_parametre SET date_backup_stock = NULL WHERE societe = ?";
        try (PreparedStatement st = jdbc.getConnect().prepareStatement(query)) {
            st.setObject(1, societe);
            return st.executeUpdate() > 0;
        }
    }

    private boolean delete(String schema, Long id) throws Exception {
        schema = (schema != null && !schema.isEmpty()) ? schema : "public";
        String query = "DELETE FROM " + schema + ".yvs_base_mouvement_stock WHERE yvs_base_mouvement_stock.id = ?";
        try (PreparedStatement st = jdbc.getConnect().prepareStatement(query)) {
            st.setObject(1, id);
            return st.executeUpdate() > 0;
        }
    }
}
