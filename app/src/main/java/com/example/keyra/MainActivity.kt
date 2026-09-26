package com.example.keyra

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.keyra.data.AppDatabase
import com.example.keyra.data.AccountRepository
import com.example.keyra.data.SessionStore
import com.example.keyra.data.UserEntity
import kotlinx.coroutines.launch
import com.example.keyra.ui.AboutScreen
import com.example.keyra.ui.AccountScreen
import com.example.keyra.ui.AddPasswordScreen
import com.example.keyra.ui.BiometricScreen
import com.example.keyra.ui.EditAccountScreen
import com.example.keyra.ui.ForgotPasswordScreen
import com.example.keyra.ui.GeneratePasswordScreen
import com.example.keyra.ui.HomeScreen
import com.example.keyra.ui.LoginScreen
import com.example.keyra.ui.RegisterScreen
import com.example.keyra.ui.SecurityScreen
import com.example.keyra.ui.SettingsScreen
import com.example.keyra.ui.ThemeScreen
import com.example.keyra.viewmodel.AccountViewModel
import com.example.keyra.viewmodel.AccountViewModelFactory

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KeyraTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation(activity = this)
                }
            }
        }
    }

    fun triggerBiometricAuth(onSuccess: () -> Unit, onCancel: () -> Unit = {}) {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val executor = ContextCompat.getMainExecutor(this)
                val biometricPrompt = BiometricPrompt(
                    this,
                    executor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                            super.onAuthenticationSucceeded(result)
                            onSuccess()
                        }
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                            super.onAuthenticationError(errorCode, errString)
                            // User press back or cancel
                            if (errorCode == BiometricPrompt.ERROR_USER_CANCELED || errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                onCancel()
                            } else {
                                Toast.makeText(this@MainActivity, "Error ($errorCode): $errString", Toast.LENGTH_SHORT).show()
                                onCancel()
                            }
                        }
                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(this@MainActivity, "Sidik jari tidak dikenali", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Buka Vault Keyra")
                    .setSubtitle("Gunakan sidik jari untuk mengakses password kamu")
                    .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "HP ini tidak memiliki sensor biometrik!", Toast.LENGTH_LONG).show()
                onSuccess()
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Sensor biometrik sedang sibuk.", Toast.LENGTH_LONG).show()
                onSuccess()
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "Belum ada sidik jari yang terdaftar di HP ini!", Toast.LENGTH_LONG).show()
                onSuccess()
            }
        }
    }
}

