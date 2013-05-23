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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class BindingSourceAdapter<T> extends ArrayAdapter<T> 
{
	private LayoutInflater _inflater;
	private List<T> _source;
	private Context _context;
	private int _resourceID;
    private final WeakHashMap<View, Map<String,View>> mParentViews = new WeakHashMap<View, Map<String,View>>();  

	public BindingSourceAdapter(Context context, int resourceID ,List<T> source)
	{
		super(context, resourceID, source);
		_context=context;
		_source = source;
		_resourceID = resourceID;
		_inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	}
	
	
	@Override
	public int getCount() {
		return _source.size();
	}

	@Override
	public T getItem(int arg0) {
		return _source.get(arg0);
		
	}
	
	private void bindView(View view,int position)
	{
		Resources res=_context.getResources();
		Object item = _source.get(position);
		Class<?> class1 = item.getClass();
		Field[] fields = class1.getFields();
		
		for(Field field : fields)
		{
			Object result = null;
			String resultString = null;
			Annotation  bindAnnotation = field.getAnnotation(DataField.class);
			if(bindAnnotation!=null)
			{
				try {
					result = field.get(item);
				} catch (Exception e) {
				}
				resultString =  result == null ? "" : result.toString();
				
				Map<String, View> parentViewMap = mParentViews.get(view);
				String controlAndFieldsName = field.getName();
				View childView = parentViewMap.get(controlAndFieldsName);
				
				if(childView == null){
					int resID = res.getIdentifier(controlAndFieldsName,"id",_context.getPackageName());
					Log.i("msg", controlAndFieldsName);
					childView = (TextView)view.findViewById(resID);
					parentViewMap.put(controlAndFieldsName, childView);
				}
				
				Log.i(controlAndFieldsName, resultString);
				((TextView)childView).setText(resultString);
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
		
		if(convertView == null)
		{
			Log.i("getview", "null");
			convertView = _inflater.inflate(_resourceID,null);
			Map<String, View> childViews = new HashMap<String, View>();
			mParentViews.put(convertView, childViews);
		}
		
		
		
		bindView(convertView,position);
		
		return convertView;
	}

}
