import java.util.LinkedHashMap;

public class No<TIPO> {

    private static class Adjacencia {
        No<?> no;
        Integer peso;

        public Adjacencia(No<?> no, Integer peso) {
            this.no = no;
            this.peso = peso;
        }

        public No<?> getNo() {
            return no;
        }

        public Integer getPeso() {
            return peso;
        }

        public void setNo(No<?> no) {
            this.no = no;
        }

        public void setPeso(Integer weight) {
            this.peso = weight;
        }
    }

    private TIPO identificador;
    private final LinkedHashMap<Object, Adjacencia> adjacencies;

    public No(TIPO identificador) {
        this.identificador = identificador;
        this.adjacencies = new LinkedHashMap<>();
    }


    public Integer getPeso(Object noAdjacente) {
        // pega o peso de uma conexao
        return adjacencies.get(noAdjacente.toString()).getPeso();
    }

    

    protected void newAdjacency(No<?> no, int weight) { // adiciona no hashmap de adjacencias
        // a chave e o rotulo, e cria um Adjacencia pra guarda o peso e o valor
        this.adjacencies.put(no.toString(), new Adjacencia(no, weight));
    }

    public No<?> getAdjacency(String key) { // retorna a adjacencia se a chave existir
        Adjacencia adjacent = this.adjacencies.get(key);
        if (adjacent != null) {
            return adjacent.getNo();
        }
        return null;
    }

    public No<?>[] getAdjacencies() { // retorna um array com os nodes adjacentes, tudo isso pra pega do hashmap
        Adjacencia[] Adjacencias = new Adjacencia[0];
        Adjacencias = this.adjacencies.values().toArray(Adjacencias);
        No<?>[] nodesList = new No<?>[Adjacencias.length];
        for (int i = 0; i < Adjacencias.length; i++) {
            nodesList[i] = Adjacencias[i].getNo();
        }
        return nodesList;
    }



    public void setPeso(Object adjacentNode, int weights) {
        this.adjacencies.get(adjacentNode.toString()).setPeso(weights);
    }

    //TO STRINGS
    @Override
    public String toString() {
        return this.identificador.toString();
    }



}