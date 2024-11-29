// 导入request请求工具类
import {
  getBaseUrl,
  getWxLogin,
  getUserInfo,
  requestUtil
} from '../../utils/requestUtil.js';
import regeneratorRuntime, { async, keys } from '../../lib/runtime/runtime'
// const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userinfo:{},
    // avatarUrl: defaultAvatarUrl,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onShow: function (options) {
    const token=wx.getStorageSync('token');
    if(!token){
      wx.showModal({
        title:'友情提示',
        content:'微信授权登录后，才可进入个人中心',
        complete: (res) => {
          if (res.cancel) {
            wx.showToast({
              title: '取消授权',
              icon:"none"
            })
            setTimeout(()=>{
              wx.switchTab({
                url: '/pages/index/index'
              })
            },800)
          }
          if (res.confirm) {
              Promise.all([getWxLogin(),getUserInfo()]).then((res)=>{
                console.log(res[0].code);
                console.log(res[1].userInfo.nickName,res[1].userInfo.avatarUrl)
                let loginParam={
                  code:res[0].code,
                  nickName:res[1].userInfo.nickName,
                  avatarUrl:res[1].userInfo.avatarUrl
                }
                console.log(loginParam)
                wx.setStorageSync('userinfo', res[1].userInfo);
                this.wxlogin(loginParam);
                this.setData({
                  userinfo:res[1].userInfo
                })
              })
            }
            
          }
        }
      )
    }else{
      console.log("token存在："+token);
      // const userinfo=wx.getStorageSync('userInfo')
      const userinfo=wx.getStorageSync("userinfo")
      this.setData({
        userinfo
      })
    }
  },

  /**
   * 请求后端获取用户token
   * @param {*} loginParam 
   */
  async wxlogin(loginParam){
    const result=await requestUtil({url:"/user/wxlogin",data:loginParam,method:"post"});
    console.log(result);
    const token=result.token;
    if(result.code===0){
      // 存储token到缓存
      wx.setStorageSync('token', token);
      console.log("注册成功");
      // this.createOrder();
    }
  },
  

  // 点击 编辑收货地址
  handleEditAddress(){
    wx.chooseAddress({
      success: (result) => {},
    })
  },
})