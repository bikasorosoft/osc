package io.osc.bikas.service;

import io.osc.bikas.kafka.service.ViewHistoryInteractiveQuery;
import io.osc.bikas.model.ViewHistory;
import io.osc.bikas.repo.ViewHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViewDataService {

    private final ViewHistoryInteractiveQuery viewHistoryInteractiveQuery;
    private final ViewHistoryRepository viewHistoryRepository;

    public List<String> getRecentlyViewedProductIdListBy(String userId) {
        return viewHistoryInteractiveQuery.get(userId);
    }

    public void saveUserViewData(List<ViewHistory> viewHistories) {
        viewHistoryRepository.saveAll(viewHistories);
    }

    public void saveUserViewData(String userId) {
        final LinkedList<String> viewHistory = viewHistoryInteractiveQuery.get(userId);
        if (viewHistory == null || viewHistory.isEmpty()) {
            return;
        }
        viewHistoryRepository.save(new ViewHistory(userId, viewHistory.toString()));
    }

}
