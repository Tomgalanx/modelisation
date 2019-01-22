package modelisation;

import java.io.IOException;
import java.util.ArrayList;

class Test
{
    static boolean visite[];
    public static void dfs(Graph g, int u)
    {
        visite[u] = true;
        System.out.println("Je visite " + u);
        for (Edge e: g.next(u))
            if (!visite[e.to])
                dfs(g,e.to);
    }

    public static void testGraph()
    {
        int n = 5;
        int i,j;
        GraphArrayList g = new GraphArrayList(n*n+2);

        for (i = 0; i < n-1; i++)
            for (j = 0; j < n ; j++)
                g.addEdge(new Edge(n*i+j, n*(i+1)+j, 1664 - (i+j)));

        for (j = 0; j < n ; j++)
            g.addEdge(new Edge(n*(n-1)+j, n*n, 666));

        for (j = 0; j < n ; j++)
            g.addEdge(new Edge(n*n+1, j, 0));

        g.addEdge(new Edge(13,17,1337));
        g.writeFile("test.dot");
        // dfs à partir du sommet 3
        visite = new boolean[n*n+2];
        dfs(g, 3);
    }


    public  static  void testMonGraph(){

        int[][] test = SeamCarving.readpgm("modelisation/ex1.pgm");

        int[][] interest = SeamCarving.interest(test);

        Graph g =SeamCarving.toGraph(interest);

        ArrayList<Integer> CChemin = new ArrayList<>();

        for(int i=0;i<50;i++) {
            int nbNoeud = interest.length * interest[0].length + 2;

            ArrayList<Integer> triTop = SeamCarving.tritopo(g);


            Object[] res = SeamCarving.Bellman(g, 0, nbNoeud, triTop);

            int[] dist = (int[]) res[0];
            int[] pere = (int[]) res[1];


            int last = triTop.get(triTop.size() - 1);

            while (last != 0) {

                CChemin.add(last);
                last = pere[last];
            }


            g = SeamCarving.toGraph(interest, CChemin);

            test = SeamCarving.imageModifier(test, CChemin);



        }

        try {
            SeamCarving.writepgm(test, "monImage");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        testMonGraph();
    }
}
