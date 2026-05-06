<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="grid place-items-center h-full">
    <div class="text-center space-y-4">
      <div class="text-6xl font-bold text-base-content/20 font-mono">404</div>
      <h2 class="text-xl font-medium text-base-content">页面未找到</h2>
      <p class="text-sm text-base-content/60">你访问的页面不存在或已被移除</p>
      <button class="btn btn-primary rounded-lg mt-4" @click="router.push('/dashboard')">
        返回首页
      </button>
    </div>
  </div>
</template>
