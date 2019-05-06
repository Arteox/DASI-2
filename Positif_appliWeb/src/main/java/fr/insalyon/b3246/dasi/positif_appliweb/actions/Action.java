/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.actions;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author bpauletto
 */
public abstract class Action {
    
    public abstract boolean executer(HttpServletRequest request);
}
