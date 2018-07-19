# EclipseEventLogger

# Getting Started
1. Install Mysql and Eclipse(Oxygen or higher) on to your computer.

2. For easy configuration, create a Mysql user named EclipseEventLogUser with a password of P@ssword, and give that user permissions to create schemas/tables.

3. In Eclipse, click help at the top menu and then install new software.

4. Click to add button at the upper right corner.

5. In the location text box input "http://homepages.uc.edu/~watsonwd/EventLogger/" without the quotes and click ok.

6. Check EclipseEventLogger and continue with the install.

7. Restart Eclipse and begin writing Java code.

8. There should be messages logged in your database at this point, if there aren't please verify your Mysql instance is running, and verify the database information is correct in the file "yourHomeDirectory/EclipseEventLoggerConfig/config.txt" your home directory is your user directory on windows.


# Description
Logs events triggered in Eclipse that relate to syntax and runtime errors. The event's information is stored in a Mysql database specified by the user in a config file located in the directory "yourHomeDirectory/EclipseEventLoggerConfig/config.txt" if you're on a windows computer your home directory will be your windows account name's directory. The File will be generated by the plugin with defaulted credentials to connect to the database, you have the option to create the file on his own. The file is required to be in the following format:
 Server=YourDbServerLocation 
 Database=DesiredDatabaseName 
 Username=MysqlUsername 
 Password=MysqlPassword
 
The ordering of the each piece of information is not important; however, each piece must be separated by a new line. The Mysql user will need permission to create tables. Assuming the given account has the permission the database will be created if it has not already been. From here all runtime and syntax events will be logged in the database.
