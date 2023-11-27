package com.dadm.appblackdog.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dadm.appblackdog.R
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.CustomIconItem
import com.dadm.appblackdog.ui_elements.MainAppBar
import kotlinx.coroutines.launch

@Composable
fun RecipesScreen(
    navController: NavController,
    drawerState: DrawerState,
) {
    val imagesUrlList = arrayListOf(
        "https://firebasestorage.googleapis.com/v0/b/blackdog-c07fa.appspot.com/o/french-restaurant-2506490_1920.jpg?alt=media&token=060ac046-29ca-4f1a-96fd-03b3b4f101ab",
    )

    val scope = rememberCoroutineScope()
    val cardRadius = 40.dp
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                MainAppBar(
                    label = stringResource(id = R.string.my_pets),
                    trailingIcon = Icons.Default.ViewList,
                    leadingAction = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    trailingAction = {}
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
            //            .background(Color.Gray),
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = imagesUrlList.random(),
                contentDescription = stringResource(id = R.string.default_description),
                contentScale = ContentScale.FillHeight,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
            ) {
                Box(modifier = Modifier.weight(3f)) {}
                Card(
                    modifier = Modifier
                        .weight(7f)
                        .fillMaxSize()
                        .border(
                            2.dp,
                            Color.White,
                            RoundedCornerShape(topStart = cardRadius)
                        ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    shape = RoundedCornerShape(topStart = cardRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    ),
                ) {
                    RecipeBody()
                }
            }
        }
    }
}

@Composable
fun RecipeBody() {

    val ingredients = arrayListOf(
        "1 taza de arroz integral",
        "500 gramos de pollo picado",
        "2 zanahorias",
        "1 patata",
        "Un puñado de espinacas",
    )

    val titleTextSize =
        with(LocalDensity.current) { dimensionResource(id = R.dimen.title_large).toSp() }

    Column(
        modifier = Modifier
            .padding(42.dp)
            .fillMaxSize()
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.Start
    ) {

        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Pollo con arroz y verduras",
            fontSize = titleTextSize,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 2
        )


        Spacer(modifier = Modifier.size(24.dp))
        Divider(color = Color.White, thickness = 1.5.dp)
        Spacer(modifier = Modifier.size(24.dp))



        ingredients.forEach {
            CustomIconItem(
                icon = painterResource(id = R.drawable.check_circle),
                text = it,
                maxLines = 2
            )
            Spacer(modifier = Modifier.size(24.dp))
        }



        Spacer(modifier = Modifier.size(24.dp))
        Divider(color = Color.White, thickness = 1.5.dp)


    }
}

@Preview(showBackground = true)
@Composable
fun RecipesScreenPreview() {
    val context = LocalContext.current

    AppBlackDogTheme {
        RecipesScreen(
            navController = NavController(context),
            drawerState = DrawerState(
                DrawerValue.Closed
            ),
        )
    }
}