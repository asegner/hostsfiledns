---------- HostsFileDNS ----------
=

Bypass VPN DNS for select hosts via...

  Locally cached DNS entries managed in the system host file


# Quickstart

1. Build with `mvn clean install`
2. Run the setup command for your OS
3. Add hostnames to the config file for your OS
4. Never worry about touching the host file again

# Linux/OSX

### Setup

`sudo java -jar hostsfiledns-control-<version>.jar`

* Installs to `/usr/local/lib`, `/usr/local/bin`
* OSX: Creates a LaunchAgent under `/Library/LaunchAgents`
* Linux: Creates a job in the user's crontab

### Config File
`hostsfiledns` under `/etc`

# Windows

### Setup

1. Right click on `Command Prompt` and select `Run as administrator`
2. Run `java -jar hostsfiledns-control-<version>.jar`

 * Creates  a scheduled task and installs to the Program Files folder

### Config File
`hostsfiledns.config` under `<Program Files>\hostsfiledns`

* Editor (eg notepad) must be in _Administrator Mode_ (example in step 1 above)

# The highlights

* The configuration file takes one hostname per line to be managed by hostsfiledns
* Each managed hostname will have one DNS resolved IP stored in the system hosts file
* By default, if the service is installed, the hosts file is checked every three hours
* To update hosts immediately, run either `sudo hostsfiledns -u` or `hostsfiledns.bat -u` in an admin command prompt
* The installing user must have sudo access on Linux/OSX and admin rights on Windows
* Supports Linux, OSX, or Windows
* Requires minimum Java 8 and Maven 3.3.3


# License
 [MIT License](http://www.opensource.org/licenses/mit-license.php)
