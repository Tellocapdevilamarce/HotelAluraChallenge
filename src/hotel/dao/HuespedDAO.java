package hotel.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hotel.modelo.Huesped;

public class HuespedDAO {

	private Connection con;

	public HuespedDAO(Connection con) {
		this.con = con;
	}

	// Method to Create a new temporaly resident

	public void crear(Huesped huesped) {
		try {
			con.setAutoCommit(false);

			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO huespedes (nombre, apellido, fecha_de_nacimiento, nacionalidad, telefono, id_reserva) VALUES(?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			try {
				
				guardarRegistro(huesped, statement);
				con.commit();
			} catch (Exception e) {
				e.getMessage();
				con.rollback();
			}
		} catch (SQLException e) {
			e.getMessage();
			throw new RuntimeException(e);
		}
	}

	private void guardarRegistro(Huesped huesped, PreparedStatement statement) throws SQLException {
		statement.setString(1, huesped.getNombre());
		statement.setString(2, huesped.getApellido());
		statement.setDate(3, huesped.getFechaNacimiento());
		statement.setString(4, huesped.getNacionalidad());
		statement.setString(5, huesped.getTelefono());
		statement.setInt(6, huesped.getReservaId());

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();
		try (resultSet) {
			while (resultSet.next()) {
				huesped.setId(resultSet.getInt(1));
				
			}
		}

	}

	//Method to find current reservation
	public Integer idReservaActual() {
		int id = 1;
		try {
			String querySelect = "SELECT ID FROM reserva ORDER BY id DESC LIMIT 1;";
			final PreparedStatement statement = con.prepareStatement(querySelect);
			try (statement) {
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				while (resultSet.next()) {
					id = resultSet.getInt("ID");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return id;
	}
	
	//Method to get all Residents

	public List<Huesped> listar() {
		List<Huesped> huespedes = new ArrayList<>();

		try {
			final PreparedStatement statement = con.prepareStatement(
					"SELECT ID, NOMBRE, APELLIDO, FECHA_DE_NACIMIENTO, NACIONALIDAD, TELEFONO, ID_RESERVA FROM huespedes;");
			try (statement) {
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				while (resultSet.next()) {
					Huesped fila = new Huesped(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
							resultSet.getString("APELLIDO"), resultSet.getDate("FECHA_DE_NACIMIENTO"),
							resultSet.getString("Nacionalidad"), resultSet.getString("TELEFONO"),
							resultSet.getInt("ID_RESERVA"));
							huespedes.add(fila);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return huespedes;
	}
	
	//Method to modify a Resident

	public int modificar(Integer id, String nombre, String apellido, Date fechaNacimiento, String nacionalidad,
			String telefono, int reservaId) {
		
		try {
			final PreparedStatement statement = con.prepareStatement(
					"UPDATE huespedes SET " + "nombre = ?, " + "apellido = ?, " + "fecha_de_Nacimiento = ?, "
							+ "nacionalidad = ?, " + "telefono = ?, " + "id_Reserva = ? WHERE id = ? ;");

			try (statement) {
				statement.setString(1, nombre);
				statement.setString(2, apellido);
				statement.setDate(3, fechaNacimiento);
				statement.setString(4, nacionalidad);
				statement.setString(5, telefono);
				statement.setInt(6, reservaId);
				statement.setInt(7, id);

				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//Method to delete a resident
	
	public int eliminar(Integer id) {
		
		try {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM huespedes WHERE ID = ? ;");
			try(statement){
				statement.setInt(1, id);
				statement.execute();
				return statement.getUpdateCount();
			}
		}catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//Method to get resident identified by his ID
	
	public List<Huesped> listar(Integer id) {
		List<Huesped> huesped = new ArrayList<>();

		try {
			final PreparedStatement statement = con.prepareStatement(
					"SELECT ID, nombre, apellido, fecha_de_nacimiento, nacionalidad, telefono, id_reserva FROM huespedes WHERE id_reserva = ?");
			try (statement) {
				statement.setInt(1, id);
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				while (resultSet.next()) {
					Huesped fila = new Huesped(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
							resultSet.getString("APELLIDO"), resultSet.getDate("FECHA_DE_NACIMIENTO"),
							resultSet.getString("Nacionalidad"), resultSet.getString("TELEFONO"),
							resultSet.getInt("ID_RESERVA"));
					huesped.add(fila);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return huesped;
	}
	
	//Method to get resident identified by his last name

	public List<Huesped> listar(String apellido) {
		List<Huesped> huesped = new ArrayList<>();

		try {
			final PreparedStatement statement = con.prepareStatement(
					"SELECT ID, NOMBRE, APELLIDO, FECHA_DE_NACIMIENTO, NACIONALIDAD, TELEFONO, ID_RESERVA FROM huespedes WHERE APELLIDO = ?");
			try (statement) {
				statement.setString(1, apellido);
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				while (resultSet.next()) {
					Huesped fila = new Huesped(resultSet.getInt("ID"), resultSet.getString("NOMBRE"),
							resultSet.getString("APELLIDO"), resultSet.getDate("FECHA_DE_NACIMIENTO"),
							resultSet.getString("Nacionalidad"), resultSet.getString("TELEFONO"),
							resultSet.getInt("ID_RESERVA"));
							huesped.add(fila);
					
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return huesped;
	}

}