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

//import es.daw.web.entities.User;

@Named("loginBean")
@SessionScoped
public class LoginBean implements Serializable {
    // ATRIBUTOS (CAMPOS FORMULARIO)
    private String username;
    private String password;
    
    // VARIABLES
    private boolean isAdmin;

    @Inject
    //CrudRepository<User> repoUser;
    CrudRepositoryUser repoUser;


    // CONSTRUCTOR VACÍO

    // GETTERS & SETTERS
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


    // MÉTODOS DE COMPORTAMIENTO ..............

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
                // FORMA 1
                // user.setPassword(password);
                // optUser = repoUser.selectByPropiedad(user);
                // if (optUser.isEmpty()) {
                //     System.out.println("****** LA PWD NO ES CORRECTA ******");
                //     throw new JPAException("La contraseña <"+password+"> no es correcta.");
                // }

                if (!optUser.get().getPassword().equals(password)){
                    System.out.println("****** LA PWD NO ES CORRECTA ******");
                    throw new JPAException("La contraseña <"+password+"> no es correcta.");
                }

                // 3. Verificar si el usuario tiene el rol adecuado (admin o cliente) en la tabla
                //isAdmin = repoUser.isAdmin(user);
                isAdmin = optUser.get().getRoles().stream()
                                        .filter(r -> r.getRoleName().equalsIgnoreCase("ADMIN"))
                                        .findAny()
                                        .isPresent();

                //isAdmin = repoUser.isAdmin(user);
                                        
                

            }catch(JPAException e){
                System.out.println("****** JPAEXCEPTION ******");
                System.out.println(e.getMessage());
                System.out.println("****************************");

                // Capturamos la excepción y mostramos un mensaje de error en la interfaz
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de inicio de sesión", " - "+e.getMessage())); 

                return null; //mantenerse en la página
                
            }


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
