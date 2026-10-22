package com.example.eventregistration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

private const val MIN_TICKET = 1
private const val MAX_TICKET = 5

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EventRegistrationScreen()
                }
            }
        }
    }
}

/**
 * Screen = OWNER dari semua state form.
 * Semua state memakai rememberSaveable supaya tidak hilang saat rotasi layar.
 */
@Composable
fun EventRegistrationScreen() {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var ticketCount by rememberSaveable { mutableIntStateOf(MIN_TICKET) }
    var agreedToTerms by rememberSaveable { mutableStateOf(false) }
    var reminderEnabled by rememberSaveable { mutableStateOf(false) }
    var isRegistered by rememberSaveable { mutableStateOf(false) }

    // Validasi diturunkan dari state (bukan state baru)
    val isNameValid = name.isNotBlank()
    val isEmailValid = isValidEmail(email)
    val isFormValid = isNameValid && isEmailValid && agreedToTerms

    // Event handler: mengubah state di satu tempat
    fun resetForm() {
        name = ""
        email = ""
        ticketCount = MIN_TICKET
        agreedToTerms = false
        reminderEnabled = false
        isRegistered = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Pendaftaran Event",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Workshop Jetpack Compose State",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // Text input 1: nama
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                isRegistered = false
            },
            label = { Text("Nama lengkap") },
            singleLine = true,
            isError = name.isNotEmpty() && !isNameValid,
            supportingText = {
                if (name.isNotEmpty() && !isNameValid) Text("Nama tidak boleh kosong")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Text input 2: email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isRegistered = false
            },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = {
                if (email.isNotEmpty() && !isEmailValid) Text("Format email belum valid")
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Komponen stateless #1: counter tiket (state di-hoist ke screen)
        TicketCounter(
            count = ticketCount,
            onIncrement = {
                if (ticketCount < MAX_TICKET) ticketCount++
                isRegistered = false
            },
            onDecrement = {
                if (ticketCount > MIN_TICKET) ticketCount--
                isRegistered = false
            }
        )

        // Komponen stateless #2: Switch
        LabeledSwitch(
            label = "Kirim pengingat event ke email",
            checked = reminderEnabled,
            onCheckedChange = { reminderEnabled = it }
        )

        // Komponen stateless #3: Checkbox
        LabeledCheckbox(
            label = "Saya setuju dengan syarat & ketentuan",
            checked = agreedToTerms,
            onCheckedChange = {
                agreedToTerms = it
                isRegistered = false
            }
        )

        // Feedback: berubah sesuai state
        FeedbackMessage(
            isFormValid = isFormValid,
            isRegistered = isRegistered,
            name = name,
            ticketCount = ticketCount,
            reminderEnabled = reminderEnabled
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            // Tombol enabled/disabled bergantung pada validasi
            Button(
                onClick = { isRegistered = true },
                enabled = isFormValid,
                modifier = Modifier.weight(1f)
            ) {
                Text("Daftar")
            }
            OutlinedButton(
                onClick = { resetForm() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }
    }
}

/** Stateless: tidak menyimpan state, hanya menerima value + callback. */
@Composable
fun TicketCounter(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Jumlah tiket", style = MaterialTheme.typography.titleSmall)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onDecrement, enabled = count > MIN_TICKET) {
                    Text("-")
                }
                Text(text = count.toString(), style = MaterialTheme.typography.titleLarge)
                OutlinedButton(onClick = onIncrement, enabled = count < MAX_TICKET) {
                    Text("+")
                }
            }
        }
    }
}

/** Stateless: state `checked` dimiliki parent. */
@Composable
fun LabeledSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

/** Stateless: state `checked` dimiliki parent. */
@Composable
fun LabeledCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label)
    }
}

@Composable
fun FeedbackMessage(
    isFormValid: Boolean,
    isRegistered: Boolean,
    name: String,
    ticketCount: Int,
    reminderEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val (message, color) = when {
        isRegistered -> {
            val reminder = if (reminderEnabled) "Pengingat aktif." else "Tanpa pengingat."
            "Berhasil! $name terdaftar dengan $ticketCount tiket. $reminder" to
                    MaterialTheme.colorScheme.primary
        }
        isFormValid -> "Data lengkap. Tekan Daftar untuk melanjutkan." to
                MaterialTheme.colorScheme.tertiary
        else -> "Isi nama, email yang valid, dan setujui syarat & ketentuan." to
                MaterialTheme.colorScheme.error
    }
    Text(text = message, color = color, modifier = modifier.fillMaxWidth())
}

private fun isValidEmail(value: String): Boolean {
    val atIndex = value.indexOf('@')
    val dotIndex = value.lastIndexOf('.')
    return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < value.length - 1
}
