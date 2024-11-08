# FASE I: Añadir un sistema de login y autorización (sin algoritmo de hashing seguro)

- JPA + JSF + CDI
- No usuaremos un controlador Servlet para comprobar el login


## Nuevas tablas en la base de datos del ejercicio de libros (LibrosDS)

Para gestionar los usuarios y roles, necesitaremos dos tablas adicionales en tu base de datos libros, una para almacenar los usuarios y otra para los roles asociados a cada usuario.

```
-- Crear tabla de usuarios
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,  -- ID de usuario autoincremental
    username VARCHAR(50) UNIQUE NOT NULL,  -- Nombre de usuario único
    password VARCHAR(255) NOT NULL  -- Contraseña no nula
);

-- Crear tabla de roles
CREATE TABLE roles (
    role_id INT PRIMARY KEY AUTO_INCREMENT,  -- ID de rol autoincremental
    role_name VARCHAR(50) NOT NULL UNIQUE  -- Nombre de rol único
);

-- Crear tabla de relación entre usuarios y roles
CREATE TABLE user_roles (
    user_id INT,  -- ID de usuario
    role_id INT,  -- ID de rol
    PRIMARY KEY (user_id, role_id),  -- Clave compuesta
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,  -- Relación con usuarios
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE  -- Relación con roles
);

-- Insertar usuarios
INSERT INTO users (username, password) VALUES ('admin', 'adminpass');
INSERT INTO users (username, password) VALUES ('cliente', 'clientepass');
INSERT INTO users (username, password) VALUES ('visitante', 'visitantepass');

-- Insertar roles
INSERT INTO roles (role_name) VALUES ('ADMIN');
INSERT INTO roles (role_name) VALUES ('USER');
INSERT INTO roles (role_name) VALUES ('CLIENTE');

-- Asignación de roles a admin_user (ID = 1)
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);  -- Rol ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (1, 2);  -- Rol USER

-- Asignación de roles a cliente_user (ID = 2)
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2);  -- Rol USER
INSERT INTO user_roles (user_id, role_id) VALUES (2, 3);  -- Rol CLIENTE

-- Asignación de roles a visitante_user (ID = 3)
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2);  -- Rol USER

```

Las passwords están en texto plano por simplicidad, pero en producción deberías usar hashing seguro como **BCrypt.**

## Persistence.xml: opciones de Configuración para jakarta.persistence.schema-generation.database.action

- **none:** Con esta configuración (value="none"), JPA no creará ni modificará las tablas en la base de datos. Úsalo cuando ya hayas creado manualmente las tablas en la base de datos o cuando quieras que tu aplicación solo interactúe con las tablas sin modificarlas.
- **create:** JPA creará todas las tablas y estructuras necesarias cada vez que la aplicación se inicie. Este valor eliminará las tablas existentes y las volverá a crear, lo que puede ser útil en el desarrollo inicial pero es destructivo para los datos existentes.
- **drop-and-create:** Similar a create, pero primero elimina todas las tablas y luego las vuelve a crear. Ideal para pruebas, ya que garantiza que siempre se inicie con una base de datos limpia, aunque también borra todos los datos cada vez.
- **update:** JPA intentará ajustar las tablas para que coincidan con los Entities sin eliminar datos. Este valor es útil durante el desarrollo, ya que permite actualizar la estructura sin eliminar datos existentes. Sin embargo, es menos confiable para cambios mayores y no siempre realiza todos los ajustes necesarios. 
- **validate:** Solo verifica que las tablas y columnas en la base de datos coincidan con los Entities. No realiza cambios en la base de datos. Útil para producción o entornos donde solo se desea confirmar que la estructura es correcta. 

En nuestro entorno vamos a usar la opción de **validate**, aunque en desarrollo, normalmente se usa drop-and-create y update.

## Proyecto JPA_LOGIN

Partimos del proyecto del ejercicio https://github.com/profeMelola/DWES-04-2024-25/tree/main/EJERCICIOS/4_JPA_Asociaciones#ejercicio-1-asociaciones-onetomany-y-manytoone

