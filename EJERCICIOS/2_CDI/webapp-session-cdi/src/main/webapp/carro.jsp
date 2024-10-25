<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="es.daw.jakarta.models.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Carro de compras</title>
</head>
<body>
    <h1>Carro de compras</h1>
    <%
        Carro carro = (Carro) session.getAttribute("carro");

        if (carro == null || carro.getItems().isEmpty()) { %>

            <p>Lo sentimos, no hay productos en el carro de compras!</p>

        <%} else {%>

            <table>
                <tr>
                    <th>id</th>
                    <th>nombre</th>
                    <th>precio</th>
                    <th>cantidad</th>
                    <th>total</th>
                </tr>

                <% for(ItemCarro item: carro.getItems()){   %>
                <tr>
                    <td><%=item.getProducto().getId()%></td>
                    <td><%=item.getProducto().getNombre()%></td>
                    <td><%=item.getProducto().getPrecio()%></td>
                    <td><%=item.getCantidad()%></td>
                    <td><%=item.getImporte()%></td>
                </tr>
                <%}%>
                <tr>
                    <td colspan="4" style="text-align: right">Total:</td>
                    <td><%=carro.getTotal()%></td>
                </tr>

            </table>
        <%}%>
</body>
</html>
