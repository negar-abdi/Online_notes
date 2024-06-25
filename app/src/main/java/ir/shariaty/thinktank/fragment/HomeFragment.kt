package ir.shariaty.thinktank.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ir.shariaty.thinktank.databinding.FragmentHomeBinding
import ir.shariaty.thinktank.utils.NoteAdapter
import ir.shariaty.thinktank.utils.NoteData


class HomeFragment : Fragment(), NoteFragment.DialogNextBtnClickListener,
    NoteAdapter.ToDoAdapterClicksInterface {

 private lateinit var auth:FirebaseAuth
 private lateinit var databaseRef:DatabaseReference
 private lateinit var navController: NavController
 private lateinit var binding: FragmentHomeBinding
 private  var popUpFragment: NoteFragment?=null
 private lateinit var adapter: NoteAdapter
 private lateinit var mList: MutableList<NoteData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
    }

    private fun registerEvents() {
        binding.plus.setOnClickListener{
            if(popUpFragment!=null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment= NoteFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(childFragmentManager,NoteFragment.TAG)
        }
    }

    private fun init(view: View){
        navController=Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        databaseRef=FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.layoutManager=LinearLayoutManager(context)
        mList= mutableListOf()
        adapter= NoteAdapter(mList)
        adapter.setListener(this)
        binding.recyclerview.adapter=adapter
    }

    private fun getDataFromFirebase(){
        databaseRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(taskSnapshot in snapshot.children){
                    val todoTask=taskSnapshot.key?.let{
                        NoteData(it,taskSnapshot.value.toString())
                    }
                    if(todoTask!=null){
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveTask(todo: String, todoEt: TextInputEditText) {
      databaseRef.push().setValue(todo).addOnCompleteListener{
         if(it.isSuccessful){
             Toast.makeText(context,"Note saved successfully!",Toast.LENGTH_SHORT).show()

         }
         else{
             Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
         }
          todoEt.text=null
          popUpFragment!!.dismiss()
     }
    }

    override fun onUpdateTask(toData: NoteData, todoEt: TextInputEditText) {
        val map=HashMap<String,Any>()
        map[toData.taskId]=toData.task
        databaseRef.updateChildren(map).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context,"Updated Successfuly",Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            todoEt.text=null
           popUpFragment!!.dismiss()
        }
    }

    override fun onDeleteNote(toDoData: NoteData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
           if(it.isSuccessful){
               Toast.makeText(context,"Deleted Successfully",Toast.LENGTH_SHORT).show()
           }else{
               Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
           }
       }
    }

    override fun onEditNote(toDoData: NoteData) {
      if(popUpFragment!=null)
          childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        popUpFragment=NoteFragment.newInstance(toDoData.taskId,toDoData.task)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager,NoteFragment.TAG)
    }

}