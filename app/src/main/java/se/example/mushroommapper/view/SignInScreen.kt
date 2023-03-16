package se.example.mushroommapper.view

import android.graphics.Color.parseColor
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
import se.example.mushroommapper.viewModel.SignInViewModel
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.ui.theme.BACKGROUND_COLOR
import se.example.mushroommapper.ui.theme.NON_INTERACTABLE_COLOR
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

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.getResult(ApiException::class.java)
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                viewModel.googleSignIn(credentials)
            } catch (it: ApiException) {
                print(it)
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
                    text = "Enter your credential's to Login",
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
                                text = "Enter your email address",
                                color = INTERACTABLE_COLOR.color
                            )
                        },
                        onValueChange = { email = it },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    TextField(
                        label = {
                            Text(
                                text = "Password",
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
                                text = "Enter your password",
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
                            scope.launch {
                                viewModel.loginUser(email, password)
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
                            text = "Sign In",
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


                Spacer(modifier = Modifier.height(20.dp))
                ClickableText(
                    text = AnnotatedString("Forgot password?"),
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
                    text = "Don't have an account?",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Default,
                        color = NON_INTERACTABLE_COLOR.color
                    ),
                )
                ClickableText(
                    text = AnnotatedString("Sign up"),
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
                    text = "or connect with",
                    color = NON_INTERACTABLE_COLOR.color)
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
                                Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
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
                                Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }

                    LaunchedEffect(key1 = googleSignInState.success) {
                        scope.launch {
                            if (googleSignInState.success != null) {
                                Toast.makeText(context, "Sign In Success", Toast.LENGTH_LONG).show()
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



/*
Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, end = 30.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Enter your credential's to Login",
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            color = Color.Gray,

        )
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White, unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ), shape = RoundedCornerShape(8.dp), singleLine = true, placeholder = {
                Text(text = "Email")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Color.Black,
                disabledLabelColor = Color.White, unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ), shape = RoundedCornerShape(8.dp), singleLine = true, placeholder = {
                Text(text = "Password")
            }
        )

        Button(
            onClick = {
                scope.launch {
                    viewModel.loginUser(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 30.dp, end = 30.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White, contentColor = Color.White
            ),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = "Sign In", color = Color.Black, modifier = Modifier.padding(7.dp))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (state.value?.isLoading == true) {
                CircularProgressIndicator()
            }
        }
        Text(
            text = "Don't have an account?",
            style = TextStyle(
                fontSize = 15.sp,
                fontFamily = FontFamily.Default
            )
        )
        ClickableText(
            text = AnnotatedString("Sign up"),
            onClick = {
                      onSignUpClick()
            },
            style = TextStyle(
                fontSize = 17.sp,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.purple_700)
            )
        )
        Text(text = "or connect with", fontWeight = FontWeight.Medium, color = Color.Gray)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = {
                val gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

                val googleSingInClient = GoogleSignIn.getClient(context, gso)

                launcher.launch(googleSingInClient.signInIntent)

            }) {
                Icon(Icons.Default.Star, contentDescription= "star")
            }
            Spacer(modifier = Modifier.width(20.dp))
            IconButton(onClick = {

            }) {
                Icon(Icons.Default.Star, contentDescription= "star")
            }
            LaunchedEffect(key1 = state.value?.isSuccess) {
                scope.launch {
                    if (state.value?.isSuccess?.isNotEmpty() == true) {
                        val success = state.value?.isSuccess
                        Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
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
                        Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
                    }
                }
            }

            LaunchedEffect(key1 = googleSignInState.success) {
                scope.launch {
                    if (googleSignInState.success != null) {
                        Toast.makeText(context, "Sign In Success", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (googleSignInState.loading){
                CircularProgressIndicator()
            }
        }
    }
 */