<img src="https://github.com/user-attachments/assets/c66d8b70-769c-4d98-ba69-7c28d8843a39" height="200px"/>


Añadimos el facelet **login.xhtml**:

```
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="http://xmlns.jcp.org/jsf/html"
      xmlns:f="http://xmlns.jcp.org/jsf/core">
<h:head>
    <title>Login</title>
    <style>
        /* Estilos generales de página */
        body {
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-color: #f0f2f5;
        }

        /* Contenedor del formulario */
        .login-container {
            width: 100%;
            max-width: 400px;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
        }

        /* Título del formulario */
        .login-container h2 {
            text-align: center;
            margin-bottom: 20px;
            color: #333;
        }

        /* Estilos de etiqueta y campo */
        .login-container label {
            font-weight: bold;
            display: block;
            margin: 10px 0 5px;
            color: #555;
        }

        .login-container input[type="text"],
        .login-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-sizing: border-box;
            transition: border-color 0.3s;
        }

        /* Efecto hover y focus */
        .login-container input[type="text"]:focus,
        .login-container input[type="password"]:focus {
            border-color: #5c9aed;
            outline: none;
        }

        /* Botón de login */
        .login-container input[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #5c9aed;
            border: none;
            color: white;
            font-size: 16px;
            font-weight: bold;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
            margin-top: 15px;
        }

        .login-container input[type="submit"]:hover {
            background-color: #4a86d0;
        }

        /* Estilos de mensaje de error */
        .error-message {
            color: red;
            font-size: 12px;
            margin-top: 5px;
        }
    </style>
</h:head>
<h:body>
    <div class="login-container">
        <h2>Iniciar Sesión</h2>
        <h:form>
            <h:outputLabel for="username" value="Username:" />
            <h:inputText id="username" value="#{loginBean.username}" required="true" requiredMessage="Username is required" />
            <h:message for="username" styleClass="error-message" />

            <h:outputLabel for="password" value="Password:" />
            <h:inputSecret id="password" value="#{loginBean.password}" required="true" requiredMessage="Password is required" />
            <h:message for="password" styleClass="error-message" />

            <h:commandButton value="Login" action="#{loginBean.login}" />
        </h:form>
    </div>
</h:body>
</html>

```

Dependiendo del usuario, obtendremos diferentes opciones en la lista desplegable:

