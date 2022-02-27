package com.example.stagram.navigation

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.stagram.R
import com.example.stagram.databinding.ItemDetailBinding
import com.example.stagram.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore


class DetailViewFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail,
            container, false)
        return view
    }
    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private lateinit var itemDetailBinding: ItemDetailBinding
        private var firestore : FirebaseFirestore = FirebaseFirestore.getInstance()
        var contentDTOs = arrayListOf<ContentDTO>()
        var contentUidList = arrayListOf<String>()

        init {
            firestore.collection("images").orderBy("timestamp").
            addSnapshotListener { querySnapShot, firebaseFirestoreException ->
                contentDTOs.clear()
                contentUidList.clear()
                for(snapshot in querySnapShot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTOs.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            // 전 단계에서 만든 item_detail.xml을 inflate 시키기
            // return의 인자는 ViewHolder
            var view = LayoutInflater.from(p0.context).inflate(R.layout.item_detail,p0,false)
            return CustomviewHolder(view)
        }
        inner class CustomviewHolder(view : View) : RecyclerView.ViewHolder(view)

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            // item을 하나, 하나 보여주는(Bind 되는) 함수
            var viewholder = (p0 as CustomviewHolder).itemView

            // user email
            itemDetailBinding.detailviewitemProfileTextview.text = contentDTOs[p1].userEmail
            // Image
            Glide.with(p0.itemView.context).load(contentDTOs[p1].imageUrl)
                .into(itemDetailBinding.detailviewitemImageviewContent)
            // Explain of content
            itemDetailBinding.detailviewitemExplainTextview.text = contentDTOs[p1].explain
            // likes
            itemDetailBinding.detailviewitemFavoriteTextview.text = "Likes " + contentDTOs[p1].favoriteCount
            // profileImage
            Glide.with(p0.itemView.context).load(contentDTOs[p1].imageUrl)
                .into(itemDetailBinding.detailviewitemProfileImage)
        }

        override fun getItemCount(): Int {
            // RecyclerView의 총 개수
            return contentDTOs.size
        }
    }

    inner class PostHolder(root : View) : RecyclerView.ViewHolder(root)

    inner class VerticalItemDecorateor(var divHeight : Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.top = divHeight
            outRect.bottom = divHeight
        }
    }
}