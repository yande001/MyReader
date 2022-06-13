package com.example.darren.myreader.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.darren.myreader.R
import com.example.darren.myreader.components.ReaderAppBar
import com.example.darren.myreader.model.MBook
import com.example.darren.myreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderHomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            ReaderAppBar(
                title = "MyReader",
                navController = navController
            )
        },
        floatingActionButton = {
            FABContent({
               navController.navigate(ReaderScreens.SearchScreen.name)
            })
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            HomeContent(navController = navController)
        }
    }
}

@Composable
fun HomeContent(
    navController: NavHostController = rememberNavController()
){
    val listOfBooks = listOf<MBook>(
        MBook(id = "bk001", title = "Book One", authors = "Author One", notes = "Good morning."),
        MBook(id = "bk002", title = "Book Two", authors = "Author One", notes = "Good morning."),
        MBook(id = "bk003", title = "Book Three", authors = "Author One", notes = "Good morning."),
        MBook(id = "bk004", title = "Book Four", authors = "Author One", notes = "Good morning."),
        MBook(id = "bk005", title = "Book Five", authors = "Author One", notes = "Good morning.")
    )
    val currentUserName
    = if(!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    } else{
        "N/A"
    }
    Column(
        Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(8.dp)
        ) {
            TitleSection(label = "Your reading \n" + " activity right now...")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "",
                    modifier = Modifier
                        .size(45.dp)
                        .clickable {
                            navController.navigate(ReaderScreens.ReaderStatsScreen.name)
                        },
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Column() {
                    Text(
                        text = currentUserName!!,
                        modifier = Modifier.padding(2.dp),
                        style = MaterialTheme.typography.overline,
                        color = Color.Red,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Divider()
                }
            }
        }
        ReadingRightNowArea(books = listOf(), navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(listOfBooks = listOfBooks, navController = navController)
    }

}

@Composable
fun BookListArea(
    listOfBooks: List<MBook>,
    navController: NavHostController
) {
    HorizontalScrollableComponent(listOfBooks){
        //TODO: go to detail
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    onCardPressed: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ){
        for(book in listOfBooks){
            ListCard(book){
                onCardPressed(it)
            }
        }

    }
}

@Composable
fun FABContent(
    onTap: (String) -> Unit
) {
    FloatingActionButton(
        onClick = {
                  onTap("")
        },
        shape = RoundedCornerShape(50.dp),
        backgroundColor = Color(0xFF92CBDF)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "",
            tint = MaterialTheme.colors.onSecondary
        )
    }
}



@Composable
fun ReadingRightNowArea(
    books: List<MBook>,
    navController: NavHostController
){
    ListCard()

}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    label: String
){
    Surface(
        modifier = modifier
            .padding(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Left
        )

    }
}

@Preview
@Composable
fun ListCard(
    book: MBook = MBook(
        id = "A103",
        title = "Secret",
        authors = "JK",
        notes = "Hello world"
    ),
    onPressDetails: (String) -> Unit = {}
){
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp

    Card(
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clickable {
                onPressDetails.invoke(book.title.toString())
            },
        backgroundColor = Color.White,
        shape = RoundedCornerShape(25.dp),
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .width(screenWidth.dp - (spacing * 2)),
            horizontalAlignment = Alignment.Start
        ) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.width(50.dp))

                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    BookRating()


                }
                
            }
            Text(
                text = book.title.toString(),
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 8.dp)
                ,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = book.authors.toString(),
                modifier = Modifier
                    .padding(4.dp)
                    .padding(start = 8.dp),
                style = MaterialTheme.typography.caption
            )
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                RoundedButton(label = "Reading", radius = 60)
            }

        }


    }

}

@Composable
fun BookRating(score: Double = 4.5){
    Surface(
        modifier = Modifier
            .height(70.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(56.dp),
        color = Color.White,
        elevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.StarBorder,
                contentDescription = "",
            )
            Text(
                text = score.toString(),
                style = MaterialTheme.typography.subtitle1
            )
        }

    }
}

@Composable
fun RoundedButton(
    label: String = "Reading",
    radius: Int = 25,
    onPress: () -> Unit = {}
){
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(
                bottomEndPercent = radius,

                topStartPercent = radius
            )),
        color = Color(0xFF92CBDF)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .heightIn(40.dp)
                .clickable {
                    onPress.invoke()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 15.sp
            )
        }

    }
    
}