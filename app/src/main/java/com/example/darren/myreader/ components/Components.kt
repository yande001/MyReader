package com.example.darren.myreader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.darren.myreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavHostController,
    onBackArrowClicked: () -> Unit = {}
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile){
                    Icon(
                        imageVector = Icons.Filled.MenuBook,
                        contentDescription = ""
                    )
                }
                if (icon != null){
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                onBackArrowClicked.invoke()
                            },
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = title,
                    color = Color.Red.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

        },
        actions = {
            IconButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut().run{
                        navController.navigate(ReaderScreens.LoginScreen.name)
                    }
                }
            ) {
                if(showProfile){
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "",
                    )
                } else{
                    Box() {
                        
                    }
                }

            }

        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}