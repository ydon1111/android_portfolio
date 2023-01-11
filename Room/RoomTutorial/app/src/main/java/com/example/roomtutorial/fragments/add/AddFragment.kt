package com.example.roomtutorial.fragments.add

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roomtutorial.R
import com.example.roomtutorial.model.User
import com.example.roomtutorial.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*


class AddFragment : Fragment() {

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_add, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        view.add_btn.setOnClickListener {
            insertDataToDatabase()
        }

        return view
    }

    private fun insertDataToDatabase() {
        val firstName = addFirstName_et.text.toString()
        val lastName = addLastName_et.text.toString()
        val age = addAge_et.text

        if (inputCheck(firstName, lastName, age)) {
            // Create User object
            val user = User(0, firstName, lastName, Integer.parseInt(age.toString()))
            // Add Data to Database
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(), "저장 성공", Toast.LENGTH_LONG).show()
            // Navigate Back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(), "저장 실패, 빈칸을 채우세요", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(firstName: String, lastName: String, age: Editable): Boolean {
        return !(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(lastName) && age.isEmpty())
    }


}
