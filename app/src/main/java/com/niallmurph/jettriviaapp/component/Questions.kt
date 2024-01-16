package com.niallmurph.jettriviaapp.component

import android.util.Log
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.niallmurph.jettriviaapp.screens.QuestionsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    runBlocking {
        delay(1000)
    }
    if(viewModel.data.value.loading == true){
        CircularProgressIndicator()
        Log.d("Q_LIST_SIZE", "Size = Loading")
    } else {
        questions?.forEach {
            Log.d("QUESTION", it.question)
        }
    }
    Log.d("Q_LIST_SIZE", "Size = ${questions?.size}")
}