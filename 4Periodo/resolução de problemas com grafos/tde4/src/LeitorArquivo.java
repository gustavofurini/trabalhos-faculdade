import java.io.*;
import java.util.*;

public class LeitorArquivo extends Grafo {
    final Grafo grafo;
    final String arquivo, pastaDeEmails;

    public LeitorArquivo(String arquivo, String pastaDeEmails) {
        this.arquivo = arquivo;
        this.grafo = new Grafo();
        this.pastaDeEmails = pastaDeEmails;
        criarGrafo();
    }


    public Grafo getGrafo() {
        return grafo;
    }

    private void adicionarAdjacente(String usuarios, String linha){
        String[] emails = linha.split(", ");

        try {
            for(String email : emails){
                //CRIA UM NO PARA A STRING
                No<?> novoNo = new No<>(email);
                //RECEVE A ADJACENCIA REFERENTE
                No<?> noAdjacente = this.grafo.getNo(usuarios).getAdjacency(email);
                //SE JA POSSUI A ADJACENTE
                if(noAdjacente != null){
                    //AUMENTA O PESO
                    this.grafo.getNo(usuarios).setPeso(email, this.grafo.getNo(usuarios).getPeso(email) + 1);
                    continue;
                }
                //ADICIONA O PESO CASO SEJA O PRIMEIRO
                this.grafo.adicionarNo(novoNo);
                this.grafo.novaAdjacencia(usuarios, novoNo, 1);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void criarGrafo(){
        //ABRIR ARQUIVO
        File pasta = new File(this.arquivo);
        for(File usuariosPasta : Objects.requireNonNull(pasta.listFiles())){
            try{
                //PASSANDO PELAS PASTAS DOS USERS
                File userSentEmailspasta = new File(usuariosPasta.getPath() + "/" + this.pastaDeEmails);
                if(!userSentEmailspasta.isDirectory()){
                    continue;
                }
                //VERIFICANDO OS ARQUIVOS DENTRO DAS PASTAS DE EMAIL
                for(File userSentEmails : Objects.requireNonNull(userSentEmailspasta.listFiles())){
                    BufferedReader reader = new BufferedReader(new FileReader(userSentEmails));
                    String linha = reader.readLine();
                    //LAÇÕ PARA LER O AQRQUIVO CASO A LINHA NAO COMECE COM FROM
                    while(!linha.startsWith("From: ")){
                        linha = reader.readLine();
                    }
                    //CRIA O VERTICE PARA ADICIONAR O USUARIO
                    No<String> noUsuario = new No<>(linha.substring(6));
                    //ADICIONA O NO
                    this.grafo.adicionarNo(noUsuario);

                    //MESMA COISA QUE O FROM
                    linha = reader.readLine();
                    if(linha.startsWith("To:")){
                        linha = linha.substring(4);
                        //ADICIONA OS NOS COMO ADJACENTES
                        adicionarAdjacente(noUsuario.toString(), linha);
                        linha = reader.readLine();
                        while (linha.startsWith("\t")){
                            adicionarAdjacente(noUsuario.toString(), linha.substring(1));
                            linha = reader.readLine();
                        }
                    }
                }
            }catch (Exception e){

            }
        }
    }

    public List<?> maiorGrauDeEntrada(int limite){
        //GUARDA USERS E FREQUENCIA DE EMAILS RECEBIDOS
        HashMap<String, Integer> listaDeMaioresRem = new HashMap<>();
        for(No<?> n : this.grafo.getNos()){
            listaDeMaioresRem.put(n.toString(), 0);
        }
        for(No<?> n : this.grafo.getNos()){
            //VERIFICA ADJACENCIAS
            for(No<?> ad : n.getAdjacencies()){
                String adjacentLabel = ad.toString();
                //ADICIONAR +1 TODA VEZ QUE APARECE UMA ADJACENCIA
                listaDeMaioresRem.put(adjacentLabel, listaDeMaioresRem.get(adjacentLabel) + 1);
            }
        }

        String[] usuarios = listaDeMaioresRem.keySet().toArray(new String[0]);
        Integer[] valor = listaDeMaioresRem.values().toArray(new Integer[0]);

        heapSortReverso(usuarios, valor);

        No<?>[] nodes = new No<?>[usuarios.length];
        //ADICIONA OS USUARIOS
        for(int i = 0; i < usuarios.length; i ++){
            nodes[i] = this.grafo.getNo(usuarios[i]);
        }

        List<List<?>> listaUsuarios = new ArrayList<>();

        listaUsuarios.add(Arrays.asList(nodes).subList(0, limite));
        listaUsuarios.add(Arrays.asList(valor).subList(0, limite));
        return listaUsuarios;
    }
    // MESMA LOGICA MAS COM OS VALORES INVERSON
    public List<?> maiorGrauDeSaida(int limite){
        List<List<?>> listaDeMaisEnv = new ArrayList<>();
        try {
            No<?>[] remetente = this.grafo.getNos();
            Integer[] mensagens = new Integer[remetente.length];
            for (int i = 0; i < remetente.length; i++) {
                mensagens[i] = remetente[i].getAdjacencies().length;
            }
            heapSortReverso(remetente, mensagens);
            listaDeMaisEnv.add(Arrays.asList(remetente).subList(0, limite));
            listaDeMaisEnv.add(Arrays.asList(mensagens).subList(0, limite));
        }catch (Exception e){

        }
        return listaDeMaisEnv;
    }
    //HEAP SORT PARA ORDENAR DE FORMA INVERSA OS VERTICES COM MENORES PESOS
    private static void heapSortReverso(Object[] nos, Integer[] peso) {
        int x = peso.length;
        for (int y = x / 2 - 1; y >= 0; y--) heapSort(nos, peso, x, y);
        for (int y = x - 1; y > 0; y--) {
            int temp = peso[0];
            Object tempNode = nos[0];
            peso[0] = peso[y];
            nos[0] = nos[y];
            peso[y] = temp;
            nos[y] = tempNode;
            heapSort(nos, peso, y, 0);
        }
    }
    //HEAP SORT PARA ORDENAR OS VERTICES
    private static void heapSort(Object[] nos, Integer[] pesos, int x, int y) {
        int maisCurto = y;
        int esquerda = 2 * y + 1;
        int direita = 2 * y + 2;
        if (esquerda < x && pesos[esquerda] < pesos[maisCurto]) maisCurto = esquerda;
        if (direita < x && pesos[direita] < pesos[maisCurto]) maisCurto = direita;
        if (maisCurto != y) {
            int swap = pesos[y];
            Object troca = nos[y];
            pesos[y] = pesos[maisCurto];
            nos[y] = nos[maisCurto];
            pesos[maisCurto] = swap;
            nos[maisCurto] = troca;
            heapSort(nos, pesos, x, maisCurto);
        }
    }
    //USUARIO ATE O OUTRO ATRAVES DE PROFUNDIADE
    public List<?> alcancaAtravesProfundidade(Object inicio, Object fim){
        //PEGA O INICIAL
        BuscaProfundidade buscaProfundidade = new BuscaProfundidade(this.grafo.getNo(inicio));
        //LISTA DO CAMINHO
        ArrayList<No<?>> procuraCaminho = new ArrayList<>();
        //VERIFICA ATRAVES DA PROFUNDIDADE
        while(buscaProfundidade.verificarNos()){ //
            procuraCaminho.add(buscaProfundidade.proximo());
            //ENCONTRA O DESTINO DIMINUI -1 POISN NAO CONTA O FIM
            if(procuraCaminho.get(procuraCaminho.size() - 1).toString().equals(fim.toString())){
                return procuraCaminho;
            }
        }
        return null;
    }
    //USUARIO ATE O OUTRO ATRAVES DA LAREGURA
    public List<?> alcanlcaAtravesLargura(Object inicio, Object fim){
        //PEGA OO NO INICIAL
        BuscaLargura buscaLargura = new BuscaLargura(this.grafo.getNo(inicio));
        //LISTA DO CAMINHO
        ArrayList<No<?>> procuraCaminho;
        procuraCaminho = new ArrayList<>();
        //VERIFICANDO NOS ATRAVES DA LARGURA
        while(buscaLargura.verificarNos()){
            procuraCaminho.add(buscaLargura.proximo());
            //ENCONTRA O DESTINO DIMINUI -1 POISN NAO CONTA O FIM
            if(procuraCaminho.get(procuraCaminho.size() - 1).toString().equals(fim.toString())){
                return procuraCaminho;
            }
        }

        return null;
    }
}