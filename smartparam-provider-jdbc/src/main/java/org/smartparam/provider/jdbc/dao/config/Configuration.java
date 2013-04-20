/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartparam.provider.jdbc.dao.config;

/**
 * @author Przemek Hertel
 */
public interface Configuration {

    String getParameterTable();

    String getParameterLevelTable();

    String getParameterEntryTable();
}
