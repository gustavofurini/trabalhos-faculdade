import java.util.*;

public class Grafo {
    private final LinkedHashMap<String, No<?>> nos;

    public Grafo(){
        this.nos = new LinkedHashMap<>();
    }


    //ADICIONAR NO NO GRAFO
    public boolean adicionarNo(No<?> no){
        if(this.nos.get(no.toString()) == null){
            this.nos.put(no.toString(), no);
            return true;
        }
        return false;
    }

    public No<?> getNo(Object no){
        return this.nos.get(no.toString());
    }
    //VERTICES
    public int vertices(){return this.nos.size();}
    //ARESTAS
    public int arestas(){
        int aresta = 0;
        for(No<?> n : this.getNos()){
            aresta += n.getAdjacencies().length;
        }
        return aresta;
    }
    //ADICIONAR ADJACENCIA
    public boolean novaAdjacencia(Object no1, Object no2, int peso){
        if(this.nos.get(no2.toString()) == null){
            return false;
        }
        this.nos.get(no1.toString()).newAdjacency(this.nos.get(no2.toString()), peso);
        return true;
    }


    public void setNo(No<?> no){
        this.nos.put(no.toString(), no);
    }

    public No<?>[] getNos() {
        No<?>[] nos = new No<?>[0];
        nos = this.nos.values().toArray(nos);
        return nos;
    }

    //FUNCAO DJIKSTRA
    public List<Object> caminhoMaisCurto(Object inicioKey, Object fim){
        return this.caminhos(this.getNo(inicioKey), this.getNo(fim), true);
    }
    //FUNCAO DJIKSTRA INVERSA
    public List<Object> caminhoMaisLongo(Object inicio, Object fim){ 
        return this.caminhos(this.getNo(inicio), this.getNo(fim), false);
    }


    private List<Object> caminhos(No<?> inicio, No<?> destino, boolean maisCurto){
        if(!this.busca(inicio.toString(), destino.toString())){
            return new ArrayList<>();
        }
        
        Map<String, Double> distancia = new HashMap<>();
        Map<String, Integer> distanciaAtual = new HashMap<>();
        distancia.put(inicio.toString(), 0.0);
        distanciaAtual.put(inicio.toString(), 0);

        Set<No<?>> nosVisitados = new HashSet<>();
        List<No<?>> nosPercorrer = new ArrayList<>();
        nosPercorrer.add(inicio);

        Map<String, No<?>> noAnterior = new HashMap<>();
        //LAÇO PARA VERIFICAR TODOS OS NOS QUE PRECISAM SER VERIFICADOS
        while(!nosPercorrer.isEmpty()){
            //PEGA O NO DE MENOR DINSTANCIA
            No<?> atual = nosNaoVisitados(nosPercorrer, distancia);
            //REFERENTE AS ADJACENCIAS DOS NOS
            No<?>[] adjacencias = atual.getAdjacencies();
            //VERIFICA AS ADJACENCIAS
            for(No<?> n : adjacencias){
                if(!nosVisitados.contains(n)){
                    //VERIFICA A DISTANCIA DO NO QUE ESTA SENDO VERIFICADO
                    Double atualsdistancia = distancia.get(atual.toString());
                    //SOMA PESO COM A ADJACENCIA, VERIFICA SE O VALOR E NULO SE NAO FAZ O DJIKISTRA "AO CONTRARIO"
                    double novaDistancia = Math.pow((atual.getPeso(n) + (atualsdistancia == null ? 0 : atualsdistancia)), (maisCurto ? 1 : -1));
                    //PEGAR A MENOR DISTANCIA CASO AINDA NAO ESTEJA DEFINIDO
                    if(!distancia.containsKey(n.toString()) || distancia.get(n.toString()) > novaDistancia){
                        //TROCA O NO E A DISTANCIA
                        distancia.put(n.toString(), novaDistancia);
                        noAnterior.put(n.toString(), atual);
                    }
                    //vVERIFICA SE O NO FOI VISITADO OU SE ELE E O DESTINO
                    if(!nosPercorrer.contains(n) && !n.equals(destino)){
                        nosPercorrer.add(n);
                    }
                }
            }
            //ATUALIZA
            nosVisitados.add(atual);
        }
        //REPASSA O CAMINHO E VOLTA
        List<Object> caminhoMaisCurto = new ArrayList<>();
        ArrayList<No<?>> caminho = new ArrayList<>();
        caminho.add(destino);
        No<?> atual = noAnterior.get(destino.toString());
        while(!atual.equals(inicio)){
            caminho.add(atual);
            atual = noAnterior.get(atual.toString());
        }
        caminho.add(inicio);
        caminhoMaisCurto.add(caminho);
        caminhoMaisCurto.add(maisCurto ? distancia.get(destino.toString()) : Math.pow(distancia.get(destino.toString()), -1));
        Collections.reverse(caminho);
        return caminhoMaisCurto;
    }

