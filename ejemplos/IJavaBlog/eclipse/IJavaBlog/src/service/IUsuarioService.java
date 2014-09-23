package service;

import model.Usuario;

public interface IUsuarioService {
	public void agregarUsuario(Usuario u);
	public Usuario autenticar(String usuario, String clave);
}
