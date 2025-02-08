package br.com.fiap.hackathon.domain.exceptions;

public class ZipImagemException extends RuntimeException{
    public ZipImagemException(String mensagemErro){
        super(mensagemErro);
    }

}
