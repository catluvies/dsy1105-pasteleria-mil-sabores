package cl.duoc.dsy1105.pasteleriamilsabores.utils

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

/**
 * Helper para subir imágenes a Firebase Storage
 */
object FirebaseStorageHelper {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    /**
     * Sube una imagen a Firebase Storage y devuelve la URL pública
     * @param uri URI de la imagen local
     * @param context Contexto de Android
     * @return URL de la imagen en Firebase Storage
     */
    suspend fun uploadImage(uri: Uri, context: Context): Result<String> {
        return try {
            // Generar nombre único para la imagen
            val fileName = "product_${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child("products/$fileName")

            // Leer bytes del URI
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return Result.failure(Exception("No se pudo leer la imagen"))

            val bytes = inputStream.readBytes()
            inputStream.close()

            // Subir a Firebase Storage
            val uploadTask = imageRef.putBytes(bytes).await()

            // Obtener URL de descarga
            val downloadUrl = imageRef.downloadUrl.await()

            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Elimina una imagen de Firebase Storage dado su URL
     * @param imageUrl URL de la imagen a eliminar
     */
    suspend fun deleteImage(imageUrl: String): Result<Unit> {
        return try {
            if (!imageUrl.contains("firebasestorage.googleapis.com")) {
                // No es una imagen de Firebase, no hacer nada
                return Result.success(Unit)
            }

            val imageRef = storage.getReferenceFromUrl(imageUrl)
            imageRef.delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            // Si falla, no es crítico
            Result.failure(e)
        }
    }
}
