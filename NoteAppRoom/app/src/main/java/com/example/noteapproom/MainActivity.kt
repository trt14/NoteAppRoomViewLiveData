package com.example.noteapproom

import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapproom.data.Note

import com.example.noteapproom.data.DataDatabase

class MainActivity : AppCompatActivity() {
    lateinit var save: Button
    lateinit var note: EditText
    lateinit var recycle: RecyclerView
    lateinit var rvAdapter:RecycleView
    lateinit var mainViewModel: myViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(this).get(myViewModel::class.java)
        mainViewModel.getNotes().observe(this, {
                notes -> rvAdapter.update(notes)
        })
        setContentView(R.layout.activity_main)
        note=findViewById(R.id.edtxt)
        save=findViewById(R.id.btn)
        recycle=findViewById(R.id.rvNote)
        rvAdapter = RecycleView(this)
        recycle.adapter = rvAdapter
        recycle.layoutManager = LinearLayoutManager(this)

        save.setOnClickListener {
            val s1=note.text.toString()
            if(s1.isNotEmpty()) {
                //ob.DataDao().insertnote(Note(0, s1))
                mainViewModel.addNote(s1)

                note.text.clear()
                Toast.makeText(applicationContext, "data successfully added", Toast.LENGTH_SHORT)
                    .show()



            }
            else{
                Toast.makeText(applicationContext,"please add note first",Toast.LENGTH_SHORT).show()
            }
        }

    }


    fun update(n: Note) {
        var at= AlertDialog.Builder(this)
        at.setTitle("Edit Note")
        val input = EditText(this)
        input.setHint(n.note)
        at.setView(input)
        at.setPositiveButton("Update", DialogInterface.OnClickListener { dialogInterface, i ->
            if(input.text.isNotEmpty()) {
                mainViewModel.editNote(n.id, input.text.toString())
                Toast.makeText(applicationContext, "data successfully Edited", Toast.LENGTH_SHORT)
                    .show()

            }else if(input.text.isEmpty()){
                Toast.makeText(applicationContext, "Field is empty", Toast.LENGTH_SHORT)
                    .show()
            }


        })

        at.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        at.show()
    }

    fun confirm(note: Note){

        var at= AlertDialog.Builder(this)
        at.setTitle("delete Note")
        at.setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->
            deleteitem(note)

        })
        at.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        at.show()
    }

    private fun deleteitem(note: Note) {
        val ob= DataDatabase.getinstant(applicationContext)

        mainViewModel.deleteNote(note.id)
        Toast.makeText(applicationContext, "data successfully Deleted", Toast.LENGTH_SHORT)
            .show()


    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        }

    }
}
