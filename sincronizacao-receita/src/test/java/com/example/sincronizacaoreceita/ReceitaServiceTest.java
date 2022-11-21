package com.example.sincronizacaoreceita;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReceitaServiceTest {

    ReceitaService receitaService;

    @BeforeEach
    public void setUp() {
        receitaService = new ReceitaService();
    }

    @Test
    public void testAtualizarContaIfTrue() throws InterruptedException {
        InformacaoConta info = getInformacaoConta();

        Assertions.assertTrue(receitaService.atualizarConta(info.getAgencia(),
                info.getConta(), info.getSaldo(), info.getStatus()));
    }

    @Test
    public void testAtualizarContaIfAgenciaIsFalse() throws InterruptedException {
        InformacaoConta info = getInformacaoConta();
        info.setAgencia("00");

        Assertions.assertFalse(receitaService.atualizarConta(info.getAgencia(),
                info.getConta(), info.getSaldo(), info.getStatus()));
    }

    @Test
    public void testAtualizarContaIfContaIsFalse() throws InterruptedException {
        InformacaoConta info = getInformacaoConta();
        info.setConta("00");

        Assertions.assertFalse(receitaService.atualizarConta(info.getAgencia(),
                info.getConta(), info.getSaldo(), info.getStatus()));
    }

    private InformacaoConta getInformacaoConta() {
        InformacaoConta informacaoConta = new InformacaoConta();
        informacaoConta.setAgencia("0000");
        informacaoConta.setConta("000000");
        informacaoConta.setStatus("A");
        informacaoConta.setSaldo(10.0);
        informacaoConta.setResultado(true);
        return informacaoConta;
    }
}
