package rushhours.algorithm;

import rushhours.model.*;
import java.util.*;

public class AStar {

    public static State solve(State initialState, String heuristicType) {
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::getTotalCost));
        Set<String> visited = new HashSet<>();

        initialState.setPastCost(0);
        initialState.setNextCost(initialState.getHeuristicValue(heuristicType, initialState.getBoard()));
        initialState.setTotalCost(initialState.getNextCost());

        queue.add(initialState);
        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.isGoal(current.getBoard())) {
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