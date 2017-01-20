package com.jakubbilinski.cemeterygravelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final int ADD_REQUEST_ID = 92;

    private FloatingActionButton floatingActionButtonAdd;
    private RecyclerView recyclerView;
    private GravesAdapter gravesAdapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        setFloatingActionButton();
        setRecyclerView();
        PermissionChecker.checkForPermissions(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menuSearcher).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    gravesAdapter.getFilter().filter("");
                } else {
                    gravesAdapter.getFilter().filter(newText);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setFloatingActionButton() {
        floatingActionButtonAdd = (FloatingActionButton) findViewById(R.id.floatingActionButtonAdd);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddGraveActivity.class);
                startActivityForResult(intent,ADD_REQUEST_ID);
            }
        });
    }

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        gravesAdapter = new GravesAdapter(this,this,db.getAllGraves());
        recyclerView.setAdapter(gravesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_REQUEST_ID) {
            gravesAdapter.refreshData(db.getAllGraves());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
