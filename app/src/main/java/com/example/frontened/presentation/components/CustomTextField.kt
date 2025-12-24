package com.example.frontened.presentation.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    leadingIcon: ImageVector? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    color: androidx.compose.ui.graphics.Color
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, label = { Text(text = label) },
        modifier = modifier, singleLine = singleLine, leadingIcon = leadingIcon?.let { icon ->
            { Icon(imageVector = icon, contentDescription = null) }
        }, keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = color,
            unfocusedTextColor = color,
            focusedBorderColor = color,
            unfocusedBorderColor = color,
            cursorColor = color
        )
    )

}