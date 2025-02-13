//package com.example.metube.presentation
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.metube.R
//import com.example.metube.databinding.ActivityLoginBinding
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.Identity
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//
//class LoginActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityLoginBinding
//    private lateinit var auth: FirebaseAuth
//    private lateinit var oneTapClient: SignInClient
//    private lateinit var signInRequest: BeginSignInRequest
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        auth = FirebaseAuth.getInstance()
//
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setServerClientId(getString(R.string.default_web_client_id))
//                    .build()
//            ).build()
//
//        binding.btnGoogleSignIn.setOnClickListener {
//            signInWithGoogle()
//        }
//    }
//
//    private fun signInWithGoogle() {
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    val signInIntent = result.pendingIntent.intentSender
//                    startIntentSenderForResult(signInIntent, 1001, null, 0, 0, 0)
//                } catch (e: Exception) {
//                    Toast.makeText(this, "Erro ao iniciar login: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(this, "Falha ao abrir Google Sign-In", Toast.LENGTH_SHORT).show()
//            }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == 1001) {
//            try {
//                val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                val idToken = credential.googleIdToken
//                if (idToken != null) {
//                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                    auth.signInWithCredential(firebaseCredential)
//                        .addOnCompleteListener(this) { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
//                                startActivity(Intent(this, MainActivity::class.java))
//                                finish()
//                            } else {
//                                Toast.makeText(this, "Erro no login", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                }
//            } catch (e: ApiException) {
//                Toast.makeText(this, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//}
