package modelisation;

import java.io.*;
import java.util.*;

public class SeamCarving
{

    public static int[][] readpgm(String fn)
    {
        try {
            InputStream f = ClassLoader.getSystemClassLoader().getResourceAsStream(fn);
            BufferedReader d = new BufferedReader(new InputStreamReader(f));
            String magic = d.readLine();
            String line = d.readLine();
            while (line.startsWith("#")) {
                line = d.readLine();
            }
            Scanner s = new Scanner(line);
            int width = s.nextInt();
            int height = s.nextInt();
            line = d.readLine();
            s = new Scanner(line);
            int maxVal = s.nextInt();
            int[][] im = new int[height][width];
            s = new Scanner(d);
            int count = 0;
            while (count < height*width) {
                im[count / width][count % width] = s.nextInt();
                count++;
            }
            return im;
        }

        catch(Throwable t) {
            t.printStackTrace(System.err) ;
            System.out.println("erreur ");
            return null;
        }
    }


    public static void writepgm(int[][] image, String filename) throws IOException {
        // On Ouvre un fichier
        File ff=new File(filename+".pgm");

        ff.createNewFile();

        FileWriter ffw=new FileWriter(ff);


        for(int i =0; i<image.length;i++){

            for(int j=0;j<image[i].length;j++){
                ffw.write(image[i][j]);

            }

        }


        ffw.close();

    }

    // renvoie un tableau de la meme taille, contenant, pour chaque pixel, son facteur d’interet
    public static int[][] interest (int[][] image){


        int[][] res = new int[image.length][image[0].length];


        for(int i =0; i<image.length;i++){

            for(int j=0;j<image[i].length;j++){

                if(j == 0){
                    res[i][j] = Math.abs(image[i][j] - image[i][j+1]);

                }

                else if (j == image[i].length-1){
                    res[i][j] = Math.abs(image[i][j]- image[i][j-1]);
                }

                else{
                    res[i][j] = Math.abs(image[i][j]-(image[i][j-1]+image[i][j+1])/2);
                }
            }

        }

        return res;
    }


    public static Graph toGraph(int[][] itr){


        int nbNoeud = itr.length * itr[0].length + 2;
        GraphArrayList graphArrayList = new GraphArrayList(nbNoeud);
        Edge edge;
        int tmp;

        for(int i =1;i<=itr[0].length;i++){
            edge = new Edge(0,i , 0);
            graphArrayList.addEdge(edge);
        }


        int compteur =1;

        for(int i = 0 ; i< itr.length;i++){
            for (int j = 0; j < itr[i].length; j++) {

                tmp = compteur;

                if(j == 0 && i < itr.length-1) {
                    edge = new Edge(tmp,tmp+itr[i].length , itr[i][j]);
                    graphArrayList.addEdge(edge);


                    edge = new Edge(tmp, tmp+itr[i].length+1, itr[i][j]);
                    graphArrayList.addEdge(edge);
                }

                else if (j == itr[i].length-1 && i < itr.length-1){

                    edge = new Edge(tmp,tmp+itr[i].length , itr[i][j]);
                    graphArrayList.addEdge(edge);


                    edge = new Edge(tmp, tmp+itr[i].length-1, itr[i][j]);
                    graphArrayList.addEdge(edge);


                }

                else if(i < itr.length-1){

                    edge = new Edge(tmp, tmp+itr[i].length-1, itr[i][j]);
                    graphArrayList.addEdge(edge);

                    edge = new Edge(tmp,tmp+itr[i].length , itr[i][j]);
                    graphArrayList.addEdge(edge);

                    edge = new Edge(tmp, tmp+itr[i].length+1, itr[i][j]);
                    graphArrayList.addEdge(edge);

                }else{


                    edge = new Edge(tmp, nbNoeud-1, itr[i][j]);
                    graphArrayList.addEdge(edge);

                }

                compteur++;
            }

        }



        graphArrayList.writeFile("monTest.dot");

        return graphArrayList;
    }


    public static ArrayList tritopo(Graph g){




        ArrayList<Integer> tmp = DFS.botched_dfs4(g,0);
        Collections.reverse(tmp);

        return tmp;
    }




    public static Object[] Bellman(Graph g, int s,int t, ArrayList<Integer> order){

        int V = g.vertices();
        int dist[] = new int[V];
        int pere[] = new int[V];

        for (int i=0; i<V; i++)
            dist[i] = Integer.MAX_VALUE;

        dist[s] = 0;

        for(int ordre : order) {

            Iterable<Edge> noeud = g.next(ordre);

            for(Edge e : noeud){

                if (e.from == ordre) {
                    dist[e.to] = Math.min(dist[e.to], dist[e.from] + e.cost);
                    pere[e.to] = e.from;
                }


            }



        }


        int index =0;
        for(int res : dist){

            System.out.println(index +" "+res);
            index ++;
        }

        return new Object[]{dist,pere};
    }


}
