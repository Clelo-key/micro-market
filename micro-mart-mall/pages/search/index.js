// 导入request请求工具类
import {
  getBaseUrl,
  requestUtil
} from '../../utils/requestUtil.js';
import regeneratorRuntime from '../../lib/runtime/runtime';
Page({

  /**
   * 页面的初始数据
   */
  data: {
    productList:[], // 商品数组
    inputValue:"", // 输入框的值
    isFocus:false // 取消 按钮 是否显示
  },

  TimeoutId:-1,

  // 处理input事件
  handleInput(e){
    const {value}=e.detail;
    if(!value.trim()){
      this.setData({
        productList:[],
        isFocus:false
      })
      return;
    }
    this.setData({
      isFocus:true
    })
    clearTimeout(this.TimeoutId);
    this.TimeoutId=setTimeout(()=>{
      this.search(value)
    },1000)
   
  },

    /**
   * 获取商品详情
   */
  async search(q) {
    const result = await requestUtil({
      url: '/product/search',
      data:{q}
    });
    this.setData({
      productList: result.message
    })
  },
  handleClear(){
    this.setData({
      inputValue:"",
      productList:[]
    })
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})