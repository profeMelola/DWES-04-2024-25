# Trabajando con la base de datos TIENDA

![Captura de pantalla de 2024-04-25 20-42-00](https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/aa73fee6-f328-4082-84f6-708f6929ad9e)

# 1. Práctica guiada: tienda CRUD DATO


![image](https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/79b85a89-84e3-4059-89af-7eac50fbb1c6)

Vamos a realizar las cuatro operaciones básicas contra una tabla usando la base de datos **tienda**.

**[ C ]** **Create:** daremos de alta un producto indicando todos los campos necesarios (nombre, precio, fabricante).

**[ R ]** **Read:** obtendremos el listado de todos los productos.

**[ U ]** **Update:** actualizaremos el nombre de un producto.

**[ D ]** **Delete:** borraremos un producto por id.

## SERVLETS
Tendremos dos servlets:

### SERVLET ENCARGADO DE LISTAR LOS PRODUCTOS

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/bf0d0086-3648-46ae-9610-d52e56191b0f" height="400px"/>

### SERVLET ENCARGADO DE OPERACIONES DE MODIFICACIÓN (INSERT, UPDATE, DELETE)

___

#### Ejemplo de inserción:

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/6b7f09ef-6c2f-419c-9a3b-567f460c8f47" height="400px"/>
___

Una vez realizada la inserción correctamente, aparecerá el listado de productos ordenados por código descendentemente:

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/f4edf22d-5017-4d17-88bb-593dcba159e4" height="400px"/>

___

#### Ejemplo de borrado:

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/59e3ff37-6fdb-442e-b9ed-78c39f151f26" height="200px"/>

Ha desaparecido el producto con código 13:

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/23c7ff4b-322e-4c6a-a06f-4939aca119b1" height="400px"/>

#### Ejemplo de actualización:

Vamos a cambiar el nombre a un producto, concretamente el que tiene código 12:

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/39603387-0d51-4105-b0ee-e1059ae5af83" height="400px"/>

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/e899b665-3ddc-4586-9516-8a4fc50698a5" height="200px"/>

___

En el nuevo listado aparece el nombre actualizado:

<img src="https://github.com/profeMelola/Programacion-08-2023-24/assets/91023374/260082e9-6357-45fe-a406-97cce8b008e4" height="400px"/>










