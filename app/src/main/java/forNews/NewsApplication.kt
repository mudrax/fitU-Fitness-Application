package forNews

import android.app.Application
import com.mudrax.fitu.HistoryDatabase

class NewsApplication :Application(){
    val db by lazy{
        HistoryDatabase.getInstance(this)
    }
}