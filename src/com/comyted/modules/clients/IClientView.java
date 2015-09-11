package com.comyted.modules.clients;

import android.graphics.Bitmap;

import com.enterlib.app.IDataView;

public interface IClientView extends IDataView {
	void BeginDownloadMap();
	
	void EndDownLoadMap(Bitmap map);
}
