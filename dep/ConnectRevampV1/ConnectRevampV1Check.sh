#!/usr/bin/bash
d=`date '+%Y%m%d'`
dd2=`date '+%H%M'`
dh=`date '+%M'`

echo "Checking the Connect Revamp V1 Tool at `date` " >> /app/ConnectRevampV1/log/CheckLog_$d.log
overdrafttool=`ps -ef | grep "ConnectRevampV1.jar" | grep -v grep | wc -l `
if [ $overdrafttool -gt 0 ]
then
echo "The  Connect Revamp V1 Tool is working properly " >> /app/ConnectRevampV1/log/CheckLog_$d.log
else
echo "Starting Connect Revamp V1 Tool " >> /app/ConnectRevampV1/log/CheckLog_$d.log
cd /app/ConnectRevampV1
nohup java -jar ConnectRevampV1.jar &
echo "The Connect Revamp V1 Tool is started successfully on the NEW IN APP Server " >> /app/ConnectRevampV1/log/CheckLog_$d.log
cd /export/home/SMSNotfication
echo "The Connect Revamp V1 Tool on the NEW IN APP Server B was down ... The Tool  is started successfully on the NEW IN APP Server B at `date` " > /export/home/SMSNotfication/InputScripts/NotificationSMS.txt
/export/home/SMSNotfication/ScriptSMSsend.sh 1 &
fi
if [ $dh -eq 00 ] || [ $dh -eq 30 ] ; then
filecount=`ls /app/ConnectRevampV1/INPUT/Ready/*.txt | wc -l`
if [ $filecount -gt 300 ]
then
echo "The Connect Revamp Input V1 Directory have $filecount files accumulated " >> /app/ConnectRevampV1/log/CheckLog_$d.log
echo "The Connect Revamp Input V1 Directory have $filecount files accumulated " > /export/home/SMSNotfication/InputScripts/NotificationSMS.txt
/export/home/SMSNotfication/ScriptSMSsend.sh 1 &
fi
fi

