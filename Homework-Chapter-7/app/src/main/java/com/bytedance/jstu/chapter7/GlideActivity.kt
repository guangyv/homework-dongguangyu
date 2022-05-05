package com.bytedance.jstu.chapter7

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.util.ArrayList

class GlideActivity : AppCompatActivity() {
    private val pages: MutableList<View> = ArrayList()
    lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        viewPager = findViewById(R.id.media_view_pager)

        addImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01f7d857e51fb20000018c1bbda474.gif&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654353361&t=68dbf5bd52cd826a92030e7ca5018da5")
        addImage("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF")
        addImage("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F015f3257fb5f94a84a0d304f14d7c3.gif&refer=http%3A%2F%2Fimg.zcool.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1654353484&t=780558dabf55bf5a56fcc8ba885ed0a1")
        addImage("https://t7.baidu.com/it/u=1819248061,230866778&fm=193&f=GIF")

        val adapter = ViewAdapter()
        adapter.setDatas(pages)
        viewPager.adapter = adapter
    }

    private fun addImage(path: String) {
        val imageView =
            layoutInflater.inflate(R.layout.activity_base_multimedia_image_item, null) as ImageView
        Glide.with(this)
            .load(path)
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .into(imageView)
        pages.add(imageView)
    }
}