# Práctica guiada para aprender a usar CDI y JSF

Partimos del proyecto sin persistencia en base de datos del carro https://github.com/profeMelola/DWES-03-2024-25/tree/main/EJERCICIOS/Filter/solucion_listener_filtro

No vamos a modificar la funcionalidad, pero vamos a comprobar que ahorramos código y queda mucho más limpio, modular y reutilizable con CDI.

Para manejar la vista, JSF es un framework orientado a componentes que se integra muy bien con CDI. 

JSF es parte de Jakarta EE y ofrece una forma limpia de gestionar la lógica de la interfaz de usuario (UI) usando CDI para manejar los beans que interactúan con la vista.

JSF usa facelets (.xhtml) en lugar de JSP, lo que te permite hacer inyecciones de dependencias directamente en la vista.

Una alternativa más moderna y con una separación más clara de la lógica y la presentación es **Thymeleaf**. Aunque es más común en aplicaciones Spring, puede usarse con Jakarta EE.

## CONFIGURACIÓN DEL ENTORNO

### Extensión en Visual Studio Code

![image](https://github.com/user-attachments/assets/40ebfed7-e713-47ff-ae6c-5ba45260f037)

### Dependencias en pom.xml

Si ya estás usando el **artefacto jakarta.jakartaee-web-api**, que abarca todas las especificaciones web de Jakarta EE (incluyendo JSF), **NO necesitas agregar una dependencia adicional** específicamente para JSF ni CDI, ya que está incluida.

Además, usamos Wildfly que tiene implementado las especificaciones pertinentes.

Si tuviéramos otro entorno, con otro servidor de aplicaciones, deberíamos revisar las dependencias necesarias a añadir. Similar a esto:

```
<dependency>
    <groupId>jakarta.faces</groupId>
    <artifactId>jakarta.faces-api</artifactId>
    <version>2.3.10</version> <!-- o la versión que necesites -->
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.glassfish</groupId>
    <artifactId>jakarta.faces</artifactId>
    <version>2.3.10</version> <!-- o la versión que necesites -->
</dependency>
<dependency>
    <groupId>jakarta.enterprise</groupId>
    <artifactId>jakarta.enterprise.cdi-api</artifactId>
    <version>3.0.0</version> <!-- o la versión que necesites -->
    <scope>provided</scope>
</dependency>
```

## Archivos de configuración XML en webapp/WEB-INF

### Añadir en web.xml

```
<servlet>
    <servlet-name>Faces Servlet</servlet-name>
    <servlet-class>jakarta.faces.webapp.FacesServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>Faces Servlet</servlet-name>
    <url-pattern>*.xhtml</url-pattern> <!-- o cualquier patrón que uses -->
</servlet-mapping>

```

### Nuevo archivo faces-config.xml

Con esta configuración de espacios de nombres:

```
<faces-config
    xmlns="https://jakarta.ee/xml/ns/jakartaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
    https://jakarta.ee/xml/ns/jakartaee/web-facesconfig_4_0.xsd"
    version="4.0">
</faces-config>

```

### Nuevo archivo beans.xml

Por ahora solo con esto:

```
<beans>
</beans>
```

## PASOS:

### 1. Clase Carro (convertilo en un Bean @SessionScoped)

```
@SessionScoped

@Named
public class Carro implements Serializable{
    private List<ItemCarro> items;
    

    public Carro() {
        items = new ArrayList<>();
    }
....
}
```
**@SessionScoped**

Esta anotación indica que la clase Carro tiene un ciclo de vida relacionado con la sesión del usuario. 

Esto significa que los datos de la instancia de Carro se mantendrán mientras dure la sesión de un usuario específico, y serán destruidos cuando la sesión termine. 

Es útil en aplicaciones web donde se quiere mantener el estado del carrito de compras del usuario durante la navegación.

**@Named**

Esta anotación indica que la clase Carro puede ser utilizada en una vista, por ejemplo, en páginas JSF (JavaServer Faces) o tecnologías similares. 

El objeto puede ser referenciado directamente desde el código de la interfaz (por ejemplo, en un archivo XHTML) usando su nombre. 

Si no se especifica un nombre explícito, el nombre por defecto será el nombre de la clase con la primera letra en minúscula (carro en este caso).

### 2. Listener

En el evento sessionCreated no tendremos que guardar en la sesión el objeto Carro.

### 3. AgregarCarroServlet: inyección del objeto Carro

Añadimos de forma global la anotación @Inject en el objeto Carro.

En nuestra aplicación, no tenemos el controlador o Servlet para actualizar el carro (forma parte de la práctica), pero si tuviéramos ActualizarCarroServlet, también usaríamos @Inject

### 4. Crear nuestra vista carro con JSF

Crea un archivo llamado carroJSF.xhtml con este contenido:

```
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Carro de compras</title>
</head>
<body>
    <h1>Carro de compras</h1>

    <!-- Mostrar mensaje si el carro está vacío -->
    <h:panelGroup rendered="#{carro == null or empty carro.items}">
        <p>Lo sentimos, no hay productos en el carro de compras!</p>
    </h:panelGroup>

    <!-- Mostrar la tabla si el carro tiene productos -->
    <h:panelGroup rendered="#{carro != null and not empty carro.items}">
        <h:dataTable value="#{carro.items}" var="item" border="1">
            <h:column>
                <f:facet name="header">ID</f:facet>
                <h:outputText value="#{item.producto.id}" />
            </h:column>
            <h:column>
                <f:facet name="header">Nombre</f:facet>
                <h:outputText value="#{item.producto.nombre}" />
            </h:column>
            <h:column>
                <f:facet name="header">Precio</f:facet>
                <h:outputText value="#{item.producto.precio}" />
            </h:column>
            <h:column>
                <f:facet name="header">Cantidad</f:facet>
                <h:outputText value="#{item.cantidad}" />
            </h:column>
            <h:column>
                <f:facet name="header">Total</f:facet>
                <h:outputText value="#{item.importe}" />
            </h:column>
        </h:dataTable>

        <!-- Fila con el total general -->
        <h:panelGrid columns="5">
            <h:outputText value="" />
            <h:outputText value="" />
            <h:outputText value="" />
            <h:outputText value="Total:" style="text-align: right" />
            <h:outputText value="#{carro.total}" />
        </h:panelGrid>
    </h:panelGroup>
</body>
</html>

```

![image](https://github.com/user-attachments/assets/7783ab9d-959a-486f-9e2b-09586592e152)


**#{}:** Se utiliza para evaluar expresiones que pueden incluir métodos y propiedades de beans (Managed Beans) y permite la invocación de métodos. Esto se traduce en que puede ejecutar lógica del lado del servidor. Este tipo de expresión se usa cuando necesitas acceder a valores dinámicos o cuando necesitas realizar acciones que requieren evaluación en tiempo de ejecución.

**${}:** Se utiliza para acceder a valores estáticos o expresiones que no requieren invocación de métodos. Es común en contextos donde solo necesitas obtener un valor sin necesidad de lógica adicional o invocaciones.

