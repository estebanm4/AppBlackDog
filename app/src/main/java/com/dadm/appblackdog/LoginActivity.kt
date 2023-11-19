package com.dadm.appblackdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.views.EmailField
import com.dadm.appblackdog.views.LabelCheckbox
import com.dadm.appblackdog.views.PasswordField

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppBlackDogTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginForm()
                }
            }
        }
    }
}

@Composable
fun LoginForm() {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        EmailField(
            value = "",
            onChange = {},
            modifier = Modifier.fillMaxWidth()
        )
        PasswordField(
            value = "",
            modifier = Modifier.fillMaxWidth(),
            onChange = {},
            submit = { /*TODO*/ }
        )
        LabelCheckbox(
            label = stringResource(id = R.string.remember_me),
            onCheckChanged = {
//                form = form.copy(remember = !form.remember)
            },
            isChecked = true
        )
        Button(
            onClick = {
//                if (!checkForm(form, context)) form = LoginData()
            },
            enabled = true,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.login))
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AppBlackDogTheme {
        LoginForm()
    }
}