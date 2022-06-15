package com.example.darren.myreader.screens.update

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.darren.myreader.components.RatingBar
import com.example.darren.myreader.components.ReaderAppBar
import com.example.darren.myreader.components.ShowAlertDialog
import com.example.darren.myreader.data.DataOrException
import com.example.darren.myreader.model.MBook
import com.example.darren.myreader.navigation.ReaderScreens
import com.example.darren.myreader.screens.home.HomeScreenViewModel
import com.example.darren.myreader.screens.home.RoundedButton
import com.example.darren.myreader.screens.login.InputField
import com.example.darren.myreader.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
                        (mBook.userId == FirebaseAuth.getInstance().currentUser?.uid) && (mBook.googleBookId == googleBookId)
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ShowSimpleForm(
    book: MBook,
    navController: NavHostController
){
    val notesText = remember{
        mutableStateOf("")
    }
    val isStartedReading = remember{
        mutableStateOf(false)
    }
    val isFinishedReading = remember{
        mutableStateOf(false)
    }
    val ratingVal = remember{
        mutableStateOf(0)
    }
    val context = LocalContext.current

    SimpleForm(
        defaultValue = if (book.notes.toString().isNotEmpty()) book.notes.toString()
            else "No thoughts available"
    ){
        notes ->
        notesText.value = notes
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

    Text(text = "Rating", modifier = Modifier.padding(bottom = 3.dp))
    book.rating?.toInt().let {
        RatingBar(rating = it!!) { rating ->
            ratingVal.value = rating
            Log.d("TAG", "ShowSimpleForm: ${ratingVal.value}")
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val changedNotes = book.notes != notesText.value
        val changedRating = book.rating?.toInt() != ratingVal.value
        val isFinishedTimeStamp =
            if (isFinishedReading.value) Timestamp.now()
            else book.finishedReading
        val isStartedTimeStamp =
            if (isStartedReading.value) Timestamp.now()
            else book.startedReading
        val bookUpdate = changedNotes || changedRating || isStartedReading.value || isFinishedReading.value
        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value).toMap()
        RoundedButton(
            label = "Update"
        ){
            if(bookUpdate){
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .update(bookToUpdate)
                    .addOnCompleteListener {
                        task ->
                        Log.d("ReaderUpdateScreen/ShowSimpleForm","OnComplete: ${task.result.toString()}")
                        Toast.makeText(
                            context,
                            "Updated Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                    }
                    .addOnFailureListener {
                        exception ->
                        Log.d("ReaderUpdateScreen/ShowSimpleForm","OnFailure: $exception")
                    }
            }
        }
        Spacer(modifier = Modifier.width(20.dp))
        val openDialog = remember{
            mutableStateOf(false)
        }
        if (openDialog.value){
            ShowAlertDialog(
                message = "Are you sure you want to delete this book?",
                openDialog = openDialog
            ) {
                FirebaseFirestore.getInstance()
                    .collection("books")
                    .document(book.id!!)
                    .delete()
                    .addOnCompleteListener {
                        if(it.isSuccessful){
                            openDialog.value = false
                            navController.navigate(ReaderScreens.ReaderHomeScreen.name)
                        }
                    }

            }
        }

        RoundedButton(
            label = "Delete"
        ){
            openDialog.value = true
        }
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