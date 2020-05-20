package com.example.cinesaragon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.cinesaragon.Adapter.MisEntradasAdapter;
import com.example.cinesaragon.model.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisEntradasActivity extends AppCompatActivity {

    private static List<Ticket> list;
    private static ListView entradasListView;

    private static FirebaseDatabase db;
    private static DatabaseReference ref;
    private static FirebaseUser currentUser;

    private MisEntradasAdapter entradasAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_entradas);

//        loadEntradas();
        entradasListView = findViewById(R.id.entradas_ListView);

        entradasAdapter = new MisEntradasAdapter(this, list);
        entradasListView.setAdapter(entradasAdapter);

    }



    public static void loadEntradas(){

        list = new ArrayList<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();
        db = FirebaseDatabase.getInstance();
        ref = db.getReference("Datos de usuario").child("Entradas");
        Query q = ref.orderByChild("userID").equalTo(uid);

        System.out.println("HELLO?SSAA");
        System.out.println("uid" + uid);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println("HELLO? 2");

//                GenericTypeIndicator<HashMap<String, Object>> objectsGTypeInd = new GenericTypeIndicator<HashMap<String, Object>>() {};
//                Map<String, Object> td = dataSnapshot.getValue(objectsGTypeInd);
//                list = new ArrayList<Ticket>(td.values());
//                list = (List) dataSnapshot.getChildren();
//                System.out.println(list.get(0).getNombre());
//                System.out.println(list.size());

                System.out.println("count" + dataSnapshot.getChildrenCount());

                for (DataSnapshot ticketSnapshot : dataSnapshot.getChildren()){
                    System.out.println("HELLO? 3");

                    Ticket t = ticketSnapshot.getValue(Ticket.class);
                    list.add(t);
                    System.out.println("ticket " + t.getPelicula());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



}
