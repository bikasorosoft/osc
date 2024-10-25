package io.osc.bikas.kafka.schedule;

import io.osc.bikas.kafka.service.ViewHistoryInteractiveQuery;
import io.osc.bikas.model.ViewHistory;
import io.osc.bikas.service.ViewDataService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.json.JSONArray;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;

//@Component
@RequiredArgsConstructor
public class ViewDataScheduledTask {

    private final ViewHistoryInteractiveQuery interactiveQueryService;
    private final ViewDataService viewDataService;

    @Scheduled(fixedRate = 300_000, initialDelay = 60_000)
    public void updateViewDataToDb() {
        KeyValueIterator<String, LinkedList<String>> keyValueIterator = interactiveQueryService.getAll();

        ArrayList<ViewHistory> viewHistoryList = new ArrayList<>();

        while (keyValueIterator.hasNext()) {
            KeyValue<String, LinkedList<String>> keyValue =
                    keyValueIterator.next();
            viewHistoryList.add(generateViewHistory(keyValue.key, keyValue.value));
        }

        viewDataService.saveUserViewData(viewHistoryList);

    }

    private ViewHistory generateViewHistory(String userId, LinkedList<String> productIds) {
        return new ViewHistory(userId, productIds.toString());
    }

}
