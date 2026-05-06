<script setup lang="ts">
import { ref, computed, h } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const sidebarCollapsed = ref(false)
const sidebarWidth = computed(() => sidebarCollapsed.value ? 'w-16' : 'w-60')

const navItems = [
  { name: 'Dashboard', title: '首页', path: '/dashboard', icon: HomeIcon },
  { name: 'Subjects', title: '科目管理', path: '/subjects', icon: BookIcon, adminOnly: true },
  { name: 'Questions', title: '题目管理', path: '/questions', icon: FileTextIcon }
]

const visibleNavItems = computed(() =>
  navItems.filter(item => !item.adminOnly || authStore.isAdmin)
)

function isActive(path: string) {
  return route.path === path
}

async function handleLogout() {
  await authStore.doLogout()
  router.push('/login')
}

function HomeIcon() {
  return h('svg', { class: 'w-5 h-5', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', 'stroke-width': 2 }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6' })
  ])
}

function BookIcon() {
  return h('svg', { class: 'w-5 h-5', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', 'stroke-width': 2 }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253' })
  ])
}

function FileTextIcon() {
  return h('svg', { class: 'w-5 h-5', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', 'stroke-width': 2 }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z' })
  ])
}

function MenuIcon() {
  return h('svg', { class: 'w-5 h-5', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', 'stroke-width': 2 }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M4 6h16M4 12h16M4 18h16' })
  ])
}

function UserIcon() {
  return h('svg', { class: 'w-5 h-5', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', 'stroke-width': 2 }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z' })
  ])
}

function LogoutIcon() {
  return h('svg', { class: 'w-5 h-5', fill: 'none', viewBox: '0 0 24 24', stroke: 'currentColor', 'stroke-width': 2 }, [
    h('path', { 'stroke-linecap': 'round', 'stroke-linejoin': 'round', d: 'M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1' })
  ])
}
</script>

<template>
  <div class="h-full flex">
    <!-- Sidebar -->
    <aside
      :class="['glass h-full flex flex-col transition-all duration-300', sidebarWidth]"
    >
      <!-- Logo area -->
      <div class="h-14 flex items-center px-4 border-b border-base-300/30">
        <span v-if="!sidebarCollapsed" class="text-lg font-semibold tracking-tight text-base-content">
          考试系统
        </span>
        <span v-else class="text-lg font-semibold">考</span>
      </div>

      <!-- Nav items -->
      <nav class="flex-1 py-4 space-y-1 px-2">
        <button
          v-for="item in visibleNavItems"
          :key="item.name"
          :class="[
            'w-full flex items-center gap-3 px-3 py-2.5 rounded-lg transition-colors duration-150 text-sm',
            isActive(item.path)
              ? 'bg-primary/15 text-primary font-medium'
              : 'text-base-content/70 hover:bg-base-100/40 hover:text-base-content'
          ]"
          @click="router.push(item.path)"
        >
          <component :is="item.icon" />
          <span v-if="!sidebarCollapsed">{{ item.title }}</span>
        </button>
      </nav>

      <!-- Bottom collapse toggle -->
      <div class="p-2 border-t border-base-300/30">
        <button
          class="w-full flex items-center justify-center gap-2 px-3 py-2 rounded-lg text-sm text-base-content/60 hover:bg-base-100/40 hover:text-base-content transition-colors"
          @click="sidebarCollapsed = !sidebarCollapsed"
        >
          <MenuIcon />
          <span v-if="!sidebarCollapsed">收起</span>
        </button>
      </div>
    </aside>

    <!-- Main area -->
    <div class="flex-1 flex flex-col min-w-0">
      <!-- Topbar -->
      <header class="h-14 glass flex items-center justify-between px-6 border-b border-base-300/30">
        <h1 class="text-base font-medium text-base-content">
          {{ route.meta.title || '考试系统' }}
        </h1>

        <div class="flex items-center gap-4">
          <!-- User info -->
          <div class="flex items-center gap-2 text-sm text-base-content/80">
            <UserIcon />
            <span>{{ authStore.user?.realName || authStore.user?.username }}</span>
            <span
              v-if="authStore.user?.roles?.length"
              class="badge badge-sm badge-primary"
            >
              {{ authStore.user.roles[0] === 'SYSTEM_ADMIN' ? '管理员' : authStore.user.roles[0] === 'TEACHER' ? '教师' : '学生' }}
            </span>
          </div>

          <!-- Logout -->
          <button
            class="btn btn-circle btn-ghost btn-sm text-base-content/60 hover:text-error"
            title="退出登录"
            @click="handleLogout"
          >
            <LogoutIcon />
          </button>
        </div>
      </header>

      <!-- Content -->
      <main class="flex-1 overflow-auto p-6">
        <Transition name="fade" mode="out-in">
          <RouterView />
        </Transition>
      </main>
    </div>
  </div>
</template>
