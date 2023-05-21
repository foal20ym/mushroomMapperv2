package se.example.mushroommapper.view

import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import se.example.mushroommapper.Extensions.ContentColorComponent
import se.example.mushroommapper.R
import se.example.mushroommapper.ui.theme.BACKGROUND_COLOR
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.ui.theme.NON_INTERACTABLE_COLOR
import se.example.mushroommapper.viewModel.SignInViewModel
import se.example.mushroommapper.viewModel.color


// Test2@gmail.com
// test123
@Composable
fun SignInScreen(
    onClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val googleSignInState = viewModel.googleState.value
    val shouldDisplayError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }


    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                viewModel.googleSignIn(credentials)
            } catch (e: ApiException) {
                print(e)
                errorMessage.value = e.localizedMessage ?: "Unknown error"
                shouldDisplayError.value = true
            }
        }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

    Row(
        modifier = Modifier
            .background(color = BACKGROUND_COLOR.color)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Mushroom Mapper",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                ),
                color = NON_INTERACTABLE_COLOR.color,
                textAlign = TextAlign.Center
            )
            Icon(
                Icons.Default.Star,
                contentDescription = "Person Icon"
            )
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                modifier = Modifier
                    .height(20.dp),
                text = stringResource(id = R.string.EnterCredentials),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = NON_INTERACTABLE_COLOR.color,
            )
            Spacer(modifier = Modifier.height(25.dp))

            ContentColorComponent(contentColor = NON_INTERACTABLE_COLOR.color) {
                TextField(
                    label = {
                        Text(
                            text = "Email",
                            color = INTERACTABLE_COLOR.color
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "emailIcon",
                            tint = INTERACTABLE_COLOR.color
                        )
                    },
                    value = email,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.EnterEmail),
                            color = INTERACTABLE_COLOR.color
                        )
                    },
                    onValueChange = { email = it },
                )

                Spacer(modifier = Modifier.height(20.dp))

                TextField(
                    label = {
                        Text(
                            text = stringResource(id = R.string.password),
                            color = INTERACTABLE_COLOR.color
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = "passwordIcon",
                            tint = INTERACTABLE_COLOR.color
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.EnterPassword),
                            color = INTERACTABLE_COLOR.color
                        )
                    },
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { password = it }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                Button(
                    onClick = {
                        val emailInput = email.trim()
                        val passwordInput = password.trim()

                        if (emailInput.isEmpty()) {
                            errorMessage.value = "Email is required"
                            shouldDisplayError.value = true
                            return@Button
                        }

                        if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                            errorMessage.value = "Invalid email address"
                            shouldDisplayError.value = true
                            return@Button
                        }

                        if (passwordInput.isEmpty()) {
                            errorMessage.value = "Password is required"
                            shouldDisplayError.value = true
                            return@Button
                        }

                        scope.launch {
                            try {
                                viewModel.loginUser(emailInput, passwordInput)
                            } catch (e: Exception) {
                                errorMessage.value = e.localizedMessage ?: "Unknown error"
                                shouldDisplayError.value = true
                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, start = 30.dp, end = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = INTERACTABLE_COLOR.color,
                    ),
                    shape = RoundedCornerShape(50.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.SignIn),
                        color = NON_INTERACTABLE_COLOR.color,
                        modifier = Modifier.padding(7.dp)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.value?.isLoading == true) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (shouldDisplayError.value) {
                ErrorDialog(errorMessage.value) {
                    shouldDisplayError.value = false
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.ForgotPassword)),
                onClick = { onForgotClick() },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = INTERACTABLE_COLOR.color
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = stringResource(id = R.string.DontHaveAccount),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    color = NON_INTERACTABLE_COLOR.color
                ),
            )
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.SignUp)),
                onClick = { onSignUpClick() },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = INTERACTABLE_COLOR.color,
                )
            )

            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(id = R.string.OrConnectWith),
                color = NON_INTERACTABLE_COLOR.color
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        //.requestIdToken(ServerClient)
                        .build()

                    val googleSingInClient = GoogleSignIn.getClient(context, gso)

                    launcher.launch(googleSingInClient.signInIntent)

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(50.dp),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(onClick = {

                }) {
                    Icon(
                        modifier = Modifier.size(52.dp),
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Facebook Icon", tint = Color.Unspecified
                    )
                }
                LaunchedEffect(key1 = state.value?.isSuccess) {
                    scope.launch {
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                        }
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            onClick()
                        }
                    }
                }

                LaunchedEffect(key1 = state.value?.isError) {
                    scope.launch {
                        if (state.value?.isError?.isNotEmpty() == true) {
                            val error = state.value?.isError
                            Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                val signInSuccess = stringResource(id = R.string.SignInSuccess)
                LaunchedEffect(key1 = googleSignInState.success) {
                    scope.launch {
                        if (googleSignInState.success != null) {
                            Toast.makeText(context, signInSuccess, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (googleSignInState.loading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

