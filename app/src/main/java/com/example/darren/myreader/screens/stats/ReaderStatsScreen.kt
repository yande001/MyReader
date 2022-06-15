package com.example.darren.myreader.screens.stats

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.darren.myreader.components.ReaderAppBar
import com.example.darren.myreader.model.MBook
import com.example.darren.myreader.screens.home.HomeScreenViewModel
import com.example.darren.myreader.screens.search.BookRow
import com.example.darren.myreader.screens.search.BookSearchViewModel
import com.example.darren.myreader.utils.formatDate
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@Composable
fun ReaderStatsScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    var books: List<MBook>
    val currentUser = FirebaseAuth.getInstance().currentUser
    
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Stats",
                icon = Icons.Default.ArrowBack ,
                navController = navController,
                showProfile = false
            ){
                navController.popBackStack()
            }
            
        }
    ) {
        Surface(modifier = Modifier.padding(it)) {
            books = if (!viewModel.data.value.data.isNullOrEmpty()) {
                viewModel.data.value.data!!.filter { mBook ->
                    (mBook.userId == currentUser?.uid)
                }
            }else {
                emptyList()
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .padding(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Person,
                            contentDescription = ""
                        )

                    }

                    val userName = currentUser?.email.toString().split("@")[0].uppercase(Locale.getDefault())
                    Text(
                        text = "Hi, $userName",
                        fontStyle = FontStyle.Italic,
                        fontSize = 18.sp
                    )
                }
                Card(
                    modifier = Modifier.padding(6.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.Gray),
                    elevation = 4.dp
                ) {
                    val readBookList: List<MBook>
                    = if (!viewModel.data.value.data.isNullOrEmpty()){
                        books.filter {
                            mBook ->
                            mBook.userId == currentUser?.uid && mBook.finishedReading != null
                        }
                    } else{
                        emptyList()
                    }

                    val readingBooks = books.filter {
                        mBook ->
                        mBook.startedReading != null && mBook.finishedReading == null
                    }

                    Column(
                        modifier = Modifier.padding(8.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Your Stats",
                            style = MaterialTheme.typography.h6
                        )
                        Divider()
                        Text(
                            text = "You're reading: ${readingBooks.size} books",
                            style = MaterialTheme.typography.subtitle1
                        )
                        Text(
                            text = "You've read: ${readBookList.size} books",
                            style = MaterialTheme.typography.subtitle1
                        )



                    }

                }

                if(viewModel.data.value.loading == true){
                    CircularProgressIndicator()
                } else{
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Finished Reading Books:")
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp)
                    ){
                        val readBookList: List<MBook>
                                = if (!viewModel.data.value.data.isNullOrEmpty()){
                            books.filter {
                                    mBook ->
                                mBook.userId == currentUser?.uid && mBook.finishedReading != null
                            }
                        } else{
                            emptyList()
                        }
                        items(readBookList){
                            mBook ->
                            StatsBookRow(mBook)
                        }


                    }
                }

            }
        }
    }
}

@Composable
fun StatsBookRow(
    book: MBook
){
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(125.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {

            AsyncImage(
                model = book.photoUrl,
                contentDescription = "",
                modifier = Modifier.size(120.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = book.title.toString(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.h6
                )
                Text(
                    text = "finished: ${book.finishedReading?.let { formatDate(it) }}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.subtitle1
                )
                Text(
                    text = "review: ${book.notes}",
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                    style = MaterialTheme.typography.subtitle1,
                )
                Text(
                    text = "rating: ${book.rating?.toInt().toString()}/5",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }

    }

}
