package web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean
@SessionScoped
public class SesionBean implements Serializable {

	private String usuario;
	private String rol; // "propietario" o "cliente"

	public String login() {
		if (usuario == null || usuario.trim().isEmpty() || rol == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Introduce usuario y rol"));
			return null;
		}
		// Simulamos el inicio de sesión
		return "dashboard?faces-redirect=true";
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "index?faces-redirect=true";
	}

	public boolean isPropietario() {
		return "propietario".equals(rol);
	}

	public boolean isCliente() {
		return "cliente".equals(rol);
	}

	// Getters y Setters
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}
}