package rushhours.algorithm;

import rushhours.model.*;
import java.util.*;

public class UCS {

    private Set<String> visited;

    public UCS() {
        this.visited = new HashSet<>();
    }

    public final int getVisitedNode() {
        return this.visited.size();
    }

    public State solve(State initialState) {
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::getPastCost));

        initialState.setNextCost(1);
        initialState.setPastCost(0);
        initialState.setTotalCost(1);
        
        queue.add(initialState);
        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.isGoal(current.getBoard())) {
                return current;
            }

            String stateKey = current.getBoardState();
            if (visited.contains(stateKey)) continue;
            visited.add(stateKey);

            for (State next : current.generateNextStates(new HashSet<>(visited), "UCS(No Heuristic)")) {
                if (!visited.contains(next.getBoardState())) {
                    next.setNextCost(current.primaryDistanceToGoal(current.getBoard()));
                    next.setTotalCost(next.getPastCost());
                    queue.add(next);
                }
            }
        }
        return null;
    }
}