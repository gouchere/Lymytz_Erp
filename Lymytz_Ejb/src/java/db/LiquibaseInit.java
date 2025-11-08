/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 *
 * @author dowes
 */
@Startup
@Singleton
public class LiquibaseInit {

    @Resource(lookup = "jdbc/lymytz-erp")
    private DataSource dataSource;

    private final Logger logger = Logger.getLogger(getClass().getName());

    @PostConstruct
    public void init() {
        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(
                    "db/changelog.xml",
                    new ClassLoaderResourceAccessor(getClass().getClassLoader()),
                    database
            );

            liquibase.update((String) null);
            System.out.println("Liquibase exécuté via la datasource JTA.");
        } catch (Exception ex) {
            Logger.getLogger(LiquibaseInit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
