/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lymytz.ws;

import java.io.Serializable;
import java.util.List;
import yvs.entity.compta.YvsComptaCaissePieceVente;
import yvs.entity.compta.YvsComptaPhasePiece;
import yvs.entity.compta.YvsComptaPhaseReglement;
import yvs.util.Constantes;

/**
 *
 * @author Lymytz Dowes
 */
public class ActionReglement extends ManagedWS implements Serializable {

    public ActionReglement() {
    }

    @Override
    public String getPathArticle() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean saveReglement(long piece, long users) {
        try {
            load(users);
            if (piece > 0) {
                YvsComptaCaissePieceVente y = (YvsComptaCaissePieceVente) dao.loadOneByNameQueries("YvsComptaCaissePieceVente.findById", new String[]{"id"}, new Object[]{piece});
                if (y.getModel().getTypeReglement().equals(Constantes.MODE_PAIEMENT_BANQUE)) {
                    List<YvsComptaPhaseReglement> phases = dao.loadNameQueries("YvsComptaPhaseReglement.findByMode", new String[]{"mode"}, new Object[]{y.getModel()});
                    YvsComptaPhasePiece pp;
                    YvsComptaPhasePiece w;
                    for (YvsComptaPhaseReglement ph : phases) {
                        pp = new YvsComptaPhasePiece(null);
                        pp.setAuthor(currentUser);
                        pp.setPhaseOk(false);
                        pp.setCaisse(y.getCaisse());
                        pp.setPhaseReg(ph);
                        pp.setPieceVente(y);
                        pp = (YvsComptaPhasePiece) dao.save1(pp);
                        y.getPhasesReglement().add(pp);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
        return false;
    }
}
