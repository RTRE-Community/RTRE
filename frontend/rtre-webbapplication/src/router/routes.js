import MainHub from '../views/MainHub.vue'
import BimViewer from '../views/BimViewer.vue'
import LoginPage from '../views/LoginPage.vue'
import PageNotFound from '../views/PageNotFound.vue'

export default [
{path:'/', name:'Home',component:MainHub, meta: {requiresAuth: true, showInBar: true}},
  { path: '/Viewer', name: 'Viewer', component: BimViewer, meta: {requiresAuth: true, showInBar: true}},
  { path: '/Login', name: 'Login', component: LoginPage, meta: { requiresAuth: false, showInBar: false } },
  {path: '*', name:'404', component: PageNotFound, meta: { requiresAuth: false, showInBar: false }}
]