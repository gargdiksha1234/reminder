package ru.ifr0z.notify.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.workManagerr.notify.R
import com.workManagerr.notify.databinding.LayoutDetailsListBinding
import com.workManagerr.notify.db.WorkListEntity
import ru.ifr0z.notify.utils.AppUtils.calculateTimeBetweenTwoDatesNotification
import ru.ifr0z.notify.utils.AppUtils.formatDateTimeFromMillis
import ru.ifr0z.notify.utils.constants.AppConstants.COMPLETE
import ru.ifr0z.notify.utils.constants.AppConstants.TIME_OVER
import javax.inject.Inject


class WorkAdapter @Inject constructor() :
    RecyclerView.Adapter<WorkAdapter.SetUserDetails>() {
    var userList: ArrayList<WorkListEntity>? = ArrayList()


    private lateinit var onTapWorkDoneCallBack: (Int, Boolean) -> Unit
    private lateinit var onDetailCallBack: (Int) -> Unit
    private lateinit var onTimeRemainingCallBack: () -> Unit
    private lateinit var onDeleteCallBack: (Int) -> Unit


    fun onWorkDoneClick(listener: (Int, Boolean) -> Unit) {
        onTapWorkDoneCallBack = listener
    }
    fun onDetailsClick(listener: (Int) -> Unit) {
        onDetailCallBack = listener
    }
    fun onTimeRemainingCallBack(listener: () -> Unit) {
        onTimeRemainingCallBack = listener
    }

    fun onDeleteCallBack(listener: (Int) -> Unit) {
        onDeleteCallBack = listener
    }



    @SuppressLint("NotifyDataSetChanged")
    fun setData(arrayList: ArrayList<WorkListEntity>) {
        userList = arrayList
        notifyDataSetChanged()


    }

    inner class SetUserDetails(private val binding: LayoutDetailsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(position: Int) {

            binding.title.text = userList?.get(position)?.title.toString()
            binding.tvDes.text = userList?.get(position)?.desc.toString()
            binding.tvTime.text = formatDateTimeFromMillis(userList?.get(position)?.createdDate!!)

            val takeDiff = calculateTimeBetweenTwoDatesNotification(userList?.get(position)?.createdDate!!)
            if (TIME_OVER == takeDiff) {
                if (userList?.get(position)?.isWorkComplete==true)
                    binding.remianing.text = COMPLETE

                else {
                    binding.remianing.text = TIME_OVER
                    binding.remianing.setTextColor(Color.BLUE)
                }
            } else {
                binding.remianing.text = "$takeDiff Rem"
                binding.remianing.setTextColor(Color.RED)
            }
            binding.isVerified.setImageResource(if (userList?.get(position)?.isWorkComplete == true) R.drawable.ic_verified_account else R.drawable.ic_account_not_verified)


            binding.isVerified.setOnClickListener {
                if (takeDiff == TIME_OVER)
                    onTapWorkDoneCallBack(position, userList?.get(position)?.isWorkComplete!!)
                else onTimeRemainingCallBack()
            }
            binding.root.setOnClickListener {
                onDetailCallBack(position)
            }
            binding.ivDelete.setOnClickListener {
                onDeleteCallBack(position)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SetUserDetails {
        return SetUserDetails(
            LayoutDetailsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {

        return userList?.size ?: 0
    }

    override fun onBindViewHolder(holder: SetUserDetails, position: Int) {

        holder.bindData(position)
    }


}
