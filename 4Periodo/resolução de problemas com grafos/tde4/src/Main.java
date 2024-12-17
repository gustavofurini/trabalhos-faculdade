
import java.util.List;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {




        Scanner scanner = new Scanner(System.in);
        while(true){
            LeitorArquivo lerEmails = new LeitorArquivo("Amostra Enron/Amostra Enron", "sent");
            System.out.println("[1]NUMERO DE VERTICES");
            System.out.println("[2]NUMERO DE ARESTAS");
            System.out.println("[3]USUARIOS QUE MAIS ENVIARAM");
            System.out.println("[4]USUARIOS QUE MAIS RECEBERAM");
            System.out.println("[5]CAMINHO MAIS CURTO");
            System.out.println("[6]CAMINHO MAIS LONGO");
            System.out.println("[7]BUSCA EM PROFUNDIDADE");
            System.out.println("[8]BUSCA EM LARGURA");
            System.out.println("[9]NOS ADJACENTES");
            System.out.println("[10]SAIR");
            System.out.println("ESCOLHA UMA OPÇÃO:");
            int opcao = scanner.nextInt();
            if(opcao == 1){
                System.out.println("Vertices: " + lerEmails.getGrafo().vertices());
            }
            else if(opcao == 2){
                System.out.println("Arestas: " + lerEmails.getGrafo().arestas());
            }
            else if(opcao == 3){
                List<?> maisEnviaram = lerEmails.maiorGrauDeSaida(20);
                System.out.println("Lista de usuários que mais enviarm: " + maisEnviaram);
            }
            else if(opcao == 4){
                List<?> maisReceberam = lerEmails.maiorGrauDeEntrada(20);
                System.out.println("Lista de usuarios que mais receberam: " + maisReceberam);
            }
            else if(opcao == 5){
                System.out.println("Caminho mais curto entre" + "drew.fossum@enron.com"+ " e " + "jalexander@gibbs-bruns.com"+ "\nCaminho:" + lerEmails.getGrafo().caminhoMaisCurto("drew.fossum@enron.com","jalexander@gibbs-bruns.com"));

            }
            else if(opcao == 6){
                System.out.println("Caminho mais longo entre" + "drew.fossum@enron.com"+ " e " + "jalexander@gibbs-bruns.com" + "\nCaminho: " + lerEmails.getGrafo().caminhoMaisLongo("drew.fossum@enron.com", "jalexander@gibbs-bruns.com"));

            }
            else if(opcao == 7){
                System.out.println("Busca em profundidade: " + "drew.fossum@enron.com" + " ----> " + "rockey.storie@enron.com" + "\nNos percorridos: " + lerEmails.alcancaAtravesProfundidade("drew.fossum@enron.com", "rockey.storie@enron.com"));

            }
            else if(opcao == 8){
                System.out.println("Busca em largura: " + "drew.fossum@enron.com"+ " ----> " + "rockey.storie@enron.com" + ": " + lerEmails.alcanlcaAtravesLargura("drew.fossum@enron.com", "rockey.storie@enron.com"));

            }
            else if(opcao == 9){
                System.out.println("Nos com distancia: " + 1 + " \nA partir do user: " + "rockey.storie@enron.com" + "\n"+ "Lista de nos:" + lerEmails.getGrafo().nosAdjacentes("drew.fossum@enron.com", 1));

            }
            else if(opcao == 10){
                System.exit(0);
            }

        }

    }
}