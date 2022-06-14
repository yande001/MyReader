package com.example.darren.myreader.screens.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.darren.myreader.components.ReaderAppBar
import com.example.darren.myreader.model.MBook
import com.example.darren.myreader.screens.login.InputField
import com.example.darren.myreader.R
import com.example.darren.myreader.model.Item
import com.example.darren.myreader.navigation.ReaderScreens

@Preview
@Composable
fun ReaderSearchScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: BookSearchViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "Search Books",
                navController = navController,
                icon = Icons.Default.ArrowBack,
                showProfile = false
            ){
                navController.navigate(ReaderScreens.ReaderHomeScreen.name)
            }
        } 
    ) {
        Surface(modifier = Modifier.padding(it)) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    viewModel = viewModel
                ){
                    query ->
                    viewModel.searchBook(query)
                }
                Spacer(modifier = Modifier.height(12.dp))
                BookList(
                    navController = navController,
                    viewModel = viewModel
                )

            }
        }
        
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    viewModel: BookSearchViewModel,
    loading: Boolean = false,
    hint: String = "Search",
    onSearch: (String) -> Unit = {}
){
    Column(modifier = modifier){
        val searchQueryState = rememberSaveable{
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value){
            searchQueryState.value.trim().isNotEmpty()
        }
        InputField(
            valueState = searchQueryState,
            labelId = "Search",
            enabled = true,
            isSingleLine = true,
            onAction = KeyboardActions{
                Log.d("TEST1","$valid, ${searchQueryState.value}")
                if(!valid) return@KeyboardActions
                onSearch(searchQueryState.value.trim())
                searchQueryState.value = ""
                keyboardController?.hide()
                Log.d("TEST2","$valid, ${searchQueryState.value}")
            }
        )
    }
}

@Composable
fun BookList(
    navController: NavHostController,
    viewModel: BookSearchViewModel = hiltViewModel()
){
    val listOfBooks = viewModel.list
    if (listOfBooks.isNotEmpty()){
        Log.d("TESTING", listOfBooks.size.toString())
    }

    if(viewModel.isLoading){
        LinearProgressIndicator()
    } else{
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ){
            items(listOfBooks){
                    item ->
                BookRow(item, navController)
            }

        }
    }
}

@Composable
fun BookRow(item: Item, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(125.dp)
            .clickable {
                navController.navigate(ReaderScreens.DetailsScreen.name + "/${item.id}")
            },
        shape = RectangleShape,
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            val imgUrl: String = if (item.volumeInfo.imageLinks != null) {
                item.volumeInfo.imageLinks.thumbnail
            } else {
                "https://img.icons8.com/external-flat-icons-inmotus-design/67/undefined/external-android-android-ui-flat-icons-inmotus-design-18.png"
            }


            AsyncImage(
                model = imgUrl,
                contentDescription = "",
                modifier = Modifier.size(120.dp)
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.volumeInfo.title,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Author: ${item.volumeInfo.authors}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "Date: ${item.volumeInfo.publishedDate}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = "${item.volumeInfo.categories}",
                    overflow = TextOverflow.Clip,
                    style = MaterialTheme.typography.caption,
                    fontStyle = FontStyle.Italic
                )

                //TODO: add more fields later

            }
        }

    }
}
