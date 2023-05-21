package se.example.mushroommapper.view

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import se.example.mushroommapper.Extensions.ContentColorComponent
import se.example.mushroommapper.R
import se.example.mushroommapper.ui.theme.BACKGROUND_COLOR
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.ui.theme.NON_INTERACTABLE_COLOR
import se.example.mushroommapper.viewModel.SignUpViewModel
import se.example.mushroommapper.viewModel.color


@Composable
fun SignUpScreen(
    onClick: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signUpState.collectAsState(initial = null)
    val shouldDisplayError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .background(BACKGROUND_COLOR.color)
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
            Icon(Icons.Default.Star, contentDescription = "Star Icon")

            Text(
                modifier = Modifier.padding(bottom = 10.dp),
                text = stringResource(id = R.string.CreateAccount),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = NON_INTERACTABLE_COLOR.color
            )
            Text(
                text = stringResource(id = R.string.EnterCredToRegister),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = NON_INTERACTABLE_COLOR.color
            )
            Spacer(modifier = Modifier.height(20.dp))
            ContentColorComponent(contentColor = NON_INTERACTABLE_COLOR.color) {
                TextField(
                    label = {
                        Text(
                            text = "Email",
                            color = INTERACTABLE_COLOR.color
                        )
                    },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.EnterEmail),
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
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.EnterPassword),
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
                    value = password,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    onValueChange = { password = it }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp),
            ) {
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

                        if (passwordInput.length < 8) {
                            errorMessage.value = "Password must be at least 8 characters long"
                            shouldDisplayError.value = true
                            return@Button
                        }

                        scope.launch {
                            try {
                                viewModel.registerUser(emailInput, passwordInput)
                            } catch (e: Exception) {
                                errorMessage.value = e.localizedMessage ?: "Unknown error"
                                shouldDisplayError.value = true
                            }
                        }

                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = INTERACTABLE_COLOR.color,
                    ),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.SignUp),
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

            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(id = R.string.HaveAccount),
                fontWeight = FontWeight.Medium,
                color = NON_INTERACTABLE_COLOR.color
            )
            Text(
                modifier = Modifier
                    .clickable {
                        onClick()
                    },
                text = stringResource(id = R.string.SignIn),
                fontWeight = FontWeight.Bold,
                color = INTERACTABLE_COLOR.color,
                textDecoration = TextDecoration.Underline
            )

            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = stringResource(id = R.string.OrConnectWith),
                color = NON_INTERACTABLE_COLOR.color
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp), horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon", tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                IconButton(onClick = {

                }) {
                    Icon(
                        modifier = Modifier.size(52.dp),
                        painter = painterResource(id = R.drawable.ic_facebook),
                        contentDescription = "Google Icon", tint = Color.Unspecified
                    )
                }
            }
        }

        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
                    onClick()
                }
            }
        }
        LaunchedEffect(key1 = state.value?.isError) {
            scope.launch {
                if (state.value?.isError?.isNotBlank() == true) {
                    val error = state.value?.isError
                    Toast.makeText(context, "$error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}