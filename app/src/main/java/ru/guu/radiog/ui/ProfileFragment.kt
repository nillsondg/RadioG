package ru.guu.radiog.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import ru.guu.radiog.R
import ru.guu.radiog.RadioPreferenceManager
import ru.guu.radiog.ui.login.LoginActivity

class ProfileFragment : Fragment() {

    lateinit var mName: TextView
    lateinit var mEmail: TextView
    lateinit var mRole: TextView
    lateinit var mExitButton: Button
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_profile, container, false)
        mName = view.findViewById(R.id.name)
        mEmail = view.findViewById(R.id.email)
        mRole = view.findViewById(R.id.role)
        val userData = RadioPreferenceManager.newInstance(context).userData ?: return view
        mName.text = userData.cn
        mEmail.text = userData.mail
        mRole.text = userData.title
        mExitButton = view.findViewById(R.id.exit_button)
        mExitButton.setOnClickListener { _: View ->
            RadioPreferenceManager.newInstance(context).singedIn = false
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.finish()
        }
        return view
    }
}
