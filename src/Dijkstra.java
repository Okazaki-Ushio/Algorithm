import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Dijkstra {

    private static int NUM = 7;

    public static void main(String[] args) throws DijkstraException {
        Node[] nodes = new Node[NUM];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(String.valueOf((char) (i + 65)));
        }
        MyGraph graph = doInitial(nodes);
        graph.dijkstraWay(nodes[0]);
        graph.print();
    }

    public static MyGraph doInitial(Node[] nodes) throws DijkstraException {
        if (nodes.length != NUM) {
            throw new DijkstraException("wrong length of the nodes");
        }
        MyGraph graph = new MyGraph();
        graph.graphNodes = new HashSet<>(Arrays.asList(nodes));
        graph.graphPath.add(new Path(nodes[0], nodes[1], 12));
        graph.graphPath.add(new Path(nodes[0], nodes[5], 16));
        graph.graphPath.add(new Path(nodes[0], nodes[6], 14));
        graph.graphPath.add(new Path(nodes[1], nodes[5], 7));
        graph.graphPath.add(new Path(nodes[1], nodes[2], 10));
        graph.graphPath.add(new Path(nodes[2], nodes[5], 6));
        graph.graphPath.add(new Path(nodes[2], nodes[3], 3));
        graph.graphPath.add(new Path(nodes[2], nodes[4], 5));
        graph.graphPath.add(new Path(nodes[3], nodes[4], 4));
        graph.graphPath.add(new Path(nodes[4], nodes[5], 2));
        graph.graphPath.add(new Path(nodes[4], nodes[6], 8));
        graph.graphPath.add(new Path(nodes[5], nodes[6], 9));
        for (Node node : nodes) {
            for (Path path : graph.graphPath) {
                if (path.pathNodes.contains(node))
                    node.nodePaths.add(path);
            }
        }
        graph.graphNodes = new HashSet<>(Arrays.asList(nodes));
        return graph;
    }

    static class MyGraph {
        Set<Node> graphNodes;
        Set<Path> graphPath = new HashSet<>();

        /**
         * @Description find the shortest way between start and all the other nodes
         * @Param start  the start node
         */
        public void dijkstraWay(Node start) {
            Set<Node> alreadyGreedyNode = new HashSet<>();
            alreadyGreedyNode.add(start);
            this.graphNodes.remove(start);
            start.setMinPath(start.nodeName);
            HashSet<Node> shouldGreedyNodes = new HashSet<>(this.graphNodes);
            doCircle(alreadyGreedyNode, shouldGreedyNodes);
        }

        /**
         * @Description print the result
         */
        public void print() {
            for (Node graphNode : this.graphNodes) {
                System.out.printf("%-16s%s", graphNode.getMinPath(), "total:" + graphNode.weight);
                System.out.println();
            }
        }

        /**
         * @Description do the loop to find min way of all nodes
         * @Param alreadyGreedyNode  already find the min way from the start
         * @Param shouldGreedyNodes  current not find the min way from the start
         */
        private void doCircle(Set<Node> alreadyGreedyNode, Set<Node> shouldGreedyNodes) {
            while (shouldGreedyNodes.size() > 0) {
                Object[] greedyWay = getGreedyWay(alreadyGreedyNode);
                Node in = (Node) greedyWay[0];
                Path path = (Path) greedyWay[1];
                Node out = getOutNode(path, in);
                if (null == out)
                    return;
                out.weight = in.weight + path.pathLength;
                in.nodePaths.remove(path);
                out.nodePaths.remove(path);
                trim(alreadyGreedyNode, out);
                alreadyGreedyNode.add(out);
                shouldGreedyNodes.remove(out);
                out.setMinPath(in.minPath + "-->" + out.nodeName);
            }
        }

        /**
         * @Description delete the path between the set which find the min way and the
         * latest node which should be add into this set
         * @Param alreadyGreedyNode  already find the min way from the start
         * @Param out  the node which should be add into the min way set
         */
        private void trim(Set<Node> alreadyGreedyNode, Node out) {
            Set<Path> set = new HashSet<>();
            for (Node node : alreadyGreedyNode) {
                set.addAll(node.nodePaths);
            }
            set.retainAll(out.nodePaths);
            for (Path path : set) {
                trim(path);
            }
        }

        /**
         * @Description delete the path between this path's nodes
         * @Param path
         */
        private void trim(Path path) {
            for (Node pathNode : path.pathNodes) {
                pathNode.nodePaths.remove(path);
            }
        }

        /**
         * @Description find the node that in this path but not the node which passed in
         * @Param path
         * @Param node
         * @Return return the node in the path but not the param node passed in
         */
        private Node getOutNode(Path path, Node node) {
            for (Node pathNode : path.pathNodes) {
                if (pathNode != node) {
                    return pathNode;
                }
            }
            return null;
        }

        /**
         * @Description find the new shortest way node which should be added into the alreadyGreedyNode set
         * @Param alreadyGreedyNode  the set which already find the shortest way to start
         * @Return Object[]  the new node and the new path which are selected
         */
        private Object[] getGreedyWay(Set<Node> alreadyGreedyNode) {
            alreadyGreedyNode.removeIf(node -> node.nodePaths.size() == 0);
            HashMap<Node, Path> all = new HashMap<>();
            for (Node node : alreadyGreedyNode) {
                Path greedyWay = node.getGreedyWay();
                all.put(node, greedyWay);
            }
            Node node = getGreedyNode(all);
            Path path = all.get(node);
            return new Object[]{node, path};
        }

        /**
         * @Description filter the hashmap to get the node which has the min way out
         * @Param all  all the nodes which has the way out
         * @Return node  the next node which has the min way
         */
        private Node getGreedyNode(HashMap<Node, Path> all) {
            Set<Node> nodes = all.keySet();
            Node resultNode = nodes.iterator().next();
            int resultLength = resultNode.weight + all.get(resultNode).pathLength;
            for (Node node : nodes) {
                int len = node.weight + all.get(node).pathLength;
                if (resultLength > len) {
                    resultLength = len;
                    resultNode = node;
                }
            }
            return resultNode;
        }

    }

    static class Path {
        Set<Node> pathNodes = new HashSet<>(2);
        Integer pathLength;

        Path(Node node1, Node node2, Integer length) {
            pathNodes.add(node1);
            pathNodes.add(node2);
            this.pathLength = length;
        }
    }

    static class Node {
        String nodeName;
        Integer weight = 0;
        Set<Path> nodePaths = new HashSet<>();
        String minPath = null;

        String getMinPath() {
            return minPath;
        }

        void setMinPath(String minPath) {
            this.minPath = minPath;
        }

        Node(String nodeName) {
            this.nodeName = nodeName;
        }

        /**
        * @Description  get the min way out of this node
        * @Return the path which has the min way on condition of contains this node
        */
        Path getGreedyWay() {
            if (this.nodePaths == null)
                return null;
            Path result = nodePaths.iterator().next();
            for (Path nodePath : nodePaths) {
                result = result.pathLength > nodePath.pathLength ? nodePath : result;
            }
            return result;
        }
    }
}
