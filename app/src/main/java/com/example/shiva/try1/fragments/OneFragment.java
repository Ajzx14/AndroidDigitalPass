package com.example.shiva.try1.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shiva.try1.Contact;
import com.example.shiva.try1.ContactsAdapter;
import com.example.shiva.try1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import info.androidhive.materialtabs.R;


public class OneFragment extends Fragment implements ContactsAdapter.ContactsAdapterListener {
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    //private List<Contact> contactList = new ArrayList<>();
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    SearchView searchbar;
    String stand_,address_profile,parents_occ,colleg_value,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
    Bitmap Image_list=null;
    StorageReference storageReference;
    FirebaseStorage storage;
    Bitmap bitmap,bitmap1;
    CardView card_view;
    private RelativeLayout mRelativeLayout;
    private PopupWindow mPopupWindow;
    Spinner college;
    public ImageView image=null;
    Button college_search;
    String fullname;
    StorageReference ref;
    File localFile = null;
    private Context mContext;
    private Activity mActivity;
    File finalLocalFile;
    Handler mVolHandler;
    Runnable mVolRunnable;
    android.widget.SearchView search;

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
         searchbar = view.findViewById(R.id.search_bar); // inititate a search view
        SearchManager searchManager = (SearchManager) this.getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchbar.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchbar.setMaxWidth(Integer.MAX_VALUE);
        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)this.getActivity()).setSupportActionBar(toolbar);

        // toolbar fancy stuff
        ((AppCompatActivity)this.getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)this.getActivity()).getSupportActionBar().setTitle("Search");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        recyclerView = view.findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(getContext(), contactList, this);
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), false));
        recyclerView.setAdapter(mAdapter);
        dataRetrive_new();
        return view;
    }

    @Override
    public void onContactSelected(Contact contact) {
        Toast.makeText(getContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        this.getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) this.getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void dataRetrive_new(){
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle("Featching Data...");
//        progressDialog.show();
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference zone1Ref = zonesRef.child("CHARUSAT");


        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finalLocalFile = localFile;

        zone1Ref.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    branch_profile =  postSnapshot.child("branch").getValue(String.class);

                    bustype_profile =  postSnapshot.child("bus type").getValue(String.class);

                    college_profile = postSnapshot.child("college").getValue(String.class);

                    email_profile = postSnapshot.child("email").getValue(String.class);

                    fname_profile = postSnapshot.child("first name").getValue(String.class);

                    lname_profile = postSnapshot.child("last name").getValue(String.class);

                    mname_profile = postSnapshot.child("middle name").getValue(String.class);

                    mobile_profile =(String) postSnapshot.child("mobile").getValue(String.class);

//                    ref.child("images/").getFile(localFile)
//                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                                    // Successfully downloaded data to local file
//                                    // ...
//                                    Image_list = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
//
//                                    //  photo.setImageBitmap(bitmap);
//                                    // progressDialog.dismiss();
//                                    // dataRetrive(imageId); return bitmap;
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception exception) {
//                            // Handle failed download
//                            // ...
//                        }
//                    });

                    paidtill_profile = postSnapshot.child("paid till").getValue(String.class);



                    route_profile = postSnapshot.child("route").getValue(String.class);

                    GoToImage(fname_profile,lname_profile,mname_profile,mobile_profile,email_profile,paidtill_profile,route_profile,bustype_profile,stand_,college_profile);


                }
                //  progressDialog.dismiss();
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(profile.this,""+databaseError,Toast.LENGTH_LONG).show();
            }
        });



    }

    private void GoToImage(final String fname_, final String lname_, final String mname_, final String mobile_, final String email_, final String paidtill_, final String route_, final String bustype_, final String stand_1,final String college_profile) {
        StorageReference islandRef = storageReference.child("images/"+mobile_);
        final long ONE_MEGABYTE = 1024 * 1024;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Image_list = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //   Movie data = new Movie(Image_list,fname_,lname_,mname_,mobile_,email_,paidtill_,route_,bustype_,stand_1,college_profile);
                fullname = fname_+"\t" + lname_;
                Contact data = new Contact(fullname,Image_list,mobile_,"CHARUSAT");


                contactList.add(data);
                mAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });




    }



    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.WHITE);
        }
    }
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
