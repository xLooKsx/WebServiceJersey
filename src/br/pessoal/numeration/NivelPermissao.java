package br.pessoal.numeration;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum NivelPermissao {

	ADMIN("ADMIN"), OPERADOR("OPERADOR"), UNKNOWN("UNKNOWN");
	
	private final String nivelPermissao;
	
	NivelPermissao(final String nivelPermissao) {
		this.nivelPermissao = nivelPermissao;
	}

	public String getNivelPermissao() {
		return nivelPermissao;
	}

	public List listNivelPermissao(){
		return new ArrayList<Enum>(EnumSet.allOf(NivelPermissao.class));
	}
}
