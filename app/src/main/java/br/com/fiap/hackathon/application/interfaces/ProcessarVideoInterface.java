package br.com.fiap.hackathon.application.interfaces;

import br.com.fiap.hackathon.domain.entities.EventoVideo;

public interface ProcessarVideoInterface {
    void execute(EventoVideo event);
}
