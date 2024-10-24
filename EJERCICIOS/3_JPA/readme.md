# Migrar el ejercicio de la tienda JDBC a JPA con el framework Hibernate

## Dependencias
Como tenemos esta dependencia, con el profile web de Jakarta, no es necesario añadir ninguna otra porque ya incluye JPA:

```
		<dependency>
			<groupId>jakarta.platform</groupId>
			<artifactId>jakarta.jakartaee-web-api</artifactId>
			<version>${jakartaee-api.version}</version>
			<scope>provided</scope>
		</dependency>
```

Si bien JakartaEE incluye la API, necesitas una implementación específica. EclipseLink (proveedor por defecto de JakartaEE) y Hibernate son opciones comunes.

```
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.6.1.Final</version> <!-- Asegúrate de usar una versión compatible con JakartaEE 10 -->
</dependency>

```

## Configuración de persistence.xml

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


No es necesario registrar manualmente todas las entidades en el archivo persistence.xml, ya que JPA permite descubrir automáticamente las entidades mediante las anotaciones en las clases de entidad. Esto simplifica bastante la configuración, ya que no tienes que declararlas explícitamente en el persistence.xml.


**Ejemplo de persistence.xml con descubrimiento automático:**
```
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
    <persistence-unit name="my-persistence-unit" transaction-type="JTA">
        <jta-data-source>java:jboss/datasources/MyDataSource</jta-data-source>

        <!-- No necesitas especificar las clases manualmente si usas anotaciones -->
        
        <properties>
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:~/tienda;AUTO_SERVER=TRUE"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>

            <!-- Otras configuraciones de JPA -->
            <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
        </properties>
    </persistence-unit>
</persistence>

```

Cuando defines una clase de entidad usando la anotación @Entity, JPA puede escanear automáticamente los paquetes y registrar las clases marcadas con dicha anotación, lo que evita tener que especificarlas manualmente en el persistence.xml.

Por lo tanto, en tu persistence.xml, no es necesario listar cada entidad individualmente con la etiqueta <class>. Solo asegúrate de que tus clases de entidad estén anotadas correctamente.


```
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MiEntidad {
    
    @Id
    private Long id;
    
    private String nombre;

    // Getters y setters
}

```

## Configuración de standalone.xml de Wildfly

Editamos dicho fichero desde Visual Studio Code (Server Actions/Edit Configuration File):

![image](https://github.com/user-attachments/assets/d5cbd71e-f335-419c-bda3-09cb18032ce7)

___

Añadimos un DataSource:

```
                <datasource jndi-name="java:jboss/datasources/MyDataSource" pool-name="MyDataSource" enabled="true" use-java-context="true" statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
                    <connection-url>jdbc:h2:~/tienda;AUTO_SERVER=TRUE</connection-url>
                    <driver>h2</driver>
                    <security user-name="sa" password="sa"/>   
                </datasource>             

```
