package com.example.darren.myreader.screens.update

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.darren.myreader.components.RatingBar
import com.example.darren.myreader.components.ReaderAppBar
import com.example.darren.myreader.data.DataOrException
import com.example.darren.myreader.model.MBook
import com.example.darren.myreader.navigation.ReaderScreens
import com.example.darren.myreader.screens.home.HomeScreenViewModel
import com.example.darren.myreader.screens.login.InputField
import com.example.darren.myreader.utils.formatDate

@Composable
fun ReaderUpdateScreen(
    navController: NavHostController,
    googleBookId: String,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Update Book",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false
            ){
                navController.navigate(ReaderScreens.ReaderHomeScreen.name)
            }
        }
    ) {
        Log.d("ABC",googleBookId)
        Surface(modifier = Modifier.padding(it)) {
            val bookInfo
            = produceState<DataOrException<List<MBook>, Boolean, Exception>>(
                initialValue = DataOrException(emptyList(), true, Exception("")),
                ){
                    value = viewModel.data.value

                }.value


            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookInfo.loading == true){
                    CircularProgressIndicator()
                    bookInfo.loading = false
                } else{
                    val book = viewModel.data.value.data!!.first{
                            mBook ->
                        mBook.googleBookId == googleBookId
                    }
                    Surface(
                        modifier = Modifier
                            .padding(2.dp)
                            .fillMaxWidth(),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        ShowBookUpdate(
                            book = book,
                            navController = navController
                        )

                    }
                    ShowSimpleForm(
                        book = book,
                        navController = navController
                    )

                    StartReadingRow(book)
                    RatingArea(book){

                    }


                }

            }
        }
        
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingArea(
    book: MBook,
    onRating: (Int) -> Unit
) {
    val ratingVal = remember{
        mutableStateOf(0)
    }
    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    book.rating?.toInt().let {
        RatingBar(rating = it!!) { rating ->
            ratingVal.value = rating
            onRating(rating)
            Log.d("TAG", "ShowSimpleForm: ${ratingVal.value}")
        }
    }
}

@Composable
fun StartReadingRow(book: MBook) {
    val isStartedReading = remember{
        mutableStateOf(false)
    }
    val isFinishedReading = remember{
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = {
                isStartedReading.value = true
            },
            enabled = book.startedReading == null
        ) {
            if(book.startedReading == null){
                if (!isStartedReading.value){
                    Text(text = "Start Reading")
                } else{
                    Text(
                        text = "Started Reading",
                        modifier = Modifier,
                        color = Color.Red.copy(alpha = 0.5f)
                    )
                }
            } else{
                Text("Started on: ${formatDate(book.startedReading!!)}")
            }
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(
                onClick = {
                    isFinishedReading.value = true
                },
                enabled = book.finishedReading == null,
            ) {
                if (book.finishedReading == null) {
                    if (!isFinishedReading.value) {
                        Text(text = "Mark as Read")
                    }else {
                        Text(
                            text = "Finish Reading",
                            modifier = Modifier,
                            color = Color.Red.copy(alpha = 0.5f)
                        )
                    }
                }else {
                    Text(text = "Finished on: ${formatDate(book.finishedReading!!)}")
                }

            }
            
        }
        
    }
    
}


@Composable
fun ShowBookUpdate(
    book: MBook,
    navController: NavHostController
){
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            BookCard(book = book) {
                //TODO:
            }


        }
    

}

@Composable
fun BookCard(book: MBook, onPressDetails: () -> Unit){
    AsyncImage(
        model = book.photoUrl,
        contentDescription = "",
        modifier = Modifier.size(120.dp)
    )
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = book.title.toString(),
            style = MaterialTheme.typography.h6
        )
        val authors = book.authors.toString()
        val displayAuthors = authors.substring(1,authors.length-1)
        Text(
            text = displayAuthors,
            style = MaterialTheme.typography.subtitle1
        )
        Text(
            text = book.publishedDate.toString(),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun ShowSimpleForm(
    book: MBook,
    navController: NavHostController
){
    val noteText = remember{
        mutableStateOf("")
    }
    SimpleForm(
        defaultValue = if (book.notes.toString().isNotEmpty()) book.notes.toString()
            else "No thoughts available"
    ){
        note ->
        noteText.value = note
    }



}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SimpleForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    defaultValue: String = "Great Book!",
    onSearch: (String) -> Unit
){
    val texFieldValue = rememberSaveable{
        mutableStateOf(defaultValue)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(texFieldValue.value){
        texFieldValue.value.trim().isNotEmpty()
    }
    InputField(
        modifier = Modifier
            .height(140.dp)
            .padding(4.dp)
            .background(Color.White, CircleShape)
            .padding(horizontal = 20.dp),
        valueState = texFieldValue,
        labelId = "Enter your thoughts",
        enabled = true,
        onAction = KeyboardActions {
            if(!valid){
                return@KeyboardActions
            }
            onSearch(texFieldValue.value.trim())
            keyboardController?.hide()
        }
    )

}