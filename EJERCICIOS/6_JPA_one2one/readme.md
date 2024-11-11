# Ejercicio para practicar con la relación one to one

Incluimos en nuestro aprendizaje la relación one2one.

Además con este ejercicio, repasarás asociaciones ya vistas previamente.

El diagrama de entidades(tablas) y relaciones de la base de datos es este:

![image](https://github.com/user-attachments/assets/5e635f4c-53cc-47c0-8aa1-a0b016e5a48f)


Ten en cuenta:

- *Cliente* tiene una relación de **uno a muchos** con *Direccion*, ya que un cliente puede tener múltiples direcciones.
- *Cliente* tiene una relación de **uno a muchos** con *Factura*, indicando que un cliente puede emitir múltiples facturas.
- *Cliente* tiene una relación de **uno a uno** con *ClienteDetalle*, indicando que un cliente puede tener un solo detalle (como atributos de cliente prime y puntosAcumulados).
- *ClienteDetalle* tiene una relación inversa de **uno a uno** con *Cliente*.
- *Cliente* tiene una relación de **uno a uno** con Auditoria, que almacena la fecha de creación y edición del cliente.