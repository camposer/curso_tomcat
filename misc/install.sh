#!/bin/bash

# Configuración de Web Server sobre Amazon Linux AMI 2013.09.2 - ami-5256b825 (64-bit) 
# Referencias:
# - http://alextheedom.wordpress.com/cloud/amazon-free-usage-tier-installing-tomcat-7-on-an-ec2-linux-instance/
# - https://www.owasp.org/index.php/Securing_tomcat
# - http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/install-LAMP.html

JAVA_HOME=/usr/java/jdk1.7.0_51
CATALINA_HOME=/usr/share/apache-tomcat-7.0.50
STATIC_HOME=/static
WAR_FILE=
FRONT_FILE=
DB_HOST=
DB_NAME=
DB_USER=
DB_PASS=
MONGODB_HOST=
MONGODB_NAME=

##
# Archivos de configuración
function write_tomcat_init_script {
	cat <<EOF >/etc/rc.d/init.d/tomcat
#!/bin/sh
export JAVA_HOME=$JAVA_HOME
export CATALINA_HOME=$CATALINA_HOME

/bin/su -s /bin/bash tomcat \$CATALINA_HOME/bin/catalina.sh stop
/bin/su -s /bin/bash tomcat \$CATALINA_HOME/bin/catalina.sh start
EOF
}

function write_application_properties {
	cat <<EOF >$STATIC_HOME/tomcat-conf/application.properties
#######################################################
#######################################################
#                     PATHS                           #
#######################################################
#######################################################

uris.path.signFiles = $STATIC_HOME/signfiles
uris.path.signFilesTemp = $STATIC_HOME/signfilesTemp
uris.path.logoComunitiesFiles = $STATIC_HOME/communities
uris.path.logoComunitiesFilesTemp = $STATIC_HOME/communities/temp

#######################################################
#######################################################
#                  DATABASE                           #
#######################################################
#######################################################

database.driverClassName=com.mysql.jdbc.Driver
database.url=jdbc:mysql://$DB_HOST:3306/$DB_NAME?createDatabaseIfNotExist=true&amp;amp;useUnicode=true&amp;amp;characterEncoding=utf-8&amp;amp;autoReconnect=true
database.username=$DB_USER
database.password=$DB_PASS

#######################################################
#######################################################
#                     MONGO                           #
#######################################################
#######################################################
mongo.server=$MONGODB_HOST
mongo.port=27017
mongo.db=$MONGODB_NAME

#######################################################
#######################################################
#       CLIENT CERTIFICATE AUTHORITY AUTORIDAD        #
#######################################################
#######################################################

client.certauthority.url=
client.certauthority.idca=
client.certauthority.template=
EOF
}

function write_httpd_conf {
	cat <<EOF >>/etc/httpd/conf/httpd.conf

# Configuración ejemplo
ProxyPass /ejemplo-api http://localhost:8080/ejemplo-api
ProxyPassReverse / http://localhost:8080/

Alias $STATIC_HOME/communities $STATIC_HOME/communities
                
<Directory "$STATIC_HOME/communities">
        Options Indexes FollowSymLinks
        AllowOverride None
        Order allow,deny
        Allow from all
</Directory>
EOF
}

function write_catalina_properties {
	cat <<EOF >>$CATALINA_HOME/conf/catalina.properties

# Configuración ejemplo
spring.profiles.active=prod
ejemplo-api.config=$STATIC_HOME/tomcat-conf/
ejemplo-api.debug=false
log4j.hostname=[Produccion]
EOF
}

## 
# Funciones de configuración
function configure_user_httpd {
	# Agregando nuevo grupo para servidores
	groupadd www

	# Agregando ec2-user al grupo www
	usermod -a -G www ec2-user
}

function configure_user_tomcat {
	# Agregando nuevo usuario y grupo
	useradd -d $CATALINA_HOME -M -U -s /sbin/nologin tomcat
}

function configure_static_dir {
	# TODO: Esta carpeta debería montarse con fstab
	mkdir $STATIC_HOME
}

