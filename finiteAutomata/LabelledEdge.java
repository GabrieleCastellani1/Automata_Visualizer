package finiteAutomata;

import graphs.Node;

public class LabelledEdge<K> {
        private final Node<K> n1;
        private final Node<K> n2;
        private final String label;

        public LabelledEdge(Node<K> n1, Node<K> n2, String label) {
            this.n1 = n1;
            this.n2 = n2;
            this.label = label;
        }

        public Node<K> n1() {
            return n1;
        }

        public Node<K> n2() {
            return n2;
        }

        public String label() {
            return label;
        }
}
