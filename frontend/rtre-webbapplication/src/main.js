import Vue from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import VueRouter from 'vue-router'
import Routes from './router/routes'
import Chat from 'vue-beautiful-chat'

Vue.config.productionTip = false
Vue.use(VueRouter)
Vue.use(Chat)

const router = new VueRouter({
  routes:Routes,
  mode: 'history'
})

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (sessionStorage.getItem('TokenId') === null ) {
      next({name:'Login'})
    } else {
      next()
    }
  } else if(to.name == 'Login' && sessionStorage.getItem('TokenId') !== null) {
      next({name:'Home'})
  } else{
    next()
  }

  next()
})

new Vue({
  vuetify,
  render: h => h(App),
  router:router
}).$mount('#app')
