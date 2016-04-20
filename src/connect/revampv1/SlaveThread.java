/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect.revampv1;


import com.etisalatmisr.smpp.SMSSender;
import connect.revampv1.Globals.TemplateParameterType;
import connect.template.TemplateParameter;
import connect.template.TemplateParameterUnit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Callable;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.Logger;




/**
 *
 * @author amir.rashed
 */
public class SlaveThread implements Runnable {
    
    File currentFile;
    String counter;
    Properties properties;
    String tool="";
    Logger logger;
    String appenderName;
    SMSSender smscSender;
    public SlaveThread(File newFile,String counter,Properties properties,SMSSender smscSender){
        
        this.counter = counter;
        this.properties = properties;
        intializeLogger();
        
        String filePath = (moveFile(newFile, Globals.WORK_DIRECTORY));
        currentFile = new File(filePath);
        this.smscSender = smscSender;
    }
    
    public void execute(){

        //process the file.
        
        try{
            if(currentFile == null || !currentFile.isFile())
                throw new Exception();
            File newFile = currentFile;
            processFile(newFile);
            //archive the file.
            archiveFile(newFile);
        }catch(Exception e){
            logger.error("Exception in reading file" + Globals.WORK_DIRECTORY+currentFile.getName());
        }   
        stopThread();
//        try{
//            throw new Exception();
//        }catch(Exception e){
//            logger.error("Execution stopped for file : " + Globals.WORK_DIRECTORY+currentFile.getName());
//        }        
    }

    public void stopThread(){
        logger.debug("Thread Stopped for file : "+ currentFile.getName());
        logger.debug("------------------------------------------------------------------------------------------------");
        logger.getAppender(appenderName).close();
        //LogManager.shutdown();
    }
    
    public  void intializeLogger(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        RollingFileAppender appender = new RollingFileAppender();
        
        appender.setAppend(true);
        appender.setMaxFileSize("1MB");
        appender.setMaxBackupIndex(1);
        String fileName="logs/ConnectRevamp_ThreadLog_"+dateFormat.format(new Date())+"_"+this.counter;
        appenderName = fileName;
        appender.setName(appenderName);
        logger = Logger.getLogger(fileName);    
        appender.setFile(fileName + ".log");
        PatternLayout layOut = new PatternLayout();
        layOut.setConversionPattern("%d{yyyy-MM-dd HH:mm:ss} %-5p :%L - %m%n");
        appender.setLayout(layOut);
        appender.activateOptions();
        logger.removeAllAppenders();
        logger.addAppender(appender);
        logger.debug("Log appended : " + fileName);
        
    }

