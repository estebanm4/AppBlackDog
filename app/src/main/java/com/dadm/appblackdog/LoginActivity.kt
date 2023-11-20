package com.dadm.appblackdog

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dadm.appblackdog.objects.LoginData
import com.dadm.appblackdog.ui.theme.AppBlackDogTheme
import com.dadm.appblackdog.views.EmailField
import com.dadm.appblackdog.views.LabelCheckbox
import com.dadm.appblackdog.views.PasswordField
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth

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

    //variables
    var form by remember { mutableStateOf(LoginData()) }
    val context = LocalContext.current
    // UI
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        EmailField(
            value = form.email,
            onChange = { data -> form = form.copy(email = data) },
            modifier = Modifier.fillMaxWidth()
        )
        PasswordField(
            value = form.pass,
            modifier = Modifier.fillMaxWidth(),
            onChange = { data -> form = form.copy(pass = data) },
            submit = { if (!checkForm(form, context)) form = LoginData() },
        )
        LabelCheckbox(
            label = stringResource(id = R.string.remember_me),
            onCheckChanged = {
                form = form.copy(remember = !form.remember)
            },
            isChecked = form.remember
        )
        Button(
            onClick = {
                if (!checkForm(form, context)) form = LoginData()
            },
            enabled = true,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.login))
        }
    }

}

fun checkForm(data: LoginData, context: Context): Boolean {
    return if (data.isNotEmpty()) {
        firebaseAuthentication(data, context)
        true
    } else {
        Toast.makeText(
            context,
            ContextCompat.getString(context, R.string.form_error),
            Toast.LENGTH_SHORT
        ).show()
        false
    }
}

fun firebaseAuthentication(data: LoginData, context: Context) {

    auth = Firebase.auth
    auth.signInWithEmailAndPassword(data.email, data.pass)
        .addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(ContentValues.TAG, "signInWithEmail:success")
                updateUI(context)
                /*auth.currentUser*/
            } else {
                // If sign in fails, display a message to the user.
                Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                Log.d("MyLog", "hello i am here")
                Toast.makeText(
                    context,
                    ContextCompat.getString(context, R.string.server_error),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
}

fun updateUI(context: Context) {
    context.startActivity(Intent(context, MainActivity::class.java))
    (context as Activity).finish()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AppBlackDogTheme {
        LoginForm()
    }
}