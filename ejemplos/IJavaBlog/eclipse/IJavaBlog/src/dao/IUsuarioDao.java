package dao;

import model.Usuario;

public interface IUsuarioDao extends IDao<Usuario, Integer> {
	public Usuario obtenerPorCorreo(String correo);
}
