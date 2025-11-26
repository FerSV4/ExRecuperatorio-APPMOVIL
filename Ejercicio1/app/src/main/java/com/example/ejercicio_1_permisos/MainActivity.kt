package com.example.ejercicio_1_permisos

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ejercicio_1_permisos.ui.theme.Ejercicio1PermisosTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState



@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ejercicio1PermisosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaPermisos()
                }
            }
        }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PantallaPermisos() {
    val contactosState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    val archivosState =
        rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    val pendientes = remember { mutableStateListOf<String>() }

    LaunchedEffect(contactosState.status, archivosState.status) {
        pendientes.clear()
        if (!contactosState.status.isGranted) pendientes.add("READ_CONTACTS")
        if (!archivosState.status.isGranted) pendientes.add("READ_EXTERNAL_STORAGE")
    }

    val multipleState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    )

    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { contactosState.launchPermissionRequest() }) {
                Text("Pedir contactos")
            }
            Button(onClick = { archivosState.launchPermissionRequest() }) {
                Text("Pedir archivos")
            }
        }

        Text("Contactos: " + estadoTexto(contactosState))
        Text("Archivos: " + estadoTexto(archivosState))

        Text("Pendientes: ${pendientes.joinToString()}")

        OutlinedButton(onClick = { multipleState.launchMultiplePermissionRequest() }) {
            Text("Pedir ambos")
        }

        val algunDontAskAgain =
            (!contactosState.status.shouldShowRationale && !contactosState.status.isGranted) ||
                    (!archivosState.status.shouldShowRationale && !archivosState.status.isGranted)

        if (algunDontAskAgain) {
            OutlinedButton(
                onClick = {
                    val uri = Uri.fromParts("package", context.packageName, null)
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = uri
                        }
                    context.startActivity(intent)
                }
            ) {
                Text("Abrir ajustes de la app")
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
fun estadoTexto(state: PermissionState): String {
    return if (state.status.isGranted) "Granted" else "Denied"
}
