# Trabajando con la base de datos TIENDA. Práctica guiada: tienda CRUD DAO

![image](https://github.com/user-attachments/assets/f28b7d19-07a4-403f-aae8-eeecfd1fa153)


![image](https://github.com/user-attachments/assets/3cffb1bb-1f2d-49f9-8f40-5e5a6fbf297a)


Vamos a realizar las cuatro operaciones básicas contra una tabla usando la base de datos **tienda**.

**[ C ]** **Create:** daremos de alta un producto indicando todos los campos necesarios (nombre, precio, fabricante).

**[ R ]** **Read:** obtendremos el listado de todos los productos.

**[ U ]** **Update:** actualizaremos el nombre de un producto.

**[ D ]** **Delete:** borraremos un producto por id.

___

**Descarga el proyecto inicial. Observa los fuentes proporcionados:**


![image](https://github.com/user-attachments/assets/75f8fdea-692f-4f55-9f98-d4bb70962491)

## CONFIGURACIÓN DE LA CONEXIÓN A H2

### 1.Fichero **JDBC.properties** que está en webapp del proyecto:

```
# Propiedad de conexión a base de datos
url=jdbc:h2:~/tienda;AUTO_SERVER=TRUE
user=sa
password=
```
Muy importante no olvidar indicar **AUTO_SERVER=TRUE** tanto en la url del fichero de propiedades como en la consola de H2 para poder usar más de una conexión abierta:

![error conexiones](https://github.com/user-attachments/assets/43a0bb8f-2a6c-4764-9acd-718b3f108893)

#### 2. Indicar el driver de conexión adecuado

En la clase **DBConnection** del paquete package es.daw.web.bd:

```
Class.forName("org.h2.Driver");
```



## SERVLETS (CONTROLLERS)

Tendremos dos servlets o controladores:

### 1. SERVLET ENCARGADO DE LISTAR LOS PRODUCTOS

![image](https://github.com/user-attachments/assets/c021d86f-3b49-45cb-8422-126bb3f6fa37)


### 2. SERVLET ENCARGADO DE OPERACIONES DE MODIFICACIÓN (INSERT, UPDATE, DELETE) DE PRODUCTOS

#### Ejemplo de inserción:

![image](https://github.com/user-attachments/assets/c49e24f0-f954-4b08-b8ab-d9d1fcebedf5)

___

Una vez realizada la inserción correctamente, aparecerá el listado de productos ordenados por código descendentemente (ordenación a realizar en Java, no order by sql):

![image](https://github.com/user-attachments/assets/704e290d-917f-44ec-ae42-847be90ee3e3)

___

#### Problemas al insertar productos. Fallo codigo duplicado.

El script de base de datos no ha creado el codigo como AUTO_INCREMENT.

La solución es modificar la tabla. Ejecutar lo siguiente:

```
ALTER TABLE producto
ALTER COLUMN codigo INT AUTO_INCREMENT;
ALTER TABLE producto ALTER COLUMN codigo RESTART WITH 12;
```



#### Ejemplo de borrado:

![image](https://github.com/user-attachments/assets/8340c376-eb61-41c9-a0bb-b3598616cc28)


___

Ha desaparecido el producto con código 13:

![image](https://github.com/user-attachments/assets/a5c1c6ab-df40-4b99-9574-ca0435ef28bd)


___

#### Ejemplo de actualización:

Vamos a cambiar el nombre a un producto, concretamente el que tiene código 12:

![image](https://github.com/user-attachments/assets/53222644-922c-44c4-a3a4-31188dacb65c)

___

En el nuevo listado aparece el nombre actualizado:

![image](https://github.com/user-attachments/assets/8d66b622-fa66-4d7b-955a-41f4713d35e7)











