package com.maxzhang.BindingSourceAdapter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.FieldPosition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.maxzhang.BindingSourceAdapter.cache.ImageLoader;
import com.maxzhang.BindingSourceAdapter.util.ViewBinderFactory;


public class BindingSourceAdapter<T> extends ArrayAdapter<T> 
{
    public Object Tag;
	private LayoutInflater _inflater;
	private List<T> _source;
	private Context _context;
	private int _resourceID;
    private final WeakHashMap<View, Map<String,View>> mParentViews = new WeakHashMap<View, Map<String,View>>();
    private boolean mBusy = false;
    ViewBinderFactory viewBinder ;

    private int animationResID = -1;

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }

    public void clearCache()
    {
        if(viewBinder!=null)
            viewBinder.clearCache();
    }

	public BindingSourceAdapter(Context context, int resourceID ,List<T> source)
	{
		super(context, resourceID, source);
		_context=context;
		_source = source;
		_resourceID = resourceID;
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewBinder = new ViewBinderFactory(context);
	}


    public void setAnimationResID(int resID)
    {
        animationResID = resID;
    }
	
	@Override
	public int getCount() {
		return _source.size();
	}

	@Override
	public T getItem(int arg0) {
		return _source.get(arg0);
		
	}

    @Override
    public void clear() {
        _source.clear();
    }

    private void bindView(View view,int position)
	{
		Resources res=_context.getResources();
		Object item = _source.get(position);
		Class<?> class1 = item.getClass();
		Field[] fields = class1.getFields();
		
		for(Field field : fields)
		{
            String controlAndFieldsName = field.getName();
            int resID = res.getIdentifier(controlAndFieldsName,"id",_context.getPackageName());
            if(resID == 0)
                continue;

			Object result = null;
			String resultString = null;
			DataField  bindAnnotation = (DataField)field.getAnnotation(DataField.class);
			if(bindAnnotation!=null)
			{
				try {
					result = field.get(item);
				} catch (Exception e) {
				}
				resultString =  result == null ? "" : result.toString();
				
				Map<String, View> parentViewMap = mParentViews.get(view);

				View childView = parentViewMap.get(controlAndFieldsName);
				
				if(childView == null){
					Log.i("msg", controlAndFieldsName);
					childView = view.findViewById(resID);
					parentViewMap.put(controlAndFieldsName, childView);
				}
				
				Log.i(controlAndFieldsName, resultString);

                viewBinder.dataBinding(childView,result,mBusy,bindAnnotation.View());
			}
			
		}
	}
	
	

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return super.getItemId(position);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    boolean hasCreateNewView = false;
		if(convertView == null)
		{
			Log.i("getview", "null");
			convertView = _inflater.inflate(_resourceID,null);
			Map<String, View> childViews = new HashMap<String, View>();
			mParentViews.put(convertView, childViews);
            hasCreateNewView = true;
		}
		
		
		
		bindView(convertView,position);

        if(animationResID > -1)
        {
            Animation animation = AnimationUtils.loadAnimation(_context, animationResID);
            convertView.startAnimation(animation);
        }
		return convertView;
	}

}
