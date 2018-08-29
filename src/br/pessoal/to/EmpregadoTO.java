package br.pessoal.to;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EmpregadoTO")
public class EmpregadoTO {

	private int codPessoa;
	private String nomePessoa;
	private int idade;
	private String profissao;
	private String tipoUsuario;

	public EmpregadoTO() {
		this(0, "", 0, "", "");
	}

	public EmpregadoTO(int codPessoa, String nomePessoa, int idade, String profissao, String tipoUsuario) {
		this.codPessoa = codPessoa;
		this.nomePessoa = nomePessoa;
		this.idade = idade;
		this.profissao = profissao;
		this.tipoUsuario = tipoUsuario;
	}

	public int getCodPessoa() {
		return codPessoa;
	}

	public void setCodPessoa(int codPessoa) {
		this.codPessoa = codPessoa;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}	

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}


	@Override
	public String toString() {
		StringBuilder builderJSON = new StringBuilder();
		builderJSON.append("{")
		.append("\"PessoaTO\":")
		.append("{")
		.append("\"codPessoa\":")
		.append(codPessoa+",")
		.append("\"nomePessoa\":")
		.append("\""+nomePessoa+"\",")			
		.append("\"idade\":")
		.append(idade+",")		
		.append("\"profissao\":")
		.append("\""+profissao+"\",")			
		.append("\"usuario\":")
		.append("\""+tipoUsuario+"\"")			
		.append("}")
		.append("}");
		return builderJSON.toString();
	}
}
