package com.example.sincronizacaoreceita;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SincronizacaoReceitaApplication {

	@Autowired
	private static ReceitaService receitaService;

	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(SincronizacaoReceitaApplication.class, args);
		Map<Integer, List<String>> dadosProcessados = ProcessaCSV.processarArquivo(args[0]);

		enviarAtualizacao(dadosProcessados);


	}

	public static void enviarAtualizacao(Map<Integer, List<String>> dadosProcessados) throws InterruptedException {
		for (int i = 1; i < dadosProcessados.size(); i++) {
			InformacaoConta informacaoConta = ProcessaCSV.convertToObject(dadosProcessados.get(i));
			boolean resultado = receitaService.atualizarConta(informacaoConta.getAgencia(),
					informacaoConta.getConta(), informacaoConta.getSaldo(), informacaoConta.getStatus());
			informacaoConta.setResultado(resultado);
		}
	}

}
