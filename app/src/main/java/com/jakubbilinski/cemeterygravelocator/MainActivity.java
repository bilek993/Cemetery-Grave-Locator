package com.jakubbilinski.cemeterygravelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButtonAdd;
    private RecyclerView recyclerView;
    private GravesAdapter gravesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFloatingActionButton();
        setRecyclerView();
    }

    private void setFloatingActionButton() {
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddGraveActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //Temporary list START
        List<Grave> graveList = new ArrayList<Grave>();
        for (int i = 0; i < 100; i++) {
            Grave graveTmp = new Grave("John Smith", "21.04.1980", "01.01.1999");
            graveList.add(graveTmp);
        }
        //Temporary list END

        gravesAdapter = new GravesAdapter(this,graveList);
        recyclerView.setAdapter(gravesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
}
