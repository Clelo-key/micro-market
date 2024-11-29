// 定义请求根路径
// const baseUrl="http://localhost:8080"
const baseUrl = 'http://192.168.43.246:8080'
export const getBaseUrl = () => {
  return baseUrl
}

// 同时并发请求的次数
let ajaxtimes = 0

/**
 * 请求工具
 * @param {*} params
 */
export const requestUtil = params => {
  // 判断url是否为带着/my/的私有路径，如果是带上请求头hander token
  let header = { ...params.header }
  if (params.url.includes('/my/')) {
    header['token'] = wx.getStorageSync('token')
  }
  ajaxtimes++
  wx.showLoading({
    title: '加载中',
    mask: true
  })
  // 模拟网络延迟
  var start = new Date().getTime()
  while (true) {
    if (new Date().getTime() - start > 1 * 1000) break
  }

  // 返回请求数据中的data
  return new Promise((resolve, reject) => {
    wx.request({
      ...params,
      header,
      url: baseUrl + params.url,
      success: result => {
        resolve(result.data)
      },
      fail: err => {
        reject(err)
      },
      complete: () => {
        ajaxtimes--
        if (ajaxtimes == 0) {
          // 关闭加载
          wx.hideLoading()
        }
      }
    })
  })
}
/**
 * 获取微信login
 *
 */
export const getWxLogin = () => {
  return new Promise((resolve, reject) => {
    wx.login({
      timeout: 5000,
      success: res => {
        resolve(res)
      },
      fail: err => {
        reject(err)
      }
    })
  })
}
/**
 * 获取用户信息
 *
 */

export const getUserInfo = () => {
  return new Promise((resolve, reject) => {
    wx.getUserProfile({
      desc: '获取用户信息',
      success: res => {
        resolve(res)
      },
      fail: err => {
        reject(err)
      }
    })
  })
}

export const requestPay = pay => {
  return new Promise((resolve, reject) => {
    wx.requestPayment({
      ...pay,
      success: res => {
        resolve(res)
      },
      fail: err => {
        reject(err)
      }
    })
  })
}
