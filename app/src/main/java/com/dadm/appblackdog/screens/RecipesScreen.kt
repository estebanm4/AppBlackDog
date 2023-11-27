package com.dadm.appblackdog.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.UiRecipeView
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.CustomIconItem
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.RecipeViewModel
import kotlinx.coroutines.launch

@Composable
fun RecipesScreen(
    drawerState: DrawerState,
    recipeViewModel: RecipeViewModel
) {
    val uiState by recipeViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                MainAppBar(
                    label = stringResource(id = R.string.recipes),
                    trailingIcon = Icons.Default.Update,
                    leadingAction = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    },
                    trailingAction = {
                        recipeViewModel.reloadRecipe()
                    }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
            //            .background(Color.Gray),
        ) {
            if (uiState.loader) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            } else {
                SubcomposeAsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = uiState.imageUrl,
                    contentDescription = stringResource(id = R.string.default_description),
                    contentScale = ContentScale.FillHeight,
                    loading = {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(64.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        }
                    }
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                ) {
//                    Box(modifier = Modifier.weight(3f)) {}
                    Box(modifier = Modifier.padding(start = 16.dp)) { RecipeBody(uiState) }

                }
            }
        }
    }
}

@Composable
fun RecipeBody(
    uiState: UiRecipeView
) {
    val cardRadius = 40.dp
    val titleTextSize =
        with(LocalDensity.current) { dimensionResource(id = R.dimen.title_large).toSp() }

    Card(
        modifier = Modifier
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
        Column(
            modifier = Modifier
                .padding(42.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = uiState.name,
                fontSize = titleTextSize,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2
            )
            Spacer(modifier = Modifier.size(24.dp))
            Divider(color = Color.White, thickness = 1.5.dp)
            Spacer(modifier = Modifier.size(24.dp))
            uiState.items.forEach {
                CustomIconItem(
                    icon = painterResource(id = R.drawable.check_circle),
                    text = it,
                    maxLines = 3
                )
//                Spacer(modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.size(24.dp))
            Divider(color = Color.White, thickness = 1.5.dp)
            Spacer(modifier = Modifier.size(24.dp))
            Text(
                text = uiState.description,
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipesScreenPreview() {
    AppBlackDogTheme {
        RecipeBody(
            uiState = UiRecipeView()
        )
    }
}