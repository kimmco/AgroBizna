package com.cokimutai.agrobizna.admin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokimutai.agrobizna.supports.FarmDetails
import com.google.firebase.database.*
import kotlinx.coroutines.launch


class AdminTeaRecViewModel : ViewModel() {
//
//    private val _teaNumbersList = MutableLiveData<List<FarmDetails>>()
//
//    val teaNumbersList: LiveData<List<FarmDetails>>
//        get() = _teaNumbersList
//
//    private val _teaNumbers = MutableLiveData<FarmDetails>()
//
//    val teaNumbers: LiveData<FarmDetails>
//        get() = _teaNumbers
//
//    init {
//        getTeaNumbers()
//    }
//
//    private fun getTeaNumbers() {
//
//        val teaNumbersListArray =  ArrayList<FarmDetails>()
//
//        viewModelScope.launch {
//            try {
//                FirebaseDatabase.getInstance().getReference("items")
//                    .addChildEventListener(object : ChildEventListener {
//
//                        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//
//                            if (snapshot.hasChildren()) {
//                                for (snap in snapshot.children) {
//                                    val selectedTeaItem =
//                                        snap.getValue(FarmDetails::class.java)
//
//                                    /*  if (selectedTeaItem?.pluckingWeight == null){
//                                            return
//                                        }else {
//                                            _teaNumbersList.value = listOf(selectedTeaItem) //.pluckingWeight
//                                        }*/
//                                   // Log.d("TEAS", selectedTeaItem.toString())
//
//                                    // list.add(0, selectedTeaItem)
//                                    if (selectedTeaItem != null) {
//                                        val list: MutableList<FarmDetails> = mutableListOf<FarmDetails>()
//                                        list.add(selectedTeaItem)
//
//                                    // selectedTeaItem?.let { list.add(it) }
//                                    teaNumbersListArray.addAll(list)
//                                    _teaNumbersList.setValue(teaNumbersListArray)
//                                     //   val teaAdapter = TeaAdapter()
//                                        teaAdapter.notifyDataSetChanged()
//                                        Log.d("TEAS", teaNumbersListArray.toString())
//
//                                     }
//                                }
//                            }
//                        }
//
//                        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                            if (snapshot.exists()) {
//                                for (snap in snapshot.children) {
//                                    val selectedTeaItem =
//                                        snap.getValue(FarmDetails::class.java)
//
//                                    if (selectedTeaItem?.pluckingWeight == null){
//                                        return
//                                    }else {
//                                        _teaNumbersList.value = listOf(selectedTeaItem) //.pluckingWeight
//                                    }
//                                    Log.d("TEAS", selectedTeaItem.toString())
//                                    //  val list: MutableList<FarmDetails> =
//                                    //          mutableListOf<FarmDetails>()
//                                    // selectedTeaItem?.let { list.add(it) }
//                                    //  teaNumbersListArray.addAll(list)
//                                    // _teaNumbersList.setValue(teaNumbersListArray)
//                                }
//                            }
//                        }
//
//                        override fun onChildRemoved(snapshot: DataSnapshot) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//                            TODO("Not yet implemented")
//                        }
//
//                        override fun onCancelled(error: DatabaseError) {
//                            TODO("Not yet implemented")
//                        }
//
//                    /*    override fun onDataChange(snapshot: DataSnapshot?) {
//                            if (snapshot.exists()) {
//                                for (snap in snapshot.children) {
//                                    val selectedTeaItem =
//                                            snap.getValue(FarmDetails::class.java)
//
//                                /*    if (selectedTeaItem?.pluckingWeight == null){
//                                      //  return
//                                    }else {
//                                        _teaNumbers.value = selectedTeaItem
//                                    } */
//                                    Log.d("TEAS", selectedTeaItem.toString())
//                                    val list: MutableList<FarmDetails> =
//                                        mutableListOf<FarmDetails>()
//                                    selectedTeaItem?.let { list.add(it) }
//                                    teaNumbersListArray.addAll(list)
//                                    _teaNumbersList.setValue(teaNumbersListArray)
//                                }
//                            }
//                        }
//                        */
//                    })
//            } catch (e: Exception) {
//
//                _teaNumbersList.value = ArrayList()
//
//            }
//
//        }
//    }
 }