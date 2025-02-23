/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yvs.util;

import java.util.List;
import java.util.Map;

/**
 *
 * @author lymytz
 */
public interface InterfaceCsvFileWriter {

    void write(List<Map<String, String>> mappedData);

    void write(List<Map<String, String>> mappedData, String[] titles);
}