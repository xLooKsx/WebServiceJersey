package br.pessoal.server.utils;

import br.pessoal.to.EmpregadoTO;
import br.pessoal.to.UsuarioTO;

public class WebServiceUtils {

	public boolean isEmpregadoVazio(EmpregadoTO empregadoTO) {

		if(empregadoTO == null)
			return true;

		int qtdCamposvazios = 0;

		qtdCamposvazios += empregadoTO.getCodPessoa()>0?-1:1;
		qtdCamposvazios += empregadoTO.getIdade()>0?-1:2;
		qtdCamposvazios += empregadoTO.getNomePessoa().isEmpty()?3:-1;
		qtdCamposvazios += empregadoTO.getProfissao().isEmpty()?4:-1;		

		return qtdCamposvazios>=0?true:false;
	}

	public boolean isUsuarioVazio(UsuarioTO usuairoTO) {

		if(usuairoTO == null)
			return true;

		int qtdCamposvazios = 0;

		qtdCamposvazios += usuairoTO.getSenha().isEmpty()?1:-1;
		qtdCamposvazios += usuairoTO.getTipoUsuario().isEmpty()?2:-1;
		qtdCamposvazios += usuairoTO.getUsuario().isEmpty()?3:-1;		

		return qtdCamposvazios>=0?true:false;
	}

	public boolean isStringEmptyOrNull(String conteudo) {

		int qtdCamposvazios = 0;

		qtdCamposvazios += conteudo==null?1:-1;
		qtdCamposvazios += conteudo.isEmpty()?-1:2;

		return qtdCamposvazios>=0?true:false;
	}

	public boolean isObjectNull(Object objeto) {

		if (objeto == null) {
			return true;
		}else {
			return false;
		}
	}
}
