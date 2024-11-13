# Ejercicio para practicar con la relación one to one

Incluimos en nuestro aprendizaje:
- La relación one2one.
- La clase embebida Auditoria.
- La clase enumerada FormaPago.

Además con este ejercicio, repasarás asociaciones ya vistas previamente.

El diagrama de entidades(tablas) y relaciones de la base de datos es este:

![image](https://github.com/user-attachments/assets/782663be-aafb-4caa-9a03-3d4aa99fb46e)



## Ten en cuenta:

- *Cliente* tiene una relación de **uno a muchos** con *Direccion*, ya que un cliente puede tener múltiples direcciones.
    - Bidireccional ya que ambas entidades tienen campos que hacen referencia entre sí, permitiendo la navegación en ambos sentidos.
    - *Direccion* tiene un relación de **muchos a uno** con *Cliente*, indicando que muchas direcciones pertenecen al mismo cliente. 
- *Cliente* tiene una relación de **uno a muchos** con *Factura*, indicando que un cliente puede emitir múltiples facturas.
    - Bidireccional ya que ambas entidades tienen campos que hacen referencia entre sí, permitiendo la navegación en ambos sentidos.
    - *Factura* tiene un relación de **muchos a uno** con *Cliente*, indicando que muchas factura pertenecen al mismo cliente. 
- *Cliente* tiene una relación de **uno a uno** con *ClienteDetalle*, indicando que un cliente puede tener un solo detalle (como atributos de cliente prime y puntosAcumulados).
- *ClienteDetalle* tiene una relación inversa de **uno a uno** con *Cliente*.


## Entidades y otras clases

Tendremos cuatro entidades:

- Cliente.
- Dirección.
- Factura.
- ClienteDetalle.

Una clase embebida:
- Dirección.

Una clase enumerada:
- FormaPago.

Fijándote en el gráfico obtendrás el nombre de los atributos.

## Relación @OneToOne

En Cliente deberás usar:

```
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "cliente")
    private ClienteDetalle detalle;
```

En ClienteDetalle deberás usar:

```
    @OneToOne
    @JoinColumn(name="cliente_detalle_id")
    private Cliente cliente;

```

## Datasource a añadir en Wildfly

```
                <datasource jndi-name="java:/ClienteDS" pool-name="ClienteDS">
                    <connection-url>jdbc:h2:file:~/clientes;AUTO_SERVER=TRUE</connection-url>
                    <driver-class>org.h2.Driver</driver-class>
                    <driver>h2</driver>
                    <security user-name="sa" password="sa"/>
                </datasource>

```

## Base de datos: clientes

Se adjunta a la tarea la base de datos vacía sin tablas.

En este ejercicio crearemos la estructura de tablas desde las Entidades JPA que diseñemos.

## Controlador: JPAServlet

Crea un Servlet que simplemente al recibir una petición GET monte todas las tablas en la base de datos (**http://localhost:8080/jpa-one2one/initDatabase**).

En caso de excpeción, que devuelva un **SC_INTERNAL_SERVER_ERROR**.

En caso contrario, que simplemente devuelva el mensaje ""La estructura de tablas se ha creado correctamente en la base de datos."

![image](https://github.com/user-attachments/assets/cb1b3821-8e4c-4d6f-b74d-08cad9655342)

  
## Proyecto inicial: jpa_one2one

Se adjunta el proyecto web inicial que completar.
