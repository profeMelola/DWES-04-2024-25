package es.daw.jakarta.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import es.daw.jakarta.models.Producto;

public interface ProductService {
    
    // Método abstracto. No es necesario poner la palabra reservada abstract
    public List<Producto> listar();

    // Método para listar productos que se obtienen de un CSV
    public List<Producto> listar(String path) throws IOException;

    public Optional<Producto> buscarProducto(String name);

    public Producto buscarProducto2(String name);

    public Optional<Producto> buscarPorId(Long id);


}
