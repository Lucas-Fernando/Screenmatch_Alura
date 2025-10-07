//package br.com.alura.Screenmatch;
//
//import br.com.alura.Screenmatch.principal.Principal;
//import br.com.alura.Screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreenmatchApplicationSemWeb implements CommandLineRunner {
//
//	@Autowired
//	SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenmatchApplicationSemWeb.class, args);
//	}
//
//
//	@Override
//	public void run(String... args) throws Exception {
//		Principal principal = new Principal(repository);
//		principal.exibeMenu();
//
//
//
//////			var consumoAPI = new ConsumoApi();
//////			var json = consumoAPI.obterDados("http://www.omdbapi.com/?apikey=efb33e6e&t=the_flash");
//////			System.out.println(json);
//////			ConverteDados conversor = new ConverteDados();
//////			DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
//////			System.out.println(dados);
//////
//////			json = consumoAPI.obterDados("https://www.omdbapi.com/?apikey=efb33e6e&t=the_flash&season=3&episode=2");
//////			DadosEpisodio dadosEpisodio = conversor.obterDados(json, DadosEpisodio.class);
//////		    System.out.println(dadosEpisodio);
//
//
//
//	}
//}
