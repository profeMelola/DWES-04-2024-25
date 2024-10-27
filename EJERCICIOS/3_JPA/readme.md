
# EJERCICIO 1: CRUD JPA de Fabricantes

Partimos de la solución que está en https://github.com/profeMelola/DWES-04-2024-25/tree/main/EJERCICIOS/1_TiendaDAO/tienda_dao

Limpiamos el proyecto para trabajar solo con Fabricantes.

![image](https://github.com/user-attachments/assets/40586e75-aade-433e-822c-c6a92af2fe8d)

## CONFIGURACIÓN
### 1. DEPENDENCIAS

En principio, teniendo la dependencia del profile <artifactId>jakarta.jakartaee-web-api</artifactId>, NO sería necesario añadir una dependencia para Hibernate, ya que Wildfly lleva implementado su propio Hibernate.

```
		<dependency>
			<groupId>jakarta.platform</groupId>
			<artifactId>jakarta.jakartaee-web-api</artifactId>
			<version>${jakartaee-api.version}</version>
			<scope>provided</scope>
		</dependency>
```
Al indicar **scope con valor provided** esta dependencia será provista por el servidor de aplicaciones o el entorno en tiempo de ejecución y no será incluida en el archivo empaquetado.

Esto significa que si estás desplegando tu aplicación en un servidor de aplicaciones que ya implementa Jakarta EE completo (como WildFly, Payara, GlassFish, OpenLiberty, entre otros), no necesitas agregar ninguna otra dependencia para JPA. El servidor ya debería proveer las librerías necesarias en tiempo de ejecución.

WildFly ya trae un proveedor JPA integrado (generalmente Hibernate), por lo que simplemente puedes empezar a utilizar JPA directamente en tu código sin más configuraciones.

Si necesitas una implementación específica, EclipseLink (proveedor por defecto de JakartaEE) y Hibernate son opciones comunes.

___

En nuestro entorno SÍ es necesario para que no nos de error. 

```
		<!-- https://docs.wildfly.org/32/Developer_Guide.html#JPA_Reference_Guide -->
		 <!-- https://hibernate.org/orm/documentation/6.1/ -->
		<dependency>
			<groupId>org.hibernate.orm</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>6.6.1.Final</version>
		</dependency>	
```


Esto va a generar conflicto de dependencias, pero vamos a asumir ese conflicto para tener un entorno funcional, ya que en ejecución se resuelve perfectamente:

![image](https://github.com/user-attachments/assets/0ded7cd2-038e-4ab0-8777-c27ca3f6f8e5)



### 2. CONFIGURAR H2: Añadir contraseña al usuario sa de H2

Es necesario cambiar la contraseña del usuario sa en H2, porque en el datasource del standalone.xml no te deja poner la password en blanco.

```
ALTER USER sa SET PASSWORD 'sa';
```


### 3. CONIGURACIÓN DE PERSISTENCIA EN NUESTRO PROYECTO: configuración de persistence.xml

Dentro de **META-INF**, deberás crear o ajustar este archivo con la configuración adecuada del proveedor de persistencia, como el nombre de tu unidad de persistencia (persistence-unit), la conexión a la base de datos, etc.

```
mi-proyecto
│
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── miapp
│   │   │           ├── model
│   │   │           │   └── Producto.java
│   │   │           └── servlets
│   │   │               └── ListarProductosServlet.java
│   │   ├── resources
│   │   └── webapp
│   │       ├── WEB-INF
│   │       │   ├── web.xml         <!-- Configuración de la aplicación web -->
│   │       │   ├── beans.xml       <!-- Si usas CDI, aquí puedes incluir esta configuración -->
│   │       │   └── librerias.jar    <!-- Dependencias necesarias -->
│   │       ├── META-INF
│   │       │   └── persistence.xml  <!-- Configuración de JPA -->
│   │       ├── productos.jsp        <!-- Vista JSP para listar productos -->
│   │       └── index.html           <!-- Página de inicio u otras vistas -->
└── pom.xml                          <!-- Archivo de configuración de Maven -->

```

**En aplicaciones web simples (Servlets y JSP sin EJB ni CDI), la detección automática de las entidades JPA no se habilita de forma predeterminada.**

En otro tipo de aplicaciones o con frameworks como Spring, no es necesario registrar manualmente todas las entidades en el archivo persistence.xml, ya que JPA permite descubrir automáticamente las entidades mediante las anotaciones en las clases de entidad. 


**COPIA ESTE persistence.xml:**

```
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
    <persistence-unit name="H2DS" transaction-type="JTA">
        <jta-data-source>java:/H2DS</jta-data-source>
        <!-- Si solo estás utilizando Servlets y JSP sin EJB ni CDI, 
         la detección automática de las entidades JPA no se habilita de forma predeterminada -->
        <!--<exclude-unlisted-classes>false</exclude-unlisted-classes>--> <!-- no funciona -->

        <class>es.daw.web.models.Fabricante</class>

        <!-- No necesitas especificar las clases manualmente si usas anotaciones -->
        
        <properties>

            <!-- Otras configuraciones de JPA -->
            <property name="jakarta.persistence.schema-generation.database.action" value="none"/>
        </properties>
    </persistence-unit>
</persistence>



```


### 4. AÑADIR DATA SOURCE: configuración de standalone.xml de Wildfly

Un data source (o fuente de datos) en un servidor de aplicaciones es una configuración que define cómo la aplicación se conecta a una base de datos externa. 

Es, básicamente, un conjunto de parámetros que encapsulan la información necesaria para la conexión, como la URL de la base de datos, el nombre de usuario, la contraseña, el tipo de base de datos y el controlador JDBC (Java Database Connectivity) adecuado.

Los data sources permiten que el servidor de aplicaciones administre y optimice las conexiones a la base de datos, facilitando que las aplicaciones obtengan acceso a los datos de manera eficiente y segura. 

En lugar de que cada aplicación gestione individualmente sus propias conexiones, el servidor de aplicaciones centraliza esta tarea


#### FORMA 1: Con la consola de administración de Wildfly (debes tener permiso de administrador)

Primero debemos crear un usuario administrador. Para ello hay que ejecutar el script **add-user.bat o add-user.sh** del directorio de instalación de Wildfly.

Vamos a crear el **usuario daw con pwd daw**. Le añadimos los grupos admin,management.

**Observa la captura:**

![image](https://github.com/user-attachments/assets/8a2de908-9e83-445a-aa2e-e475eaf8f1a9)

___

Después nos conectamos a la página principal de Wildfly (http://localhost:8080) y accedemos a la consola de administración:

![image](https://github.com/user-attachments/assets/59d6d632-06ba-4d85-a7ce-81822002af0e)

___

Introducimos nuestro login y pwd y procedemos a crear un nuevo Data Source:

![image](https://github.com/user-attachments/assets/361e8d0b-9a18-4215-8be0-63a25346768f)

___


![image](https://github.com/user-attachments/assets/1ca327e2-26b8-480c-af7e-a5787cd7adef)


___

Vemos el fichero de configuración standalone.xml y ahí tenemos nuestro DataSource:

```
<datasource jndi-name="java:/H2DS" pool-name="H2DS">
       <connection-url>jdbc:h2:~/tienda;AUTO_SERVER=TRUE</connection-url>
       <driver-class>org.h2.Driver</driver-class>
       <driver>h2</driver>
       <security user-name="sa" password="sa"/>
       <validation>
             <validate-on-match>true</validate-on-match>
       </validation>
</datasource>
```
___


#### FORMA 2: editando directamente standalone.xml ( COMO LO HAREMOS EN CLASE POR PROBLEMAS DE PERMISOS)

Editamos dicho fichero desde Visual Studio Code (Server Actions/Edit Configuration File):

![image](https://github.com/user-attachments/assets/d5cbd71e-f335-419c-bda3-09cb18032ce7)

___

Añadimos un DataSource:

```
<datasource jndi-name="java:/H2DS" pool-name="H2DS">
       <connection-url>jdbc:h2:~/tienda;AUTO_SERVER=TRUE</connection-url>
       <driver-class>org.h2.Driver</driver-class>
       <driver>h2</driver>
       <security user-name="sa" password="sa"/>
       <validation>
             <validate-on-match>true</validate-on-match>
       </validation>
</datasource>    

```

___

**El jndi-name y connection-url deben coincidir en el datasource y en persistence.xml**





## Sigue las indicaciones de tu profesor para crear las entidades, controladores y vistas....


# EJERCICIO 2: Migrar el ejercicio de la tienda JDBC a JPA con el framework Hibernate

Partimos de la solución que está en https://github.com/profeMelola/DWES-04-2024-25/tree/main/EJERCICIOS/1_TiendaDAO/tienda_dao
