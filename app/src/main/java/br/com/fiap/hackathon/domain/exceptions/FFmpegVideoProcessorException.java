package br.com.fiap.hackathon.domain.exceptions;

public class FFmpegVideoProcessorException extends RuntimeException{
    public FFmpegVideoProcessorException(String mensagemErro){
        super(mensagemErro);
    }

}