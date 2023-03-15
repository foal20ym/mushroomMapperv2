package se.example.mushroommapper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import se.example.mushroommapper.Extensions.ContentColorComponent
import se.example.mushroommapper.R
import se.example.mushroommapper.ui.theme.BACKGROUND_COLOR
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.ui.theme.NON_INTERACTABLE_COLOR
import se.example.mushroommapper.ui.theme.Purple700
import se.example.mushroommapper.viewModel.color

@Composable
fun ResetPasswordScreen(
    onClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {

    Row() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BACKGROUND_COLOR.color)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {

            val email = remember { mutableStateOf(TextFieldValue()) }

            Text(
                text = "Mushroom Mapper",
                style = TextStyle(
                    fontSize = 40.sp,
                    color = NON_INTERACTABLE_COLOR.color)
            )

            Icon(Icons.Default.Star, contentDescription = "Star Icon")

            Spacer(modifier = Modifier.height(35.dp))
            Text(
                text = "Enter your email address to recover your password",
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
                    value = email.value,
                    onValueChange = { email.value = it },
                    placeholder = {
                        Text(
                            text = "Enter your email address",
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
                    onClick = { /* TODO */ },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = INTERACTABLE_COLOR.color,
                    ),
                ) {
                    Text(
                        text = "Reset Password",
                        color = NON_INTERACTABLE_COLOR.color
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Don't have an account?",
                style = TextStyle(

                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    color = NON_INTERACTABLE_COLOR.color
                )
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
            Spacer( modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "If you want to try login in again go to",
                style = TextStyle(

                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    color = NON_INTERACTABLE_COLOR.color
                )
            )
            ClickableText(
                text = AnnotatedString("Sign in"),
                onClick = { onSignUpClick() },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    fontFamily = FontFamily.Default,
                    textDecoration = TextDecoration.Underline,
                    color = INTERACTABLE_COLOR.color,
                )
            )
        }
    }
}

