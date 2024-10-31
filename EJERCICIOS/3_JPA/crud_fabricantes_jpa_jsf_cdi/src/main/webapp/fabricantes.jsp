<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="es.daw.web.models.Fabricante"%>

    <!DOCTYPE html>
    <html>
    <head>
        <title>Fabricantes</title>
    </head>
    <body>
        <h1>Operación - <%=(String)request.getAttribute("operacion") %> - realizada con éxito</h1>
        <h2> LISTADO DE FABRICANTES </h2>
        <table border='1'>
            <tr>
                <th>CÓDIGO</th>
                <th>NOMBRE</th>
            </tr>
            <%
                List<Fabricante> fabricantes = (List<Fabricante>)request.getAttribute("fabricantes");
                for (Fabricante f: fabricantes){
            %>

                    <tr>
                        <td><%=f.getCodigo()%></td>
                        <td><%=f.getNombre()%></td>
                    </tr>
            <%}%>
            <a href='<%=request.getContextPath()%>/index.html'>Volver</a>
        </table>
    </body>

    </html>