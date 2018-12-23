import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.Collection;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Stack;

public class SeamCarver {
    private int[][] pic;
    private int width;
    private int height;
    private double energy[][];
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture){
        if (picture == null)
            throw new IllegalArgumentException("null picture");
        width = picture.width();
        height = picture.height();
        energy = new double[width][height];
        pic = new int[width][height];
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                pic[i][j] = picture.getRGB(i, j);
            }
        }
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                energy[i][j] = cal_energy(i, j);
            }
        }
    }

    // current picture
    public Picture picture(){
        Picture returnPic = new Picture(width, height);
        for (int i = 0; i < width; i++){
            for (int j = 0; j < height; j++){
                returnPic.setRGB(i, j, pic[i][j]);
            }
        }
        return returnPic;
    }

    // width of current picture
    public     int width(){
        return width;
    }

    // height of current picture
    public     int height(){
        return height;
    }

    private int[] get_rgb(int x, int y){
        //int rgb = picture.getRGB(x, y);
        int rgb = pic[x][y];
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >>  8) & 0xFF;
        int b = (rgb >>  0) & 0xFF;

        int []rgbs = new int[]{r,g,b};
        return rgbs;
    }
    private double cal_energy(int x, int y){
        //define the energy of a pixel at the border of the image to be 1000
        if (x == 0 || x == width -1 || y == 0 || y == height -1)
            return 1000;
        int[]rgb_l = get_rgb(x-1, y);
        int[]rgb_r = get_rgb(x+1, y);
        int[]rgb_u = get_rgb(x, y - 1);
        int[]rgb_d = get_rgb(x, y + 1);

        double deltaX = Math.pow(rgb_l[0] - rgb_r[0], 2) + Math.pow(rgb_l[1] - rgb_r[1], 2)
            + Math.pow(rgb_l[2] - rgb_r[2], 2);
        double deltaY = Math.pow(rgb_u[0] - rgb_d[0], 2) + Math.pow(rgb_u[1] - rgb_d[1], 2)
            + Math.pow(rgb_u[2] - rgb_d[2], 2);
        return Math.sqrt(deltaX + deltaY);
    }
    // energy of pixel at column x and row y
    public  double energy(int x, int y){
        if(x < 0|| y < 0 || x >= width|| y >= height)
            throw new IllegalArgumentException("x, y out of bounds");
        return energy[x][y];
    }

    private void add_Collection(Collection<int[]> adj, int[]p, boolean[][]isSearched){
        if(isSearched == null || (isSearched != null && !isSearched[p[0]][p[1]]))
            adj.add(p);
    }
    private Collection<int[]> get_adj(int[]p, boolean isHorizontal, boolean[][] isSearched){
        Collection<int[]> get_adj = new ArrayList<>();
        int x = p[0];
        int y = p[1];

        if(isHorizontal){
            if (x + 1 < width){
                if (y - 1 >= 0)
                    add_Collection(get_adj, new int[]{x+1, y-1}, isSearched);
                if (y + 1 < height)
                    add_Collection(get_adj, new int[]{x+1, y+1}, isSearched);
                add_Collection(get_adj, new int[]{x+1, y}, isSearched);
            }
        }
        else{
            if (y + 1 < height){
                if (x - 1 >= 0)
                    add_Collection(get_adj, new int[]{x-1, y+1}, isSearched);
                if (x + 1 < width)
                    add_Collection(get_adj, new int[]{x+1, y+1}, isSearched);
                add_Collection(get_adj, new int[]{x, y+1}, isSearched);
            }

        }
        return get_adj;
    }

    private Stack<int[]> TopologicalSort(boolean isHorizontal){

        //left to right
        Stack<int[]> stackSearch = new Stack<>();
        Stack<int[]> stackResult = new Stack<>();
        boolean [][]isSearched  = new boolean[width][height];
        if (isHorizontal){
            for (int j = 0; j < height; j++){
                stackSearch.add(new int []{0, j});
            }
        }
        else{
            for (int i = 0; i < width; i++){
                stackSearch.add(new int []{i, 0});
            }
        }
        while(!stackSearch.isEmpty()){
            int[] p = stackSearch.peek();
            isSearched[p[0]][p[1]] = true;
            Collection<int[]> bags = get_adj(p, isHorizontal, isSearched);
            if (bags.isEmpty()){
                stackSearch.pop();
                stackResult.push(p);
            }
            else{
                for (int[] i:bags)
                    stackSearch.push(i);
            }
        }
        return stackResult;
    }
    private void printStack(Stack<int[]> stack){
        Stack<int[]> newstack = (Stack<int[]>) (stack.clone());
        for(int[] i: newstack)
            StdOut.println(i[0]+"; "+i[1]);
    }

    private int[] SeamSearch(boolean isHorizontal){
        Stack<int[]> stackResult = TopologicalSort(isHorizontal);
        //StdOut.println("TopologicalSort");
        //printStack(stackResult);
        int[][][]edgeTo = new int[width][height][2];
        //IndexMinPQ<Double> pq = new IndexMinPQ();
        double [][]distTo = new double[width][height];
        if (isHorizontal){
            for (int i = 1; i<width; i++){
                for(int j = 0; j<height; j++){
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        else{
            for (int i = 0; i<width; i++){
                for(int j = 1; j<height; j++){
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
        boolean first = true;
        while(!stackResult.isEmpty()){
            int[] p = stackResult.pop();
            relax(distTo, edgeTo, isHorizontal, p);
        }

        int[] res = get_seam(edgeTo, distTo, isHorizontal);
        return res;
    }
    private int[] get_seam(int[][][] edgeTo, double[][]distTo, boolean isHorizontal){
        int [] res;
        if (isHorizontal)
            res = new int[width];
        else
            res = new int[height];
        int[] target = new int[2];
        double mindist = -1;
        if (isHorizontal){
            target[0] = width - 1;
            for (int j = 0; j<height; j++){
                if (j == 0 || distTo[width-1][j] < mindist){
                    target[1] = j;
                    mindist = distTo[width-1][j];
                }
            }
            for(int i = width - 1; i >=0; i--){
                res[i] = target[1];
                target = edgeTo[i][res[i]];
            }
        }
        else{
            target[1] = height - 1;
            for (int i = 0; i < width; i++){
                if (i == 0 || distTo[i][height-1] < mindist){
                    target[0] = i;
                    mindist = distTo[i][height-1];
                }
            }
            for(int j = height - 1; j >=0; j--){
                res[j] = target[0];
                target = edgeTo[res[j]][j];
            }
        }
        return res;
    }

    private void relax(double[][] distTo, int[][][] edgeTo, boolean isHorizontal, int[] p){
        for(int[] v: get_adj(p, isHorizontal, null)){
            if(distTo[p[0]][p[1]] + energy[v[0]][v[1]] < distTo[v[0]][v[1]]){
                distTo[v[0]][v[1]] = distTo[p[0]][p[1]] + energy[v[0]][v[1]];
                edgeTo[v[0]][v[1]][0] = p[0];
                edgeTo[v[0]][v[1]][1] = p[1];
            }
        }
    }

    // sequence of indices for horizontal seam
    public   int[] findHorizontalSeam(){
        return SeamSearch(true);
    }


    // sequence of indices for vertical seam
    public   int[] findVerticalSeam(){
        return SeamSearch(false);
    }

    private void update_energy(int x, int y){
        energy[x][y] = cal_energy(x, y);
    }

    // remove horizontal seam from current picture
    public    void removeHorizontalSeam(int[] seam){
        if (seam == null)
            throw new IllegalArgumentException("calling null seam");
        if(seam.length != width)
            throw new IllegalArgumentException("Call seam with wrong length");

        for (int i = 0; i < width; i++){
            if(seam[i]<0||seam[i]>=height)
                throw new IllegalArgumentException("Call seam x, y out of bounds");
            if (i >= 1 && Math.abs(seam[i] - seam[i-1]) > 1)
                throw new IllegalArgumentException("Distance larger than 1");
            for (int j = seam[i]; j < height - 1; j++){
                pic[i][j] = pic[i][j+1];
                energy[i][j] = energy[i][j+1];
            }
        }
        height = height - 1;
        for (int i = 0; i < width; i++){
            if (seam[i] == 0)
                update_energy(i, seam[i]);
            else if (seam[i] == height)
                update_energy(i, seam[i] - 1);
            else{
                update_energy(i, seam[i]);
                update_energy(i, seam[i] - 1);
            }
        }
    }

    // remove vertical seam from current picture
    public    void removeVerticalSeam(int[] seam){
        if (seam == null)
            throw new IllegalArgumentException("calling null seam");
        if (seam.length != height)
            throw new IllegalArgumentException("Call seam with wrong length");

        for (int j = 0; j < height; j++){
            if (seam[j]<0 || seam[j] >= width)
                throw new IllegalArgumentException("Call seam x, y out of bounds");
            if (j >= 1 && Math.abs(seam[j] - seam[j-1]) > 1)
                throw new IllegalArgumentException("Distance larger than 1");
            for (int i = seam[j]; i < width - 1; i++){
                pic[i][j] = pic[i+1][j];
                energy[i][j] = energy[i+1][j];
            }
        }
        width = width - 1;
        for (int j = 0; j < height; j++){
            if (seam[j] == 0)
                update_energy(seam[j], j);
            else if (seam[j] == width)
                update_energy(seam[j] - 1, j);
            else{
                update_energy(seam[j], j);
                update_energy(seam[j] - 1, j);
            }
        }
    }
}

