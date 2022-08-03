/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.adapter.users;

import com.fasterxml.jackson.databind.util.StdConverter;
import yvs.entity.users.YvsUsersAgence;

/**
 *
 * @author hp Elite 8300
 */
public class CYvsUsersAgence extends StdConverter<YvsUsersAgence, YvsUsersAgence> {
    
    @Override
    public YvsUsersAgence convert(YvsUsersAgence value) {
        if (value != null) {
            YvsUsersAgence result = new YvsUsersAgence();
            result.setId(value.getId());
            result.setUsers(value.getUsers());
            result.setAgence(value.getAgence());
        }
        return value;
    }
    
}
