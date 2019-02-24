package modelisation;

import java.util.ArrayList;

public class GraphImpliciteLine extends GraphImplicit {


    // Classe qui construit le graphe en ligne

    // Dans cette modelisation le premier sommet est NbNoeud +1
    // Et le dernier sommet est nbNoeud +2


    // Cette classe est un copier-coller du GraphImplicit
    // On l'a adapté pour le parcours par ligne
    public GraphImpliciteLine(int[][] interest, int w, int h){
        super(interest,w,h);
    }


    @Override
    public int vertices() {
        return N;
    }

    @Override
    public Iterable<Edge> next(int v) {

        // La liste des arretes suivante du sommet v
        ArrayList<Edge> edges = new ArrayList<Edge>();

        // Pour les calculs intermédiaire
        int lig,col;

        // Si le sommet est le premier sommet
        if(v == N-2){
            // Donc on les relies avec la premiere colonne
            for(int i=0; i < h; i++){
                // Et de cout 0
                edges.add(new Edge(v, i*w, 0));
            }
            return edges;
        }

        // Si le sommet est le dernier donc il n'a pas de voisin
        if(v == N-1)
            return edges;// Donc on rend une liste vide



        // On retrouve son numero de colonne et son numero de ligne
        lig = v/w;
        col = v%w;

        // Si on est sur la derniere colonne
        if((v + 1) % w == 0){
            edges.add(new Edge(v, N-1, interest[lig][w-1]));
            return edges;
        }

        // Arrete communes pour le milieu,droite et gauche
        edges.add(new Edge(v, v + 1, interest[lig][col]));

        // Si on est au milieu ou au bord a droite
        if(lig != 0){
            edges.add(new Edge(v, v-w+1, interest[lig][col]));
        }

        // On est au milieu ou a gauche
        if(lig != h-1){
            edges.add(new Edge(v, v+w+1, interest[lig][col]));
        }
        return edges;
    }

    @Override
    public Iterable<Edge> prev(int v) {


        // La liste des arretes suivante du sommet v
        ArrayList<Edge> edges = new ArrayList();

        // Pour les calculs intermédiaire
        int lig,col;

        // Si c'est le premier sommet alors il n'y a pas de précédent
        if(v == N-2)
            return edges;


        // Si on est sur le dernier sommet
        if(v == N-1){
            // On relis la derniere colonne et le dernier sommet
            for(int i = 0; i < h; i++){
                lig = (v - 2 - (i * w)) / w;
                col = (v - 2 - (i * w)) % w;
                edges.add(new Edge(v - 2 - (i * w), v, interest[lig][col]));
            }
            return edges;
        }



        // Si on est sur la premiere colonne
        if(v%w == 0){
            // On retourne les arretes vers le premier sommet de cout 0
            edges.add(new Edge(N-2, v, 0));
            return edges;
        }


        lig = v/w;
        col = v%w;

        // Arrete communes pour le milieu,droite et gauche
        edges.add(new Edge(v - 1, v, interest[(v - 1) / w][(v - 1) % w]));

        // Si on est au milieu ou au bord a droite
        if(lig != 0)
            edges.add(new Edge(v - w -1, v, interest[( v - w - 1) / w][( v - w - 1 ) % w]));

        // On est au milieu ou a gauche
        if(lig != h-1)
            edges.add(new Edge(v + w -1, v, interest[(v + w - 1) / w][(v + w - 1) % w]));

        return edges;
    }
}
