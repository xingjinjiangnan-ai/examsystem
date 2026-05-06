import { createRouter, createWebHashHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import LoginView from '@/views/LoginView.vue'
import DashboardLayout from '@/layouts/DashboardLayout.vue'
import DashboardView from '@/views/DashboardView.vue'
import SubjectView from '@/views/SubjectView.vue'
import QuestionView from '@/views/QuestionView.vue'
import NotFoundView from '@/views/NotFoundView.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
    meta: { public: true }
  },
  {
    path: '/',
    component: DashboardLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: DashboardView,
        meta: { title: '首页', icon: 'HomeIcon' }
      },
      {
        path: 'subjects',
        name: 'Subjects',
        component: SubjectView,
        meta: { title: '科目管理', icon: 'BookOpenIcon', requireAdmin: true }
      },
      {
        path: 'questions',
        name: 'Questions',
        component: QuestionView,
        meta: { title: '题目管理', icon: 'DocumentTextIcon' }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    component: NotFoundView
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, _from, next) => {
  const authStore = useAuthStore()

  if (!to.meta.public && !authStore.isLoggedIn) {
    next('/login')
    return
  }

  if (to.meta.requireAdmin && !authStore.isAdmin) {
    next('/dashboard')
    return
  }

  next()
})

export default router
