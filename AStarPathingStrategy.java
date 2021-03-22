import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AStarPathingStrategy
        implements PathingStrategy
{


    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> path = new LinkedList<>();
        TreeSet<Node> open = new TreeSet<>();
        HashMap<Node, Point> closed = new HashMap<>();
        Node pathNode = null;
        boolean pathFound = false;

        open.add(new Node(start, null, 0, 0));
        long time = System.currentTimeMillis();


        while (!pathFound && !open.isEmpty()){
            Node currentNode = open.pollFirst();
//            System.out.println(currentNode.getPoint().x + ", " + currentNode.getPoint().y);

            if (withinReach.test(currentNode.getPoint(), end)){
                pathNode = currentNode;
                pathFound = true;
            }
            List<Point> potential = potentialNeighbors.apply(currentNode.getPoint()).filter(canPassThrough).collect(Collectors.toList());
            for (Point Point : potential){
                Node adjacent = new Node(Point, currentNode,Math.abs(end.x - Point.x) + Math.abs(end.y - Point.y), currentNode.getG() + 1);
                if (!closed.containsKey(adjacent) && !open.contains(adjacent)){
                        open.add(adjacent);



                }

            }
            if (!closed.containsKey(currentNode)){
                closed.put(currentNode, currentNode.getPoint());
            }
//            open.sort(Node::compareTo);



        }
        if (open.isEmpty()){
            return path;
        }

        Node previous = pathNode;
        while(previous != null){
            path.add(previous.getPoint());
            previous = previous.getPrevious();

        }
        System.out.println(System.currentTimeMillis() - time);


//        path.remove(path.size()-1);
        Collections.reverse(path);
        return path;
    }
}
