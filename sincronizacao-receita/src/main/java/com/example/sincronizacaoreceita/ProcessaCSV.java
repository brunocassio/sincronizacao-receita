package com.example.sincronizacaoreceita;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class ProcessaCSV {

    public static void exportarArquivoExcel(List<InformacaoConta> informacoesAtualizadas) throws IOException {
        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Resultado");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 4000);

        Row header = sheet.createRow(0);
        createHeaderCell(header, workbook);
        createCell(workbook, sheet, informacoesAtualizadas);

        String home = System.getProperty("user.home");
        File currDir = new File(home + "/Downloads/temp.xlsx");
        String path = currDir.getAbsolutePath();

        FileOutputStream outputStream = new FileOutputStream(path);
        workbook.write(outputStream);
        workbook.close();
    }

    private static void createCell(Workbook workbook, Sheet sheet, List<InformacaoConta> informacoesAtualizadas) {
        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        int count = 2;
       for (InformacaoConta info : informacoesAtualizadas) {

            Row row = sheet.createRow(count);
            Cell cell = row.createCell(0);
            cell.setCellValue(info.getAgencia());
            cell.setCellStyle(style);

            cell = row.createCell(1);
            cell.setCellValue(info.getConta());
            cell.setCellStyle(style);

            cell = row.createCell(2);
            cell.setCellValue(info.getSaldo());
            cell.setCellStyle(style);

            cell = row.createCell(3);
            cell.setCellValue(info.getStatus());
            cell.setCellStyle(style);

            cell = row.createCell(4);
            cell.setCellValue(info.isResultado());
            cell.setCellStyle(style);

            count++;
        }
    }

    private static void createHeaderCell(Row header, Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("agencia");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("conta");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("saldo");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("status");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("resultado");
        headerCell.setCellStyle(headerStyle);
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
