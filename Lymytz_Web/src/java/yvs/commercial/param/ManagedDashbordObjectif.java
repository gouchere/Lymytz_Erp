/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.commercial.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import yvs.commercial.ManagedCommercial;
import yvs.commercial.objectifs.ManagedPeriodeObjectif;
import yvs.dao.Options;
import yvs.entity.commercial.objectifs.YvsComPeriodeObjectif;
import yvs.etats.Dimension2;
import yvs.etats.Dimension3;
import yvs.etats.Dimension4;
import yvs.etats.Dashboards;
import yvs.etats.Valeurs;
import static yvs.init.Initialisation.FILE_SEPARATOR;

/**
 *
 * @author Lymytz Dowes
 */
@ManagedBean
@SessionScoped
public class ManagedDashbordObjectif extends ManagedCommercial<Serializable, Serializable> implements Serializable {

    private Dashboards tables = new Dashboards();
    private String periode, type;
    private Date dateDebut = new Date(), dateFin = new Date();

    private List<YvsComPeriodeObjectif> periodes;
    private String IdPeriode = "0";

    public ManagedDashbordObjectif() {
        periodes = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdPeriode() {
        return IdPeriode;
    }

    public void setIdPeriode(String IdPeriode) {
        this.IdPeriode = IdPeriode;
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

    public Dashboards getTables() {
        return tables;
    }

    public void setTables(Dashboards tables) {
        this.tables = tables;
    }

    public List<YvsComPeriodeObjectif> getPeriodes() {
        return periodes;
    }

    public void setPeriodes(List<YvsComPeriodeObjectif> periodes) {
        this.periodes = periodes;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    @Override
    public boolean controleFiche(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateBean() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Serializable recopieView() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void populateView(Serializable bean) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void loadOnView(SelectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComPeriodeObjectif y = (YvsComPeriodeObjectif) ev.getObject();
            y.setSelect(periodes.contains(y));
            if (y.isSelect()) {
                periodes.remove(y);
            } else {
                periodes.add(y);
            }
            y.setSelect(!y.isSelect());
            buildPeriodes();
        }
    }

    @Override
    public void unLoadOnView(UnselectEvent ev) {
        if (ev != null ? ev.getObject() != null : false) {
            YvsComPeriodeObjectif y = (YvsComPeriodeObjectif) ev.getObject();
            y.setSelect(periodes.contains(y));
            if (y.isSelect()) {
                periodes.remove(y);
            }
            y.setSelect(false);
            buildPeriodes();
        }
    }

    @Override
    public void loadAll() {
        if (dateDebut.equals(dateFin) && dateDebut.equals(new Date())) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.DAY_OF_YEAR, c.getActualMinimum(Calendar.DAY_OF_YEAR));
            dateDebut = c.getTime();
            c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
            dateFin = c.getTime();
        }
    }

    public void loadDataObjectifs() {
//        if (IdPeriode.equals("0")) {
//            ManagedPeriodeObjectif service = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
//            if (service != null) {
//                periodes = new ArrayList<>(service.getPeriodes());
//            }
//            buildPeriodes();
//        }
//        tables.returnObjectifsCommerciaux(currentAgence.getSociete().getId(), IdPeriode, type, dao);
        loadDataObjectif();
        update("data_dashboard_objectif");
    }

    private void buildPeriodes() {
        IdPeriode = "0";
        for (YvsComPeriodeObjectif p : periodes) {
            IdPeriode += "," + p.getId();
        }
    }

    public void downloadObjectifs() {
        try {
            Map<String, Object> param = new HashMap<>();
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath(FILE_SEPARATOR + "WEB-INF" + FILE_SEPARATOR + "report");
            param.put("VENDEUR", (int) 0);
            param.put("AGENCE", currentAgence.getId().intValue());
            param.put("DATE_DEBUT", dateDebut);
            param.put("DATE_FIN", dateFin);
            param.put("PERIODE", periode);

            param.put("AUTEUR", currentUser.getUsers().getNomUsers());
            param.put("LOGO", returnLogo());
            param.put("SUBREPORT_DIR", path + FILE_SEPARATOR);
            executeReport("dashboard_article_vendeurs", param);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(ManagedDashbordObjectif.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * *********NOUVELLE CONSTRUCTIONS************
     */
    private List<Dimension4> listData = new ArrayList<>();
    private List<Valeurs> values = new ArrayList<>();
    private List<String> colonnes = new ArrayList<>();

    public List<Dimension4> getListData() {
        return listData;
    }

    public void setListData(List<Dimension4> listData) {
        this.listData = listData;
    }

    public List<String> getColonnes() {
        return colonnes;
    }

    public void setColonnes(List<String> colonnes) {
        this.colonnes = colonnes;
    }

    public void loadDataObjectif() {
        if (IdPeriode.equals("0")) {
            ManagedPeriodeObjectif service = (ManagedPeriodeObjectif) giveManagedBean(ManagedPeriodeObjectif.class);
            if (service != null) {
                periodes = new ArrayList<>(service.getPeriodes());
            }
            buildPeriodes();
        }
        String query = "select y.indicateur, y.objectif, y.element, y.code, y.nom, y.periode, y.entete, y.attente, y.valeur, y.rang from public.com_et_objectif(?,?,?) y order by y.indicateur, y.code, y.objectif, y.rang";
        Options[] param = new Options[]{new Options(currentAgence.getSociete().getId(), 1), new Options(periode, 2), new Options(type, 3)};
        Query qr = dao.getEntityManager().createNativeQuery(query);
        for (Options o : param) {
            qr.setParameter(o.getPosition(), o.getValeur());
        }
        Object[] line;
        String head;
        String comercial, objectif, indicateur;
        Valeurs value;
        List<String> objectifs = new ArrayList<>();
        List<String> commerciales = new ArrayList<>();
        List<String> indicateurs = new ArrayList<>();
        for (Object y : qr.getResultList()) {
            line = (Object[]) y;
            head = (String) line[6];
            comercial = (String) line[3];
            objectif = (String) line[1];
            indicateur = (String) line[0];
            if (!colonnes.contains(head)) {
                colonnes.add(head);
            }
            if (!indicateurs.contains(indicateur)) {
                indicateurs.add(indicateur);
            }
            if (!objectifs.contains(objectif)) {
                objectifs.add(objectif);
            }
            if (!commerciales.contains(comercial)) {
                commerciales.add(comercial);
            }
            value = new Valeurs(head, (Double) line[7], (Double) line[8], indicateur, comercial, objectif);
            values.add(value);
        }
        Valeurs v;
        Dimension4 data;
        Dimension3 dim3;
        Dimension2 dim2;
        listData.clear();
        double soeAttendu, soeRealise;
        for (String ind : indicateurs) {
            data = new Dimension4(ind);
            for (String com : commerciales) {
                dim3 = new Dimension3(com);                
                for (String obj : objectifs) {
                    dim2 = new Dimension2(obj);
                    soeAttendu = soeRealise = 0;
                    for (String col : colonnes) {
                        //construit l'objet valeur
                        int idx = values.indexOf(new Valeurs(col, ind, com, obj));
                        if (idx >= 0) {
                            v = values.get(idx);
                            soeAttendu += v.getValeurAttendu();
                            soeRealise += v.getValeurReelle();
                            dim2.getValues().add(v);
                        } else {
                            dim2.getValues().add(new Valeurs(0, 0));
                        }
                    }
                    //mise à jour des totaux
                    dim2.setTotalAttendu(soeAttendu);
                    dim2.setTotalRealise(soeRealise);
//                    dim3.getValues().set(dim3.getValues().indexOf(dim2),dim2);
                    if (soeAttendu > 0 || soeRealise > 0) {
                        dim3.getValues().add(dim2);
                    }
                }
                if(!dim3.getValues().isEmpty()){
                    data.getValues().add(dim3);
                }
            }
            listData.add(data);
        }
    }

    public Valeurs getValue(Dimension2 dim2, String periode) {
        for (Valeurs v : dim2.getValues()) {
            if (v.getPeriode().equals(periode)) {
                return v;
            }
        }
        return new Valeurs();
    }

    @Override
    public void resetFiche() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
