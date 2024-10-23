# Práctica guiada para aprender a usar CDI

Partimos del proyecto sin persistencia en base de datos del carro https://github.com/profeMelola/DWES-03-2024-25/tree/main/EJERCICIOS/Filter/solucion_listener_filtro

No vamos a modificar la funcionalidad, pero vamos a comprobar que ahorramos código y queda mucho más limpio, modular y reutilizable con CDI.

## Vamos a modificar:

1. Clase Carro (convertilo en un Bean @SessionScoped)

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

2. Listener (sessionCreated para no crear ni guardar en sesión el carro )
4. AgregarCarroServlet (para @Inject el Carro). Modificaremos también el request, llamando directamente a carro.jsp
5. En nuestra aplicación, no tenemos el controlador o Servlet para actualizar el carro (forma parte de la práctica), pero si tuviéramos ActualizarCarroServlet, también usaríamos @Inject
6. Modificamos la forma en leer de la Sesión el carro en carro.jsp 
7. Es necesario crear un archivo de configuración llamado beans.xml con la etiqueta beans vacía en WEB-INF del proyecto. Es requisito.
   
![image](https://github.com/user-attachments/assets/483fcdf2-e322-40ac-b6e3-d9786df269a3)

## CDI y JavaServer Faces (JSF)

Para manejar la vista, JSF es un framework orientado a componentes que se integra muy bien con CDI. 

JSF es parte de Jakarta EE y ofrece una forma limpia de gestionar la lógica de la interfaz de usuario (UI) usando CDI para manejar los beans que interactúan con la vista.

JSF usa facelets (.xhtml) en lugar de JSP, lo que te permite hacer inyecciones de dependencias directamente en la vista. Ejemplo:

```
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core">
<head>
    <title>Carro de Compras</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" />
</head>
<body>

<h1>Carro de Compras</h1>

<!-- Mostrar alerta si el carro está vacío -->
<h:panelGroup rendered="#{carro.empty}">
    <div class="alert alert-warning">
        Lo sentimos, no hay productos en el carro de compras!
    </div>
</h:panelGroup>

<!-- Mostrar productos del carro si no está vacío -->
<h:panelGroup rendered="#{not carro.empty}">
    <h:dataTable value="#{carro.items}" var="item" border="1">
        <h:column>
            <f:facet name="header">Producto</f:facet>
            <h:outputText value="#{item}" />
        </h:column>
    </h:dataTable>

    <!-- Botón para simular agregar un producto -->
    <h:form>
        <h:commandButton value="Agregar Producto"
                         action="#{carro.agregarItem('Producto ' + (carro.items.size() + 1))}" />
    </h:form>
</h:panelGroup>

</body>
</html>

```

Una alternativa más moderna y con una separación más clara de la lógica y la presentación, es **Thymeleaf** es otra opción para la vista. Aunque es más común en aplicaciones Spring, puede usarse con Jakarta EE.
