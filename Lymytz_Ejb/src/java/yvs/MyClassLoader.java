package yvs;

import java.io.File;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Lymytz Dowes
 */
public class MyClassLoader {

    public String getParent() {
        return (new File(getPath()).getParent());
    }

    public String getFile() {
        return getClass().getResource("").getFile();
    }

    public String getPath() {
        return getClass().getResource("").getPath();
    }
}
