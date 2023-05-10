package hotel.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import hotel.modelo.Reserva;

public class ReservaDAO {
	private Connection con;

	public ReservaDAO(Connection con) {
		this.con = con;
	}

	// Method to create a new reservation

	public void crear(Reserva reserva) {
		try {
			con.setAutoCommit(false);
			final PreparedStatement statement = con.prepareStatement(
					"INSERT INTO reserva (fecha_de_Entrada, fecha_de_Salida, valor, forma_de_Pago, tipo_de_Habitacion) VALUES (?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);

			try (statement) {
				guardarReserva(reserva, statement);
				con.commit();
			} catch (SQLException e) {
				e.getMessage();
				con.rollback();
			}

		} catch (SQLException e) {
			e.getMessage();
			throw new RuntimeException(e);
		}
	}

	public void guardarReserva(Reserva reserva, PreparedStatement statement) throws SQLException {
		statement.setDate(1, reserva.getFechaEntrada());
		statement.setDate(2, reserva.getFechaSalida());
		statement.setDouble(3, reserva.getValor());
		statement.setString(4, reserva.getFormaPago());
		statement.setString(5, reserva.getTipoHabitacion());

		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();
		
		try (resultSet) {
			while (resultSet.next()) {
				System.out.println(resultSet.getInt(1));
				reserva.setId(resultSet.getInt(1));
				
			}
		}
	}

	public List<Reserva> listar() {
		List<Reserva> reservas = new ArrayList<>();

		try {
			final PreparedStatement statement = con
					.prepareStatement("SELECT ID, FECHA_DE_ENTRADA, FECHA_DE_SALIDA, VALOR, FORMA_DE_PAGO, TIPO_DE_HABITACION FROM reserva");

			try (statement) {
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				
				while (resultSet.next()) {
					Reserva fila = new Reserva(resultSet.getInt("ID"), resultSet.getDate("FECHA_DE_ENTRADA"),
					resultSet.getDate("FECHA_DE_SALIDA"), resultSet.getDouble("VALOR"),
					resultSet.getString("FORMA_DE_PAGO"), resultSet.getString("TIPO_DE_HABITACION"));

					reservas.add(fila);
					System.out.println("mate");
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return reservas;
	}

	public int modificar(Integer id, Date fechaEntrada, Date fechaSalida, double valor, String formaPago, String tipoHabitacion) {

		try {

			final PreparedStatement statement = con.prepareStatement("UPDATE reserva SET " + "fecha_de_Entrada = ?, "
					+ "fecha_de_Salida = ?, " + "valor = ?, " + "forma_de_Pago = ?, tipo_de_Habitacion = ? WHERE id = ?");

			try (statement) {
				statement.setDate(1, fechaEntrada);
				statement.setDate(2, fechaSalida);
				statement.setDouble(3, valor);
				statement.setString(4, formaPago);
				statement.setString(5, tipoHabitacion);
				statement.setInt(6, id);

				statement.execute();

				int updateCount = statement.getUpdateCount();

				return updateCount;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public int eliminar(Integer id) {

		try {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM reserva WHERE ID = ? ;");
			try (statement) {
				statement.setInt(1, id);
				statement.execute();
				return statement.getUpdateCount();
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Reserva> listar(int id) {
		List<Reserva> reserva = new ArrayList<>();

		try {
			final PreparedStatement statement = con.prepareStatement(
					"SELECT ID, fecha_de_entrada, fecha_de_salida, valor, forma_de_pago, tipo_de_habitacion FROM reserva WHERE ID = ?");
			try (statement) {
				statement.setInt(1, id);
				statement.execute();
				final ResultSet resultSet = statement.getResultSet();
				while (resultSet.next()) {
				
					Reserva fila = new Reserva(resultSet.getInt("ID"), resultSet.getDate("FECHA_DE_ENTRADA"),
					resultSet.getDate("FECHA_DE_SALIDA"), resultSet.getDouble("VALOR"),
					resultSet.getString("FORMA_DE_PAGO"), resultSet.getString("TIPO_DE_HABITACION"));

					reserva.add(fila);
					
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return reserva;
	}

}