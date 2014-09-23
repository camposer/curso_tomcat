package dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import exception.AppDaoException;

public abstract class GenericDao<T, K> implements IDao<T, K> {
	@Autowired
	@Qualifier("entityManager") // Especifica el id del bean
	@PersistenceContext // Habilita el manejo de EntityManager por parte de Spring. Permite el manejo de @Transactional
	protected EntityManager em;
	protected Class<T> clase;
	
	@SuppressWarnings("unchecked")
	public GenericDao() {
		// Obtiene en tiempo de ejecución la clase de la tabla (primer genérico de la superclase BaseDao)
		// Utiliza la API de Reflection de Java
		clase = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	@Override
	protected void finalize() throws Throwable {
		em.close();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> obtenerTodos() {
		try {
			return 	em
						.createQuery("SELECT t FROM " + 
								clase.getSimpleName() + " t")
						.getResultList();
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			throw new AppDaoException(pe);
		}
	}

	@Override
	public void agregar(T t) {
		try {
			em.persist(t);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			throw new AppDaoException(pe);
		}
	}

	@Override
	public T obtener(K id) {
		try {
			return em.find(clase, id);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			throw new AppDaoException(pe);
		}
	}

	@Override
	public void modificar(T t) {
		try {
			em.merge(t);
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			throw new AppDaoException(pe);
		}
	}

	@Override
	public void eliminar(K id) {
		try {
			T obj = em.find(clase, id); // => SELECT * FROM clase WHERE id = ?
			em.remove(obj); // => DELETE FROM clase WHERE id = ?
		} catch (PersistenceException pe) {
			pe.printStackTrace();
			throw new AppDaoException(pe);
		}
	}
}

