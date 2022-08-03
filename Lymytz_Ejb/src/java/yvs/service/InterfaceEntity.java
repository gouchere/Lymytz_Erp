/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.service;

import yvs.dao.DaoInterfaceLocal;
import yvs.entity.param.YvsAgences;
import yvs.entity.users.YvsNiveauAcces;

/**
 *
 * @author Lymytz Dowes
 */
public interface InterfaceEntity {

    void setDao(DaoInterfaceLocal dao);

    void setAgence(YvsAgences agence);

    void setNiveauAcces(YvsNiveauAcces agence);
    
}
