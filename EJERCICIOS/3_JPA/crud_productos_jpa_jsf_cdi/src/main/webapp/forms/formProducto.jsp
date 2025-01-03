<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Conexión a base de datos</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <style>
            h1 {
                text-align: center;
            }

            body {
                font-size: 16px;
                font-family: sans-serif;
            }

            form {
                margin: 0px auto;
                width: 50%;
            }

            .fieldset-principal {
                padding-left: 2em;
                padding-right: 2em;
                margin-top: 10px;
                margin-bottom: 10px;
                border: 2px solid #395870;
                box-shadow: 3px 6px 2px rgba(0, 0, 0, .3);
            }

            .fieldset-principal>legend {
                padding: 0.2em 0.5em;
                border: 1px solid;
                background: #395870;
                background: linear-gradient(#49708f, #293f50);
                color: white;
            }

            legend {
                font-weight: bold;
            }

            label[for] {
                margin-top: 0.5em;
                font-weight: bold;
                display: block;
            }

            label input[type="checkbox"],
            label input[type="radio"] {
                display: inline;
                font-weight: normal;
            }

            .obligatorio:after {
                content: ' (obligatorio)';
            }

            button {
                padding: 0 5px 0 5px;
                height: 25px;
                line-height: 20px;
                border: 2px solid #395870;
                border-radius: 6px;
                background: #395870;
                background: linear-gradient(#49708f, #293f50);
                font-weight: bold;
                font-size: 16px;
                color: white;
            }

            button:hover {
                background: #314b60;
                box-shadow: inset 0 0 10px 1px rgba(0, 0, 0, .3);
            }

            /* CSS para el enlace redondeado con sombra */
            .custom-rounded-link {
                display: inline-block;
                padding: 10px 20px;
                color: #fff;
                background-color: #17a2b8;
                border-radius: 15px;
                text-decoration: none;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                transition: all 0.3s ease;
            }

            .custom-rounded-link:hover {
                background-color: #138496;
                box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
            }
        </style>

    </head>

    <body>
        <h1>ADMINISTRAR PRODUCTOS</h1>
        <form action="<%=request.getServletContext()%>/productos/jpa" method="GET" target="_blank">
            <fieldset class="fieldset-principal">
                <legend>Datos del producto</legend>
                <label for="nombre">Nombre:<input type="text" name="nombre" /></label>
                <label for="precio">Precio:<input type="text" name="precio" /></label>
                <label for="departamento">Fabricantes:</label>

                <!-- GENERAR LOS FABRICANTES DINÁMICAMENTE-->
                 <!-- Usa un servlet que obtenga el listado de fabricantes y luego los pase a través del request -->
                <!--<select name="codigo_fabricante">
                    <option value="1">Asus</option>
                    <option value="2">Lenovo</option>
                    <option value="3">Hewlett-Packard</option>
                    <option value="4">Samsung</option>
                    <option value="5">Seagate</option>
                    <option value="6">Crucial</option>
                    <option value="7">Gigabyte</option>
                    <option value="8">Huawei</option>
                    <option value="9">Xiaomi</option>
                </select>-->

            </fieldset>

            <fieldset class="fieldset-principal">
                <legend>Operaciones</legend>
                <label for="operacion">Listar productos:<input type="radio" value="select"
                    name="operacion" required /></label>
                <label for="operacion">Insertar nuevo producto con todos los campos:<input type="radio" value="insert"
                        name="operacion" required /></label>
                <label for="operacion">Actualizar nombre del producto:<input type="radio" name="operacion"
                        value="update" required /></label>
                <label for="operacion">Borrar por código de producto:<input type="radio" name="operacion" value="delete"
                        required /></label>
                <label for="codigo">Código del producto (actualizar/borrar):<input type="text" name="codigo" /></label>

            </fieldset>

            <button name="enviar" type="submit">Ejecutar controlador</button>
        </form>

        <p><a href="../index.html" class="custom-rounded-link">Volver</a></p>
    </body>

    </html>