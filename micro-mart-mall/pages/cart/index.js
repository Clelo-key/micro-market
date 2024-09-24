// pages/cart/index.js
import { getBaseUrl, requestUtil } from '../../utils/requestUtil'
import regeneratorRuntime from '../../lib/runtime/runtime'
Page({
  /**
   * 
   * 页面的初始数据
   */
  data: {
    base_Url: '',
    address: {},
    cart: [],
    allChecked: false,
    totalPrice: 0,
    totalNum: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.initBseUrl()
  },
  // 获取BaseUrl
  initBseUrl() {
    this.setData({
      base_Url: getBaseUrl()
    })
  },
  // 获取收获地址
  handleChooseAdress() {
    wx.chooseAddress({
      success: result => {
        wx.setStorageSync('address', result)
      }
    })
  },
  // 生命周期函数
  onShow() {
    const address = wx.getStorageSync('address') //购物车地址
    const cart = wx.getStorageSync('cart') || [] //购物车商品
    this.setData({
      address
    })
    this.resetCart(cart)
  },
  // 商品数量处理
  handleNumChange(e) {
    const { id, operation } = e.currentTarget.dataset
    const { cart } = this.data
    const index = cart.findIndex(v => {
      return v.id == id
    })
    if (cart[index].num === 1 && operation === '-1') {
      wx.showModal({
        title: '系统提示',
        content: '你确定要删除该商品吗?',
        complete: res => {
          if (res.confirm) {
            cart.splice(index, 1)
            this.resetCart(cart)
          }
        }
      })
    } else {
      cart[index].num += parseInt(operation)
      this.resetCart(cart)
    }
  },
  // 商品选中处理
  handItemChange(e) {
    const { id } = e.currentTarget.dataset
    const { cart } = this.data
    const index = cart.findIndex(v => {
      return v.id == id
    })
    cart[index].checked = !cart[index].checked
    this.resetCart(cart)
  },
  // 底部工具栏数据处理
  resetCart(cart) {
    let allChecked = true //全选按钮
    let totalNum = 0
    let totalPrice = 0
    cart.forEach(element => {
      if (!element.checked) {
        allChecked = false
      } else {
        totalPrice += element.num * element.price
        totalNum += element.num
      }
    })
    allChecked = cart.length == 0 ? false : allChecked
    this.setData({
      cart,
      allChecked,
      totalNum,
      totalPrice
    })
    wx.setStorageSync('cart', cart)
  },
  // 商品全选处理
  handleChooseAll() {
    let { cart, allChecked } = this.data
    cart.forEach(v => {
      v.checked = allChecked
    })
    this.resetCart(cart)
  },
  // 商品结算
  handlePay() {
    const { address, totalNum } = this.data
    if(!address){
      wx.showToast({
        title: '您还没有选择收获地址',
        icon:"none",
      })
      return
    }
    if(totalNum === 0) {
      wx.showToast({
        title: '您还没有选购商品',
        icon:"none",
      })
      return
    }
    wx.navigateTo({
      url: '/pages/pay/index',
    })
  }
})
