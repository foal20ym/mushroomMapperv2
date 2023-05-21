package se.example.mushroommapper.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import se.example.mushroommapper.viewModel.ResetPasswordViewModel
import se.example.mushroommapper.viewModel.color


@Composable
fun ResetPasswordScreen(
    onClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: ResetPasswordViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.resetPasswordState.collectAsState(initial = null)
    Row() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BACKGROUND_COLOR.color)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = "Mushroom Mapper",
                style = TextStyle(
                    fontSize = 40.sp,
                    color = NON_INTERACTABLE_COLOR.color)
            )

            Icon(Icons.Default.Star, contentDescription = "Star Icon")

            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = stringResource(id = R.string.RevoveryEmail),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default,
                    color = NON_INTERACTABLE_COLOR.color
                )
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
                    value = email,
                    onValueChange = { email = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.EnterEmail),
                            color = INTERACTABLE_COLOR.color
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "emailicon",
                            tint = INTERACTABLE_COLOR.color
                        )
                    },
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.resetPassword(email)
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = INTERACTABLE_COLOR.color,
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.ResetPassword),
                        color = NON_INTERACTABLE_COLOR.color
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.DontHaveAccount),
                style = TextStyle(

                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    color = NON_INTERACTABLE_COLOR.color
                )
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
            Spacer( modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.tryLoginAgain),
                style = TextStyle(

                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    color = NON_INTERACTABLE_COLOR.color
                )
            )
            ClickableText(
                text = AnnotatedString(stringResource(id = R.string.SignIn)),
                onClick = { onClick() },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = INTERACTABLE_COLOR.color,
                )
            )
        }
        LaunchedEffect(key1 = state.value?.isSuccess) {
            scope.launch {
                if (state.value?.isSuccess?.isNotEmpty() == true) {
                    val success = state.value?.isSuccess
                    Toast.makeText(context, "$success", Toast.LENGTH_LONG).show()
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

