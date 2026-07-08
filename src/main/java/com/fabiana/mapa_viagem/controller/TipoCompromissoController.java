package com.fabiana.mapa_viagem.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fabiana.mapa_viagem.enums.TipoCompromisso;

@RestController
@RequestMapping("/tipocompromisso")
public class TipoCompromissoController {

	@GetMapping
    public List<String> listarTipoCompromisso() {
        return Arrays.stream(TipoCompromisso.values())
                .map(Enum::name)
                .toList();
    }

}
