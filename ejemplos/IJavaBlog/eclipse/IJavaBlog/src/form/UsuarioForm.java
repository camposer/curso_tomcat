package form;

import java.util.Date;

public class UsuarioForm {
	private String correo;
	private String clave;
	private String confirmacionClave;
	private Date fechaNacimiento;
	
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getConfirmacionClave() {
		return confirmacionClave;
	}
	public void setConfirmacionClave(String confirmacionClave) {
		this.confirmacionClave = confirmacionClave;
	}
}
