# Ejercicio para practicar con la relación one to one

Incluimos en nuestro aprendizaje la relación one2one.

Además con este ejercicio, repasarás asociaciones ya vistas previamente.

El diagrama de entidades(tablas) y relaciones de la base de datos es este:

![image](https://github.com/user-attachments/assets/676105b1-1c71-4024-a6a3-6a2f59223de2)


## Ten en cuenta:

- *Cliente* tiene una relación de **uno a muchos** con *Direccion*, ya que un cliente puede tener múltiples direcciones.
- *Cliente* tiene una relación de **uno a muchos** con *Factura*, indicando que un cliente puede emitir múltiples facturas.
- *Factura* tiene un relación de **muchos a uno** con *Cliente*, indicando que una factura pertenecen al mismo cliente.
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

## Controlador: JPAServlet

Crea un Servlet que simplemente al recibir una petición GET monte todas las tablas en la base de datos.

En caso de excpeción, que devuelva un SC_INTERNAL_SERVER_ERROR.

En caso contrario, que simplemente devuelva el mensaje ""La estructura de tablas se ha creado correctamente en la base de datos."


  
