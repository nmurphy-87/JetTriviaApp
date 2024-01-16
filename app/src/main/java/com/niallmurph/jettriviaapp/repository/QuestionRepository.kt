package com.niallmurph.jettriviaapp.repository

import android.util.Log
import com.niallmurph.jettriviaapp.data.DataOrException
import com.niallmurph.jettriviaapp.model.QuestionItem
import com.niallmurph.jettriviaapp.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val api: QuestionApi) {

    private val dataOrException = DataOrException<ArrayList<QuestionItem>, Boolean, Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if(dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (exception: Exception) {
            dataOrException.e = exception
            Log.d("Data Exception", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        }
        return dataOrException
    }
}