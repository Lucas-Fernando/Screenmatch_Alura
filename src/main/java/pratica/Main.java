package pratica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    //exercicios desafio pratica lambda 0.3
    //1- Dada a lista de números inteiros abaixo, filtre apenas os números pares e imprima-os.
    //public static void main(String[] args) {
//        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);
//        List<Integer> pares = numeros.stream().filter(n-> (n%2==0)).collect(Collectors.toList());
//        System.out.println(pares);
//
    //}

    //2-  Dada a lista de strings abaixo, converta todas para letras maiúsculas e imprima-as.
//    public static void main(String[] args) {
//        List<String> palavras = Arrays.asList("java", "stream", "lambda");
//        List<String> maiusculas = palavras.stream()
//                .map(p -> p.toUpperCase()).collect(Collectors.toList());
//        System.out.println(maiusculas);
//    }

    //3 - Dada a lista de números inteiros abaixo,
    // filtre os números ímpares, multiplique cada um por 2 e colete os resultados em uma nova lista.

//        public static void main(String[] args) {
//            List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);
//            List<Integer> impares = numeros.stream().filter(n-> (n%2==1)).
//                    map(n -> n*2).collect(Collectors.toList());
//            System.out.println(impares);
//    }


//Imagine que você tem uma lista de strings. Algumas das strings são números, mas outras não. Queremos converter
// a lista de strings para uma lista de números. Se a conversão falhar, você deve ignorar o valor. Por exemplo,
// na lista a seguir, a saída deve ser [10, 20]:

//    public static void main(String[] args) {
//        List<String> input = Arrays.asList("10", "abc", "20", "30x");
//        List<Integer> numero = input.stream() .map(str -> { try { return Optional.of(Integer.parseInt(str)); }
//        catch (NumberFormatException e)
//        { return Optional.<Integer>empty(); } })
//                .filter(Optional::isPresent) .map(Optional::get) .toList();
//        System.out.println(numero);
//    }

    //Implemente um método que recebe um número inteiro dentro de um Optional. Se o número estiver presente e for
    // positivo, calcule seu quadrado. Caso contrário, retorne Optional.empty.
    public static void main(String[] args) {
        System.out.println(processaNumero(Optional.of(5))); // Saída: Optional[25]
        System.out.println(processaNumero(Optional.of(-3))); // Saída: Optional.empty
        System.out.println(processaNumero(Optional.empty())); // Saída: Optional.empty


    }
    public static Optional<Integer> processaNumero(Optional<Integer> numero) {
            return numero.filter(n -> n > 0).map(n -> n * n);
    }




}

