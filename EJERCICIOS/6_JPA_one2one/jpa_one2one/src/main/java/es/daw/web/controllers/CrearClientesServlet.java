package es.daw.web.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

import es.daw.web.entities.Auditoria;
import es.daw.web.entities.Cliente;
import es.daw.web.entities.ClienteDetalle;
import es.daw.web.entities.Direccion;
import es.daw.web.entities.Factura;
import es.daw.web.entities.FormaPago;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;


@WebServlet("/clientes/save")
@Transactional
public class CrearClientesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

      try{
        System.out.println("*********** CREAR CLIENTE ***********");

        // METO TODAS LA OPERACIONES EN LA MISMA TRANSACCIÓN
        // Estás indicando a JPA que los cambios que realices entre estas dos llamadas deben ser tratados como una unidad de trabajo indivisible.
        // Si ocurre un error o una excepción antes de commit, puedes llamar a em.getTransaction().rollback()
        // para deshacer todos los cambios, asegurando que la base de datos no quede en un estado inconsistente.
        em.getTransaction().begin();

        // -------- CLIENTE ---------
        // Voy a dar de alta un nuevo cliente
        Cliente cliente = new Cliente();
        // Porque cliente tiene como atributo embebido un objeto auditoria
        //Auditoria audit = new Auditoria();

        cliente.setNombre("melola");
        cliente.setApellido("elola");
        cliente.setFormaPago(FormaPago.PAYPAL);
        // //cliente.setAudit(audit); //cliente tiene un atributo audit que hay que inicializar con un objeto audit...

        // -------- DIRECCIONES ---------

        Direccion dir1 = new Direccion();
        dir1.setCalle("Alonso");
        dir1.setNumero(666);

        Direccion dir2 = new Direccion();
        dir2.setCalle("Avellaneda");
        dir2.setNumero(999);

        cliente.addDireccion(dir1);
        cliente.addDireccion(dir2);


        // -------- DETALLES ---------

        ClienteDetalle detalle = new ClienteDetalle();
        detalle.setPrime(true);
        detalle.setPuntosAcumulados(10L);

        ClienteDetalle detalle2 = new ClienteDetalle();
        detalle2.setPrime(false);
        detalle2.setPuntosAcumulados(1L);

        cliente.setDetalle(detalle);
        cliente.setDetalle(detalle2);


        // -------- FACTURAS ---------
        Factura f1 = new Factura();
        f1.setDescripcion("factura 1");
        f1.setTotal(1000L);

        Factura f2 = new Factura();
        f2.setDescripcion("factura 2");
        f2.setTotal(2000L);

        cliente.addFactura(f1);
        cliente.addFactura(f2);


        // ---------------------------------------------------------------------------------------------
        // -------- DAR DE ALTA EL CLIENTE... AUTOMÁTICAMENTE SE DAN DE ALTA SUS REGISTROS ASOCIADOS...
        em.persist(cliente);
        System.out.println(" ***********> cliente dado de alta...");
        // ---------------------------------------------------------------------------------------------


        // -----------------------------------------------------------------------------------------------------------------------------
        // --------------- A PARTIR DE AQUÍ, PARA IR PROBANDO LOS DIFERENTES ESCENARIOS, COMENTAD Y DESCOMENTAD CÓDIGO -----------------
        // -----------------------------------------------------------------------------------------------------------------------------

        // ---------------------------------------------------------------
        // ---------- BORRAR EL CLIENTE 
        // Al borrar el cliente se borran todos los registros asociados
        // em.remove(cliente);
        // System.out.println(" ***********> cliente borrado...");
        // ----------------------------------------------------------------

        // ----------------------------------------------------------------------------------------
        // ---------------- OTRAS PRUEBAS TRASTEANDO DIRECTAMENTE CON FACTURAS --------------------
        // // INSERTAR DIRECTAMENTE UN OBJETO FACTURA
        // Factura f3 = new Factura();
        // f3.setDescripcion("factura 3");
        // f3.setTotal(2000L);

        // // Se inserta con null en el campo id_cliente (FK), salvo que hayamos puesto en Factura ==> nullable = false.
        // // En el @JoinColumn(name="id_cliente", nullable = false). Esto lanzará una excepción. 
        // // em.persist(f3);
        // // System.out.println(" ***********> factura creada sin asociar a cliente");

        // f3.setCliente(cliente);
        // em.persist(f3);
        // System.out.println(" ***********> factura creada asociando cliente");

        // em.remove(f3); // Ni caso....
        // System.out.println("***********> factura3 borrada directamente ...");

        // cliente.removeFactura(f2);
        // System.out.println(" ***********> factura 2 borrada usando removeFactura de cliente...");
        // ----------------------------------------------------------------------------------------

        em.getTransaction().commit();

        
      }catch(Exception e){
        em.getTransaction().rollback();
        e.printStackTrace();
        response.sendError(response.SC_INTERNAL_SERVER_ERROR,e.getMessage());
      }

      response.setContentType("text/plain");
      response.getWriter().write("Tablas creadas y registros dados de alta!");

        

    }

}
