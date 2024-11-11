# JPQL - Jakarta Persistence Query Language

![alt text](image.png)

```
@Entity
public class Empleado {
    @Id
    private Long id;
    private String nombre;
    private Double salario;

    @ManyToOne
    private Departamento departamento;
}

@Entity
public class Departamento {
    @Id
    private Long id;
    private String nombre;

    @OneToMany(mappedBy = "departamento")
    private List<Empleado> empleados;
}

```

![alt text](image-1.png)

![alt text](image-2.png)