@Composable
fun KeyraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF4E8CF7),
            onPrimary = Color(0xFFFFFFFF),
            background = Color(0xFF0F1115),
            onBackground = Color(0xFFFFFFFF),
            surface = Color(0xFF334155),
            onSurface = Color(0xFFFFFFFF)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF2563EB),
            onPrimary = Color(0xFFFFFFFF),
            background = Color(0xFFF8FAFC),
            onBackground = Color(0xFF0F172A),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF0F172A)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun AppNavigation(activity: MainActivity) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val repository = AccountRepository(db.accountDao())
    val viewModel = remember { ViewModelProvider(activity, AccountViewModelFactory(repository)).get(AccountViewModel::class.java) }
    
    val allAccountsEntity by viewModel.allAccounts.collectAsState()
    // Convert AccountEntity to AccountItem untuk UI
    val accounts = allAccountsEntity.map { entity ->
        com.example.keyra.ui.AccountItem(
            id = entity.id.toString(),
            name = entity.name,
            email = entity.email,
            category = "Personal",
            isFavorite = entity.isFavorite,
            iconUri = entity.iconUri,
            password = entity.password,
            url = entity.url,
            notes = entity.notes
        )
    }
    
    val scope = rememberCoroutineScope()

    val savedEmail = remember { SessionStore.loadEmail(context) }
    val savedBio = remember { SessionStore.isBiometric(context) }
    var currentScreen by remember { mutableStateOf(if (savedEmail != null) "unlock" else "login") }
    var currentThemeSetting by remember { mutableStateOf("Dark Mode") }
    var biometricEnabled by remember { mutableStateOf(savedBio) }
    var editingAccount by remember { mutableStateOf<com.example.keyra.ui.AccountItem?>(null) }
    var generatedForAdd by remember { mutableStateOf<String?>(null) }
    var draftAdd by remember { mutableStateOf(com.example.keyra.ui.DraftAccount()) }
    val backStack = remember { mutableStateListOf(currentScreen) }

    LaunchedEffect(Unit) {
        if (savedEmail != null && savedBio) {
            activity.triggerBiometricAuth(
                onSuccess = { currentScreen = "home"; backStack.clear(); backStack.add("home") },
                onCancel = { /* Tetap di UnlockScreen jika user cancel */ }
            )
        }
    }
    fun nav(s: String) { backStack.add(s); currentScreen = s }
    fun goBack() { 
        if (currentScreen == "unlock") {
            activity.finish()
            return
        }
        if (backStack.size > 1) { 
            backStack.removeLast()
            currentScreen = backStack.last() 
        } else {
            activity.finish() 
        }
    }
    BackHandler(enabled = currentScreen != "login") { goBack() }

    val systemIsDark = isSystemInDarkTheme()
    val isDark = when (currentThemeSetting) {
        "Dark Mode" -> true
        "Light Mode" -> false
        else -> systemIsDark
    }

    KeyraTheme(darkTheme = isDark) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            when (currentScreen) {
                "unlock" -> com.example.keyra.ui.UnlockScreen(
                    email = SessionStore.loadEmail(context) ?: "",
                    biometricEnabled = biometricEnabled,
                    onUnlockWithPassword = { pass ->
                        scope.launch {
                            val e = SessionStore.loadEmail(context) ?: return@launch
                            val user = db.userDao().getUser(e)
                            if (user != null && user.password == pass) {
                                activity.runOnUiThread { nav("home") }
                            } else activity.runOnUiThread { Toast.makeText(activity, "Password salah!", Toast.LENGTH_SHORT).show() }
                        }
                    },
                    onUnlockWithBiometric = {
                        activity.triggerBiometricAuth(
                            onSuccess = { nav("home") },
                            onCancel = { /* Tetap di UnlockScreen */ }
                        )
                    },
                    onUseOtherAccount = { SessionStore.clearEmail(context); backStack.clear(); backStack.add("login"); currentScreen = "login" }
                )
                "login" -> LoginScreen(
                    onLoginClick = { email, pass ->
                        scope.launch {
                            val user = db.userDao().getUser(email)
                            if (user != null && user.password == pass) {
                                SessionStore.saveEmail(context, email)
                                // hanya pakai biometrik jika user pernah aktifkan, bukan paksa tiap login
                                if (biometricEnabled) {
                                    activity.triggerBiometricAuth(
                                        onSuccess = { activity.runOnUiThread { nav("home") } },
                                        onCancel = { /* Tetap di halaman login */ }
                                    )
                                } else {
                                    activity.runOnUiThread { nav("home") }
                                }
                            } else {
                                activity.runOnUiThread {
                                    Toast.makeText(activity, "Email atau Password salah!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    onSignUpClick = { nav("register") },
                    onForgotPasswordClick = { nav("forgot_password") }
                )
                "register" -> RegisterScreen(
                    onRegisterClick = { email, pass ->
                        scope.launch {
                            db.userDao().insertUser(UserEntity(email = email, password = pass, isVerified = true))
                            SessionStore.saveEmail(context, email)
                            activity.runOnUiThread {
                                Toast.makeText(activity, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show()
                                nav("biometric")
                            }
                        }
                    },
                    onLoginRedirectClick = { goBack() }
                )
                "biometric" -> BiometricScreen(
                    onSetupBiometricClick = {
                        biometricEnabled = true
                        SessionStore.setBiometric(context, true)
                        Toast.makeText(activity, "Biometrik Berhasil Disetting!", Toast.LENGTH_SHORT).show()
                        nav("home")
                    },
                    onSkipClick = {
                        biometricEnabled = false
                        SessionStore.setBiometric(context, false)
                        nav("home")
                    }
                )
                "home" -> HomeScreen(
                    accounts = accounts,
                    onAddPasswordClick = { nav("add_password") },
                    onGeneratePasswordClick = { nav("generate_password") },
                    onSettingsClick = { nav("settings") },
                    onSecurityClick = { nav("security") },
                    onLockAppClick = {
                        // ponytail: kunci kembali ke unlock (keep email), bukan login kosong
                        backStack.clear(); backStack.add("unlock"); currentScreen = "unlock"
                        Toast.makeText(activity, "Aplikasi Dikunci", Toast.LENGTH_SHORT).show()
                    },
                    onEditAccountClick = { account ->
                        editingAccount = account
                        nav("edit_account")
                    },
                    onDeleteAccount = { item ->
                        val entity = allAccountsEntity.find { it.id.toString() == item.id }
                        if (entity != null) viewModel.deleteAccount(entity)
                    },
                    onToggleFavorite = { item ->
                        val entity = allAccountsEntity.find { it.id.toString() == item.id }
                        if (entity != null) viewModel.toggleFavorite(entity)
                    }
                )
                "add_password" -> AddPasswordScreen(
                    draft = draftAdd,
                    prefillPassword = generatedForAdd,
                    onBackClick = { goBack() },
                    onGenerateClick = { nav("generate_password") },
                    onDraftChange = { d -> draftAdd = d },
                    onSaveClick = { finalDraft ->
                        val raw = finalDraft.url.trim()
                        val dom = raw.substringAfter("://").substringBefore("/").substringBefore("?").substringBefore("#")
                        val favUrl = if (dom.contains(".")) "https://www.google.com/s2/favicons?domain=$dom&sz=128" else null
                        viewModel.addAccount(
                            name = finalDraft.name,
                            email = finalDraft.username,
                            password = finalDraft.password,
                            url = finalDraft.url,
                            notes = finalDraft.notes,
                            iconUri = favUrl
                        )
                        generatedForAdd = null
                        draftAdd = com.example.keyra.ui.DraftAccount()
                        Toast.makeText(activity, "Akun berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        goBack()
                    }
                )
                "generate_password" -> GeneratePasswordScreen(
                    onBackClick = { goBack() },
                    onUsePassword = { pw -> generatedForAdd = pw; draftAdd = draftAdd.copy(password = pw); goBack() },
                    onCopyPassword = {}
                )
                "settings" -> SettingsScreen(
                    biometricEnabled = biometricEnabled,
                    onBiometricChange = { v -> biometricEnabled = v; SessionStore.setBiometric(context, v) },
                    onHomeClick = { goBack() },
                    onAccountClick = { nav("account") },
                    onBiometricClick = { enabled ->
                        val msg = if (enabled) "Biometrik diaktifkan" else "Biometrik dimatikan"
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                    },
                    onThemeClick = { nav("theme") },
                    onLanguageClick = { Toast.makeText(activity, "Pengaturan Bahasa", Toast.LENGTH_SHORT).show() },
                    onBackupClick = { Toast.makeText(activity, "Backup & Restore", Toast.LENGTH_SHORT).show() },
                    onAboutClick = { nav("about") },
                    onLogoutClick = {
                        SessionStore.clearEmail(context)
                        backStack.clear(); backStack.add("login"); currentScreen = "login"
                        Toast.makeText(activity, "Berhasil Keluar Akun", Toast.LENGTH_SHORT).show()
                    }
                )
                "account" -> AccountScreen(
                    onBackClick = { goBack() },
                    onChangeMasterPasswordClick = { Toast.makeText(activity, "Ubah Master Password", Toast.LENGTH_SHORT).show() }
                )
                "about" -> AboutScreen(onBackClick = { goBack() })
                "theme" -> ThemeScreen(
                    currentTheme = currentThemeSetting,
                    onThemeSelected = { selected -> currentThemeSetting = selected; Toast.makeText(activity, "Tema diubah ke $selected", Toast.LENGTH_SHORT).show() },
                    onBackClick = { goBack() }
                )
                "forgot_password" -> ForgotPasswordScreen(
                    onBackClick = { goBack() },
                    onResetClick = { goBack() }
                )
                "security" -> SecurityScreen(
                    accounts = accounts,
                    onBackClick = { goBack() },
                    onHomeClick = { backStack.clear(); backStack.add("login"); backStack.add("home"); currentScreen = "home" },
                    onSettingsClick = { backStack.clear(); backStack.add("login"); backStack.add("home"); backStack.add("settings"); currentScreen = "settings" },
                    onEditClick = { account ->
                        editingAccount = account
                        nav("edit_account")
                    }
                )
                "edit_account" -> {
                    if (editingAccount != null) {
                        EditAccountScreen(
                            account = editingAccount!!,
                            onBackClick = { goBack(); editingAccount = null },
                            onSaveClick = { updatedAccount ->
                                viewModel.updateAccount(
                                    id = updatedAccount.id.toIntOrNull() ?: 0,
                                    name = updatedAccount.name,
                                    email = updatedAccount.email,
                                    password = updatedAccount.password,
                                    url = updatedAccount.url,
                                    notes = updatedAccount.notes,
                                    isFavorite = updatedAccount.isFavorite,
                                    iconUri = updatedAccount.iconUri
                                )
                                Toast.makeText(activity, "Akun berhasil diupdate!", Toast.LENGTH_SHORT).show()
                                goBack()
                                editingAccount = null
                            }
                        )
                    }
                }
            }
        }
    }
}