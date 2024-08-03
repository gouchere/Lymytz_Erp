/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.service.proj;

import yvs.dao.DaoInterfaceLocal;

/**
 *
 * @author Lymytz-pc
 */
public class SYvsProjProjetService extends AYvsProjProjetService implements IYvsProjProjetService {

    public SYvsProjProjetService() {
    }

    public SYvsProjProjetService(DaoInterfaceLocal dao) {
        super(dao);
    }

}
