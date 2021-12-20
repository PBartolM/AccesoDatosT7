package exercisis



import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JComboBox
import javax.swing.JTextArea
import java.awt.BorderLayout
import javax.swing.JPanel
import java.awt.FlowLayout
import java.awt.Color
import javax.swing.JScrollPane
import java.io.FileInputStream
import com.google.firebase.FirebaseOptions
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import java.awt.EventQueue


class EstadisticaRD : JFrame() {

    val etProv = JLabel("Provincia: ")
    val provincia = JComboBox<String>()

    val etiqueta = JLabel("Missatges:")
    val area = JTextArea()


    // en iniciar posem un contenidor per als elements anteriors
    init {
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        setBounds(100, 100, 450, 450)
        setLayout(BorderLayout())
        // contenidor per als elements
        //Hi haurà títol. Panell de dalt: últim missatge. Panell de baix: per a introduir missatge. Panell central: tot el xat

        val panell1 = JPanel(FlowLayout())
        panell1.add(etProv)
        panell1.add(provincia)
        getContentPane().add(panell1, BorderLayout.NORTH)

        val panell2 = JPanel(BorderLayout())
        panell2.add(etiqueta, BorderLayout.NORTH)
        area.setForeground(Color.blue)
        area.setEditable(false)
        val scroll = JScrollPane(area)
        panell2.add(scroll, BorderLayout.CENTER)
        getContentPane().add(panell2, BorderLayout.CENTER)

        setVisible(true)

        val serviceAccount = FileInputStream("xat-ad-firebase-adminsdk-my2d0-8c69944b34.json")

        val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://xat-ad.firebaseio.com").build()

        FirebaseApp.initializeApp(options)

        // Posar tota la llista de províncies al JComboBox anomenat provincia


        val estadistica = FirebaseDatabase.getInstance().getReference("EstadisticaVariacioPoblacional")
        estadistica.addValueEventListener(object : ValueEventListener {
            override
            fun onDataChange(dataSnapshot: DataSnapshot) {
                for (i in 0 until dataSnapshot.children.count()){
                    provincia.addItem(dataSnapshot.child("$i").child("nombre").value.toString())
                }
            }
            override
            fun onCancelled(error: DatabaseError ) {
            }
        })









        provincia.addActionListener() {
            // Posar la informació de tots els anys en el JTextArea anomenat area
            var e =provincia.selectedIndex
            area.text=""
            estadistica.addValueEventListener(object : ValueEventListener {
                override
                fun onDataChange(dataSnapshot: DataSnapshot) {
                    val data =dataSnapshot.child("$e").child("data")
                    for (i in 0 until data.children.count()){
                        val anyo =data.child("$i").child("nombrePeriodo").value.toString()
                        val elValor =data.child("$i").child("valor").value.toString()
                        area.text=area.text+"$anyo : $elValor \n"
                    }
                }
                override
                fun onCancelled(error: DatabaseError ) {
                }
            })

        }
    }
}

fun main(args: Array<String>) {
    EventQueue.invokeLater {
        EstadisticaRD().isVisible = true
    }
}

