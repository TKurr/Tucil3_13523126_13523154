package rushhours.algorithm;

import rushhours.model.State;
import java.util.*;

public class BestFirstSearch {

    private HashSet<String> visited;

    public BestFirstSearch() {
        this.visited = new HashSet<>();
    }

    public final int getVisitedNode() {
        return this.visited.size();
    }

    public State solve(State start, String heuristicType) {
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::getNextCost));

        int initialHeuristic = start.getHeuristicValue(heuristicType, start.getBoard());
        start.setNextCost(initialHeuristic);
        start.setPastCost(0);
        start.setTotalCost(initialHeuristic); 

        queue.add(start);
        visited.add(start.getBoardState());

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.isGoal(current.getBoard())) {
                return current;
            }

            Set<State> nextStates = current.generateNextStates(visited, heuristicType);
            for (State next : nextStates) {
                String boardState = next.getBoardState();
                if (!visited.contains(boardState)) {
                    visited.add(boardState);
                    queue.add(next);
                }
            }
        }
        return null;
    }
}