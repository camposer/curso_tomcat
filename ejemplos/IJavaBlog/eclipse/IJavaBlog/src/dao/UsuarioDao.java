package dao;

import java.util.List;

import javax.persistence.Query;

import model.Usuario;

public class UsuarioDao 
		extends GenericDao<Usuario, Integer> 
		implements IUsuarioDao {

	@SuppressWarnings("unchecked")
	@Override
	public Usuario obtenerPorCorreo(String correo) {
		String jql = "SELECT u FROM Usuario u WHERE u.correo = :correo";
		Query q = em.createQuery(jql);
		q.setParameter("correo", correo);
		
		List<Usuario> usuarios = q.getResultList();
		
		if (usuarios != null && usuarios.size() > 0)
			return usuarios.get(0);
		else
			return null;
	}

}
