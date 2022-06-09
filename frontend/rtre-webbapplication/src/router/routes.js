import MainHub from '../views/MainHub.vue'
import BimViewer from '../views/BimViewer.vue'
import LoginPage from '../views/LoginPage.vue'

export default [
{path:'/home', name:'Home',component:MainHub},
{path: '/Viewer', name:'Viewer', component:BimViewer},
{path:'/Login', name:'Login', component:LoginPage}
]