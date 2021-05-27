package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.model.Question;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    public ArrayList<Question> questionArrayList = new ArrayList<>();
    private final String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    public List<Question> getQuestions(final AnswerListAsyncResponse callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONArray questionArray = response.getJSONArray(i);

                            Question question = new Question(questionArray.getString(0),
                                    questionArray.getBoolean(1));

                            questionArrayList.add(question);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if(callback != null) callback.processFinished(questionArrayList);
                },
                error -> {
                    Log.d("MainActivity", "error: " + error);
                }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;
    }

}
