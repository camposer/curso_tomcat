package config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import service.IUsuarioService;
import service.UsuarioService;
import dao.IUsuarioDao;
import dao.UsuarioDao;

@Configuration // Le dice a Spring que esta es una clase de configuraciones
@EnableTransactionManagement // Habilita el manejo de transacciones en Spring
public class Config {
	@Bean // Define un bean singleton que se llama entityManagerFactory
	public EntityManagerFactory entityManagerFactory() {
		return Persistence.createEntityManagerFactory("IJavaBlog");
	}

	@Bean
	public EntityManager entityManager() {
		return entityManagerFactory().createEntityManager();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory());
		return transactionManager;
	}
	
	@Bean
	public IUsuarioDao usuarioDao() {
		return new UsuarioDao();
	}

	@Bean
	public IUsuarioService usuarioService() {
		return new UsuarioService();
	}

}