    private No<?> nosNaoVisitados(List<No<?>> nosPercorrer, Map<String, Double> distancia){
        int primeiroNo = 0;
        Double menorValor = Double.MAX_VALUE;
        for(int i = 0; i < nosPercorrer.size(); i++){
            if(!distancia.containsKey(nosPercorrer.get(i).toString()) || menorValor > distancia.get(nosPercorrer.get(i).toString())){
                menorValor = distancia.get(nosPercorrer.get(i).toString());
                primeiroNo = i;
            }
        }
        return nosPercorrer.remove(primeiroNo);
    }
    //BUSCA
    //PERCORRE O GRAFO ATE NÃO HAVER MAIS VERTICES OU SER AONDE QUER CHEGAR
    public boolean busca(Object inicio, Object fim){
        BuscaProfundidade buscaProfundidade = new BuscaProfundidade(this.getNo(inicio));
        while(buscaProfundidade.verificarNos()){
            if(buscaProfundidade.proximo().toString().equals(fim.toString())){
                return true;
            }
        }
        return false;
    }




    public abstract class Busca {

        LinkedList<No<?>> nosPercorrer = new LinkedList<>();
        Set<No<?>> visited = new HashSet<>();

        public Busca(No<?> inicio){
            this.nosPercorrer.add(inicio);
        }

        public abstract No<?> proximo();
        public No<?> percorrerNos(){
            return nosPercorrer.getFirst();
        }
        public boolean verificarNos(){
            return !nosPercorrer.isEmpty();
        }
        abstract void adicionarLista(No<?> atualNextNode);
    }
    //BUSCA EM PROFUNDIDADE
    public class BuscaProfundidade extends Busca {
        //NAO CONTA COM O O INICO
        private int noInicial = -1;
        private int proximoNo = 0;
        private int noAnterior = 0;

        public BuscaProfundidade(No<?> inicio) {
            super(inicio);
        }

        @Override
        //PERCORRE O GRAFO E ADICIONA O PROXIMO NA LISTA
        public No<?> proximo() {
            if(!this.verificarNos()){
                return null;
            }
            atualizarNo();
            No<?> nextNode = nosPercorrer.getFirst();
            nosPercorrer.removeFirst();
            visited.add(nextNode);
            this.adicionarLista(nextNode);
            return nextNode;
        }

        public int proximoNo(){
            if(this.noAnterior <= 0){
                return noInicial + 1;
            }else{
                return noInicial;
            }
        }

        private void atualizarNo(){
            if(this.noAnterior <= 0){
                noInicial++;
                this.noAnterior = this.proximoNo;
                this.proximoNo = -1;
            }else{
                this.noAnterior--;
            }
        }

        @Override
        //ADICIONA TODOS OS NOS NA LISTA
        public void adicionarLista(No<?> atualProximoNo) {
            No<?>[] adjacents = atualProximoNo.getAdjacencies();
            for(No<?> n : adjacents){
                if(!visited.contains(n) && !nosPercorrer.contains(n)){
                    nosPercorrer.add(n);
                    this.proximoNo++;
                }
            }
        }
    }
//BUSCA EM LARGURA
    public class BuscaLargura extends Busca {

        public BuscaLargura(No<?> inicio) {
            super(inicio);
        }

        @Override
        public No<?> proximo() {
            if(!this.verificarNos()){
                return null;
            }
            No<?> proximoNo = this.nosPercorrer.getLast();
            this.nosPercorrer.removeLast();
            visited.add(proximoNo);
            this.adicionarLista(proximoNo);
            return proximoNo;
        }

        @Override
        void adicionarLista(No<?> atualNextNode) {
            No<?>[] adjacencias = atualNextNode.getAdjacencies();
            for(No<?> n : adjacencias){
                if(!visited.contains(n)){
                    nosPercorrer.removeFirstOccurrence(n);
                    nosPercorrer.add(n);
                }
            }
        }
    }

    public List<?> nosAdjacentes(Object inicio, int distancia){
        return this.nosAdjacentes(this.getNo(inicio.toString()), distancia);
    }
    public List<?> nosAdjacentes(No<?> inicio, int distancia){
        BuscaProfundidade buscaProfundidade = new BuscaProfundidade(inicio);
        List<No<?>> nos = new ArrayList<>();
        //PERCORRE O GRAFO ATE A PROFUNDIDADE SER REFERENTE A DISTANCIA
        while(buscaProfundidade.verificarNos() && !(buscaProfundidade.proximoNo() == distancia)){
            buscaProfundidade.proximo();
        }
        //PERCORRE O NO ATE A DISTANCIA INFORMADA ANTERIORMENTE
        while (buscaProfundidade.verificarNos() && buscaProfundidade.proximoNo() == distancia){
            // ADICIONA TODOS OS NOS CUJO A ESTEJA NA DISTANCIA
            nos.add(buscaProfundidade.proximo());
        }
        return nos;
    }

    @Override
    public String toString() {
        return Arrays.toString(this.nos.values().toArray());
    }

}