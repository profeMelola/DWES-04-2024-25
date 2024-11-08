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

    // public void logout() throws IOException{
    //     System.out.println("**************** Cerrando sessión.....");
    //     // Limpia la sesión del usuario, eliminando cualquier atributo almacenado en la sesión.
    //     FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Invalidar la sesión
    //     FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml"); // Redirigir a login.xhtml
    // }    

    public String logout() {
        System.out.println("**************** Cerrando sesión.....");
        // Limpia la sesión del usuario, eliminando cualquier atributo almacenado en la sesión.
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        // Retornamos la página a la que queremos navegar tras el logout
        return "login?faces-redirect=true";  // Redirige a "login.xhtml" mediante navegación de JSF
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
