package modelisation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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

    public static void testGraph(String nom) throws IOException {

        // On met l'image sous forme d'un tableau de deux dimensions
        int[][] test = SeamCarving.readpgm("modelisation/"+nom);

        // On initialise le nombre de noeuds du graph
        int nbNoeud = test.length * test[0].length + 2;

        // On supprime 50 colonne de l'image
        for (int i = 0; i < 50; i++) {

            System.out.println("Nombre de ligne traité :"+i);

            // On genere un tableau a deux dimensions qui contient les facteurs d'interet pour chaque pixel
            int[][] interest = SeamCarving.interest(test);

            // On genere le graph
            Graph g = SeamCarving.toGraph(interest);

            // On applique le tri topologique sur le graph genere
            ArrayList<Integer> tritopo = SeamCarving.tritopo(g);

            // On applique Bellman pour recuperer le chemin de cout minimal
            Integer[] parents = SeamCarving.Bellman(g, 0, nbNoeud, tritopo);

            ArrayList<Integer> chemin = new ArrayList<>(g.vertices());

            // On cherche les noeuds qui constituent le chemin de cout minimal
            Integer parcours = parents[g.vertices() - 1];

            while (parcours != -1) {
                chemin.add(parcours);
                parcours = parents[parcours];
            }

            chemin.remove(chemin.size()-1);
            chemin.remove(new Integer(0));


            Collections.reverse(chemin);

            // On obtient le tableau avec les colonne modifie et on refait la boucle
            test = SeamCarving.imageModifier(test, chemin);
        }

        // On ecrit le fichier .pgm
        SeamCarving.writepgm(test, nom);

        System.out.println("\n L'image a été réduite sous le nom de "+nom);
    }


    public  static  void testMonGraph(){

        int[][] test = SeamCarving.readpgm("modelisation/ex1.pgm");

        int[][] interest = SeamCarving.interest(test);

        Graph g =SeamCarving.toGraph(interest);

        ArrayList<Integer> CChemin = new ArrayList<>();



        for(int i=0;i<50;i++) {
            int nbNoeud = interest.length * interest[0].length + 2;

            ArrayList<Integer> triTop = SeamCarving.tritopo(g);


            //ArrayList<Integer> pere = SeamCarving.Bellman(g, 0, nbNoeud, triTop);



            int last = triTop.get(triTop.size() - 1);


            g = SeamCarving.toGraph(interest, CChemin);

            test = SeamCarving.imageModifier(test, CChemin);



        }

        try {
            SeamCarving.writepgm(test, "monImage");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException {

        if(args.length < 1){
            System.out.println("Vous devez indiquer le nom de l'image");
        }
        else {

            if(!args[0].contains(".pgm"))
                System.out.println("L'image doit etre au format .pgm");
            else
                testGraph(args[0]);
        }
    }
}
