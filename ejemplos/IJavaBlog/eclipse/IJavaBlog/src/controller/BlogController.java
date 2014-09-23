package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import service.IUsuarioService;
import editor.FechaEditor;
import exception.AppServiceException;
import form.UsuarioForm;

@Controller
public class BlogController {
	@Autowired
	private IUsuarioService usuarioService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new FechaEditor());
	}
	
	@ModelAttribute("usuarioForm")
	public UsuarioForm usuarioForm() {
		return new UsuarioForm();
	}
	
	@RequestMapping("index")
	public String index() {
		return "jsp/blog/index.jsp";
	}
	
	private List<String> validar(String correo, String clave) {
		List<String> errores = new ArrayList<String>();

		// TODO: Añadir regexp para el correo
		if (correo == null || correo.trim().equals(""))
			errores.add("Correo inválido");
		
		if (clave == null || clave.trim().equals(""))
			errores.add("Clave inválida");
		
		return errores;
	}
	
	private List<String> validar(UsuarioForm usuarioForm) {
		List<String> errores = new ArrayList<String>();
		
		// TODO: Validar correo con regexp
		if (usuarioForm.getCorreo() == null ||
				usuarioForm.getCorreo().trim().equals(""))
			errores.add("Correo inválido");

		if (usuarioForm.getClave() == null ||
				usuarioForm.getClave().trim().equals(""))
			errores.add("Clave inválida");
		
		if (usuarioForm.getConfirmacionClave() == null ||
				usuarioForm.getConfirmacionClave().trim().equals(""))
			errores.add("Clave de confirmación inválida");
		
		if (usuarioForm.getClave() != null && 
				usuarioForm.getConfirmacionClave() != null &&
				!usuarioForm.getClave().equals(usuarioForm.getConfirmacionClave()))
			errores.add("Clave de confirmación no corresponde con clave");
		
		if (usuarioForm.getFechaNacimiento() == null) 
			errores.add("Fecha de nacimiento inválida");

		return errores;
	}
	
	@RequestMapping("agregarUsuario")
	public String agregarUsuario(
			@ModelAttribute("usuarioForm") UsuarioForm usuarioForm,
			Model model, 
			HttpSession session) {
		
		List<String> errores = validar(usuarioForm);
		
		if (errores.size() <= 0) {
			Usuario u = new Usuario();
			u.setCorreo(usuarioForm.getCorreo());
			u.setClave(usuarioForm.getClave());
			u.setFechanacimiento(usuarioForm.getFechaNacimiento());
			
			try {
				usuarioService.agregarUsuario(u);
				session.setAttribute("usuario", u);
			} catch (AppServiceException e) {
				e.printStackTrace();
				errores.add("Error al insertar usuario: " + e.getMessage());
			}
		}
		
		if (errores.size() > 0) {
			model.addAttribute("errores", errores);
			return "forward:/jsp/blog/index.jsp";
		} else {
			return "redirect:/inicio.blog";
		}
	}
	
	@RequestMapping("inicio")
	public String inicio() {
		return "forward:/jsp/blog/inicio.jsp";
	}
	
	@RequestMapping("autenticar")
	public String autenticar(
			@RequestParam("correo") String correo,
			@RequestParam("clave") String clave,
			HttpSession session,
			Model model) {
		
		List<String> errores = validar(correo, clave);
		
		Usuario u = null;
		try {
			u = usuarioService.autenticar(correo, clave);
			
			if (u == null)
				errores.add("Usuario o clave inválida");
			else
				session.setAttribute("usuario", u);
		} catch (AppServiceException e) {
			e.printStackTrace();
			errores.add("Error al acceder a la BD: " + e.getMessage());
		}
		
		if (errores.size() > 0) {
			model.addAttribute("errores", errores);
			return "forward:/jsp/blog/index.jsp";
		} else {
			return "redirect:/inicio.blog";
		}
		
	}
	
	@RequestMapping("cerrarSesion")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/index.blog";
	}
	
	@RequestMapping("registrarUsuario")
	public String registrarUsuario() {
		return "forward:/jsp/blog/registro.jsp";
	}
	
}


