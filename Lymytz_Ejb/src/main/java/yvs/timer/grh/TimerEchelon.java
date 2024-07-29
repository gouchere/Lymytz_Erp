/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.timer.grh;

import java.util.Date;
import yvs.timer.InterfaceTimerLocal;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import yvs.dao.DaoInterfaceLocal;
import yvs.dao.Options;

/**
 *
 * @author LYMYTZ-PC
 */
@Stateless
public class TimerEchelon implements InterfaceTimerLocal {

    @EJB
    public DaoInterfaceLocal dao;

//    @Schedule(dayOfMonth = "*", month = "*", year = "*", hour = "23", dayOfWeek = "*")
    @Override
    public void myTimer() {
//        //récupère les sociétés qui autorisent l'avancement automatique
//        String[] champ = new String[]{"auto"};  
//        Object[] value = new Object[]{true};  
//        List<YvsParametreGrh> ls = dao.loadListTableByNameQueries("YvsParametreGrh.findByEchellonageAutomatique", champ, value);
//        champ = new String[]{"societe"};         
//        for (YvsParametreGrh sct : ls) {        
//            //Récupérer les employé par société            
//            value = new Object[]{sct.getSociete()};  
//            List<YvsGrhEmployes> le=dao.loadListTableByNameQueries("YvsGrhEmployes.findAlls", champ, value);
//            String rq="SELECT public.insertion_avancement(?)";
//            for(YvsGrhEmployes e:le){
//                Options[] paramProc=new Options[]{new Options(e.getId().intValue(),1 )};
//                long conv=dao.callFonction1(rq, paramProc);
//                System.err.println(conv); 
//            }
//        }
//        System.err.println("begin update");
//        clotureMissionAndCongeTermine();
//        System.err.println("end update");
    }

    @Override
    public void avancement() {
    }

    private void clotureMissionAndCongeTermine(){
        String rq="UPDATE yvs_grh_missions SET statut_mission=? WHERE statut_mission=? AND date_fin<?";
        dao.requeteLibre(rq, new Options[]{new Options('C', 1),new Options('V', 2),new Options(new Date(), 3)});
        rq="UPDATE yvs_grh_conge_emps SET statut=? WHERE statut=? AND date_retour<?";
        dao.requeteLibre(rq, new Options[]{new Options('C', 1),new Options('V', 2),new Options(new Date(), 3)});
    }
}
