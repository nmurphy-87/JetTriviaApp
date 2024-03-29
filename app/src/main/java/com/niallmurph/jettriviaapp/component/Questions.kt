package com.niallmurph.jettriviaapp.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niallmurph.jettriviaapp.model.QuestionItem
import com.niallmurph.jettriviaapp.screens.QuestionsViewModel
import com.niallmurph.jettriviaapp.util.AppColours

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember { mutableStateOf(0) }

    if (viewModel.data.value.loading == true) {
        CircularProgressIndicator()
    } else {
        val question = try {
            questions?.get(questionIndex.value)
        } catch (ex: Exception) {
            null
        }
        if (questions != null) {
            QuestionDisplay(
                question = question!!,
                questionIndex = questionIndex,
                viewModel = viewModel,
                onPreviousClicked = {
                    questionIndex.value = questionIndex.value - 1
                }
            ) {
                questionIndex.value = questionIndex.value + 1
            }
        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
    ) {
        drawLine(
            color = AppColours.mLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, y = 0f),
            pathEffect = pathEffect
        )
    }
}

@Composable
fun QuestionDisplay(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    viewModel: QuestionsViewModel,
    onPreviousClicked: (Int) -> Unit = {},
    onNextClicked: (Int) -> Unit = {}
) {
    val choicesState = remember(question) { question.choices.toMutableList() }
    val answerState = remember(question) { mutableStateOf<Int?>(null) }
    val correctAnswerState = remember { mutableStateOf<Boolean?>(null) }
    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColours.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (questionIndex.value >= 3) ShowProgress(score = questionIndex.value)

            QuestionTracker(counter = questionIndex.value)
            DrawDottedLine(pathEffect = pathEffect)
            Column() {
                Text(
                    question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 16.sp,
                    color = AppColours.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
                //Choices
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .height(40.dp)
                            .border(
                                width = 4.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColours.mLightPurple,
                                        AppColours.mLightPurple
                                    ),
                                ), shape = RoundedCornerShape(12.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier
                                .padding(start = 16.dp),
                            colors = RadioButtonDefaults
                                .colors(
                                    selectedColor = if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green.copy(alpha = 0.2f)
                                    } else {
                                        Color.Red.copy(alpha = 0.2f)
                                    }
                                )
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green.copy(alpha = 0.2f)
                                    } else if (correctAnswerState.value == false && index == answerState.value) {
                                        Color.Red.copy(alpha = 0.2f)
                                    } else {
                                        AppColours.mOffWhite
                                    },
                                    fontSize = 16.sp
                                )
                            ) {
                                append(answerText)
                            }
                        }
                        Text(annotatedString)
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            onPreviousClicked(questionIndex.value)
                        },
                        modifier = Modifier
                            .padding(4.dp),
                        shape = RoundedCornerShape(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColours.mLightBlue,
                        )
                    ) {
                        Text(
                            text = "Previous",
                            modifier = Modifier
                                .padding(4.dp),
                            color = AppColours.mOffWhite,
                            fontSize = 16.sp
                        )
                    }
                    Button(
                        onClick = {
                            onNextClicked(questionIndex.value)
                        },
                        modifier = Modifier
                            .padding(4.dp),
                        shape = RoundedCornerShape(36.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppColours.mLightBlue,
                        )
                    ) {
                        Text(
                            text = "Next",
                            modifier = Modifier
                                .padding(4.dp),
                            color = AppColours.mOffWhite,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun QuestionTracker(
    counter: Int = 10,
    listSize: Int = 100
) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = ParagraphStyle(
                    textIndent = TextIndent.None
                )
            ) {
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

@Preview
@Composable
fun ShowProgress(score: Int = 12) {

    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075), Color(0xFFBE6BE5)))
    val progressFactor = remember(score) { mutableStateOf(score * 0.005f) }

    Row(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(40.dp)
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColours.mLightPurple, AppColours.mLightPurple
                    )
                ),
                shape = RoundedCornerShape(33.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth(progressFactor.value)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = "${score*10}",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColours.mOffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}