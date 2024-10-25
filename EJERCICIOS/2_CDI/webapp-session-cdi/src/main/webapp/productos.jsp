<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Optional"%>
<%@page import="es.daw.jakarta.models.Producto"%>
<%@page import="es.daw.jakarta.services.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Listado de productos</title>
<style>
    table{
        border-collapse: collapse;
    }
    th, td{
        border: 5px solid fuchsia;
    }
</style>
</head>
<body>
    <h1>Listado de productos</h1>
    <h2>
        <%
            LoginService auth = new LoginServiceImpl();

            Optional<String> sessionOpt = auth.getUserName(request);
            if (sessionOpt.isPresent())
                out.print("Hola "+sessionOpt.get()+" bienvenido!!!!!");
            else
                out.print("NO REGISTRADO!!!");
        %>

    </h2>

    <table>
        <thead>
            <th>ID</th>
            <th>NOMBRE</th>
            <th>TIPO</th>
            <% if (sessionOpt.isPresent()) {%>
                <th>PRECIO</th>
                <th>AGREGAR</th>
            <%}%>
        </thead>
        <tbody>
            <%
                List<Producto> productos = (List<Producto>)request.getAttribute("productos");

                for (Producto p: productos){
                    out.println("<tr>");
                    out.println("<td>"+p.getId()+"</td>");
                    out.println("<td>"+p.getNombre()+"</td>");
                    out.println("<td>"+p.getTipo()+"</td>");

                    if (sessionOpt.isPresent()){
                        out.println("<td>"+p.getPrecio()+"</td>");
                        out.println("<td><a href='carro/agregar?id="+p.getId()+"'>Agregar producto</a></td>");
                    }

                    out.println("</tr>");
                }
            %>
        </tbody>
    </table>
    <p><span style="font-weight: bold;">Request attribute:</span> <%= (String)request.getAttribute("mensaje")%></p>
    <p><span style="font-weight: bold;">Context attribute:</span> <%= (String)application.getAttribute("mensaje")%></p>
</body>
</html>
