package exemples

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream


fun main(){

    val serviceAccount = FileInputStream("./acces-a-dades.json")

    val options = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .setDatabaseUrl("https://acces-a-dades-c18e4-default-rtdb.firebaseio.com")
        .build()

    FirebaseApp.initializeApp(options)

}