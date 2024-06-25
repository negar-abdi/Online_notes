package ir.shariaty.thinktank.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import ir.shariaty.thinktank.R
import ir.shariaty.thinktank.databinding.FragmentLoginBinding
import ir.shariaty.thinktank.databinding.FragmentSignUpBinding


class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var navControl: NavController
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }



    private fun init(view: View){
        navControl= Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun registerEvents() {
        binding.showSignUp.setOnClickListener{
            navControl.navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        binding.signUpBtn.setOnClickListener{
            val email=binding.email.text.toString().trim()
            val pass= binding.password.text.toString().trim()


            if(email.isNotEmpty() && pass.isNotEmpty()  ){
                binding.progressBar.visibility=View.VISIBLE

                    auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(
                        OnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(context,"Login Successfully", Toast.LENGTH_SHORT).show()
                                navControl.navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                            else{
                                Toast.makeText(context,it.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                            binding.progressBar.visibility=View.GONE
                        })

            }else{
                Toast.makeText(context,"Empty fields not allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }

}