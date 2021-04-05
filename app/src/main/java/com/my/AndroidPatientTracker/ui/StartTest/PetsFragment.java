package com.my.AndroidPatientTracker.ui.StartTest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.my.AndroidPatientTracker.adapters.NatureAdapter;
import com.my.AndroidPatientTracker.models.ImageModel;
import com.my.AndroidPatientTracker.utils.Constants;


public class PetsFragment extends Fragment implements NatureAdapter.OnItemClickListener{

    private static final String TAG = "PetsFragment";
    private NatureAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef = db.collection("pets");
    private RecyclerView recyclerView;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pets, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        recyclerView = view.findViewById(R.id.pets_recycler);
        setUpRecyclerView();


    }

    private void setUpRecyclerView() {

        Query query = blogsRef.orderBy("image", Query.Direction.DESCENDING);

        adapter = new NatureAdapter(query, this) {

            @Override
            protected void onDataChanged() {
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


    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot) {

        ImageModel model = snapshot.toObject(ImageModel.class);

        assert model != null;
        Constants.pets_score = model.getScores();


        //startActivity(new Intent(getApplicationContext(), FaceRecognizetion.class));
        //finish();
        navController.navigate(R.id.action_petsFragment_to_faceRecFragment);


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
