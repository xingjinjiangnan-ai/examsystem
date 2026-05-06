<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
</script>

<template>
  <div class="space-y-6 max-w-screen-2xl mx-auto">
    <div class="surface p-5">
      <h2 class="text-lg font-medium text-base-content mb-2">
        欢迎，{{ authStore.user?.realName || authStore.user?.username }}
      </h2>
      <p class="text-sm text-base-content/60">
        当前角色：
        <span class="badge badge-primary badge-sm">
          {{ authStore.isAdmin ? '管理员' : authStore.isTeacher ? '教师' : '学生' }}
        </span>
      </p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="surface p-5">
        <div class="text-xs text-base-content/60 uppercase tracking-wider mb-1">科目数量</div>
        <div class="text-2xl font-semibold text-base-content font-mono">-</div>
      </div>
      <div class="surface p-5">
        <div class="text-xs text-base-content/60 uppercase tracking-wider mb-1">题目总数</div>
        <div class="text-2xl font-semibold text-base-content font-mono">-</div>
      </div>
      <div class="surface p-5">
        <div class="text-xs text-base-content/60 uppercase tracking-wider mb-1">考试场次</div>
        <div class="text-2xl font-semibold text-base-content font-mono">-</div>
      </div>
    </div>
  </div>
</template>
