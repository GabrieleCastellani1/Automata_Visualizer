package graphs.disjointedSets;

public class DisjointSet<K> {
    public K key;
    public DisjointSet<K> rep;
    public DisjointSet<K> next;
    public DisjointSet<K> last;

    private DisjointSet() {

    }

    public static DisjointSet Make(Object x) {// crea una nuova lista con un solo elemento "nodo" al suo interno, settandone tutti i relativi campi
        DisjointSet d = new DisjointSet();// questo è un nodo della lista, ogni "nodo" ha i campi:
        d.key = x;                          // key che contiene un interno memorizzato nel "nodo" della lista
        d.rep = d;                          // rep al rappresentante della lista, il primo "nodo"
        d.next = null;                      // next al "nodo" successivo
        d.last = d;                         // last all'ultimo "nodo" della lista
        return d;
    }

    public DisjointSet<K> Find() {// trova il rappresentante della lista, il primo "nodo"
        return this.rep;
    }

    public void Union(DisjointSet<K> y) {// unisce la prima lista alla seconda, senza considerarne le dimensioni
        DisjointSet<K> z = this.Find();// trovo il rappresentante della prima lista
        DisjointSet<K> w = y.Find();// trovo il rappresentante della seconda lista
        if (z != w) {
            Link(z, w);// unisce la prima lista alla seconda
        }
    }

    private void Link(DisjointSet<K> z, DisjointSet<K> w) {// unisce la prima lista alla seconda
        z.last.next = w; // faccio puntare z.last, l'ultimo elemento "nodo" della prima lista x, al primo elemento "nodo" della lista y, il rappresentante w
        z.last = w.last; // faccio ora puntare z.last a quello che è il nuovo ultimo nodo w.last, cioè l'ultimo nodo della lista y aggiunta in coda a x
        while (w != null) {// devo ora scorrere tutti i "nodi" di y e modificare il rappresentante di questi che ora è diventerà il rappresentante della lista x
            w.rep = z;
            w = w.next;
        }
    }
}