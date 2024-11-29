import SvgIcon from '@/components/SvgIcon'
/**
 * 自定义SVG组件
 */
const svgRequired = require.context('./svg', false, /\.svg$/)
svgRequired.keys().forEach(item => svgRequired(item))

export default app => {
  app.component('svg-icon', SvgIcon)
}
