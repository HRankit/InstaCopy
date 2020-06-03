package com.demo.instacopy.feed.adapter

/*
* Created by hrank8t on 03-06-2020 - 16:31:45.
*/



import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.demo.instacopy.R
import com.demo.instacopy.feed.models.Photos
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_single_photo.view.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.roundToInt


@Suppress("ControlFlowWithEmptyBody")
class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val typeLike: Int = 1
    private val typeUnlike: Int = 2


    @SuppressLint("SetTextI18n")
    fun bind(photo: Photos?) {
        if (photo != null) {
            val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
            //set the background color of the bitmap to GRAY
            bitmap.eraseColor(Color.GRAY)
            val d: Drawable = BitmapDrawable(itemView.context.resources, bitmap)
            Picasso.get().load(photo.urls.small)
                .placeholder(d)
                .error(R.drawable.ic_launcher_background)

                .priority(Picasso.Priority.HIGH)
                .into(itemView.img_banner, object : Callback {
                    override fun onSuccess() {
                        itemView.imageLoader.visibility = GONE
                    }

                    override fun onError(e: Exception?) {
                        Timber.e(e)
                    }
                })

            Picasso.get().load(photo.user.profile_image.small)
                .placeholder(d)
                .error(R.drawable.ic_launcher_background)
//                    .config(Bitmap.Config.RGB_565)
                .priority(Picasso.Priority.HIGH)
                .into(itemView.userProfileImage, object : Callback {
                    override fun onSuccess() {
                        itemView.imageLoader.visibility = GONE

                    }

                    override fun onError(e: Exception?) {
                        Timber.e(e)
                    }
                })

            if (photo.description.isNullOrEmpty()) {
                itemView.txt_subtitle.visibility = INVISIBLE
            } else {
                itemView.txt_subtitle.text = photo.description
                itemView.txt_subtitle.visibility = VISIBLE
            }

            itemView.txt_user_name.text = photo.user.name


            itemView.txt_likes.text = "${photo.likes} likes"
            itemView.txt_created_at.text = timeAgo(photo.created_at)


            itemView.heart_imageview.setColorFilter(mColorControlNormal)
            setImage(R.drawable.ic_favorite_border_black_24dp)

            if (photo.il == null) {
                photo.il = false
            }



            itemView.heart_imageview.setOnClickListener {
//                Null Safety for nullable variables
                photo.il = if (photo.il!!) {

                    itemView.heart_imageview.setColorFilter(mColorControlNormal)
                    setImage(R.drawable.ic_favorite_border_black_24dp)
                    sendFeedLike(typeUnlike, photo)
                    photo.il = false
                    decreaseLikesCount()
                    false
                } else {
                    animateHeart(itemView.heart_imageview)
                    setImage(R.drawable.ic_favorite_black_24dp)

                    itemView.heart_imageview.setColorFilter(Color.RED)
                    sendFeedLike(typeLike, photo)
                    photo.il = true
                    increaseLikesCount()

                    true
                }
            }


        }
    }


    private fun setImage(int: Int) {
        itemView.heart_imageview.setImageDrawable(
            AppCompatResources.getDrawable(
                itemView.context,
                int
            )
        )
    }

    @Suppress("UNUSED_PARAMETER")
    private fun sendFeedLike(likeUnlike: Int, photo: Photos) {
        //If Needed send this to server or save to local Database.
        if (likeUnlike == typeLike) {
            // User has liked
        } else {
            // User has dis-liked
        }
    }


    @SuppressLint("SetTextI18n")
    private fun increaseLikesCount() {
        var text: String = itemView.txt_likes.text.toString()
        text = text.replace(" likes", "")
        val num: Int = text.toInt() + 1
        itemView.txt_likes.text = "$num likes"
    }

    @SuppressLint("SetTextI18n")
    private fun decreaseLikesCount() {
        var text: String = itemView.txt_likes.text.toString()
        text = text.replace(" likes", "")
        val num: Int = text.toInt() - 1
        itemView.txt_likes.text = "$num likes"
    }


    private fun animateHeart(view: ImageView) {
        val scaleAnimation = ScaleAnimation(
            0.0f, 1.5f, 0.0f, 1.5f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )

        val animation = AnimationSet(true)
        animation.addAnimation(scaleAnimation)

        animation.duration = 600
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                val scaleAnimation1 = ScaleAnimation(
                    1.5f, 1.0f, 1.5f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                )
                val shrinkAnimation = AnimationSet(true)
                shrinkAnimation.addAnimation(scaleAnimation1)
                shrinkAnimation.duration = 600
                shrinkAnimation.fillAfter = true
                view.startAnimation(shrinkAnimation)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(animation)


    }

    private fun timeAgo(created_at: String): String? {

        val inPattern = "yyyy-MM-dd'T'HH:mm:ssZ"
        val inFormat = SimpleDateFormat(inPattern, Locale.getDefault())
        val inDate = inFormat.parse(created_at)
        return getTimeAgo(inDate, itemView.context)
    }


    private fun getTimeAgo(date: Date?, ctx: Context): String? {
        if (date == null) {
            return null
        }
        val time = date.time
        val curDate: Date = currentDate()
        val now = curDate.time
        if (time > now || time <= 0) {
            return null
        }
        val dim = getTimeDistanceInMinutes(time)
        val timeAgo: String?
        // When operator instead of If Else/Switch with single line return.
        timeAgo = when (dim) {
            0 -> {
                ctx.resources.getString(R.string.date_util_term_less) + " " + ctx.resources
                    .getString(R.string.date_util_term_a) + " " + ctx.resources
                    .getString(R.string.date_util_unit_minute)
            }
            1 -> {
                return "1 " + ctx.resources.getString(R.string.date_util_unit_minute)
            }
            in 2..44 -> {
                dim.toString() + " " + ctx.resources.getString(R.string.date_util_unit_minutes)
            }
            in 45..89 -> {
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + ctx.resources
                    .getString(R.string.date_util_term_an) + " " + ctx.resources
                    .getString(R.string.date_util_unit_hour)
            }
            in 90..1439 -> {
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + (dim / 60.toFloat()).roundToInt() + " " + ctx.resources
                    .getString(R.string.date_util_unit_hours)
            }
            in 1440..2519 -> {
                "1 " + ctx.resources.getString(R.string.date_util_unit_day)
            }
            in 2520..43199 -> {
                (dim / 1440.toFloat()).roundToInt().toString() + " " + ctx.resources
                    .getString(R.string.date_util_unit_days)
            }
            in 43200..86399 -> {
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + ctx.resources
                    .getString(R.string.date_util_term_a) + " " + ctx.resources
                    .getString(R.string.date_util_unit_month)
            }
            in 86400..525599 -> {
                (dim / 43200.toFloat()).roundToInt().toString() + " " + ctx.resources
                    .getString(R.string.date_util_unit_months)
            }
            in 525600..655199 -> {
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + ctx.resources
                    .getString(R.string.date_util_term_a) + " " + ctx.resources
                    .getString(R.string.date_util_unit_year)
            }
            in 655200..914399 -> {
                ctx.resources.getString(R.string.date_util_prefix_over) + " " + ctx.resources
                    .getString(R.string.date_util_term_a) + " " + ctx.resources
                    .getString(R.string.date_util_unit_year)
            }
            in 914400..1051199 -> {
                ctx.resources.getString(R.string.date_util_prefix_almost) + " 2 " + ctx.resources.getString(
                    R.string.date_util_unit_years
                )
            }
            else -> {
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + (dim / 525600.toFloat()).roundToInt() + " " + ctx.resources
                    .getString(R.string.date_util_unit_years)
            }
        }
        return timeAgo + " " + ctx.resources.getString(R.string.date_util_suffix)
    }

    private fun getTimeDistanceInMinutes(time: Long): Int {
        val timeDistance: Long = currentDate().time - time
        return (abs(timeDistance) / 1000 / 60.toFloat()).roundToInt()
    }

    private fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    companion object {
        private var mColorControlNormal: Int = 0
        fun create(parent: ViewGroup, colorControlNormal: Int): PhotoViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_single_photo, parent, false)
            mColorControlNormal = colorControlNormal
            return PhotoViewHolder(view)
        }
    }
}