package handbank.hbwallet

data class ResResponse<out T>(val retCode: Int, val retMsg: String, val retData: T)