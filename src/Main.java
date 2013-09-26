import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*****************************************************************************
 * Written by: Simon Cicek                                                   *
 * Last changed: 2013-07-20                                                  *
 *                                                                           *
 * The class holds everything to solve the bipartite problem using           *
 * Edmond-Karps maximum flow algorithm.                                      *
 *****************************************************************************/

public class Main 
{
    // Used to represent the edges in the graph
    public class Edge
    {
        public int from, to, capacity, flow = 0;  
        public boolean forwardEdge = false, saved = false;
        public Edge otherDir;
        
        Edge(int from, int to, boolean dir, int cap, int flow)
        {
            this.from = from;
            this.to = to;
            this.forwardEdge = dir;
            this.capacity = cap;
            this.flow = flow;
        }
        
        // Returns the residual capacity
        public int getResCap()
        {
            return capacity - flow;
        }
        
        @Override
        public String toString()
        {
            return "From: " + from + " To: " + to + " Capacity: " + capacity + " Flow: " + flow;
        }
    }
    
    Main()
    {
        try
        {
            in = new Scanner(System.in);
        }
        catch(Exception e){}
    }
    
    public static final int unvisited = 0, visited = 1;
    private int v, e, source, sink, max_flow, x, y;
    private Edge[] parent;
    private Queue<Integer> queue;
    private LinkedList<Edge>[] graph;
    private LinkedList<Edge> answer = new LinkedList();
    Scanner in;
    
    // The Edmond-Karp algorithm used to find the maximum flow
    private void maxFlow()
    {
        max_flow = 0;
        while(true)
        {
            int m = BFS();
            if(m == 0)
                break;
            
            max_flow += m;
            Edge b = parent[sink];
            while(b.to != source)
            {
                b.flow += m;
                if(b.otherDir != null)
                    b.otherDir.flow -= m;
                b = parent[b.from]; 
                if(b.forwardEdge && !b.saved && b.from != source && b.to != sink)
                {
                    answer.add(b);
                    b.saved = true;
                }
            }
        }
    }
    
    // Beadth First Search
    private int BFS()
    {
        parent = new Edge[v];        
        parent[source] = new Edge(source, source, false, 0, 0); 
        
        int[] M = new int[v];
        M[source] = Integer.MAX_VALUE;
        
        queue = new LinkedList();
        queue.offer(source);
        
        while(!queue.isEmpty())
        {
            int a = queue.poll();
            if(graph[a] == null)
                continue;
            for(Edge b : graph[a])
            {
                if(b.getResCap() > 0 && parent[b.to] == null)
                {
                    parent[b.to] = b;
                    M[b.to] = Math.min(M[b.from], b.getResCap());
                    if(b.to != sink)
                        queue.offer(b.to);
                    else
                        return M[sink];
                }            
            }
        }
        return 0;
    }
    
    public void solve()
    {
        System.out.println("Number of vertices on the left hand side of the graph: ");
        x  = in.nextInt();
        System.out.println("Number of vertices on the right hand side of the graph: ");
        y  = in.nextInt(); 
        System.out.println("Number of edges in the graph: ");
        int eo = in.nextInt(); 
        
        // Nr. of vertices
        v  = x + y + 3;    
        // Nr. of edges
        e  = x + y + eo; 
        source = x + y + 1;
        sink   = x + y + 2;           
        graph = new LinkedList[v];
        
        // Add the edges to the graph
        // and edge goes from vertice i to vertice i + 1
        System.out.println("\nInput edges");
        for(int i = 0; i < eo; i++)
        {
            
            System.out.println("Starts in: ");
            int a = in.nextInt(); 
            System.out.println("Ends in: ");
            int b = in.nextInt();
            
            if(graph[a] == null)
                graph[a] = new LinkedList();
            if(graph[b] == null)
                graph[b] = new LinkedList();
            Edge n = new Edge(a, b, true, 1, 0);
            Edge n2 = new Edge(b, a, false, 1, 1);
            n.otherDir = n2;
            n2.otherDir = n;
            graph[a].add(n);
            graph[b].add(n2);
        }
        
        // Add all edges that start in the source
        for (int i = 1; i <= x; i++)
        {
            if(graph[source] == null)
                graph[source] = new LinkedList();
            Edge n = new Edge(source, i, true, 1, 0);
            graph[source].add(n);
        }
        
        // Add all edges that end in the sink
        for (int i = x + 1; i <= x + y; i++)
        {
            if(graph[i] == null)
                graph[i] = new LinkedList();
            Edge n = new Edge(i, sink, true, 1, 0);
            graph[i].add(n);
        }
        
        maxFlow();
        
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for(Edge e : answer)
            if(e.flow > 0)
            {
                k++;
                sb.append(e.from + " " + e.to + "\n");
            }
        System.out.println("\nNumber of vertices on the left hand side of the graph: " 
                           + x + "\n" + "Number of vertices on the right hand side of the graph: "
                           + y + "\n" + "Number of edges: " + k + "\n" + 
                           "Edges\nFrom  To\n" + sb.toString());
    }    
    public static void main(String[] args)
    {
        Main m = new Main();
        m.solve();
    }
}