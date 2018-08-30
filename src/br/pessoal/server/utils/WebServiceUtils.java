package br.pessoal.server.utils;

import br.pessoal.to.EmpregadoTO;
import br.pessoal.to.UsuarioTO;

public class WebServiceUtils {

	public boolean isEmpregadoVazio(EmpregadoTO empregadoTO) {
		
		boolean isPersonvalid = false;
		
		isPersonvalid = empregadoTO.getCodPessoa()>0?true:false;
		isPersonvalid = empregadoTO.getIdade()>0?true:false;
		isPersonvalid = empregadoTO.getNomePessoa().isEmpty()?false:true;
		isPersonvalid = empregadoTO.getProfissao().isEmpty()?false:true;		
		
		return !isPersonvalid;
	}

	public boolean isUsuarioVazio(UsuarioTO usuairoTO) {
		
		boolean isUsuariovalid = false;
		
		isUsuariovalid = usuairoTO.getSenha().isEmpty()?false:true;
		isUsuariovalid = usuairoTO.getTipoUsuario().isEmpty()?false:true;
		isUsuariovalid = usuairoTO.getUsuario().isEmpty()?false:true;		
		
		return !isUsuariovalid;
	}
	
	public boolean isStringEmptyOrNull(String conteudo) {
		
		boolean isStringValid = false;
		
		isStringValid = conteudo==null?false:true;
		isStringValid = conteudo.isEmpty()?false:true;
		
		return isStringValid;
	}
}
