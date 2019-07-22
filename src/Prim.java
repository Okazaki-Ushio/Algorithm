import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Prim {

    private static Queue queue = new LinkedList();

    public static void main(String[] args) {
        Graph graph = new Graph();
        doInitial(graph);
        Set<Path> prettyPaths = getPrettyPath(graph);
        for (Path prettyPath : prettyPaths) {
            System.out.println(prettyPath.nodes[0].name + "--" + prettyPath.nodes[1].name);
        }
        graph.BFS(2);
        graph.DFS(2);
    }

    //do the initial of this graph
    private static void doInitial(Graph graph) {
        Node[] nodes = new Node[7];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(String.valueOf((char) (i + 65)));
        }
        graph.nodes = nodes;
        Path[] paths = new Path[11];
        paths[1] = new Path(new Node[]{nodes[0], nodes[1]}, 7);
        paths[2] = new Path(new Node[]{nodes[0], nodes[3]}, 5);

        paths[3] = new Path(new Node[]{nodes[1], nodes[2]}, 8);
        paths[4] = new Path(new Node[]{nodes[1], nodes[3]}, 9);
        paths[5] = new Path(new Node[]{nodes[1], nodes[4]}, 7);

        paths[6] = new Path(new Node[]{nodes[2], nodes[4]}, 5);

        paths[7] = new Path(new Node[]{nodes[3], nodes[4]}, 15);
        paths[8] = new Path(new Node[]{nodes[3], nodes[5]}, 6);

        paths[9] = new Path(new Node[]{nodes[4], nodes[5]}, 8);
        paths[10] = new Path(new Node[]{nodes[4], nodes[6]}, 9);
        paths[0] = new Path(new Node[]{nodes[5], nodes[6]}, 11);
        graph.paths = paths;
        for (Node node : nodes) {
            for (Path path : paths) {
                Node node1 = path.getNodes()[0];
                Node node2 = path.getNodes()[1];
                if (node == node1 || node == node2) {
                    node.paths.add(path);
                }
            }
        }
    }

    //get the pretty path of the graph
    private static Set<Path> getPrettyPath(Graph graph) {
        Set<Path> result = new HashSet<>();
        if (graph.paths == null) {
            return null;
        }
        while (graph.getPrettyPath() != null) {
            Path prettyPath = graph.getPrettyPath();
            prettyPath.nodes[0].isVisited = true;
            prettyPath.nodes[1].isVisited = true;
            result.add(prettyPath);
        }
        return result;
    }

    private static class Graph {
        private Node[] nodes;
        private Path[] paths;

        //deep first search
        public void DFS(int index) {
            if (nodes == null || nodes.length == 0) {
                return;
            }
            nodes[index].DFS();
            doSetNotVisited();
        }

        //breadth first search
        public void BFS(int index) {
            if (nodes == null || nodes.length == 0) {
                return;
            }
            System.out.println(nodes[index].name);
            nodes[index].isVisited = true;
            queue.add(nodes[index]);
            while (queue.size() > 0) {
                Node poll = (Node) queue.poll();
                poll.BFS();
            }
            doSetNotVisited();
        }

        //reset the visited flag to false
        private void doSetNotVisited() {
            for (Node node : nodes) {
                node.isVisited = false;
            }
        }

        //the initial of the graph
        private void initial(Node[] nodes, Set<Path> paths) {
            this.nodes = nodes;
            this.paths = this.paths;
        }

        //get the prettypath of the graph
        public Path getPrettyPath() {
            Set<Path> set = new HashSet<>();
            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i].isVisited) {
                    Path prettyPath = nodes[i].getPrettyPath();
                    if (null != prettyPath) {
                        set.add(prettyPath);
                    }
                }
            }
            int len = 1024;
            Path result = null;
            for (Path path : set) {
                if (path.length < len) {
                    len = path.length;
                    result = path;
                }
            }
            return result;
        }
    }

    private static class Node {
        private String name;
        private boolean isVisited = false;
        private Set<Path> paths = new HashSet<>();

        public void BFS() {
            if (paths == null || paths.size() == 0)
                return;
            for (Path path : paths) {
                if (path.nodes[0] == this && !path.nodes[1].isVisited) {
                    System.out.println(path.nodes[1].name);
                    path.nodes[1].isVisited = true;
                    queue.add(path.nodes[1]);
                } else if (path.nodes[1] == this && !path.nodes[0].isVisited) {
                    System.out.println(path.nodes[0].name);
                    path.nodes[0].isVisited = true;
                    queue.add(path.nodes[0]);
                }
            }
        }

        //the deep first search of the node
        public void DFS() {
            isVisited = true;
            System.out.println(name);
            for (Path path : paths) {
                if (path.nodes[0] == this && !path.nodes[1].isVisited) {
                    path.nodes[1].DFS();
                } else if (path.nodes[1] == this && !path.nodes[0].isVisited) {
                    path.nodes[0].DFS();
                }
            }
        }

        //get the pretty path of this node
        public Path getPrettyPath() {
            int length = 1024;
            Path result = null;
            for (Path path : paths) {
                Node node1 = path.getNodes()[0];
                Node node2 = path.getNodes()[1];
                if (this == node1 && !node2.isVisited) {
                    if (length > path.length) {
                        result = path;
                        length = path.length;
                    }
                } else if (this == node2 && !node1.isVisited) {
                    if (length > path.length) {
                        result = path;
                        length = path.length;
                    }
                }
            }
            return result;
        }

        public Node(String name) {
            this.name = name;
        }

    }

    private static class Path {
        private Node[] nodes;
        private Integer length;

        public Node[] getNodes() {
            return nodes;
        }

        public Path(Node[] nodes, Integer length) {
            this.nodes = nodes;
            this.length = length;
        }
    }
}
