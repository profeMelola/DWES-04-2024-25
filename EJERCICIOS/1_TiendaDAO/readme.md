# Trabajando con la base de datos TIENDA. Práctica guiada: tienda CRUD DAO

![image](https://github.com/user-attachments/assets/f28b7d19-07a4-403f-aae8-eeecfd1fa153)


![image](https://github.com/user-attachments/assets/3cffb1bb-1f2d-49f9-8f40-5e5a6fbf297a)


Vamos a realizar las cuatro operaciones básicas contra una tabla usando la base de datos **tienda**.

**[ C ]** **Create:** daremos de alta un producto indicando todos los campos necesarios (nombre, precio, fabricante).

**[ R ]** **Read:** obtendremos el listado de todos los productos.

**[ U ]** **Update:** actualizaremos el nombre de un producto.

**[ D ]** **Delete:** borraremos un producto por id.

## SERVLETS
Tendremos dos servlets:

### SERVLET ENCARGADO DE LISTAR LOS PRODUCTOS

![image](https://github.com/user-attachments/assets/c021d86f-3b49-45cb-8422-126bb3f6fa37)


### SERVLET ENCARGADO DE OPERACIONES DE MODIFICACIÓN (INSERT, UPDATE, DELETE)

___

#### Ejemplo de inserción:

![image](https://github.com/user-attachments/assets/c49e24f0-f954-4b08-b8ab-d9d1fcebedf5)

___

Una vez realizada la inserción correctamente, aparecerá el listado de productos ordenados por código descendentemente:

![image](https://github.com/user-attachments/assets/704e290d-917f-44ec-ae42-847be90ee3e3)

___

#### Ejemplo de borrado:

![image](https://github.com/user-attachments/assets/8340c376-eb61-41c9-a0bb-b3598616cc28)


Ha desaparecido el producto con código 13:

![image](https://github.com/user-attachments/assets/a5c1c6ab-df40-4b99-9574-ca0435ef28bd)


#### Ejemplo de actualización:

Vamos a cambiar el nombre a un producto, concretamente el que tiene código 12:

![image](https://github.com/user-attachments/assets/53222644-922c-44c4-a3a4-31188dacb65c)

___

En el nuevo listado aparece el nombre actualizado:

![image](https://github.com/user-attachments/assets/8d66b622-fa66-4d7b-955a-41f4713d35e7)