function configure_static_dirs {
	# TODO: Agregar permisos sugeridos por Miguel
	mkdir $STATIC_HOME/signfiles
	chown tomcat:tomcat $STATIC_HOME/signfiles

	mkdir $STATIC_HOME/signfilesTemp
	chown tomcat:tomcat $STATIC_HOME/signfilesTemp

	mkdir $STATIC_HOME/communities
	chown tomcat:www $STATIC_HOME/communities

	mkdir $STATIC_HOME/communities/temp
	chown tomcat:www $STATIC_HOME/communities/temp
}

function configure_ejemplo_api {
	# Creando directorios de contenido estático
	configure_static_dirs

	# Configuración de mod_proxy
	write_httpd_conf

	# Configuración de catalina.properties
	write_catalina_properties

	# Creando application.properties
	write_application_properties
}

##
# Funciones de instalación
function init {
	yum update -y
}

function install_java {
	# Descarga JDK
	wget --no-cookies --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com"  http://download.oracle.com/otn-pub/java/jdk/7u51-b13/jdk-7u51-linux-x64.rpm

	# Instalación JDK
	rpm -i jdk-7u51-linux-x64.rpm

	# Configuración de variables de entorno
	cat <<EOF >> ~ec2-user/.bashrc
export JAVA_HOME=$JAVA_HOME
export PATH=\$JAVA_HOME/bin:\$PATH
EOF
	. ~ec2-user/.bashrc
}

function install_tomcat {
	# Descarga Tomcat
	wget http://apache.rediris.es/tomcat/tomcat-7/v7.0.50/bin/apache-tomcat-7.0.50.tar.gz

	# Instalación Tomcat
	tar -xf apache-tomcat-7.0.50.tar.gz -C /usr/share

	# Asegurando instalación
	chown -R tomcat:tomcat $CATALINA_HOME
	#chmod -R 400 $CATALINA_HOME/conf
	rm -rf $CATALINA_HOME/webapps/*
	# TODO: Falta agregar más configuraciones!! (ver aseguramiento de tomcat en Referencias)

	# Script de inicialización
	write_tomcat_init_script

	# Dando permisos de ejecución
	chmod 755 /etc/rc.d/init.d/tomcat
}

function install_httpd {
	# Agregando grupo
	yum grouplist "Web Server"

	# Instalando Apache HTTP Server
	yum install httpd24.x86_64

	# Configurando servicio para iniciar al reiniciar
	chkconfig httpd on

	# Asignando permisos
	chown -R root:www /var/www
	chmod 2775 /var/www
	find /var/www -type d -exec chmod 2775 {} +
	find /var/www -type f -exec chmod 0664 {} +
}

function install_ejemplo_api {
	# Copiando war en la ruta correspondiente
	cp $WAR_FILE $CATALINA_HOME/webapps/ejemplo-api.war
}

function install_mongo {
	# Descargando MondoDB
	wget http://downloads-distro.mongodb.org/repo/redhat/os/x86_64/RPMS/mongo-10gen-server-2.4.9-mongodb_1.x86_64.rpm
	wget http://downloads-distro.mongodb.org/repo/redhat/os/x86_64/RPMS/mongo-10gen-2.4.9-mongodb_1.x86_64.rpm
	
	# Instalando MondoDB
	rpm -i mongo-10gen-2.4.9-mongodb_1.x86_64.rpm mongo-10gen-server-2.4.9-mongodb_1.x86_64.rpm

	# TODO: Falta incluir configureciones del Cluster
}

function install_ejemplo_front {
	# Descomprimiendo front
	unzip -x $FRONT_FILE
	
	# Moviendo al DocumentRoot del Apache
	mv $FRONT_FILE/espacioTrabajoejemploFront/* /var/www/html
	
	# Configurando permisos
	chown -R www:www /var/www/html/*
}

function init_servers {
        # Iniciando httpd
        service httpd restart

	# Iniciando tomcat
	/etc/rc.d/init.d/tomcat
}

##
# Main
if [[ $EUID -ne 0 ]]; then
   echo "Este script debe ejecutarse como root" 1>&2
   exit 1
fi

#init
#configure_user_tomcat
#install_tomcat
#configure_user_httpd
#install_httpd
#install_mongo
#configure_static_dir
#configure_ejemplo_api
#install_ejemplo_api
#install_ejemplo_front
#init_servers
