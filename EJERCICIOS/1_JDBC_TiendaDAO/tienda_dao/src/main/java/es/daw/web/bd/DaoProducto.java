package es.daw.web.bd;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import es.daw.web.models.Producto;

import java.sql.*;

public class DaoProducto implements Dao<Producto>{

	private Connection con;
	
    public DaoProducto(String dbSettingsPropsFilePath) throws SQLException {
		super();
		con = DBConnection.getConnection(dbSettingsPropsFilePath);
	}

    @Override
    public Producto select(int id) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'select'");
    }

    @Override
    public List<Producto> selectAll() throws SQLException {

        try (
            PreparedStatement ps = con.prepareStatement("SELECT * FROM producto");
            ResultSet rs = ps.executeQuery()
        ){

            List<Producto> productos = new ArrayList<>();
	
	        while (rs.next()) {
	
	            Producto p = new Producto();
	            p.setCodigo(rs.getInt("codigo"));
	            p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getFloat("precio"));
                p.setCodigo_fabricante(rs.getInt("codigo_fabricante"));
	            productos.add(p);
	            
	        }
	        return productos;
        }
		



    }

    @Override
    public void insert(Producto t) throws SQLException {

        try(PreparedStatement ps = con.prepareStatement("INSERT INTO producto (nombre,precio,codigo_fabricante) VALUES (?,?,?)")){
            ps.setString(1, t.getNombre());
            ps.setFloat(2, t.getPrecio());
            ps.setInt(3,t.getCodigo_fabricante());

            ps.executeUpdate();

        }

    }

    @Override
    public void update(Producto t) throws SQLException {
        try(PreparedStatement ps = con.prepareStatement("UPDATE producto SET nombre = ? where codigo = ?")){
            ps.setString(1, t.getNombre());
            ps.setInt(2, t.getCodigo());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Producto t) throws SQLException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void delete(int id) throws SQLException {
        try(PreparedStatement ps = con.prepareStatement("DELETE FROM producto WHERE codigo = ?")){
            ps.setInt(1, id);

            ps.executeUpdate();

        }
    }
    
}
