package com.dadm.appblackdog.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.dadm.appblackdog.R
import com.dadm.appblackdog.models.BlackDogNavigationRoutes
import com.dadm.appblackdog.models.UiPetForm
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.ui_elements.CustomDropDownField
import com.dadm.appblackdog.ui_elements.CustomInputText
import com.dadm.appblackdog.ui_elements.CustomUnitField
import com.dadm.appblackdog.ui_elements.MainAppBar
import com.dadm.appblackdog.viewmodels.AppViewModelProvider
import com.dadm.appblackdog.viewmodels.PetScreenViewModel

@Composable
fun AddPetScreen(
    petViewModel: PetScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
) {
    val uiState by petViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            MainAppBar(
                label = stringResource(id = R.string.add_pet_title),
                hideTrailingIcon = true,
                leadingIcon = Icons.Default.ArrowBack,
                leadingAction = {
                    navController.popBackStack(
                        BlackDogNavigationRoutes.UserData.name,
                        inclusive = false
                    )
                },
            )
        }
    ) { padding ->
        if (uiState.isLoader)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        else
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .imePadding()
                    .verticalScroll(rememberScrollState())
            ) {
                AddPetBody(petViewModel = petViewModel, uiState = uiState)
            }
    }
}

@Composable
fun AddPetBody(petViewModel: PetScreenViewModel?, uiState: UiPetForm) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxHeight()
            .padding(dimensionResource(id = R.dimen.padding_large))
    ) {
        AddPetForm(petViewModel = petViewModel, uiState = uiState)
    }
}

@Composable
fun AddPetForm(petViewModel: PetScreenViewModel?, uiState: UiPetForm) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        /** name textField */
        CustomInputText(
            error = uiState.nameError,
            value = uiState.name,
            icon = Icons.Default.Pets,
            label = stringResource(id = R.string.name),
            onChange = { data -> petViewModel?.updateName(data) },
            modifier = Modifier.fillMaxWidth()
        )
        /** breed selector */
        CustomDropDownField(
            value = uiState.breed,
            icon=Icons.Default.Bloodtype,
            label = stringResource(id = R.string.breed),
            items = uiState.breedList,
            onItemChange = { data -> petViewModel?.updateBreedId(data) })
        /** weight textField and unit selector */
        CustomUnitField(
            value = uiState.weight,
            valueSelector = uiState.measureUnit,
            label = stringResource(id = R.string.weight),
            selectorLabel = stringResource(id = R.string.unit),
            items = uiState.measureUnitList,
            icon = Icons.Rounded.MonitorWeight,
            onChange = { data -> petViewModel?.updateWeight(data) },
            onItemChange = { data -> petViewModel?.updateMeasureUnitId(data) },
            keyboardType = KeyboardType.Number
        )
        /** description textField */
        CustomInputText(
            value = uiState.description,
            icon = Icons.Default.Description,
            label = stringResource(id = R.string.description),
            onChange = { data -> petViewModel?.updateDescription(data) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun AddPetScreenPreview() {
    AppBlackDogTheme {
        Surface {
            AddPetBody(null, UiPetForm())
        }
    }
}