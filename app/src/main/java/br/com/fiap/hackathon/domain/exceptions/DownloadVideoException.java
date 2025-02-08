package br.com.fiap.hackathon.domain.exceptions;

public class DownloadVideoException extends RuntimeException{
    public DownloadVideoException(String mensagemErro){
        super(mensagemErro);
    }

}
