package br.com.fiap.hackathon.domain.exceptions;

public class UploadZipFramesException extends RuntimeException{
    public UploadZipFramesException(String mensagemErro){
        super(mensagemErro);
    }

}
