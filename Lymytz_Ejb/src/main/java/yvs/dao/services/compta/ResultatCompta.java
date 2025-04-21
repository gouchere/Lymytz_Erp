/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yvs.dao.services.compta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import yvs.dao.salaire.service.ResultatAction;
import yvs.entity.compta.YvsComptaContentJournal;

/**
 *
 * @author hp Elite 8300
 * @param <T>
 */
public class ResultatCompta<T extends Serializable> extends ResultatAction {

    public ResultatCompta() {
    }

    public ResultatCompta(boolean result, int codeInfo, String message) {
        super(result, codeInfo, message);
    }

    public ResultatCompta(boolean result, int codeInfo, String message, String module, String fonctionalite) {
        super(result, codeInfo, message, module, fonctionalite);
    }

    public ResultatCompta(boolean result, Object data, List<YvsComptaContentJournal> listContent, String message) {
        super(result, data, 0L, message);
        this.listContent = listContent;
    }

}
