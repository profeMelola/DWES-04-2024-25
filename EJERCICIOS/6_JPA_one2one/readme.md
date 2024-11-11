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

## Datasource a añadir en Wildfly



  
