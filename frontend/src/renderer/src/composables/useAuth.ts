import { useAuthStore } from '@/stores/auth'
import { storeToRefs } from 'pinia'

export function useAuth() {
  const store = useAuthStore()
  const { user, isLoggedIn, isAdmin, isTeacher, isStudent } = storeToRefs(store)

  return {
    user,
    isLoggedIn,
    isAdmin,
    isTeacher,
    isStudent,
    doLogin: store.doLogin,
    doLogout: store.doLogout,
    setUser: store.setUser
  }
}
