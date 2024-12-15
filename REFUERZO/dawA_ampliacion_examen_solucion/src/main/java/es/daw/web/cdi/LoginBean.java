package es.daw.web.cdi;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Optional;

import es.daw.web.entities.User;
import es.daw.web.exceptions.JPAException;
import es.daw.web.repositories.CrudRepositoryUser;
import es.daw.web.repositories.JpaManagerCdi;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;
    
    private boolean isAdmin;

    @Inject
    CrudRepositoryUser repoUser;

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
     * 2. Verificar que la contraseña coincida. 
     * 3. Verificar si el usuario tiene el rol adecuado (admin o cliente) en la tabla
     * ROLES.
     * 
     * @return
     */
    public String login() {
        System.out.println("*********** LOGIN ************");
        if (validateInputs()) {

            try{
                // 1. Verificar si el usuario existe
                User user = new User();
                user.setUsername(username);
                Optional<User> optUser = repoUser.selectByPropiedad(user);

                if (optUser.isEmpty()) {
                    System.out.println("****** NO EXISTE EL USER EN EL SISTEMA ******");
                    throw new JPAException("El usuario <"+username+"> no existe en el sistema.");
                }

                // 2. Verificar que la contraseña coincida.
                User usuario = optUser.get(); //no está vacío porque se ha comprobado antes
                if (!usuario.getPassword().equals(password)){
                    System.out.println("****** LA CONTRASEÑA NO ES CORRECTA ******");
                    throw new JPAException("La contraseña <"+password+"> no es correcta.");
                }

                // 3. Verificar si el usuario tiene el rol adecuado (admin o cliente) en la tabla
                isAdmin = repoUser.isAdmin(username);
                System.out.println("****** ES ADMIN ("+isAdmin+") ******");

            }catch(JPAException e){
                System.out.println("****** JPAEXCEPTION ******");
                System.out.println(JpaManagerCdi.getMessageError(e));
                
                // Capturamos la excepción y mostramos un mensaje de error en la interfaz
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de inicio de sesión", " - "+e.getMessage())); 

                return null;

            }


            // Redirigimos a la página principal
            // JSF realiza una nueva solicitud HTTP hacia main.xhtml.
            // Cambia la URL en la barra del navegador a main.xhtml y hace que la página se
            // recargue completamente.
            return "main?faces-redirect=true";
        }
        return null; // Permanece en la misma página si falla la validación
    }

    /**
     * 
     * @return
     */
    public String logout() {
        System.out.println("********************* LOGOUT **********************");
        // Limpia la sesión del usuario, eliminando cualquier atributo almacenado en la sesión.
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession(); // Invalidar la sesión
        return "login?faces-redirect=true"; // Redirigir a login.xhtml
    }    

    /**
     * 
     * @return
     */
    private boolean validateInputs() {

        // SI HAY VALIDACIONES QUE HACER CON CAMPOS....

        return true;
    }

    /* ******************************************** */
    public boolean canUpdate() {
        return isAdmin;
    }

    public boolean canSelect() {
        return true; // Todos los usuarios pueden seleccionar
    }
}
