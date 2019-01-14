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

                if(i == 0){
                    res[i][j] = Math.abs(image[i][j] - image[i+1][j]);

                }

                else if (i == image.length-1){
                    res[i][j] = Math.abs(image[i][j]- image[i-1][j]);
                }

                else{
                    res[i][j] = Math.abs(image[i][j]-(image[i-1][j]+image[i+1][j])/2);
                }
            }

            }

       return res;
    }


    public static Graph toGraph(int[][] itr){


       int nbNoeud = itr.length * itr[0].length + 2;
       GraphArrayList graphArrayList = new GraphArrayList(itr.length);
        Edge edge;


        int compteur =0;

        for(int i = 1 ; i< itr.length;i++){
            edge = new Edge(0,i,0);
            graphArrayList.addEdge(edge);
        }




        graphArrayList.writeFile("monTest.dot");

       return graphArrayList;
    }

   
}
