/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.compta.doc.divers;

import yvs.dao.DaoInterfaceWs;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaAcompteClient;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsComptaAcompteClient extends AYvsComptaAcompteClient implements IYvsComptaAcompteClient {

    public SYvsComptaAcompteClient() {
    }

    public SYvsComptaAcompteClient(DaoInterfaceWs dao) {
        super(dao);
    }

}
