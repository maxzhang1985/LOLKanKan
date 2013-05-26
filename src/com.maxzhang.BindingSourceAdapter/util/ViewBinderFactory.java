package com.maxzhang.BindingSourceAdapter.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.maxzhang.BindingSourceAdapter.cache.ImageLoader;

/**
 * Created with IntelliJ IDEA.
 * User: zlhxd_000
 * Date: 5/26/13
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewBinderFactory {



    public ViewBinderFactory(Context context)
    {
        this.mContext = context;
    }

    private Context mContext;
    private ImageLoader mImageLoader;
    public void dataBinding(View view,Object dataValue,boolean isBusy,ViewDefined viewType)
    {
        Log.v("viewbinderFactory",viewType.toString() + "=" + dataValue.toString());

        switch (viewType)
        {
            case Text:
                ((TextView)view).setText(dataValue.toString());
                break;
            case CheckBox:
                break;
            case Image:
                break;
            case RemoteImage:
                if(mImageLoader==null)
                    mImageLoader = new ImageLoader(this.mContext);
                if (!isBusy) {
                    mImageLoader.DisplayImage(dataValue.toString(), (ImageView)view, false);
                    Log.v("viewbinderFactory","IDLE ||TOUCH_SCROLL");
                } else {
                    mImageLoader.DisplayImage(dataValue.toString(), (ImageView)view, false);
                    Log.v("viewbinderFactory","FLING");
                }
                break;
        }



    }

    public void clearCache()
    {
        if (mImageLoader != null){
            mImageLoader.clearCache();
        }
    }
}
