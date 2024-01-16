package com.niallmurph.jettriviaapp.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.niallmurph.jettriviaapp.component.Questions

@Composable
fun TriviaHome(viewModel : QuestionsViewModel = hiltViewModel()){
    Questions(viewModel)
}