package es.daw.jakarta.models;

public class ItemCarro {

    // ATRIBUTOS
    private int cantidad;
    private Producto producto;

    // CONSTRUCTORES
    public ItemCarro(int cantidad, Producto producto) {
        this.cantidad = cantidad;
        this.producto = producto;
    }

    // GETTERS & SETTERS
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // MÉTODOS DE COMPORTAMIENTO
    public int getImporte(){
        return cantidad*producto.getPrecio();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((producto == null) ? 0 : producto.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ItemCarro other = (ItemCarro) obj;
        if (producto == null) {
            if (other.producto != null)
                return false;
        } else if (!producto.equals(other.producto))
            return false;
        return true;
    }
    
    // SOBREESCRITURA DE MÉTODOS DE OBJECT...
    
    
    
}
