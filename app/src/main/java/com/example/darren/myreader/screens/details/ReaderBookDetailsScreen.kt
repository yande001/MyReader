package com.example.darren.myreader.screens.details

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.substring
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.darren.myreader.components.ReaderAppBar
import com.example.darren.myreader.data.Resource
import com.example.darren.myreader.model.Item
import com.example.darren.myreader.model.MBook
import com.example.darren.myreader.navigation.ReaderScreens
import com.example.darren.myreader.screens.home.RoundedButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ReaderBookDetailsScreen(
    navController: NavHostController,
    bookId: String,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Book Details",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false,
            ){
                navController.popBackStack()
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            val bookInfo = produceState<Resource<Item>>(initialValue = Resource.Loading()){
                value = viewModel.getBookInfo(bookId)
            }.value
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(bookInfo.data == null){
                    CircularProgressIndicator()
                } else{
                    ShowBookDetails(bookInfo = bookInfo, navController = navController)
                }

            }

        }
    }
}

@Composable
fun ShowBookDetails(
    bookInfo: Resource<Item>,
    navController: NavHostController
){
    val bookData = bookInfo.data?.volumeInfo
    val googleBookId = bookInfo.data?.id
    Card(
        modifier = Modifier.padding(16.dp),
        shape = RectangleShape,
        elevation = 4.dp
    ) {
        val imgUrl: String = if (bookData?.imageLinks != null) {
            bookData.imageLinks.thumbnail
        } else {
            "https://img.icons8.com/ios-filled/250/undefined/android-os.png"
        }
        AsyncImage(
            model = imgUrl,
            contentDescription = "",
            modifier = Modifier.size(150.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val categories = bookData?.categories.toString()
        val displayCategories = categories.substring(1,categories.length-1)
        Text(
            text = displayCategories,
            style = MaterialTheme.typography.subtitle2,
            color = Color.Blue.copy(alpha = 0.8f)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = bookData?.title.toString(),
            style = MaterialTheme.typography.h6,
            color = Color.Black
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        val authors = bookData?.authors.toString()
        val displayAuthors = authors.substring(1,authors.length-1)
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        text ="Authors: ",
                        spanStyle = SpanStyle(
                            color = Color.Black,
                        ),
                    )
                )
                append(
                    AnnotatedString(
                        text = displayAuthors,
                        spanStyle = SpanStyle(
                            color = Color.Blue,
                        )
                    )
                )
            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        text ="Publisher: ",
                        spanStyle = SpanStyle(
                            color = Color.Black,
                        ),
                    )
                )
                append(
                    AnnotatedString(
                        text = bookData?.publisher.toString(),
                        spanStyle = SpanStyle(
                            color = Color.Blue,
                        )
                    )
                )
            }
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = buildAnnotatedString {
                append(
                    AnnotatedString(
                        text ="Published Date: ",
                        spanStyle = SpanStyle(
                            color = Color.Black,
                        ),
                    )
                )
                append(
                    AnnotatedString(
                        text = bookData?.publishedDate.toString(),
                        spanStyle = SpanStyle(
                            color = Color.Blue,
                        )
                    )
                )
            }
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    val cleanDescription = HtmlCompat.fromHtml(
        bookData?.description.toString(),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    ).toString()
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.75f),
        shape = RectangleShape,
        border = BorderStroke(1.dp,Color.Gray)
    ) {
        LazyColumn{
            item{
                Text(
                    text = cleanDescription,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundedButton(label = "Save"){
            val book = MBook(
                title = bookData?.title,
                authors = bookData?.authors.toString(),
                description = bookData?.description.toString(),
                categories = bookData?.categories.toString(),
                notes = "",
                photoUrl = if (bookData?.imageLinks != null) {
                    bookData.imageLinks.thumbnail
                } else {
                    "https://img.icons8.com/ios-filled/250/undefined/android-os.png"
                },
                publishedDate = bookData?.publishedDate,
                pageCount = bookData?.pageCount.toString(),
                publisher = bookData?.publisher.toString(),
                rating = 0.0,
                googleBookId = googleBookId,
                userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
            )
            saveToFirebase(book = book, navController = navController)

        }
        Spacer(modifier = Modifier.width(20.dp))
        RoundedButton(label = "Cancel"){
            navController.popBackStack()
        }

    }


}


fun saveToFirebase(
    book: MBook,
    navController: NavHostController
){
    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if(book.toString().isNotEmpty()){
        dbCollection.add(book)
            .addOnSuccessListener {
                documentRef ->
                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String, Any>)
                    .addOnCompleteListener {
                        task ->
                        if (task.isSuccessful){
                            navController.popBackStack()
                        }
                    }
                    .addOnFailureListener {
                        Log.w("SaveToFirebase","Error updating: $it")
                    }
            }
    } else{

    }
}