    public  void processFile(File workFile){
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(workFile.getAbsolutePath());
            sc = new Scanner(inputStream, "UTF-8");
            logger.debug("stating to processing File "+workFile.getName()+" line by line ");
            int linecounter = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                try{
                    processLineConnectRevamp(line,linecounter);
                }catch(Exception ex){
                    logger.error("Processing line failed for line : " + line);
                }
                linecounter++;
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } 
        catch (FileNotFoundException ex) {
            logger.error("Method : processFile");
            logger.error("File not found exception for file : " + workFile.getAbsolutePath());
        }
        catch(IOException ex){
            logger.error("Method : processFile");
            logger.error("Error in parsing file : " + workFile.getAbsolutePath());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    logger.error("Method : processFile");
                    logger.error("Error in closing input stream while parsing file : " + workFile.getAbsolutePath());
                }
            }
            if (sc != null) {
                sc.close();
                logger.debug("Scanner is closing for file : "+workFile.getName());
                
            }
        }
    }

    public  void processLineConnectRevamp(String line, int lineCounter) {
        StringBuilder sb = new StringBuilder();
        String [] templates =null;
        String [] lineFields=null;
        ConnectRevampCDR currentCDR=null;        
        String arabicSMS = null;
        ArrayList<TemplateParameter> allParameters =null;
        
        try{
            lineFields = line.split(",",-1);
            currentCDR = new ConnectRevampCDR(lineFields);

            logger.debug("CDR : "+ line);
        }catch(Exception e){
            logger.error("Error in parsing CDR : "+ line);
        }
       
        try{
             templates = currentCDR.getTemplates().split(":",-1);
        }catch(Exception e){
            logger.error("Error in parsing Templates : "+ line);
        }
        for(int i=0; i<templates.length-1;i++){
            
            try{
                allParameters = HandleTemplate(templates[i]);
            }catch(Exception e){
                logger.error("Error in Hanlding Parameter : "+ templates[i]);
            }
            
            try{
                arabicSMS = properties.getProperty(templates[i]).split(",")[properties.getProperty(templates[i]).split(",").length-1];
            }catch(Exception e){
                logger.error("Error in getting the arabic SMS ");
            }
            if(allParameters != null){
                for(int j=0; j<allParameters.size();j++){
                    try{
                        if(allParameters.get(j).getTemplateParameterUnit().getTemplateParameterUnitType() == TemplateParameterType.DATESTAMP)
                            allParameters.get(j).setTemplateParameterValue(currentCDR.getParametersValue().get(j));
                        else
                            allParameters.get(j).setTemplateParameterValue(currentCDR.getParametersValue().get(j).split(":",-1)[i]);
                        int k=j+1;
                        arabicSMS = arabicSMS.replace("$param"+k, allParameters.get(j).getTemplateParameterValue());
                    }
                    catch(Exception e){
                        logger.error("Error in Parsing Parameter :  " + currentCDR.getParametersValue().get(j));
                    }
                }
            }
            
            try{
                sendSMS(properties.getProperty(templates[i]).split(",")[0],currentCDR.msisdn,arabicSMS, lineCounter);
                logger.debug("Sucessfully sent SMS for the template : "+templates[i]);
            }
            catch(Exception e){
                    logger.error("Error in Sending SMS");
                }
        }
        
    }
    
    public  void sendSMS(String sender,String dial,String toString,int lineCounter) {

        // For Deployment
          try{
            String [] smsSplit = toString.split(",");
            boolean result = smscSender.sendMessage(sender, "0"+dial, toString,2);
            logger.debug("Successfully Sending SMS");
            if(result){
               logger.debug(toString);
               logger.debug("Successfully Sent SMS to the dial "+dial); 
            } else 
            {
                logger.debug(toString);
                logger.debug("Failed to send SMS to the dial  "+dial); 
            }
        }catch(Exception e){
            logger.error("Failed to send SMS to the dial  "+dial); 
        }  
        
        //For Testing
//        Writer writer = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        String currentFileName ="";
//        File resultFile = null;
//        File newFile  = null;
//        if(Util.getOSType() == Globals.OS_UNIX){
//            currentFileName = Globals.SMS_PREPARATION_DIRECTORY+"ConnectRevamp_"+sdf.format(new Date())+"_T"+dial+"_L"+lineCounter+currentFile.getName();
//            resultFile = new File(currentFileName);
//            newFile = new File(Globals.SMS_DIRECTORY+resultFile.getName());
//                    
//        }
//        else{
//            currentFileName = Globals.SMS_PREPARATION_DIRECTORY+"ConnectRevamp_"+sdf.format(new Date())+"_T"+dial+"_L"+lineCounter+currentFile.getName();
//            resultFile = new File(currentFileName);
//            newFile = new File(Globals.SMS_DIRECTORY+resultFile.getName());
//        }
//        
//        
//        if(!resultFile.exists())
//            try {
//                resultFile.createNewFile();
//        } catch (IOException ex) {
//           logger.error("Cannot create the SMS file in the SMS Preparation Directory: "+ currentFileName);
//        }
//        try {
//            FileWriter fw = new FileWriter(resultFile,true);
//            //BufferedWriter writer give better performance
//            BufferedWriter bw = new BufferedWriter(fw);
//            bw.append(toString);
//            bw.close();
//            // Send the file to the SMS tool
//            //logger.info("Deleting moved file : "+ newFile.delete());
//            try{
//            Files.move(Paths.get(resultFile.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()),StandardCopyOption.REPLACE_EXISTING);
//            logger.info("File "+ newFile.getAbsolutePath()+" is moved to the SMS Directory : "+ Globals.SMS_DIRECTORY);
//            }catch(Exception e){
//                logger.error("Failed to move File "+ resultFile.getAbsolutePath()+" to the SMS Directory : " + Globals.SMS_DIRECTORY + " under name : " + newFile.getName());
//                logger.error("Exception : "+ e);
//            }    
//            if(resultFile.renameTo(newFile)){
//              logger.info("File "+ newFile.getAbsolutePath()+" is moved to the SMS Directory");
//            }
//            else {
//               logger.error("Failed to move File "+ resultFile.getAbsolutePath()+" to the SMS Directory : " + Globals.SMS_DIRECTORY + " under name : " + newFile.getName());
//            }
//        } catch (UnsupportedEncodingException ex) {
//            logger.error("Error in writing in file : "+ currentFileName);
//        } catch (FileNotFoundException ex) {
//            logger.error("File not found : "+ currentFileName);
//        } catch (IOException ex) {
//            logger.error("IO Exception: "+ currentFileName);;
//        }
    }
    
    public  void archiveFile(File currentFile){
        File newFile = null;
        
        SimpleDateFormat  sdf1 = new SimpleDateFormat("yyyyMMdd");
        try {
            if(Globals.IS_OSWIN){
                File file = new File (Globals.ARCHIVE_DIRECTORY+sdf1.format(new Date()));
                if (!file.exists()) {
                    if (file.mkdir()) {
                            logger.debug("Directory "+file.getAbsolutePath()+" is created!");
                            newFile = new File(file.getAbsolutePath()+"\\"+currentFile.getName());
                    } else {
                            logger.error("Failed to create directory "+ file.getAbsolutePath());
                    }
                }
                else {
                    logger.debug("Directory "+file.getAbsolutePath()+" already exists");
                    newFile = new File(file.getAbsolutePath()+"\\"+currentFile.getName());
                }
            }
            else {
                File file = new File (Globals.ARCHIVE_DIRECTORY+sdf1.format(new Date()));
                if (!file.exists()) {
                    if (file.mkdir()) {
                            logger.debug("Directory "+file.getAbsolutePath()+" is created!");
                            
                    } else {
                            logger.debug("Failed to create directory "+ file.getAbsolutePath());
                    }
                    
                }
                else {
                    logger.debug("Directory "+file.getAbsolutePath()+" already exists");
                    
                }
                newFile = new File(file.getAbsolutePath()+"/"+currentFile.getName());
            }
        }catch(Exception e){
            logger.error("Exception in creating the archive date folder or setting the path of the file in the archive");
            logger.error(e);
        }
        try{
            Files.move(Paths.get(currentFile.getAbsolutePath()), Paths.get(newFile.getAbsolutePath()),StandardCopyOption.REPLACE_EXISTING);
            logger.debug("File : " + currentFile.getAbsolutePath() + " is moved to the archive directory ");
//            newFile.delete();
//            if(currentFile.renameTo(newFile)){
//
//                logger.debug("File "+ currentFile.getAbsolutePath() + " is moved to " + newFile.getAbsolutePath());
//                logger.debug("work file "+newFile.getName()+" is archived");
//            }
//            else {
//                logger.error("Failed to move "+ currentFile.getAbsolutePath() + " to " + newFile.getAbsolutePath());
//                logger.debug("work file "+newFile.getName()+" was not archived");
//            }
        }catch(Exception e){
            logger.error("Error in moving the file : "+ currentFile.getName() + " To the archive folder");
            logger.error(e);
        }
     
    }

    @Override
    public void run()  {
       execute();      
    }

    public String moveFile(File file, String destination){
        File directory = new File(destination);
        if(!directory.isDirectory()){
            logger.error("Not a directory");
            return "Not a directory";
        }
        if(!file.isFile()){
            logger.error("Not a File");
            return "Not a File";
        }
        File newFile =null;
        if(Globals.IS_OSWIN){
            newFile = new File(destination+"\\"+file.getName());
        }
        else {
            newFile = new File(destination+"/"+file.getName());
        }
        newFile.delete();
        if(file.renameTo(newFile)){
            logger.debug("File "+ file.getName() +" is moved to "+newFile.getAbsolutePath());
            return newFile.getAbsolutePath();
        }
        else {
            logger.debug("Failed to move File "+ file.getName() +" to "+newFile.getAbsolutePath());
            return "Failed to move";
        }
    }

    private ArrayList<TemplateParameter> HandleTemplate(String template) {
        ArrayList<TemplateParameter> allParameters = new ArrayList<TemplateParameter>();
        String templatParametersArray [] = properties.getProperty(template).split(",");
        int numberOfParamters = Integer.parseInt(templatParametersArray[1]);
        if(numberOfParamters == 0){
            //return the arabic SMS Script
            return null;
        }else {
            for(int i=2; i<numberOfParamters+2; i++){
                TemplateParameterUnit templateParameterUnit = TemplateParameterUnit.getTemplateParameterUnit(templatParametersArray[i],logger);
                TemplateParameter currentParameter = new TemplateParameter();
                currentParameter.setTemplateParameterUnit(templateParameterUnit);
                allParameters.add(currentParameter);
            }
            return allParameters;
            
        }
        
        
    }
    
    
}
