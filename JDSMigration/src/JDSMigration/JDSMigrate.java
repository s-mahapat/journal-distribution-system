/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

/**
 *
 * @author Shailendra Mahapatra
 */
public class JDSMigrate {

    private static final Logger logger = Logger.getLogger(JDSMigrate.class);

    // set this to override all other migration flags
    private boolean MIGRATE_ALL = true;

    private boolean INIT_MASTER_DATA = true;

    private boolean MIGRATE_INWARD = MIGRATE_ALL && true;
    private boolean MIGRATE_SUBSCRIBER = true;
    private boolean MIGRATE_SUBSCRIPTION = true;
    private boolean MIGRATE_CORR = MIGRATE_ALL && true;
    private boolean MIGRATE_FELLOWS = MIGRATE_ALL && true;
    private boolean MIGRATE_ASSOCIATES = MIGRATE_ALL && true;
    private boolean MIGRATE_EBALL = MIGRATE_ALL && true;
    private boolean MIGRATE_HONFEL = MIGRATE_ALL && true;
    private boolean MIGRATE_JGRANT = MIGRATE_ALL && true;
    private boolean MIGRATE_EXCHANGE = MIGRATE_ALL && true;

    private boolean CURRMEM = MIGRATE_ALL && true;
    private boolean CURTWAS = MIGRATE_ALL && true;
    //private boolean MIGRATE_CURR = MIGRATE_ALL || false;
    private boolean MIGRATE_CURRIEX = MIGRATE_ALL && true;
    private boolean MIGRATE_CLIFESUB = MIGRATE_ALL && true;
    private boolean MIGRATE_CURRWC = MIGRATE_ALL && true;
    private boolean MIGRATE_CURREB = MIGRATE_ALL && true;
    private boolean MIGRATE_CURRGRAN = MIGRATE_ALL && true;
    private boolean MIGRATE_CURRFEX = MIGRATE_ALL && true;
    private boolean MIGRATE_HON = MIGRATE_ALL && true;
    private boolean MIGRATE_MEMBER = MIGRATE_ALL && true;

    //private boolean MIGRATE_RES = MIGRATE_ALL || false;
    private boolean MIGRATE_RESOCOMP = MIGRATE_ALL && true;
    private boolean MIGRATE_RESOEB = MIGRATE_ALL && true;

    private boolean CIRCULATION_FIGURES = MIGRATE_ALL && true;


