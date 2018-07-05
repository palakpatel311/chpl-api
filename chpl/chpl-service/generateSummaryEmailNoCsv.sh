#!/bin/bash
(set -o igncr) 2>/dev/null && set -o igncr; # this comment is required to trick cygwin into dealing with windows vs. linux EOL characters

# to generate the XML file regularly, add a line to a crontab on the machine hosting the application that looks something like:
# 15 5 * * * cd /some/directory/chpl-api/chpl/chpl-service && ./generateXml.sh
# This will run it at 0515 UTC, which (depending on DST) is 0015 EST

# deal with spaces in filenames by saving off the default file separator (including spaces)
# and using a different one for this application
SAVEIFS=$IFS
IFS=$(echo -en "\n\b")

# get start date as same day of week as today but within the 7 days on or before 4-1-2016
STARTDATE=$(date -d '2016-04-01' "+%Y-%m-%d")
STARTDATEDOW=$(date -d "$STARTDATE" "+%u")
DAYOFWEEK=$(date "+%u")
while [[ $STARTDATEDOW -ne $DAYOFWEEK ]]
do
        STARTDATE=$(date -d "$STARTDATE - 1 day" '+%F')
        STARTDATEDOW=$(date -d "$STARTDATE" "+%u")
done

# get current time as formatted timestamp to input as command-line argument
ENDDATE=$(date "+%F")

# GenerateSummaryEmail application takes three parameters: startDate, endDate, numDaysInPeriod. The dates have this format: yyyy-mm-dd
java -Xmx800m -Dchpl.home=/opt/chpl -Dchpl.appName=generateSummaryEmailNoCsv -Dlog4j.configurationFile=log4j2-app.xml -cp target/chpl-service-jar-with-dependencies.jar gov.healthit.chpl.app.statistics.SummaryStatistics $STARTDATE $ENDDATE 7 false

# restore filename delimiters
IFS=$SAVEIFS