![image](https://github.com/user-attachments/assets/2b4994f3-15eb-4f6a-8895-d722f2442e20)

___

![image](https://github.com/user-attachments/assets/400b0435-c4a5-4122-8215-9fd009ea3726)



## Creamos el Bean CDI correspondiente

Partimos de esta estructura que tendremos que completar:

```
package es.daw.web.cdi;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import java.io.IOException;
import java.io.Serializable;

//import es.daw.web.entities.User;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;
    private boolean isAdmin;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Establece si el usuario es administrador y redirige al usuario a la página
     * main.xhtml si la validación es correcta
     * 1. Verificar si el usuario existe en la base de datos (en la tabla USERS).
     * 2. Verificar que la contraseña coincida (aunque las contraseñas nunca deberían
     * guardarse en texto plano en producción, aquí se asume que la contraseña está
     * en texto plano).
     * 3. Verificar si el usuario tiene el rol adecuado (admin o cliente) en la tabla
     * ROLES.
     * 
     * @return
     */
    public String login() {
        System.out.println("*********** LOGIN ************");
        if (validateInputs()) {

            // CAMBIAR ESTO POR EL ACCESO A DATOS...
            if (username.equalsIgnoreCase("admin"))
                isAdmin = true;            


            // Redirigimos a la página principal
            // JSF realiza una nueva solicitud HTTP hacia main.xhtml.
            // Cambia la URL en la barra del navegador a main.xhtml y hace que la página se
            // recargue completamente.
            return "main?faces-redirect=true";
        }
        return null; // Permanece en la misma página si falla la validación
    }

    public void logout() throws IOException{
        System.out.println("**************** Cerrando sessión.....");
        // Limpia la sesión del usuario, eliminando cualquier atributo almacenado en la sesión.
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Invalidar la sesión
        FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml"); // Redirigir a login.xhtml
    }    

    private boolean validateInputs() {

        // SI HAY VALIDACIONES QUE HACER CON CAMPOS....

        return true;
    }

    public boolean canSave() {
        return isAdmin;
    }

    public boolean canSelect() {
        return true; // Todos los usuarios pueden seleccionar
    }
}
```

## Creamos los Entity Bean necesarios

Practicaremos las **relaciones @ManyToMany**

Necesitamos:

     1. Verificar si el usuario existe en la base de datos (en la tabla USERS).
     2. Verificar que la contraseña coincida (aunque las contraseñas nunca deberían guardarse en texto plano en producción, aquí se asume que la contraseña está en texto plano).
     3. Verificar si el usuario tiene el rol adecuado (admin o cliente) en la tabla

## Completar la lógica de negocio

Tenemos que implementar el código necesario en **LoginBean** que actúa como controlador.


# FASE II: JAAS (Java Authentication an Authorization Service)

## Configurar el security-domain en WildFly

Debes configurar un security-domain en el archivo de configuración de WildFly (standalone.xml) para manejar la autenticación. 

Usa el módulo Database para conectar con H2 y autenticar a los usuarios.

```
<security-domain name="AppSecurityDomain" cache-type="default">
    <authentication>
        <login-module code="Database" flag="required">
            <module-option name="dsJndiName" value=" jdbc:h2:~/tienda"/>
            <module-option name="principalsQuery" value="SELECT password FROM USERS WHERE username = ?"/>
            <module-option name="rolesQuery" value="SELECT role_name, 'Roles' FROM ROLES WHERE username = ?"/>
            <module-option name="hashAlgorithm" value="SHA-256"/>
        </login-module>
    </authentication>
</security-domain>

```

## Configurar en web.xml las restricciones de seguridad

```
<!-- Configuración de seguridad -->
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>AdminPages</web-resource-name>
            <url-pattern>/admin/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>ADMIN</role-name>
        </auth-constraint>
    </security-constraint>

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>ClientPages</web-resource-name>
            <url-pattern>/client/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>CLIENTE</role-name>
            <role-name>ADMIN</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>FORM</auth-method>
        <realm-name>AppSecurityDomain</realm-name>
    </login-config>

    <security-role>
        <role-name>ADMIN</role-name>
    </security-role>
    <security-role>
        <role-name>CLIENTE</role-name>
    </security-role>
```

En este ejemplo:

- Los usuarios con rol ADMIN pueden acceder a URLs que empiezan con /admin/*.
- Los usuarios CLIENTE y ADMIN pueden acceder a URLs que empiezan con /client/*.

## Añadir la lógica de seguridad en las páginas y componentes

Asegúrate de que tus páginas JSP o servlets solo muestren contenido según el rol del usuario. 

Usa las anotaciones de seguridad en tus servlets o beans para definir permisos específicos.

### Por ejemplo en un servlet:

```
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/manage")
@RolesAllowed("ADMIN")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        // Lógica de gestión de productos/fabricantes solo accesible por ADMIN
    }
}

```


### Por ejemplo en las páginas JSF

```
<h:panelGroup rendered="#{request.isUserInRole('ADMIN')}">
    <!-- Contenido exclusivo para ADMIN -->
    <h:commandButton value="Agregar Producto" action="#{productoBean.agregar}" />
</h:panelGroup>

<h:panelGroup rendered="#{request.isUserInRole('CLIENTE')}">
    <!-- Contenido exclusivo para CLIENTE -->
    <h:outputText value="Bienvenido, Cliente. Aquí están los productos:" />
</h:panelGroup>

```

# FASE III: con BCrypt...

```
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>

```
