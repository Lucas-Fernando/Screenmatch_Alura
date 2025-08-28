package br.com.alura.Screenmatch.principal;

import br.com.alura.Screenmatch.model.DadosEpisodio;
import br.com.alura.Screenmatch.model.DadosSerie;
import br.com.alura.Screenmatch.model.DadosTemporada;
import br.com.alura.Screenmatch.model.Episodio;
import br.com.alura.Screenmatch.service.ConsumoApi;
import br.com.alura.Screenmatch.service.ConverteDados;


import java.util.*;
import java.util.stream.Collectors;


public class Principalbkp {
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO ="https://www.omdbapi.com/?t=";
    private final String APIKEY = "&apikey=efb33e6e";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibirMenu(){

      System.out.println("Digite o nome da série");
       String nomeSerie = leitura.nextLine();
      //  String nomeSerie = "game of thrones";
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+APIKEY);
//      var json = consumo.obterDados("http://www.omdbapi.com/?apikey=efb33e6e&t=the_flash");
        System.out.println(json);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i =1; i<dados.totalTemporadas(); i++){
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+")+ "&season="+i+APIKEY);
            DadosTemporada dadosTemporada =conversor.obterDados(json, DadosTemporada.class);
            System.out.println(dadosTemporada);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t->t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

//        System.out.println("\nTop 10 episódios");
//        dadosEpisodios.stream()
//                .filter(e->!e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(10)
//                .map(e -> e.titulo().toUpperCase())
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        //System.out.println("Digite o episódio buscado");

        String trechoTitulo = "gift";


       episodios.forEach(System.out::println);
       Optional<Episodio> episodioBuscado = episodios.stream().filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
               .findFirst();

       if(episodioBuscado.isPresent()){
           System.out.println("Episódio encontrado: "+episodioBuscado.get().getTitulo());
           System.out.println("Temporada: " + episodioBuscado.get().getTemporada());

       }else{
           System.out.println("episódio não encontrada.");
       }

       //System.out.println("A partir de que ano você deseja ver os episódios? ");
     //  var ano = leitura.nextInt();
       // System.out.println(" ");
//       var ano = 2020;
//        LocalDate dataBusca = LocalDate.of(ano,1 ,1 );
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println("Temporada: " + e.getTemporada() +
//                        " Episódio: " + e.getTitulo() +
//                        " Data Lançamento " + e.getDataLancamento().format(dtf)));
        Map<Integer, Double> avaliacoesTemporada = episodios.stream()
                .filter(e-> e.getAvaliacao()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao()>0.0).collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("média: " + est.getAverage());
        System.out.println("Melhor Episodio: "+ est.getMax());
        System.out.println("Pior Episodio: "+ est.getMin());
        System.out.println("Quantidade: " + est.getCount());




  }


}
