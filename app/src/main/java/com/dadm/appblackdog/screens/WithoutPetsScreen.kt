package com.dadm.appblackdog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.BlackDogNavigationRoutes
import com.dadm.appblackdog.ui_elements.GenericSpacer
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.PetAddViewModel
import kotlinx.coroutines.launch

@Composable
fun WithoutPetsScreen(
    navController: NavController,
    drawerState: DrawerState,
    petViewModel: PetAddViewModel?,
) {
    val scope = rememberCoroutineScope()

    val titleTextSize =
        with(LocalDensity.current) { dimensionResource(id = R.dimen.title_large).toSp() }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                contentColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    petViewModel?.init()
                    navController.navigate(BlackDogNavigationRoutes.AddPet.name)
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.default_description)
                )
            }
        },
        topBar = {
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

    ) { padding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large))
        ) {
            Text(
                text = stringResource(id = R.string.without_pets),
                fontSize = titleTextSize,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            GenericSpacer()
            GenericSpacer()
            Image(
                painter = painterResource(id = R.drawable.owner_profile),
                contentDescription = "owner",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(
                        1.5.dp,
                        MaterialTheme.colorScheme.secondary,
                        CircleShape
                    )
            )
            GenericSpacer()
            Text(
                text = stringResource(id = R.string.without_pets_description),
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
            GenericSpacer()
            GenericSpacer()
            Image(
                modifier = Modifier
                    .size(150.dp)
                    .graphicsLayer {
                        this.rotationZ = 40f
                    },
                painter = painterResource(id = R.drawable.curve_arrow_right),
                contentDescription = stringResource(id = R.string.default_description)
            )
//            GenericSpacer(space = 8.dp)
//            OutlinedButton(onClick = actionButton) {
//                Text(text = stringResource(id = R.string.add_pet))
//            }
        }
    }
}

@Preview
@Composable
fun WithoutPetsScreenPreview() {
    val context = LocalContext.current
    WithoutPetsScreen(
        petViewModel = null,
        navController = NavController(context),
        drawerState = DrawerState(
            DrawerValue.Closed
        ),
    )
}