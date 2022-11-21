package com.example.sincronizacaoreceita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SincronizacaoReceitaApplication {


	public static void main(String[] args) throws IOException, InterruptedException {
		SpringApplication.run(SincronizacaoReceitaApplication.class, args);
		Map<Integer, List<String>> dadosProcessados = ProcessaCSV.processarArquivo(args[0]);

		List<InformacaoConta> informacoesAtualizadas = enviarAtualizacaoAReceita(dadosProcessados);

		ProcessaCSV.exportarArquivoExcel(informacoesAtualizadas);
	}

	public static List<InformacaoConta> enviarAtualizacaoAReceita(Map<Integer, List<String>> dadosProcessados) throws InterruptedException {
		List<InformacaoConta> informacoesAtualizadas = new ArrayList<>();
		for (int i = 1; i < dadosProcessados.size(); i++) {
			InformacaoConta informacaoConta = ProcessaCSV.convertToObject(dadosProcessados.get(i));

			ReceitaService receitaService = new ReceitaService();
			boolean resultado = receitaService.atualizarConta(informacaoConta.getAgencia(),
					informacaoConta.getConta(), informacaoConta.getSaldo(), informacaoConta.getStatus());
			informacaoConta.setResultado(resultado);
			informacoesAtualizadas.add(informacaoConta);
		}
		return informacoesAtualizadas;
	}

}
