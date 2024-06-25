package ir.shariaty.thinktank.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import ir.shariaty.thinktank.databinding.FragmentNoteBinding
import ir.shariaty.thinktank.utils.NoteData


class NoteFragment : DialogFragment() {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var listener: DialogNextBtnClickListener
    private   var toDoData:NoteData?=null

    fun setListener(listener:DialogNextBtnClickListener){
        this.listener=listener
    }

    companion object{
        const val TAG="NoteFragment"
        @JvmStatic
        fun newInstance(taskId:String,task:String)=NoteFragment().apply {
           arguments=Bundle().apply {
               putString("taskId",taskId)
               putString("task",task)

           }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=FragmentNoteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments!=null){
            toDoData= NoteData(arguments?.getString("taskId").toString()
                ,arguments?.getString("task").toString())

            binding.todoEt.setText(toDoData?.task)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.save.setOnClickListener{
            val todoTask=binding.todoEt.text.toString()
            if(todoTask.isNotEmpty()){
                if(toDoData==null){
                    listener.onSaveTask(todoTask,binding.todoEt)
                }
              else{
                  toDoData?.task=todoTask
                    listener.onUpdateTask(toDoData!!,binding.todoEt)
              }
            }
            else{
                Toast.makeText(context,"Please type some task",Toast.LENGTH_SHORT).show()
            }
        }
        binding.Close.setOnClickListener{
            dismiss()
        }
    }
    interface DialogNextBtnClickListener{
        fun onSaveTask(todo:String,todoEt:TextInputEditText)
        fun onUpdateTask(toData: NoteData,todoEt:TextInputEditText)
    }
}