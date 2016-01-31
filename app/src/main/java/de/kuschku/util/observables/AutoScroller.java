package de.kuschku.util.observables;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class AutoScroller {
    private final RecyclerView recyclerView;
    private final LinearLayoutManager manager;

    public AutoScroller(@NonNull RecyclerView recyclerView) {
        this(recyclerView, (LinearLayoutManager) recyclerView.getLayoutManager());
    }

    public AutoScroller(RecyclerView recyclerView, LinearLayoutManager manager) {
        this.recyclerView = recyclerView;
        this.manager = manager;
    }

    public void notifyScroll() {
        if (manager.findFirstVisibleItemPosition() == 0)
            manager.smoothScrollToPosition(recyclerView, null, 0);
    }
}
