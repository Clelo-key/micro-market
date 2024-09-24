// pages/product_detail/index.js
import { getBaseUrl, requestUtil } from '../../utils/requestUtil'
import regeneratorRuntime from '../../lib/runtime/runtime'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    base_Url: '',
    productObj: {},
    active_index: 0
  },
  productInfo: {},
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.initBseUrl(), this.getProductDetail(options.id)
  },
  // 初始化生命周期函数
  initBseUrl() {
    this.setData({
      base_Url: getBaseUrl()
    })
  },

  // 标签点击事件
  handItemTab(e) {
    const { index } = e.currentTarget.dataset
    this.setData({
      active_index: index
    })
  },

  // 加入购物车事件
  handleCartAdd() {
    this.setCartAdd()
    // 提示加入购物车成功
    wx.showToast({
      title: '加入成功',
      incon: 'success',
      mask: true
    })
  },
  // 设置加入购物车
  setCartAdd() {
    // 判空设值
    // let cart=wx.getStorageSync('cart')??cart; //判断对象undifind,null赋值
    let cart = wx.getStorageSync('cart') || [] //判断假值赋值
    let index = cart.findIndex(v => {
      return v.id === this.productInfo.id
    }) //在购物车中寻找当前商品索引
    if (index === -1) {
      // 购物车不存在当前
      // 设置购物车中当前商品数量为num,初始值为1
      this.productInfo.num = 1
      this.productInfo.checked = true //设置商品加入购物车时,默认选中
      cart.push(this.productInfo)
    } else {
      //已经加入购物车
      cart[index].num += 1
    }
    wx.setStorageSync('cart', cart)
  },

  // 立即购买事件
  handleBuyImmediate(e) {
    const { id } = e.currentTarget.dataset
    wx.reLaunch({
      url: '/pages/pay/index?id=' + id
    })
  },
  //获取商品详情
  async getProductDetail(id) {
    const result = await requestUtil({
      url: '/product/detail',
      data: { id },
      method: 'GET'
    })
    this.productInfo = result.message
    this.productInfo.productIntroImgs = this.setclass(this.productInfo.productIntroImgs)
    this.productInfo.productParaImgs = this.setclass(this.productInfo.productParaImgs)
    this.setData({
      productObj: result.message
    })
  },
  // 富文本图片处理
  setclass(string) {
    if (string!=null) {
      string = string.replace(/width\s*:\s*[0-9]+px/g, 'width:100%');
      string = string.replace(/<([\/]?)(center)((:?\s*)(:?[^>]*)(:?\s*))>/g, '<$1div$3>');//替换center标签
      string = string.replace(/\<img/gi, '<img class="rich-img" ');//正则给img标签增加class
      //或者这样直接添加修改style
      string = string.replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;display: block;margin:auto"');
      string = string.replace(/\<p/gi, '<P class="rich-p" ');//正则给p标签增加class
    }else{
      string=''
    }
       return string
  }
})
