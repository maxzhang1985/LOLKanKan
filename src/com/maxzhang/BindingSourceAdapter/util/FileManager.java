package com.maxzhang.BindingSourceAdapter.util;


public class FileManager {

	public static String getSaveFilePath() {
		if (CommonUtil.hasSDCard()) {
			return CommonUtil.getRootFilePath() + "com.maxzhang.lolkankan/files/";
		} else {
			return CommonUtil.getRootFilePath() + "com.maxzhang.lolkankan/files";
		}
	}
}
