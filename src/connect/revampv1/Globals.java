
package connect.revampv1;

/**
 * This is the Globals class for the IN-SMS Tool V1.
 * It contains all the global variables that need to be set only once and used across all the execution.
 * 
 */
public class Globals {
    
//    public static final String WATCHED_DIRECTORY="D:\\Arts\\IN-SMSToolV1\\INPUT\\Ready\\";
//    public static final String SMS_DIRECTORY="/export/home/etisalatSMS/input/"; 
//   public static final String SMS_DIRECTORY="D:\\GitRepository\\ConnectRevampV1\\SMS_DIRECTORY\\";
//    public static final String SMS_DIRECTORY="/arc/INPlanning/ConnectRevampV1/SMS_DIRECTORY/";
    public static  String DIRECTORY_PATH;
    public static  String WATCHED_DIRECTORY;
    public static  String WORK_DIRECTORY;
    public static  String SMS_PREPARATION_DIRECTORY;    
    public static  String INSTANT_LOG_PATH;   
    public static  String ARCHIVE_LOG_DIRECTORY;
    public static  String ARCHIVE_DIRECTORY;

   
    public static final boolean OS_WIN = true;
    
    public static final boolean OS_UNIX = false;
    
    public static boolean IS_OSWIN;
    static int NUMBER_OF_THREADS;
    

    public static enum TemplateParameterType {
    TIME,QUOTA,MONEY, DATESTAMP
 
    }
    
}   
