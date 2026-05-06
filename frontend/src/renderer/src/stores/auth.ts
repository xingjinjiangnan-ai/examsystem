import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { UserProfile } from '@/types/user'
import { login, logout as apiLogout } from '@/api/modules/user'

const USER_KEY = 'examsystem_user'

function loadUserFromStorage(): UserProfile | null {
  try {
    const raw = localStorage.getItem(USER_KEY)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export const useAuthStore = defineStore('auth', () => {
  const user = ref<UserProfile | null>(loadUserFromStorage())
  const token = ref<string | null>(null)

  const isLoggedIn = computed(() => !!user.value)

  const isAdmin = computed(() => user.value?.roles.includes('SYSTEM_ADMIN') ?? false)
  const isTeacher = computed(() => user.value?.roles.includes('TEACHER') ?? false)
  const isStudent = computed(() => user.value?.roles.includes('STUDENT') ?? false)

  function setUser(profile: UserProfile) {
    user.value = profile
    localStorage.setItem(USER_KEY, JSON.stringify(profile))
  }

  async function doLogin(username: string, password: string) {
    const profile = await login({ username, password })
    user.value = profile
    localStorage.setItem(USER_KEY, JSON.stringify(profile))
    return profile
  }

  async function doLogout() {
    try {
      await apiLogout()
    } finally {
      user.value = null
      token.value = null
      localStorage.removeItem(USER_KEY)
    }
  }

  return {
    user,
    token,
    isLoggedIn,
    isAdmin,
    isTeacher,
    isStudent,
    setUser,
    doLogin,
    doLogout
  }
})
