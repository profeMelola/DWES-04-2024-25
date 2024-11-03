# FASE I: Añadir un sistema de login y autorización (sin algoritmo de hashing seguro)

JAAS (Java Authentication an Authorization Service)

## 1. Nuevas tablas en H2

Para gestionar los usuarios y roles, necesitaremos dos tablas adicionales en tu base de datos H2, una para almacenar los usuarios y otra para los roles asociados a cada usuario.

```
CREATE TABLE USERS (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE ROLES (
    username VARCHAR(50),
    role_name VARCHAR(50),
    FOREIGN KEY (username) REFERENCES USERS(username)
);

-- Insertamos algunos usuarios de ejemplo
INSERT INTO USERS (username, password) VALUES ('admin', 'adminpass');
INSERT INTO USERS (username, password) VALUES ('cliente', 'clientepass');

-- Asignamos roles
INSERT INTO ROLES (username, role_name) VALUES ('admin', 'ADMIN');
INSERT INTO ROLES (username, role_name) VALUES ('cliente', 'CLIENTE');

```

Las passwords están en texto plano por simplicidad, pero en producción deberías usar hashing seguro como **BCrypt.**

## 2. Configurar el security-domain en WildFly

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

## 3. Configurar en web.xml las restricciones de seguridad

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

## 4. Añadir la lógica de seguridad en las páginas y componentes

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

# FASE II: con BCrypt...

```
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>

```
