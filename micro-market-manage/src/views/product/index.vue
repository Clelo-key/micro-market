<template>
  <el-card>
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input
          placeholder="请输入商品名称..."
          clearable
          v-model="queryForm.query"
        ></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initProductList"
        >搜索</el-button
      >
      <el-button type="primary" :icon="DocumentAdd" @click="handleDialogValue()"
        >添加商品</el-button
      >
    </el-row>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column prop="name" label="商品名称" width="300" fixed />
      <el-table-column prop="image" label="商品图片" width="150">
        <template v-slot="scope">
          <img
            :src="getServerUrl('image/product/') + scope.row.proPic"
            width="80"
            height="80"
          />
        </template>
      </el-table-column>
      <el-table-column prop="price" label="商品名称" width="100" />
      <el-table-column prop="stock" label="商品库存" width="100" />
      <el-table-column
        prop="type"
        label="商品类别"
        width="200"
        :formatter="typeNameFormatter"
      />
      <el-table-column prop="hot" label="热卖?" width="100" align="center">
        <template v-slot="{ row }">
          <el-switch
            v-model="row.hot"
            @change="handleHotChange(row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        prop="swiper"
        label="首页幻灯？"
        width="100"
        align="center"
      >
        <template v-slot="{ row }">
          <el-switch
            v-model="row.swiper"
            @change="handleSwiperChange(row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column
        prop="swiperPic"
        label="幻灯图片"
        width="150"
        align="center"
      >
        <template v-slot="scope">
          <img
            :src="getServerUrl('image/swiper/') + scope.row.swiperPic"
            width="150"
            height="75"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="swiperSort"
        label="幻灯排序"
        width="100"
        align="center"
      />
      <el-table-column prop="description" label="商品描述" width="400" />
      <el-table-column prop="action" label="操作" width="500" fixed="right">
        <template v-slot="scope">
          <el-button type="success" @click="handleChangeImage(scope.row)"
            >更 换图片</el-button
          >
          <el-button type="primary" @click="handleChangeSwiper(scope.row)"
            >幻 灯设置</el-button
          >
          <el-button
            type="primary"
            :icon="Edit"
            @click="handleDialogValue(scope.row.id)"
          ></el-button>
          <el-button
            type="danger"
            :icon="Delete"
            @click="handleDelete(scope.row.id)"
          ></el-button>
          <el-button
            type="primary"
            @click="handleChangeProductSwiper(scope.row)"
            >轮 播图设置</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-model:currentPage="queryForm.pageNum"
      v-model:page-size="queryForm.pageSize"
      :page-sizes="[10, 20, 30, 40, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </el-card>
  <Dialog
    v-model="dialogVisible"
    :dialogTitle="dialogTitle"
    :dialogVisible="dialogVisible"
    :id="productId"
    @initProductList="initProductList"
  ></Dialog>
  <ImageDialog
    v-model="imageDialogVisible"
    :imageDialogValue="imageDialogValue"
    @initProductList="initProductList"
  />
  <SwiperImageDialog
    v-model="SwiperimageDialogVisible"
    :imageDialogValue="imageDialogValue"
    @initProductList="initProductList"
  />
  <productSwiperImageDialog
    v-model="productSwiperImageDialogVisible"
    :imageDialogValue="imageDialogValue"
    @initProductList="initProductList"
  />
</template>
<script setup>
import { Search, Delete, Edit, DocumentAdd } from '@element-plus/icons-vue'
import { ref } from 'vue'
import axios, { getServerUrl } from '@/util/axios'
import { ElMessageBox, ElMessage } from 'element-plus'
import Dialog from './components/dialog'
import ImageDialog from './components/imageDialog.vue'
import SwiperImageDialog from './components/SwiperImageDialog.vue'
import productSwiperImageDialog from './components/productSwiperImageDialog.vue'

const imageDialogValue = ref({})
const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 10
})
const total = ref(0)
const tableData = ref([])
const productId = ref(-1)
const dialogTitle = ref('')
const dialogVisible = ref(false)
const imageDialogVisible = ref(false)
const SwiperimageDialogVisible = ref(false)
const productSwiperImageDialogVisible = ref(false)
const initProductList = async () => {
  const res = await axios.post('admin/product/list', queryForm.value)
  tableData.value = res.data.productList
  total.value = res.data.total
}
initProductList()
const handleSizeChange = (pageSize) => {
  queryForm.value.pageNum = 1
  queryForm.value.pageSize = pageSize
  initProductList()
}
const handleCurrentChange = (pageNum) => {
  queryForm.value.pageNum = pageNum
  initProductList()
}
const typeNameFormatter = (row) => {
  return row.type.name
}
const handleDelete = (id) => {
  ElMessageBox.confirm('您确定要删除这条记录吗？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      const res = await axios.get('admin/product/delete/' + id)
      if (res.data.code === 0) {
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
        initProductList()
      } else {
        ElMessage({
          type: 'error',
          message: res.data.msg
        })
      }
    })
    .catch(() => {})
}
const handleHotChange = async (row) => {
  const res = await axios.get(
    'admin/product/updateHot/' + row.id + '/state/' + row.hot
  )
  console.log(res)
  if (res.data.code === 0) {
    ElMessage({
      type: 'success',
      message: '执行成功'
    })
  } else {
    ElMessage({
      type: 'error',
      message: '执行失败'
    })
    initProductList()
  }
}
const handleDialogValue = (Id) => {
  if (Id) {
    productId.value = Id
    dialogTitle.value = '商品修改'
  } else {
    productId.value = -1
    dialogTitle.value = '商品添加'
  }
  dialogVisible.value = true
}
const handleSwiperChange = async (row) => {
  const res = await axios.get(
    'admin/product/updateSwiper/' + row.id + '/state/' + row.swiper
  )
  console.log(res)
  if (res.data.code === 0) {
    ElMessage({
      type: 'success',
      message: '执行成功'
    })
  } else {
    ElMessage({
      type: 'error',
      message: '执行失败'
    })
    initProductList()
  }
}
const handleChangeImage = (row) => {
  imageDialogVisible.value = true
  imageDialogValue.value = JSON.parse(JSON.stringify(row))
}
const handleChangeSwiper = (row) => {
  SwiperimageDialogVisible.value = true
  imageDialogValue.value = JSON.parse(JSON.stringify(row))
}
const handleChangeProductSwiper = (row) => {
  productSwiperImageDialogVisible.value = true
  imageDialogValue.value = JSON.parse(JSON.stringify(row))
}
</script>
<style lang="scss" scoped>
.header {
  padding-bottom: 16px;
  box-sizing: border-box;
}
.el-pagination {
  padding-top: 15px;
  box-sizing: border-box;
}
</style>
