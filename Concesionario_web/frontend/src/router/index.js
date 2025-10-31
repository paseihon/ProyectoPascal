import { createRouter, createWebHistory } from 'vue-router'
import VehiculoForm from '../components/VehiculoForm.vue'
import VehiculoLista from '../components/VehiculoLista.vue'
import VehiculoBuscar from '../components/VehiculoBuscar.vue'
import VehiculoEditar from '../components/VehiculoEditar.vue'

const routes = [
  { path: '/', redirect: '/lista' },
  { path: '/lista', component: VehiculoLista },
  { path: '/nuevo', component: VehiculoForm },
  { path: '/buscar', component: VehiculoBuscar },
  { path: '/modificar', component: VehiculoEditar },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

export default router
