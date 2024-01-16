package com.niallmurph.jettriviaapp.component

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niallmurph.jettriviaapp.screens.QuestionsViewModel
import com.niallmurph.jettriviaapp.util.AppColours
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

@Preview
@Composable
fun QuestionDisplay(){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(4.dp),
        color = AppColours.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            QuestionTracker()
        }
    }
}

@Preview
@Composable
fun QuestionTracker(
    counter : Int = 10,
    listSize : Int = 100
){
    Text(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(
                textIndent = TextIndent.None
            )) {
                withStyle(
                    style = SpanStyle(
                        color = AppColours.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                ) {
                    append("Question $counter /")

                    withStyle(
                        style = SpanStyle(
                            color = AppColours.mLightGray,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    ) {
                        append(" $listSize")
                    }
                }
            }
        },
        modifier = Modifier.padding(18.dp)
    )
}