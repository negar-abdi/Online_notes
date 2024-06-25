package ir.shariaty.thinktank.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.animation.AnimatableView.Listener
import ir.shariaty.thinktank.databinding.FragmentNoteBinding
import ir.shariaty.thinktank.databinding.ItemEachNoteBinding
import java.lang.reflect.Type
import java.text.FieldPosition

class NoteAdapter (private val list: MutableList<NoteData>):
RecyclerView.Adapter<NoteAdapter.ToDoViewHolder>(){
     private var listener:ToDoAdapterClicksInterface?=null
     fun setListener(listener: ToDoAdapterClicksInterface){
         this.listener=listener
     }
    inner class ToDoViewHolder( val binding: ItemEachNoteBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent:ViewGroup,viewType: Int):ToDoViewHolder{
       val binding=ItemEachNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }
    override  fun onBindViewHolder(holder: ToDoViewHolder,position: Int){
        with(holder) {
            with(list[position]) {

                binding.todoTask.text = this.task
                binding.delete.setOnClickListener {
                 listener?.onDeleteNote(this)
                }
                binding.edit.setOnClickListener {
                 listener?.onEditNote(this)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
interface ToDoAdapterClicksInterface{
    fun onDeleteNote(toDoData: NoteData)
    fun onEditNote(toDoData: NoteData)
}

}