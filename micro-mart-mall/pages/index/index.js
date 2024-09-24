// index.js
// 获取应用实例
const app = getApp()
import { getBaseUrl, requestUtil } from '../../utils/requestUtil'
import regeneratorRuntime from '../../lib/runtime/runtime'
Page({
  data: {
    base_Url: '',
    swiperList: [],
    bigtypeList: [],
    bigtypeList_row1: [],
    bigtypeList_row2: [],
    hotproductList: []
  },
  // 生命周期,页面加载函数
  onLoad() {
    //初始化BaseUrl
    this.initBseUrl()
    //获取swiper资源
    this.getSwiper()
    // 获取BigType资源
    this.getBigTypeList()
    // 获取hotproduct资源
    this.getHotProductList()
  },
  // 初始化baseUrl函数
  initBseUrl() {
    this.setData({
      base_Url: getBaseUrl()
    })
  },
  //请求swiper资源
  async getSwiper() {
    const result = await requestUtil({
      url: '/product/findSwiper',
      method: 'GET'
    })
    this.setData({
      swiperList: result.message
    })
  },
  //请求BigType资源
  async getBigTypeList() {
    const result = await requestUtil({
      url: '/bigtype/findAll',
      method: 'GET'
    })
    const bigtypeList = result.message
    const bigtypeList_row1 = bigtypeList.filter((item, index) => {
      return index < 5
    })
    const bigtypeList_row2 = bigtypeList.filter((item, index) => {
      return index >= 5
    })
    this.setData({
      bigtypeList,
      bigtypeList_row1,
      bigtypeList_row2
    })
  },
  //请求hotproductList资源
  async getHotProductList() {
    const result = await requestUtil({
      url: '/product/findHot',
      method: 'GET'
    })
    this.setData({
      hotproductList: result.message
    })
  },

  // 商业页面跳转事件
  handleTypeJump(e) {
    const { index } = e.currentTarget.dataset
    // 设置全局参数
    const app = getApp()
    app.globalData.index = index
    wx.switchTab({
      url: '/pages/category/index'
    })
  }
})
