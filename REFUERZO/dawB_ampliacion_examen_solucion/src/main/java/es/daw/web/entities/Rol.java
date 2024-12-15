package es.daw.web.entities;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "role_name", length = 50, unique = true, nullable = false)
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY) // Relación inversa, carga perezosa
    private Set<User> users = new HashSet<>();

    // Constructor vacío para JPA
    public Rol() {}

    // Constructor con parámetros
    public Rol(String roleName) {
        this.roleName = roleName;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    // Método para agregar un usuario a un rol
    public void addUser(User user) {
        this.users.add(user);
        user.getRoles().add(this);
    }

    // Método para eliminar un usuario de un rol
    public void removeUser(User user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }
}
