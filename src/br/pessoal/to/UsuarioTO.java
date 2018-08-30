package br.pessoal.to;

public class UsuarioTO {

	private String usuario;
	private String senha;
	private String tipoUsuario;
	
	public UsuarioTO() {
		this("", "", "");
	}
	
	public UsuarioTO(String usuario, String senha, String tipoUsuario) {
		this.usuario = usuario;
		this.senha = senha;
		this.tipoUsuario = tipoUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}	
}
