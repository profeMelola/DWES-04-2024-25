package es.daw.web.models;

import java.util.Comparator;

public class ComparatorProdByNameDesc implements Comparator<Producto>{

    @Override
    public int compare(Producto p1, Producto p2) {
        return p2.getNombre().compareTo(p1.getNombre());
    }

    
}
