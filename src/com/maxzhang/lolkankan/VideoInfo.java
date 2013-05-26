package com.maxzhang.lolkankan;

import com.maxzhang.BindingSourceAdapter.*;
import com.maxzhang.BindingSourceAdapter.util.ViewDefined;

/**
 * Created with IntelliJ IDEA.
 * User: Maxzhang8
 * Date: 13-5-23
 * Time: 下午2:05
 * To change this template use File | Settings | File Templates.
 */
public class VideoInfo {
    @DataField
    public String Url;

    @DataField(View= ViewDefined.RemoteImage)
    public String ImageUrl;

    @DataField
    public String Title;

    @DataField
    public String TimeSpan;


    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTimeSpan() {
        return TimeSpan;
    }

    public void setTimeSpan(String timeSpan) {
        TimeSpan = timeSpan;
    }
}
