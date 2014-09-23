package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import dao.IUsuarioDao;
import exception.AppDaoException;
import exception.AppServiceException;
import model.Usuario;

@Transactional // Le dice a Spring que gestione las transacciones 
public class UsuarioService implements IUsuarioService {
	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	public void agregarUsuario(Usuario u) {
		try {
			Usuario uBd = usuarioDao.obtenerPorCorreo(u.getCorreo());
			
			if (uBd == null) {
				usuarioDao.agregar(u);
			} else {
				throw new AppDaoException(new Exception("Usuario ya existente"));
			}
		} catch (AppDaoException e) {
			throw new AppServiceException(e);
		}
	}

	/**
	 * Si autentica devuelve el usuario sino devuelve null
	 */
	@Override
	public Usuario autenticar(String correo, String clave) {
		Usuario u = usuarioDao.obtenerPorCorreo(correo);
		
		if (u != null && u.getClave().equals(clave))
			return u;
		else
			return null;
	}

}
