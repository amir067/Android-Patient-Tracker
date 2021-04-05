package com.my.AndroidPatientTracker.ui.StartTest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.my.AndroidPatientTracker.R;
import com.my.AndroidPatientTracker.adapters.StatementAdapter;
import com.my.AndroidPatientTracker.models.StatementModel;
import com.my.AndroidPatientTracker.utils.Constants;
import com.my.AndroidPatientTracker.utils.Tools;


public class StartTestFragment extends Fragment implements StatementAdapter.OnItemClickListener {

    private static final String TAG = "StartTestFragment";
    private Button prev, next;
    private StatementAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef;
    private RecyclerView recyclerView;
    private int position = 0;
    private int i0 = 0, i1 = 0, i2 = 0, i3 = 0;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Window window =getActivity().getWindow();
        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Tools.setSystemBarLight(requireActivity());
        //        //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //        //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start_test, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prev = view.findViewById(R.id.prevbtn);
        next = view.findViewById(R.id.nextbtn);
        recyclerView = view.findViewById(R.id.recycler_statement);
        navController = Navigation.findNavController(view);

        setUpRecyclerView();


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(my.personal.psychiatrist.ui.play.StatementActivity.this, HomeActivity.class));
                navController.popBackStack();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //startActivity(new Intent(my.personal.psychiatrist.ui.play.StatementActivity.this, NatureActivity.class));
            }
        });

//        done.setOnClickListener(v -> startActivity(new Intent(StatementActivity.this, NatureActivity.class)));
//
//        menue.setOnClickListener(v -> startActivity(new Intent(StatementActivity.this, HomeActivity.class)));

    }


    private void setUpRecyclerView() {

        Log.d(TAG, "setUpRecyclerView: " + position);

        switch (position) {
            case 0:
                Log.i(TAG, "collection:statement ?data: ");
                blogsRef = db.collection("statement");
                break;
            case 1:
                blogsRef = db.collection("statement1");
                break;
            case 2:
                blogsRef = db.collection("statement2");
                break;
            case 3:
                blogsRef = db.collection("statement3");
                break;
        }

        Query query = blogsRef.orderBy("question", Query.Direction.DESCENDING);

        adapter = new StatementAdapter(query, this) {

            @Override
            protected void onDataChanged() {
                notifyDataSetChanged();
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e(TAG, "onError: "+e.getLocalizedMessage() );
            }
        };

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot) {


        if (position == 4) {
            Constants.statement_score = i0 + i1 + i2 + i3;
            Log.d(TAG, "onItemClick: " + Constants.statement_score);


            Log.d(TAG, "position=4 ok : " + position);

            //startActivity(new Intent(this,NatureActivity.class));
            //finish();

            navController.navigate(R.id.action_startTestFragment_to_natureFragment);
            //navController.popBackStack();

        } else {

            StatementModel model = snapshot.toObject(StatementModel.class);

            if (position == 0) {
                i0 = model.getScores();
                Log.d(TAG, "question 1 => " + model.getScores());
            } else if (position == 1) {
                i1 = model.getScores();
                Log.d(TAG, "question 2 => " + model.getScores());
            } else if (position == 2) {
                i2 = model.getScores();
                Log.d(TAG, "question 3 => " + model.getScores());
            } else if (position == 3) {
                i3 = model.getScores();
                Log.d(TAG, "question 4 => " + model.getScores());
            }

            position++;
            setUpRecyclerView();

        }


//        if (position == 0) {
//            i0 = model.getScores();
//            Log.d(TAG, "onItemClick: " + i0);
//        }
//
//        switch (position) {
//            case 0:
//                i0 = model.getScores();
//                break;
//            case 1:
//                i1 = model.getScores();
//                break;
//            case 2:
//                i2 = model.getScores();
//                break;
//            case 3:
//                i3 = model.getScores();
//                break;
//            case 4:
//                Constants.statement_score = i0 + i1 + i2 + i3;
//                Log.d(TAG, "onItemClick: " + Constants.statement_score);
//                break;
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
