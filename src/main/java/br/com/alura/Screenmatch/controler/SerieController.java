package br.com.alura.Screenmatch.controler;

import br.com.alura.Screenmatch.DTO.EpisodioDTO;
import br.com.alura.Screenmatch.DTO.SerieDTO;
import br.com.alura.Screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService service;

    @GetMapping
    public List<SerieDTO> obterSeries(){
        return service.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> ObterTop5Series(){
        return  service.obterTop5Series();
    }
    @GetMapping("/lancamentos")
    public List<SerieDTO> Obterlancamentos(){
        return service.obterLancamento();
    }
    @GetMapping("/{id}")
    public SerieDTO obterPorId(@PathVariable Long id){
        return service.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return service.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long temporada){
        return service.ObterTemporadasPorNumero(id, temporada);
    }
}
