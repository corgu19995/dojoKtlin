package com.dojo.santiagoparrar.dojokotlin.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.dojo.santiagoparrar.dojokotlin.model.Note
import java.security.AccessControlContext

/**
 * Created by santiago.parrar on 25/08/18.
 */
val DATABASE_NAME = "Notes"
val DATABASE_VERSION = 1

class DBhelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION){
    override fun onCreate(p0: SQLiteDatabase?) {
        val queryCreateTable = "CREATE TABLE "+ Tables.Notes.TABLE_NAME +
                " ("+ Tables.Notes._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Tables.Notes.COLUMN_TITLE + " TEXT NOT NULL,"+
                Tables.Notes.COLUMN_BODY+ " TEXT NOT NULL)";
        p0!!.execSQL(queryCreateTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun insertNote(note : Note){
        val db = this.writableDatabase
        val datos = ContentValues()
        datos.put(Tables.Notes.COLUMN_TITLE,note.getTitle())
        datos.put(Tables.Notes.COLUMN_BODY,note.getBody())

        var resultado = db!!.insert(Tables.Notes.TABLE_NAME,null,datos)

        if(resultado === -1.toLong()){
            Toast.makeText(context,"Hubo un error al crear la nota",Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(context,"Nota creada exitosamente",Toast.LENGTH_SHORT).show()
        }
        db!!.close()
    }

    fun getNotes() : MutableList<Note>{
        var notes : MutableList<Note> = ArrayList()
        val db = this.readableDatabase
        val querySelect = "SELECT * FROM" + Tables.Notes.TABLE_NAME
        var resultado = db!!.rawQuery(querySelect,null)
        if(resultado.moveToFirst())
        {
            //Se crean notas a medida que se encuentren
            //Se setean los valores
            do {
                var note = Note()
                note.setID(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes._ID)).toInt())

                note.setBody(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes.COLUMN_BODY)))

                note.setTitle(resultado.getString(resultado
                        .getColumnIndex(Tables.Notes.COLUMN_TITLE)))

                notes.add(note)
            }while (resultado.moveToNext())
        }
        //Cerrar conexiones
        resultado.close()
        db!!.close()
        return notes
    }

}
