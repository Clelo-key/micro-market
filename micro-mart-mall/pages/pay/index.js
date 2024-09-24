// pages/pay/index.js
import { getBaseUrl, requestUtil, getWxLogin, getUserInfo } from '../../utils/requestUtil'
import regeneratorRuntime, { async, keys } from '../../lib/runtime/runtime'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    base_Url: '',
    address: {},
    cart: [],
    allChecked: false,
    totalPrice: 0,
    totalNum: 0,
    payWay: 0
  },
  productInfo: {}, //商品请求
  payWay: 0, // payWay等于0时默认为购物车页面跳转. 等于1时为立即购买
  onLoad(options) {
    this.initBseUrl()
    if (options.id) {
      this.payWay = 1
      this.getGoods(options.id)
      this.setData({
        payWay: this.payWay
      })
    }
  },
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
  /**
   * 生命周期函数--监听页面显示
   */
  onShow() {
    const address = wx.getStorageSync('address') //购物车地址
    this.setData({
      address
    })
    if (this.payWay == 0) {
      const cart = wx.getStorageSync('cart') || [] //购物车商品
      this.resetCart(cart)
    }
  },

  // 购物车結算商品處理
  resetCart(cart) {
    let allChecked = true //全选按钮
    let totalNum = 0
    let totalPrice = 0
    cart.forEach(element => {
      if (!element.checked && this.payWay == 0) {
        allChecked = false
      } else {
        totalPrice += element.num * element.price
        totalNum += element.num
      }
    })
    this.setData({
      cart,
      allChecked,
      totalNum,
      totalPrice
    })
    // wx.setStorageSync('cart', cart)
  },
  //  加载商品到本地
  async getGoods(id) {
    const cart = []
    let productObj = await this.getProductDetail(id)
    productObj.num = 1
    cart.push(productObj)
    this.resetCart(cart)
  },
  // 请求商品详情
  async getProductDetail(id) {
    const result = await requestUtil({
      url: '/product/detail',
      data: { id },
      method: 'GET'
    })
    this.productInfo = result.message
    return result.message
  },
  // 处理订单支付
  async handleOrderPay() {
    if (wx.getStorageSync('address') === '') {
      wx.showToast({
        title: '请填写收获地址',
        icon: 'none'
      })
      return
    }
    const token = wx.getStorageSync('token')
    if (!token) {
      wx.showModal({
        title: '友情提示',
        content: '微信授权登录后，才可进入个人中心',
        complete: res => {
          if (res.cancel) {
            wx.showToast({
              title: '取消授权',
              icon: 'none'
            })
          }
          if (res.confirm) {
            Promise.all([getWxLogin(), getUserInfo()]).then(res => {
              console.log(res[0].code)
              console.log(res[1].userInfo.nickName, res[1].userInfo.avatarUrl)
              let loginParam = {
                code: res[0].code,
                nickName: res[1].userInfo.nickName,
                avatarUrl: res[1].userInfo.avatarUrl
              }
              console.log(loginParam)
              wx.setStorageSync('userinfo', res[1].userInfo)
              this.wxlogin(loginParam)
              this.setData({
                userinfo: res[1].userInfo
              })
            })
          }
        }
      })
    } else {
      this.createOrder()
    }
  },
  // 获取token
  async wxlogin(loginParam) {
    const result = await requestUtil({ url: '/user/wxlogin', data: loginParam, method: 'post' })
    const token = result.token
    if (result.code === 0) {
      wx.setStorageSync('token', token)
      // token已存在
      this.createOrder()
    } else {
      console.log('token不存在')
    }
  },
  //创建订单
  async createOrder() {
    try {
      const totalPrice = this.data.totalPrice
      const address = this.data.address.provinceName + this.data.address.cityName + this.data.address.countyName + this.data.address.detailInfo
      const consignee = this.data.address.userName
      const telNumber = this.data.address.telNumber
      let goods = []
      this.data.cart.forEach(v => {
        if (v.checked || this.payWay == 1) {
          goods.push({
            goodsId: v.id,
            goodsNumber: v.num,
            goodsPrice: v.price,
            goodsName: v.name,
            goodsPic: v.proPic
          })
        }
      })
      const orderParam = {
        totalPrice,
        address,
        consignee,
        telNumber,
        goods
      }
      // 创建订单
      const res = await requestUtil({
        url: '/my/order/create',
        method: 'post',
        data: orderParam
      })
      let orderNo = res.orderNo //订单号

      // 通过订单号获取参数
      // const preparePay = await requestUtil({
      //   url: '/my/order/preparePay',
      //   method: 'POST',
      //   data: orderNo
      // })
      let newcart = wx.getStorageSync('cart')
      if (this.payWay == 0) {
        newcart = newcart.filter(v => !v.checked)
      }
      wx.setStorageSync('cart', newcart)
      wx.showToast({
        title: '支付成功，联系客服付款',
        icon: 'none'
      })
      setTimeout(() => {
        wx.navigateTo({
          url: '/pages/order/index?type=0'
        })
      }, 800)
    } catch {
      wx.showToast({
        title: '支付失败，请稍后重试'
      })
    }
  }
})
