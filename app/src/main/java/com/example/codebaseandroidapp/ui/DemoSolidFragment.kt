package com.example.codebaseandroidapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codebaseandroidapp.R
import com.example.codebaseandroidapp.base.BaseFragment
import com.example.codebaseandroidapp.base.OthersBaseFragment
import com.example.codebaseandroidapp.databinding.FragmentDemoSolidBinding
import com.example.codebaseandroidapp.di.FragmentModule
import dagger.hilt.android.AndroidEntryPoint
import java.io.Console
import javax.inject.Inject
import kotlin.math.E

/*
* Solid principles:
* S (Single responsibility principle): Mỗi class nên có 1 chức năng duy nhất
* O (Open and close principle): (Objects or entities should be open for extension,
*   but closed for modification) Một class nên được mở rộng chứ không nên thay
*   đổi bên trong nó.
* L (Liskov substitution principle): Các class con có thể thay thế class cha mà
*   không làm thay đổi tính đúng đắn.
* I (Interface segregation principle): Thay vì dùng 1 interface lớn, ta có thể chia
*   thành những interface nhỏ với nhiều mục đích cụ thể.
* D (Dependency inversion principle):
*   - Module cấp cao không nên phụ thuộc vào module cấp thấp. Cả 2 nên phụ thuộc vào
*   abstraction
*   - Interface (Abstraction) không nên phụ thuộc vào chi tiết mà ngược lại (Các
*   class giao tiếp với nhau thông qua interface, không phải thông qua implementation)
* */

@AndroidEntryPoint
class DemoSolidFragment : OthersBaseFragment<FragmentDemoSolidBinding>(FragmentDemoSolidBinding::inflate) {

    @Inject
    lateinit var userController: UserController

    @Inject
    @FragmentModule.PhoneController
    lateinit var phoneController: SenderController

    @Inject
    @FragmentModule.EmailController
    lateinit var emailController: SenderController

    companion object {
        @JvmStatic fun newInstance() = DemoSolidFragment()
    }

    override fun initObserve() {

    }

    override fun initialize() {
        val user = userController.getUserByPhone("0328562866")
        emailController.sendMessage(user, "xin chào các bạn").let {
            binding.tvEmailMessage.text = it
        }
        phoneController.sendMessage(user, "xin chào các bạn").let {
            binding.tvPhoneMessage.text = it
        }
    }
}

class User(
    var phone: String,
    var name: String,
    var eName: String
) {
    override fun toString(): String {
        return "User(phone='$phone', name='$name', eName='$eName')"
    }
}

class UserController @Inject constructor() {
    fun getUserByPhone(phone: String): User {
        return User(phone, "Duy Hiếu", "hieupnd")
    }
}

abstract class Sender {
    abstract fun sendMessage(user: User, text: String) : String
}

class EmailSender @Inject constructor() : Sender() {

    override fun sendMessage(user: User, text: String) : String {
        return "${user.eName} - $text"
    }
}

class PhoneSender @Inject constructor() : Sender() {

    override fun sendMessage(user: User, text: String) : String {
        return "${user.phone} - $text"
    }
}

class SenderController @Inject constructor(private val sender: Sender) {

    fun sendMessage(user: User, text: String) : String {
        return sender.sendMessage(user, text)
    }
}