    public static void main(String[] args) throws IOException, FileNotFoundException,
            ParseException, SQLException, BiffException, InvocationTargetException,
            IllegalAccessException, Exception{

        JDSMigrate _jdsmigrate = new JDSMigrate();
        HashMap<String, String> agentSubscriberMap = new HashMap<>();

        // This function sets up the master data as well clear all the transaction data
        if (_jdsmigrate.INIT_MASTER_DATA) {
            MigrationBase _migrationBase = new MigrationBase();
            _migrationBase.executeMasterDataScripts();
            JDSMigrate.logger.fatal("Setup master data and trucated transaction data");
        }

        if (_jdsmigrate.MIGRATE_INWARD) {
            OldInward _oldinward = new OldInward("INW2009.txt");
            _oldinward.Migrate();
            String[] inwardFiles = {"INW2010.txt", "INW2011.txt", "INW2012.txt", "NEWINW.txt"};
            Inward _inward;
            for (int i = 0; i < inwardFiles.length; i++) {
                _inward = new Inward(inwardFiles[i]);
                _inward.Migrate();
            }

        }
        if (_jdsmigrate.MIGRATE_SUBSCRIBER) {
            // the subscriber table will be truncated here
            IndTemp _subscriber = new IndTemp(agentSubscriberMap);
            _subscriber.Migrate();

            // the subscriber table will ***NOT** be truncated here
            Temp _subscriber2 = new Temp(agentSubscriberMap);
            _subscriber2.Migrate();
        }
        if (_jdsmigrate.MIGRATE_SUBSCRIPTION) {
            Subscription _subscription = new Subscription(agentSubscriberMap);
            _subscription.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CORR) {
            Corr _corr = new Corr();
            _corr.Migrate();
        }
        if (_jdsmigrate.MIGRATE_FELLOWS) {
            migrateFellows _fellows = new migrateFellows();
            _fellows.Migrate();
        }
        if (_jdsmigrate.MIGRATE_ASSOCIATES) {
            migrateAssociates _associates = new migrateAssociates();
            _associates.Migrate();
        }
        if (_jdsmigrate.MIGRATE_EBALL) {
            migrateEBALL _editorialBoardMembers = new migrateEBALL();
            _editorialBoardMembers.Migrate();
        }
        if (_jdsmigrate.MIGRATE_HONFEL) {
            migrateHONFEL _honfel = new migrateHONFEL();
            _honfel.Migrate();
        }
        if (_jdsmigrate.MIGRATE_JGRANT) {
            migrateJGRANT _jgrant = new migrateJGRANT();
            _jgrant.Migrate();
        }
        if (_jdsmigrate.MIGRATE_EXCHANGE) {
            migrateExchange _exchange = new migrateExchange();
            _exchange.Migrate();
        }
        if(_jdsmigrate.CURRMEM){
            CurrMem _currmem = new CurrMem();
            _currmem.Migrate();
        }
        if(_jdsmigrate.CURTWAS){
            CurtWas _curtwas = new CurtWas();
            _curtwas.Migrate();
        }

        if (_jdsmigrate.MIGRATE_CURRIEX) {
            CURRIEX _migrateiex = new CURRIEX();
            _migrateiex.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CLIFESUB) {
            CLIFESUB _migrateclife = new CLIFESUB();
            _migrateclife.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CURRWC) {
            CURRWC _migrate = new CURRWC();
            _migrate.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CURREB) {
            CURREB _migrate = new CURREB();
            _migrate.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CURRGRAN) {
            CURRGRAN _migrate = new CURRGRAN();
            _migrate.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CURRFEX) {
            CURRFEX _migrate = new CURRFEX();
            _migrate.Migrate();
        }
        if (_jdsmigrate.MIGRATE_HON) {
            HON _migrate = new HON();
            _migrate.Migrate();
        }
        if (_jdsmigrate.MIGRATE_MEMBER) {
            member _migrate = new member();
            _migrate.Migrate();
        }

        if (_jdsmigrate.MIGRATE_RESOCOMP) {
            RESOCOMP _resocomp = new RESOCOMP();
            _resocomp.Migrate();
        }
        if (_jdsmigrate.MIGRATE_RESOEB) {
            RESOEB _resoeb = new RESOEB();
            _resoeb.Migrate();
        }
        if (_jdsmigrate.CIRCULATION_FIGURES) {
            circulationFigures _circulationFigures = new circulationFigures();
            _circulationFigures.getCount();
        }

//        } catch (IOException | ParseException | SQLException e) {
//
//            logger.fatal(e.getMessage());
//            StackTraceElement elements[] = e.getStackTrace();
//            for (int i = 0, n = elements.length; i < n; i++) {
//                logger.fatal("--------------- start --------------------");
//                logger.fatal("Message: " + elements[i].toString());
//                logger.fatal("Class: " + elements[i].getClassName());
//                logger.fatal("Method Name: " + elements[i].getMethodName());
//                logger.fatal("Line Number: " + elements[i].getLineNumber());
//                logger.fatal("File Name: " + elements[i].getFileName());
//                logger.fatal("--------------- end --------------------");
//            }
//        } catch (Exception e) {
//            logger.fatal(e.getMessage());
//            StackTraceElement elements[] = e.getStackTrace();
//            for (int i = 0, n = elements.length; i < n; i++) {
//                logger.fatal("--------------- start --------------------");
//                logger.fatal("Message: " + elements[i].toString());
//                logger.fatal("Class: " + elements[i].getClassName());
//                logger.fatal("Method Name: " + elements[i].getMethodName());
//                logger.fatal("Line Number: " + elements[i].getLineNumber());
//                logger.fatal("File Name: " + elements[i].getFileName());
//                logger.fatal("--------------- end --------------------");
//            }
//        }
    }
}
