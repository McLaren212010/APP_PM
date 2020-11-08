package com.example.app_pm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.app_pm.R
import com.example.app_pm.entities.Note

class NoteAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notes = emptyList<Note>() // Cached copy of notes

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteItemView: TextView = itemView.findViewById(R.id.Note)
        val idItemView: TextView = itemView.findViewById(R.id.ID)
        val priorityItemView: TextView = itemView.findViewById(R.id.Priority)

        fun initialize(notes: Note, action:OnNoteItemClickListener){
            noteItemView.text = notes.note
            idItemView.text = notes.id.toString()
            priorityItemView.text = notes.note
        }
    }


    fun getNoteAt(position: Int): Note = notes.get(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val current = notes[position]
        holder.noteItemView.text = current.note
        holder.idItemView.text ="ID: "+ current.id.toString()
        holder.priorityItemView.text = current.priority
    }


    internal fun setNotes(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size
}

interface OnNoteItemClickListener {
    fun onItemClick(note: Note, id: Note)

}


