package com.zilaneleftoz.sportwavestore.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zilaneleftoz.sportwavestore.common.Resource
import com.zilaneleftoz.sportwavestore.data.model.User
import kotlinx.coroutines.tasks.await

class UserAuthenticationRepository (
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore){

    suspend fun signUp(user: User, password: String): Resource<Unit> {
        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(user.email.orEmpty(), password).await()

            if (result.user != null) {
                val user = User(
                    name = user.name,
                    surname = user.surname,
                    email = user.email
                )
                firestore.collection("users").document(getUserId()).set(user).await()

                Resource.Success(Unit)
            }
            else {
                Resource.Error("An error occurred during sign up")
            }
        } catch (e: Exception) {
            Resource.Error(e.message.orEmpty())
        }
    }

    suspend fun signIn(email: String, password: String): Resource<Unit> {

        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            if (result.user != null) {
                Resource.Success(Unit)
            } else {
                Resource.Error("Oturum açma sırasında bir sorun oluştu")
            }
        } catch (e: Exception) {
            Resource.Error("Şifre veya e-posta hatalı")
        }
    }

    fun getUserId(): String  = firebaseAuth.currentUser?.uid.orEmpty()


    suspend fun getUser(): User {
        val user = firestore.collection("users").document(getUserId()).get().await()

        val name = user.data?.get("name") as String
        val surname = user.data?.get("surname") as String
        val email = user.data?.get("email") as String

        return User(
            name = name,
            surname = surname,
            email = email

        )
    }
}