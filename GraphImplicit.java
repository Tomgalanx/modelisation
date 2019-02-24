package modelisation;

import java.util.ArrayList;

class GraphImplicit extends Graph{

    private  int[][] interest;
    private  int w;
    private  int h;
    private int N;


    /*
        * Pour cette classe nous avons change la modelisation
        * Nous avons decide de ne plus mettre 0 en premiere case mais NbNoeud +1
        * Et NbNoeud +2 en derniere case
        * Ce qui nous permet de connaitre la ligne et la colonne d'un sommet
     */
    GraphImplicit(int[][] interest, int w, int h){
        this.interest = interest;
        this.w = w;
        this.h = h;

        // +2 car il y a le premier sommet et le dernier
        this.N = h * w +2;
    }

    public int vertices(){
        return N;
    }

    public Iterable<Edge> next(int v)
    {
	     /*
	     ArrayList<Edge> edges = new ArrayList();
	     for (int i = v; i < N; i++)
		  edges.add(new Edge(v,i,i));
	     return edges;
	     */

	     // La liste des arretes suivante du sommet v
        ArrayList<Edge> edges = new ArrayList<Edge>();

        // Pour les calculs intermédiaire
        int lig,col;

        // Si le sommet est le premier sommet
        if(v == N-2){
            // Donc on les relies avec la premiere ligne
            for(int i=0; i < w; i++){
                // Et de cout 0
                edges.add(new Edge(v, i, 0));
            }
            return edges;
        }

        // Si le sommet est le dernier donc il n'a pas de voisin
        if(v == N-1)
            return edges;// Donc on rend une liste vide



        // On retrouve son numero de colonne et son numero de ligne
        lig = v/w;
        col = v%w;

        // Si on est sur la derniere ligne
        if(v >= w*h - w){
            edges.add(new Edge(v, N-1, interest[lig][col]));
            return edges;
        }

        // Arrete communes pour le milieu,droite et gauche
        edges.add(new Edge(v, v + w, interest[lig][col]));

        // Si on est au milieu ou au bord a droite
        if(col != 0){
            edges.add(new Edge(v, v+w-1, interest[lig][col]));
        }

        // On est au milieu ou a gauche
        if(col != w-1){
            edges.add(new Edge(v, v+w+1, interest[lig][col]));
        }
        return edges;

    }

    public Iterable<Edge> prev(int v)
    {

	     /*
	     ArrayList<Edge> edges = new ArrayList();
	     for (int i = 0; i < v-1; i++)
		  edges.add(new Edge(i,v,v));
	     return edges;
	     */

	     // La liste des arretes suivante du sommet v
        ArrayList<Edge> edges = new ArrayList();

        // Pour les calculs intermédiaire
        int lig,col;

        // Si c'est le premier sommet alors il n'y a pas de précédent
        if(v == N-2)
            return edges;


        // Si on est sur le dernier sommet
        if(v == N-1){
            // On relis la derniere ligne et le dernier sommet
            for(int i = w*h-w; i < w*h; i++){
                lig = i/w;
                col = i%w;
                edges.add(new Edge(i, v, interest[lig][col]));
            }
            return edges;
        }

        // Si on est sur la premier ligne
        if(v < w){
            // On retourne les arretes vers le premier sommet de cout 0
            edges.add(new Edge(N-2, v, 0));
            return edges;
        }


        lig = v/w;
        col = v%w;

        // Arrete communes pour le milieu,droite et gauche
        edges.add(new Edge(v - w , v, interest[( v - w ) / w ][( v - w ) % w ]));

        // Si on est au milieu ou au bord a droite
        if(col != 0)
            edges.add(new Edge(v - w -1, v, interest[( v - w - 1) / w][( v - w - 1 ) % w]));

        // On est au milieu ou a gauche
        if(col != w-1)
            edges.add(new Edge(v - w +1, v, interest[(v - w + 1) / w][(v - w + 1) % w]));

        return edges;

    }


}
