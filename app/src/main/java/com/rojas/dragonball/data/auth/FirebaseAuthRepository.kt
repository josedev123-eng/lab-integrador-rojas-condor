package com.rojas.dragonball.data.auth

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rojas.dragonball.domain.model.Rol
import com.rojas.dragonball.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> = try {
        val uid = auth.signInWithEmailAndPassword(correo, clave).await().user!!.uid
        Result.success(leerSesion(uid, correo))
    } catch (e: FirebaseNetworkException) {
        Result.failure(Exception("No hay conexión a internet"))
    } catch (e: Exception) {
        Result.failure(Exception("Correo o contraseña incorrectos"))
    }

    override suspend fun sesionActual(): Sesion? {
        val u = auth.currentUser ?: return null      // nadie ha iniciado sesion
        return leerSesion(u.uid, u.email ?: "")
    }

    override fun cerrarSesion() = auth.signOut()

    // El rol no se decide en la app: se lee de Firestore (usuarios/{uid})
    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val rol = try {
            val doc = db.collection("usuarios").document(uid).get().await()
            if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        } catch (e: Exception) {
            Rol.USUARIO       // si no se puede leer, entra como usuario normal
        }
        return Sesion(uid, correo, rol)
    }
}
