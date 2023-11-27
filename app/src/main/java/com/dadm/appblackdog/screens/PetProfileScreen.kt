package com.dadm.appblackdog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.BlackDogNavigationRoutes
import com.dadm.appblackdog.models.UiPetProfile
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.CustomIconItem
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.PetAddViewModel
import com.dadm.appblackdog.viewmodels.PetProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun PetProfileScreen(
    navController: NavController,
    drawerState: DrawerState,
    petAddViewModel: PetAddViewModel?,
    profileViewModel: PetProfileViewModel?,
) {
    val uiState by profileViewModel!!.uiState.collectAsState()

    val imagesUrlList = arrayListOf(
        "https://firebasestorage.googleapis.com/v0/b/blackdog-c07fa.appspot.com/o/dog-5773397_1920.jpg?alt=media&token=5341861f-abf3-49a5-8131-ece0cb85468f",
        "https://firebasestorage.googleapis.com/v0/b/blackdog-c07fa.appspot.com/o/dog-7956828_1920.jpg?alt=media&token=f722f7f5-ccda-4edb-8616-a4a0af738683",
        "https://firebasestorage.googleapis.com/v0/b/blackdog-c07fa.appspot.com/o/dog-8262506_1920.jpg?alt=media&token=158736b2-8224-4c29-bbd9-e6f2f08b4588",
        "https://firebasestorage.googleapis.com/v0/b/blackdog-c07fa.appspot.com/o/sample-1.jpg?alt=media&token=f28f65af-67c0-4d17-adc2-80cc97da53b8",
        "https://firebasestorage.googleapis.com/v0/b/blackdog-c07fa.appspot.com/o/terrier-1851108_1920.jpg?alt=media&token=8c640896-b231-420f-b13e-4f98e192b23c"
    )

    val scope = rememberCoroutineScope()
    var expandedMenu by remember { mutableStateOf(false) }
    val cardRadius = 40.dp

    when {
        uiState.loadInfo -> {
            profileViewModel?.refresh()
        }
    }
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
                    trailingAction = {
                        expandedMenu = !expandedMenu
                    }
                )
                DropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    uiState.petList.forEach { item ->
                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            text = { Text(text = item) },
                            onClick = {
                                expandedMenu = false
                                profileViewModel?.changePet(item)
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                contentColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    petAddViewModel?.init()
                    navController.navigate(BlackDogNavigationRoutes.AddPet.name)
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.default_description)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = uiState.photoUrl.ifEmpty { imagesUrlList.random() },
                contentDescription = stringResource(id = R.string.default_description),
                contentScale = ContentScale.FillHeight,
                onLoading = {
                    if (!uiState.loader) profileViewModel?.showLoader(true)
                },
                onSuccess = {
                    profileViewModel?.showLoader(false)
                },
            )
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp),
            ) {
                Box(modifier = Modifier.weight(4f)) {}
                Card(
                    modifier = Modifier
                        .weight(6f)
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
                    Box {
                        ProfileBody(uiState)
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp, top = 16.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.dog_sample_photo),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                        }
                    }
                }
            }
        }
        if (uiState.loader)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White.copy(alpha = 0.4f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
    }
}

@Composable
fun ProfileBody(uiState: UiPetProfile) {
    val titleTextSize =
        with(LocalDensity.current) { dimensionResource(id = R.dimen.title_extra_large).toSp() }

    Column(
        modifier = Modifier
            .padding(42.dp)
            .fillMaxSize()
            .width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = uiState.name,
            fontSize = titleTextSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = uiState.breed,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.size(24.dp))
        Divider(color = Color.White, thickness = 1.5.dp)
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = uiState.description,
            fontSize = 16.sp,
            color = Color.White,
            maxLines = 3
        )
        Spacer(modifier = Modifier.size(24.dp))
        Divider(color = Color.White, thickness = 1.5.dp)
        Spacer(modifier = Modifier.size(24.dp))
        CustomIconItem(
            icon = painterResource(id = R.drawable.dog_icon),
            text = uiState.ageRange
        )
        Spacer(modifier = Modifier.size(24.dp))
        CustomIconItem(
            icon = painterResource(id = R.drawable.birthday),
            text = uiState.birthday
        )
        Spacer(modifier = Modifier.size(24.dp))
        CustomIconItem(
            icon = painterResource(id = R.drawable.weight),
            text = "${uiState.weight} ${uiState.measureUnit}"
        )
    }
}

@Preview
@Composable
fun PetProfileScreenPreview() {
    val context = LocalContext.current
    AppBlackDogTheme {
        Surface {
            PetProfileScreen(
                petAddViewModel = null,
                profileViewModel = null,
                navController = NavController(context),
                drawerState = DrawerState(
                    DrawerValue.Closed
                ),
            )
        }
    }
}