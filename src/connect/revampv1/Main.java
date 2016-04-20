/*
    This is the main class for ConnectRevampV1.
    the main method is where the execution begins
 */
package connect.revampv1;

import com.etisalatmisr.smpp.SMSSender;
import static connect.revampv1.Util.getLogLevel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

/**
 *
 * @author Amir.Rashed
 */

public class Main {
    
    static Logger logger;
    static Properties properties;
    static ExecutorService pool;
    static int counter;
    static Set<Future<String>> resultsHashSet = new LinkedHashSet<Future<String>>();
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SMSSender smscSender;
    public static void main(String args[]) {
        
        
        logger = Logger.getLogger(Main.class);
        Util.intializeLogger(logger);
        
        if(!initiatePropertiesFile())
            return;
        initiateSMSCConfigurations();
        Globals.NUMBER_OF_THREADS = Integer.parseInt(properties.getProperty("NumberOfThreads"));
        Globals.DIRECTORY_PATH =properties.getProperty("HOME_DIRECTORY");
        Util.setGlobals();
        logger.debug("Connect Revamp's Input Folder : " + Globals.WATCHED_DIRECTORY );
        ArrayList<File> BulkFiles = new ArrayList<File>(Globals.NUMBER_OF_THREADS);
        Globals.IS_OSWIN = Util.getOSType();
        pool = Executors.newCachedThreadPool();
        
        File file = new File(Globals.WATCHED_DIRECTORY);
        while(true){
            try{
                Util.checkLogCapacity(logger);
            } catch(Exception ex){
                logger.error("Error in checking for log file size");
            }
            File[] directoryListing = file.listFiles();
            
            if(directoryListing != null && directoryListing.length > 0){
                logger.debug("Number of files in the Watched folder is : "+ directoryListing.length);
                counter =0;
                for(int i=0; i< directoryListing.length;i++){
                    try {
                        Util.archiveWatchDoLogFile(logger);
                        
                    } catch(Exception ex){
                        logger.error("Error in accumulating Watch Dog logs : " + ex);
                    }
                    try {
                        Util.accumulateLogs();
                    }catch(Exception ex){
                        logger.error("Error in accumulating logs : " + ex);
                    }
                    
                    if(counter >= Globals.NUMBER_OF_THREADS){
                        logger.debug("Checking the number of threads running");
                        while(!CheckBulkFiles(BulkFiles)){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                logger.error("Thread sleeping fails");
                            }
                        }
                        if(CheckBulkFiles(BulkFiles)){
                            counter =0;
                            BulkFiles = null;
                            BulkFiles=new ArrayList<File>(Globals.NUMBER_OF_THREADS);
                        }
                        
                    }
                    
                    try{
                        logger.debug("File being processed : "+ directoryListing[i].getName());
                        HandleFile(directoryListing[i]);
                        BulkFiles.add(directoryListing[i]);
                        logger.debug("Done Processing file : "+ directoryListing[i].getName());
                    } catch(Exception ex){
                        logger.error("Error in Handling file : "+ directoryListing[i].getAbsolutePath());
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    logger.error("Thread sleeping fails");
                }
            }
        }
        
        
    }
    
    private static void HandleFile(File toFile) {
        File currentFile = toFile;
        try {
            if(currentFile.getAbsolutePath().toLowerCase().contains("tmp")){
                return;
            }
        }catch(Exception ex){
            logger.error("A Temp File check caused an Exception : " + ex);
        }
        try{
            SlaveThread a = new SlaveThread(currentFile,"T"+counter,properties,smscSender);
            a.run();
            counter++;
        } catch(Exception ex){
            logger.error("Error in initializing the Thread : " + ex);
        }
        counter++;
    }

    public static boolean initiatePropertiesFile(){
        properties = new Properties();
        properties.clear();
        try {
            File file = new File(System.getProperty("user.dir")+"/Resources/config.properties");
            logger.debug("Resource File Path : "+ file.getAbsolutePath());
            FileInputStream fileInput = new FileInputStream(file);
            properties.load(fileInput);
            fileInput.close();
            logger.setLevel(getLogLevel(properties.getProperty("LogLevel")));
            return true;
        } catch (FileNotFoundException ex) {
            logger.error("Config File Not Found : " + ex.getMessage());
            
        } catch (IOException ex) {
            logger.error("Config File Parsing Error: " + ex.getMessage());
        }
        return false;
    }
    
    public static boolean CheckBulkFiles(ArrayList<File> BulkFiles){
        
        for(int i=0; i<BulkFiles.size();i++){
            File newFile = null;
            if(Globals.IS_OSWIN){
                newFile = new File(Globals.WORK_DIRECTORY+"\\"+BulkFiles.get(i).getName());
            } else {
                newFile = new File(Globals.WORK_DIRECTORY+"/"+BulkFiles.get(i).getName());
            }
            if(newFile.isFile())
                return false;
        }
        return true;
    }

    public static void initiateSMSCConfigurations(){
        Properties smsSenderProperties = new Properties();
        FileInputStream fileInput;
        File smsPropertiesFile = new File(System.getProperty("user.dir")+"/Resources/smpp.cfg");
        logger.debug("the SMSC config file : "+ smsPropertiesFile.getAbsolutePath());
        try {
            fileInput = new FileInputStream(smsPropertiesFile);
        } catch (FileNotFoundException ex) {
            logger.error("SMSC config file is not found");
            return;
        }
        try {
            smsSenderProperties.load(fileInput);
        } catch (IOException ex) {
            logger.error("Error in loading the SMSC config ");
            return;
        }

            
        
        try {
            smscSender = new SMSSender(smsSenderProperties, 1);
            logger.debug("SMSSender initialized successfully");
        } catch (Exception ex) {
            logger.error("Error in initializing the SMSSender Object");
            logger.error(ex);
        }
        
    }
}
