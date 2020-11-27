# opendns-utility
A springboot web application allowing block/unblock/clear/list blocked domains. It uses Selenium to automatically login and update the changes. The following functions are suported.

1) list: show current blocked domains in the currently network id (see below for how to get the network id from opendns)
2) block: blocks a domain or a group of domains. A group of domains can be defined in application.properties in the format of, for example, youtube.domains=domain1, domain ...
3) unblock: unblock a domain of a domain group
4) clear: unblock all existing blocked domains

# Prerequisit
Maven, Spring Tools Suite (STS), opendns account

# Setup
1) change the username and password in application properties
2) get the settings ID from Open DNS. Go to settings tab, select a network IP, in the address bar it shows an id in the url 
https://dashboard.opendns.com/settings/1234567/content_filtering where the network id is 1234567. Set the opendns.settings.network.id=1234567 in application properties
3) the chrome driver already included in the lib folder. Change the WebDriverHelper class to update the value CHROME_DRIVER_DIRECTORY so that it points to absolute folder of the lib directory containing the chrome driver.

# Start the application
After making changes, go do the selinum-web folder and run:  
mvn clean package -Dmaven.test.skip
mvn spring-boot:run

# Sample Usage
curl localhost:9090/list
curl localhost:9090/block/chesskid.com
curl localhost:9090/unblock/chesskid.com
curl localhost:9090/block/youtube    --> here youtube is defined as a group of domains in application.properties
curl clear

# Other Usage
Use cron job to block youtube during daytime (if you have small school age children) and unblock youtube after school over



