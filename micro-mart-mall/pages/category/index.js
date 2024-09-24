// pages/category/index.js
import { getBaseUrl, requestUtil } from '../../utils/requestUtil'
import regeneratorRuntime from '../../lib/runtime/runtime'
Page({
  /**
   * 页面的初始数据
   */
  data: {
    base_Url: '',
    leftMenuList: [],
    rightMenuList: [],
    currentIndex: 0,
    scrollTop: 0
  },
  // 所有商品类别数据
  Cates: [],
  // 获取全局参数index
  gobal_index: getApp().globalData.index,
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {
    this.initBseUrl(),
      // 传入全局定位参数index
      this.getCatesList(this.gobal_index)
  },
  // 监听器,监听index的变化
  onShow: function () {
    const app = getApp()
    const { index } = app.globalData
    // 存在异步请求问题,cates可能不存在!!
    if (index != this.gobal_index) {
      this.getCatesList(index)
    }
    // let rightMenuList=this.Cates[index].smallTypeList;
    // this.setData({
    //   leftMenuList,
    //   rightMenuList
    // })
  },
  initBseUrl() {
    this.setData({
      base_Url: getBaseUrl()
    })
  },
  //请求hotproductList资源
  async getCatesList(index) {
    const result = await requestUtil({
      url: '/bigtype/findCategories',
      method: 'GET'
    })
    this.Cates = result.message
    // this.Cates.map((v)=>{
    //   return v.name
    // }),
    let leftMenuList = this.Cates.map(v => v.name)
    let rightMenuList
    // 判断点击页面方式,更具不同方式获取内容
    if (index == -1) {
      rightMenuList = this.Cates[0].smallTypeList
    } else {
      rightMenuList = this.Cates[index].smallTypeList
    }
    this.setData({
      currentIndex: index,
      leftMenuList,
      rightMenuList
    })
  },

  // 菜单切换事件
  handMeanuItemChange(e) {
    // 解构赋值
    const { index } = e.currentTarget.dataset
    let rightMenuList = this.Cates[index].smallTypeList
    this.setData({
      currentIndex: index,
      rightMenuList,
      scrollTop: 0
    })
  }
})
