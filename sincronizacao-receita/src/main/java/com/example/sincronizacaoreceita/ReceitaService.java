package com.example.sincronizacaoreceita;

import org.springframework.stereotype.Service;

@Service
public interface ReceitaService {

    boolean atualizarConta(String agencia, String conta, double saldo, String status) throws RuntimeException, InterruptedException;
}
