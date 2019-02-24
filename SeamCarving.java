package modelisation;

import java.io.*;
import java.util.*;

public class SeamCarving
{

    private static final int MAXVAL = 255;


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

            d.close();
            f.close();

            return im;
        }

        catch(Throwable t) {
            t.printStackTrace(System.err) ;
           // System.out.println("erreur ");
            return null;
        }


    }


    public static void writepgm(int[][] image, String filename) throws IOException {

        PrintWriter pw = new PrintWriter("src/modelisation/image/"+filename+"_réduite.pgm");
        int width = image[0].length;
        int height = image.length;


        pw.println("P2");
        pw.println(width + " " + height);
        pw.println(MAXVAL);

        int lineLength = 0;
        for (int i = 0; i < height; ++i)
        {
            for (int j = 0; j < width; ++j)
            {
                int value = image[i][j];

                String stringValue = "" + value;
                int currentLength = stringValue.length() + 1;
                if (currentLength + lineLength > 70)
                {
                    pw.println();
                    lineLength = 0;
                }
                lineLength += currentLength;
                pw.print(value + " ");
            }
        }
        pw.close();

    }

    // renvoie un tableau de la meme taille, contenant, pour chaque pixel, son facteur d’interet
    public static int[][] interest (int[][] image){


        int[][] res = new int[image.length][image[0].length];

        // On parcours le tableau de l'image
        for(int i =0; i<image.length;i++){

            for(int j=0;j<image[i].length;j++){

                // le pixel est en d´ebut de ligne , le facteur d'interet est la difference entre le pixel et le pixel suivant
                if(j == 0){
                    res[i][j] = Math.abs(image[i][j] - image[i][j+1]);

                }
                // le pixel au bout de la ligne , le facteur d'interet est la difference entre le pixel et le pixel precedent
                else if (j == image[i].length-1){
                    res[i][j] = Math.abs(image[i][j]- image[i][j-1]);
                }
                // le facteur d’interet est la difference entre la valeur du pixel, et la moyenne des valeurs de ses voisins de gauche et de droite
                else{
                    res[i][j] = Math.abs(image[i][j]-(image[i][j-1]+image[i][j+1])/2);
                }
            }

        }

        return res;
    }


    // Excatement la meme methode que pour les colonnes mais on inverse avec les lignes
    public static int[][] interestLine(int[][] image){

        int[][] res = new int[image.length][image[0].length];

        // On parcours le tableau de l'image
        for(int i =0; i<image.length;i++){

            for(int j=0;j<image[i].length;j++){

                // le pixel est en d´ebut de ligne , le facteur d'interet est la difference entre le pixel et le pixel suivant
                if(i == 0){
                    res[i][j] = Math.abs(image[i][j] - image[i+1][j]);

                }
                // le pixel au bout de la ligne , le facteur d'interet est la difference entre le pixel et le pixel precedent
                else if (i == image.length-1){
                    res[i][j] = Math.abs(image[i][j]- image[i-1][j]);
                }
                // le facteur d’interet est la difference entre la valeur du pixel, et la moyenne des valeurs de ses voisins de gauche et de droite
                else{
                    res[i][j] = Math.abs(image[i][j]-(image[i-1][j]+image[i+1][j])/2);
                }
            }

        }

        return res;
    }

    // Cree le graph depuis un tableau
    public static Graph toGraph(int[][] itr) {

        // On initialise le nombre de noeuds du graph
        int nbNoeud = itr.length * itr[0].length + 2;
        GraphArrayList graphArrayList = new GraphArrayList(nbNoeud);
        Edge edge;
        int tmp;

        // On cree les arretes entre la racine et les noeuds de la premiere ligne avec un cout a 0
        for (int i = 1; i <= itr[0].length; i++) {
            edge = new Edge(0, i, 0);
            graphArrayList.addEdge(edge);
        }


        int compteur = 1;

        // On cree les autres arretes
        for (int i = 0; i < itr.length; i++) {
            for (int j = 0; j < itr[i].length; j++) {

                tmp = compteur;

                // On est au debut de la ligne et avant l'avant derniere ligne
                if (j == 0 && i < itr.length - 1) {
                    edge = new Edge(tmp, tmp + itr[i].length, itr[i][j]);
                    graphArrayList.addEdge(edge);

                    edge = new Edge(tmp, tmp + itr[i].length + 1, itr[i][j]);
                    graphArrayList.addEdge(edge);
                }
                // On est a la fin de la ligne et avant l'avant derniere ligne
                else if (j == itr[i].length - 1 && i < itr.length - 1) {

                    edge = new Edge(tmp, tmp + itr[i].length, itr[i][j]);
                    graphArrayList.addEdge(edge);


                    edge = new Edge(tmp, tmp + itr[i].length - 1, itr[i][j]);
                    graphArrayList.addEdge(edge);


                }
                // On est juste avant l'avant derniere ligne
                else if (i < itr.length - 1) {

                    edge = new Edge(tmp, tmp + itr[i].length - 1, itr[i][j]);
                    graphArrayList.addEdge(edge);

                    edge = new Edge(tmp, tmp + itr[i].length, itr[i][j]);
                    graphArrayList.addEdge(edge);

                    edge = new Edge(tmp, tmp + itr[i].length + 1, itr[i][j]);
                    graphArrayList.addEdge(edge);

                }
                // on est a l'avant derniere ligne
                else {


                    edge = new Edge(tmp, nbNoeud - 1, itr[i][j]);
                    graphArrayList.addEdge(edge);


                }

                compteur++;
            }

        }


        //graphArrayList.writeFile("monTest.dot");

        return graphArrayList;
    }


    public static ArrayList tritopo(Graph g,int depart){

        // Application du DFS
        ArrayList<Integer> tmp = DFSIteratif(g,depart);

        // Et on inverse la liste
        Collections.reverse(tmp);

        return tmp;
    }

    // On cherche le chemin de cout minimal
    public static Integer[] Bellman(Graph g, int s, int t, ArrayList<Integer> order){

        int V = g.vertices();
        int dist[] = new int[V];
        Integer pere[] = new Integer[V];

        for(int i=0;i<pere.length;i++){
            pere[i] =-1;
        }

        for (int i=0; i<V; i++)
            dist[i] = Integer.MAX_VALUE;

        dist[s] = 0;

        for (Integer ordre : order){

            for (Edge e : g.prev(ordre)) {

                // Si la distance trouver est plus petite que la distance precedente on la remplace et on memorise le noeud
                if ( dist[e.to] != Math.min(dist[e.to], dist[e.from] + e.cost)) {
                    dist[ordre] = dist[e.from] + e.cost;
                    pere[ordre] = e.from;
                }
            }
        }


        return pere;
    }



    // Retourne le tableau image modifie sans le chemin donnee
    public static int[][] imageModifier(int[][] image, ArrayList<Integer> chemin){


        int[][] res = new int[image.length][image[0].length - 1];

        int compteur =0;

        for (int i = 0; i < image.length; i++) {
            boolean finded = false;
            for (int j = 0; j < image[0].length; j++) {

                // Si on trouve la valeur du compteur dans le chemin on decale les valeurs du tableau
                if (!chemin.contains(compteur)) {
                    if(finded) {
                        res[i][j-1] = image[i][j];
                    }
                    else {
                        res[i][j] = image[i][j];
                    }

                }
                else{
                    finded = true;
                }

                compteur++;
            }
        }

        return res;
    }

    // Retourne le tableau image modifie sans le chemin donnee
    public static int[][] imageModifierAugmente(int[][] image, ArrayList<Integer> chemin){


        int[][] res = new int[image.length][image[0].length +1];

        int compteur =1;

        for (int i = 0; i < image.length; i++) {
            boolean trouve = false;
            for (int j = 0; j < image[0].length; j++) {


                // Si on trouve la valeur du compteur dans le chemin on decale les valeurs du tableau
                if (!chemin.contains(compteur)) {
                    if(trouve) {
                        res[i][j+1] = image[i][j];
                    }
                    else {
                        res[i][j] = image[i][j];
                    }

                }
                else{
                    trouve = true;
                }



                compteur++;
            }
        }

        return res;
    }

    


    public static ArrayList<Integer> DFSIteratif(Graph g, int s){

        ArrayList<Integer> res = new ArrayList<>();

        Stack<Integer> uStack =new Stack<>();
        Stack<Iterator<Edge>> itStack = new Stack<>();

        boolean visited[] = new boolean[g.vertices()];


        // On ajoute la paire s et it

        uStack.push(s);
        itStack.push(g.next(s).iterator());

        // Tant que la pile n'est pas vide

        while(!uStack.empty()){

            // On récupert la pile u,it

            int newU = uStack.peek();
            Iterator<Edge> newIt = itStack.peek();

            // Si il reste un voisin u non testé
            if(newIt.hasNext()){

                int v = newIt.next().to;

                if(!visited[v]){
                    visited[v] =true;

                    uStack.push(v);
                    itStack.push(g.next(v).iterator());
                }

            }

            // Sinon on retire u,it de la pile
            else{

                uStack.pop();
                itStack.pop();

                res.add(newU);
            }
        }



        return res;

    }

    // Creer le graph depuis un tableau sans qu'il contient le chemin donnee en parametre
    public static Graph toGraph(int[][] itr, ArrayList<Integer> chemin){

        int nbNoeud = itr.length * itr[0].length + 2;
        GraphArrayList graphArrayList = new GraphArrayList(nbNoeud);
        Edge edge;
        int tmp;

        for(int i =1;i<=itr[0].length;i++){
            edge = new Edge(0,i , 0);
            if (!chemin.contains(i))
                graphArrayList.addEdge(edge);
        }


        int compteur =1;

        for(int i = 0 ; i< itr.length;i++){
            for (int j = 0; j < itr[i].length; j++) {

                tmp = compteur;


                if (!chemin.contains(tmp)) {


                    if (j == 0 && i < itr.length - 1) {
                        edge = new Edge(tmp, tmp + itr[i].length, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length))
                            graphArrayList.addEdge(edge);

                        edge = new Edge(tmp, tmp + itr[i].length + 1, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length + 1))
                            graphArrayList.addEdge(edge);
                    } else if (j == itr[i].length - 1 && i < itr.length - 1) {

                        edge = new Edge(tmp, tmp + itr[i].length, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length))
                            graphArrayList.addEdge(edge);


                        edge = new Edge(tmp, tmp + itr[i].length - 1, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length - 1))
                            graphArrayList.addEdge(edge);


                    } else if (i < itr.length - 1) {

                        edge = new Edge(tmp, tmp + itr[i].length - 1, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length - 1))
                            graphArrayList.addEdge(edge);

                        edge = new Edge(tmp, tmp + itr[i].length, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length))
                            graphArrayList.addEdge(edge);

                        edge = new Edge(tmp, tmp + itr[i].length + 1, itr[i][j]);
                        if (!chemin.contains(tmp + itr[i].length + 1))
                            graphArrayList.addEdge(edge);

                    } else {


                        edge = new Edge(tmp, nbNoeud - 1, itr[i][j]);
                        graphArrayList.addEdge(edge);


                    }

                }
                compteur++;
            }

        }


        // On genere le fichier .dot
        graphArrayList.writeFile("monTest.dot");

        return graphArrayList;
    }


    public static int[][] imageModifierLine(int[][] image, ArrayList<Integer> chemin) {

        // On enleve une ligne de ligne originale
        int[][] res = new int[image.length-1][image[0].length];

        for (int j = 0; j < image[0].length; j++) {
            boolean trouve = false;
            for (int i = 0; i < image.length-1; i++) {

                // On retrouve le numero de la case dans le graphe
                int indice = i*image[i].length +j;

                // Si le sommet du graphe est dans le plus court chemin
                if (!chemin.contains(i * image[0].length + j)) {
                    // Alors on decale tous
                    if(trouve) {
                        res[i-1][j] = image[i][j];
                    }
                    else {
                        res[i][j] = image[i][j];
                    }

                }
                else{
                    trouve = true;
                }
            }
        }

        return res;
    }
}
