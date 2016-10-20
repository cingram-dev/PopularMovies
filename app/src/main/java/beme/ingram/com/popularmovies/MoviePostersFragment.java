package beme.ingram.com.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import beme.ingram.com.popularmovies.asyncs.MoviePosterTask;
import beme.ingram.com.popularmovies.models.Movie;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePostersFragment extends Fragment {

    private static final String IMDB_URL = "http://api.themoviedb.org/3";
    private static final String POPULAR_URL = "/movie/popular";
    private static final String TOP_RATED_URL = "/movie/top_rated";
    private static final String API_URL = "?api_key=";
    private static final String API_KEY = "";

    ArrayList<Movie> movies;

    View rootView;
    @BindView(R.id.poster_recycler)RecyclerView posterRecycler;



    public MoviePostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.movie_poster_layout, container, false);
        ButterKnife.bind(this,rootView);

        movies = new ArrayList<>();

        RunVolley();
        new MoviePosterTask(getActivity()).execute();
        posterRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        posterRecycler.setHasFixedSize(true);

        return rootView;
    }

    private void RunVolley() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, IMDB_URL + POPULAR_URL + API_URL + API_KEY, null, new Response.Listener<JSONObject>() {

                    JSONArray ja_data;
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                             ja_data = response.getJSONArray("results");
                            int length = response.length();


                            for(int i=0; i<length; i++)
                            {
                                JSONObject jObj = ja_data.getJSONObject(i);
                                movies.add(new Movie(jObj));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });

// Access the RequestQueue through your singleton class.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jsObjRequest);
    }
}