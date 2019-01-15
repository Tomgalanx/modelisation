package modelisation;

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
        test = SeamCarving.interest(test);

        SeamCarving.toGraph(test);
    }

    public static void main(String[] args)
    {

        int [][] tab  = new int[3][4];
        tab[0][0] = 3;
        tab[0][1] = 11;
        tab[0][2] = 24;
        tab[0][3] = 39;

        tab[1][0] = 8;
        tab[1][1] = 21;
        tab[1][2] = 29;
        tab[1][3] = 39;

        tab[2][0] = 200;
        tab[2][1] = 60;
        tab[2][2] = 25;
        tab[2][3] = 0;

        tab =SeamCarving.interest(tab);

        for(int i =0; i<tab.length;i++) {

            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j]);
                System.out.print(" ");
            }
            System.out.println();

        }


        testMonGraph();
    }
}
