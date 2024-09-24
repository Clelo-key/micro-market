<template>
  <el-card>
    <el-row :gutter="20" class="header">
      <el-col :span="7">
        <el-input
          placeholder="请输入商品大类名称..."
          clearable
          v-model="queryForm.query"
        ></el-input>
      </el-col>
      <el-button type="primary" :icon="Search" @click="initBigTypeList"
        >搜索</el-button
      >
      <el-button type="primary" :icon="DocumentAdd" @click="handleDialogValue()"
        >添加 商品大类</el-button
      >
    </el-row>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column
        type="index"
        :index="indexMethod"
        label="#ID"
        width="80"
      />
      <el-table-column prop="name" label="商品大类名称" width="200" />
      <el-table-column prop="image" label="商品大类图片" width="200">
        <template v-slot="scope">
          <img
            :src="getServerUrl('image/bigtype/') + scope.row.image"
            width="80"
            height="80"
          />
        </template>
      </el-table-column>
      <el-table-column
        show-overflow-tooltip
        class="remark"
        prop="remark"
        label="商品大类描述"
      />
      <el-table-column prop="action" label="操作" width="300">
        <template v-slot="scope">
          <el-button type="success" @click="handleChangeImage(scope.row)"
            >更换 图片</el-button
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
  <Dialog v-model="dialogVisible" :id="bigtypeId" :dialogTitle="dialogTitle">
  </Dialog>
  <ImageDialog
    v-model="imageDialogVisible"
    :imageDialogValue="imageDialogValue"
    @initBigTypeList="initBigTypeList"
  />
</template>
<script setup>
import { Search, Delete, Edit, DocumentAdd } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import axios, { getServerUrl } from '@/util/axios'
import Dialog from './compoents/dialog'
import ImageDialog from './compoents/imageDialog'

const imageDialogVisible = ref(false)
const imageDialogValue = ref({})

const queryForm = ref({
  query: '',
  pageNum: 1,
  pageSize: 10
})
const bigtypeId = ref(-1)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const total = ref(0)
const tableData = ref([])
const indexMethod = (index) => {
  return index + (queryForm.value.pageNum - 1) * queryForm.value.pageSize + 1
}
const initBigTypeList = async () => {
  const res = await axios.post('admin/bigType/list', queryForm.value)
  tableData.value = res.data.bigTypeList
  total.value = res.data.total
}
initBigTypeList()
const handleSizeChange = (pageSize) => {
  queryForm.value.pageNum = 1
  queryForm.value.pageSize = pageSize
  initBigTypeList()
}
const handleCurrentChange = (pageNum) => {
  queryForm.value.pageNum = pageNum
  initBigTypeList()
}
const handleDialogValue = (Id) => {
  if (Id) {
    bigtypeId.value = Id
    dialogTitle.value = '商品大类修改'
  } else {
    bigtypeId.value = -1
    dialogTitle.value = '商品大类添加'
  }
  dialogVisible.value = true
}
const handleDelete = (id) => {
  ElMessageBox.confirm('您确定要删除这条记录吗？', '系统提示', {
    confirmButtonText: '确定',

    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      const res = await axios.get('admin/bigType/delete/' + id)
      if (res.data.code === 0) {
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
        initBigTypeList()
      } else {
        ElMessage({
          type: 'error',
          message: res.data.msg
        })
      }
    })
    .catch(() => {})
}
const handleChangeImage = (row) => {
  imageDialogVisible.value = true
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
