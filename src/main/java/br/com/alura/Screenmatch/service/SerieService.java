package br.com.alura.Screenmatch.service;

import br.com.alura.Screenmatch.DTO.EpisodioDTO;
import br.com.alura.Screenmatch.DTO.SerieDTO;
import br.com.alura.Screenmatch.model.Episodio;
import br.com.alura.Screenmatch.model.Serie;
import br.com.alura.Screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obterTodasAsSeries(){
        return ConverteDados(repository.findAll());
    }


    public List<SerieDTO> obterTop5Series() {
    return ConverteDados(repository.findTop5ByOrderByAvaliacaoDesc());

    }
    private List<SerieDTO> ConverteDados(List<Serie> series){
       return series.stream()
                .map(s -> new SerieDTO(s.getId(),s.getTitulo(),s.getTotalTemporadas(),s.getAvaliacao(),
                        s.getGenero(), s.getAtores(), s.getPoster(), s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterLancamento() {
        return ConverteDados(repository.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
      Optional<Serie> serieOptional = repository.findById(id);
      if (serieOptional.isPresent()){
          Serie serie = serieOptional.get();
          return new SerieDTO(serie.getId(),serie.getTitulo(),serie.getTotalTemporadas(),serie.getAvaliacao(),
                  serie.getGenero(), serie.getAtores(), serie.getPoster(), serie.getSinopse());
      }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serieOptional = repository.findById(id);
        if (serieOptional.isPresent()){
            Serie serie = serieOptional.get();
            return ConverteEpisodios(serie.getEpisodios());
        }
        return null;
    }

    public List<EpisodioDTO> ObterTemporadasPorNumero(Long id, Long temporada) {
        return ConverteEpisodios(repository.obterEpisodiosPorTemporada(id, temporada));
    }

    private List<EpisodioDTO> ConverteEpisodios(List<Episodio> episodios){
        return (episodios.stream()
                .map(episodio ->
                        new EpisodioDTO(episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo()))
                .collect(Collectors.toList()));
    }
}
