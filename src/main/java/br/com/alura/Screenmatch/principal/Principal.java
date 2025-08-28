package br.com.alura.Screenmatch.principal;

import br.com.alura.Screenmatch.model.*;
import br.com.alura.Screenmatch.repository.SerieRepository;
import br.com.alura.Screenmatch.service.ConsumoApi;
import br.com.alura.Screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=efb33e6e";
    List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>() ;

    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
    this.repository = repository;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4- Buscar Serie por Titulo
                    5- Busca serie por ator
                    6- Buscar top 5 melhores series
                    7- Buscar por categoria
                    8- buscar por numero de temporadas e avaliação
                    9-Buscar episódio por trecho
                    1- Buscar dos 5 melhores episódios
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSerieBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarPorCategoria();
                    break;
                case 8:
                    buscarPortemporadaseAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodioPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do Ator/Atriz");
        var nomeAtor = leitura.nextLine();
        System.out.printf("Avalicao apartir de qual valor");
        var avalicao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avalicao );
        System.out.println("Serie em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(serie -> System.out.println(serie.getTitulo() + " avaliação "+ serie.getAvaliacao() ));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBusca= repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série: "+ serieBusca.get());
        }else {
            System.out.println("Série não encontrada!");

        }
    }

    private void listarSerieBuscadas() {

        series = repository.findAll();
        series.stream().
                sorted(Comparator.comparing(Serie::getGenero)).
                forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){
        listarSerieBuscadas();
        System.out.println("Escolha uma serie pelo nome");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serie = series.stream()
                .filter(s-> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase())).findFirst();
        if (serie.isPresent()) {
            var serieEncontrada =serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
           List<Episodio> episodios = temporadas.stream().flatMap(d->d.episodios().stream().
                    map(e->new Episodio(d.numero(), e))).toList();
           serieEncontrada.setEpisodios(episodios);
           repository.save(serieEncontrada);
        }else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repository.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(serie -> System.out.println(serie.getTitulo() + " avaliação "+ serie.getAvaliacao() ));
    }
    private void buscarPorCategoria(){
        System.out.println("Digite a categoria para busca");
        String categoriaBuscada = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(categoriaBuscada);
        List<Serie> seriesCategoria = repository.findByGenero(categoria);
        System.out.printf("as series da categoria %s são: ", categoria);
        seriesCategoria.forEach(System.out::println);

    }

    private void buscarPortemporadaseAvaliacao(){
        System.out.println("Digite o numero Maximo de Temporadas");
        int temporadas = leitura.nextInt();
        System.out.println("Digite a avalição minima");
        double avaliacao = leitura.nextDouble();
        List<Serie> seriesTA= repository.listarPorTemporadaEAvaliacao(temporadas,avaliacao);
        seriesTA.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));

    }

    private void buscarEpisodioPorTrecho(){
        System.out.println( "Qual o nome do episódio para busca?");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodioPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e->
                System.out.printf("Série %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()));
    }
    private void topEpisodioPorSerie(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie= serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e->
                    System.out.printf("Série %s Temporada %s - Episódio %s - %s Avaliação: %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }
    private void buscarEpisodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie=serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = leitura.nextInt();
            List<Episodio> episodiosAno = repository.EpisodioPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

}