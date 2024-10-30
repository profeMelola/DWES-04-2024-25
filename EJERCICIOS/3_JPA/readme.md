
# EJERCICIO 1: JPA de listado de Fabricantes

![image](https://github.com/user-attachments/assets/3475296e-f4fb-4254-bbcb-7a0d21d3bdb8)


Partimos de la solución que está en https://github.com/profeMelola/DWES-04-2024-25/tree/main/EJERCICIOS/1_TiendaDAO/tienda_dao

Limpiamos el proyecto para trabajar solo con Fabricantes. Nuestro proyecto se llamará **PRIMER_JPA**:

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

Pero...**en nuestro entorno SÍ es necesario para que no nos de error.**

Por tanto vamos a añadir esta dependencia:

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

# EJERCICIO 2: CRUD JPA de Fabricantes con JSP

Para poder insertar fabricantes vamos a modificar la tabla en H2 para que el código (primary key) sea autoincrement:

```
ALTER TABLE fabricante
ALTER COLUMN codigo INT AUTO_INCREMENT;
ALTER TABLE fabricante ALTER COLUMN codigo RESTART WITH 10;
```
___

Esta es la página de inicio:

![image](https://github.com/user-attachments/assets/a20db192-e40c-41c4-8c2f-171e8f25ab70)


# EJERCICIO 3: CRUD JPA de Fabricantes con JSF y Bootstrap

## Esta será la nueva página de inicio

![image](https://github.com/user-attachments/assets/61a46ea9-874f-46bf-82c2-6024ef2b38a6)

```
<!DOCTYPE html>
<html>
    <head>
        <title>CRUD JPA</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />        
    </head>
    <body class="bg-light">
        <div class="container mt-5">
            <h1 class="text-center mb-4">CRUD JPA - FABRICANTES</h1>
            <form action="fabricantes/jpa" method="GET">
                <fieldset class="border p-4 mb-4 rounded">
                    <legend class="w-auto font-weight-bold">Datos del fabricante</legend>
                    <div class="form-group">
                        <label for="nombre">Nombre:</label>
                        <input type="text" class="form-control" name="nombre" id="nombre"/>
                    </div>
                </fieldset>
                <fieldset class="border p-4 mb-4 rounded">
                    <legend class="w-auto font-weight-bold">Operaciones</legend>
                    <div class="form-group">
                        <div class="form-check">
                            <input type="radio" class="form-check-input" value="select" name="operacion" id="listar" required/>
                            <label class="form-check-label" for="listar">Listar fabricantes</label>
                        </div>
                        <div class="form-check">
                            <input type="radio" class="form-check-input" value="insert" name="operacion" id="insertar" required/>
                            <label class="form-check-label" for="insertar">Insertar nuevo fabricante</label>
                        </div>
                        <div class="form-check">
                            <input type="radio" class="form-check-input" value="update" name="operacion" id="actualizar" required/>
                            <label class="form-check-label" for="actualizar">Actualizar fabricante</label>
                        </div>
                        <div class="form-check">
                            <input type="radio" class="form-check-input" value="delete" name="operacion" id="borrar"/>
                            <label class="form-check-label" for="borrar">Borrar fabricante por código</label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="codigo">Código del fabricante:</label>
                        <input type="text" class="form-control" name="codigo" id="codigo"/>
                    </div>
                </fieldset>   
                <button class="btn btn-primary btn-block" name="enviar" type="submit">Ejecutar</button>
            </form>
        </div>
    
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.6/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
```

El archivo **bootstrap.min.css** está en el repositorio de GitHub. Descárgalo y copíalo en el directorio **webapp/css** de tu proyecto.

___


## Configura tu proyecto para que soporte JSF.

![image](https://github.com/user-attachments/assets/9da1b01d-c880-4cd6-a7a8-33550a410a88)


## Crea un facelet (página xhtml) para mostar el listado de fabricantes

Vamos a crear un snippet para facilitar el trabajo. Llama al snippet **facelet** y copia lo siguiente:

```
{
    "Plantilla básica XHTML": {
        "prefix": "xhtmltemplate",
        "body": [
            "<!DOCTYPE html>",
            "<html xmlns=\"http://www.w3.org/1999/xhtml\"",
            "      xmlns:h=\"http://xmlns.jcp.org/jsf/html\"",
            "      xmlns:f=\"http://xmlns.jcp.org/jsf/core\">",
            "<head>",
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />",
            "    <title>Título</title>",
            "    <link rel=\"stylesheet\" type=\"text/css\" href=\"#{request.contextPath}/css/bootstrap.min.css\" />",
            "</head>",
            "<body class=\"container mt-4\">",
            "    <h1 class=\"text-primary mb-4\">Título</h1>",
            "</body>",
            "</html>"
        ],
        "description": "Plantilla básica para un archivo XHTML con Jakarta EE"
    }
}

```
___


Este es el resultado de la lista de fabricantes:

![image](https://github.com/user-attachments/assets/30cb4ef8-ff2b-4449-b5dc-7b89f04ed05e)


# EJERCICIO 3: CRUD JPA de Fabricantes con JSF, Bootstrap y CDI

Vamos a pasar de un modelo Singleton (artesanal) para obtener el EntityManager a usar CDI.

___

El archivo **beans.xml** debe contener lo siguiente:

```
<?xml version="1.0" encoding="UTF-8"?>
<!-- 
Si no necesitas ninguna configuración especial, puedes omitir el archivo beans.xml o usar el archivo mínimo.
Jakarta EE 10 asume bean-discovery-mode="annotated" por defecto si no se incluye beans.xml, 
lo que es suficiente para la mayoría de los casos. -->
<beans xmlns="https://jakarta.ee/xml/ns/jakartaee"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/beans_4_0.xsd"
       bean-discovery-mode="all">

</beans>
<!-- 
bean-discovery-mode: Controla cómo se descubren los beans CDI en la aplicación.
"all": Descubre todos los beans disponibles.
"annotated": Solo descubre beans que estén anotados específicamente para CDI (@Dependent, @RequestScoped, etc.).
"none": Desactiva la detección de beans.

alternatives: Lista de clases que deben ser tratadas como alternativas, es decir, versiones alternativas de otros beans.
interceptors: Define clases interceptoras que manejan la lógica adicional para métodos específicos.
decorators: Especifica decoradores que agregan funcionalidad alrededor de otros beans.
-->

```




# EJERCICIO 4: Migrar el ejercicio de la tienda JDBC a JPA

Partimos de las especificaciones del ejercicio JDBC TiendaDAO que está en https://github.com/profeMelola/DWES-04-2024-25/tree/main/EJERCICIOS/1_TiendaDAO/tienda_dao.

En base a lo aprendido en los ejercicios anteriores, implementa la solución completa JPA para administrar productos.

El listado de fabricantes debe salir dinámicamente según lo existente en la base de datos.

## Nuevas etiquetas para la relación ManyToOne entre productos y fabricantes

- **@ManyToOne:** Indica que muchos productos pueden estar asociados a un solo fabricante.
- **@JoinColumn:** Especifica la columna de la tabla Producto que actúa como la clave foránea (codigo_fabricante).
	- **name:** El nombre de la columna en la tabla Producto.
	- **referencedColumnName:** El nombre de la columna en la tabla Fabricante que se referencia (en este caso, codigo).
	- **nullable:** Indica si el campo puede ser nulo (especifica false si todos los productos deben tener un fabricante).

### Convierte Producto en una entidad:

```
@Entity
@Table(name = "producto")
public class Producto{

    @Id
    private int codigo;
    private String nombre;
    private float precio;

    @ManyToOne
    @JoinColumn(name = "codigo_fabricante", referencedColumnName = "codigo", nullable = false)
    //private int codigo_fabricante;
    private Fabricante fabricante; // En lugar de int, aquí tienes el objeto Fabricante
```
