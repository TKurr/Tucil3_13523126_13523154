package rushhours.algorithm;

import rushhours.model.*;
import java.util.*;

public class Astar {

    private Set<String> visited;

    public Astar() {
        this.visited = new HashSet<>();
    }

    public final int getVisitedNode() {
        return this.visited.size();
    }

    public State solve(State initialState, String heuristicType) {
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::getTotalCost));

        initialState.setPastCost(0);
        initialState.setNextCost(initialState.getHeuristicValue(heuristicType, initialState.getBoard()));
        initialState.setTotalCost(initialState.getPastCost() + initialState.getNextCost());

        queue.add(initialState);
        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.isGoal(current.getBoard())) {
                visited.add(current.getBoardState());
                return current;
            }

            String stateKey = current.getBoardState();
            if (visited.contains(stateKey)) continue;
            visited.add(stateKey);

            for (State next : current.generateNextStates(new HashSet<>(visited), heuristicType)) {
                if (!visited.contains(next.getBoardState())) {
                    queue.add(next);
                }
            }
        }
        return null;
    }
}