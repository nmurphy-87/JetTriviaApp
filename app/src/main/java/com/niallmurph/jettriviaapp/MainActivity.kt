package com.niallmurph.jettriviaapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.niallmurph.jettriviaapp.screens.QuestionsViewModel
import com.niallmurph.jettriviaapp.ui.theme.JetTriviaAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetTriviaAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TriviaHome()
                }
            }
        }
    }
}

@Composable
fun TriviaHome(viewModel : QuestionsViewModel = hiltViewModel()){
    Questions(viewModel)
}

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    runBlocking {
        delay(1000)
    }
    if(viewModel.data.value.loading == true){
        Log.d("Q_LIST_SIZE", "Size = Loading")
    } else {
        questions?.forEach {
            Log.d("QUESTION", it.question)
        }
    }
    Log.d("Q_LIST_SIZE", "Size = ${questions?.size}")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetTriviaAppTheme {
    }
}