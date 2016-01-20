---------- HostsFileDNS ----------
=

Bypass VPN DNS for select hosts via...

  Locally cached DNS entries managed in the system host file


# Quickstart

1. Run the setup command for your OS
2. Add hostnames to the config file for your OS
3. Never worry what a hosts file is again

# Linux/OSX

### Setup

`sudo java -jar hostsfiledns-control.jar --install`

### Config File
`hostsfiledns` under `/etc/`<br/>

# Windows

### Setup

1. Right click on `Command Prompt` and select `Run as administrator`
2. Run `java -jar hostsfiledns-control.jar --install`


### Config File
`hostsfiledns.config` under `<Program Files>\hostsfiledns`

# General Usage

* The configuration file takes one hostname per line to be managed by hostsfiledns
* Each managed hostname will have one DNS resolved IP stored in the system hosts file
* By default, if the service is installed, the hosts file is checked every three hours
* To update hosts immediately, run either `hostsfiledns -u` or `hostsfiledns.bat -u`  Unsure as to which one, try both!
* Supports Linux, OSX, or Windows
