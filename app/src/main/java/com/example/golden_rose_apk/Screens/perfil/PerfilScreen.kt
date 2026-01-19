package com.example.golden_rose_apk.Screens.perfil

import android.Manifest
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.golden_rose_apk.Screens.HomeBottomNavigationBar
import com.example.golden_rose_apk.ViewModel.AuthViewModel
import com.example.golden_rose_apk.ViewModel.AuthViewModelFactory
import com.example.golden_rose_apk.ViewModel.SettingsViewModel
import com.example.golden_rose_apk.model.BottomNavItem
import java.io.File
import java.io.FileOutputStream
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application))

    // Estado que viene del SettingsViewModel COMPARTIDO
    val username by settingsViewModel.username.collectAsState()
    val receiveOffers by settingsViewModel.receiveOffers.collectAsState()
    val currentTheme by settingsViewModel.appTheme.collectAsState()
    val pushNotificationsEnabled by settingsViewModel.pushNotificationsEnabled.collectAsState()

    // ---- Firebase user ----
    val firebaseUser = FirebaseAuth.getInstance().currentUser
    val email = firebaseUser?.email.orEmpty()

    // 🔹 username que viene de Firestore (campo "username")
    var firestoreUsername by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(firebaseUser?.uid) {
        val uid = firebaseUser?.uid ?: return@LaunchedEffect

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                firestoreUsername = doc.getString("username")
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    // Nombre para mostrar: primero displayName, luego username guardado, luego parte del email
    val displayName = firestoreUsername
        ?: firebaseUser?.displayName
        ?: username.takeIf { it.isNotBlank() }
        ?: email.substringBefore("@")
        ?: "Invitado"

    // Estado para almacenar imagen de perfil
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para abrir galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    // Launcher para tomar foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val uri = saveBitmapToCache(context, bitmap)
            profileImageUri = uri
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch("image/*")
        }
    }

    // Bottom Navigation
    val navItems = listOf(
        BottomNavItem("Inicio", Icons.Filled.Home, "home"),
        BottomNavItem("Categorías", Icons.Filled.Category, "categories"),
        BottomNavItem("Blogs", Icons.Filled.Article, "blogs"),
        BottomNavItem("Perfil", Icons.Filled.Person, "perfil")
    )

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            settingsViewModel.setPushNotificationsEnabled(true)
            FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM", "Suscripción a notificaciones activada tras permiso")
                        Toast.makeText(
                            context,
                            "Notificaciones activadas",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            settingsViewModel.setPushNotificationsEnabled(false)
            Log.d("FCM", "Usuario negó el permiso de notificaciones")
            Toast.makeText(
                context,
                "Permiso de notificaciones denegado",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5649A5),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            HomeBottomNavigationBar(
                navController = navController,
                navItems = navItems
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Avatar + nombre + email
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier.size(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profileImageUri),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(Color(0xFF5649A5), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = displayName.take(1).uppercase(),
                                color = Color.White,
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (email.isNotBlank()) {
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Botón subir imagen
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = {
                    val permission =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            Manifest.permission.READ_MEDIA_IMAGES
                        else
                            Manifest.permission.READ_EXTERNAL_STORAGE

                    permissionLauncher.launch(permission)
                }) {
                    Icon(Icons.Default.Image, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Subir desde galería")
                }
            }

            // Botón Editar Perfil
            TextButton(
                onClick = { navController.navigate("editarPerfil") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Editar Perfil")
            }

            SettingItemDivider(title = "Tus compras")

            TextButton(
                onClick = { navController.navigate("orderHistory") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.ShoppingBag, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Mis compras")
            }


            // Preferencias
            SettingItemDivider(title = "Preferencias")

            SettingSwitchItem(
                text = "Recibir ofertas por correo",
                icon = Icons.Default.Email,
                checked = receiveOffers,
                onCheckedChange = { settingsViewModel.setReceiveOffers(it) }
            )

            SettingSwitchItem(
                text = "Activar notificaciones push",
                icon = Icons.Default.Notifications,
                checked = pushNotificationsEnabled,
                onCheckedChange = { enabled ->
                    if (enabled) {
                        // Usuario quiere activar
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                // Ya tiene permiso → activar y suscribirse
                                settingsViewModel.setPushNotificationsEnabled(true)
                                FirebaseMessaging.getInstance().subscribeToTopic("all")
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Log.d("FCM", "Suscripción a notificaciones activada")
                                            Toast.makeText(
                                                context,
                                                "Notificaciones activadas",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // Pedir permiso
                                notificationPermissionLauncher.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            }
                        } else {
                            // Android < 13 → no necesita permiso
                            settingsViewModel.setPushNotificationsEnabled(true)
                            FirebaseMessaging.getInstance().subscribeToTopic("all")
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d("FCM", "Suscripción a notificaciones activada (<13)")
                                        Toast.makeText(
                                            context,
                                            "Notificaciones activadas",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                    } else {
                        // Usuario desactiva el switch
                        settingsViewModel.setPushNotificationsEnabled(false)
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("all")
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("FCM", "Suscripción a notificaciones desactivada")
                                    Toast.makeText(
                                        context,
                                        "Notificaciones desactivadas",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    }
                }
            )

            // Apariencia
            SettingItemDivider(title = "Apariencia")

            Column(
                Modifier
                    .selectableGroup()
                    .padding(horizontal = 16.dp)
            ) {
                ThemeOptionRow(
                    text = "Claro",
                    selected = currentTheme == "light",
                    onClick = { settingsViewModel.setAppTheme("light") }
                )
                ThemeOptionRow(
                    text = "Oscuro",
                    selected = currentTheme == "dark",
                    onClick = { settingsViewModel.setAppTheme("dark") }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón Cerrar Sesión
            Button(
                onClick = {
                    Log.d("SettingsScreen", "Logout button clicked!")
                    authViewModel.logout()

                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Cerrar Sesión")
            }
        }
    }
}

@Composable
fun SettingItemDivider(title: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
        )
        Divider()
    }
}

@Composable
fun SettingSwitchItem(
    text: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun ThemeOptionRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(start = 16.dp)
                .clickable { onClick() }
        )
    }
}

fun saveBitmapToCache(context: Context, bitmap: Bitmap): Uri {
    val file = File(context.cacheDir, "profile_${System.currentTimeMillis()}.jpg")
    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    out.flush()
    out.close()
    return file.toUri()
}