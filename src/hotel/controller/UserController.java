package hotel.controller;

import hotel.dao.UsuarioDAO;
import hotel.factory.ConnectionFactory;

public class UserController {
	
	private UsuarioDAO userDAO;
	
	public UserController() {
		this.userDAO = new UsuarioDAO(new ConnectionFactory().conectar());
	}
	
	public boolean validarUsuario(String nombreUsuario, String password) {
		return userDAO.existeUsuario(nombreUsuario, password);
	}

}