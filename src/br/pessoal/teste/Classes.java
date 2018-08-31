package br.pessoal.teste;

import br.pessoal.numeration.NivelPermissao;

public class Classes {

	public static void main(String[] args) {
		
		for (NivelPermissao string : NivelPermissao.values()) {
			System.out.println(string);
		}
	}
}
