import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
public class BaseballElimination{
    private int numOfTeams;
    private List<String> Teams;
    private int[] Wins;
    private int[] Losses;
    private int[] Remains;
    private int[][] Games;

    private boolean[] IsEliminated;
    private boolean[] IsValid;
    private Hashtable<Integer, HashSet<String>> certificates;

// create a baseball division from given filename in format specified below
    public BaseballElimination(String filename){
        if (filename == null)
            throw new IllegalArgumentException("null filename");
        In in = new In(filename);
        numOfTeams = in.readInt();
        Teams = new ArrayList<String>(numOfTeams);
        Wins = new int[numOfTeams];
        Losses = new int[numOfTeams];
        Remains = new int[numOfTeams];
        Games = new int[numOfTeams][numOfTeams];
        IsEliminated = new boolean[numOfTeams];
        IsValid = new boolean[numOfTeams];
        certificates = new Hashtable<>(numOfTeams);

        for (int i = 0; i < numOfTeams; i++){
            Teams.add(in.readString());
            Wins[i] = in.readInt();
            Losses[i] = in.readInt();
            Remains[i] = in.readInt();
            for (int j = 0; j < numOfTeams; j++){
                Games[i][j] = in.readInt();
            }
        }
    }

    private void check(int idx){
        List<Integer> list = new ArrayList<Integer>();
        certificates.put(idx, new HashSet<String>());
        for(int i = 0; i < numOfTeams; i++){
            if (i != idx){
                if (Wins[idx] + Remains[idx] < Wins[i]){
                    certificates.get(idx).add(Teams.get(i));
                    IsEliminated[idx] = true;
                }
                else{
                    list.add(i);
                }
            }
        }
        int size = list.size();
        if (size <= 1)
            return;
        //StdOut.print("size = " + size);
        //StdOut.print("Net size = " + (1 + ((size * (size - 1))>>1) + size + 1));
        FlowNetwork net = new FlowNetwork(1 + ((size * (size - 1))>>1) + size + 1);
        int count = 1;
        int start = 1 + ((size * (size - 1)) >> 1);
        int sink = 1 + ((size * (size - 1)) >> 1) + size;
        for (int i = 0; i < size; i++){
            for (int j = i + 1; j < size; j++){
                net.addEdge(new FlowEdge(0, count, (double) Games[list.get(i)][list.get(j)]));
                net.addEdge(new FlowEdge(count, start + i, Double.POSITIVE_INFINITY));
                net.addEdge(new FlowEdge(count, start + j, Double.POSITIVE_INFINITY));
                count++;
            }
            net.addEdge(new FlowEdge(start + i, sink, Wins[idx] + Remains[idx] - Wins[list.get(i)]));
        }

        FordFulkerson maxflow = new FordFulkerson(net, 0, sink);

        for (FlowEdge e: net.adj(0)){
            if (maxflow.inCut(e.other(0))){
                //StdOut.print(e.other(0) + " ");
                IsEliminated[idx] = true;
                for (FlowEdge e2: net.adj(e.other(0))){
                    if (e2.other(e.other(0)) != 0)
                        certificates.get(idx).add(Teams.get(list.get(e2.other(e.other(0)) - start)));
                }
            }
        }
        IsValid[idx] = true;
    }

    // number of teams
    public  int numberOfTeams(){
        return numOfTeams;
    }
    // all teams
    public Iterable<String> teams(){
        return Teams;
    }

    // number of wins for given team
    public  int wins(String team) {
        int idx = Teams.indexOf(team);
        if (idx == -1)
            throw new IllegalArgumentException("invalid team");
        return Wins[idx];
    }
    public  int losses(String team) {
        int idx = Teams.indexOf(team);
        if (idx == -1)
            throw new IllegalArgumentException("invalid team");
        return Losses[idx];
    }
    // number of losses for given team
    public int remaining(String team){
        int idx = Teams.indexOf(team);
        if (idx == -1)
            throw new IllegalArgumentException("invalid team");
        return Remains[idx];
    }
    // number of remaining games for given team
    public              int against(String team1, String team2){
        int idx1 = Teams.indexOf(team1);
        int idx2 = Teams.indexOf(team2);
        if (idx1 == -1 || idx2 == -1)
            throw new IllegalArgumentException("invalid team");
        return Games[idx1][idx2];
    }
    // number of remaining games between team1 and team2
    public  boolean isEliminated(String team) {
        int idx = Teams.indexOf(team);
        if (idx == -1)
            throw new IllegalArgumentException("invalid team");
        if (!IsValid[idx]){
            check(idx);
        }
        return IsEliminated[idx];
    }
    // is given team eliminated?
    public Iterable<String> certificateOfElimination(String team){
        int idx = Teams.indexOf(team);
        if (idx == -1)
            throw new IllegalArgumentException("invalid team");
        if (!IsValid[idx]){
            check(idx);
        }
        if (IsEliminated[idx])
            return certificates.get(idx);
        else
            return null;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public static void main(String[] args){
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }

    }
}
