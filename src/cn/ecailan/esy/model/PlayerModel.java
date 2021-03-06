package cn.ecailan.esy.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import org.ry8.CeaFrame.model.BaseModel;
import org.ry8.CeaFrame.model.BeeCallback;
import org.ry8.CeaFrame.view.MyProgressDialog;
import cn.ecailan.esy.ApiInterface;
import cn.ecailan.esy.protocol.PLAYER;
import cn.ecailan.esy.protocol.SHOT;
import org.ry8.external.androidquery.callback.AjaxStatus;
import org.ry8.external.androidquery.util.Constants;

/*
 *	 ______    ______    ______
 *	/\  __ \  /\  ___\  /\  ___\
 *	\ \  __<  \ \  __\_ \ \  __\_
 *	 \ \_____\ \ \_____\ \ \_____\
 *	  \/_____/  \/_____/  \/_____/
 *
 *
 *	Copyright (c) 2013-2014, {Bee} open source community
 *	http://www.bee-framework.com
 *
 *
 *	Permission is hereby granted, free of charge, to any person obtaining a
 *	copy of this software and associated documentation files (the "Software"),
 *	to deal in the Software without restriction, including without limitation
 *	the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *	and/or sell copies of the Software, and to permit persons to whom the
 *	Software is furnished to do so, subject to the following conditions:
 *
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *	FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 *	IN THE SOFTWARE.
 */
public class PlayerModel extends BaseModel {

	public PLAYER player;
	public ArrayList<SHOT> dataList = new ArrayList<SHOT>();
	public PlayerModel(Context context) {
		super(context);
	}
	
	public void getPlayer(int id) {
		String url = ApiInterface.PLAYERS;
        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status) {
            	PlayerModel.this.callback(url, jo, status);
                try {
                    if(jo!=null){
                    	player = new PLAYER();
                    	player.fromJson(jo);
                    	PlayerModel.this.OnMessageResponse(url,jo,status);
                    }
                } catch (JSONException e) {
                     e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        };

        url +=id;
        cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
        aq.ajax(cb);
	}
	
	public void getProfileShotList(int id)
    {
        
        String url = ApiInterface.PLAYERS;
        int page = 1;
        int per_page = 3;

        BeeCallback<JSONObject> cb = new BeeCallback<JSONObject>(){

            @Override
            public void callback(String url, JSONObject jo, AjaxStatus status)
            {
            	PlayerModel.this.callback(url, jo, status);
            	
                if (null != jo)
                {
                    dataList.clear();
                    try
                    {
                        JSONArray shotArray = jo.optJSONArray("shots");
                        for (int i = 0; i < shotArray.length(); i++)
                        {
                            JSONObject jsonItem = shotArray.getJSONObject(i);
                            SHOT shotItem = new SHOT();
                            shotItem.fromJson(jsonItem);
                            dataList.add(shotItem);
                        }
                        PlayerModel.this.OnMessageResponse(url, jo, status);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        url += id;
        url += "/shots?" + "page="+page+"&per_page="+per_page;
        cb.url(url).type(JSONObject.class).method(Constants.METHOD_GET);
        MyProgressDialog mPro = new MyProgressDialog(mContext, "请稍后...");
        aq.progress(mPro.mDialog).ajax(cb);
    }

}
