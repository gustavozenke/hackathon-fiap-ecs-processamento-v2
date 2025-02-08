package br.com.fiap.hackathon.domain.exceptions;

public class GravarEventoProcessamentoException extends RuntimeException{
    public GravarEventoProcessamentoException(String mensagemErro){
        super(mensagemErro);
    }

}
