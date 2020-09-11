package cn.sancell.xingqiu.bean

data class LiveInviteData(var desc: String,
                          var logoUrl: String,
                          var title: String,
                          var link: String,
                          var path: String, //小程序路径
                          var inviteSwitch: Int = 0//1可以；0不可以
) {
}