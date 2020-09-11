package cn.sancell.xingqiu.interfaces

interface OnImagUpLoadLinsener {

    fun onImagUploadSucess(fildPaht: String)
    fun onImagUploadError(errorMsg: String)
}