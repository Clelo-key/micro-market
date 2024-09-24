<template>
  <el-dialog
    model-value="productSwiperImageDialogVisible"
    title="商品轮播图片设置"
    width="30%"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      label-width="100px"
      style="text-align: center"
      :rules="rules"
    >
      <el-form-item label="排列序号" prop="swiperSort">
        <el-input v-model="form.sort" style="width: 100px" />
      </el-form-item>
      <el-upload
        :headers="headers"
        class="avatar-uploader"
        :action="getServerUrl('admin/productSwiperImage/uploadImage')"
        :show-file-list="false"
        :on-success="handleAvatarSuccess"
        :before-upload="beforeAvatarUpload"
        style="padding: 10px"
      >
        <img v-if="imageUrl" :src="imageUrl" class="avatar" />
        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
      </el-upload>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleConfirm">确认</el-button>
    </el-form>

    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column type="index" width="50" />
      <el-table-column prop="image" label="轮播图片">
        <template v-slot="scope">
          <img
            :src="getServerUrl('image/productSwiperImage/') + scope.row.image"
            width="80"
            height="80"
          />
        </template>
      </el-table-column>
      <el-table-column prop="sort" label="排列序号" width="100" />
      <el-table-column prop="action" label="操作" width="100">
        <template v-slot="scope">
          <el-button
            type="danger"
            :icon="Delete"
            @click="handleDelete(scope.row.id)"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>
<script setup>
import { defineProps, ref, watch, defineEmits } from 'vue'
import axios, { getServerUrl } from '@/util/axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
const props = defineProps({
  imageDialogValue: {
    type: Object,
    default: () => {},
    required: true
  }
})
const rules = ref({
  sort: [
    {
      required: true,
      message: '请输入排列序号'
    },
    {
      type: 'number',
      message: '排序序号必须是数值类型',
      transform: (value) => Number(value)
    }
  ]
})
const form = ref({
  productId: -1,
  sort: 0,
  image: ''
})
const headers = ref({
  token: window.sessionStorage.getItem('token')
})
const formRef = ref(null)
const imageUrl = ref('')
const tableData = ref([])
const initProductSwiperImageList = async () => {
  const res = await axios.get(
    'admin/productSwiperImage/list/' + form.value.productId
  )
  tableData.value = res.data.productSwiperImageList
}
watch(
  () => props.imageDialogValue,
  () => {
    form.value.productId = props.imageDialogValue.id
    initProductSwiperImageList()
  },
  { deep: true, immediate: true }
)
const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg'
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isJPG) {
    ElMessage.error('图片必须是jpg格式')
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2M!')
  }
  return isJPG && isLt2M
}
const handleAvatarSuccess = (res) => {
  imageUrl.value = getServerUrl() + res.data.src
  form.value.image = res.data.title
}
// 定义父组件事件
const emits = defineEmits(['update:modelValue', 'initProductList'])
const handleClose = () => {
  emits('update:modelValue', false)
}
const handleConfirm = async () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      const result = await axios.post(
        'admin/productSwiperImage/add',
        form.value
      )
      const data = result.data
      if (data.code === 0) {
        ElMessage.success('执行成功！')
        formRef.value.resetFields()
        initProductSwiperImageList()
        handleClose()
      } else {
        ElMessage.error(data.msg)
      }
    }
  })
}
const handleDelete = (id) => {
  ElMessageBox.confirm('您确定要删除这条记录吗？', '系统提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      const res = await axios.get('admin/productSwiperImage/delete/' + id)
      if (res.data.code === 0) {
        ElMessage({
          type: 'success',
          message: '删除成功'
        })
        initProductSwiperImageList()
      } else {
        ElMessage({
          type: 'error',
          message: res.data.msg
        })
      }
    })
    .catch(() => {})
}
</script>
<style>
avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
