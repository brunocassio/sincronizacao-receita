package com.example.sincronizacaoreceita;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProcessaCSV {

    @Autowired
    private ReceitaService receitaService;

    public void enviarAtualizacao(Map<Integer, List<String>> dadosProcessados) throws InterruptedException {
        for (int i = 1; i < dadosProcessados.size(); i++) {
            InformacaoConta informacaoConta = convertToObject(dadosProcessados.get(i));
            boolean resultado = receitaService.atualizarConta(informacaoConta.getAgencia(),
                    informacaoConta.getConta(), informacaoConta.getSaldo(), informacaoConta.getStatus());
            informacaoConta.setResultado(resultado);
        }
    }

    public static InformacaoConta convertToObject(List<String> dados) {
        InformacaoConta informacaoConta = new InformacaoConta();
        informacaoConta.setAgencia(dados.get(0));
        informacaoConta.setConta(dados.get(1));
        informacaoConta.setSaldo(Double.parseDouble(dados.get(2)));
        informacaoConta.setStatus(dados.get(3));

        return informacaoConta;
    }

    public static Map<Integer, List<String>> processarArquivo(String fileLocation) throws IOException {

        FileInputStream file = new FileInputStream(new File(fileLocation));
        Workbook workbook = new XSSFWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING:
                        data.get(i).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(cell.getDateCellValue() + "");
                        } else {
                            data.get(i).add(cell.getNumericCellValue() + "");
                        }
                        break;
                    default: data.get(i).add(" ");
                }
            }
            i++;
        }
        return data;
    }
}
