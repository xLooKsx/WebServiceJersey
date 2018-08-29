package br.pessoal.server.utils;

import br.pessoal.to.EmpregadoTO;

public class WebServiceUtils {

	public boolean isEmpregadoVazio(EmpregadoTO empregadoTO) {
		
		boolean isPersonvalid = false;
		
		isPersonvalid = empregadoTO.getCodPessoa()>0?true:false;
		isPersonvalid = empregadoTO.getIdade()>0?true:false;
		isPersonvalid = empregadoTO.getNomePessoa().isEmpty()?false:true;
		isPersonvalid = empregadoTO.getProfissao().isEmpty()?false:true;
		isPersonvalid = empregadoTO.getTipoUsuario().isEmpty()?false:true;
		
		return isPersonvalid;
	}
}
