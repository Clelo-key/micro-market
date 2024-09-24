<template>
  <el-dropdown>
    <span class="el-dropdown-link">
      <el-avatar shape="square" :size="40" :src="squareUrl" />
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="logout">安全退出</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>
<script setup>
import { useStore } from 'vuex'
// import { ref } from 'vue'
// const squareUrl = ref(
//   'https://foruda.gitee.com/avatar/1701912762092488336/12588686_clelostar_1701912762.png!avatar100'
// )
import squareUrl from '@/icons/avatar.png'
const store = useStore()
const logout = () => {
  store.dispatch('logout')
}
</script>

<style lang="scss" scoped></style>
