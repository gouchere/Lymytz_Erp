/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.grh;

import yvs.dao.*;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import yvs.dao.salaire.service.ManagedSalaire;
import yvs.entity.grh.param.YvsParametreGrh;
import yvs.timer.InterfaceTimerLocal;

/**
 *
 * @author LYMYTZ-PC
 */
@Singleton
public class TimerCheckCalculSalaire implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;
    String[] champ;
    Object[] value;
    ManagedSalaire managedS;
    private YvsParametreGrh paramGrh;

    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*", minute = "*/5", persistent = false)
    @Override
    public void myTimer() {
//        System.err.println("---- exécution du timer (SALAIRE)");
//        champ = new String[]{"realise"};
//        value = new Object[]{false};
//        Date dateDebut, dateFin;
//        List<YvsGrhOrdreCalculSalaire> ordres = dao.loadNameQueries("YvsGrhOrdreCalculSalaire.findByRealise", champ, value);
//        System.err.println("Ordre planifié " + ordres.size());
//        if (!ordres.isEmpty()) {
//            champ = new String[]{"societe"};
//            value = new Object[]{ordres.get(0).getSociete()};
//            paramGrh = (YvsParametreGrh) dao.loadOneByNameQueries("YvsParametreGrh.findAll", champ, value);
//            if (paramGrh == null) {
//                return;
//            }
//        }
//        for (YvsGrhOrdreCalculSalaire ord : ordres) {
//            if (!ord.getContrats().isEmpty()) {
//                if (ord.getDateExecution().before(new Date())) {
//                    dateDebut = ord.getDebutMois();
//                    dateFin = ord.getFinMois();
//                    //lance le calcul des salaires pour les contrats rattachés à cet ordre dans la periode spécifié.
//                    managedS = new ManagedSalaire(paramGrh, dao, ord.getAuthor(), ord.getSociete(), ord);
//                    int nombreBulletin = ord.getContrats().size();
//                    managedS.nombreBulletinSave = 0;
//                    for (YvsGrhPlanifSalaireContrat contrat : ord.getContrats()) {
//                        managedS.createBulletin(contrat.getContrat(), dateDebut, dateFin);
//                    }
//                    if (managedS.nombreBulletinSave == nombreBulletin) {
//                        ord.setRealise(true);
//                        System.err.println("Nombre d'erreurs trouvées = " + managedS.erreurs.size());
//                        dao.update(ord);
//                    }
//                }
//            }
//        }
        System.err.println("---- end timer (SALAIRE)");
    }

    @Timeout
    public void timeout() {
        System.out.println("Fin du traitement... (SALAIRE)");
    }

    @Override
    public void avancement() {
        //-
    }
}
