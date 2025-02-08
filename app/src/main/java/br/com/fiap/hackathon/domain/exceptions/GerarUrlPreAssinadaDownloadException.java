package br.com.fiap.hackathon.domain.exceptions;

public class GerarUrlPreAssinadaDownloadException extends RuntimeException{
    public GerarUrlPreAssinadaDownloadException(String mensagemErro){
        super(mensagemErro);
    }

}
