# Práctica guiada para aprender a usar CDI

Partimos del proyecto sin persistencia en base de datos del carro https://github.com/profeMelola/DWES-03-2024-25/tree/main/EJERCICIOS/Filter/solucion_listener_filtro

No vamos a modificar la funcionalidad, pero vamos a comprobar que ahorramos código y queda mucho más limpio, modular y reutilizable con CDI.

## Vamos a modificar:

1. Clase Carro (convertilo en un Bean @SessionScoped)
2. Listener (sessionCreated para no crear ni guardar en sesión el carro )
3. AgregarCarroServlet (para @Inject el Carro)
4. En nuestra aplicación, no tenemos el controlador o Servlet para actualizar el carro (forma parte de la práctica), pero si tuviéramos ActualizarCarroServlet, también usaríamos @Inject
5. Es necesario crear un archivo de configuración llamado beans.xml con la etiqueta beans vacía en WEB-INF del proyecto. Es requisito.
6. 
![image](https://github.com/user-attachments/assets/483fcdf2-e322-40ac-b6e3-d9786df269a3)

