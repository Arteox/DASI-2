/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.insalyon.b3246.dasi.positif_appliweb.serialisations;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import metier.modele.Client;

/**
 *
 * @author bpauletto
 */

public abstract class Serialisation {
    
    protected PrintWriter getWriterWithJsonHeader(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        return out;
    }
    
    public abstract void serialiser(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
