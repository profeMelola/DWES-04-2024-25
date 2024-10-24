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

Dentro de META-INF, deberás crear o ajustar este archivo con la configuración adecuada del proveedor de persistencia, como el nombre de tu unidad de persistencia (persistence-unit), la conexión a la base de datos, etc.

```
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
    <persistence-unit name="my-persistence-unit" transaction-type="JTA">
        <jta-data-source>java:jboss/datasources/MyDataSource</jta-data-source>
        <!--<class>com.miapp.model.MiEntidad</class>--> <!-- Tus entidades aquí. OBSOLETO!!!! -->
        <properties>
            <!-- Configuración del proveedor de JPA -->
            <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:~/tienda;AUTO_SERVER=TRUE"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>

            <!-- Otras configuraciones como el dialecto -->
            <property name="jakarta.persistence.schema-generation.database.action" value="create"/>
        </properties>
    </persistence-unit>
</persistence>

```
